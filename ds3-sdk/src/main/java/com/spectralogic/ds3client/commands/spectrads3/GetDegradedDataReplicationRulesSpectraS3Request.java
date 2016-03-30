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
import java.util.UUID;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.DataPersistenceRuleState;
import com.spectralogic.ds3client.models.DataReplicationRuleType;

public class GetDegradedDataReplicationRulesSpectraS3Request extends AbstractRequest {

    // Variables
    
    private String dataPolicyId;

    private String ds3TargetId;

    private boolean lastPage;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private DataPersistenceRuleState state;

    private DataReplicationRuleType type;

    // Constructor
    
    public GetDegradedDataReplicationRulesSpectraS3Request() {
        
    }

    public GetDegradedDataReplicationRulesSpectraS3Request withDataPolicyId(final UUID dataPolicyId) {
        this.dataPolicyId = dataPolicyId.toString();
        this.updateQueryParam("data_policy_id", dataPolicyId);
        return this;
    }

    public GetDegradedDataReplicationRulesSpectraS3Request withDataPolicyId(final String dataPolicyId) {
        this.dataPolicyId = dataPolicyId;
        this.updateQueryParam("data_policy_id", dataPolicyId);
        return this;
    }

    public GetDegradedDataReplicationRulesSpectraS3Request withDs3TargetId(final UUID ds3TargetId) {
        this.ds3TargetId = ds3TargetId.toString();
        this.updateQueryParam("ds3_target_id", ds3TargetId);
        return this;
    }

    public GetDegradedDataReplicationRulesSpectraS3Request withDs3TargetId(final String ds3TargetId) {
        this.ds3TargetId = ds3TargetId;
        this.updateQueryParam("ds3_target_id", ds3TargetId);
        return this;
    }

    public GetDegradedDataReplicationRulesSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }

    public GetDegradedDataReplicationRulesSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }

    public GetDegradedDataReplicationRulesSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }

    public GetDegradedDataReplicationRulesSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }

    public GetDegradedDataReplicationRulesSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }

    public GetDegradedDataReplicationRulesSpectraS3Request withState(final DataPersistenceRuleState state) {
        this.state = state;
        this.updateQueryParam("state", state);
        return this;
    }

    public GetDegradedDataReplicationRulesSpectraS3Request withType(final DataReplicationRuleType type) {
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
        return "/_rest_/degraded_data_replication_rule";
    }
    
    public String getDataPolicyId() {
        return this.dataPolicyId;
    }


    public String getDs3TargetId() {
        return this.ds3TargetId;
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


    public DataReplicationRuleType getType() {
        return this.type;
    }

}