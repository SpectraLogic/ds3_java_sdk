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
import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationRequest;
import java.lang.Double;
import java.util.UUID;
import com.spectralogic.ds3client.models.Priority;
import com.spectralogic.ds3client.models.JobRequestType;

public class GetCacheThrottleRulesSpectraS3Request extends AbstractPaginationRequest {

    // Variables
    
    private String bucketId;

    private Double burstThreshold;

    private boolean lastPage;

    private double maxCachePercent;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private Priority priority;

    private JobRequestType requestType;

    // Constructor
    
    
    public GetCacheThrottleRulesSpectraS3Request() {
        
    }

    public GetCacheThrottleRulesSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }


    public GetCacheThrottleRulesSpectraS3Request withBurstThreshold(final Double burstThreshold) {
        this.burstThreshold = burstThreshold;
        this.updateQueryParam("burst_threshold", burstThreshold);
        return this;
    }


    public GetCacheThrottleRulesSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }


    public GetCacheThrottleRulesSpectraS3Request withMaxCachePercent(final double maxCachePercent) {
        this.maxCachePercent = maxCachePercent;
        this.updateQueryParam("max_cache_percent", maxCachePercent);
        return this;
    }


    public GetCacheThrottleRulesSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }


    public GetCacheThrottleRulesSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }


    public GetCacheThrottleRulesSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetCacheThrottleRulesSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetCacheThrottleRulesSpectraS3Request withPriority(final Priority priority) {
        this.priority = priority;
        this.updateQueryParam("priority", priority);
        return this;
    }


    public GetCacheThrottleRulesSpectraS3Request withRequestType(final JobRequestType requestType) {
        this.requestType = requestType;
        this.updateQueryParam("request_type", requestType);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/cache_throttle_rule";
    }
    
    public String getBucketId() {
        return this.bucketId;
    }


    public Double getBurstThreshold() {
        return this.burstThreshold;
    }


    public boolean getLastPage() {
        return this.lastPage;
    }


    public double getMaxCachePercent() {
        return this.maxCachePercent;
    }


    public int getPageLength() {
        return this.pageLength;
    }


    public int getPageOffset() {
        return this.pageOffset;
    }


    public String getPageStartMarker() {
        return this.pageStartMarker;
    }


    public Priority getPriority() {
        return this.priority;
    }


    public JobRequestType getRequestType() {
        return this.requestType;
    }

}