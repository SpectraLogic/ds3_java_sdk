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
import java.lang.Integer;
import com.spectralogic.ds3client.models.AutoInspectMode;
import com.spectralogic.ds3client.models.Priority;
import com.spectralogic.ds3client.models.UnavailableMediaUsagePolicy;

public class ModifyDataPathBackendSpectraS3Request extends AbstractRequest {

    // Variables
    
    private boolean activated;

    private boolean allowNewJobRequests;

    private Integer autoActivateTimeoutInMins;

    private AutoInspectMode autoInspect;

    private int cacheAvailableRetryAfterInSeconds;

    private Priority defaultVerifyDataAfterImport;

    private boolean defaultVerifyDataPriorToImport;

    private double iomCacheLimitationPercent;

    private boolean iomEnabled;

    private int maxAggregatedBlobsPerChunk;

    private int maxNumberOfConcurrentJobs;

    private Integer partiallyVerifyLastPercentOfTapes;

    private boolean poolSafetyEnabled;

    private UnavailableMediaUsagePolicy unavailableMediaPolicy;

    private int unavailablePoolMaxJobRetryInMins;

    private int unavailableTapePartitionMaxJobRetryInMins;

    private boolean verifyCheckpointBeforeRead;

    // Constructor
    
    
    public ModifyDataPathBackendSpectraS3Request() {
        
    }

    public ModifyDataPathBackendSpectraS3Request withActivated(final boolean activated) {
        this.activated = activated;
        this.updateQueryParam("activated", activated);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withAllowNewJobRequests(final boolean allowNewJobRequests) {
        this.allowNewJobRequests = allowNewJobRequests;
        this.updateQueryParam("allow_new_job_requests", allowNewJobRequests);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withAutoActivateTimeoutInMins(final Integer autoActivateTimeoutInMins) {
        this.autoActivateTimeoutInMins = autoActivateTimeoutInMins;
        this.updateQueryParam("auto_activate_timeout_in_mins", autoActivateTimeoutInMins);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withAutoInspect(final AutoInspectMode autoInspect) {
        this.autoInspect = autoInspect;
        this.updateQueryParam("auto_inspect", autoInspect);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withCacheAvailableRetryAfterInSeconds(final int cacheAvailableRetryAfterInSeconds) {
        this.cacheAvailableRetryAfterInSeconds = cacheAvailableRetryAfterInSeconds;
        this.updateQueryParam("cache_available_retry_after_in_seconds", cacheAvailableRetryAfterInSeconds);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withDefaultVerifyDataAfterImport(final Priority defaultVerifyDataAfterImport) {
        this.defaultVerifyDataAfterImport = defaultVerifyDataAfterImport;
        this.updateQueryParam("default_verify_data_after_import", defaultVerifyDataAfterImport);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withDefaultVerifyDataPriorToImport(final boolean defaultVerifyDataPriorToImport) {
        this.defaultVerifyDataPriorToImport = defaultVerifyDataPriorToImport;
        this.updateQueryParam("default_verify_data_prior_to_import", defaultVerifyDataPriorToImport);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withIomCacheLimitationPercent(final double iomCacheLimitationPercent) {
        this.iomCacheLimitationPercent = iomCacheLimitationPercent;
        this.updateQueryParam("iom_cache_limitation_percent", iomCacheLimitationPercent);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withIomEnabled(final boolean iomEnabled) {
        this.iomEnabled = iomEnabled;
        this.updateQueryParam("iom_enabled", iomEnabled);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withMaxAggregatedBlobsPerChunk(final int maxAggregatedBlobsPerChunk) {
        this.maxAggregatedBlobsPerChunk = maxAggregatedBlobsPerChunk;
        this.updateQueryParam("max_aggregated_blobs_per_chunk", maxAggregatedBlobsPerChunk);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withMaxNumberOfConcurrentJobs(final int maxNumberOfConcurrentJobs) {
        this.maxNumberOfConcurrentJobs = maxNumberOfConcurrentJobs;
        this.updateQueryParam("max_number_of_concurrent_jobs", maxNumberOfConcurrentJobs);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withPartiallyVerifyLastPercentOfTapes(final Integer partiallyVerifyLastPercentOfTapes) {
        this.partiallyVerifyLastPercentOfTapes = partiallyVerifyLastPercentOfTapes;
        this.updateQueryParam("partially_verify_last_percent_of_tapes", partiallyVerifyLastPercentOfTapes);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withPoolSafetyEnabled(final boolean poolSafetyEnabled) {
        this.poolSafetyEnabled = poolSafetyEnabled;
        this.updateQueryParam("pool_safety_enabled", poolSafetyEnabled);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withUnavailableMediaPolicy(final UnavailableMediaUsagePolicy unavailableMediaPolicy) {
        this.unavailableMediaPolicy = unavailableMediaPolicy;
        this.updateQueryParam("unavailable_media_policy", unavailableMediaPolicy);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withUnavailablePoolMaxJobRetryInMins(final int unavailablePoolMaxJobRetryInMins) {
        this.unavailablePoolMaxJobRetryInMins = unavailablePoolMaxJobRetryInMins;
        this.updateQueryParam("unavailable_pool_max_job_retry_in_mins", unavailablePoolMaxJobRetryInMins);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withUnavailableTapePartitionMaxJobRetryInMins(final int unavailableTapePartitionMaxJobRetryInMins) {
        this.unavailableTapePartitionMaxJobRetryInMins = unavailableTapePartitionMaxJobRetryInMins;
        this.updateQueryParam("unavailable_tape_partition_max_job_retry_in_mins", unavailableTapePartitionMaxJobRetryInMins);
        return this;
    }


    public ModifyDataPathBackendSpectraS3Request withVerifyCheckpointBeforeRead(final boolean verifyCheckpointBeforeRead) {
        this.verifyCheckpointBeforeRead = verifyCheckpointBeforeRead;
        this.updateQueryParam("verify_checkpoint_before_read", verifyCheckpointBeforeRead);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/data_path_backend";
    }
    
    public boolean getActivated() {
        return this.activated;
    }


    public boolean getAllowNewJobRequests() {
        return this.allowNewJobRequests;
    }


    public Integer getAutoActivateTimeoutInMins() {
        return this.autoActivateTimeoutInMins;
    }


    public AutoInspectMode getAutoInspect() {
        return this.autoInspect;
    }


    public int getCacheAvailableRetryAfterInSeconds() {
        return this.cacheAvailableRetryAfterInSeconds;
    }


    public Priority getDefaultVerifyDataAfterImport() {
        return this.defaultVerifyDataAfterImport;
    }


    public boolean getDefaultVerifyDataPriorToImport() {
        return this.defaultVerifyDataPriorToImport;
    }


    public double getIomCacheLimitationPercent() {
        return this.iomCacheLimitationPercent;
    }


    public boolean getIomEnabled() {
        return this.iomEnabled;
    }


    public int getMaxAggregatedBlobsPerChunk() {
        return this.maxAggregatedBlobsPerChunk;
    }


    public int getMaxNumberOfConcurrentJobs() {
        return this.maxNumberOfConcurrentJobs;
    }


    public Integer getPartiallyVerifyLastPercentOfTapes() {
        return this.partiallyVerifyLastPercentOfTapes;
    }


    public boolean getPoolSafetyEnabled() {
        return this.poolSafetyEnabled;
    }


    public UnavailableMediaUsagePolicy getUnavailableMediaPolicy() {
        return this.unavailableMediaPolicy;
    }


    public int getUnavailablePoolMaxJobRetryInMins() {
        return this.unavailablePoolMaxJobRetryInMins;
    }


    public int getUnavailableTapePartitionMaxJobRetryInMins() {
        return this.unavailableTapePartitionMaxJobRetryInMins;
    }


    public boolean getVerifyCheckpointBeforeRead() {
        return this.verifyCheckpointBeforeRead;
    }

}