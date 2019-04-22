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
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.JobRequestType;

public class TruncateAllActiveJobsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private String bucketId;

    private JobRequestType requestType;

    // Constructor
    
    
    public TruncateAllActiveJobsSpectraS3Request() {
        
    }

    public TruncateAllActiveJobsSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }


    public TruncateAllActiveJobsSpectraS3Request withRequestType(final JobRequestType requestType) {
        this.requestType = requestType;
        this.updateQueryParam("request_type", requestType);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.DELETE;
    }

    @Override
    public String getPath() {
        return "/_rest_/active_job";
    }
    
    public String getBucketId() {
        return this.bucketId;
    }


    public JobRequestType getRequestType() {
        return this.requestType;
    }

}