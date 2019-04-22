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
import java.util.UUID;
import com.google.common.net.UrlEscapers;

public class ModifyUserSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String userId;

    private String defaultDataPolicyId;

    private int maxBuckets;

    private String name;

    private String secretKey;

    // Constructor
    
    
    public ModifyUserSpectraS3Request(final UUID userId) {
        this.userId = userId.toString();
        
    }

    
    public ModifyUserSpectraS3Request(final String userId) {
        this.userId = userId;
        
    }

    public ModifyUserSpectraS3Request withDefaultDataPolicyId(final UUID defaultDataPolicyId) {
        this.defaultDataPolicyId = defaultDataPolicyId.toString();
        this.updateQueryParam("default_data_policy_id", defaultDataPolicyId);
        return this;
    }


    public ModifyUserSpectraS3Request withDefaultDataPolicyId(final String defaultDataPolicyId) {
        this.defaultDataPolicyId = defaultDataPolicyId;
        this.updateQueryParam("default_data_policy_id", defaultDataPolicyId);
        return this;
    }


    public ModifyUserSpectraS3Request withMaxBuckets(final int maxBuckets) {
        this.maxBuckets = maxBuckets;
        this.updateQueryParam("max_buckets", maxBuckets);
        return this;
    }


    public ModifyUserSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }


    public ModifyUserSpectraS3Request withSecretKey(final String secretKey) {
        this.secretKey = secretKey;
        this.updateQueryParam("secret_key", secretKey);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/user/" + userId;
    }
    
    public String getUserId() {
        return this.userId;
    }


    public String getDefaultDataPolicyId() {
        return this.defaultDataPolicyId;
    }


    public int getMaxBuckets() {
        return this.maxBuckets;
    }


    public String getName() {
        return this.name;
    }


    public String getSecretKey() {
        return this.secretKey;
    }

}