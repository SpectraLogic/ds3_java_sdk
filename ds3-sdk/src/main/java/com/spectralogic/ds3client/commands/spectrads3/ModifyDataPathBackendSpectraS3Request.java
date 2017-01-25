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
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import java.lang.Integer;
import com.spectralogic.ds3client.models.AutoInspectMode;
import com.spectralogic.ds3client.models.ImportConflictResolutionMode;
import com.spectralogic.ds3client.models.Priority;
import com.spectralogic.ds3client.models.UnavailableMediaUsagePolicy;

public class ModifyDataPathBackendSpectraS3Request extends AbstractRequest {

    // Variables
    
    private boolean activated;

    private Integer autoActivateTimeoutInMins;

    private AutoInspectMode autoInspect;

    private ImportConflictResolutionMode defaultImportConflictResolutionMode;

    private Priority defaultVerifyDataAfterImport;

    private boolean defaultVerifyDataPriorToImport;

    private Integer partiallyVerifyLastPercentOfTapes;

    private UnavailableMediaUsagePolicy unavailableMediaPolicy;

    private int unavailablePoolMaxJobRetryInMins;

    private int unavailableTapePartitionMaxJobRetryInMins;

    // Constructor
    
    
    public ModifyDataPathBackendSpectraS3Request() {
        
    }

    public ModifyDataPathBackendSpectraS3Request withActivated(final boolean activated) {
        this.activated = activated;
        this.updateQueryParam("activated", activated);
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


    public ModifyDataPathBackendSpectraS3Request withDefaultImportConflictResolutionMode(final ImportConflictResolutionMode defaultImportConflictResolutionMode) {
        this.defaultImportConflictResolutionMode = defaultImportConflictResolutionMode;
        this.updateQueryParam("default_import_conflict_resolution_mode", defaultImportConflictResolutionMode);
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


    public ModifyDataPathBackendSpectraS3Request withPartiallyVerifyLastPercentOfTapes(final Integer partiallyVerifyLastPercentOfTapes) {
        this.partiallyVerifyLastPercentOfTapes = partiallyVerifyLastPercentOfTapes;
        this.updateQueryParam("partially_verify_last_percent_of_tapes", partiallyVerifyLastPercentOfTapes);
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


    public Integer getAutoActivateTimeoutInMins() {
        return this.autoActivateTimeoutInMins;
    }


    public AutoInspectMode getAutoInspect() {
        return this.autoInspect;
    }


    public ImportConflictResolutionMode getDefaultImportConflictResolutionMode() {
        return this.defaultImportConflictResolutionMode;
    }


    public Priority getDefaultVerifyDataAfterImport() {
        return this.defaultVerifyDataAfterImport;
    }


    public boolean getDefaultVerifyDataPriorToImport() {
        return this.defaultVerifyDataPriorToImport;
    }


    public Integer getPartiallyVerifyLastPercentOfTapes() {
        return this.partiallyVerifyLastPercentOfTapes;
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

}