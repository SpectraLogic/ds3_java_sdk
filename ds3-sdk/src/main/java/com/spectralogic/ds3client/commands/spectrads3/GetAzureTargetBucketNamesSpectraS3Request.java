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
import com.google.common.net.UrlEscapers;
import java.util.UUID;

public class GetAzureTargetBucketNamesSpectraS3Request extends AbstractPaginationRequest {

    // Variables
    
    private String bucketId;

    private boolean lastPage;

    private String name;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private String targetId;

    // Constructor
    
    
    public GetAzureTargetBucketNamesSpectraS3Request() {
        
    }

    public GetAzureTargetBucketNamesSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }


    public GetAzureTargetBucketNamesSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }


    public GetAzureTargetBucketNamesSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }


    public GetAzureTargetBucketNamesSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }


    public GetAzureTargetBucketNamesSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }


    public GetAzureTargetBucketNamesSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetAzureTargetBucketNamesSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetAzureTargetBucketNamesSpectraS3Request withTargetId(final UUID targetId) {
        this.targetId = targetId.toString();
        this.updateQueryParam("target_id", targetId);
        return this;
    }


    public GetAzureTargetBucketNamesSpectraS3Request withTargetId(final String targetId) {
        this.targetId = targetId;
        this.updateQueryParam("target_id", targetId);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/azure_target_bucket_name";
    }
    
    public String getBucketId() {
        return this.bucketId;
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


    public String getTargetId() {
        return this.targetId;
    }

}