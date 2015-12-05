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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.HttpVerb;

import com.spectralogic.ds3client.models.Range;
import org.apache.http.entity.ContentType;

import java.nio.channels.WritableByteChannel;
import java.util.Collection;
import java.util.UUID;

/**
 * Retrieves an object from DS3.  This should always be used within the context of a BulkGet command.
 * If not performance will be impacted.
 */
public class GetObjectRequest extends AbstractRequest {

    private final String bucketName;
    private final String objectName;
    private final long offset;
    private final UUID jobId;
    private final WritableByteChannel channel;
    private ImmutableCollection<Range> byteRanges = null;

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
     * Sets a Range of bytes that should be retrieved from the object in the
     * format: 'Range: bytes=[start]-[end],...'.
     */
    public GetObjectRequest withByteRanges(final Range... byteRanges) {
        if (byteRanges != null) {
            this.setRanges(ImmutableList.copyOf(byteRanges));
        }
        return this;
    }

    public GetObjectRequest withByteRanges(final Collection<Range> byteRanges) {
        if (byteRanges != null) {
            this.setRanges(ImmutableList.copyOf(byteRanges));
        }
        return this;
    }

    private void setRanges(final ImmutableList<Range> byteRanges) {
        this.byteRanges = byteRanges;

        if (this.getHeaders().containsKey("Range")) {
            this.getHeaders().removeAll("Range");
        }

        this.getHeaders().put("Range", buildRangeHeaderText(byteRanges));
    }

    private static String buildRangeHeaderText(final ImmutableList<Range> byteRanges) {

        final ImmutableList.Builder<String> builder = ImmutableList.builder();

        for (final Range range : byteRanges) {
            builder.add(String.format("%d-%d", range.getStart(), range.getEnd()));
        }

        final Joiner stringJoiner = Joiner.on(",");

        return "bytes=" + stringJoiner.join(builder.build());
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public ImmutableCollection<Range> getByteRanges() {
        return this.byteRanges;
    }

    public long getOffset() {
        return offset;
    }
    @Override
    public String getPath() {
        return "/" + this.bucketName + "/" + this.objectName;
    }

    @Override
    public String getContentType() {
        return ContentType.APPLICATION_OCTET_STREAM.toString();
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
