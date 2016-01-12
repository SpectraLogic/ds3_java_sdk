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

import com.spectralogic.ds3client.commands.AbstractRequest;
import com.spectralogic.ds3client.HttpVerb;
import java.util.UUID;
import com.spectralogic.ds3client.models.BucketAclPermission;

public class CreateGlobalBucketAclForGroupSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String bucketAcl;

    private final UUID groupId;

    private final BucketAclPermission permission;


    // Constructor
    public CreateGlobalBucketAclForGroupSpectraS3Request(final String bucketAcl, final UUID groupId, final BucketAclPermission permission) {
        this.bucketAcl = bucketAcl;
        this.groupId = groupId;
        this.permission = permission;
        
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/bucket_acl/" + bucketAcl;
    }
    
    public String getBucketAcl() {
        return this.bucketAcl;
    }


    public UUID getGroupId() {
        return this.groupId;
    }


    public BucketAclPermission getPermission() {
        return this.permission;
    }


}