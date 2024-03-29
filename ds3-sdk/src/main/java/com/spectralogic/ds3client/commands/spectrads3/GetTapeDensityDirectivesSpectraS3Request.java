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
import com.spectralogic.ds3client.models.TapeDriveType;
import java.util.UUID;

public class GetTapeDensityDirectivesSpectraS3Request extends AbstractPaginationRequest {

    // Variables
    
    private TapeDriveType density;

    private boolean lastPage;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private String partitionId;

    private String tapeType;

    // Constructor
    
    
    public GetTapeDensityDirectivesSpectraS3Request() {
        
    }

    public GetTapeDensityDirectivesSpectraS3Request withDensity(final TapeDriveType density) {
        this.density = density;
        this.updateQueryParam("density", density);
        return this;
    }


    public GetTapeDensityDirectivesSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }


    public GetTapeDensityDirectivesSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }


    public GetTapeDensityDirectivesSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }


    public GetTapeDensityDirectivesSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetTapeDensityDirectivesSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetTapeDensityDirectivesSpectraS3Request withPartitionId(final UUID partitionId) {
        this.partitionId = partitionId.toString();
        this.updateQueryParam("partition_id", partitionId);
        return this;
    }


    public GetTapeDensityDirectivesSpectraS3Request withPartitionId(final String partitionId) {
        this.partitionId = partitionId;
        this.updateQueryParam("partition_id", partitionId);
        return this;
    }


    public GetTapeDensityDirectivesSpectraS3Request withTapeType(final String tapeType) {
        this.tapeType = tapeType;
        this.updateQueryParam("tape_type", tapeType);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/tape_density_directive";
    }
    
    public TapeDriveType getDensity() {
        return this.density;
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


    public String getPartitionId() {
        return this.partitionId;
    }


    public String getTapeType() {
        return this.tapeType;
    }

}