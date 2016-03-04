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
import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class StorageDomainMember {

    // Variables
    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("PoolPartitionId")
    private UUID poolPartitionId;

    @JsonProperty("State")
    private StorageDomainMemberState state;

    @JsonProperty("StorageDomainId")
    private UUID storageDomainId;

    @JsonProperty("TapePartitionId")
    private UUID tapePartitionId;

    @JsonProperty("TapeType")
    private TapeType tapeType;

    @JsonProperty("WritePreference")
    private WritePreferenceLevel writePreference;

    // Constructor
    public StorageDomainMember() {
        //pass
    }

    // Getters and Setters
    
    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public UUID getPoolPartitionId() {
        return this.poolPartitionId;
    }

    public void setPoolPartitionId(final UUID poolPartitionId) {
        this.poolPartitionId = poolPartitionId;
    }


    public StorageDomainMemberState getState() {
        return this.state;
    }

    public void setState(final StorageDomainMemberState state) {
        this.state = state;
    }


    public UUID getStorageDomainId() {
        return this.storageDomainId;
    }

    public void setStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId;
    }


    public UUID getTapePartitionId() {
        return this.tapePartitionId;
    }

    public void setTapePartitionId(final UUID tapePartitionId) {
        this.tapePartitionId = tapePartitionId;
    }


    public TapeType getTapeType() {
        return this.tapeType;
    }

    public void setTapeType(final TapeType tapeType) {
        this.tapeType = tapeType;
    }


    public WritePreferenceLevel getWritePreference() {
        return this.writePreference;
    }

    public void setWritePreference(final WritePreferenceLevel writePreference) {
        this.writePreference = writePreference;
    }

}