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
import java.util.Date;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.Priority;
import java.util.UUID;

public class ModifyJobSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String jobId;

    private Date createdAt;

    private String name;

    private Priority priority;

    // Constructor
    
    
    public ModifyJobSpectraS3Request(final UUID jobId) {
        this.jobId = jobId.toString();
        
    }

    
    public ModifyJobSpectraS3Request(final String jobId) {
        this.jobId = jobId;
        
    }

    public ModifyJobSpectraS3Request withCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
        this.updateQueryParam("created_at", createdAt);
        return this;
    }


    public ModifyJobSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }


    public ModifyJobSpectraS3Request withPriority(final Priority priority) {
        this.priority = priority;
        this.updateQueryParam("priority", priority);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/job/" + jobId;
    }
    
    public String getJobId() {
        return this.jobId;
    }


    public Date getCreatedAt() {
        return this.createdAt;
    }


    public String getName() {
        return this.name;
    }


    public Priority getPriority() {
        return this.priority;
    }

}