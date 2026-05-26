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
import java.lang.Double;
import com.spectralogic.ds3client.models.Priority;
import com.spectralogic.ds3client.models.JobRequestType;

public class PutCacheThrottleRuleSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final double maxCachePercent;

    private String bucketId;

    private Double burstThreshold;

    private Priority priority;

    private JobRequestType requestType;

    // Constructor
    
    
    public PutCacheThrottleRuleSpectraS3Request(final double maxCachePercent) {
        this.maxCachePercent = maxCachePercent;
        
        this.updateQueryParam("max_cache_percent", maxCachePercent);

    }

    public PutCacheThrottleRuleSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }


    public PutCacheThrottleRuleSpectraS3Request withBurstThreshold(final Double burstThreshold) {
        this.burstThreshold = burstThreshold;
        this.updateQueryParam("burst_threshold", burstThreshold);
        return this;
    }


    public PutCacheThrottleRuleSpectraS3Request withPriority(final Priority priority) {
        this.priority = priority;
        this.updateQueryParam("priority", priority);
        return this;
    }


    public PutCacheThrottleRuleSpectraS3Request withRequestType(final JobRequestType requestType) {
        this.requestType = requestType;
        this.updateQueryParam("request_type", requestType);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/cache_throttle_rule";
    }
    
    public double getMaxCachePercent() {
        return this.maxCachePercent;
    }


    public String getBucketId() {
        return this.bucketId;
    }


    public Double getBurstThreshold() {
        return this.burstThreshold;
    }


    public Priority getPriority() {
        return this.priority;
    }


    public JobRequestType getRequestType() {
        return this.requestType;
    }

}