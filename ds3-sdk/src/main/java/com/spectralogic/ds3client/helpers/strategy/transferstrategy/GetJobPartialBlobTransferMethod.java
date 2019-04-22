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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.ChannelStrategy;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.common.Range;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

/**
 * When a blob transfer retrieves less data than is needed to compete the transfer, the {@link GetJobNetworkFailureRetryDecorator}
 * that recognizes that situation will create an instance of this class.  This class aggregates the data needed to
 * transfer the remainder of blob data.
 */
public class GetJobPartialBlobTransferMethod implements TransferMethod {
    private final ChannelStrategy channelStrategy;
    private final String bucketName;
    private final String jobId;
    private final EventDispatcher eventDispatcher;
    private final ImmutableCollection<Range> blobRanges;
    private final long destinationChannelOffset;

    public GetJobPartialBlobTransferMethod(final ChannelStrategy channelStrategy,
                                           final String bucketName,
                                           final String jobId,
                                           final EventDispatcher eventDispatcher,
                                           final ImmutableCollection<Range> blobRanges,
                                           final long destinationChannelOffset)
    {
        Preconditions.checkNotNull(blobRanges, "blobRanges may not be null.");
        Preconditions.checkState(destinationChannelOffset >= 0, "destinationChannelOffset must be >= 0.");

        this.channelStrategy = channelStrategy;
        this.bucketName = bucketName;
        this.jobId = jobId;
        this.eventDispatcher = eventDispatcher;
        this.blobRanges = blobRanges;
        this.destinationChannelOffset = destinationChannelOffset;
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

        seekableByteChannel.position(destinationChannelOffset);

        try {
            final GetObjectResponse getObjectResponse = jobPart.getClient().getObject(makeGetObjectRequest(seekableByteChannel, blob));

            eventDispatcher.emitChecksumEvent(blob, getObjectResponse.getChecksumType(), getObjectResponse.getChecksum());
            eventDispatcher.emitMetaDataReceivedEvent(blob.getName(), getObjectResponse.getMetadata());

            eventDispatcher.emitBlobTransferredEvent(blob);
            eventDispatcher.emitDataTransferredEvent(blob);
        } finally {
            channelStrategy.releaseChannelForBlob(seekableByteChannel, blob);
        }
    }

    private GetObjectRequest makeGetObjectRequest(final SeekableByteChannel seekableByteChannel, final BulkObject blob) {
        final GetObjectRequest getObjectRequest = new GetObjectRequest(
                bucketName,
                blob.getName(),
                seekableByteChannel,
                jobId,
                blob.getOffset());

        if (blob.getVersionId() != null) {
            getObjectRequest.withVersionId(blob.getVersionId());
        }
        getObjectRequest.withByteRanges(blobRanges);

        return getObjectRequest;
    }
}
