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

public class PutDs3TargetUserMappingSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String authId;

    private final long orderNum;

    private final String secretKey;

    private String bucketId;

    private String groupId;

    private String targetId;

    private String userId;

    // Constructor
    
    public PutDs3TargetUserMappingSpectraS3Request(final String authId, final long orderNum, final String secretKey) {
        this.authId = authId;
        this.orderNum = orderNum;
        this.secretKey = secretKey;
        
        this.getQueryParams().put("auth_id", UrlEscapers.urlFragmentEscaper().escape(authId).replace("+", "%2B"));
        this.getQueryParams().put("order_num", Long.toString(orderNum));
        this.getQueryParams().put("secret_key", UrlEscapers.urlFragmentEscaper().escape(secretKey).replace("+", "%2B"));
    }

    public PutDs3TargetUserMappingSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }

    public PutDs3TargetUserMappingSpectraS3Request withGroupId(final UUID groupId) {
        this.groupId = groupId.toString();
        this.updateQueryParam("group_id", groupId);
        return this;
    }

    public PutDs3TargetUserMappingSpectraS3Request withGroupId(final String groupId) {
        this.groupId = groupId;
        this.updateQueryParam("group_id", groupId);
        return this;
    }

    public PutDs3TargetUserMappingSpectraS3Request withTargetId(final UUID targetId) {
        this.targetId = targetId.toString();
        this.updateQueryParam("target_id", targetId);
        return this;
    }

    public PutDs3TargetUserMappingSpectraS3Request withTargetId(final String targetId) {
        this.targetId = targetId;
        this.updateQueryParam("target_id", targetId);
        return this;
    }

    public PutDs3TargetUserMappingSpectraS3Request withUserId(final UUID userId) {
        this.userId = userId.toString();
        this.updateQueryParam("user_id", userId);
        return this;
    }

    public PutDs3TargetUserMappingSpectraS3Request withUserId(final String userId) {
        this.userId = userId;
        this.updateQueryParam("user_id", userId);
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/ds3_target_user_mapping";
    }
    
    public String getAuthId() {
        return this.authId;
    }


    public long getOrderNum() {
        return this.orderNum;
    }


    public String getSecretKey() {
        return this.secretKey;
    }


    public String getBucketId() {
        return this.bucketId;
    }


    public String getGroupId() {
        return this.groupId;
    }


    public String getTargetId() {
        return this.targetId;
    }


    public String getUserId() {
        return this.userId;
    }

}