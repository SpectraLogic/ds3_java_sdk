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
public class DataPersistenceRuleApiBean {

    // Variables
    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("IsolationLevel")
    private DataIsolationLevel isolationLevel;

    @JsonProperty("MinimumDaysToRetain")
    private Integer minimumDaysToRetain;

    @JsonProperty("State")
    private DataPlacementRuleState state;

    @JsonProperty("StorageDomainId")
    private UUID storageDomainId;

    @JacksonXmlProperty(isAttribute = true, localName = "StorageDomainName")
    private String storageDomainName;

    @JsonProperty("Type")
    private DataPersistenceRuleType type;

    // Constructor
    public DataPersistenceRuleApiBean() {
        //pass
    }

    // Getters and Setters
    
    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public DataIsolationLevel getIsolationLevel() {
        return this.isolationLevel;
    }

    public void setIsolationLevel(final DataIsolationLevel isolationLevel) {
        this.isolationLevel = isolationLevel;
    }


    public Integer getMinimumDaysToRetain() {
        return this.minimumDaysToRetain;
    }

    public void setMinimumDaysToRetain(final Integer minimumDaysToRetain) {
        this.minimumDaysToRetain = minimumDaysToRetain;
    }


    public DataPlacementRuleState getState() {
        return this.state;
    }

    public void setState(final DataPlacementRuleState state) {
        this.state = state;
    }


    public UUID getStorageDomainId() {
        return this.storageDomainId;
    }

    public void setStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId;
    }


    public String getStorageDomainName() {
        return this.storageDomainName;
    }

    public void setStorageDomainName(final String storageDomainName) {
        this.storageDomainName = storageDomainName;
    }


    public DataPersistenceRuleType getType() {
        return this.type;
    }

    public void setType(final DataPersistenceRuleType type) {
        this.type = type;
    }

}