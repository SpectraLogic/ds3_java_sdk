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
import com.spectralogic.ds3client.commands.AbstractRequest;
import com.google.common.net.UrlEscapers;
import java.util.UUID;
import com.spectralogic.ds3client.models.WriteOptimization;

public class GetStorageDomainsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private String autoEjectUponCron;

    private boolean autoEjectUponJobCancellation;

    private boolean autoEjectUponJobCompletion;

    private boolean autoEjectUponMediaFull;

    private boolean lastPage;

    private boolean mediaEjectionAllowed;

    private String name;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private WriteOptimization writeOptimization;

    // Constructor
    
    public GetStorageDomainsSpectraS3Request() {
        
    }

    public GetStorageDomainsSpectraS3Request withAutoEjectUponCron(final String autoEjectUponCron) {
        this.autoEjectUponCron = autoEjectUponCron;
        this.updateQueryParam("auto_eject_upon_cron", autoEjectUponCron);
        return this;
    }

    public GetStorageDomainsSpectraS3Request withAutoEjectUponJobCancellation(final boolean autoEjectUponJobCancellation) {
        this.autoEjectUponJobCancellation = autoEjectUponJobCancellation;
        this.updateQueryParam("auto_eject_upon_job_cancellation", autoEjectUponJobCancellation);
        return this;
    }

    public GetStorageDomainsSpectraS3Request withAutoEjectUponJobCompletion(final boolean autoEjectUponJobCompletion) {
        this.autoEjectUponJobCompletion = autoEjectUponJobCompletion;
        this.updateQueryParam("auto_eject_upon_job_completion", autoEjectUponJobCompletion);
        return this;
    }

    public GetStorageDomainsSpectraS3Request withAutoEjectUponMediaFull(final boolean autoEjectUponMediaFull) {
        this.autoEjectUponMediaFull = autoEjectUponMediaFull;
        this.updateQueryParam("auto_eject_upon_media_full", autoEjectUponMediaFull);
        return this;
    }

    public GetStorageDomainsSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }

    public GetStorageDomainsSpectraS3Request withMediaEjectionAllowed(final boolean mediaEjectionAllowed) {
        this.mediaEjectionAllowed = mediaEjectionAllowed;
        this.updateQueryParam("media_ejection_allowed", mediaEjectionAllowed);
        return this;
    }

    public GetStorageDomainsSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }

    public GetStorageDomainsSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }

    public GetStorageDomainsSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }

    public GetStorageDomainsSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }

    public GetStorageDomainsSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }

    public GetStorageDomainsSpectraS3Request withWriteOptimization(final WriteOptimization writeOptimization) {
        this.writeOptimization = writeOptimization;
        this.updateQueryParam("write_optimization", writeOptimization);
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/storage_domain";
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


    public boolean getLastPage() {
        return this.lastPage;
    }


    public boolean getMediaEjectionAllowed() {
        return this.mediaEjectionAllowed;
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


    public WriteOptimization getWriteOptimization() {
        return this.writeOptimization;
    }

}