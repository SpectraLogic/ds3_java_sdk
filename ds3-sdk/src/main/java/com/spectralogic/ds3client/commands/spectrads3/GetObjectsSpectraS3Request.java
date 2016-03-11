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

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.AbstractRequest;
import com.google.common.net.UrlEscapers;
import java.util.UUID;
import com.spectralogic.ds3client.models.S3ObjectType;

public class GetObjectsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private String bucketId;

    private String folder;

    private boolean includePhysicalPlacement;

    private boolean lastPage;

    private boolean latest;

    private String name;

    private int pageLength;

    private int pageOffset;

    private UUID pageStartMarker;

    private S3ObjectType type;

    private long version;

    // Constructor
    
    public GetObjectsSpectraS3Request() {
            }

    public GetObjectsSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }

    public GetObjectsSpectraS3Request withFolder(final String folder) {
        this.folder = folder;
        this.updateQueryParam("folder", UrlEscapers.urlFragmentEscaper().escape(folder).replace("+", "%2B"));
        return this;
    }

    public GetObjectsSpectraS3Request withIncludePhysicalPlacement(final boolean includePhysicalPlacement) {
        this.includePhysicalPlacement = includePhysicalPlacement;
        if (this.includePhysicalPlacement) {
            this.getQueryParams().put("include_physical_placement", null);
        } else {
            this.getQueryParams().remove("include_physical_placement");
        }
        return this;
    }

    public GetObjectsSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }

    public GetObjectsSpectraS3Request withLatest(final boolean latest) {
        this.latest = latest;
        this.updateQueryParam("latest", String.valueOf(latest));
        return this;
    }

    public GetObjectsSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", UrlEscapers.urlFragmentEscaper().escape(name).replace("+", "%2B"));
        return this;
    }

    public GetObjectsSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", Integer.toString(pageLength));
        return this;
    }

    public GetObjectsSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", Integer.toString(pageOffset));
        return this;
    }

    public GetObjectsSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker.toString());
        return this;
    }

    public GetObjectsSpectraS3Request withType(final S3ObjectType type) {
        this.type = type;
        this.updateQueryParam("type", type.toString());
        return this;
    }

    public GetObjectsSpectraS3Request withVersion(final long version) {
        this.version = version;
        this.updateQueryParam("version", Long.toString(version));
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


    public String getFolder() {
        return this.folder;
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


    public UUID getPageStartMarker() {
        return this.pageStartMarker;
    }


    public S3ObjectType getType() {
        return this.type;
    }


    public long getVersion() {
        return this.version;
    }

}