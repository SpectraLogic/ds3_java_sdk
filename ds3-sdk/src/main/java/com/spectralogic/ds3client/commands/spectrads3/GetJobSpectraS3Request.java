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

public class GetJobSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String jobId;

    // Constructor
    
    
    public GetJobSpectraS3Request(final UUID jobId) {
        this.jobId = jobId.toString();
        
    }

    
    public GetJobSpectraS3Request(final String jobId) {
        this.jobId = jobId;
        
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/job/" + jobId;
    }
    
    public String getJobId() {
        return this.jobId;
    }


    @Override
    public int hashCode() {
        return jobId.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (! (obj instanceof GetJobSpectraS3Request)) return false;
        final GetJobSpectraS3Request other = (GetJobSpectraS3Request) obj;
        return other.getJobId().equals(this.getJobId());
    }
}