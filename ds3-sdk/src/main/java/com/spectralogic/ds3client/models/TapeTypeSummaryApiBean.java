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

@JacksonXmlRootElement(namespace = "TapeTypeSummary")
public class TapeTypeSummaryApiBean {

    // Variables
    @JsonProperty("AvailableStorageCapacity")
    private long availableStorageCapacity;

    @JsonProperty("Count")
    private int count;

    @JsonProperty("TotalStorageCapacity")
    private long totalStorageCapacity;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("UsedStorageCapacity")
    private long usedStorageCapacity;

    // Constructor
    public TapeTypeSummaryApiBean() {
        //pass
    }

    // Getters and Setters
    
    public long getAvailableStorageCapacity() {
        return this.availableStorageCapacity;
    }

    public void setAvailableStorageCapacity(final long availableStorageCapacity) {
        this.availableStorageCapacity = availableStorageCapacity;
    }


    public int getCount() {
        return this.count;
    }

    public void setCount(final int count) {
        this.count = count;
    }


    public long getTotalStorageCapacity() {
        return this.totalStorageCapacity;
    }

    public void setTotalStorageCapacity(final long totalStorageCapacity) {
        this.totalStorageCapacity = totalStorageCapacity;
    }


    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }


    public long getUsedStorageCapacity() {
        return this.usedStorageCapacity;
    }

    public void setUsedStorageCapacity(final long usedStorageCapacity) {
        this.usedStorageCapacity = usedStorageCapacity;
    }

}