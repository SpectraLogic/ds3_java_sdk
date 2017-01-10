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
package com.spectralogic.ds3client.metadata.interfaces;

import com.google.common.collect.ImmutableMap;

import java.nio.file.attribute.BasicFileAttributes;

import static com.spectralogic.ds3client.utils.MetadataKeyConstants.*;

public class AbstractMetaDataStore implements MetaDataStore {

    protected ImmutableMap.Builder<String, String> mMetadataMap;

    //creation time key
    private String mCreationTime;

    //access time key
    private String mAccessTime;


    //last modified time key
    private String mlastModified;

    @Override
    public String saveCreationTimeMetaData(final BasicFileAttributes attr) {
        mCreationTime = String.valueOf(attr.creationTime().toMillis());
        mMetadataMap.put(METADATA_PREFIX + KEY_CREATION_TIME, mCreationTime);
        setmCreationTime(mCreationTime);
        return mCreationTime;
    }

    @Override
    public String saveAccessTimeMetaData(final BasicFileAttributes attr) {
        mAccessTime = String.valueOf(attr.lastAccessTime().toMillis());
        mMetadataMap.put(METADATA_PREFIX + KEY_ACCESS_TIME, mAccessTime);
        setmAccessTime(mAccessTime);
        return mAccessTime;
    }

    @Override
    public String saveLastModifiedTime(final BasicFileAttributes attr) {
        mlastModified = String.valueOf(attr.lastModifiedTime().toMillis());
        mMetadataMap.put(METADATA_PREFIX + KEY_LAST_MODIFIED_TIME, mlastModified);
        setMlastModified(mlastModified);
        return mlastModified;
    }


    @Override
    public String saveOSMetaData(final String osName) {
        mMetadataMap.put(METADATA_PREFIX + KEY_OS, osName);
        return osName;
    }

    public String getmCreationTime() {
        return mCreationTime;
    }

    public void setmCreationTime(final String mCreationTime) {
        this.mCreationTime = mCreationTime;
    }

    public String getmAccessTime() {
        return mAccessTime;
    }

    public void setmAccessTime(final String mAccessTime) {
        this.mAccessTime = mAccessTime;
    }

    public String getMlastModified() {
        return mlastModified;
    }

    public void setMlastModified(final String mlastModified) {
        this.mlastModified = mlastModified;
    }
}
