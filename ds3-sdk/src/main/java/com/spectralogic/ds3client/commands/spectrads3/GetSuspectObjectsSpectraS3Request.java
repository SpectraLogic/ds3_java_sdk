/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

public class GetSuspectObjectsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private String bucketId;

    private String storageDomainId;

    // Constructor
    
    
    public GetSuspectObjectsSpectraS3Request() {
        
    }

    public GetSuspectObjectsSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }


    public GetSuspectObjectsSpectraS3Request withStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId.toString();
        this.updateQueryParam("storage_domain_id", storageDomainId);
        return this;
    }


    public GetSuspectObjectsSpectraS3Request withStorageDomainId(final String storageDomainId) {
        this.storageDomainId = storageDomainId;
        this.updateQueryParam("storage_domain_id", storageDomainId);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/suspect_object";
    }
    
    public String getBucketId() {
        return this.bucketId;
    }


    public String getStorageDomainId() {
        return this.storageDomainId;
    }

}