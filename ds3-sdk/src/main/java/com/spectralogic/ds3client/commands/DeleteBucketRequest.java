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

package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

public class DeleteBucketRequest extends AbstractRequest {

    private final String bucket;
    public DeleteBucketRequest(final String bucket) {
        this.bucket  = bucket;
    }
    
    public String getBucket() {
        return this.bucket;
    }

    @Override
    public String getPath() {
        return "/"+ this.bucket;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.DELETE;
    }
}
