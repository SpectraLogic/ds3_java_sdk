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
package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;

public class DeleteObjectRequest extends AbstractRequest {

    // Variables
    
    private final String bucketName;

    private final String objectName;

    private boolean replicate;

    private boolean rollBack;

    // Constructor
    
    
    public DeleteObjectRequest(final String bucketName, final String objectName) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        
    }

    public DeleteObjectRequest withReplicate(final boolean replicate) {
        this.replicate = replicate;
        if (this.replicate) {
            this.getQueryParams().put("replicate", null);
        } else {
            this.getQueryParams().remove("replicate");
        }
        return this;
    }


    public DeleteObjectRequest withRollBack(final boolean rollBack) {
        this.rollBack = rollBack;
        if (this.rollBack) {
            this.getQueryParams().put("roll_back", null);
        } else {
            this.getQueryParams().remove("roll_back");
        }
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.DELETE;
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


    public boolean getReplicate() {
        return this.replicate;
    }


    public boolean getRollBack() {
        return this.rollBack;
    }

}