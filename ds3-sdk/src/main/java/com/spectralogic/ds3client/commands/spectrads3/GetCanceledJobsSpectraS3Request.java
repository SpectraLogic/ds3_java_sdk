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
import com.spectralogic.ds3client.models.JobChunkClientProcessingOrderGuarantee;
import java.util.UUID;
import com.spectralogic.ds3client.models.Priority;
import java.util.Date;
import com.spectralogic.ds3client.models.JobRequestType;

public class GetCanceledJobsSpectraS3Request extends AbstractPaginationRequest {

    // Variables
    
    private String bucketId;

    private boolean canceledDueToTimeout;

    private JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee;

    private boolean lastPage;

    private String name;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private Priority priority;

    private Date rechunked;

    private JobRequestType requestType;

    private boolean truncated;

    private String userId;

    // Constructor
    
    
    public GetCanceledJobsSpectraS3Request() {
        
    }

    public GetCanceledJobsSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }


    public GetCanceledJobsSpectraS3Request withCanceledDueToTimeout(final boolean canceledDueToTimeout) {
        this.canceledDueToTimeout = canceledDueToTimeout;
        this.updateQueryParam("canceled_due_to_timeout", canceledDueToTimeout);
        return this;
    }


    public GetCanceledJobsSpectraS3Request withChunkClientProcessingOrderGuarantee(final JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee) {
        this.chunkClientProcessingOrderGuarantee = chunkClientProcessingOrderGuarantee;
        this.updateQueryParam("chunk_client_processing_order_guarantee", chunkClientProcessingOrderGuarantee);
        return this;
    }


    public GetCanceledJobsSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }


    public GetCanceledJobsSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }


    public GetCanceledJobsSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }


    public GetCanceledJobsSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }


    public GetCanceledJobsSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetCanceledJobsSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetCanceledJobsSpectraS3Request withPriority(final Priority priority) {
        this.priority = priority;
        this.updateQueryParam("priority", priority);
        return this;
    }


    public GetCanceledJobsSpectraS3Request withRechunked(final Date rechunked) {
        this.rechunked = rechunked;
        this.updateQueryParam("rechunked", rechunked);
        return this;
    }


    public GetCanceledJobsSpectraS3Request withRequestType(final JobRequestType requestType) {
        this.requestType = requestType;
        this.updateQueryParam("request_type", requestType);
        return this;
    }


    public GetCanceledJobsSpectraS3Request withTruncated(final boolean truncated) {
        this.truncated = truncated;
        this.updateQueryParam("truncated", truncated);
        return this;
    }


    public GetCanceledJobsSpectraS3Request withUserId(final UUID userId) {
        this.userId = userId.toString();
        this.updateQueryParam("user_id", userId);
        return this;
    }


    public GetCanceledJobsSpectraS3Request withUserId(final String userId) {
        this.userId = userId;
        this.updateQueryParam("user_id", userId);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/canceled_job";
    }
    
    public String getBucketId() {
        return this.bucketId;
    }


    public boolean getCanceledDueToTimeout() {
        return this.canceledDueToTimeout;
    }


    public JobChunkClientProcessingOrderGuarantee getChunkClientProcessingOrderGuarantee() {
        return this.chunkClientProcessingOrderGuarantee;
    }


    public boolean getLastPage() {
        return this.lastPage;
    }


    public String getName() {
        return this.name;
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


    public Date getRechunked() {
        return this.rechunked;
    }


    public JobRequestType getRequestType() {
        return this.requestType;
    }


    public boolean getTruncated() {
        return this.truncated;
    }


    public String getUserId() {
        return this.userId;
    }

}