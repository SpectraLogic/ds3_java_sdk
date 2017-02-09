/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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
import com.google.common.net.UrlEscapers;
import java.lang.Integer;
import com.spectralogic.ds3client.models.TargetReadPreferenceType;

public class RegisterAzureTargetSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String accountKey;

    private final String accountName;

    private final String name;

    private Integer autoVerifyFrequencyInDays;

    private String cloudBucketPrefix;

    private String cloudBucketSuffix;

    private TargetReadPreferenceType defaultReadPreference;

    private boolean https;

    private boolean permitGoingOutOfSync;

    // Constructor
    
    
    public RegisterAzureTargetSpectraS3Request(final String accountKey, final String accountName, final String name) {
        this.accountKey = accountKey;
        this.accountName = accountName;
        this.name = name;
        
        this.getQueryParams().put("account_key", UrlEscapers.urlFragmentEscaper().escape(accountKey).replace("+", "%2B"));
        this.getQueryParams().put("account_name", UrlEscapers.urlFragmentEscaper().escape(accountName).replace("+", "%2B"));
        this.getQueryParams().put("name", UrlEscapers.urlFragmentEscaper().escape(name).replace("+", "%2B"));
    }

    public RegisterAzureTargetSpectraS3Request withAutoVerifyFrequencyInDays(final Integer autoVerifyFrequencyInDays) {
        this.autoVerifyFrequencyInDays = autoVerifyFrequencyInDays;
        this.updateQueryParam("auto_verify_frequency_in_days", autoVerifyFrequencyInDays);
        return this;
    }


    public RegisterAzureTargetSpectraS3Request withCloudBucketPrefix(final String cloudBucketPrefix) {
        this.cloudBucketPrefix = cloudBucketPrefix;
        this.updateQueryParam("cloud_bucket_prefix", cloudBucketPrefix);
        return this;
    }


    public RegisterAzureTargetSpectraS3Request withCloudBucketSuffix(final String cloudBucketSuffix) {
        this.cloudBucketSuffix = cloudBucketSuffix;
        this.updateQueryParam("cloud_bucket_suffix", cloudBucketSuffix);
        return this;
    }


    public RegisterAzureTargetSpectraS3Request withDefaultReadPreference(final TargetReadPreferenceType defaultReadPreference) {
        this.defaultReadPreference = defaultReadPreference;
        this.updateQueryParam("default_read_preference", defaultReadPreference);
        return this;
    }


    public RegisterAzureTargetSpectraS3Request withHttps(final boolean https) {
        this.https = https;
        this.updateQueryParam("https", https);
        return this;
    }


    public RegisterAzureTargetSpectraS3Request withPermitGoingOutOfSync(final boolean permitGoingOutOfSync) {
        this.permitGoingOutOfSync = permitGoingOutOfSync;
        this.updateQueryParam("permit_going_out_of_sync", permitGoingOutOfSync);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/azure_target";
    }
    
    public String getAccountKey() {
        return this.accountKey;
    }


    public String getAccountName() {
        return this.accountName;
    }


    public String getName() {
        return this.name;
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


    public boolean getPermitGoingOutOfSync() {
        return this.permitGoingOutOfSync;
    }

}