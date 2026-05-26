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

public class ModifyCacheThrottleRuleSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String cacheThrottleRule;

    private String bucketId;

    private Double burstThreshold;

    private double maxCachePercent;

    private Priority priority;

    private JobRequestType requestType;

    // Constructor
    
    
    public ModifyCacheThrottleRuleSpectraS3Request(final String cacheThrottleRule) {
        this.cacheThrottleRule = cacheThrottleRule;
        
    }

    public ModifyCacheThrottleRuleSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }


    public ModifyCacheThrottleRuleSpectraS3Request withBurstThreshold(final Double burstThreshold) {
        this.burstThreshold = burstThreshold;
        this.updateQueryParam("burst_threshold", burstThreshold);
        return this;
    }


    public ModifyCacheThrottleRuleSpectraS3Request withMaxCachePercent(final double maxCachePercent) {
        this.maxCachePercent = maxCachePercent;
        this.updateQueryParam("max_cache_percent", maxCachePercent);
        return this;
    }


    public ModifyCacheThrottleRuleSpectraS3Request withPriority(final Priority priority) {
        this.priority = priority;
        this.updateQueryParam("priority", priority);
        return this;
    }


    public ModifyCacheThrottleRuleSpectraS3Request withRequestType(final JobRequestType requestType) {
        this.requestType = requestType;
        this.updateQueryParam("request_type", requestType);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/cache_throttle_rule/" + cacheThrottleRule;
    }
    
    public String getCacheThrottleRule() {
        return this.cacheThrottleRule;
    }


    public String getBucketId() {
        return this.bucketId;
    }


    public Double getBurstThreshold() {
        return this.burstThreshold;
    }


    public double getMaxCachePercent() {
        return this.maxCachePercent;
    }


    public Priority getPriority() {
        return this.priority;
    }


    public JobRequestType getRequestType() {
        return this.requestType;
    }

}