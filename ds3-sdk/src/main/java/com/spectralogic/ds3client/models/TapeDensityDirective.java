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
import com.spectralogic.ds3client.models.TapeDriveType;
import java.util.UUID;
import com.spectralogic.ds3client.models.TapeType;

public class TapeDensityDirective {

    // Variables
    @JsonProperty("Density")
    private TapeDriveType density;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("PartitionId")
    private UUID partitionId;

    @JsonProperty("TapeType")
    private TapeType tapeType;

    // Constructor
    public TapeDensityDirective(final TapeDriveType density, final UUID id, final UUID partitionId, final TapeType tapeType) {
        this.density = density;
        this.id = id;
        this.partitionId = partitionId;
        this.tapeType = tapeType;
    }

    // Getters and Setters
    
    public TapeDriveType getDensity() {
        return this.density;
    }

    public void setDensity(final TapeDriveType density) {
        this.density = density;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public UUID getPartitionId() {
        return this.partitionId;
    }

    public void setPartitionId(final UUID partitionId) {
        this.partitionId = partitionId;
    }


    public TapeType getTapeType() {
        return this.tapeType;
    }

    public void setTapeType(final TapeType tapeType) {
        this.tapeType = tapeType;
    }

}