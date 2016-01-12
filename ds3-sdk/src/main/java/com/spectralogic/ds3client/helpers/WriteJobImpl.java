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

package com.spectralogic.ds3client.helpers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.AllocateJobChunkRequest;
import com.spectralogic.ds3client.commands.AllocateJobChunkResponse;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;
import com.spectralogic.ds3client.helpers.ChunkTransferrer.ItemTransferrer;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.models.Range;
import com.spectralogic.ds3client.models.bulk.BulkObject;
import com.spectralogic.ds3client.models.bulk.MasterObjectList;
import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class WriteJobImpl extends JobImpl {
    static private final Logger LOG = LoggerFactory.getLogger(WriteJobImpl.class);
    private final JobPartTracker partTracker;
    private final List<Objects> filteredChunks;
    private final int retryAfter; // Negative retryAfter value represent infinity retries
    private int retryAfterLeft; // The number of retries left

    public WriteJobImpl(final Ds3Client client, final MasterObjectList masterObjectList, final int retryAfter) {
        super(client, masterObjectList);
        if (this.masterObjectList == null || this.masterObjectList.getObjects() == null) {
            LOG.info("Job has no data to transfer");
            this.filteredChunks = null;
            this.partTracker = null;
        } else {
            LOG.info("Ready to start transfer for job " + masterObjectList.getJobId().toString() + " with " + masterObjectList.getObjects().size() + " chunks");
            this.filteredChunks = filterChunks(this.masterObjectList.getObjects());
            this.partTracker = JobPartTrackerFactory
                    .buildPartTracker(Iterables.concat(filteredChunks));
        }
        this.retryAfter = this.retryAfterLeft = retryAfter;
    }

    @Override
    public void attachDataTransferredListener(final DataTransferredListener listener) {
        this.partTracker.attachDataTransferredListener(listener);
    }

    @Override
    public void attachObjectCompletedListener(final ObjectCompletedListener listener) {
        this.partTracker.attachObjectCompletedListener(listener);

    }

    @Override
    public void removeDataTransferredListener(final DataTransferredListener listener) {
        this.partTracker.removeDataTransferredListener(listener);
    }

    @Override
    public void removeObjectCompletedListener(final ObjectCompletedListener listener) {
        this.partTracker.removeObjectCompletedListener(listener);

    }

    @Override
    public void transfer(final ObjectChannelBuilder channelBuilder)
            throws SignatureException, IOException, XmlProcessingException {
        LOG.debug("Starting job transfer");
        if (this.masterObjectList == null || this.masterObjectList.getObjects() == null) {
            LOG.info("There is nothing to transfer for job" + ((this.getJobId() == null) ? "" : " " + this.getJobId().toString()));
            return;
        }
        try (final JobState jobState = new JobState(channelBuilder, filteredChunks, partTracker, ImmutableMap.<String, ImmutableMultimap<BulkObject,Range>>of())) {
            final ChunkTransferrer chunkTransferrer = new ChunkTransferrer(
                new PutObjectTransferrer(jobState),
                this.client,
                jobState.getPartTracker(),
                this.maxParallelRequests
            );
            for (final Objects chunk : filteredChunks) {
                LOG.debug("Allocating chunk: " + chunk.getChunkId().toString());
                chunkTransferrer.transferChunks(this.masterObjectList.getNodes(), Collections.singletonList(filterChunk(allocateChunk(chunk))));
            }
        } catch (final SignatureException | IOException | XmlProcessingException | RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Objects allocateChunk(final Objects filtered) throws IOException, SignatureException {
        Objects chunk = null;
        while (chunk == null) {
            chunk = tryAllocateChunk(filtered);
        }
        return chunk;
    }

    private Objects tryAllocateChunk(final Objects filtered) throws IOException, SignatureException {
        final AllocateJobChunkResponse response =
                this.client.allocateJobChunk(new AllocateJobChunkRequest(filtered.getChunkId()));

        LOG.info("AllocatedJobChunkResponse status: " + response.getStatus().toString());
        switch (response.getStatus()) {
        case ALLOCATED:
            retryAfterLeft = retryAfter; // Reset the number of retries to the initial value
            return response.getObjects();
        case RETRYLATER:
            try {
                if (retryAfterLeft == 0) {
                    throw new Ds3NoMoreRetriesException();
                }
                retryAfterLeft--;

                final int retryAfter = response.getRetryAfterSeconds() * 1000;
                LOG.debug("Will retry allocate chunk call after " + retryAfter + " seconds");
                Thread.sleep(retryAfter);
                return null;
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        default:
            assert false : "This line of code should be impossible to hit."; return null;
        }
    }

    /**
     * Filters out chunks that have already been completed.  We will get the same chunk name back from the server, but it
     * will not have any objects in it, so we remove that from the list of objects that are returned.
     * @param chunks The list to be filtered
     * @return The filtered list
     */
    private static List<Objects> filterChunks(final List<Objects> chunks) {
        final List<Objects> filteredChunks = new ArrayList<>();
        for (final Objects chunk : chunks) {
            final Objects filteredChunk = filterChunk(chunk);
            if (filteredChunk.getObjects().size() > 0) {
                filteredChunks.add(filteredChunk);
            }
        }
        return filteredChunks;
    }

    private static Objects filterChunk(final Objects chunk) {
        final Objects newChunk = new Objects();
        newChunk.setChunkId(chunk.getChunkId());
        newChunk.setChunkNumber(chunk.getChunkNumber());
        newChunk.setNodeId(chunk.getNodeId());
        newChunk.setObjects(filterObjects(chunk.getObjects()));
        return newChunk;
    }

    private static List<BulkObject> filterObjects(final List<BulkObject> list) {
        final List<BulkObject> filtered = new ArrayList<>();
        for (final BulkObject obj : list) {
            if (!obj.isInCache()) {
                filtered.add(obj);
            }
        }
        return filtered;
    }

    private final class PutObjectTransferrer implements ItemTransferrer {
        private final JobState jobState;

        private PutObjectTransferrer(final JobState jobState) {
            this.jobState = jobState;
        }

        @Override
        public void transferItem(final Ds3Client client, final BulkObject ds3Object)
                throws SignatureException, IOException {
            client.putObject(new PutObjectRequest(
                WriteJobImpl.this.masterObjectList.getBucketName(),
                ds3Object.getName(),
                WriteJobImpl.this.getJobId(),
                ds3Object.getLength(),
                ds3Object.getOffset(),
                jobState.getChannel(ds3Object.getName(), ds3Object.getOffset(), ds3Object.getLength())
            ));
        }
    }
}
