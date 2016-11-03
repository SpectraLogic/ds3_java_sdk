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
import java.lang.String;
import java.lang.Integer;
import java.util.UUID;
import java.util.Date;

@JacksonXmlRootElement(namespace = "Data")
public class AzureTarget {

    // Variables
    @JsonProperty("AccountKey")
    private String accountKey;

    @JsonProperty("AccountName")
    private String accountName;

    @JsonProperty("AutoVerifyFrequencyInDays")
    private Integer autoVerifyFrequencyInDays;

    @JsonProperty("CloudBucketPrefix")
    private String cloudBucketPrefix;

    @JsonProperty("CloudBucketSuffix")
    private String cloudBucketSuffix;

    @JsonProperty("DefaultReadPreference")
    private TargetReadPreferenceType defaultReadPreference;

    @JsonProperty("Https")
    private boolean https;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("LastFullyVerified")
    private Date lastFullyVerified;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("PermitGoingOutOfSync")
    private boolean permitGoingOutOfSync;

    @JsonProperty("Quiesced")
    private Quiesced quiesced;

    @JsonProperty("ReplicateDeletes")
    private boolean replicateDeletes;

    @JsonProperty("State")
    private TargetState state;

    // Constructor
    public AzureTarget() {
        //pass
    }

    // Getters and Setters
    
    public String getAccountKey() {
        return this.accountKey;
    }

    public void setAccountKey(final String accountKey) {
        this.accountKey = accountKey;
    }


    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(final String accountName) {
        this.accountName = accountName;
    }


    public Integer getAutoVerifyFrequencyInDays() {
        return this.autoVerifyFrequencyInDays;
    }

    public void setAutoVerifyFrequencyInDays(final Integer autoVerifyFrequencyInDays) {
        this.autoVerifyFrequencyInDays = autoVerifyFrequencyInDays;
    }


    public String getCloudBucketPrefix() {
        return this.cloudBucketPrefix;
    }

    public void setCloudBucketPrefix(final String cloudBucketPrefix) {
        this.cloudBucketPrefix = cloudBucketPrefix;
    }


    public String getCloudBucketSuffix() {
        return this.cloudBucketSuffix;
    }

    public void setCloudBucketSuffix(final String cloudBucketSuffix) {
        this.cloudBucketSuffix = cloudBucketSuffix;
    }


    public TargetReadPreferenceType getDefaultReadPreference() {
        return this.defaultReadPreference;
    }

    public void setDefaultReadPreference(final TargetReadPreferenceType defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
    }


    public boolean getHttps() {
        return this.https;
    }

    public void setHttps(final boolean https) {
        this.https = https;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public Date getLastFullyVerified() {
        return this.lastFullyVerified;
    }

    public void setLastFullyVerified(final Date lastFullyVerified) {
        this.lastFullyVerified = lastFullyVerified;
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


    public boolean getReplicateDeletes() {
        return this.replicateDeletes;
    }

    public void setReplicateDeletes(final boolean replicateDeletes) {
        this.replicateDeletes = replicateDeletes;
    }


    public TargetState getState() {
        return this.state;
    }

    public void setState(final TargetState state) {
        this.state = state;
    }

}