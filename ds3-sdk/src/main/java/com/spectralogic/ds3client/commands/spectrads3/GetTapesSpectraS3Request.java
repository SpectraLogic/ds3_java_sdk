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
import java.lang.Long;
import com.google.common.net.UrlEscapers;
import java.util.Date;
import java.util.UUID;
import com.spectralogic.ds3client.models.TapeState;
import com.spectralogic.ds3client.models.TapeType;
import com.spectralogic.ds3client.models.Priority;

public class GetTapesSpectraS3Request extends AbstractPaginationRequest {

    // Variables
    
    private boolean assignedToStorageDomain;

    private Long availableRawCapacity;

    private String barCode;

    private String bucketId;

    private String ejectLabel;

    private String ejectLocation;

    private boolean fullOfData;

    private boolean lastPage;

    private Date lastVerified;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private Date partiallyVerifiedEndOfTape;

    private String partitionId;

    private TapeState previousState;

    private String serialNumber;

    private String sortBy;

    private TapeState state;

    private String storageDomainId;

    private TapeType type;

    private Priority verifyPending;

    private boolean writeProtected;

    // Constructor
    
    
    public GetTapesSpectraS3Request() {
        
    }

    public GetTapesSpectraS3Request withAssignedToStorageDomain(final boolean assignedToStorageDomain) {
        this.assignedToStorageDomain = assignedToStorageDomain;
        this.updateQueryParam("assigned_to_storage_domain", assignedToStorageDomain);
        return this;
    }


    public GetTapesSpectraS3Request withAvailableRawCapacity(final Long availableRawCapacity) {
        this.availableRawCapacity = availableRawCapacity;
        this.updateQueryParam("available_raw_capacity", availableRawCapacity);
        return this;
    }


    public GetTapesSpectraS3Request withBarCode(final String barCode) {
        this.barCode = barCode;
        this.updateQueryParam("bar_code", barCode);
        return this;
    }


    public GetTapesSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }


    public GetTapesSpectraS3Request withEjectLabel(final String ejectLabel) {
        this.ejectLabel = ejectLabel;
        this.updateQueryParam("eject_label", ejectLabel);
        return this;
    }


    public GetTapesSpectraS3Request withEjectLocation(final String ejectLocation) {
        this.ejectLocation = ejectLocation;
        this.updateQueryParam("eject_location", ejectLocation);
        return this;
    }


    public GetTapesSpectraS3Request withFullOfData(final boolean fullOfData) {
        this.fullOfData = fullOfData;
        this.updateQueryParam("full_of_data", fullOfData);
        return this;
    }


    public GetTapesSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }


    public GetTapesSpectraS3Request withLastVerified(final Date lastVerified) {
        this.lastVerified = lastVerified;
        this.updateQueryParam("last_verified", lastVerified);
        return this;
    }


    public GetTapesSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }


    public GetTapesSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }


    public GetTapesSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetTapesSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetTapesSpectraS3Request withPartiallyVerifiedEndOfTape(final Date partiallyVerifiedEndOfTape) {
        this.partiallyVerifiedEndOfTape = partiallyVerifiedEndOfTape;
        this.updateQueryParam("partially_verified_end_of_tape", partiallyVerifiedEndOfTape);
        return this;
    }


    public GetTapesSpectraS3Request withPartitionId(final UUID partitionId) {
        this.partitionId = partitionId.toString();
        this.updateQueryParam("partition_id", partitionId);
        return this;
    }


    public GetTapesSpectraS3Request withPartitionId(final String partitionId) {
        this.partitionId = partitionId;
        this.updateQueryParam("partition_id", partitionId);
        return this;
    }


    public GetTapesSpectraS3Request withPreviousState(final TapeState previousState) {
        this.previousState = previousState;
        this.updateQueryParam("previous_state", previousState);
        return this;
    }


    public GetTapesSpectraS3Request withSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
        this.updateQueryParam("serial_number", serialNumber);
        return this;
    }


    public GetTapesSpectraS3Request withSortBy(final String sortBy) {
        this.sortBy = sortBy;
        this.updateQueryParam("sort_by", sortBy);
        return this;
    }


    public GetTapesSpectraS3Request withState(final TapeState state) {
        this.state = state;
        this.updateQueryParam("state", state);
        return this;
    }


    public GetTapesSpectraS3Request withStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId.toString();
        this.updateQueryParam("storage_domain_id", storageDomainId);
        return this;
    }


    public GetTapesSpectraS3Request withStorageDomainId(final String storageDomainId) {
        this.storageDomainId = storageDomainId;
        this.updateQueryParam("storage_domain_id", storageDomainId);
        return this;
    }


    public GetTapesSpectraS3Request withType(final TapeType type) {
        this.type = type;
        this.updateQueryParam("type", type);
        return this;
    }


    public GetTapesSpectraS3Request withVerifyPending(final Priority verifyPending) {
        this.verifyPending = verifyPending;
        this.updateQueryParam("verify_pending", verifyPending);
        return this;
    }


    public GetTapesSpectraS3Request withWriteProtected(final boolean writeProtected) {
        this.writeProtected = writeProtected;
        this.updateQueryParam("write_protected", writeProtected);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/tape";
    }
    
    public boolean getAssignedToStorageDomain() {
        return this.assignedToStorageDomain;
    }


    public Long getAvailableRawCapacity() {
        return this.availableRawCapacity;
    }


    public String getBarCode() {
        return this.barCode;
    }


    public String getBucketId() {
        return this.bucketId;
    }


    public String getEjectLabel() {
        return this.ejectLabel;
    }


    public String getEjectLocation() {
        return this.ejectLocation;
    }


    public boolean getFullOfData() {
        return this.fullOfData;
    }


    public boolean getLastPage() {
        return this.lastPage;
    }


    public Date getLastVerified() {
        return this.lastVerified;
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


    public Date getPartiallyVerifiedEndOfTape() {
        return this.partiallyVerifiedEndOfTape;
    }


    public String getPartitionId() {
        return this.partitionId;
    }


    public TapeState getPreviousState() {
        return this.previousState;
    }


    public String getSerialNumber() {
        return this.serialNumber;
    }


    public String getSortBy() {
        return this.sortBy;
    }


    public TapeState getState() {
        return this.state;
    }


    public String getStorageDomainId() {
        return this.storageDomainId;
    }


    public TapeType getType() {
        return this.type;
    }


    public Priority getVerifyPending() {
        return this.verifyPending;
    }


    public boolean getWriteProtected() {
        return this.writeProtected;
    }

}