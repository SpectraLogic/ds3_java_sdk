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

public class PutDataPolicyAclForUserSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String dataPolicyId;

    private final String userId;

    // Constructor
    
    
    public PutDataPolicyAclForUserSpectraS3Request(final UUID dataPolicyId, final UUID userId) {
        this.dataPolicyId = dataPolicyId.toString();
        this.userId = userId.toString();
        
        this.updateQueryParam("data_policy_id", dataPolicyId);

        this.updateQueryParam("user_id", userId);

    }

    
    public PutDataPolicyAclForUserSpectraS3Request(final String dataPolicyId, final String userId) {
        this.dataPolicyId = dataPolicyId;
        this.userId = userId;
        
        this.updateQueryParam("data_policy_id", dataPolicyId);

        this.updateQueryParam("user_id", userId);

    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/data_policy_acl";
    }
    
    public String getDataPolicyId() {
        return this.dataPolicyId;
    }


    public String getUserId() {
        return this.userId;
    }

}