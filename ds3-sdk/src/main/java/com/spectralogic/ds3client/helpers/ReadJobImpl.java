/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers;

import com.google.common.collect.*;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.exceptions.ContentLengthNotMatchException;
import com.spectralogic.ds3client.helpers.ChunkTransferrer.ItemTransferrer;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.helpers.events.EventRunner;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.helpers.strategy.BlobStrategy;
import com.spectralogic.ds3client.helpers.strategy.GetStreamerStrategy;
import com.spectralogic.ds3client.helpers.util.PartialObjectHelpers;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.Guard;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

class ReadJobImpl extends JobImpl {

    private final ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> blobToRanges;
    private final Set<MetadataReceivedListener> metadataListeners;

    private final int retryAfter; // Negative retryAfter value represent infinity retries
    private final int retryDelay; // Negative value represents default

    public ReadJobImpl(
            final Ds3Client client,
            final MasterObjectList masterObjectList,
            final ImmutableMultimap<String, Range> objectRanges,
            final int objectTransferAttempts,
            final int retryAfter,
            final int retryDelay,
            final EventRunner eventRunner
    ) {
        super(client, masterObjectList, objectTransferAttempts, eventRunner);

        this.blobToRanges = PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(), objectRanges);
        this.metadataListeners = Sets.newIdentityHashSet();

        this.retryAfter = retryAfter;
        this.retryDelay = retryDelay;
    }

    protected static ImmutableList<BulkObject> getAllBlobApiBeans(final List<Objects> jobWithChunksApiBeans) {
        final ImmutableList.Builder<BulkObject> builder = ImmutableList.builder();
        for (final Objects objects : jobWithChunksApiBeans) {
            builder.addAll(objects.getObjects());
        }
        return builder.build();
    }

    @Override
    public void attachMetadataReceivedListener(final MetadataReceivedListener listener) {
        checkRunning();
        this.metadataListeners.add(listener);
    }

    @Override
    public void removeMetadataReceivedListener(final MetadataReceivedListener listener) {
        checkRunning();
        this.metadataListeners.remove(listener);
    }

    @Override
    public Ds3ClientHelpers.Job withMetadata(final Ds3ClientHelpers.MetadataAccess access) {
        throw new IllegalStateException("withMetadata method is not used with Read Jobs");
    }

    @Override
    public Ds3ClientHelpers.Job withChecksum(final ChecksumFunction checksumFunction) {
        throw new IllegalStateException("withChecksum is not supported on Read Jobs");
    }

    @Override
    public void transfer(final ObjectChannelBuilder channelBuilder)
            throws IOException {
        try {
            running = true;

            final BlobStrategy strategy = new GetStreamerStrategy(
                    client,
                    this.masterObjectList,
                    retryAfter,
                    retryDelay,
                    new GetStreamerStrategy.ChunkEventHandler() {
                        @Override
                        public void emitWaitingForChunksEvents(final int secondsToDelay) {
                            ReadJobImpl.super.emitWaitingForChunksEvents(secondsToDelay);
                        }
                    });

            try (final JobState jobState = new JobState(
                    channelBuilder,
                    this.masterObjectList.getObjects(),
                    getJobPartTracker(), blobToRanges)) {
                try (final ChunkTransferrer chunkTransferrer = new ChunkTransferrer(
                        new GetObjectTransferrerRetryDecorator(jobState),
                        jobState.getPartTracker(),
                        this.maxParallelRequests
                )) {
                    while (jobState.hasObjects()) {
                        chunkTransferrer.transferChunks(strategy);
                    }
                }
            } catch (final RuntimeException | IOException e) {
                throw e;
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        } catch (final Throwable t) {
            emitFailureEvent(makeFailureEvent(FailureEvent.FailureActivity.GettingObject, t, masterObjectList.getObjects().get(0)));
            throw t;
        }
    }

    @Override
    protected List<Objects> getChunks(final MasterObjectList masterObjectList) {
        return masterObjectList.getObjects();
    }

    @Override
    protected JobPartTrackerDecorator makeJobPartTracker(final List<Objects> chunks, final EventRunner eventRunner) {
        return new JobPartTrackerDecorator(chunks, eventRunner);
    }

    private final class GetObjectTransferrerRetryDecorator implements ItemTransferrer {
        private final ItemTransferrer getObjectTransferrer;

        private GetObjectTransferrerRetryDecorator(final JobState jobState) {
            getObjectTransferrer = new GetObjectTransferrerNetworkFailureDecorator(jobState);
        }

        @Override
        public void transferItem(final Ds3Client client, final BulkObject ds3Object) throws IOException {
            ReadJobImpl.this.transferItem(client, ds3Object, getObjectTransferrer);
        }
    }

    private final class GetObjectTransferrerNetworkFailureDecorator implements ItemTransferrer {
        private final JobState jobState;
        private Long numBytesToTransfer;
        private ImmutableCollection<Range> ranges;
        private final AtomicLong destinationChannelOffset = new AtomicLong(0);
        private final AtomicReference<ItemTransferrer> itemTransferrer = new AtomicReference<>();

        private GetObjectTransferrerNetworkFailureDecorator(final JobState jobState) {
            this.jobState = jobState;
            itemTransferrer.set(new GetObjectTransferrer(jobState));
        }

        @Override
        public void transferItem(final Ds3Client client, final BulkObject ds3Object) throws IOException {
            try {
                itemTransferrer.get().transferItem(client, ds3Object);
            } catch (final ContentLengthNotMatchException e) {
                initializeRangesAndTransferSize(ds3Object);
                updateRanges(e.getTotalBytes());
                destinationChannelOffset.getAndAdd(e.getTotalBytes());
                final long objectLength = RangeHelper.transferSizeForRanges(ranges);

                final GetObjectRequest getObjectRequest = new GetObjectRequest(
                        ReadJobImpl.this.masterObjectList.getBucketName(),
                        ds3Object.getName(),
                        jobState.getChannel(ds3Object.getName(), destinationChannelOffset.get(), objectLength),
                        ReadJobImpl.this.getJobId().toString(),
                        ds3Object.getOffset()
                );

                getObjectRequest.withByteRanges(ranges);

                itemTransferrer.set(new GetPartialObjectTransferrer(getObjectRequest));

                emitContentLengthMismatchFailureEvent(ds3Object, e);

                throw new RecoverableIOException(e);
            }
        }

        private synchronized void initializeRangesAndTransferSize(final BulkObject ds3Object) {
            if (ranges == null) {
                ranges = getRangesForBlob(blobToRanges, ds3Object);
            }

            if (ranges == null) {
                ranges = RangeHelper.replaceRange(ranges, 0, ds3Object.getLength());
            }

            if (numBytesToTransfer == null) {
                numBytesToTransfer = RangeHelper.transferSizeForRanges(ranges);
            }
        }

        private synchronized void updateRanges(final long numBytesTransferred) {
            ranges = RangeHelper.replaceRange(ranges, numBytesTransferred, numBytesToTransfer);
        }

        private void emitContentLengthMismatchFailureEvent(final BulkObject ds3Object, final Throwable t) {
            final FailureEvent failureEvent = new FailureEvent.Builder()
                    .doingWhat(FailureEvent.FailureActivity.GettingObject)
                    .usingSystemWithEndpoint(client.getConnectionDetails().getEndpoint())
                    .withCausalException(t)
                    .withObjectNamed(ds3Object.getName())
                    .build();
            emitFailureEvent(failureEvent);
        }
    }

    private final class GetPartialObjectTransferrer implements ItemTransferrer {
        private final GetObjectRequest getObjectRequest;

        private GetPartialObjectTransferrer(final GetObjectRequest getObjectRequest) {
            this.getObjectRequest = getObjectRequest;
        }

        @Override
        public void transferItem(final Ds3Client client, final BulkObject ds3Object)
                throws IOException {

            final GetObjectResponse response = client.getObject(getObjectRequest);
            final Metadata metadata = response.getMetadata();

            emitChecksumEvents(ds3Object, response.getChecksumType(), response.getChecksum());
            sendMetadataEvents(ds3Object.getName(), metadata);
        }
    }

    private final class GetObjectTransferrer implements ItemTransferrer {
        private final JobState jobState;

        private GetObjectTransferrer(final JobState jobState) {
            this.jobState = jobState;
        }

        @Override
        public void transferItem(final Ds3Client client, final BulkObject ds3Object)
                throws IOException {

            final ImmutableCollection<Range> ranges = getRangesForBlob(blobToRanges, ds3Object);

            final GetObjectRequest request = new GetObjectRequest(
                    ReadJobImpl.this.masterObjectList.getBucketName(),
                    ds3Object.getName(),
                    jobState.getChannel(ds3Object.getName(), ds3Object.getOffset(), ds3Object.getLength()),
                    ReadJobImpl.this.getJobId().toString(),
                    ds3Object.getOffset()
            );

            if (Guard.isNotNullAndNotEmpty(ranges)) {
                request.withByteRanges(ranges);
            }

            final GetObjectResponse response = client.getObject(request);
            final Metadata metadata = response.getMetadata();

            emitChecksumEvents(ds3Object, response.getChecksumType(), response.getChecksum());
            sendMetadataEvents(ds3Object.getName(), metadata);
        }
    }

    private void sendMetadataEvents(final String objName , final Metadata metadata) {
        for (final MetadataReceivedListener listener : this.metadataListeners) {
            getEventRunner().emitEvent(new Runnable() {
                @Override
                public void run() {
                    listener.metadataReceived(objName, metadata);
                }
            });
        }
    }

    private static ImmutableCollection<Range> getRangesForBlob(
            final ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> blobToRanges,
            final BulkObject ds3Object) {
        final ImmutableMultimap<BulkObject, Range> ranges =  blobToRanges.get(ds3Object.getName());
        if (ranges == null) return null;
        return ranges.get(ds3Object);
    }
}
