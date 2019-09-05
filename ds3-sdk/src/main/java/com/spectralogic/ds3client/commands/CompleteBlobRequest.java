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
package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import java.util.UUID;
import com.google.common.net.UrlEscapers;

public class CompleteBlobRequest extends AbstractRequest {

    // Variables
    
    private final String bucketName;

    private final String objectName;

    private final String blob;

    private final String job;

    // Constructor
    
    
    public CompleteBlobRequest(final String bucketName, final String objectName, final UUID blob, final UUID job) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.blob = blob.toString();
        this.job = job.toString();
        
        this.updateQueryParam("blob", blob);

        this.updateQueryParam("job", job);

    }

    
    public CompleteBlobRequest(final String bucketName, final String objectName, final String blob, final String job) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.blob = blob;
        this.job = job;
        
        this.updateQueryParam("blob", blob);

        this.updateQueryParam("job", job);

    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/" + this.bucketName + "/" + this.objectName;
    }
    
    public String getBucketName() {
        return this.bucketName;
    }


    public String getObjectName() {
        return this.objectName;
    }


    public String getBlob() {
        return this.blob;
    }


    public String getJob() {
        return this.job;
    }

}