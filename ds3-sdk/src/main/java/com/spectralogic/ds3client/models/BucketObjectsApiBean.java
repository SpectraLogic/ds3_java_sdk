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
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.util.Date;
import com.spectralogic.ds3client.models.S3ObjectApiBean;

public class BucketObjectsApiBean {

    // Variables
    @JsonProperty("CommonPrefixes")
    @JacksonXmlElementWrapper
    private List<String> commonPrefixes;

    @JsonProperty("CreationDate")
    private Date creationDate;

    @JsonProperty("Delimiter")
    private String delimiter;

    @JsonProperty("Marker")
    private String marker;

    @JsonProperty("MaxKeys")
    private int maxKeys;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("NextMarker")
    private String nextMarker;

    @JsonProperty("Objects")
    @JacksonXmlElementWrapper
    private List<S3ObjectApiBean> objects;

    @JsonProperty("Prefix")
    private String prefix;

    @JsonProperty("Truncated")
    private boolean truncated;

    // Constructor
    public BucketObjectsApiBean(final List<String> commonPrefixes, final Date creationDate, final String delimiter, final String marker, final int maxKeys, final String name, final String nextMarker, final List<S3ObjectApiBean> objects, final String prefix, final boolean truncated) {
        this.commonPrefixes = commonPrefixes;
        this.creationDate = creationDate;
        this.delimiter = delimiter;
        this.marker = marker;
        this.maxKeys = maxKeys;
        this.name = name;
        this.nextMarker = nextMarker;
        this.objects = objects;
        this.prefix = prefix;
        this.truncated = truncated;
    }

    // Getters and Setters
    
    public List<String> getCommonPrefixes() {
        return this.commonPrefixes;
    }

    public void setCommonPrefixes(final List<String> commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
    }


    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }


    public String getDelimiter() {
        return this.delimiter;
    }

    public void setDelimiter(final String delimiter) {
        this.delimiter = delimiter;
    }


    public String getMarker() {
        return this.marker;
    }

    public void setMarker(final String marker) {
        this.marker = marker;
    }


    public int getMaxKeys() {
        return this.maxKeys;
    }

    public void setMaxKeys(final int maxKeys) {
        this.maxKeys = maxKeys;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public String getNextMarker() {
        return this.nextMarker;
    }

    public void setNextMarker(final String nextMarker) {
        this.nextMarker = nextMarker;
    }


    public List<S3ObjectApiBean> getObjects() {
        return this.objects;
    }

    public void setObjects(final List<S3ObjectApiBean> objects) {
        this.objects = objects;
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

}