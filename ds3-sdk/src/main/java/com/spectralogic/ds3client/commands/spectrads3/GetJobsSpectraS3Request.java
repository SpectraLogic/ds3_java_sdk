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
package com.spectralogic.ds3client.commands.spectrads3;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.google.common.net.UrlEscapers;

public class GetJobsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private String bucketId;

    private boolean fullDetails;

    // Constructor
    
    
    public GetJobsSpectraS3Request() {
        
    }

    public GetJobsSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }


    public GetJobsSpectraS3Request withFullDetails(final boolean fullDetails) {
        this.fullDetails = fullDetails;
        if (this.fullDetails) {
            this.getQueryParams().put("full_details", null);
        } else {
            this.getQueryParams().remove("full_details");
        }
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/job";
    }
    
    public String getBucketId() {
        return this.bucketId;
    }


    public boolean getFullDetails() {
        return this.fullDetails;
    }

}