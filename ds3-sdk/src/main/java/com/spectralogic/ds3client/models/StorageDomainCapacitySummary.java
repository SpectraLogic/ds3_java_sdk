/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

@JacksonXmlRootElement(namespace = "Data")
public class StorageDomainCapacitySummary {

    // Variables
    @JsonProperty("PhysicalAllocated")
    private long physicalAllocated;

    @JsonProperty("PhysicalFree")
    private long physicalFree;

    @JsonProperty("PhysicalUsed")
    private long physicalUsed;

    // Constructor
    public StorageDomainCapacitySummary() {
        //pass
    }

    // Getters and Setters
    
    public long getPhysicalAllocated() {
        return this.physicalAllocated;
    }

    public void setPhysicalAllocated(final long physicalAllocated) {
        this.physicalAllocated = physicalAllocated;
    }


    public long getPhysicalFree() {
        return this.physicalFree;
    }

    public void setPhysicalFree(final long physicalFree) {
        this.physicalFree = physicalFree;
    }


    public long getPhysicalUsed() {
        return this.physicalUsed;
    }

    public void setPhysicalUsed(final long physicalUsed) {
        this.physicalUsed = physicalUsed;
    }

}