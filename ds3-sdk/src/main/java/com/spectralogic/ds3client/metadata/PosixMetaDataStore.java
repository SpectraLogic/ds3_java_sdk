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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;

import static com.spectralogic.ds3client.utils.MetadataKeyConstants.*;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;


public class PosixMetaDataStore extends AbstractMetaDataStore {


    //user id of a file linux/mac
    private String mUid;

    //group id for linux/mac
    private String mGid;


    //owner name
    private String mOwnerName;

    //group name
    private String mGroupName;

    //permissions
    private String mPermissions;


    public PosixMetaDataStore(final ImmutableMap.Builder<String, String> metadataMap) {
        this.mMetadataMap = metadataMap;
    }

    /**
     * @param file path of file
     * @return user id of file Owner inLinux
     * @throws IOException throws IO exception if file is not present
     */
    public String saveUserId(final Path file) throws IOException {
        final int uid = (int) Files.getAttribute(file, "unix:uid", NOFOLLOW_LINKS);
        mMetadataMap.put(METADATA_PREFIX + KEY_UID, String.valueOf(uid));
        setmUid(String.valueOf(uid));
        return String.valueOf(uid);
    }


    /**
     * @param file path of file
     * @return group id of file Owner inLinux
     * @throws IOException
     */
    public String saveGroupId(final Path file) throws IOException {
        final int gid = (int) Files.getAttribute(file, "unix:gid", NOFOLLOW_LINKS);
        mMetadataMap.put(METADATA_PREFIX + KEY_GID, String.valueOf(gid));
        setmGid(String.valueOf(gid));
        return String.valueOf(gid);
    }

    /**
     * @param file path of file
     * @return group id of file Owner inLinux
     * @throws IOException
     */

    public String saveModeMetaData(final Path file) throws IOException {
        final int mode = (int) Files.getAttribute(file, "unix:mode", NOFOLLOW_LINKS);
        final String modeOctal = Integer.toOctalString(mode);
        mMetadataMap.put(METADATA_PREFIX + KEY_MODE, modeOctal);
        return modeOctal;
    }

    /**
     * @param attr PosixFileAttributes to get posix info
     * @return owner name of the file
     */
    public String saveOwnerNameMetaData(final PosixFileAttributes attr) {
        final UserPrincipal owner = attr.owner();
        final String ownerName = owner.getName();
        mMetadataMap.put(METADATA_PREFIX + KEY_OWNER_NAME, ownerName);
        setmOwnerName(ownerName);
        return ownerName;
    }


    /**
     * @param attr PosixFileAttributes to get posix info
     * @return group name of the file owner
     */
    public String saveGroupNameMetaData(final PosixFileAttributes attr) {
        final GroupPrincipal group = attr.group();
        final String groupName = group.getName();
        mMetadataMap.put(METADATA_PREFIX + KEY_GROUP_NAME, groupName);
        setmGroupName(groupName);
        return groupName;
    }

    /**
     * @param attr PosixFileAttributes to get posix info
     * @return file permissions in octal
     */
    public String savePosixPermssionsMeta(final PosixFileAttributes attr) {
        final String permissionsOctal = getPermissionInOctal(PosixFilePermissions.toString(attr.permissions()));
        mMetadataMap.put(METADATA_PREFIX + KEY_PERMISSION, permissionsOctal);
        setmPermissions(permissionsOctal);
        return permissionsOctal;
    }


    // get the octal number for the permission
    private String getPermissionInOctal(String permissions) {
        final String permString = new String(permissions);
        permissions = permissions.replaceAll("r", "4");
        permissions = permissions.replaceAll("w", "2");
        permissions = permissions.replaceAll("x", "1");
        permissions = permissions.replaceAll("-", "0");
        final String ownerPerm = String.valueOf(Integer.parseInt(String.valueOf(permissions.charAt(0))) + Integer.parseInt(String.valueOf(permissions.charAt(1))) + Integer.parseInt(String.valueOf(permissions.charAt(2))));
        final String groupPerm = String.valueOf(Integer.parseInt(String.valueOf(permissions.charAt(3))) + Integer.parseInt(String.valueOf(permissions.charAt(4))) + Integer.parseInt(String.valueOf(permissions.charAt(5))));
        final String otherPerm = String.valueOf(Integer.parseInt(String.valueOf(permissions.charAt(6))) + Integer.parseInt(String.valueOf(permissions.charAt(7))) + Integer.parseInt(String.valueOf(permissions.charAt(8))));
        final String totalPerm = ownerPerm + groupPerm + otherPerm;
        return totalPerm + "(" + permString + ")";
    }


    public String getmUid() {
        return mUid;
    }

    public void setmUid(final String mUid) {
        this.mUid = mUid;
    }

    public String getmGid() {
        return mGid;
    }

    public void setmGid(final String mGid) {
        this.mGid = mGid;
    }

    public String getmOwnerName() {
        return mOwnerName;
    }

    public void setmOwnerName(final String mOwnerName) {
        this.mOwnerName = mOwnerName;
    }

    public String getmGroupName() {
        return mGroupName;
    }

    public void setmGroupName(final String mGroupName) {
        this.mGroupName = mGroupName;
    }

    public String getmPermissions() {
        return mPermissions;
    }

    public void setmPermissions(final String mPermissions) {
        this.mPermissions = mPermissions;
    }

}
