/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.models.common.Range;
import org.apache.http.entity.ContentType;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Collection;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import java.util.UUID;
import javax.annotation.Nonnull;
import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.models.ChecksumType;

public class GetObjectRequest extends AbstractRequest {

    // Variables
    
    private final WritableByteChannel channel;

    private final String bucketName;

    private final String objectName;

    private boolean cachedOnly;

    private String job;

    private long offset;

    private String versionId;
    private ImmutableCollection<Range> byteRanges = null;
    private ChecksumType checksum = ChecksumType.none();
    private ChecksumType.Type checksumType = ChecksumType.Type.NONE;

    // Constructor
    
    /** @deprecated use {@link #GetObjectRequest(String, String, WritableByteChannel, UUID, long)} instead */
    @Deprecated
    public GetObjectRequest(final String bucketName, final String objectName, @Nonnull final WritableByteChannel channel) {
        Preconditions.checkNotNull(channel, "Channel may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.channel = channel;
        

    }

    
    public GetObjectRequest(final String bucketName, final String objectName, @Nonnull final WritableByteChannel channel, final UUID job, final long offset) {
        Preconditions.checkNotNull(channel, "Channel may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.job = job.toString();
        this.offset = offset;
        this.channel = channel;
        
        this.updateQueryParam("job", job);

        this.updateQueryParam("offset", offset);


    }

    
    public GetObjectRequest(final String bucketName, final String objectName, @Nonnull final WritableByteChannel channel, final String job, final long offset) {
        Preconditions.checkNotNull(channel, "Channel may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.job = job;
        this.offset = offset;
        this.channel = channel;
        
        this.updateQueryParam("job", job);

        this.updateQueryParam("offset", offset);


    }

    
    public GetObjectRequest(final String bucketName, final String objectName, final UUID job, final long offset, @Nonnull final OutputStream stream) {
        Preconditions.checkNotNull(stream, "Stream may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.job = job.toString();
        this.offset = offset;
        this.channel = Channels.newChannel(stream);
        
        this.updateQueryParam("job", job);

        this.updateQueryParam("offset", offset);


    }

    
    public GetObjectRequest(final String bucketName, final String objectName, final String job, final long offset, @Nonnull final OutputStream stream) {
        Preconditions.checkNotNull(stream, "Stream may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.job = job;
        this.offset = offset;
        this.channel = Channels.newChannel(stream);
        
        this.updateQueryParam("job", job);

        this.updateQueryParam("offset", offset);


    }


    public GetObjectRequest withCachedOnly(final boolean cachedOnly) {
        this.cachedOnly = cachedOnly;
        if (this.cachedOnly) {
            this.getQueryParams().put("cached_only", null);
        } else {
            this.getQueryParams().remove("cached_only");
        }
        return this;
    }


    public GetObjectRequest withJob(final UUID job) {
        this.job = job.toString();
        this.updateQueryParam("job", job);
        return this;
    }


    public GetObjectRequest withJob(final String job) {
        this.job = job;
        this.updateQueryParam("job", job);
        return this;
    }


    public GetObjectRequest withOffset(final long offset) {
        this.offset = offset;
        this.updateQueryParam("offset", offset);
        return this;
    }


    public GetObjectRequest withVersionId(final UUID versionId) {
        this.versionId = versionId.toString();
        this.updateQueryParam("version_id", versionId);
        return this;
    }


    public GetObjectRequest withVersionId(final String versionId) {
        this.versionId = versionId;
        this.updateQueryParam("version_id", versionId);
        return this;
    }



    /**
     * Set a MD5 checksum for the request.
     */
    public GetObjectRequest withChecksum(final ChecksumType checksum) {
        return withChecksum(checksum, ChecksumType.Type.MD5);
    }

    public GetObjectRequest withChecksum(final ChecksumType checksum, final ChecksumType.Type checksumType) {
        this.checksum = checksum;
        this.checksumType = checksumType;
        return this;
    }

    @Override
    public ChecksumType getChecksum() {
        return this.checksum;
    }


    @Override
    public ChecksumType.Type getChecksumType() {
        return this.checksumType;
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

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/" + this.bucketName + "/" + this.objectName;
    }
    @Override
    public String getContentType() {
        return ContentType.APPLICATION_OCTET_STREAM.toString();
    }

    
    public WritableByteChannel getChannel() {
        return this.channel;
    }


    public String getBucketName() {
        return this.bucketName;
    }


    public String getObjectName() {
        return this.objectName;
    }


    public boolean getCachedOnly() {
        return this.cachedOnly;
    }


    public String getJob() {
        return this.job;
    }


    public long getOffset() {
        return this.offset;
    }


    public String getVersionId() {
        return this.versionId;
    }


    public Collection<Range> getByteRanges() {
        return this.byteRanges;
    }


}