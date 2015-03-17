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

import org.apache.http.entity.ContentType;

import java.nio.channels.WritableByteChannel;
import java.util.UUID;

/**
 * Retrieves an object from DS3.  This should always be used within the context of a BulkGet command.
 * If not performance will be impacted.
 */
public class GetObjectRequest extends AbstractRequest {
    public long getOffset() {
        return offset;
    }

    public static class Range {
        private final long start;
        private final long end;

        public Range(final long start, final long end) {
            this.start = start;
            this.end = end;
        }

        public long getStart() {
            return this.start;
        }

        public long getEnd() {
            return this.end;
        }
    }

    private final String bucketName;
    private final String objectName;
    private final long offset;
    private final UUID jobId;
    private final WritableByteChannel channel;
    private Range byteRange = null;

    /**
     * Creates a request to get an object within the context of a bulk job.  This is the preferred method of creating a get object request.
     * See {@link BulkGetRequest} for more information on creating a bulk get request.
    */
    public GetObjectRequest(final String bucketName, final String objectName, final long offset, final UUID jobId, final WritableByteChannel channel) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.jobId = jobId;
        this.offset = offset;
        this.channel = channel;

        this.getQueryParams().put("job", jobId.toString());
        this.getQueryParams().put("offset", Long.toString(offset));
    }

    /**
     * Sets a Range of bytes that should be retrieved from the object.
     */
    public GetObjectRequest withByteRange(final Range byteRange) {
        this.byteRange = byteRange;
        if (byteRange != null) {
            this.getHeaders().put("Range", buildRangeHeaderText(byteRange));
        }
        return this;
    }
    
    public String getBucketName() {
        return this.bucketName;
    }

    public String getObjectName() {
        return this.objectName;
    }

    private static String buildRangeHeaderText(final Range byteRange) {
        return String.format("bytes=%d-%d", byteRange.getStart(), byteRange.getEnd());
    }

    public Range getByteRange() {
        return this.byteRange;
    }

    @Override
    public String getPath() {
        return "/" + this.bucketName + "/" + this.objectName;
    }

    @Override
    public ContentType getContentType() {
        return ContentType.APPLICATION_OCTET_STREAM;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    public UUID getJobId() {
        return this.jobId;
    }

    public WritableByteChannel getDestinationChannel() {
        return this.channel;
    }
}
