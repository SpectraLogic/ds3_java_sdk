/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

import com.spectralogic.ds3client.commands.AbstractRequest;
import com.spectralogic.ds3client.HttpVerb;
import java.util.UUID;
import com.spectralogic.ds3client.models.JobChunkClientProcessingOrderGuarantee;
import com.spectralogic.ds3client.models.BlobStoreTaskPriority;
import java.util.Date;
import com.spectralogic.ds3client.models.JobRequestType;

public class GetActiveJobsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final UUID activeJobId;

    private UUID bucketId;
    private JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee;
    private boolean lastPage;
    private int pageLength;
    private int pageOffset;
    private UUID pageStartMarker;
    private BlobStoreTaskPriority priority;
    private Date rechunked;
    private JobRequestType requestType;
    private boolean reshapable;
    private boolean truncated;
    private UUID userId;

    // Constructor
    public GetActiveJobsSpectraS3Request(final UUID activeJobId) {
        this.activeJobId = activeJobId;
        
    }
    public GetActiveJobsSpectraS3Request withBucketId(final UUID bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId.toString());
        return this;
    }

    public GetActiveJobsSpectraS3Request withChunkClientProcessingOrderGuarantee(final JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee) {
        this.chunkClientProcessingOrderGuarantee = chunkClientProcessingOrderGuarantee;
        this.updateQueryParam("chunk_client_processing_order_guarantee", chunkClientProcessingOrderGuarantee.toString());
        return this;
    }

    public GetActiveJobsSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }

    public GetActiveJobsSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", Integer.toString(pageLength));
        return this;
    }

    public GetActiveJobsSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", Integer.toString(pageOffset));
        return this;
    }

    public GetActiveJobsSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker.toString());
        return this;
    }

    public GetActiveJobsSpectraS3Request withPriority(final BlobStoreTaskPriority priority) {
        this.priority = priority;
        this.updateQueryParam("priority", priority.toString());
        return this;
    }

    public GetActiveJobsSpectraS3Request withRechunked(final Date rechunked) {
        this.rechunked = rechunked;
        this.updateQueryParam("rechunked", rechunked.toString());
        return this;
    }

    public GetActiveJobsSpectraS3Request withRequestType(final JobRequestType requestType) {
        this.requestType = requestType;
        this.updateQueryParam("request_type", requestType.toString());
        return this;
    }

    public GetActiveJobsSpectraS3Request withReshapable(final boolean reshapable) {
        this.reshapable = reshapable;
        this.updateQueryParam("reshapable", null);
        return this;
    }

    public GetActiveJobsSpectraS3Request withTruncated(final boolean truncated) {
        this.truncated = truncated;
        this.updateQueryParam("truncated", null);
        return this;
    }

    public GetActiveJobsSpectraS3Request withUserId(final UUID userId) {
        this.userId = userId;
        this.updateQueryParam("user_id", userId.toString());
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/active_job/" + activeJobId.toString();
    }
    
    public UUID getActiveJobId() {
        return this.activeJobId;
    }


    public UUID getBucketId() {
        return this.bucketId;
    }

    public JobChunkClientProcessingOrderGuarantee getChunkClientProcessingOrderGuarantee() {
        return this.chunkClientProcessingOrderGuarantee;
    }

    public boolean getLastPage() {
        return this.lastPage;
    }

    public int getPageLength() {
        return this.pageLength;
    }

    public int getPageOffset() {
        return this.pageOffset;
    }

    public UUID getPageStartMarker() {
        return this.pageStartMarker;
    }

    public BlobStoreTaskPriority getPriority() {
        return this.priority;
    }

    public Date getRechunked() {
        return this.rechunked;
    }

    public JobRequestType getRequestType() {
        return this.requestType;
    }

    public boolean getReshapable() {
        return this.reshapable;
    }

    public boolean getTruncated() {
        return this.truncated;
    }

    public UUID getUserId() {
        return this.userId;
    }

}