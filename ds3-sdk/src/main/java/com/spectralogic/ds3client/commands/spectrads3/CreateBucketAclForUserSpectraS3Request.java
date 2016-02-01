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

import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.commands.AbstractRequest;
import java.util.UUID;
import com.spectralogic.ds3client.models.BucketAclPermission;

public class CreateBucketAclForUserSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final UUID bucketId;

    private final BucketAclPermission permission;

    private final UUID userId;

    // Constructor
    
    public CreateBucketAclForUserSpectraS3Request(final UUID bucketId, final BucketAclPermission permission, final UUID userId) {
        this.bucketId = bucketId;
        this.permission = permission;
        this.userId = userId;
                this.getQueryParams().put("bucket_id", bucketId.toString());
        this.getQueryParams().put("permission", permission.toString());
        this.getQueryParams().put("user_id", userId.toString());
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/bucket_acl";
    }
    
    public UUID getBucketId() {
        return this.bucketId;
    }


    public BucketAclPermission getPermission() {
        return this.permission;
    }


    public UUID getUserId() {
        return this.userId;
    }

}