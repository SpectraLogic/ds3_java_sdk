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
import java.lang.String;

public class TapeLibrary {

    // Variables
    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("ManagementUrl")
    private String managementUrl;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("SerialNumber")
    private String serialNumber;

    // Constructor
    public TapeLibrary(final UUID id, final String managementUrl, final String name, final String serialNumber) {
        this.id = id;
        this.managementUrl = managementUrl;
        this.name = name;
        this.serialNumber = serialNumber;
    }

    // Getters and Setters
    
    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public String getManagementUrl() {
        return this.managementUrl;
    }

    public void setManagementUrl(final String managementUrl) {
        this.managementUrl = managementUrl;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }

}