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
import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationRequest;
import com.google.common.net.UrlEscapers;
import java.util.UUID;
import com.spectralogic.ds3client.models.S3ObjectType;

public class GetObjectsWithFullDetailsSpectraS3Request extends AbstractPaginationRequest {

    // Variables
    
    private String bucketId;

    private long endDate;

    private boolean includePhysicalPlacement;

    private boolean lastPage;

    private boolean latest;

    private String name;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private long startDate;

    private S3ObjectType type;

    // Constructor
    
    
    public GetObjectsWithFullDetailsSpectraS3Request() {
        
        this.getQueryParams().put("full_details", null);
    }

    public GetObjectsWithFullDetailsSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }


    public GetObjectsWithFullDetailsSpectraS3Request withEndDate(final long endDate) {
        this.endDate = endDate;
        this.updateQueryParam("end_date", endDate);
        return this;
    }


    public GetObjectsWithFullDetailsSpectraS3Request withIncludePhysicalPlacement(final boolean includePhysicalPlacement) {
        this.includePhysicalPlacement = includePhysicalPlacement;
        if (this.includePhysicalPlacement) {
            this.getQueryParams().put("include_physical_placement", null);
        } else {
            this.getQueryParams().remove("include_physical_placement");
        }
        return this;
    }


    public GetObjectsWithFullDetailsSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }


    public GetObjectsWithFullDetailsSpectraS3Request withLatest(final boolean latest) {
        this.latest = latest;
        this.updateQueryParam("latest", latest);
        return this;
    }


    public GetObjectsWithFullDetailsSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }


    public GetObjectsWithFullDetailsSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }


    public GetObjectsWithFullDetailsSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }


    public GetObjectsWithFullDetailsSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetObjectsWithFullDetailsSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetObjectsWithFullDetailsSpectraS3Request withStartDate(final long startDate) {
        this.startDate = startDate;
        this.updateQueryParam("start_date", startDate);
        return this;
    }


    public GetObjectsWithFullDetailsSpectraS3Request withType(final S3ObjectType type) {
        this.type = type;
        this.updateQueryParam("type", type);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/object";
    }
    
    public String getBucketId() {
        return this.bucketId;
    }


    public long getEndDate() {
        return this.endDate;
    }


    public boolean getIncludePhysicalPlacement() {
        return this.includePhysicalPlacement;
    }


    public boolean getLastPage() {
        return this.lastPage;
    }


    public boolean getLatest() {
        return this.latest;
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


    public long getStartDate() {
        return this.startDate;
    }


    public S3ObjectType getType() {
        return this.type;
    }

}