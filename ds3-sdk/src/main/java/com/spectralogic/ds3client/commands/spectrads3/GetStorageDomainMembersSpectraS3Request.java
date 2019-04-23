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
import java.util.UUID;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.StorageDomainMemberState;
import com.spectralogic.ds3client.models.WritePreferenceLevel;

public class GetStorageDomainMembersSpectraS3Request extends AbstractPaginationRequest {

    // Variables
    
    private boolean lastPage;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private String poolPartitionId;

    private StorageDomainMemberState state;

    private String storageDomainId;

    private String tapePartitionId;

    private String tapeType;

    private WritePreferenceLevel writePreference;

    // Constructor
    
    
    public GetStorageDomainMembersSpectraS3Request() {
        
    }

    public GetStorageDomainMembersSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }


    public GetStorageDomainMembersSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }


    public GetStorageDomainMembersSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }


    public GetStorageDomainMembersSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetStorageDomainMembersSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetStorageDomainMembersSpectraS3Request withPoolPartitionId(final UUID poolPartitionId) {
        this.poolPartitionId = poolPartitionId.toString();
        this.updateQueryParam("pool_partition_id", poolPartitionId);
        return this;
    }


    public GetStorageDomainMembersSpectraS3Request withPoolPartitionId(final String poolPartitionId) {
        this.poolPartitionId = poolPartitionId;
        this.updateQueryParam("pool_partition_id", poolPartitionId);
        return this;
    }


    public GetStorageDomainMembersSpectraS3Request withState(final StorageDomainMemberState state) {
        this.state = state;
        this.updateQueryParam("state", state);
        return this;
    }


    public GetStorageDomainMembersSpectraS3Request withStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId.toString();
        this.updateQueryParam("storage_domain_id", storageDomainId);
        return this;
    }


    public GetStorageDomainMembersSpectraS3Request withStorageDomainId(final String storageDomainId) {
        this.storageDomainId = storageDomainId;
        this.updateQueryParam("storage_domain_id", storageDomainId);
        return this;
    }


    public GetStorageDomainMembersSpectraS3Request withTapePartitionId(final UUID tapePartitionId) {
        this.tapePartitionId = tapePartitionId.toString();
        this.updateQueryParam("tape_partition_id", tapePartitionId);
        return this;
    }


    public GetStorageDomainMembersSpectraS3Request withTapePartitionId(final String tapePartitionId) {
        this.tapePartitionId = tapePartitionId;
        this.updateQueryParam("tape_partition_id", tapePartitionId);
        return this;
    }


    public GetStorageDomainMembersSpectraS3Request withTapeType(final String tapeType) {
        this.tapeType = tapeType;
        this.updateQueryParam("tape_type", tapeType);
        return this;
    }


    public GetStorageDomainMembersSpectraS3Request withWritePreference(final WritePreferenceLevel writePreference) {
        this.writePreference = writePreference;
        this.updateQueryParam("write_preference", writePreference);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/storage_domain_member";
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


    public String getPoolPartitionId() {
        return this.poolPartitionId;
    }


    public StorageDomainMemberState getState() {
        return this.state;
    }


    public String getStorageDomainId() {
        return this.storageDomainId;
    }


    public String getTapePartitionId() {
        return this.tapePartitionId;
    }


    public String getTapeType() {
        return this.tapeType;
    }


    public WritePreferenceLevel getWritePreference() {
        return this.writePreference;
    }

}