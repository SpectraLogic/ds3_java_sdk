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
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.google.common.net.UrlEscapers;
import java.util.UUID;
import com.spectralogic.ds3client.models.PoolHealth;
import com.spectralogic.ds3client.models.PoolState;
import com.spectralogic.ds3client.models.PoolType;
import com.spectralogic.ds3client.models.TapeState;

public class GetBucketCapacitySummarySpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String bucketId;

    private final String storageDomainId;

    private PoolHealth poolHealth;

    private PoolState poolState;

    private PoolType poolType;

    private TapeState tapeState;

    private String tapeType;

    // Constructor
    
    
    public GetBucketCapacitySummarySpectraS3Request(final String bucketId, final UUID storageDomainId) {
        this.bucketId = bucketId;
        this.storageDomainId = storageDomainId.toString();
        
        this.updateQueryParam("bucket_id", bucketId);

        this.updateQueryParam("storage_domain_id", storageDomainId);

    }

    
    public GetBucketCapacitySummarySpectraS3Request(final String bucketId, final String storageDomainId) {
        this.bucketId = bucketId;
        this.storageDomainId = storageDomainId;
        
        this.updateQueryParam("bucket_id", bucketId);

        this.updateQueryParam("storage_domain_id", storageDomainId);

    }

    public GetBucketCapacitySummarySpectraS3Request withPoolHealth(final PoolHealth poolHealth) {
        this.poolHealth = poolHealth;
        this.updateQueryParam("pool_health", poolHealth);
        return this;
    }


    public GetBucketCapacitySummarySpectraS3Request withPoolState(final PoolState poolState) {
        this.poolState = poolState;
        this.updateQueryParam("pool_state", poolState);
        return this;
    }


    public GetBucketCapacitySummarySpectraS3Request withPoolType(final PoolType poolType) {
        this.poolType = poolType;
        this.updateQueryParam("pool_type", poolType);
        return this;
    }


    public GetBucketCapacitySummarySpectraS3Request withTapeState(final TapeState tapeState) {
        this.tapeState = tapeState;
        this.updateQueryParam("tape_state", tapeState);
        return this;
    }


    public GetBucketCapacitySummarySpectraS3Request withTapeType(final String tapeType) {
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
        return "/_rest_/capacity_summary";
    }
    
    public String getBucketId() {
        return this.bucketId;
    }


    public String getStorageDomainId() {
        return this.storageDomainId;
    }


    public PoolHealth getPoolHealth() {
        return this.poolHealth;
    }


    public PoolState getPoolState() {
        return this.poolState;
    }


    public PoolType getPoolType() {
        return this.poolType;
    }


    public TapeState getTapeState() {
        return this.tapeState;
    }


    public String getTapeType() {
        return this.tapeType;
    }

}