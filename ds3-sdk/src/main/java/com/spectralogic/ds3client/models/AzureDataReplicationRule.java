/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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
import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class AzureDataReplicationRule {

    // Variables
    @JsonProperty("DataPolicyId")
    private UUID dataPolicyId;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("MaxBlobPartSizeInBytes")
    private long maxBlobPartSizeInBytes;

    @JsonProperty("ReplicateDeletes")
    private boolean replicateDeletes;

    @JsonProperty("State")
    private DataPlacementRuleState state;

    @JsonProperty("TargetId")
    private UUID targetId;

    @JsonProperty("Type")
    private DataReplicationRuleType type;

    // Constructor
    public AzureDataReplicationRule() {
        //pass
    }

    // Getters and Setters
    
    public UUID getDataPolicyId() {
        return this.dataPolicyId;
    }

    public void setDataPolicyId(final UUID dataPolicyId) {
        this.dataPolicyId = dataPolicyId;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public long getMaxBlobPartSizeInBytes() {
        return this.maxBlobPartSizeInBytes;
    }

    public void setMaxBlobPartSizeInBytes(final long maxBlobPartSizeInBytes) {
        this.maxBlobPartSizeInBytes = maxBlobPartSizeInBytes;
    }


    public boolean getReplicateDeletes() {
        return this.replicateDeletes;
    }

    public void setReplicateDeletes(final boolean replicateDeletes) {
        this.replicateDeletes = replicateDeletes;
    }


    public DataPlacementRuleState getState() {
        return this.state;
    }

    public void setState(final DataPlacementRuleState state) {
        this.state = state;
    }


    public UUID getTargetId() {
        return this.targetId;
    }

    public void setTargetId(final UUID targetId) {
        this.targetId = targetId;
    }


    public DataReplicationRuleType getType() {
        return this.type;
    }

    public void setType(final DataReplicationRuleType type) {
        this.type = type;
    }

}