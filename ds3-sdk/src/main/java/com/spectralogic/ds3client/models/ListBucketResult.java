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

package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

public class ListBucketResult {

    @JsonProperty("Name")
    private String name;
    @JsonProperty("Prefix")
    private String prefix;
    @JsonProperty("Marker")
    private String marker;
    @JsonProperty("MaxKeys")
    private int maxKeys;
    @JsonProperty("IsTruncated")
    private boolean isTruncated;
    @JsonProperty("CreationDate")
    private String creationDate;
    @JsonProperty("Delimiter")
    private String delimiter;
    @JsonProperty("NextMarker")
    private String nextMarker;

    @JsonProperty("CommonPrefixes")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<CommonPrefixes> commonPrefixes;

    @JsonProperty("Contents")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Contents> contentsList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(final String marker) {
        this.marker = marker;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public void setMaxKeys(final int maxKeys) {
        this.maxKeys = maxKeys;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public void setTruncated(final boolean isTruncated) {
        this.isTruncated = isTruncated;
    }

    public List<Contents> getContentsList() {
        return contentsList;
    }

    public void setContentsList(final List<Contents> contentsList) {
        this.contentsList = contentsList;
    }

    @Override
    public String toString() {
        return "{{bucket:: " + name + "},\n" + "{numKeys:: " + maxKeys + "},\n" + "{objects:: " + contentsList + "}}";
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(final String delimiter) {
        this.delimiter = delimiter;
    }

    public String getNextMarker() {
        return nextMarker;
    }

    public void setNextMarker(final String nextMarker) {
        this.nextMarker = nextMarker;
    }

    public List<CommonPrefixes> getCommonPrefixes() {
        return commonPrefixes;
    }

    public void setCommonPrefixes(final List<CommonPrefixes> commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
    }
}
