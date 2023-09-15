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
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;

import java.util.UUID;

public class UndeleteObjectSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String bucketId;

    private final String name;

    private String versionId;

    // Constructor
    
    
    public UndeleteObjectSpectraS3Request(final String bucketId, final String name) {
        this.bucketId = bucketId;
        this.name = name;
        
        this.updateQueryParam("bucket_id", bucketId);

        this.updateQueryParam("name", name);

    }

    public UndeleteObjectSpectraS3Request withVersionId(final UUID versionId) {
        this.versionId = versionId.toString();
        this.updateQueryParam("version_id", versionId);
        return this;
    }


    public UndeleteObjectSpectraS3Request withVersionId(final String versionId) {
        this.versionId = versionId;
        this.updateQueryParam("version_id", versionId);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/object";
    }
    
    public String getBucketId() {
        return this.bucketId;
    }


    public String getName() {
        return this.name;
    }


    public String getVersionId() {
        return this.versionId;
    }

}