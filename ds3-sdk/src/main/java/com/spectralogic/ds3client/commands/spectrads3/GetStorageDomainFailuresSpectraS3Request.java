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
import com.spectralogic.ds3client.models.StorageDomainFailureType;

public class GetStorageDomainFailuresSpectraS3Request extends AbstractRequest {

    // Variables
    
    private String errorMessage;

    private boolean lastPage;

    private int pageLength;

    private int pageOffset;

    private UUID pageStartMarker;

    private UUID storageDomainId;

    private StorageDomainFailureType type;

    // Constructor
    
    public GetStorageDomainFailuresSpectraS3Request() {
            }

    public GetStorageDomainFailuresSpectraS3Request withErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
        this.updateQueryParam("error_message", UrlEscapers.urlFragmentEscaper().escape(errorMessage).replace('+', ' '));
        return this;
    }

    public GetStorageDomainFailuresSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }

    public GetStorageDomainFailuresSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", Integer.toString(pageLength));
        return this;
    }

    public GetStorageDomainFailuresSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", Integer.toString(pageOffset));
        return this;
    }

    public GetStorageDomainFailuresSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker.toString());
        return this;
    }

    public GetStorageDomainFailuresSpectraS3Request withStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId;
        this.updateQueryParam("storage_domain_id", storageDomainId.toString());
        return this;
    }

    public GetStorageDomainFailuresSpectraS3Request withType(final StorageDomainFailureType type) {
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
        return "/_rest_/storage_domain_failure";
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
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


    public UUID getStorageDomainId() {
        return this.storageDomainId;
    }


    public StorageDomainFailureType getType() {
        return this.type;
    }

}