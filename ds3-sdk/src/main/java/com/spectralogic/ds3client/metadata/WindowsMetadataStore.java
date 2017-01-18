/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3client.metadata;

import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.metadata.jna.Advapi32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_DACL;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_FLAGS;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_GROUP;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_OWNER;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.METADATA_PREFIX;

class WindowsMetadataStore extends AbstractMetadataStore {
    private static final Logger LOG = LoggerFactory.getLogger(WindowsMetadataStore.class);

    public WindowsMetadataStore(final ImmutableMap.Builder<String, String> metadataMap) {
        this.metadataMap = metadataMap;
    }

    /**
     * save owner sid , group sid and dacl for windows
     *
     * @param path local path of the file
     */
    private void saveWindowsDescriptors(final Path path) throws IOException {
        final int infoType = WinNT.OWNER_SECURITY_INFORMATION
                | WinNT.GROUP_SECURITY_INFORMATION
                | WinNT.DACL_SECURITY_INFORMATION;
        final PointerByReference ppsidOwner = new PointerByReference();
        final PointerByReference ppsidGroup = new PointerByReference();
        final PointerByReference ppDacl = new PointerByReference();
        final PointerByReference ppSecurityDescriptor = new PointerByReference();
        final File file = path.toFile();

    final int winApiResult = Advapi32.INSTANCE.GetNamedSecurityInfo(
                file.getAbsolutePath(),
                1,
                infoType,
                ppsidOwner,
                ppsidGroup,
                ppDacl,
                null,
                ppSecurityDescriptor);
        if (winApiResult == 0) {
            final WinNT.PSID psidOwner = new WinNT.PSID(ppsidOwner.getValue());
            final String ownerSid = psidOwner.getSidString();
            final WinNT.PSID psidGroup = new WinNT.PSID(ppsidGroup.getValue());
            final String groupSid = psidGroup.getSidString();
            metadataMap.put(METADATA_PREFIX + KEY_GROUP, groupSid);
            metadataMap.put(METADATA_PREFIX + KEY_OWNER, ownerSid);
            final WinNT.ACL acl = new WinNT.ACL(ppDacl.getValue());
            final String daclString = getDaclString(acl);
            metadataMap.put(METADATA_PREFIX + KEY_DACL, daclString);
        } else {
            throw new IOException("GetNamedSecurityInfo returned error code: " + winApiResult);
        }
    }

    /**
     * Get the dacl string from acl
     *
     * @param acl acl got from jna
     * @return dacl string
     */
    private String getDaclString(final WinNT.ACL acl) {
        final WinNT.ACCESS_ACEStructure[] aceStructures = acl.getACEStructures();
        String daclString = "";
        for (int i = 0; i < aceStructures.length; i++) {
            daclString = daclString + "(";
            final WinNT.ACCESS_ACEStructure aceStructure = aceStructures[i];
            if (aceStructure.AceType == 0) {
                daclString = daclString + "A;";
            } else if (aceStructure.AceType == 1) {
                daclString = daclString + "D;";
            } else if (aceStructure.AceType == 2) {
                daclString = daclString + "AU;";
            }
            daclString = daclString + "0x" + Integer.toHexString(aceStructure.AceFlags) + ";";
            daclString = daclString + "0x" + Integer.toHexString(aceStructure.Mask) + ";;;";
            daclString = daclString + (aceStructure.getSidString()) + ")";
        }
        return daclString;
    }

    /**
     * save flag of file in windows
     *
     * @param file local file of the path
     * @return flag of file in String
     */
    private String saveFlagMetaData(final Path file) throws IOException {
        final StringBuilder flagBuilder = new StringBuilder();

            final ProcessBuilder processBuilder = new ProcessBuilder("attrib", file.toString());
            final Process process = processBuilder.start();
            final BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            final String flagWindows = reader.readLine();
            final String[] flags = flagWindows.split(" ");
            for (int i = 0; i < flags.length - 1; i++) {
                if (!flags[i].equals("")) {
                    if (flags[i].contains("\\")) {
                        break;
                    } else {
                        flagBuilder.append(flags[i].trim());
                    }
                }
            }
            if (flagBuilder.toString().equals("")) {
                flagBuilder.append("N");
            }
            metadataMap.put(METADATA_PREFIX + KEY_FLAGS, flagBuilder.toString());

        return flagBuilder.toString();
    }

    /**
     * if os is windows then posix will not be called and we need to find permission in different manner
     *
     * @param file local file path
     */
    private void saveWindowsfilePermissions(final Path file) throws IOException {

            Set<AclEntryPermission> aclEntryPermissions;
            String userType;
            String userDisplay;
            StringBuilder permission;
            final AclFileAttributeView view = Files.getFileAttributeView(file, AclFileAttributeView.class);
            final List<AclEntry> aclEntries = view.getAcl();
            final StringBuilder userList = new StringBuilder();
            final StringBuilder userDisplayList = new StringBuilder();
            final Map<String, Set<Integer>> stringSetMap = new HashMap<>();
            for (final AclEntry aclEntry : aclEntries) {
                userDisplay = aclEntry.principal().getName().split("\\\\")[1];
                Set<Integer> newSet = stringSetMap.get(userDisplay);
                aclEntryPermissions = aclEntry.permissions();
                if (newSet == null) {
                    newSet = new HashSet<>();
                }
                for (final AclEntryPermission aclEntryPermission : aclEntryPermissions) {
                    newSet.add(aclEntryPermission.ordinal());
                }
                stringSetMap.put(userDisplay, newSet);
            }
            final Set<String> keys = stringSetMap.keySet();
            Set<Integer> ordinals;
            int userCount = 1;
            for (final String key : keys) {
                int index = 1;
                ordinals = stringSetMap.get(key);
                userType = key.replaceAll(" ", "").toLowerCase();
                permission = new StringBuilder();
                for (final int ord : ordinals) {
                    if (ordinals.size() == index) {
                        permission.append(ord);
                    } else {
                        permission.append(ord + "-");
                    }
                    index++;
                }
                if (keys.size() == userCount) {
                    userDisplayList.append(key);
                    userList.append(userType);
                } else {
                    userDisplayList.append(key + "-");
                    userList.append(userType + "-");
                }
                metadataMap.put("x-amz-meta-ds3-" + userType, permission.toString());
                userCount++;
            }
            metadataMap.put("x-amz-meta-ds3-userList", userList.toString());
            metadataMap.put("x-amz-meta-ds3-userListDisplay", userDisplayList.toString());
    }

    @Override
    public void saveOSSpecificMetadata(final Path file, final BasicFileAttributes attrs) throws IOException {
        saveWindowsfilePermissions(file);
        saveWindowsDescriptors(file);
        saveFlagMetaData(file);
    }
}
