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
import java.util.UUID;
import java.lang.Long;
import java.lang.Double;
import java.lang.String;

public class CacheFilesystem {

    // Variables
    @JsonProperty("AutoReclaimInitiateThreshold")
    private double autoReclaimInitiateThreshold;

    @JsonProperty("AutoReclaimTerminateThreshold")
    private double autoReclaimTerminateThreshold;

    @JsonProperty("BurstThreshold")
    private double burstThreshold;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("MaxCapacityInBytes")
    private Long maxCapacityInBytes;

    @JsonProperty("MaxPercentUtilizationOfFilesystem")
    private Double maxPercentUtilizationOfFilesystem;

    @JsonProperty("NodeId")
    private UUID nodeId;

    @JsonProperty("Path")
    private String path;

    // Constructor
    public CacheFilesystem() {
        //pass
    }

    // Getters and Setters
    
    public double getAutoReclaimInitiateThreshold() {
        return this.autoReclaimInitiateThreshold;
    }

    public void setAutoReclaimInitiateThreshold(final double autoReclaimInitiateThreshold) {
        this.autoReclaimInitiateThreshold = autoReclaimInitiateThreshold;
    }


    public double getAutoReclaimTerminateThreshold() {
        return this.autoReclaimTerminateThreshold;
    }

    public void setAutoReclaimTerminateThreshold(final double autoReclaimTerminateThreshold) {
        this.autoReclaimTerminateThreshold = autoReclaimTerminateThreshold;
    }


    public double getBurstThreshold() {
        return this.burstThreshold;
    }

    public void setBurstThreshold(final double burstThreshold) {
        this.burstThreshold = burstThreshold;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public Long getMaxCapacityInBytes() {
        return this.maxCapacityInBytes;
    }

    public void setMaxCapacityInBytes(final Long maxCapacityInBytes) {
        this.maxCapacityInBytes = maxCapacityInBytes;
    }


    public Double getMaxPercentUtilizationOfFilesystem() {
        return this.maxPercentUtilizationOfFilesystem;
    }

    public void setMaxPercentUtilizationOfFilesystem(final Double maxPercentUtilizationOfFilesystem) {
        this.maxPercentUtilizationOfFilesystem = maxPercentUtilizationOfFilesystem;
    }


    public UUID getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(final UUID nodeId) {
        this.nodeId = nodeId;
    }


    public String getPath() {
        return this.path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

}