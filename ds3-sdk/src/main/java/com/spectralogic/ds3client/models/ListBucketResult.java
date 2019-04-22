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
import java.lang.String;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.util.Date;
import com.spectralogic.ds3client.models.common.CommonPrefixes;

@JacksonXmlRootElement(namespace = "ListBucketResult")
public class ListBucketResult {

    // Variables
    @JsonProperty("CommonPrefixes")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<CommonPrefixes> commonPrefixes = new ArrayList<>();

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

    @JsonProperty("Contents")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Contents> objects = new ArrayList<>();

    @JsonProperty("Prefix")
    private String prefix;

    @JsonProperty("IsTruncated")
    private boolean truncated;

    // Constructor
    public ListBucketResult() {
        //pass
    }

    // Getters and Setters
    
    public List<CommonPrefixes> getCommonPrefixes() {
        return this.commonPrefixes;
    }

    public void setCommonPrefixes(final List<CommonPrefixes> commonPrefixes) {
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


    public List<Contents> getObjects() {
        return this.objects;
    }

    public void setObjects(final List<Contents> objects) {
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