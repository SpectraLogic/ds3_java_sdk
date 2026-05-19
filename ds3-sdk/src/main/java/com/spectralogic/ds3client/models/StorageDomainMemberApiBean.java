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
import java.util.UUID;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JacksonXmlRootElement(namespace = "Data")
public class StorageDomainMemberApiBean {

    // Variables
    @JsonProperty("AutoCompactionThreshold")
    private Integer autoCompactionThreshold;

    @JsonProperty("Id")
    private UUID id;

    @JacksonXmlProperty(isAttribute = true, localName = "PartitionName")
    private String partitionName;

    @JsonProperty("PoolPartitionId")
    private UUID poolPartitionId;

    @JsonProperty("State")
    private StorageDomainMemberState state;

    @JsonProperty("TapePartitionId")
    private UUID tapePartitionId;

    @JacksonXmlProperty(isAttribute = true, localName = "TapeType")
    private String tapeType;

    @JsonProperty("WritePreference")
    private WritePreferenceLevel writePreference;

    // Constructor
    public StorageDomainMemberApiBean() {
        //pass
    }

    // Getters and Setters
    
    public Integer getAutoCompactionThreshold() {
        return this.autoCompactionThreshold;
    }

    public void setAutoCompactionThreshold(final Integer autoCompactionThreshold) {
        this.autoCompactionThreshold = autoCompactionThreshold;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public String getPartitionName() {
        return this.partitionName;
    }

    public void setPartitionName(final String partitionName) {
        this.partitionName = partitionName;
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


    public UUID getTapePartitionId() {
        return this.tapePartitionId;
    }

    public void setTapePartitionId(final UUID tapePartitionId) {
        this.tapePartitionId = tapePartitionId;
    }


    public String getTapeType() {
        return this.tapeType;
    }

    public void setTapeType(final String tapeType) {
        this.tapeType = tapeType;
    }


    public WritePreferenceLevel getWritePreference() {
        return this.writePreference;
    }

    public void setWritePreference(final WritePreferenceLevel writePreference) {
        this.writePreference = writePreference;
    }

}