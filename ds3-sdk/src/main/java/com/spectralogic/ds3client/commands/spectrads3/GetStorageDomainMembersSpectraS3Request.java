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
import com.spectralogic.ds3client.models.StorageDomainMemberState;
import com.spectralogic.ds3client.models.TapeType;
import com.spectralogic.ds3client.models.WritePreferenceLevel;

public class GetStorageDomainMembersSpectraS3Request extends AbstractRequest {

    // Variables
    
    private boolean lastPage;
    private int pageLength;
    private int pageOffset;
    private UUID pageStartMarker;
    private UUID poolPartitionId;
    private StorageDomainMemberState state;
    private UUID storageDomainId;
    private UUID tapePartitionId;
    private TapeType tapeType;
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
        this.updateQueryParam("page_length", Integer.toString(pageLength));
        return this;
    }

    public GetStorageDomainMembersSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", Integer.toString(pageOffset));
        return this;
    }

    public GetStorageDomainMembersSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker.toString());
        return this;
    }

    public GetStorageDomainMembersSpectraS3Request withPoolPartitionId(final UUID poolPartitionId) {
        this.poolPartitionId = poolPartitionId;
        this.updateQueryParam("pool_partition_id", poolPartitionId.toString());
        return this;
    }

    public GetStorageDomainMembersSpectraS3Request withState(final StorageDomainMemberState state) {
        this.state = state;
        this.updateQueryParam("state", state.toString());
        return this;
    }

    public GetStorageDomainMembersSpectraS3Request withStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId;
        this.updateQueryParam("storage_domain_id", storageDomainId.toString());
        return this;
    }

    public GetStorageDomainMembersSpectraS3Request withTapePartitionId(final UUID tapePartitionId) {
        this.tapePartitionId = tapePartitionId;
        this.updateQueryParam("tape_partition_id", tapePartitionId.toString());
        return this;
    }

    public GetStorageDomainMembersSpectraS3Request withTapeType(final TapeType tapeType) {
        this.tapeType = tapeType;
        this.updateQueryParam("tape_type", tapeType.toString());
        return this;
    }

    public GetStorageDomainMembersSpectraS3Request withWritePreference(final WritePreferenceLevel writePreference) {
        this.writePreference = writePreference;
        this.updateQueryParam("write_preference", writePreference.toString());
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

    public UUID getPageStartMarker() {
        return this.pageStartMarker;
    }

    public UUID getPoolPartitionId() {
        return this.poolPartitionId;
    }

    public StorageDomainMemberState getState() {
        return this.state;
    }

    public UUID getStorageDomainId() {
        return this.storageDomainId;
    }

    public UUID getTapePartitionId() {
        return this.tapePartitionId;
    }

    public TapeType getTapeType() {
        return this.tapeType;
    }

    public WritePreferenceLevel getWritePreference() {
        return this.writePreference;
    }

}