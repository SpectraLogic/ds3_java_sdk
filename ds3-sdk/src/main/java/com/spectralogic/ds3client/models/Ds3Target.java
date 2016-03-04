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
import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class Ds3Target {

    // Variables
    @JsonProperty("AccessControlReplication")
    private Ds3TargetAccessControlReplication accessControlReplication;

    @JsonProperty("AdminAuthId")
    private String adminAuthId;

    @JsonProperty("AdminSecretKey")
    private String adminSecretKey;

    @JsonProperty("DataPath")
    private String dataPath;

    @JsonProperty("DefaultReadPreference")
    private TargetReadPreference defaultReadPreference;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("LocalAdminUserId")
    private UUID localAdminUserId;

    @JsonProperty("LocalReplicatedUserDefaultDataPolicyId")
    private UUID localReplicatedUserDefaultDataPolicyId;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Quiesced")
    private Quiesced quiesced;

    @JsonProperty("ReplicatedUserDefaultDataPolicy")
    private String replicatedUserDefaultDataPolicy;

    @JsonProperty("State")
    private TargetState state;

    // Constructor
    public Ds3Target() {
        //pass
    }

    // Getters and Setters
    
    public Ds3TargetAccessControlReplication getAccessControlReplication() {
        return this.accessControlReplication;
    }

    public void setAccessControlReplication(final Ds3TargetAccessControlReplication accessControlReplication) {
        this.accessControlReplication = accessControlReplication;
    }


    public String getAdminAuthId() {
        return this.adminAuthId;
    }

    public void setAdminAuthId(final String adminAuthId) {
        this.adminAuthId = adminAuthId;
    }


    public String getAdminSecretKey() {
        return this.adminSecretKey;
    }

    public void setAdminSecretKey(final String adminSecretKey) {
        this.adminSecretKey = adminSecretKey;
    }


    public String getDataPath() {
        return this.dataPath;
    }

    public void setDataPath(final String dataPath) {
        this.dataPath = dataPath;
    }


    public TargetReadPreference getDefaultReadPreference() {
        return this.defaultReadPreference;
    }

    public void setDefaultReadPreference(final TargetReadPreference defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public UUID getLocalAdminUserId() {
        return this.localAdminUserId;
    }

    public void setLocalAdminUserId(final UUID localAdminUserId) {
        this.localAdminUserId = localAdminUserId;
    }


    public UUID getLocalReplicatedUserDefaultDataPolicyId() {
        return this.localReplicatedUserDefaultDataPolicyId;
    }

    public void setLocalReplicatedUserDefaultDataPolicyId(final UUID localReplicatedUserDefaultDataPolicyId) {
        this.localReplicatedUserDefaultDataPolicyId = localReplicatedUserDefaultDataPolicyId;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public Quiesced getQuiesced() {
        return this.quiesced;
    }

    public void setQuiesced(final Quiesced quiesced) {
        this.quiesced = quiesced;
    }


    public String getReplicatedUserDefaultDataPolicy() {
        return this.replicatedUserDefaultDataPolicy;
    }

    public void setReplicatedUserDefaultDataPolicy(final String replicatedUserDefaultDataPolicy) {
        this.replicatedUserDefaultDataPolicy = replicatedUserDefaultDataPolicy;
    }


    public TargetState getState() {
        return this.state;
    }

    public void setState(final TargetState state) {
        this.state = state;
    }

}