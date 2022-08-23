/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.Guard;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.WinNT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_FLAGS;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_GROUP;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_OWNER;

class WindowsMetadataRestore extends AbstractMetadataRestore {

    private static final Logger LOG = LoggerFactory.getLogger(WindowsMetadataRestore.class);

    WindowsMetadataRestore(final Metadata metadata, final String filePath, final String localOS) {
        this.metadata = metadata;
        this.objectName = filePath;
        this.localOS = localOS;
    }

    @Override
    public void restoreUserAndOwner() throws IOException {
        try {
            if (storedOS != null && storedOS.equals(localOS)) {

                final String ownerSid = getMetadataProperty(metadata, KEY_OWNER);
                final String groupSid = getMetadataProperty(metadata, KEY_GROUP);

                if (!Guard.isStringNullOrEmpty(ownerSid) && !Guard.isStringNullOrEmpty(groupSid)) {
                    setOwnerIdAndGroupId(ownerSid, groupSid);
                } else {
                    LOG.warn("Cannot determine owner or group settings for {}", this.objectName);
                }
            } else {
                LOG.warn("The OS settings for owner and group properties cannot be restored for {}", this.objectName);
            }
        } catch (final Throwable t) {
            throw new WindowsMetadataException("Restoring user and owner.", t);
        }
    }

    private static String getMetadataProperty(final Metadata metadata, final String metadataName) {
        return metadata.get(metadataName).get(0);
    }

    @Override
    public void restorePermissions() throws IOException, InterruptedException {
        try {
            if (storedOS != null && storedOS.equals(localOS)) {
                setPermissionsForWindows();
            } else {
                LOG.warn("The OS settings for the file permissions cannot be restored for {}", this.objectName);
            }
            restoreFlags();
        } catch (final Throwable t) {
            throw new WindowsMetadataException("Restoring permissions.", t);
        }
    }

    private void setPermissionsForWindows() throws IOException {
        final Path path = Paths.get(objectName);
        final AclFileAttributeView aclAttributeView = Files.getFileAttributeView(path, AclFileAttributeView.class);
        final ImmutableList.Builder<AclEntry> aclEntryBuilder = new ImmutableList.Builder<>();
        final String userList;
        final String userListDisplay;
        final String[] users;
        final String[] usersDisplay;
        if (Guard.isNotNullAndNotEmpty(metadata.get("ds3-userList"))) {
            userList = metadata.get("ds3-userList").get(0);
            if (Guard.isNotNullAndNotEmpty(metadata.get("ds3-userListDisplay"))) {
                userListDisplay = metadata.get("ds3-userListDisplay").get(0);
                users = userList.split("-");
                usersDisplay = userListDisplay.split("-");
                for (int i = 0; i < users.length; i++) {
                    final String user = users[i];
                    if (Guard.isNotNullAndNotEmpty(metadata.get("ds3-" + user))) {
                        final String ownerPermission = metadata.get("ds3-" + user).get(0);
                        restorePermissionByUser(ownerPermission, usersDisplay[i], aclEntryBuilder);
                    } else {
                        LOG.warn("Did not find any permissions for {} for file {}", user, this.objectName);
                    }
                }
            } else {
                LOG.warn("There was not a 'userListDisplay' metadata entry for file {}, so we will not restore any permissions", this.objectName);
            }
        } else {
            LOG.warn("There was not a 'userList' metadata entry for file {}, so we will not restore any permissions", this.objectName);
        }

        aclAttributeView.setAcl(aclEntryBuilder.build());
    }


    /**
     *
     * default map for ordinal permissions
     * @return map
     */

    private Map<String,AclEntryPermission> defaultOrdinalPermission(){
        final Map<String,AclEntryPermission> defaultOrdinalMap = new HashMap<>();
        defaultOrdinalMap.put("0",AclEntryPermission.READ_DATA);
        defaultOrdinalMap.put("1",AclEntryPermission.WRITE_DATA);
        defaultOrdinalMap.put("2",AclEntryPermission.APPEND_DATA);
        defaultOrdinalMap.put("3",AclEntryPermission.READ_NAMED_ATTRS);
        defaultOrdinalMap.put("4",AclEntryPermission.WRITE_NAMED_ATTRS);
        defaultOrdinalMap.put("5",AclEntryPermission.EXECUTE);
        defaultOrdinalMap.put("6",AclEntryPermission.DELETE_CHILD);
        defaultOrdinalMap.put("7",AclEntryPermission.READ_ATTRIBUTES);
        defaultOrdinalMap.put("8",AclEntryPermission.WRITE_ATTRIBUTES);
        defaultOrdinalMap.put("9",AclEntryPermission.DELETE);
        defaultOrdinalMap.put("10",AclEntryPermission.READ_ACL);
        defaultOrdinalMap.put("11",AclEntryPermission.WRITE_ACL);
        defaultOrdinalMap.put("12",AclEntryPermission.WRITE_OWNER);
        defaultOrdinalMap.put("13",AclEntryPermission.SYNCHRONIZE);
        return  defaultOrdinalMap;
    }

    private void restorePermissionByUser(final String permission,
                                         final String user,
                                         final ImmutableList.Builder<AclEntry> aclEntryBuilder)
        throws IOException
    {
        final AclEntry.Builder builderWindow = AclEntry.newBuilder();
        final UserPrincipal userPrinciple = FileSystems.getDefault()
                .getUserPrincipalLookupService().lookupPrincipalByName(user);
        final Set<AclEntryPermission> permissions = new HashSet<>();
        final String[] ordinalArr = permission.split("-");
        final Map<String,AclEntryPermission> defaultPermssionMap = defaultOrdinalPermission();

        for (final String ordinal : ordinalArr) {
          permissions.add(defaultPermssionMap.get(ordinal));
        }
        builderWindow.setPrincipal(userPrinciple);
        builderWindow.setPermissions(permissions);
        builderWindow.setType(AclEntryType.ALLOW);
        final AclEntry aclEntry = builderWindow.build();
        aclEntryBuilder.add(aclEntry);
    }

    /**
     * set the owner and group on the file by using owner and group sid
     *
     * @param ownerSidId sid of the owner
     * @param groupSidId sid of the group
     */
    private void setOwnerIdAndGroupId(final String ownerSidId, final String groupSidId) throws IOException {
        final int infoType = WinNT.OWNER_SECURITY_INFORMATION | WinNT.GROUP_SECURITY_INFORMATION;
        final WinNT.PSIDByReference referenceOwner = new WinNT.PSIDByReference();
        Advapi32.INSTANCE.ConvertStringSidToSid(ownerSidId, referenceOwner);
        final WinNT.PSIDByReference referenceGroup = new WinNT.PSIDByReference();
        Advapi32.INSTANCE.ConvertStringSidToSid(groupSidId, referenceGroup);
        final File file = new File(objectName);
        final int winApiResult = Advapi32.INSTANCE.SetNamedSecurityInfo(file.getAbsolutePath(), 1, infoType, referenceOwner.getValue().getPointer(), referenceGroup.getValue().getPointer(), null, null);
        if (winApiResult != 0) {
            throw new IOException("Failure setting owner and group on file: " + objectName + ".  Windows error code: " + winApiResult);
        }
    }

    private void restoreFlags()  throws IOException, InterruptedException {
        if (storedOS != null && storedOS.equals(localOS)) {
            if (metadata.get(KEY_FLAGS).size() > 0) {
                final String flags = metadata.get(KEY_FLAGS).get(0);
                restoreFlagsWindows(flags);
            } else {
                LOG.warn("The file flag settings do not exist for file {} and will not be restored", this.objectName);
            }
        } else {
            LOG.warn("The OS settings for restoring the file flags cannot be done for {}", this.objectName);
        }
    }

    /**
     * restore  the attributes of windows
     *
     * @param flag : flag retrieved from blackperl
     */
    private void restoreFlagsWindows(final String flag) throws IOException, InterruptedException {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("attrib");
        if (flag.contains("A")) {
            stringBuilder.append(" +A");
        }
        if (flag.contains("R")) {
            stringBuilder.append(" +R");
        }
        if (flag.contains("H")) {
            stringBuilder.append(" +H");
        }
        if (flag.contains("S")) {
            stringBuilder.append(" +S");
        }
        if (flag.contains("I")) {
            stringBuilder.append(" +I");
        }
        if (flag.contains("N")) {
            stringBuilder.append(" -A");
            stringBuilder.append(" -R");
            stringBuilder.append(" -I");
            stringBuilder.append(" -H");
        }
        stringBuilder.append(" " + "\"").append(objectName).append("\"");

        final Process p = Runtime.getRuntime().exec(stringBuilder.toString().split(" "));
        final int returnCode = p.waitFor();
        if (returnCode != 0) {
            LOG.error("Restoring the flag settings for file {} was not successful", this.objectName);
        }
    }
}
