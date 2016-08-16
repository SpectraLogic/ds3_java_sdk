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

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.google.common.net.UrlEscapers;
import java.lang.Long;
import com.spectralogic.ds3client.models.LtfsFileNamingMode;
import java.lang.Integer;
import com.spectralogic.ds3client.models.Priority;
import com.spectralogic.ds3client.models.WriteOptimization;

public class PutStorageDomainSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String name;

    private Long autoEjectMediaFullThreshold;

    private String autoEjectUponCron;

    private boolean autoEjectUponJobCancellation;

    private boolean autoEjectUponJobCompletion;

    private boolean autoEjectUponMediaFull;

    private LtfsFileNamingMode ltfsFileNaming;

    private int maxTapeFragmentationPercent;

    private Integer maximumAutoVerificationFrequencyInDays;

    private boolean mediaEjectionAllowed;

    private boolean secureMediaAllocation;

    private Priority verifyPriorToAutoEject;

    private WriteOptimization writeOptimization;

    // Constructor
    
    public PutStorageDomainSpectraS3Request(final String name) {
        this.name = name;
        
        this.getQueryParams().put("name", UrlEscapers.urlFragmentEscaper().escape(name).replace("+", "%2B"));
    }

    public PutStorageDomainSpectraS3Request withAutoEjectMediaFullThreshold(final Long autoEjectMediaFullThreshold) {
        this.autoEjectMediaFullThreshold = autoEjectMediaFullThreshold;
        this.updateQueryParam("auto_eject_media_full_threshold", autoEjectMediaFullThreshold);
        return this;
    }

    public PutStorageDomainSpectraS3Request withAutoEjectUponCron(final String autoEjectUponCron) {
        this.autoEjectUponCron = autoEjectUponCron;
        this.updateQueryParam("auto_eject_upon_cron", autoEjectUponCron);
        return this;
    }

    public PutStorageDomainSpectraS3Request withAutoEjectUponJobCancellation(final boolean autoEjectUponJobCancellation) {
        this.autoEjectUponJobCancellation = autoEjectUponJobCancellation;
        this.updateQueryParam("auto_eject_upon_job_cancellation", autoEjectUponJobCancellation);
        return this;
    }

    public PutStorageDomainSpectraS3Request withAutoEjectUponJobCompletion(final boolean autoEjectUponJobCompletion) {
        this.autoEjectUponJobCompletion = autoEjectUponJobCompletion;
        this.updateQueryParam("auto_eject_upon_job_completion", autoEjectUponJobCompletion);
        return this;
    }

    public PutStorageDomainSpectraS3Request withAutoEjectUponMediaFull(final boolean autoEjectUponMediaFull) {
        this.autoEjectUponMediaFull = autoEjectUponMediaFull;
        this.updateQueryParam("auto_eject_upon_media_full", autoEjectUponMediaFull);
        return this;
    }

    public PutStorageDomainSpectraS3Request withLtfsFileNaming(final LtfsFileNamingMode ltfsFileNaming) {
        this.ltfsFileNaming = ltfsFileNaming;
        this.updateQueryParam("ltfs_file_naming", ltfsFileNaming);
        return this;
    }

    public PutStorageDomainSpectraS3Request withMaxTapeFragmentationPercent(final int maxTapeFragmentationPercent) {
        this.maxTapeFragmentationPercent = maxTapeFragmentationPercent;
        this.updateQueryParam("max_tape_fragmentation_percent", maxTapeFragmentationPercent);
        return this;
    }

    public PutStorageDomainSpectraS3Request withMaximumAutoVerificationFrequencyInDays(final Integer maximumAutoVerificationFrequencyInDays) {
        this.maximumAutoVerificationFrequencyInDays = maximumAutoVerificationFrequencyInDays;
        this.updateQueryParam("maximum_auto_verification_frequency_in_days", maximumAutoVerificationFrequencyInDays);
        return this;
    }

    public PutStorageDomainSpectraS3Request withMediaEjectionAllowed(final boolean mediaEjectionAllowed) {
        this.mediaEjectionAllowed = mediaEjectionAllowed;
        this.updateQueryParam("media_ejection_allowed", mediaEjectionAllowed);
        return this;
    }

    public PutStorageDomainSpectraS3Request withSecureMediaAllocation(final boolean secureMediaAllocation) {
        this.secureMediaAllocation = secureMediaAllocation;
        this.updateQueryParam("secure_media_allocation", secureMediaAllocation);
        return this;
    }

    public PutStorageDomainSpectraS3Request withVerifyPriorToAutoEject(final Priority verifyPriorToAutoEject) {
        this.verifyPriorToAutoEject = verifyPriorToAutoEject;
        this.updateQueryParam("verify_prior_to_auto_eject", verifyPriorToAutoEject);
        return this;
    }

    public PutStorageDomainSpectraS3Request withWriteOptimization(final WriteOptimization writeOptimization) {
        this.writeOptimization = writeOptimization;
        this.updateQueryParam("write_optimization", writeOptimization);
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/storage_domain";
    }
    
    public String getName() {
        return this.name;
    }


    public Long getAutoEjectMediaFullThreshold() {
        return this.autoEjectMediaFullThreshold;
    }


    public String getAutoEjectUponCron() {
        return this.autoEjectUponCron;
    }


    public boolean getAutoEjectUponJobCancellation() {
        return this.autoEjectUponJobCancellation;
    }


    public boolean getAutoEjectUponJobCompletion() {
        return this.autoEjectUponJobCompletion;
    }


    public boolean getAutoEjectUponMediaFull() {
        return this.autoEjectUponMediaFull;
    }


    public LtfsFileNamingMode getLtfsFileNaming() {
        return this.ltfsFileNaming;
    }


    public int getMaxTapeFragmentationPercent() {
        return this.maxTapeFragmentationPercent;
    }


    public Integer getMaximumAutoVerificationFrequencyInDays() {
        return this.maximumAutoVerificationFrequencyInDays;
    }


    public boolean getMediaEjectionAllowed() {
        return this.mediaEjectionAllowed;
    }


    public boolean getSecureMediaAllocation() {
        return this.secureMediaAllocation;
    }


    public Priority getVerifyPriorToAutoEject() {
        return this.verifyPriorToAutoEject;
    }


    public WriteOptimization getWriteOptimization() {
        return this.writeOptimization;
    }

}