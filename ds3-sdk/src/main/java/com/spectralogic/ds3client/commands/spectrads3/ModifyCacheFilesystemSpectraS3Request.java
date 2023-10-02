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
import java.lang.Long;

public class ModifyCacheFilesystemSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String cacheFilesystem;

    private double autoReclaimInitiateThreshold;

    private double autoReclaimTerminateThreshold;

    private double burstThreshold;

    private boolean cacheSafetyEnabled;

    private Long maxCapacityInBytes;

    private boolean needsReconcile;

    // Constructor
    
    
    public ModifyCacheFilesystemSpectraS3Request(final String cacheFilesystem) {
        this.cacheFilesystem = cacheFilesystem;
        
    }

    public ModifyCacheFilesystemSpectraS3Request withAutoReclaimInitiateThreshold(final double autoReclaimInitiateThreshold) {
        this.autoReclaimInitiateThreshold = autoReclaimInitiateThreshold;
        this.updateQueryParam("auto_reclaim_initiate_threshold", autoReclaimInitiateThreshold);
        return this;
    }


    public ModifyCacheFilesystemSpectraS3Request withAutoReclaimTerminateThreshold(final double autoReclaimTerminateThreshold) {
        this.autoReclaimTerminateThreshold = autoReclaimTerminateThreshold;
        this.updateQueryParam("auto_reclaim_terminate_threshold", autoReclaimTerminateThreshold);
        return this;
    }


    public ModifyCacheFilesystemSpectraS3Request withBurstThreshold(final double burstThreshold) {
        this.burstThreshold = burstThreshold;
        this.updateQueryParam("burst_threshold", burstThreshold);
        return this;
    }


    public ModifyCacheFilesystemSpectraS3Request withCacheSafetyEnabled(final boolean cacheSafetyEnabled) {
        this.cacheSafetyEnabled = cacheSafetyEnabled;
        this.updateQueryParam("cache_safety_enabled", cacheSafetyEnabled);
        return this;
    }


    public ModifyCacheFilesystemSpectraS3Request withMaxCapacityInBytes(final Long maxCapacityInBytes) {
        this.maxCapacityInBytes = maxCapacityInBytes;
        this.updateQueryParam("max_capacity_in_bytes", maxCapacityInBytes);
        return this;
    }


    public ModifyCacheFilesystemSpectraS3Request withNeedsReconcile(final boolean needsReconcile) {
        this.needsReconcile = needsReconcile;
        this.updateQueryParam("needs_reconcile", needsReconcile);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/cache_filesystem/" + cacheFilesystem;
    }
    
    public String getCacheFilesystem() {
        return this.cacheFilesystem;
    }


    public double getAutoReclaimInitiateThreshold() {
        return this.autoReclaimInitiateThreshold;
    }


    public double getAutoReclaimTerminateThreshold() {
        return this.autoReclaimTerminateThreshold;
    }


    public double getBurstThreshold() {
        return this.burstThreshold;
    }


    public boolean getCacheSafetyEnabled() {
        return this.cacheSafetyEnabled;
    }


    public Long getMaxCapacityInBytes() {
        return this.maxCapacityInBytes;
    }


    public boolean getNeedsReconcile() {
        return this.needsReconcile;
    }

}