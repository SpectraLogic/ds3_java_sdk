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

import com.spectralogic.ds3client.metadata.interfaces.AbstractMetadataRestore;
import com.spectralogic.ds3client.metadata.interfaces.MetaDataRestoreListener;
import com.spectralogic.ds3client.networking.Metadata;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.spectralogic.ds3client.utils.MetadataKeyConstants.*;

public class PosixMetaDataRestore extends AbstractMetadataRestore {

    public PosixMetaDataRestore(final Metadata metadata, final String filePath, final String localOS, final MetaDataRestoreListener metaDataRestoreListener) {
        this.metadata = metadata;
        this.objectName = filePath;
        this.localOS = localOS;
        this.metaDataRestoreListener = metaDataRestoreListener;
    }


    @Override
    public void restoreUserAndOwner() {
        String ownerId = null;
        if (metadata.get(KEY_UID).size() > 0) {
            ownerId = metadata.get(KEY_UID).get(0);
        }
        String groupId = null;
        if (metadata.get(KEY_GID).size() > 0) {
            groupId = metadata.get(KEY_GID).get(0);
        }
        if (ownerId != null && groupId != null && !ownerId.equals("") && !groupId.equals("")) {
            setOwnerNGroupLnx(ownerId, groupId);
        }
    }

    @Override
    public void restorePermissions() {
        if (metadata.get(KEY_PERMISSION).size() > 0) {
            String permissions = metadata.get(KEY_PERMISSION).get(0);
            if (permissions != null && !permissions.equals("")) {
                final Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(permissions);
                while (m.find()) {
                    permissions = (m.group(1));
                }
                setPermissionsLnx(objectName, permissions);
            }
        }
    }


    /**
     * set owner and group name on local from the black perl server in case of linux
     *
     * @param ownerName name of the owner got from server
     * @param groupName name of the group got from server
     */
    private void setOwnerNGroupLnx(final String ownerName, final String groupName) {
        try {
            final Path file = Paths.get(objectName);
            final UserPrincipal owner = file.getFileSystem().getUserPrincipalLookupService()
                    .lookupPrincipalByName(ownerName);
            Files.setOwner(file, owner);
            final GroupPrincipal group =
                    file.getFileSystem().getUserPrincipalLookupService()
                            .lookupPrincipalByGroupName(groupName);
            Files.getFileAttributeView(file, PosixFileAttributeView.class)
                    .setGroup(group);
        } catch (final Exception e) {
            LOG.error("Unable to set owner and group name", e);
            metaDataRestoreListener.metadataRestoreFailed("Unable to set owner and group name::"+e.getMessage());
        }
    }

    /**
     * restore the linux permissions
     *
     * @param filePath    path of the file
     * @param permissions permissions got from the blackperl server
     */
    private void setPermissionsLnx(final String filePath, final String permissions) {
        try {
            final Path file = Paths.get(filePath);
            final Set<PosixFilePermission> perms =
                    PosixFilePermissions.fromString(permissions);
            Files.setPosixFilePermissions(file, perms);
        } catch (final Exception e) {
            LOG.error("Unable to restore Permissions", e);
            metaDataRestoreListener.metadataRestoreFailed("Unable to restore Permissions::"+e.getMessage());
        }
    }
}
