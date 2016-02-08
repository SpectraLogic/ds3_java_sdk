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
import com.spectralogic.ds3client.models.CacheFilesystem;
import com.spectralogic.ds3client.models.CacheEntryInformation;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.lang.String;

@JacksonXmlRootElement(namespace = "Data")
public class CacheFilesystemInformation {

    // Variables
    @JsonProperty("AvailableCapacityInBytes")
    private long availableCapacityInBytes;

    @JsonProperty("CacheFilesystem")
    private CacheFilesystem cacheFilesystem;

    @JsonProperty("Entries")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<CacheEntryInformation> entries = new ArrayList<>();

    @JsonProperty("Summary")
    private String summary;

    @JsonProperty("TotalCapacityInBytes")
    private long totalCapacityInBytes;

    @JsonProperty("UnavailableCapacityInBytes")
    private long unavailableCapacityInBytes;

    @JsonProperty("UsedCapacityInBytes")
    private long usedCapacityInBytes;

    // Constructor
    public CacheFilesystemInformation() {
        //pass
    }

    // Getters and Setters
    
    public long getAvailableCapacityInBytes() {
        return this.availableCapacityInBytes;
    }

    public void setAvailableCapacityInBytes(final long availableCapacityInBytes) {
        this.availableCapacityInBytes = availableCapacityInBytes;
    }


    public CacheFilesystem getCacheFilesystem() {
        return this.cacheFilesystem;
    }

    public void setCacheFilesystem(final CacheFilesystem cacheFilesystem) {
        this.cacheFilesystem = cacheFilesystem;
    }


    public List<CacheEntryInformation> getEntries() {
        return this.entries;
    }

    public void setEntries(final List<CacheEntryInformation> entries) {
        this.entries = entries;
    }


    public String getSummary() {
        return this.summary;
    }

    public void setSummary(final String summary) {
        this.summary = summary;
    }


    public long getTotalCapacityInBytes() {
        return this.totalCapacityInBytes;
    }

    public void setTotalCapacityInBytes(final long totalCapacityInBytes) {
        this.totalCapacityInBytes = totalCapacityInBytes;
    }


    public long getUnavailableCapacityInBytes() {
        return this.unavailableCapacityInBytes;
    }

    public void setUnavailableCapacityInBytes(final long unavailableCapacityInBytes) {
        this.unavailableCapacityInBytes = unavailableCapacityInBytes;
    }


    public long getUsedCapacityInBytes() {
        return this.usedCapacityInBytes;
    }

    public void setUsedCapacityInBytes(final long usedCapacityInBytes) {
        this.usedCapacityInBytes = usedCapacityInBytes;
    }

}