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
import java.lang.String;
import com.spectralogic.ds3client.models.BuildInformation;
import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class SystemInformationApiBean {

    // Variables
    @JsonProperty("ApiVersion")
    private String apiVersion;

    @JsonProperty("BackendActivated")
    private boolean backendActivated;

    @JsonProperty("BuildInformation")
    private BuildInformation buildInformation;

    @JsonProperty("InstanceId")
    private UUID instanceId;

    @JsonProperty("SerialNumber")
    private String serialNumber;

    // Constructor
    public SystemInformationApiBean() {
        //pass
    }

    // Getters and Setters
    
    public String getApiVersion() {
        return this.apiVersion;
    }

    public void setApiVersion(final String apiVersion) {
        this.apiVersion = apiVersion;
    }


    public boolean getBackendActivated() {
        return this.backendActivated;
    }

    public void setBackendActivated(final boolean backendActivated) {
        this.backendActivated = backendActivated;
    }


    public BuildInformation getBuildInformation() {
        return this.buildInformation;
    }

    public void setBuildInformation(final BuildInformation buildInformation) {
        this.buildInformation = buildInformation;
    }


    public UUID getInstanceId() {
        return this.instanceId;
    }

    public void setInstanceId(final UUID instanceId) {
        this.instanceId = instanceId;
    }


    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }

}