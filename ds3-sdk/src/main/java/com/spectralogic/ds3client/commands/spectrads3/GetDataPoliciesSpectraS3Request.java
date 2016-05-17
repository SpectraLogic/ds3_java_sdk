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
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.spectralogic.ds3client.models.ChecksumType;
import com.google.common.net.UrlEscapers;
import java.util.UUID;

public class GetDataPoliciesSpectraS3Request extends AbstractRequest {

    // Variables
    
    private boolean alwaysForcePutJobCreation;

    private boolean alwaysMinimizeSpanningAcrossMedia;

    private boolean alwaysReplicateDeletes;

    private ChecksumType.Type checksumType;

    private boolean endToEndCrcRequired;

    private boolean lastPage;

    private String name;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    // Constructor
    
    public GetDataPoliciesSpectraS3Request() {
        
    }

    public GetDataPoliciesSpectraS3Request withAlwaysForcePutJobCreation(final boolean alwaysForcePutJobCreation) {
        this.alwaysForcePutJobCreation = alwaysForcePutJobCreation;
        this.updateQueryParam("always_force_put_job_creation", alwaysForcePutJobCreation);
        return this;
    }

    public GetDataPoliciesSpectraS3Request withAlwaysMinimizeSpanningAcrossMedia(final boolean alwaysMinimizeSpanningAcrossMedia) {
        this.alwaysMinimizeSpanningAcrossMedia = alwaysMinimizeSpanningAcrossMedia;
        this.updateQueryParam("always_minimize_spanning_across_media", alwaysMinimizeSpanningAcrossMedia);
        return this;
    }

    public GetDataPoliciesSpectraS3Request withAlwaysReplicateDeletes(final boolean alwaysReplicateDeletes) {
        this.alwaysReplicateDeletes = alwaysReplicateDeletes;
        this.updateQueryParam("always_replicate_deletes", alwaysReplicateDeletes);
        return this;
    }

    public GetDataPoliciesSpectraS3Request withChecksumType(final ChecksumType.Type checksumType) {
        this.checksumType = checksumType;
        this.updateQueryParam("checksum_type", checksumType);
        return this;
    }

    public GetDataPoliciesSpectraS3Request withEndToEndCrcRequired(final boolean endToEndCrcRequired) {
        this.endToEndCrcRequired = endToEndCrcRequired;
        this.updateQueryParam("end_to_end_crc_required", endToEndCrcRequired);
        return this;
    }

    public GetDataPoliciesSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }

    public GetDataPoliciesSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }

    public GetDataPoliciesSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }

    public GetDataPoliciesSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }

    public GetDataPoliciesSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }

    public GetDataPoliciesSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/data_policy";
    }
    
    public boolean getAlwaysForcePutJobCreation() {
        return this.alwaysForcePutJobCreation;
    }


    public boolean getAlwaysMinimizeSpanningAcrossMedia() {
        return this.alwaysMinimizeSpanningAcrossMedia;
    }


    public boolean getAlwaysReplicateDeletes() {
        return this.alwaysReplicateDeletes;
    }


    public ChecksumType.Type getChecksumType() {
        return this.checksumType;
    }


    public boolean getEndToEndCrcRequired() {
        return this.endToEndCrcRequired;
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

}