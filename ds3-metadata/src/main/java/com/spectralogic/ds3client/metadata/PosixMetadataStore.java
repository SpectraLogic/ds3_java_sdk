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

import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.*;

import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_GID;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_GROUP_NAME;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_MODE;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_OWNER_NAME;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_PERMISSION;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_UID;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.METADATA_PREFIX;

import static com.spectralogic.ds3client.metadata.PermissionsUtils.getPermissionInOctal;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

class PosixMetadataStore extends AbstractMetadataStore {
    public PosixMetadataStore(final ImmutableMap.Builder<String, String> metadataMap) {
        this.metadataMap = metadataMap;
    }

    /**
     * @param file path of file
     * @return user id of file Owner inLinux
     * @throws IOException throws IO exception if file is not present
     */
    private String saveUserId(final Path file) throws IOException {
        final int uid = (int) Files.getAttribute(file, "unix:uid", NOFOLLOW_LINKS);
        metadataMap.put(METADATA_PREFIX + KEY_UID, String.valueOf(uid));
        return String.valueOf(uid);
    }


    /**
     * @param file path of file
     * @return group id of file Owner inLinux
     * @throws IOException
     */
    private String saveGroupId(final Path file) throws IOException {
        final int gid = (int) Files.getAttribute(file, "unix:gid", NOFOLLOW_LINKS);
        metadataMap.put(METADATA_PREFIX + KEY_GID, String.valueOf(gid));
        return String.valueOf(gid);
    }

    /**
     * @param file path of file
     * @return group id of file Owner inLinux
     * @throws IOException
     */

    private String saveModeMetaData(final Path file) throws IOException {
        final int mode = (int) Files.getAttribute(file, "unix:mode", NOFOLLOW_LINKS);
        final String modeOctal = Integer.toOctalString(mode);
        metadataMap.put(METADATA_PREFIX + KEY_MODE, modeOctal);
        return modeOctal;
    }

    /**
     * @param attr PosixFileAttributes to get posix info
     * @return owner name of the file
     */
    private String saveOwnerNameMetaData(final PosixFileAttributes attr) {
        final UserPrincipal owner = attr.owner();
        final String ownerName = owner.getName();
        metadataMap.put(METADATA_PREFIX + KEY_OWNER_NAME, ownerName);
        return ownerName;
    }


    /**
     * @param attr PosixFileAttributes to get posix info
     * @return group name of the file owner
     */
    private String saveGroupNameMetaData(final PosixFileAttributes attr) {
        final GroupPrincipal group = attr.group();
        final String groupName = group.getName();
        metadataMap.put(METADATA_PREFIX + KEY_GROUP_NAME, groupName);
        return groupName;
    }

    /**
     * @param attr PosixFileAttributes to get posix info
     * @return file permissions in octal
     */
    private String savePosixPermissionsMeta(final PosixFileAttributes attr) {
        final String permissionsOctal = getPermissionInOctal(PosixFilePermissions.toString(attr.permissions()));
        metadataMap.put(METADATA_PREFIX + KEY_PERMISSION, permissionsOctal);

        return permissionsOctal;
    }


    @Override
    public void saveOSSpecificMetadata(final Path file,final BasicFileAttributes attrs) throws IOException {
        saveUserId(file);
        saveGroupId(file);
        saveModeMetaData(file);
        saveOwnerNameMetaData((PosixFileAttributes) attrs);
        saveGroupNameMetaData((PosixFileAttributes) attrs);
        savePosixPermissionsMeta((PosixFileAttributes) attrs);
    }
}
