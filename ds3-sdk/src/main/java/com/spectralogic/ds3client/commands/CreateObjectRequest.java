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
package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;
import java.io.InputStream;
import java.nio.channels.SeekableByteChannel;
import java.util.UUID;
import com.spectralogic.ds3client.models.ChecksumType;
public class CreateObjectRequest extends AbstractRequest {

    // Variables
    public final static String AMZ_META_HEADER = "x-amz-meta-";

    private final InputStream stream;
    private final long size;
    
    private final String bucketName;

    private final String objectName;

    private UUID job;
    private long offset;
    private SeekableByteChannel channel;
    private ChecksumType checksum = ChecksumType.none();
    private ChecksumType.Type checksumType = ChecksumType.Type.NONE;

    // Constructor

    /**
     * @deprecated use {@link #CreateObjectRequest(String, String, SeekableByteChannel, UUID, long, long) instead
     */
    @Deprecated
    public CreateObjectRequest(final String bucketName, final String objectName, final SeekableByteChannel channel, final long size) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.size = size;
        this.channel = channel;
        this.stream = new SeekableByteChannelInputStream(channel);
    }

    public CreateObjectRequest(final String bucketName, final String objectName, final SeekableByteChannel channel, final UUID job, final long offset, final long size) {
        this(bucketName, objectName, job, offset, size, new SeekableByteChannelInputStream(channel));

        this.channel = channel;
    }

    public CreateObjectRequest(final String bucketName, final String objectName, final UUID job, final long offset, final long size, final InputStream stream) {
            this.bucketName = bucketName;
            this.objectName = objectName;
            this.job = job;
            this.offset = offset;
            this.size = size;
            this.stream = stream;

            this.getQueryParams().put("job", job.toString());
            this.getQueryParams().put("offset", Long.toString(offset));
        }

    public CreateObjectRequest withJob(final UUID job) {
        this.job = job;
        this.updateQueryParam("job", job.toString());
        return this;
    }

    public CreateObjectRequest withOffset(final long offset) {
        this.offset = offset;
        this.updateQueryParam("offset", Long.toString(offset));
        return this;
    }


    /**
     * Set a MD5 checksum for the request.
     */
    public CreateObjectRequest withChecksum(final ChecksumType checksum) {
        return withChecksum(checksum, ChecksumType.Type.MD5);
    }

    public CreateObjectRequest withChecksum(final ChecksumType checksum, final ChecksumType.Type checksumType) {
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


	public CreateObjectRequest withMetaData(final String key, final String value) {
		final String modifiedKey;
		if (!key.toLowerCase().startsWith(AMZ_META_HEADER)){
			modifiedKey = AMZ_META_HEADER + key;
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


    
    public String getBucketName() {
        return this.bucketName;
    }


    public String getObjectName() {
        return this.objectName;
    }


    public UUID getJob() {
        return this.job;
    }

    public long getOffset() {
        return this.offset;
    }


    public SeekableByteChannel getChannel() {
        return this.channel;
    }

}