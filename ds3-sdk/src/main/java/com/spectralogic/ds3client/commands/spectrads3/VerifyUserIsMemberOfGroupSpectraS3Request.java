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

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.AbstractRequest;
import java.util.UUID;
import com.google.common.net.UrlEscapers;

public class VerifyUserIsMemberOfGroupSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String group;

    private UUID userId;

    // Constructor
    
    public VerifyUserIsMemberOfGroupSpectraS3Request(final String group) {
        this.group = group;
        
        this.getQueryParams().put("operation", "verify");
    }

    public VerifyUserIsMemberOfGroupSpectraS3Request withUserId(final UUID userId) {
        this.userId = userId;
        this.updateQueryParam("user_id", userId.toString());
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/group/" + group;
    }
    
    public String getGroup() {
        return this.group;
    }


    public UUID getUserId() {
        return this.userId;
    }

}