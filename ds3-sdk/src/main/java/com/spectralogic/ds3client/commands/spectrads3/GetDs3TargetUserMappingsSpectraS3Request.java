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

public class GetDs3TargetUserMappingsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private String authId;

    private String bucketId;

    private UUID groupId;

    private boolean lastPage;

    private long orderNum;

    private int pageLength;

    private int pageOffset;

    private UUID pageStartMarker;

    private UUID targetId;

    private UUID userId;

    // Constructor
    
    public GetDs3TargetUserMappingsSpectraS3Request() {
            }

    public GetDs3TargetUserMappingsSpectraS3Request withAuthId(final String authId) {
        this.authId = authId;
        this.updateQueryParam("auth_id", UrlEscapers.urlFragmentEscaper().escape(authId));
        return this;
    }

    public GetDs3TargetUserMappingsSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }

    public GetDs3TargetUserMappingsSpectraS3Request withGroupId(final UUID groupId) {
        this.groupId = groupId;
        this.updateQueryParam("group_id", groupId.toString());
        return this;
    }

    public GetDs3TargetUserMappingsSpectraS3Request withLastPage(final boolean lastPage) {
        this.lastPage = lastPage;
        if (this.lastPage) {
            this.getQueryParams().put("last_page", null);
        } else {
            this.getQueryParams().remove("last_page");
        }
        return this;
    }

    public GetDs3TargetUserMappingsSpectraS3Request withOrderNum(final long orderNum) {
        this.orderNum = orderNum;
        this.updateQueryParam("order_num", Long.toString(orderNum));
        return this;
    }

    public GetDs3TargetUserMappingsSpectraS3Request withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", Integer.toString(pageLength));
        return this;
    }

    public GetDs3TargetUserMappingsSpectraS3Request withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", Integer.toString(pageOffset));
        return this;
    }

    public GetDs3TargetUserMappingsSpectraS3Request withPageStartMarker(final UUID pageStartMarker) {
        this.pageStartMarker = pageStartMarker;
        this.updateQueryParam("page_start_marker", pageStartMarker.toString());
        return this;
    }

    public GetDs3TargetUserMappingsSpectraS3Request withTargetId(final UUID targetId) {
        this.targetId = targetId;
        this.updateQueryParam("target_id", targetId.toString());
        return this;
    }

    public GetDs3TargetUserMappingsSpectraS3Request withUserId(final UUID userId) {
        this.userId = userId;
        this.updateQueryParam("user_id", userId.toString());
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/ds3_target_user_mapping";
    }
    
    public String getAuthId() {
        return this.authId;
    }


    public String getBucketId() {
        return this.bucketId;
    }


    public UUID getGroupId() {
        return this.groupId;
    }


    public boolean getLastPage() {
        return this.lastPage;
    }


    public long getOrderNum() {
        return this.orderNum;
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


    public UUID getTargetId() {
        return this.targetId;
    }


    public UUID getUserId() {
        return this.userId;
    }

}