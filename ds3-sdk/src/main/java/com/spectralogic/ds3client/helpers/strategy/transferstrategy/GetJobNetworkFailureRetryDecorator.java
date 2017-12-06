/*
 * ****************************************************************************
 *    Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.spectralogic.ds3client.exceptions.ContentLengthNotMatchException;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.RecoverableIOException;
import com.spectralogic.ds3client.helpers.strategy.StrategyUtils;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.ChannelStrategy;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.utils.Guard;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of {@link TransferMethod} that wraps another instance of TransferMethod to provide the
 * ability to resume a get operation that transfers less data than is needed for a blob transfer to complete.
 */
public class GetJobNetworkFailureRetryDecorator implements TransferMethod {
    private final ChannelStrategy channelStrategy;
    private final String bucketName;
    private final String jobId;
    private final EventDispatcher eventDispatcher;
    private final ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> rangesForBlobs;

    private final Map<JobPart, TransferState> transferMethodMap = new HashMap<>();

    public GetJobNetworkFailureRetryDecorator(final ChannelStrategy channelStrategy,
                                              final String bucketName,
                                              final String jobId,
                                              final EventDispatcher eventDispatcher,
                                              final ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> rangesForBlobs)
    {
        this.channelStrategy = channelStrategy;
        this.bucketName = bucketName;
        this.jobId = jobId;
        this.eventDispatcher = eventDispatcher;
        this.rangesForBlobs = rangesForBlobs;
    }

    @Override
    public void transferJobPart(final JobPart jobPart) throws IOException {
        try {
            getOrMakeTransferMethod(jobPart).transferJobPart(jobPart);
        } catch (final ContentLengthNotMatchException contentLengthNotMatchException) {
            makeTransferMethodForPartialRetry(jobPart, contentLengthNotMatchException.getTotalBytes());
            throw new RecoverableIOException(contentLengthNotMatchException);
        }
    }

    private synchronized TransferMethod getOrMakeTransferMethod(final JobPart jobPart) {
        TransferState transferState = transferMethodMap.get(jobPart);

        if (transferState != null) {
            return transferState.getTransferMethod();
        }

        transferState = makeInitialTransferState();

        transferMethodMap.put(jobPart, transferState);

        return transferState.getTransferMethod();
    }

    private TransferState makeInitialTransferState() {
        final ImmutableCollection<Range> ranges = null;
        final Long numBytesToTransfer = null;
        final long destinationChannelOffset = 0L;

        return new TransferState(
                ranges,
                numBytesToTransfer,
                destinationChannelOffset,
                new GetJobTransferMethod(channelStrategy, bucketName, jobId, eventDispatcher, rangesForBlobs)
        );
    }

    private synchronized void makeTransferMethodForPartialRetry(final JobPart jobPart, final long numBytesTransferred) {
        final TransferState existingTransferState = transferMethodMap.get(jobPart);

        ImmutableCollection<Range> ranges = initializeRanges(jobPart.getBlob(), existingTransferState);
        final Long numBytesToTransfer = initializeNumBytesToTransfer(existingTransferState, ranges);
        ranges = updateRanges(ranges, numBytesTransferred, numBytesToTransfer);
        final long destinationChannelOffset = existingTransferState.getDestinationChannelOffset() + numBytesTransferred;

        final TransferState newTransferState = new TransferState(
                ranges,
                numBytesToTransfer,
                destinationChannelOffset,
                new GetJobPartialBlobTransferMethod(channelStrategy,
                        bucketName,
                        jobId,
                        eventDispatcher,
                        ranges,
                        destinationChannelOffset)
        );

        transferMethodMap.put(jobPart, newTransferState);
    }

    private ImmutableCollection<Range> initializeRanges(final BulkObject blob, final TransferState existingTransferState) {
        ImmutableCollection<Range> ranges = existingTransferState.getRanges();

        if (ranges == null) {
            ranges = StrategyUtils.getRangesForBlob(rangesForBlobs, blob);
        }

        if (ranges == null) {
            final long numBytesTransferred = 0;
            ranges = adjustRangesForBlobOffset(updateRanges(ranges, numBytesTransferred, blob.getLength()), blob);
        }

        return ranges;
    }

    private ImmutableCollection<Range> adjustRangesForBlobOffset(final ImmutableCollection<Range> ranges, final BulkObject blob) {
        if (Guard.isNullOrEmpty(ranges) || ranges.size() > 1) {
            return ranges;
        }

        final Range firstRange = ranges.iterator().next();

        final long blobOffset = blob.getOffset();

        return ImmutableList.of(new Range(firstRange.getStart() + blobOffset, firstRange.getEnd() + blobOffset));
    }

    private ImmutableCollection<Range> updateRanges(final ImmutableCollection<Range> ranges,
                                                    final long numBytesTransferred,
                                                    final Long numBytesToTransfer)
    {
        return RangeHelper.replaceRange(ranges, numBytesTransferred, numBytesToTransfer);
    }

    private Long initializeNumBytesToTransfer(final TransferState existingTransferState,
                                              final ImmutableCollection<Range> ranges)
    {
        Long numBytesToTransfer = existingTransferState.getNumBytesToTransfer();

        if (numBytesToTransfer == null) {
            numBytesToTransfer = RangeHelper.transferSizeForRanges(ranges);
        }

        return numBytesToTransfer;
    }

    private class TransferState {
        private final ImmutableCollection<Range> ranges;
        private final Long numBytesToTransfer;
        private final Long destinationChannelOffset;
        private final TransferMethod transferMethod;

        private TransferState(final ImmutableCollection<Range> ranges,
                              final Long numBytesToTransfer,
                              final Long destinationChannelOffset,
                              final TransferMethod transferMethod)
        {
            this.ranges = ranges;
            this.numBytesToTransfer = numBytesToTransfer;
            this.destinationChannelOffset = destinationChannelOffset;
            this.transferMethod = transferMethod;
        }

        public ImmutableCollection<Range> getRanges() {
            return ranges;
        }

        public Long getNumBytesToTransfer() {
            return numBytesToTransfer;
        }

        public Long getDestinationChannelOffset() {
            return destinationChannelOffset;
        }

        public TransferMethod getTransferMethod() {
            return transferMethod;
        }
    }
}
