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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Sets;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.commands.spectrads3.AllocateJobChunkSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.AllocateJobChunkSpectraS3Response;
import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;
import com.spectralogic.ds3client.helpers.ChunkTransferrer.ItemTransferrer;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.helpers.events.EventRunner;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.utils.Guard;
import com.spectralogic.ds3client.utils.SeekableByteChannelInputStream;
import com.spectralogic.ds3client.utils.hashing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.SeekableByteChannel;
import java.util.*;

class WriteJobImpl extends JobImpl {

    static private final Logger LOG = LoggerFactory.getLogger(WriteJobImpl.class);

    private final JobPartTracker partTracker;
    private final List<Objects> filteredChunks;
    private final ChecksumType.Type checksumType;
    private final Set<ChecksumListener> checksumListeners;
    private final Set<WaitingForChunksListener> waitingForChunksListeners;
    private final Set<FailureEventListener> failureEventListeners;
    private final EventRunner eventRunner;
    private final int retryAfter; // Negative retryAfter value represent infinity retries
    private final int retryDelay; //Negative value means use default

    private int retryAfterLeft; // The number of retries left
    private Ds3ClientHelpers.MetadataAccess metadataAccess = null;
    private ChecksumFunction checksumFunction = null;

    public WriteJobImpl(
            final Ds3Client client,
            final MasterObjectList masterObjectList,
            final int retryAfter,
            final ChecksumType.Type type,
            final int objectTransferAttempts,
            final int retryDelay,
            final EventRunner eventRunner) {
        super(client, masterObjectList, objectTransferAttempts);
        if (this.masterObjectList == null || this.masterObjectList.getObjects() == null) {
            LOG.info("Job has no data to transfer");
            this.filteredChunks = null;
            this.partTracker = null;
        } else {
            LOG.info("Ready to start transfer for job {} with {} chunks",
                    this.masterObjectList.getJobId().toString(), this.masterObjectList.getObjects().size());
            this.filteredChunks = filterChunks(this.masterObjectList.getObjects());
            this.partTracker = JobPartTrackerFactory
                    .buildPartTracker(ReadJobImpl.getAllBlobApiBeans(filteredChunks), eventRunner);
        }
        this.retryAfter = this.retryAfterLeft = retryAfter;
        this.retryDelay = retryDelay;
        this.checksumListeners = Sets.newIdentityHashSet();
        this.waitingForChunksListeners = Sets.newIdentityHashSet();
        this.failureEventListeners = Sets.newIdentityHashSet();
        this.eventRunner = eventRunner;

        this.checksumType = type;
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
        throw new IllegalStateException("Metadata listeners are not used with Write jobs");
    }

    @Override
    public void removeMetadataReceivedListener(final MetadataReceivedListener listener) {
        throw new IllegalStateException("Metadata listeners are not used with Write jobs");
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
    public void attachFailureEventListener(FailureEventListener listener) {
        checkRunning();
        this.failureEventListeners.add(listener);
    }

    @Override
    public void removeFailureEventListener(FailureEventListener listener) {
        checkRunning();
        this.failureEventListeners.remove(listener);
    }

    @Override
    public Ds3ClientHelpers.Job withMetadata(final Ds3ClientHelpers.MetadataAccess access) {
        checkRunning();
        this.metadataAccess = access;
        return this;
    }

    @Override
    public Ds3ClientHelpers.Job withChecksum(final ChecksumFunction checksumFunction) {
        this.checksumFunction = checksumFunction;
        return this;
    }

    @Override
    public void transfer(final ObjectChannelBuilder channelBuilder)
            throws IOException {
        try {
            running = true;
            LOG.debug("Starting job transfer");
            if (this.masterObjectList == null || this.masterObjectList.getObjects() == null) {
                LOG.info("There is nothing to transfer for job"
                        + (this.getJobId() == null ? "" : " " + this.getJobId().toString()));
                return;
            }

            try (final JobState jobState = new JobState(
                    channelBuilder,
                    filteredChunks,
                    partTracker,
                    ImmutableMap.<String, ImmutableMultimap<BulkObject, Range>>of())) {
                final ChunkTransferrer chunkTransferrer = new ChunkTransferrer(
                        new PutObjectTransferrerRetryDecorator(jobState),
                        this.client,
                        jobState.getPartTracker(),
                        this.maxParallelRequests
                );
                for (final Objects chunk : filteredChunks) {
                    LOG.debug("Allocating chunk: {}", chunk.getChunkId().toString());
                    chunkTransferrer.transferChunks(
                            this.masterObjectList.getNodes(),
                            Collections.singletonList(filterChunk(allocateChunk(chunk))));
                }
            } catch (final IOException | RuntimeException e) {
                throw e;
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        } catch (final Throwable t) {
            emitFailureEvent(new FailureEvent.Builder()
                    .doingWhat("putting object")
                    .withCausalException(t)
                    .usingSystemWithEndpoint(client.getConnectionDetails().getEndpoint())
                    .withObjectNamed(getLabelForChunk(filteredChunks.get(0)))
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

    private String getLabelForChunk(final Objects chunk) {
        try {
            return chunk.getObjects().get(0).getName();
        } catch (final Throwable t) {
            LOG.error("Failed to get label for chunk.", t);
        }

        return "unnamed object";
    }

    private Objects allocateChunk(final Objects filtered) throws IOException {
        Objects chunk = null;
        while (chunk == null) {
            chunk = tryAllocateChunk(filtered);
        }
        return chunk;
    }

    private Objects tryAllocateChunk(final Objects filtered) throws IOException {
        final AllocateJobChunkSpectraS3Response response =
                this.client.allocateJobChunkSpectraS3(new AllocateJobChunkSpectraS3Request(filtered.getChunkId().toString()));

        LOG.info("AllocatedJobChunkResponse status: {}", response.getStatus().toString());

        try {
            switch (response.getStatus()) {
                case ALLOCATED:
                    retryAfterLeft = retryAfter; // Reset the number of retries to the initial value
                    return response.getObjectsResult();
                case RETRYLATER:
                    try {
                        if (retryAfterLeft == 0) {
                            throw new Ds3NoMoreRetriesException(this.retryAfter);
                        }
                        retryAfterLeft--;

                        final int retryAfter = computeRetryAfter(response.getRetryAfterSeconds());
                        emitWaitingForChunksEvents(retryAfter);

                        LOG.debug("Will retry allocate chunk call after {} seconds", retryAfter);
                        Thread.sleep(retryAfter * 1000);
                        return null;
                    } catch (final InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                default:
                    assert false : "This line of code should be impossible to hit.";
                    return null;
            }
        } catch (final Throwable t) {
            emitFailureEvent(new FailureEvent.Builder()
                    .doingWhat("allocating chunk")
                    .usingSystemWithEndpoint(client.getConnectionDetails().getEndpoint())
                    .withObjectNamed(getLabelForChunk(filtered))
                    .withCausalException(t)
                    .build());
            throw t;
        }
    }

    private void emitWaitingForChunksEvents(final int retryAfter) {
        for (final WaitingForChunksListener waitingForChunksListener : waitingForChunksListeners) {
            eventRunner.emitEvent(new Runnable() {
                @Override
                public void run() {
                    waitingForChunksListener.waiting(retryAfter);
                }
            });
        }
    }

    private int computeRetryAfter(final int retryAfterSeconds) {
        if (retryDelay == -1) {
            return retryAfterSeconds;
        } else {
            return retryDelay;
        }
    }

    /**
     * Filters out chunks that have already been completed.  We will get the same chunk name back from the server, but it
     * will not have any objects in it, so we remove that from the list of objects that are returned.
     * @param objectsList The list to be filtered
     * @return The filtered list
     */
    private static List<Objects> filterChunks(final List<Objects> objectsList) {
        final List<Objects> filteredChunks = new ArrayList<>();
        for (final Objects objects : objectsList) {
            final Objects filteredChunk = filterChunk(objects);
            if (filteredChunk.getObjects().size() > 0) {
                filteredChunks.add(filteredChunk);
            }
        }
        return filteredChunks;
    }

    private static Objects filterChunk(final Objects objects) {
        final Objects newObjects = new Objects();
        newObjects.setChunkId(objects.getChunkId());
        newObjects.setChunkNumber(objects.getChunkNumber());
        newObjects.setNodeId(objects.getNodeId());
        newObjects.setObjects(filterObjects(objects.getObjects()));
        return newObjects;
    }

    private static List<BulkObject> filterObjects(final List<BulkObject> list) {
        final List<BulkObject> filtered = new ArrayList<>();
        for (final BulkObject obj : list) {
            if (!obj.getInCache()) {
                filtered.add(obj);
            }
        }
        return filtered;
    }

    private final class PutObjectTransferrerRetryDecorator implements ItemTransferrer {
        private final PutObjectTransferrer putObjectTransferrer;

        private PutObjectTransferrerRetryDecorator(final JobState jobState) {
            putObjectTransferrer = new PutObjectTransferrer(jobState);
        }

        @Override
        public void transferItem(final Ds3Client client, final BulkObject ds3Object) throws IOException {
            WriteJobImpl.this.transferItem(client, ds3Object, putObjectTransferrer);
        }
    }

    private final class PutObjectTransferrer implements ItemTransferrer {
        private final JobState jobState;

        private PutObjectTransferrer(final JobState jobState) {
            this.jobState = jobState;
        }

        @Override
        public void transferItem(final Ds3Client client, final BulkObject ds3Object)
                throws IOException {
            client.putObject(createRequest(ds3Object));
        }

        private PutObjectRequest createRequest(final BulkObject ds3Object) throws IOException {
            final SeekableByteChannel channel = jobState.getChannel(ds3Object.getName(), ds3Object.getOffset(), ds3Object.getLength());

            final PutObjectRequest request = new PutObjectRequest(
                    WriteJobImpl.this.masterObjectList.getBucketName(),
                    ds3Object.getName(),
                    jobState.getChannel(ds3Object.getName(), ds3Object.getOffset(), ds3Object.getLength()),
                    WriteJobImpl.this.getJobId().toString(),
                    ds3Object.getOffset(),
                    ds3Object.getLength()
            );

            if (ds3Object.getOffset() == 0 && metadataAccess != null) {
                final Map<String, String> metadata = metadataAccess.getMetadataValue(ds3Object.getName());
                if (Guard.isMapNullOrEmpty(metadata)) return request;
                final ImmutableMap<String, String> immutableMetadata = ImmutableMap.copyOf(metadata);
                for (final Map.Entry<String, String> value : immutableMetadata.entrySet()) {
                    request.withMetaData(value.getKey(), value.getValue());
                }
            }

            final String checksum = calculateChecksum(ds3Object, channel);
            if (checksum != null) {
                request.withChecksum(ChecksumType.value(checksum), WriteJobImpl.this.checksumType);
                emitChecksumEvents(ds3Object, WriteJobImpl.this.checksumType, checksum);
            }

            return request;
        }

        private String calculateChecksum(final BulkObject ds3Object, final SeekableByteChannel channel) throws IOException {
            if (WriteJobImpl.this.checksumType != ChecksumType.Type.NONE) {
                if (WriteJobImpl.this.checksumFunction == null) {
                    LOG.info("Calculating {} checksum for blob: {}", WriteJobImpl.this.checksumType.toString(), ds3Object.toString());
                    final SeekableByteChannelInputStream dataStream = new SeekableByteChannelInputStream(channel);
                    final Hasher hasher = getHasher(WriteJobImpl.this.checksumType);
                    final String checksum = hashInputStream(hasher, dataStream);
                    LOG.info("Computed checksum for blob: {}", checksum);
                    return checksum;
                } else {
                    LOG.info("Getting checksum from user supplied callback for blob: {}", ds3Object.toString());
                    final String checksum = WriteJobImpl.this.checksumFunction.compute(ds3Object, channel);
                    LOG.info("User supplied checksum is: {}", checksum);
                    return checksum;
                }
            }
            return null;
        }

        private static final int READ_BUFFER_SIZE = 10 * 1024 * 1024;
        private String hashInputStream(final Hasher digest, final InputStream stream) throws IOException {
            final byte[] buffer = new byte[READ_BUFFER_SIZE];
            int bytesRead;

            while (true) {
                bytesRead = stream.read(buffer);

                if (bytesRead < 0) {
                    break;
                }

                digest.update(buffer, 0, bytesRead);
            }

            return digest.digest();
        }

        private Hasher getHasher(final ChecksumType.Type checksumType) {
            switch (checksumType) {
                case MD5: return new MD5Hasher();
                case SHA_256: return new SHA256Hasher();
                case SHA_512: return new SHA512Hasher();
                case CRC_32: return new CRC32Hasher();
                case CRC_32C: return new CRC32CHasher();
                default: throw new RuntimeException("Unknown checksum type " + checksumType.toString());
            }
        }
    }

    private void emitChecksumEvents(final BulkObject bulkObject, final ChecksumType.Type type, final String checksum) {
        for (final ChecksumListener listener : checksumListeners) {
            eventRunner.emitEvent(new Runnable() {
                @Override
                public void run() {
                    listener.value(bulkObject, type, checksum);
                }
            });
        }
    }
}
