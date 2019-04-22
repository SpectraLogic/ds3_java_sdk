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

public class GetGroupMembersSpectraS3Request extends AbstractPaginationRequest {

    // Variables
    
    private String groupId;

    private boolean lastPage;

    private String memberGroupId;

    private String memberUserId;

    private int pageLength;

    private int pageOffset;

    private String pageStartMarker;

    // Constructor
    
    
    public GetGroupMembersSpectraS3Request() {
        
    }

    public GetGroupMembersSpectraS3Request withGroupId(final UUID groupId) {
        this.groupId = groupId.toString();
        this.updateQueryParam("group_id", groupId);
        return this;
    }


    public GetGroupMembersSpectraS3Request withGroupId(final String groupId) {
        this.groupId = groupId;
        this.updateQueryParam("group_id", groupId);
        return this;
    }


    public GetGroupMembersSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }


    public GetGroupMembersSpectraS3Request withMemberGroupId(final UUID memberGroupId) {
        this.memberGroupId = memberGroupId.toString();
        this.updateQueryParam("member_group_id", memberGroupId);
        return this;
    }


    public GetGroupMembersSpectraS3Request withMemberGroupId(final String memberGroupId) {
        this.memberGroupId = memberGroupId;
        this.updateQueryParam("member_group_id", memberGroupId);
        return this;
    }


    public GetGroupMembersSpectraS3Request withMemberUserId(final UUID memberUserId) {
        this.memberUserId = memberUserId.toString();
        this.updateQueryParam("member_user_id", memberUserId);
        return this;
    }


    public GetGroupMembersSpectraS3Request withMemberUserId(final String memberUserId) {
        this.memberUserId = memberUserId;
        this.updateQueryParam("member_user_id", memberUserId);
        return this;
    }


    public GetGroupMembersSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", pageLength);
        return this;
    }


    public GetGroupMembersSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", pageOffset);
        return this;
    }


    public GetGroupMembersSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker.toString();
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }


    public GetGroupMembersSpectraS3Request withPageStartMarker(final String pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/group_member";
    }
    
    public String getGroupId() {
        return this.groupId;
    }


    public boolean getLastPage() {
        return this.lastPage;
    }


    public String getMemberGroupId() {
        return this.memberGroupId;
    }


    public String getMemberUserId() {
        return this.memberUserId;
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

}