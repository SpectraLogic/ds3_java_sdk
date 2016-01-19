/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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
import java.lang.String;
import com.spectralogic.ds3client.models.UserApiBean;
import java.lang.Integer;
import com.spectralogic.ds3client.models.MultiPartUploadPartApiBean;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.util.UUID;

public class ListMultiPartUploadPartsApiBean {

    // Variables
    @JsonProperty("Bucket")
    private String bucket;

    @JsonProperty("Key")
    private String key;

    @JsonProperty("MaxParts")
    private int maxParts;

    @JsonProperty("NextPartNumberMarker")
    private int nextPartNumberMarker;

    @JsonProperty("Owner")
    private UserApiBean owner;

    @JsonProperty("PartNumberMarker")
    private Integer partNumberMarker;

    @JsonProperty("Parts")
    @JacksonXmlElementWrapper
    private List<MultiPartUploadPartApiBean> parts;

    @JsonProperty("Truncated")
    private boolean truncated;

    @JsonProperty("UploadId")
    private UUID uploadId;

    // Constructor
    public ListMultiPartUploadPartsApiBean() {
        //pass
    }

    // Getters and Setters
    
    public String getBucket() {
        return this.bucket;
    }

    public void setBucket(final String bucket) {
        this.bucket = bucket;
    }


    public String getKey() {
        return this.key;
    }

    public void setKey(final String key) {
        this.key = key;
    }


    public int getMaxParts() {
        return this.maxParts;
    }

    public void setMaxParts(final int maxParts) {
        this.maxParts = maxParts;
    }


    public int getNextPartNumberMarker() {
        return this.nextPartNumberMarker;
    }

    public void setNextPartNumberMarker(final int nextPartNumberMarker) {
        this.nextPartNumberMarker = nextPartNumberMarker;
    }


    public UserApiBean getOwner() {
        return this.owner;
    }

    public void setOwner(final UserApiBean owner) {
        this.owner = owner;
    }


    public Integer getPartNumberMarker() {
        return this.partNumberMarker;
    }

    public void setPartNumberMarker(final Integer partNumberMarker) {
        this.partNumberMarker = partNumberMarker;
    }


    public List<MultiPartUploadPartApiBean> getParts() {
        return this.parts;
    }

    public void setParts(final List<MultiPartUploadPartApiBean> parts) {
        this.parts = parts;
    }


    public boolean getTruncated() {
        return this.truncated;
    }

    public void setTruncated(final boolean truncated) {
        this.truncated = truncated;
    }


    public UUID getUploadId() {
        return this.uploadId;
    }

    public void setUploadId(final UUID uploadId) {
        this.uploadId = uploadId;
    }

}