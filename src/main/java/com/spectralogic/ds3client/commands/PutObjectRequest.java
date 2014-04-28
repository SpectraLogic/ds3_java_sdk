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

import java.io.InputStream;
import java.util.UUID;

public class PutObjectRequest extends AbstractRequest {

    private final String bucketName;
    private final String objectName;
    private final UUID jobId;
    private final InputStream stream;
    private final long size;

    @Deprecated
    public PutObjectRequest(final String bucketName, final String objectName, final long size, final InputStream stream) {
        this(bucketName,objectName, null, size, stream);
    }

    public PutObjectRequest(final String bucketName, final String objectName, final UUID jobId, final long size, final InputStream stream) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.stream = stream;
        this.size = size;
        this.jobId = jobId;

        if(jobId != null) {
            this.getQueryParams().put("job", jobId.toString());
        }
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public String getPath() {
        return "/" + bucketName + "/" + objectName;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public InputStream getStream() {
        return stream;
    }

    public UUID getJobId() {
        return this.jobId;
    }
}
