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
import java.util.UUID;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.DataIsolationLevel;
import com.spectralogic.ds3client.models.DataPersistenceRuleState;
import com.spectralogic.ds3client.models.DataPersistenceRuleType;

public class GetDegradedDataPersistenceRulesSpectraS3Request extends AbstractRequest {

    // Variables
    
    private String dataPolicyId;

    private DataIsolationLevel isolationLevel;

    private boolean lastPage;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private DataPersistenceRuleState state;

    private String storageDomainId;

    private DataPersistenceRuleType type;

    // Constructor
    
    public GetDegradedDataPersistenceRulesSpectraS3Request() {
        
    }

    public GetDegradedDataPersistenceRulesSpectraS3Request withDataPolicyId(final UUID dataPolicyId) {
        this.dataPolicyId = dataPolicyId.toString();
        this.updateQueryParam("data_policy_id", dataPolicyId);
        return this;
    }

    public GetDegradedDataPersistenceRulesSpectraS3Request withDataPolicyId(final String dataPolicyId) {
        this.dataPolicyId = dataPolicyId;
        this.updateQueryParam("data_policy_id", dataPolicyId);
        return this;
    }

    public GetDegradedDataPersistenceRulesSpectraS3Request withIsolationLevel(final DataIsolationLevel isolationLevel) {
        this.isolationLevel = isolationLevel;
        this.updateQueryParam("isolation_level", isolationLevel);
        return this;
    }

    public GetDegradedDataPersistenceRulesSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }

    public GetDegradedDataPersistenceRulesSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }

    public GetDegradedDataPersistenceRulesSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }

    public GetDegradedDataPersistenceRulesSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }

    public GetDegradedDataPersistenceRulesSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }

    public GetDegradedDataPersistenceRulesSpectraS3Request withState(final DataPersistenceRuleState state) {
        this.state = state;
        this.updateQueryParam("state", state);
        return this;
    }

    public GetDegradedDataPersistenceRulesSpectraS3Request withStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId.toString();
        this.updateQueryParam("storage_domain_id", storageDomainId);
        return this;
    }

    public GetDegradedDataPersistenceRulesSpectraS3Request withStorageDomainId(final String storageDomainId) {
        this.storageDomainId = storageDomainId;
        this.updateQueryParam("storage_domain_id", storageDomainId);
        return this;
    }

    public GetDegradedDataPersistenceRulesSpectraS3Request withType(final DataPersistenceRuleType type) {
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
        return "/_rest_/degraded_data_persistence_rule";
    }
    
    public String getDataPolicyId() {
        return this.dataPolicyId;
    }


    public DataIsolationLevel getIsolationLevel() {
        return this.isolationLevel;
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


    public String getPageStartMarker() {
        return this.pageStartMarker;
    }


    public DataPersistenceRuleState getState() {
        return this.state;
    }


    public String getStorageDomainId() {
        return this.storageDomainId;
    }


    public DataPersistenceRuleType getType() {
        return this.type;
    }

}