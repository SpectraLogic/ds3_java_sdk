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

package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.helpers.ChecksumFunction;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.MetadataAccess;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.ChannelStrategy;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.ChecksumType;

import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.util.Map;

import com.spectralogic.ds3client.utils.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The HTTP PUT implementation class that retrieves a blob from a Black Pearl.
 */
public class PutJobTransferMethod implements TransferMethod {
    private static final Logger LOG = LoggerFactory.getLogger(PutJobTransferMethod.class);

    private final ChannelStrategy channelStrategy;
    private final String bucketName;
    private final String jobId;
    private final EventDispatcher eventDispatcher;
    private final ChecksumFunction checksumFunction;
    private final ChecksumType.Type checksumType;
    private final MetadataAccess metadataAccess;

    public PutJobTransferMethod(final ChannelStrategy channelStrategy,
                                final String bucketName,
                                final String jobId,
                                final EventDispatcher eventDispatcher,
                                final ChecksumFunction checksumFunction,
                                final ChecksumType.Type checksumType,
                                final MetadataAccess metadataAccess)
    {
        this.channelStrategy = channelStrategy;
        this.bucketName = bucketName;
        this.jobId = jobId;
        this.eventDispatcher = eventDispatcher;
        this.checksumFunction = checksumFunction;
        this.checksumType = checksumType;
        this.metadataAccess = metadataAccess;
    }

    /**
     * @param jobPart An instance of {@link JobPart}, which tells us which Black Pearl is the source
     *                or destination for a blob transfer.
     * @throws IOException
     */
    @Override
    public void transferJobPart(final JobPart jobPart) throws IOException {
        final BulkObject blob = jobPart.getBlob();

        final SeekableByteChannel seekableByteChannel = channelStrategy.acquireChannelForBlob(blob);

        jobPart.getClient().putObject(makePutObjectRequest(seekableByteChannel, jobPart));

        channelStrategy.releaseChannelForBlob(seekableByteChannel, blob);

        eventDispatcher.emitBlobTransferredEvent(blob);
        eventDispatcher.emitDataTransferredEvent(blob);
    }

    private PutObjectRequest makePutObjectRequest(final SeekableByteChannel seekableByteChannel, final JobPart jobPart) {
        final BulkObject blob = jobPart.getBlob();

        final PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName,
                blob.getName(),
                seekableByteChannel,
                jobId,
                blob.getOffset(),
                blob.getLength());

        addCheckSum(blob, putObjectRequest);
        addMetadata(blob, putObjectRequest);

        return putObjectRequest;
    }

    private void addCheckSum(final BulkObject blob, final PutObjectRequest putObjectRequest) {
        if (checksumFunction != null) {
            final String checksum;

            try {
                final ByteChannel byteChannel = channelStrategy.acquireChannelForBlob(blob);
                checksum = checksumFunction.compute(blob, byteChannel);

                if (checksum != null) {
                    putObjectRequest.withChecksum(ChecksumType.value(checksum), checksumType);
                    eventDispatcher.emitChecksumEvent(blob, checksumType, checksum);
                }
            }  catch (final IOException e) {
                // TODO Emit a failure event here
                LOG.info("Failure creating channel to calculate checksum.", e);
            }
        }
    }

    private void addMetadata(final BulkObject blob, final PutObjectRequest putObjectRequest) {
        if (metadataAccess != null) {
            final Map<String, String> metadata = metadataAccess.getMetadataValue(blob.getName());
            if ( ! Guard.isMapNullOrEmpty(metadata)) {
                final ImmutableMap<String, String> immutableMetadata = ImmutableMap.copyOf(metadata);
                for (final Map.Entry<String, String> value : immutableMetadata.entrySet()) {
                    putObjectRequest.withMetaData(value.getKey(), value.getValue());
                }
            }
        }
    }
}
