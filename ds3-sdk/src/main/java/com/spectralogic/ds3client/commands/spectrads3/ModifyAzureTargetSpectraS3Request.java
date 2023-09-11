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
package com.spectralogic.ds3client.commands.spectrads3;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;

import java.lang.Integer;
import com.spectralogic.ds3client.models.TargetReadPreferenceType;
import com.spectralogic.ds3client.models.Quiesced;

public class ModifyAzureTargetSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String azureTarget;

    private String accountKey;

    private String accountName;

    private Integer autoVerifyFrequencyInDays;

    private String cloudBucketPrefix;

    private String cloudBucketSuffix;

    private TargetReadPreferenceType defaultReadPreference;

    private boolean https;

    private String name;

    private boolean permitGoingOutOfSync;

    private Quiesced quiesced;

    // Constructor
    
    
    public ModifyAzureTargetSpectraS3Request(final String azureTarget) {
        this.azureTarget = azureTarget;
        
    }

    public ModifyAzureTargetSpectraS3Request withAccountKey(final String accountKey) {
        this.accountKey = accountKey;
        this.updateQueryParam("account_key", accountKey);
        return this;
    }


    public ModifyAzureTargetSpectraS3Request withAccountName(final String accountName) {
        this.accountName = accountName;
        this.updateQueryParam("account_name", accountName);
        return this;
    }


    public ModifyAzureTargetSpectraS3Request withAutoVerifyFrequencyInDays(final Integer autoVerifyFrequencyInDays) {
        this.autoVerifyFrequencyInDays = autoVerifyFrequencyInDays;
        this.updateQueryParam("auto_verify_frequency_in_days", autoVerifyFrequencyInDays);
        return this;
    }


    public ModifyAzureTargetSpectraS3Request withCloudBucketPrefix(final String cloudBucketPrefix) {
        this.cloudBucketPrefix = cloudBucketPrefix;
        this.updateQueryParam("cloud_bucket_prefix", cloudBucketPrefix);
        return this;
    }


    public ModifyAzureTargetSpectraS3Request withCloudBucketSuffix(final String cloudBucketSuffix) {
        this.cloudBucketSuffix = cloudBucketSuffix;
        this.updateQueryParam("cloud_bucket_suffix", cloudBucketSuffix);
        return this;
    }


    public ModifyAzureTargetSpectraS3Request withDefaultReadPreference(final TargetReadPreferenceType defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
        this.updateQueryParam("default_read_preference", defaultReadPreference);
        return this;
    }


    public ModifyAzureTargetSpectraS3Request withHttps(final boolean https) {
        this.https = https;
        this.updateQueryParam("https", https);
        return this;
    }


    public ModifyAzureTargetSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }


    public ModifyAzureTargetSpectraS3Request withPermitGoingOutOfSync(final boolean permitGoingOutOfSync) {
        this.permitGoingOutOfSync = permitGoingOutOfSync;
        this.updateQueryParam("permit_going_out_of_sync", permitGoingOutOfSync);
        return this;
    }


    public ModifyAzureTargetSpectraS3Request withQuiesced(final Quiesced quiesced) {
        this.quiesced = quiesced;
        this.updateQueryParam("quiesced", quiesced);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/azure_target/" + azureTarget;
    }
    
    public String getAzureTarget() {
        return this.azureTarget;
    }


    public String getAccountKey() {
        return this.accountKey;
    }


    public String getAccountName() {
        return this.accountName;
    }


    public Integer getAutoVerifyFrequencyInDays() {
        return this.autoVerifyFrequencyInDays;
    }


    public String getCloudBucketPrefix() {
        return this.cloudBucketPrefix;
    }


    public String getCloudBucketSuffix() {
        return this.cloudBucketSuffix;
    }


    public TargetReadPreferenceType getDefaultReadPreference() {
        return this.defaultReadPreference;
    }


    public boolean getHttps() {
        return this.https;
    }


    public String getName() {
        return this.name;
    }


    public boolean getPermitGoingOutOfSync() {
        return this.permitGoingOutOfSync;
    }


    public Quiesced getQuiesced() {
        return this.quiesced;
    }

}