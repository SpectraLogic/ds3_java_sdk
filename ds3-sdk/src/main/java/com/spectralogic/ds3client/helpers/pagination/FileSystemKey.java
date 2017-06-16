/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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
package com.spectralogic.ds3client.helpers.pagination;

import com.spectralogic.ds3client.exceptions.UnableToConvertToCommonPrefixesException;
import com.spectralogic.ds3client.exceptions.UnableToConvertToContentsException;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.User;
import com.spectralogic.ds3client.models.common.CommonPrefixes;

import java.util.Date;

public class FileSystemKey {
    private final String prefix;
    private final String eTag;
    private final String key;
    private final Date lastModified;
    private final User owner;
    private final Long size;
    private final String storageClass;

    private FileSystemKey(final String prefixString) {
        prefix = prefixString;
        eTag = null;
        key = null;
        lastModified = null;
        owner = null;
        size = null;
        storageClass = null;
    }

    public FileSystemKey(final CommonPrefixes commonPrefixes) {
        this(commonPrefixes.getPrefix());
    }

    private FileSystemKey(final String eTag, final  String key, final  Date lastModified, final User owner, final long size, final String storageClass) {
        this.prefix = null;
        this.eTag = eTag;
        this.key = key;
        this.lastModified = lastModified;
        this.owner = owner;
        this.size = size;
        this.storageClass = storageClass;
    }

    public FileSystemKey(final Contents contents) {
        this(contents.getETag(), contents.getKey(), contents.getLastModified(), contents.getOwner(), contents.getSize(), contents.getStorageClass());
    }

    public boolean isPrefix() {
        return prefix != null;
    }

    public boolean isContents() {
        return prefix == null;
    }

    public CommonPrefixes toCommonPrefixes() throws RuntimeException{
       if (isContents()) {
           throw new UnableToConvertToCommonPrefixesException("Could not create a CommonPrefix");
       }
       final CommonPrefixes commonPrefixes = new CommonPrefixes();
       commonPrefixes.setPrefix(prefix);
       return commonPrefixes;
    }

    public Contents toContents() throws RuntimeException {
        if (isPrefix()) {
            throw new UnableToConvertToContentsException("Could not create a Contents");
        }
        final Contents contents = new Contents();
        contents.setETag(eTag);
        contents.setKey(key);
        contents.setLastModified(lastModified);
        contents.setOwner(owner);
        contents.setSize(size);
        contents.setStorageClass(storageClass);
        return contents;
    }

    public String getName() {
        return isPrefix() ? prefix : key;
    }
}