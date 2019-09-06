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
import com.spectralogic.ds3client.commands.interfaces.AbstractPaginationRequest;
import java.util.UUID;
import com.google.common.net.UrlEscapers;

public class GetDataPolicyAclsSpectraS3Request extends AbstractPaginationRequest {

    // Variables
    
    private String dataPolicyId;

    private String groupId;

    private boolean lastPage;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    private String userId;

    // Constructor
    
    
    public GetDataPolicyAclsSpectraS3Request() {
        
    }

    public GetDataPolicyAclsSpectraS3Request withDataPolicyId(final UUID dataPolicyId) {
        this.dataPolicyId = dataPolicyId.toString();
        this.updateQueryParam("data_policy_id", dataPolicyId);
        return this;
    }


    public GetDataPolicyAclsSpectraS3Request withDataPolicyId(final String dataPolicyId) {
        this.dataPolicyId = dataPolicyId;
        this.updateQueryParam("data_policy_id", dataPolicyId);
        return this;
    }


    public GetDataPolicyAclsSpectraS3Request withGroupId(final UUID groupId) {
        this.groupId = groupId.toString();
        this.updateQueryParam("group_id", groupId);
        return this;
    }


    public GetDataPolicyAclsSpectraS3Request withGroupId(final String groupId) {
        this.groupId = groupId;
        this.updateQueryParam("group_id", groupId);
        return this;
    }


    public GetDataPolicyAclsSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }


    public GetDataPolicyAclsSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }


    public GetDataPolicyAclsSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }


    public GetDataPolicyAclsSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetDataPolicyAclsSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetDataPolicyAclsSpectraS3Request withUserId(final UUID userId) {
        this.userId = userId.toString();
        this.updateQueryParam("user_id", userId);
        return this;
    }


    public GetDataPolicyAclsSpectraS3Request withUserId(final String userId) {
        this.userId = userId;
        this.updateQueryParam("user_id", userId);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/data_policy_acl";
    }
    
    public String getDataPolicyId() {
        return this.dataPolicyId;
    }


    public String getGroupId() {
        return this.groupId;
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


    public String getUserId() {
        return this.userId;
    }

}