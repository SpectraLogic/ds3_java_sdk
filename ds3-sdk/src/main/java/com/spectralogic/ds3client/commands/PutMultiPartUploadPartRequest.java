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
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import java.util.UUID;
import com.google.common.net.UrlEscapers;
import javax.annotation.Nonnull;
import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.utils.SeekableByteChannelInputStream;
import java.nio.channels.SeekableByteChannel;
import java.io.InputStream;

public class PutMultiPartUploadPartRequest extends AbstractRequest {

    // Variables
    
    private final String bucketName;

    private final String objectName;

    private final int partNumber;

    private final String uploadId;

    private final long size;

    private final InputStream stream;

    private SeekableByteChannel channel;

    // Constructor
    
    
    public PutMultiPartUploadPartRequest(final String bucketName, final String objectName, @Nonnull final SeekableByteChannel channel, final int partNumber, final long size, final UUID uploadId) {
        Preconditions.checkNotNull(channel, "Channel may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.partNumber = partNumber;
        this.uploadId = uploadId.toString();
        this.size = size;
        this.channel = channel;
        
        this.updateQueryParam("part_number", partNumber);

        this.updateQueryParam("upload_id", uploadId);

        this.stream = new SeekableByteChannelInputStream(channel);
    }

    
    public PutMultiPartUploadPartRequest(final String bucketName, final String objectName, @Nonnull final SeekableByteChannel channel, final int partNumber, final long size, final String uploadId) {
        Preconditions.checkNotNull(channel, "Channel may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.partNumber = partNumber;
        this.uploadId = uploadId;
        this.size = size;
        this.channel = channel;
        
        this.updateQueryParam("part_number", partNumber);

        this.updateQueryParam("upload_id", uploadId);

        this.stream = new SeekableByteChannelInputStream(channel);
    }

    
    public PutMultiPartUploadPartRequest(final String bucketName, final String objectName, final int partNumber, final long size, @Nonnull final InputStream stream, final UUID uploadId) {
        Preconditions.checkNotNull(stream, "Stream may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.partNumber = partNumber;
        this.uploadId = uploadId.toString();
        this.size = size;
        this.stream = stream;
        
        this.updateQueryParam("part_number", partNumber);

        this.updateQueryParam("upload_id", uploadId);

    }

    
    public PutMultiPartUploadPartRequest(final String bucketName, final String objectName, final int partNumber, final long size, @Nonnull final InputStream stream, final String uploadId) {
        Preconditions.checkNotNull(stream, "Stream may not be null.");
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.partNumber = partNumber;
        this.uploadId = uploadId;
        this.size = size;
        this.stream = stream;
        
        this.updateQueryParam("part_number", partNumber);

        this.updateQueryParam("upload_id", uploadId);

    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/" + this.bucketName + "/" + this.objectName;
    }
    
    public String getBucketName() {
        return this.bucketName;
    }


    public String getObjectName() {
        return this.objectName;
    }


    public int getPartNumber() {
        return this.partNumber;
    }


    public String getUploadId() {
        return this.uploadId;
    }


    public long getSize() {
        return this.size;
    }


    public InputStream getStream() {
        return this.stream;
    }


    public SeekableByteChannel getChannel() {
        return this.channel;
    }

}
