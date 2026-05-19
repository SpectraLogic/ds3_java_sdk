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
public class TargetApiBean {

    // Variables
    @JsonProperty("CloudNamingMode")
    private CloudNamingMode cloudNamingMode;

    @JsonProperty("DefaultReadPreference")
    private TargetReadPreferenceType defaultReadPreference;

    @JsonProperty("Id")
    private UUID id;

    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    private String name;

    @JsonProperty("PermitGoingOutOfSync")
    private boolean permitGoingOutOfSync;

    @JsonProperty("Quiesced")
    private Quiesced quiesced;

    @JsonProperty("State")
    private TargetState state;

    // Constructor
    public TargetApiBean() {
        //pass
    }

    // Getters and Setters
    
    public CloudNamingMode getCloudNamingMode() {
        return this.cloudNamingMode;
    }

    public void setCloudNamingMode(final CloudNamingMode cloudNamingMode) {
        this.cloudNamingMode = cloudNamingMode;
    }


    public TargetReadPreferenceType getDefaultReadPreference() {
        return this.defaultReadPreference;
    }

    public void setDefaultReadPreference(final TargetReadPreferenceType defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public boolean getPermitGoingOutOfSync() {
        return this.permitGoingOutOfSync;
    }

    public void setPermitGoingOutOfSync(final boolean permitGoingOutOfSync) {
        this.permitGoingOutOfSync = permitGoingOutOfSync;
    }


    public Quiesced getQuiesced() {
        return this.quiesced;
    }

    public void setQuiesced(final Quiesced quiesced) {
        this.quiesced = quiesced;
    }


    public TargetState getState() {
        return this.state;
    }

    public void setState(final TargetState state) {
        this.state = state;
    }

}