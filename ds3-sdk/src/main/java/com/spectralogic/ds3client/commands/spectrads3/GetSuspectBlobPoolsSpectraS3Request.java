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
import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationRequest;
import java.util.UUID;
import com.google.common.net.UrlEscapers;

public class GetSuspectBlobPoolsSpectraS3Request extends AbstractPaginationRequest {

    // Variables
    
    private String blobId;

    private boolean lastPage;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private String poolId;

    // Constructor
    
    
    public GetSuspectBlobPoolsSpectraS3Request() {
        
    }

    public GetSuspectBlobPoolsSpectraS3Request withBlobId(final UUID blobId) {
        this.blobId = blobId.toString();
        this.updateQueryParam("blob_id", blobId);
        return this;
    }


    public GetSuspectBlobPoolsSpectraS3Request withBlobId(final String blobId) {
        this.blobId = blobId;
        this.updateQueryParam("blob_id", blobId);
        return this;
    }


    public GetSuspectBlobPoolsSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }


    public GetSuspectBlobPoolsSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }


    public GetSuspectBlobPoolsSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }


    public GetSuspectBlobPoolsSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetSuspectBlobPoolsSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetSuspectBlobPoolsSpectraS3Request withPoolId(final UUID poolId) {
        this.poolId = poolId.toString();
        this.updateQueryParam("pool_id", poolId);
        return this;
    }


    public GetSuspectBlobPoolsSpectraS3Request withPoolId(final String poolId) {
        this.poolId = poolId;
        this.updateQueryParam("pool_id", poolId);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/suspect_blob_pool";
    }
    
    public String getBlobId() {
        return this.blobId;
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


    public String getPageStartMarker() {
        return this.pageStartMarker;
    }


    public String getPoolId() {
        return this.poolId;
    }

}