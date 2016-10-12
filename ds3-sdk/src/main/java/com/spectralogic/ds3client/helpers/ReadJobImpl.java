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
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Response;
import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;
import com.spectralogic.ds3client.helpers.ChunkTransferrer.ItemTransferrer;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.helpers.events.EventRunner;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.helpers.util.PartialObjectHelpers;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.Guard;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReadJobImpl extends JobImpl {
    private static final Logger LOG = LoggerFactory.getLogger(ReadJobImpl.class);

    private final JobPartTracker partTracker;
    private final List<Objects> chunks;
    private final ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> blobToRanges;
    private final Set<MetadataReceivedListener> metadataListeners;
    private final Set<ChecksumListener> checksumListeners;
    private final Set<WaitingForChunksListener> waitingForChunksListeners;
    private final Set<FailureEventListener> failureEventListeners;
    private final int retryAfter; // Negative retryAfter value represent infinity retries
    private final int retryDelay; // Negative value represents default
    private final EventRunner eventRunner;

    private int retryAfterLeft; // The number of retries left

    public ReadJobImpl(
            final Ds3Client client,
            final MasterObjectList masterObjectList,
            final ImmutableMultimap<String, Range> objectRanges,
            final int objectTransferAttempts,
            final int retryAfter,
            final int retryDelay,
            final EventRunner eventRunner
            ) {
        super(client, masterObjectList, objectTransferAttempts);

        this.chunks = this.masterObjectList.getObjects();
        this.partTracker = JobPartTrackerFactory
                .buildPartTracker(getAllBlobApiBeans(chunks), eventRunner);
        this.blobToRanges = PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(), objectRanges);
        this.metadataListeners = Sets.newIdentityHashSet();
        this.checksumListeners = Sets.newIdentityHashSet();
        this.waitingForChunksListeners = Sets.newIdentityHashSet();
        this.failureEventListeners = Sets.newIdentityHashSet();
        this.eventRunner = eventRunner;

        this.retryAfter = this.retryAfterLeft = retryAfter;
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
    public void attachDataTransferredListener(final DataTransferredListener listener) {
        checkRunning();
        this.partTracker.attachDataTransferredListener(listener);
    }

    @Override
    public void attachObjectCompletedListener(final ObjectCompletedListener listener) {
        checkRunning();
        this.partTracker.attachObjectCompletedListener(listener);
    }

    @Override
    public void removeDataTransferredListener(final DataTransferredListener listener) {
        checkRunning();
        this.partTracker.removeDataTransferredListener(listener);
    }

    @Override
    public void removeObjectCompletedListener(final ObjectCompletedListener listener) {
        checkRunning();
        this.partTracker.removeObjectCompletedListener(listener);
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
    public void attachChecksumListener(final ChecksumListener listener) {
        checkRunning();
        this.checksumListeners.add(listener);
    }

    @Override
    public void removeChecksumListener(final ChecksumListener listener) {
        checkRunning();
        this.checksumListeners.remove(listener);
    }

    @Override
    public void attachWaitingForChunksListener(final WaitingForChunksListener listener) {
        checkRunning();
        this.waitingForChunksListeners.add(listener);
    }

    @Override
    public void removeWaitingForChunksListener(final WaitingForChunksListener listener) {
        checkRunning();
        this.waitingForChunksListeners.remove(listener);
    }

    @Override
    public void attachFailureEventListener(final FailureEventListener listener) {
        failureEventListeners.add(listener);
    }

    @Override
    public void removeFailureEventListener(final FailureEventListener listener) {
        failureEventListeners.remove(listener);
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
            throws IOException
    {
        running = true;
        try (final JobState jobState = new JobState(
                channelBuilder,
                this.masterObjectList.getObjects(),
                partTracker, blobToRanges)) {
            final ChunkTransferrer chunkTransferrer = new ChunkTransferrer(
                    new GetObjectTransferrerRetryDecorator(jobState),
                    this.client,
                    jobState.getPartTracker(),
                    this.maxParallelRequests
            );
            while (jobState.hasObjects()) {
                transferNextChunks(chunkTransferrer);
            }
        } catch (final RuntimeException | IOException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getLabelForChunk(final Objects chunk) {
        try {
            return chunk.getObjects().get(0).getName();
        } catch (final Throwable t) {
            LOG.error("Failed to get label for chunk.", t);
        }

        return "unnamed object";
    }

    private void transferNextChunks(final ChunkTransferrer chunkTransferrer)
            throws IOException, InterruptedException
    {
        try {
            final GetJobChunksReadyForClientProcessingSpectraS3Response availableJobChunks =
                    this.client.getJobChunksReadyForClientProcessingSpectraS3(new GetJobChunksReadyForClientProcessingSpectraS3Request(this.masterObjectList.getJobId().toString()));
            switch (availableJobChunks.getStatus()) {
                case AVAILABLE: {
                    final MasterObjectList availableMol = availableJobChunks.getMasterObjectListResult();
                    chunkTransferrer.transferChunks(availableMol.getNodes(), availableMol.getObjects());
                    retryAfterLeft = retryAfter; // Reset the number of retries to the initial value
                    break;
                }
                case RETRYLATER: {
                    if (retryAfterLeft == 0) {
                        throw new Ds3NoMoreRetriesException(this.retryAfter);
                    }
                    retryAfterLeft--;
                    final int secondsToDelay = computeDelay(availableJobChunks.getRetryAfterSeconds());
                    emitWaitingForChunksEvents(secondsToDelay);
                    Thread.sleep(secondsToDelay * 1000);
                    break;
                }
                default:
                    assert false : "This line of code should be impossible to hit.";
            }
        } catch (final Throwable t) {
            emitFailureEvent(new FailureEvent.Builder()
                    .doingWhat("getting object")
                    .withObjectNamed(getLabelForChunk(masterObjectList.getObjects().get(0)))
                    .usingSystemWithEndpoint(client.getConnectionDetails().getEndpoint())
                    .withCausalException(t)
                    .build());

            throw t;
        }
    }

    private void emitFailureEvent(final FailureEvent failureEvent) {
        for (final FailureEventListener failureEventListener : failureEventListeners) {
            eventRunner.emitEvent(new Runnable() {
                @Override
                public void run() {
                    failureEventListener.onFailure(failureEvent);
                }
            });
        }
    }

    private int computeDelay(final int retryAfterSeconds) {
        if (retryDelay == -1) {
            return retryAfterSeconds;
        } else {
            return retryDelay;
        }
    }

    private void emitWaitingForChunksEvents(final int secondsToRetry) {
        for (final WaitingForChunksListener waitingForChunksListener : waitingForChunksListeners) {
            eventRunner.emitEvent(new Runnable() {
                @Override
                public void run() {
                    waitingForChunksListener.waiting(secondsToRetry);
                }
            });
        }
    }

    private final class GetObjectTransferrerRetryDecorator implements ItemTransferrer {
        private final GetObjectTransferrer getObjectTransferrer;

        private GetObjectTransferrerRetryDecorator(final JobState jobState) {
            getObjectTransferrer = new GetObjectTransferrer(jobState);
        }

        @Override
        public void transferItem(final Ds3Client client, final BulkObject ds3Object) throws IOException {
            ReadJobImpl.this.transferItem(client, ds3Object, getObjectTransferrer);
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

            sendChecksumEvents(ds3Object, response.getChecksumType(), response.getChecksum());
            sendMetadataEvents(ds3Object.getName(), metadata);
        }
    }

    private void sendChecksumEvents(final BulkObject ds3Object, final ChecksumType.Type type, final String checksum) {
        for (final ChecksumListener listener : this.checksumListeners) {
            eventRunner.emitEvent(new Runnable() {
                @Override
                public void run() {
                    listener.value(ds3Object, type, checksum);
                }
            });
        }
    }

    private void sendMetadataEvents(final String objName , final Metadata metadata) {
        for (final MetadataReceivedListener listener : this.metadataListeners) {
            eventRunner.emitEvent(new Runnable() {
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
