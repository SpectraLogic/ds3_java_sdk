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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.lang.Boolean;
import java.util.Date;
import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class Contents {

    // Variables
    @JsonProperty("ETag")
    private String eTag;

    @JsonProperty("IsLatest")
    private Boolean isLatest;

    @JsonProperty("Key")
    private String key;

    @JsonProperty("LastModified")
    private Date lastModified;

    @JsonProperty("Owner")
    private User owner;

    @JsonProperty("Size")
    private long size;

    @JsonProperty("StorageClass")
    private String storageClass;

    @JsonProperty("VersionId")
    private UUID versionId;

    // Constructor
    public Contents() {
        //pass
    }

    // Getters and Setters
    
    public String getETag() {
        return this.eTag;
    }

    public void setETag(final String eTag) {
        this.eTag = eTag;
    }


    public Boolean getIsLatest() {
        return this.isLatest;
    }

    public void setIsLatest(final Boolean isLatest) {
        this.isLatest = isLatest;
    }


    public String getKey() {
        return this.key;
    }

    public void setKey(final String key) {
        this.key = key;
    }


    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(final Date lastModified) {
        this.lastModified = lastModified;
    }


    public User getOwner() {
        return this.owner;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }


    public long getSize() {
        return this.size;
    }

    public void setSize(final long size) {
        this.size = size;
    }


    public String getStorageClass() {
        return this.storageClass;
    }

    public void setStorageClass(final String storageClass) {
        this.storageClass = storageClass;
    }


    public UUID getVersionId() {
        return this.versionId;
    }

    public void setVersionId(final UUID versionId) {
        this.versionId = versionId;
    }

}