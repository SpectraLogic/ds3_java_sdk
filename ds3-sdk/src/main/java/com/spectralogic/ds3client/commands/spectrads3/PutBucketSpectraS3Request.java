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
package com.spectralogic.ds3client.commands.spectrads3;

import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.commands.AbstractRequest;
import com.google.common.net.UrlEscapers;
import java.util.UUID;

public class PutBucketSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String name;

    private UUID dataPolicyId;

    private UUID userId;

    // Constructor
    
    public PutBucketSpectraS3Request(final String name) {
        this.name = name;
                this.getQueryParams().put("name", UrlEscapers.urlFragmentEscaper().escape(name));
    }

    public PutBucketSpectraS3Request withDataPolicyId(final UUID dataPolicyId) {
        this.dataPolicyId = dataPolicyId;
        this.updateQueryParam("data_policy_id", dataPolicyId.toString());
        return this;
    }

    public PutBucketSpectraS3Request withUserId(final UUID userId) {
        this.userId = userId;
        this.updateQueryParam("user_id", userId.toString());
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/bucket";
    }
    
    public String getName() {
        return this.name;
    }


    public UUID getDataPolicyId() {
        return this.dataPolicyId;
    }


    public UUID getUserId() {
        return this.userId;
    }

}