package com.spectralogic.ds3client.utils;

/**
 * Created by linchpin on 14/12/16.
 */
public interface MetadataKeyConstants {
    //Every metadata must start from x-amz
    public static final String METADATA_PREFIX = "x-amz-meta-";
    //creation time key
    public static final String KEY_CREATION_TIME = "ds3-creation-time";
    //access time key
    public static final String KEY_ACCESS_TIME = "ds3-last-access-time";
    //modified time key
    public static final String KEY_LAST_MODIFIED_TIME = "ds3-last-modified-time";
    //owner sid for windows
    public static final String KEY_OWNER = "ds3-owner";
    //group sid for windows
    public static final String KEY_GROUP = "ds3-group";
    //user id of a file linux
    public static final String KEY_UID = "ds3-uid";
    //group id for linux
    public static final String KEY_GID = "ds3-gid";
    //mode for linux
    public static final String KEY_MODE = "ds3-mode";
    //control flag
    public static final String KEY_FLAGS = "ds3-flags";
    //dacl String for windows
    public static final String KEY_DACL = "ds3-dacl";
    //os
    public static final String KEY_OS = "ds3-os";
    //permissions
    public static final String KEY_PERMISSION = "ds3-permissions";
    //owner name
    public static final String KEY_OWNER_NAME = "ds3-ownerName";
    //group Name
    public static final String KEY_GROUP_NAME = "ds3-groupName";
}
