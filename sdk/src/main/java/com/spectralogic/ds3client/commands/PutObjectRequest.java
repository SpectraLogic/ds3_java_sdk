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
import com.spectralogic.ds3client.models.Checksum;

import java.io.InputStream;
import java.util.UUID;

/**
 * Maps to a DS3 Single Object Put request.  This request requires that the InputStream is seekable, that is the
 * InputStream.markSupported() method must return true.  By default the Java FileInputStream does not do this.  To
 * use a FileInputStream see {@link com.spectralogic.ds3client.helpers.ResettableFileInputStream} which wraps a
 * FileInputStream and makes it seekable.
 */
public class PutObjectRequest extends AbstractRequest {

    private final String bucketName;
    private final String objectName;
    private final UUID jobId;
    private final InputStream stream;
    private final long size;
    private final long offset;
    private Checksum checksum = Checksum.none();

    @Deprecated
    public PutObjectRequest(final String bucketName, final String objectName, final long size, final InputStream stream) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.stream = stream;
        this.size = size;
        this.jobId = null;
        this.offset = 0;
    }

    public PutObjectRequest(final String bucketName, final String objectName, final UUID jobId, final long size, final long offset, final InputStream stream) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.stream = stream;
        this.size = size;
        this.jobId = jobId;
        this.offset = offset;

        this.getQueryParams().put("job", jobId.toString());
        this.getQueryParams().put("offset", Long.toString(offset));
    }
    
    public PutObjectRequest withChecksum(final Checksum checksum) {
        this.checksum = checksum;
        return this;
    }
    
    public String getBucketName() {
        return this.bucketName;
    }

    public String getObjectName() {
        return this.objectName;
    }

    @Override
    public Checksum getChecksum() {
        return this.checksum;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public String getPath() {
        return "/" + this.bucketName + "/" + this.objectName;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public InputStream getStream() {
        return this.stream;
    }

    public UUID getJobId() {
        return this.jobId;
    }

    public long getOffset() {
        return offset;
    }
}
