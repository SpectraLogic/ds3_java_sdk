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
import com.spectralogic.ds3client.models.PoolHealth;
import java.lang.String;
import com.spectralogic.ds3client.models.PoolState;
import com.spectralogic.ds3client.models.PoolType;

public class GetPoolsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String pool;

    private boolean assignedToStorageDomain;
    private UUID bucketId;
    private PoolHealth health;
    private boolean lastPage;
    private String name;
    private int pageLength;
    private int pageOffset;
    private UUID pageStartMarker;
    private UUID partitionId;
    private boolean poweredOn;
    private PoolState state;
    private UUID storageDomainId;
    private PoolType type;

    // Constructor
    public GetPoolsSpectraS3Request(final String pool) {
        this.pool = pool;
        
    }
    public GetPoolsSpectraS3Request withAssignedToStorageDomain(final boolean assignedToStorageDomain) {
        this.assignedToStorageDomain = assignedToStorageDomain;
        this.updateQueryParam("assigned_to_storage_domain", null);
        return this;
    }

    public GetPoolsSpectraS3Request withBucketId(final UUID bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId.toString());
        return this;
    }

    public GetPoolsSpectraS3Request withHealth(final PoolHealth health) {
        this.health = health;
        this.updateQueryParam("health", health.toString());
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

    public GetPoolsSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }

    public GetPoolsSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", Integer.toString(pageLength));
        return this;
    }

    public GetPoolsSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", Integer.toString(pageOffset));
        return this;
    }

    public GetPoolsSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker.toString());
        return this;
    }

    public GetPoolsSpectraS3Request withPartitionId(final UUID partitionId) {
        this.partitionId = partitionId;
        this.updateQueryParam("partition_id", partitionId.toString());
        return this;
    }

    public GetPoolsSpectraS3Request withPoweredOn(final boolean poweredOn) {
        this.poweredOn = poweredOn;
        this.updateQueryParam("powered_on", null);
        return this;
    }

    public GetPoolsSpectraS3Request withState(final PoolState state) {
        this.state = state;
        this.updateQueryParam("state", state.toString());
        return this;
    }

    public GetPoolsSpectraS3Request withStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId;
        this.updateQueryParam("storage_domain_id", storageDomainId.toString());
        return this;
    }

    public GetPoolsSpectraS3Request withType(final PoolType type) {
        this.type = type;
        this.updateQueryParam("type", type.toString());
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/pool/" + pool;
    }
    
    public String getPool() {
        return this.pool;
    }


    public boolean getAssignedToStorageDomain() {
        return this.assignedToStorageDomain;
    }

    public UUID getBucketId() {
        return this.bucketId;
    }

    public PoolHealth getHealth() {
        return this.health;
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

    public UUID getPageStartMarker() {
        return this.pageStartMarker;
    }

    public UUID getPartitionId() {
        return this.partitionId;
    }

    public boolean getPoweredOn() {
        return this.poweredOn;
    }

    public PoolState getState() {
        return this.state;
    }

    public UUID getStorageDomainId() {
        return this.storageDomainId;
    }

    public PoolType getType() {
        return this.type;
    }

}