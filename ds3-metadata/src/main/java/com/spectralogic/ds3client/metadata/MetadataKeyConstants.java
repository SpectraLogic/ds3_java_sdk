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

final class MetadataKeyConstants {
    private MetadataKeyConstants() {}

    //Every metadata must start from x-amz
    static final String METADATA_PREFIX = "x-amz-meta-";
    //creation time key
    static final String KEY_CREATION_TIME = "ds3-creation-time";
    //access time key
    static final String KEY_ACCESS_TIME = "ds3-last-access-time";
    //modified time key
    static final String KEY_LAST_MODIFIED_TIME = "ds3-last-modified-time";
    //owner sid for windows
    static final String KEY_OWNER = "ds3-owner";
    //group sid for windows
    static final String KEY_GROUP = "ds3-group";
    //user id of a file linux
    static final String KEY_UID = "ds3-uid";
    //group id for linux
    static final String KEY_GID = "ds3-gid";
    //mode for linux
    static final String KEY_MODE = "ds3-mode";
    //control flag
    static final String KEY_FLAGS = "ds3-flags";
    //dacl String for windows
    static final String KEY_DACL = "ds3-dacl";
    //os
    static final String KEY_OS = "ds3-os";
    //permissions
    static final String KEY_PERMISSION = "ds3-permissions";
    //owner name
    static final String KEY_OWNER_NAME = "ds3-ownerName";
    //group Name
    static final String KEY_GROUP_NAME = "ds3-groupName";
}
