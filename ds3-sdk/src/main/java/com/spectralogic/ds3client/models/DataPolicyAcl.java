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

public class DataPolicyAcl {

    // Variables
    @JsonProperty("DataPolicyId")
    private UUID dataPolicyId;

    @JsonProperty("GroupId")
    private UUID groupId;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("UserId")
    private UUID userId;

    // Constructor
    public DataPolicyAcl(final UUID dataPolicyId, final UUID groupId, final UUID id, final UUID userId) {
        this.dataPolicyId = dataPolicyId;
        this.groupId = groupId;
        this.id = id;
        this.userId = userId;
    }

    // Getters and Setters
    
    public UUID getDataPolicyId() {
        return this.dataPolicyId;
    }

    public void setDataPolicyId(final UUID dataPolicyId) {
        this.dataPolicyId = dataPolicyId;
    }


    public UUID getGroupId() {
        return this.groupId;
    }

    public void setGroupId(final UUID groupId) {
        this.groupId = groupId;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(final UUID userId) {
        this.userId = userId;
    }

}