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
import java.util.Date;
import java.util.UUID;
import java.lang.Boolean;
import java.lang.Long;
import java.lang.String;

@JacksonXmlRootElement(namespace = "Data")
public class Bucket {

    // Variables
    @JsonProperty("CreationDate")
    private Date creationDate;

    @JsonProperty("DataPolicyId")
    private UUID dataPolicyId;

    @JsonProperty("Empty")
    private Boolean empty;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("LastPreferredChunkSizeInBytes")
    private Long lastPreferredChunkSizeInBytes;

    @JsonProperty("LogicalUsedCapacity")
    private Long logicalUsedCapacity;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("UserId")
    private UUID userId;

    // Constructor
    public Bucket() {
        //pass
    }

    // Getters and Setters
    
    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }


    public UUID getDataPolicyId() {
        return this.dataPolicyId;
    }

    public void setDataPolicyId(final UUID dataPolicyId) {
        this.dataPolicyId = dataPolicyId;
    }


    public Boolean getEmpty() {
        return this.empty;
    }

    public void setEmpty(final Boolean empty) {
        this.empty = empty;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public Long getLastPreferredChunkSizeInBytes() {
        return this.lastPreferredChunkSizeInBytes;
    }

    public void setLastPreferredChunkSizeInBytes(final Long lastPreferredChunkSizeInBytes) {
        this.lastPreferredChunkSizeInBytes = lastPreferredChunkSizeInBytes;
    }


    public Long getLogicalUsedCapacity() {
        return this.logicalUsedCapacity;
    }

    public void setLogicalUsedCapacity(final Long logicalUsedCapacity) {
        this.logicalUsedCapacity = logicalUsedCapacity;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(final UUID userId) {
        this.userId = userId;
    }

}