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
import com.spectralogic.ds3client.models.Priority;
import com.google.common.net.UrlEscapers;

public class VerifyPoolSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String pool;

    private Priority priority;

    // Constructor
    
    public VerifyPoolSpectraS3Request(final String pool) {
        this.pool = pool;
        
        this.getQueryParams().put("operation", "verify");
    }

    public VerifyPoolSpectraS3Request withPriority(final Priority priority) {
        this.priority = priority;
        this.updateQueryParam("priority", priority.toString());
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/pool/" + pool;
    }
    
    public String getPool() {
        return this.pool;
    }


    public Priority getPriority() {
        return this.priority;
    }

}