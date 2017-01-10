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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.metadata;

import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.metadata.interfaces.AbstractMetaDataStore;
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
import java.util.*;

import static com.spectralogic.ds3client.utils.MetadataKeyConstants.*;


public class WindowsMetaDataStore extends AbstractMetaDataStore {


    public WindowsMetaDataStore(final ImmutableMap.Builder<String, String> metadataMap) {
        this.mMetadataMap = metadataMap;
    }

    /**
     * save owner sid , group sid and dacl for windows
     *
     * @param path local path of the file
     */
    public void saveWindowsDescriptors(final Path path) {
        final  int infoType = WinNT.OWNER_SECURITY_INFORMATION
                | WinNT.GROUP_SECURITY_INFORMATION
                | WinNT.DACL_SECURITY_INFORMATION | 0;

        final PointerByReference ppsidOwner = new PointerByReference();
        final PointerByReference ppsidGroup = new PointerByReference();
        final PointerByReference ppDacl = new PointerByReference();
        final PointerByReference ppSecurityDescriptor = new PointerByReference();

        final File file = path.toFile();
        try {
            final int bool = Advapi32.INSTANCE.GetNamedSecurityInfo(
                    file.getAbsolutePath(),
                    1,
                    infoType,
                    ppsidOwner,
                    ppsidGroup,
                    ppDacl,
                    null,
                    ppSecurityDescriptor);
            if (bool == 0) {
                final WinNT.PSID psidOwner = new WinNT.PSID(ppsidOwner.getValue().getByteArray(0, 256));
                final String ownerSid = psidOwner.getSidString();

                final WinNT.PSID psidGroup = new WinNT.PSID(ppsidGroup.getValue().getByteArray(0, 256));
                final String groupSid = psidGroup.getSidString();

                mMetadataMap.put(METADATA_PREFIX + KEY_GROUP, groupSid);
                mMetadataMap.put(METADATA_PREFIX + KEY_OWNER, ownerSid);

                final WinNT.ACL acl = new WinNT.ACL(ppDacl.getValue());
                final String daclString = getDaclString(acl);
                mMetadataMap.put(METADATA_PREFIX + KEY_DACL, daclString);
            }
        } catch (final Exception e) {
            LOG.error("Unable to get sid of user and owner", e);
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
    public String saveFlagMetaData(final Path file) {
        final StringBuilder flagBuilder = new StringBuilder();
        try {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("attrib  ");
            stringBuilder.append("\""+file+"\"");

            final ProcessBuilder processBuilder = new ProcessBuilder("attrib",file.toString());
            final  Process process =  processBuilder.start();
            final BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            final String flagWindows =  reader.readLine();
            final String[] flags = flagWindows.split(" ");

            for (int i = 0; i < flags.length - 1; i++) {
                if(!flags[i].equals("")) {
                    if (flags[i].contains("\\")) {
                        break;
                    } else {
                        flagBuilder.append(flags[i].trim());
                    }
                }
            }
            if(flagBuilder.toString().equals("")){
                flagBuilder.append("N");
            }
            mMetadataMap.put(METADATA_PREFIX + KEY_FLAGS, flagBuilder.toString());
        } catch (final IOException ioe) {
            LOG.error("Unable to read file", ioe);
        } catch (final Exception e) {
            LOG.error("Unable to fetch attributes of file", e);
        }
        return flagBuilder.toString();
    }

    /**
     * if os is windows then posix will not be called and we need to find permission in different manner
     *
     * @param file local file path
     */
    public void saveWindowsfilePermissions(final Path file) {
        try {
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
                // permission = new StringBuilder();
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
                mMetadataMap.put("x-amz-meta-ds3-" + userType, permission.toString());
                userCount++;
            }
            mMetadataMap.put("x-amz-meta-ds3-userList", userList.toString());
            mMetadataMap.put("x-amz-meta-ds3-userListDisplay", userDisplayList.toString());
        } catch (final Exception e) {
            LOG.error("Unable to get list of users or their permissions", e);
        }
    }



}
