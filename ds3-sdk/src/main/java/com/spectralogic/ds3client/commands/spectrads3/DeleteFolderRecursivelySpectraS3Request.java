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

public class DeleteFolderRecursivelySpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String folder;

    private final UUID bucketId;

    private boolean rollBack;

    // Constructor
    
    public DeleteFolderRecursivelySpectraS3Request(final UUID bucketId, final String folder) {
        this.folder = folder;
        this.bucketId = bucketId;
                this.getQueryParams().put("bucket_id", bucketId.toString());
        this.getQueryParams().put("recursive", null);
    }

    public DeleteFolderRecursivelySpectraS3Request withRollBack(final boolean rollBack) {
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
        return "/_rest_/folder/" + folder;
    }
    
    public String getFolder() {
        return this.folder;
    }


    public UUID getBucketId() {
        return this.bucketId;
    }


    public boolean getRollBack() {
        return this.rollBack;
    }

}