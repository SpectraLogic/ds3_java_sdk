/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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
import com.spectralogic.ds3client.models.bulk.Priority;
import com.spectralogic.ds3client.models.bulk.WriteOptimization;

public class PutBucketRequest extends AbstractRequest {

    final private String bucket;
    private WriteOptimization writeOptimization;
    private Priority putPriority;
    private Priority getPriority;

    public PutBucketRequest(final String bucket) {
        this.bucket = bucket;
        writeOptimization = null;
        putPriority = null;
        getPriority = null;
    }

    public PutBucketRequest withDefaultWriteOptimization(final WriteOptimization writeOptimization) {
        this.writeOptimization = writeOptimization;
        this.updateQueryParam("defaultWriteOptimization", writeOptimization.toString());
        return this;
    }

    public PutBucketRequest withDefaultPutJobPriority(final Priority priority) {
        this.putPriority = priority;
        this.updateQueryParam("defaultPutJobPriority", priority.toString());
        return this;
    }

    public PutBucketRequest withDefaultGetJobPriority(final Priority priority) {
        this.getPriority = priority;
        this.updateQueryParam("defaultGetJobPriority", priority.toString());
        return this;
    }

    public String getBucket() {
        return this.bucket;
    }

    @Override
    public String getPath() {
        return "/" + this.bucket;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }
}
