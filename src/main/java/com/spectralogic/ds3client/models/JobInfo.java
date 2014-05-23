/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JobInfo {
    @JsonProperty("BucketName")
    private String bucketName;
    
    @JsonProperty("StartDate")
    private String startDate;
    
    @JsonProperty("JobId")
    private UUID jobId;
    
    @JsonProperty("Priority")
    private String priority;
    
    @JsonProperty("RequestType")
    private String requestType;
    
    public JobInfo() {
        // For serializer.
    }
    
    public JobInfo(
            final String bucketName,
            final String startDate,
            final UUID jobId,
            final String priority,
            final String requestType) {
        this.bucketName = bucketName;
        this.startDate = startDate;
        this.jobId = jobId;
        this.priority = priority;
        this.requestType = requestType;
    }

    public String getBucketName() {
        return this.bucketName;
    }
    
    public String getStartDate() {
        return this.startDate;
    }
    
    public UUID getJobId() {
        return this.jobId;
    }
    
    public String getPriority() {
        return this.priority;
    }
    
    public String getRequestType() {
        return this.requestType;
    }
}
