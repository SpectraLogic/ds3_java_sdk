/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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
import com.google.common.net.UrlEscapers;
import java.util.UUID;

public class PutAzureTargetBucketNameSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String bucketId;

    private final String name;

    private final String targetId;

    // Constructor
    
    
    public PutAzureTargetBucketNameSpectraS3Request(final String bucketId, final String name, final UUID targetId) {
        this.bucketId = bucketId;
        this.name = name;
        this.targetId = targetId.toString();
        
        this.getQueryParams().put("bucket_id", bucketId);
        this.getQueryParams().put("name", UrlEscapers.urlFragmentEscaper().escape(name).replace("+", "%2B"));
        this.getQueryParams().put("target_id", targetId.toString());
    }

    
    public PutAzureTargetBucketNameSpectraS3Request(final String bucketId, final String name, final String targetId) {
        this.bucketId = bucketId;
        this.name = name;
        this.targetId = targetId;
        
        this.getQueryParams().put("bucket_id", bucketId);
        this.getQueryParams().put("name", UrlEscapers.urlFragmentEscaper().escape(name).replace("+", "%2B"));
        this.getQueryParams().put("target_id", UrlEscapers.urlFragmentEscaper().escape(targetId).replace("+", "%2B"));
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/azure_target_bucket_name";
    }
    
    public String getBucketId() {
        return this.bucketId;
    }


    public String getName() {
        return this.name;
    }


    public String getTargetId() {
        return this.targetId;
    }

}