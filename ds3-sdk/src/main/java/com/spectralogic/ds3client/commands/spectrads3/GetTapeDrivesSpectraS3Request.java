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
import com.spectralogic.ds3client.models.TapeDriveState;
import com.spectralogic.ds3client.models.TapeDriveType;

public class GetTapeDrivesSpectraS3Request extends AbstractPaginationRequest {

    // Variables
    
    private boolean lastPage;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private String partitionId;

    private String serialNumber;

    private TapeDriveState state;

    private TapeDriveType type;

    // Constructor
    
    
    public GetTapeDrivesSpectraS3Request() {
        
    }

    public GetTapeDrivesSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }


    public GetTapeDrivesSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }


    public GetTapeDrivesSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }


    public GetTapeDrivesSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetTapeDrivesSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetTapeDrivesSpectraS3Request withPartitionId(final UUID partitionId) {
        this.partitionId = partitionId.toString();
        this.updateQueryParam("partition_id", partitionId);
        return this;
    }


    public GetTapeDrivesSpectraS3Request withPartitionId(final String partitionId) {
        this.partitionId = partitionId;
        this.updateQueryParam("partition_id", partitionId);
        return this;
    }


    public GetTapeDrivesSpectraS3Request withSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
        this.updateQueryParam("serial_number", serialNumber);
        return this;
    }


    public GetTapeDrivesSpectraS3Request withState(final TapeDriveState state) {
        this.state = state;
        this.updateQueryParam("state", state);
        return this;
    }


    public GetTapeDrivesSpectraS3Request withType(final TapeDriveType type) {
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
        return "/_rest_/tape_drive";
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


    public String getSerialNumber() {
        return this.serialNumber;
    }


    public TapeDriveState getState() {
        return this.state;
    }


    public TapeDriveType getType() {
        return this.type;
    }

}