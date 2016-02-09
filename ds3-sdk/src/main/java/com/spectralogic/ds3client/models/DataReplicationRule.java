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
import com.spectralogic.ds3client.models.ReplicationConflictResolutionMode;
import java.util.UUID;
import java.lang.String;
import com.spectralogic.ds3client.models.DataPersistenceRuleState;
import com.spectralogic.ds3client.models.DataReplicationRuleType;

@JacksonXmlRootElement(namespace = "Data")
public class DataReplicationRule {

    // Variables
    @JsonProperty("ConflictResolutionMode")
    private ReplicationConflictResolutionMode conflictResolutionMode;

    @JsonProperty("DataPolicyId")
    private UUID dataPolicyId;

    @JsonProperty("Ds3TargetDataPolicy")
    private String ds3TargetDataPolicy;

    @JsonProperty("Ds3TargetId")
    private UUID ds3TargetId;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("State")
    private DataPersistenceRuleState state;

    @JsonProperty("Type")
    private DataReplicationRuleType type;

    // Constructor
    public DataReplicationRule() {
        //pass
    }

    // Getters and Setters
    
    public ReplicationConflictResolutionMode getConflictResolutionMode() {
        return this.conflictResolutionMode;
    }

    public void setConflictResolutionMode(final ReplicationConflictResolutionMode conflictResolutionMode) {
        this.conflictResolutionMode = conflictResolutionMode;
    }


    public UUID getDataPolicyId() {
        return this.dataPolicyId;
    }

    public void setDataPolicyId(final UUID dataPolicyId) {
        this.dataPolicyId = dataPolicyId;
    }


    public String getDs3TargetDataPolicy() {
        return this.ds3TargetDataPolicy;
    }

    public void setDs3TargetDataPolicy(final String ds3TargetDataPolicy) {
        this.ds3TargetDataPolicy = ds3TargetDataPolicy;
    }


    public UUID getDs3TargetId() {
        return this.ds3TargetId;
    }

    public void setDs3TargetId(final UUID ds3TargetId) {
        this.ds3TargetId = ds3TargetId;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public DataPersistenceRuleState getState() {
        return this.state;
    }

    public void setState(final DataPersistenceRuleState state) {
        this.state = state;
    }


    public DataReplicationRuleType getType() {
        return this.type;
    }

    public void setType(final DataReplicationRuleType type) {
        this.type = type;
    }

}