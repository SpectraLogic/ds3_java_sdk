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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.lang.String;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.spectralogic.ds3client.models.MultiPartUploadApiBean;

@JacksonXmlRootElement(namespace = "ListMultipartUploadsResult")
public class ListMultiPartUploadsApiBean {

    // Variables
    @JsonProperty("Bucket")
    private String bucket;

    @JsonProperty("CommonPrefixes")
    @JacksonXmlElementWrapper(useWrapping = true)
    private List<String> commonPrefixes;

    @JsonProperty("Delimiter")
    private String delimiter;

    @JsonProperty("KeyMarker")
    private String keyMarker;

    @JsonProperty("MaxUploads")
    private int maxUploads;

    @JsonProperty("NextKeyMarker")
    private String nextKeyMarker;

    @JsonProperty("NextUploadIdMarker")
    private String nextUploadIdMarker;

    @JsonProperty("Prefix")
    private String prefix;

    @JsonProperty("IsTruncated")
    private boolean truncated;

    @JsonProperty("UploadIdMarker")
    private String uploadIdMarker;

    @JsonProperty("Upload")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<MultiPartUploadApiBean> uploads;

    // Constructor
    public ListMultiPartUploadsApiBean() {
        //pass
    }

    // Getters and Setters
    
    public String getBucket() {
        return this.bucket;
    }

    public void setBucket(final String bucket) {
        this.bucket = bucket;
    }


    public List<String> getCommonPrefixes() {
        return this.commonPrefixes;
    }

    public void setCommonPrefixes(final List<String> commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
    }


    public String getDelimiter() {
        return this.delimiter;
    }

    public void setDelimiter(final String delimiter) {
        this.delimiter = delimiter;
    }


    public String getKeyMarker() {
        return this.keyMarker;
    }

    public void setKeyMarker(final String keyMarker) {
        this.keyMarker = keyMarker;
    }


    public int getMaxUploads() {
        return this.maxUploads;
    }

    public void setMaxUploads(final int maxUploads) {
        this.maxUploads = maxUploads;
    }


    public String getNextKeyMarker() {
        return this.nextKeyMarker;
    }

    public void setNextKeyMarker(final String nextKeyMarker) {
        this.nextKeyMarker = nextKeyMarker;
    }


    public String getNextUploadIdMarker() {
        return this.nextUploadIdMarker;
    }

    public void setNextUploadIdMarker(final String nextUploadIdMarker) {
        this.nextUploadIdMarker = nextUploadIdMarker;
    }


    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }


    public boolean getTruncated() {
        return this.truncated;
    }

    public void setTruncated(final boolean truncated) {
        this.truncated = truncated;
    }


    public String getUploadIdMarker() {
        return this.uploadIdMarker;
    }

    public void setUploadIdMarker(final String uploadIdMarker) {
        this.uploadIdMarker = uploadIdMarker;
    }


    public List<MultiPartUploadApiBean> getUploads() {
        return this.uploads;
    }

    public void setUploads(final List<MultiPartUploadApiBean> uploads) {
        this.uploads = uploads;
    }

}