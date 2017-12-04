/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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
import java.io.InputStream;
import java.nio.channels.SeekableByteChannel;
import com.spectralogic.ds3client.utils.SeekableByteChannelInputStream;
import static com.spectralogic.ds3client.utils.Guard.isStringNullOrEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import java.util.UUID;
import com.google.common.net.UrlEscapers;
import javax.annotation.Nonnull;
import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.models.ChecksumType;
public class PutObjectRequest extends AbstractRequest {

    final static private Logger LOG = LoggerFactory.getLogger(PutObjectRequest.class);

    // Variables
    public final static String AMZ_META_HEADER = "x-amz-meta-";

    private final InputStream stream;
    
    private final String bucketName;

    private final String objectName;

    private final long size;

    private String job;

    private long offset;
    private SeekableByteChannel channel;
    private ChecksumType checksum = ChecksumType.none();
    private ChecksumType.Type checksumType = ChecksumType.Type.NONE;

    // Constructor
    
    /** @deprecated use {@link #PutObjectRequest(String, String, SeekableByteChannel, UUID, long, long)} instead */
    @Deprecated
    public PutObjectRequest(final String bucketName, final String objectName, @Nonnull final SeekableByteChannel channel, final long size) {
        Preconditions.checkNotNull(channel, "Channel may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.size = size;
        this.channel = channel;
        this.stream = new SeekableByteChannelInputStream(channel);
        

    }

    
    public PutObjectRequest(final String bucketName, final String objectName, @Nonnull final SeekableByteChannel channel, final UUID job, final long offset, final long size) {
        Preconditions.checkNotNull(channel, "Channel may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.size = size;
        this.job = job.toString();
        this.offset = offset;
        this.channel = channel;
        this.stream = new SeekableByteChannelInputStream(channel);
        
        this.updateQueryParam("job", job);

        this.updateQueryParam("offset", offset);


    }

    
    public PutObjectRequest(final String bucketName, final String objectName, @Nonnull final SeekableByteChannel channel, final String job, final long offset, final long size) {
        Preconditions.checkNotNull(channel, "Channel may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.size = size;
        this.job = job;
        this.offset = offset;
        this.channel = channel;
        this.stream = new SeekableByteChannelInputStream(channel);
        
        this.updateQueryParam("job", job);

        this.updateQueryParam("offset", offset);


    }

    
    public PutObjectRequest(final String bucketName, final String objectName, final UUID job, final long offset, final long size, @Nonnull final InputStream stream) {
        Preconditions.checkNotNull(stream, "Stream may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.size = size;
        this.job = job.toString();
        this.offset = offset;
        this.stream = stream;
        
        this.updateQueryParam("job", job);

        this.updateQueryParam("offset", offset);


    }

    
    public PutObjectRequest(final String bucketName, final String objectName, final String job, final long offset, final long size, @Nonnull final InputStream stream) {
        Preconditions.checkNotNull(stream, "Stream may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.size = size;
        this.job = job;
        this.offset = offset;
        this.stream = stream;
        
        this.updateQueryParam("job", job);

        this.updateQueryParam("offset", offset);


    }


    public PutObjectRequest withJob(final UUID job) {
        this.job = job.toString();
        this.updateQueryParam("job", job);
        return this;
    }


    public PutObjectRequest withJob(final String job) {
        this.job = job;
        this.updateQueryParam("job", job);
        return this;
    }


    public PutObjectRequest withOffset(final long offset) {
        this.offset = offset;
        this.updateQueryParam("offset", offset);
        return this;
    }



    /**
     * Set a MD5 checksum for the request.
     */
    public PutObjectRequest withChecksum(final ChecksumType checksum) {
        return withChecksum(checksum, ChecksumType.Type.MD5);
    }

    public PutObjectRequest withChecksum(final ChecksumType checksum, final ChecksumType.Type checksumType) {
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


    public PutObjectRequest withMetaData(final String key, final String value) {
        if (isStringNullOrEmpty(value)) {
            LOG.warn("Key has not been added to metadata because value was null or empty: {}", key);
            return this;
        }
        final String modifiedKey;
        if (!key.toLowerCase().startsWith(AMZ_META_HEADER)){
            modifiedKey = AMZ_META_HEADER + key.toLowerCase();
        } else {
            modifiedKey = key;
        }
        this.getHeaders().put(modifiedKey, value);
        return this;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/" + this.bucketName + "/" + this.objectName;
    }
    @Override
    public long getSize() {
        return this.size;
    }


    @Override
    public InputStream getStream() {
        return this.stream;
    }


    public SeekableByteChannel getChannel() {
        return this.channel;
    }


    public String getBucketName() {
        return this.bucketName;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public String getJob() {
        return this.job;
    }

    public long getOffset() {
        return this.offset;
    }


}