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
import java.util.UUID;
import com.google.common.net.UrlEscapers;

public class GetJobChunksReadyForClientProcessingSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String job;

    private String jobChunk;

    private int preferredNumberOfChunks;

    // Constructor
    
    
    public GetJobChunksReadyForClientProcessingSpectraS3Request(final UUID job) {
        this.job = job.toString();
        
        this.updateQueryParam("job", job);

    }

    
    public GetJobChunksReadyForClientProcessingSpectraS3Request(final String job) {
        this.job = job;
        
        this.updateQueryParam("job", job);

    }

    public GetJobChunksReadyForClientProcessingSpectraS3Request withJobChunk(final UUID jobChunk) {
        this.jobChunk = jobChunk.toString();
        this.updateQueryParam("job_chunk", jobChunk);
        return this;
    }


    public GetJobChunksReadyForClientProcessingSpectraS3Request withJobChunk(final String jobChunk) {
        this.jobChunk = jobChunk;
        this.updateQueryParam("job_chunk", jobChunk);
        return this;
    }


    public GetJobChunksReadyForClientProcessingSpectraS3Request withPreferredNumberOfChunks(final int preferredNumberOfChunks) {
        this.preferredNumberOfChunks = preferredNumberOfChunks;
        this.updateQueryParam("preferred_number_of_chunks", preferredNumberOfChunks);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/job_chunk";
    }
    
    public String getJob() {
        return this.job;
    }


    public String getJobChunk() {
        return this.jobChunk;
    }


    public int getPreferredNumberOfChunks() {
        return this.preferredNumberOfChunks;
    }

}