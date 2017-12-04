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
import com.spectralogic.ds3client.models.PoolHealth;
import java.util.Date;
import java.util.UUID;
import com.spectralogic.ds3client.models.PoolState;
import com.spectralogic.ds3client.models.PoolType;

public class GetPoolsSpectraS3Request extends AbstractPaginationRequest {

    // Variables
    
    private boolean assignedToStorageDomain;

    private String bucketId;

    private PoolHealth health;

    private boolean lastPage;

    private Date lastVerified;

    private String name;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private String partitionId;

    private boolean poweredOn;

    private PoolState state;

    private String storageDomainId;

    private PoolType type;

    // Constructor
    
    
    public GetPoolsSpectraS3Request() {
        
    }

    public GetPoolsSpectraS3Request withAssignedToStorageDomain(final boolean assignedToStorageDomain) {
        this.assignedToStorageDomain = assignedToStorageDomain;
        this.updateQueryParam("assigned_to_storage_domain", assignedToStorageDomain);
        return this;
    }


    public GetPoolsSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }


    public GetPoolsSpectraS3Request withHealth(final PoolHealth health) {
        this.health = health;
        this.updateQueryParam("health", health);
        return this;
    }


    public GetPoolsSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }


    public GetPoolsSpectraS3Request withLastVerified(final Date lastVerified) {
        this.lastVerified = lastVerified;
        this.updateQueryParam("last_verified", lastVerified);
        return this;
    }


    public GetPoolsSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }


    public GetPoolsSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }


    public GetPoolsSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }


    public GetPoolsSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetPoolsSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetPoolsSpectraS3Request withPartitionId(final UUID partitionId) {
        this.partitionId = partitionId.toString();
        this.updateQueryParam("partition_id", partitionId);
        return this;
    }


    public GetPoolsSpectraS3Request withPartitionId(final String partitionId) {
        this.partitionId = partitionId;
        this.updateQueryParam("partition_id", partitionId);
        return this;
    }


    public GetPoolsSpectraS3Request withPoweredOn(final boolean poweredOn) {
        this.poweredOn = poweredOn;
        this.updateQueryParam("powered_on", poweredOn);
        return this;
    }


    public GetPoolsSpectraS3Request withState(final PoolState state) {
        this.state = state;
        this.updateQueryParam("state", state);
        return this;
    }


    public GetPoolsSpectraS3Request withStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId.toString();
        this.updateQueryParam("storage_domain_id", storageDomainId);
        return this;
    }


    public GetPoolsSpectraS3Request withStorageDomainId(final String storageDomainId) {
        this.storageDomainId = storageDomainId;
        this.updateQueryParam("storage_domain_id", storageDomainId);
        return this;
    }


    public GetPoolsSpectraS3Request withType(final PoolType type) {
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
        return "/_rest_/pool";
    }
    
    public boolean getAssignedToStorageDomain() {
        return this.assignedToStorageDomain;
    }


    public String getBucketId() {
        return this.bucketId;
    }


    public PoolHealth getHealth() {
        return this.health;
    }


    public boolean getLastPage() {
        return this.lastPage;
    }


    public Date getLastVerified() {
        return this.lastVerified;
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


    public String getPartitionId() {
        return this.partitionId;
    }


    public boolean getPoweredOn() {
        return this.poweredOn;
    }


    public PoolState getState() {
        return this.state;
    }


    public String getStorageDomainId() {
        return this.storageDomainId;
    }


    public PoolType getType() {
        return this.type;
    }

}