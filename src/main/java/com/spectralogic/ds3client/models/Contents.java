/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contents {

    @JsonProperty("Key")
    private String key;
    @JsonProperty("LastModified")
    private String lastModified;
    @JsonProperty("ETag")
    private String eTag;
    @JsonProperty("Size")
    private int size;
    @JsonProperty("StorageClass")
    private String storageClass;
    @JsonProperty("Owner")
    private Owner owner;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(final String lastModified) {
        this.lastModified = lastModified;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(final String eTag) {
        this.eTag = eTag;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(final String storageClass) {
        this.storageClass = storageClass;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(final Owner owner) {
        this.owner = owner;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public String toString() {
        return "{{key:: " + key + "}, " + "{size:: " + size + "}, " + "{owner:: " + owner + "}}";
    }
}
