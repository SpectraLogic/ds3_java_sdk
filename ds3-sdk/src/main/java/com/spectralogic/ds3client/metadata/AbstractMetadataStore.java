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
import com.spectralogic.ds3client.metadata.interfaces.MetadataStore;
import com.spectralogic.ds3client.metadata.interfaces.MetadataStoreListener;

import java.nio.file.attribute.BasicFileAttributes;

import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_ACCESS_TIME;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_CREATION_TIME;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_LAST_MODIFIED_TIME;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.KEY_OS;
import static com.spectralogic.ds3client.metadata.MetadataKeyConstants.METADATA_PREFIX;

abstract class AbstractMetadataStore implements MetadataStore {


    protected ImmutableMap.Builder<String, String> mMetadataMap;

    protected MetadataStoreListener metadataStoreListener;


    @Override
    public void saveCreationTimeMetaData(final BasicFileAttributes attr) {
        mMetadataMap.put(METADATA_PREFIX + KEY_CREATION_TIME, String.valueOf(attr.creationTime().toMillis()));
    }

    @Override
    public void saveAccessTimeMetaData(final BasicFileAttributes attr) {
        mMetadataMap.put(METADATA_PREFIX + KEY_ACCESS_TIME, String.valueOf(attr.lastAccessTime().toMillis()));

    }

    @Override
    public void saveLastModifiedTime(final BasicFileAttributes attr) {
        mMetadataMap.put(METADATA_PREFIX + KEY_LAST_MODIFIED_TIME, String.valueOf(attr.lastModifiedTime().toMillis()));

    }


    @Override
    public String saveOSMetaData(final String osName) {
        mMetadataMap.put(METADATA_PREFIX + KEY_OS, osName);
        return osName;
    }
}
