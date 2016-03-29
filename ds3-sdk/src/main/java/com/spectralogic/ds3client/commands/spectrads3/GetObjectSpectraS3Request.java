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
package com.spectralogic.ds3client.commands.spectrads3;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.models.common.Range;
import org.apache.http.entity.ContentType;
import java.nio.channels.WritableByteChannel;
import java.util.Collection;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.ChecksumType;
public class GetObjectSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String objectName;

    private final WritableByteChannel channel;

    private final String bucketId;
    private ImmutableCollection<Range> byteRanges = null;
    private ChecksumType checksum = ChecksumType.none();
    private ChecksumType.Type checksumType = ChecksumType.Type.NONE;

    // Constructor
    public GetObjectSpectraS3Request(final String objectName, final String bucketId, final WritableByteChannel channel) {
        this.objectName = objectName;
        this.channel = channel;
        this.bucketId = bucketId;
        
        this.getQueryParams().put("bucket_id", bucketId);

    }



    /**
     * Set a MD5 checksum for the request.
     */
    public GetObjectSpectraS3Request withChecksum(final ChecksumType checksum) {
        return withChecksum(checksum, ChecksumType.Type.MD5);
    }

    public GetObjectSpectraS3Request withChecksum(final ChecksumType checksum, final ChecksumType.Type checksumType) {
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
    public GetObjectSpectraS3Request withByteRanges(final Range... byteRanges) {
        if (byteRanges != null) {
            this.setRanges(ImmutableList.copyOf(byteRanges));
        }
        return this;
    }

    public GetObjectSpectraS3Request withByteRanges(final Collection<Range> byteRanges) {
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
        return "/_rest_/object/" + objectName;
    }
    @Override
    public String getContentType() {
        return ContentType.APPLICATION_OCTET_STREAM.toString();
    }

    
    public String getObjectName() {
        return this.objectName;
    }


    public WritableByteChannel getChannel() {
        return this.channel;
    }


    public String getBucketId() {
        return this.bucketId;
    }


    public Collection<Range> getByteRanges() {
        return this.byteRanges;
    }


}