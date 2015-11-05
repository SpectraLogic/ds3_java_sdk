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
import com.spectralogic.ds3client.models.Checksum;
import com.sun.tools.javac.comp.Check;

import java.io.InputStream;
import java.nio.channels.SeekableByteChannel;
import java.util.UUID;

/**
 * Maps to a DS3 Single Object Put request.
 */
public class PutObjectRequest extends AbstractRequest {

    private final String bucketName;
    private final String objectName;
    private final UUID jobId;
    private final SeekableByteChannel channel;
    private final InputStream stream;
    private final long size;
    private final long offset;
    private Checksum checksum = Checksum.none();
    private Checksum.Type checksumType = Checksum.Type.NONE;
    public final static String AMZ_META_HEADER = "x-amz-meta-";

    /**
     * Creates a request to put a request within the context of a bulk job.  This is the preferred method of creating a put object request.
     * See {@link BulkPutRequest} for more information on creating a bulk put request.
    */
    public PutObjectRequest(final String bucketName, final String objectName, final UUID jobId, final long size, final long offset, final SeekableByteChannel channel) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.channel = channel;
        this.stream = new SeekableByteChannelInputStream(channel);
        this.size = size;
        this.jobId = jobId;
        this.offset = offset;

        this.getQueryParams().put("job", jobId.toString());
        this.getQueryParams().put("offset", Long.toString(offset));
    }

    /**
     * Set a md5 checksum for the request.
     */
    public PutObjectRequest withChecksum(final Checksum checksum) {
        return withChecksum(checksum, Checksum.Type.MD5);
    }

    public PutObjectRequest withChecksum(final Checksum checksum, final Checksum.Type checksumType) {
        this.checksum = checksum;
        this.checksumType = checksumType;
        return this;
    }

	public PutObjectRequest withMetaData(final String key, final String value) {
		final String modifiedKey;
		if (!key.toLowerCase().startsWith(AMZ_META_HEADER)){
			modifiedKey = AMZ_META_HEADER + key;
		} else {
			modifiedKey = key;
		}
		this.getHeaders().put(modifiedKey, value);
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
    public Checksum.Type getChecksumType() {
        return this.checksumType;
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

    public SeekableByteChannel getChannel() {
        return this.channel;
    }

    public UUID getJobId() {
        return this.jobId;
    }

    public long getOffset() {
        return offset;
    }
}
