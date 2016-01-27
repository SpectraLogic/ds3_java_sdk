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
import com.spectralogic.ds3client.commands.CreateObjectRequest;
import com.spectralogic.ds3client.commands.spectrads3.AllocateJobChunkSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.AllocateJobChunkSpectraS3Response;
import com.spectralogic.ds3client.helpers.ChunkTransferrer.ItemTransferrer;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.models.BlobApiBean;
import com.spectralogic.ds3client.models.JobChunkApiBean;
import com.spectralogic.ds3client.models.JobWithChunksApiBean;
import com.spectralogic.ds3client.models.Range;
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
    private final List<JobChunkApiBean> filteredChunks;

    public WriteJobImpl(
            final Ds3Client client,
            final JobWithChunksApiBean jobWithChunksApiBean) {
        super(client, jobWithChunksApiBean);
        if (this.jobWithChunksApiBean == null || this.jobWithChunksApiBean.getObjects() == null) {
            LOG.info("Job has no data to transfer");
            this.filteredChunks = null;
            this.partTracker = null;
        } else {
            LOG.info("Ready to start transfer for job " + jobWithChunksApiBean.getJobId().toString() + " with "
                    + jobWithChunksApiBean.getObjects().size() + " chunks");
            this.filteredChunks = filterChunks(this.jobWithChunksApiBean.getObjects());
            this.partTracker = JobPartTrackerFactory
                    .buildPartTracker(Iterables.concat(ReadJobImpl.getAllBlobApiBeans(filteredChunks)));
        }

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
        if (this.jobWithChunksApiBean == null || this.jobWithChunksApiBean.getObjects() == null) {
            LOG.info("There is nothing to transfer for job"
                    + ((this.getJobId() == null) ? "" : " " + this.getJobId().toString()));
            return;
        }
        try (final JobState jobState = new JobState(channelBuilder, filteredChunks, partTracker,
                ImmutableMap.<String, ImmutableMultimap<BlobApiBean,Range>>of())) {
            final ChunkTransferrer chunkTransferrer = new ChunkTransferrer(
                new PutObjectTransferrer(jobState),
                this.client,
                jobState.getPartTracker(),
                this.maxParallelRequests
            );
            for (final JobChunkApiBean chunk : filteredChunks) {
                LOG.debug("Allocating chunk: " + chunk.getChunkId().toString());
                chunkTransferrer.transferChunks(
                        this.jobWithChunksApiBean.getNodes(),
                        Collections.singletonList(filterChunk(allocateChunk(chunk))));
            }
        } catch (final SignatureException | IOException | XmlProcessingException | RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JobChunkApiBean allocateChunk(final JobChunkApiBean filtered) throws IOException, SignatureException {
        JobChunkApiBean chunk = null;
        while (chunk == null) {
            chunk = tryAllocateChunk(filtered);
        }
        return chunk;
    }

    private JobChunkApiBean tryAllocateChunk(final JobChunkApiBean filtered) throws IOException, SignatureException {
        final AllocateJobChunkSpectraS3Response response =
                this.client.allocateJobChunkSpectraS3(new AllocateJobChunkSpectraS3Request(filtered.getChunkId()));

        LOG.info("AllocatedJobChunkResponse status: " + response.getStatus().toString());
        switch (response.getStatus()) {
        case ALLOCATED:
            return response.getJobChunkApiBeanResult();
        case RETRYLATER:
            try {
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
     * @param jobChunkApiBeanList The list to be filtered
     * @return The filtered list
     */
    private static List<JobChunkApiBean> filterChunks(final List<JobChunkApiBean> jobChunkApiBeanList) {
        final List<JobChunkApiBean> filteredChunks = new ArrayList<>();
        for (final JobChunkApiBean jobChunkApiBean : jobChunkApiBeanList) {
            final JobChunkApiBean filteredChunk = filterChunk(jobChunkApiBean);
            if (filteredChunk.getObjects().size() > 0) {
                filteredChunks.add(filteredChunk);
            }
        }
        return filteredChunks;
    }

    private static JobChunkApiBean filterChunk(final JobChunkApiBean jobChunkApiBean) {
        final JobChunkApiBean newJobChunkApiBean = new JobChunkApiBean();
        newJobChunkApiBean.setChunkId(jobChunkApiBean.getChunkId());
        newJobChunkApiBean.setChunkNumber(jobChunkApiBean.getChunkNumber());
        newJobChunkApiBean.setNodeId(jobChunkApiBean.getNodeId());
        newJobChunkApiBean.setObjects(filterObjects(jobChunkApiBean.getObjects()));
        return newJobChunkApiBean;
    }

    private static List<BlobApiBean> filterObjects(final List<BlobApiBean> list) {
        final List<BlobApiBean> filtered = new ArrayList<>();
        for (final BlobApiBean obj : list) {
            if (!obj.getInCache()) {
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
        public void transferItem(final Ds3Client client, final BlobApiBean ds3Object)
                throws SignatureException, IOException {
            client.createObject(new CreateObjectRequest(
                    WriteJobImpl.this.jobWithChunksApiBean.getBucketName(),
                    ds3Object.getName(),
                    jobState.getChannel(ds3Object.getName(), ds3Object.getOffset(), ds3Object.getLength()),
                    WriteJobImpl.this.getJobId(),
                    ds3Object.getOffset(),
                    ds3Object.getLength()
            ));
        }
    }
}
