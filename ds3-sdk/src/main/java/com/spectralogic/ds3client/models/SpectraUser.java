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

@JacksonXmlRootElement(namespace = "Data")
public class SpectraUser {

    // Variables
    @JsonProperty("AuthId")
    private String authId;

    @JsonProperty("DefaultDataPolicyId")
    private UUID defaultDataPolicyId;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("MaxBuckets")
    private int maxBuckets;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("SecretKey")
    private String secretKey;

    // Constructor
    public SpectraUser() {
        //pass
    }

    // Getters and Setters
    
    public String getAuthId() {
        return this.authId;
    }

    public void setAuthId(final String authId) {
        this.authId = authId;
    }


    public UUID getDefaultDataPolicyId() {
        return this.defaultDataPolicyId;
    }

    public void setDefaultDataPolicyId(final UUID defaultDataPolicyId) {
        this.defaultDataPolicyId = defaultDataPolicyId;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public int getMaxBuckets() {
        return this.maxBuckets;
    }

    public void setMaxBuckets(final int maxBuckets) {
        this.maxBuckets = maxBuckets;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }

}