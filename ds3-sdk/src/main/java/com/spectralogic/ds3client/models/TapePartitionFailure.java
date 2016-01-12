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
import java.util.Date;
import java.lang.String;
import java.util.UUID;
import com.spectralogic.ds3client.models.TapePartitionFailureType;

public class TapePartitionFailure {

    // Variables
    @JsonProperty("Date")
    private Date date;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("PartitionId")
    private UUID partitionId;

    @JsonProperty("Type")
    private TapePartitionFailureType type;

    // Constructor
    public TapePartitionFailure(final Date date, final String errorMessage, final UUID id, final UUID partitionId, final TapePartitionFailureType type) {
        this.date = date;
        this.errorMessage = errorMessage;
        this.id = id;
        this.partitionId = partitionId;
        this.type = type;
    }

    // Getters and Setters
    
    public Date getDate() {
        return this.date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }


    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
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


    public TapePartitionFailureType getType() {
        return this.type;
    }

    public void setType(final TapePartitionFailureType type) {
        this.type = type;
    }

}