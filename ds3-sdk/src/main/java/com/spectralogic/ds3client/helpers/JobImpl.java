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

package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.Job;
import com.spectralogic.ds3client.models.JobWithChunksApiBean;
import com.spectralogic.ds3client.models.bulk.MasterObjectList;

import java.util.UUID;

abstract class JobImpl implements Job {
    protected int maxParallelRequests = 20;
    protected final Ds3Client client;
    protected final JobWithChunksApiBean jobWithChunksApiBean;

    public JobImpl(final Ds3Client client, final JobWithChunksApiBean jobWithChunksApiBean) {
        this.client = client;
        this.jobWithChunksApiBean = jobWithChunksApiBean;
    }
    
    @Override
    public UUID getJobId() {
        if (this.jobWithChunksApiBean == null) {
            return null;
        }
        return this.jobWithChunksApiBean.getJobId();
    }

    @Override
    public String getBucketName() {
        if (this.jobWithChunksApiBean == null) {
            return null;
        }
        return this.jobWithChunksApiBean.getBucketName();
    }
    
    @Override
    public Job withMaxParallelRequests(final int maxParallelRequests) {
        this.maxParallelRequests = maxParallelRequests;
        return this;
    }
}
