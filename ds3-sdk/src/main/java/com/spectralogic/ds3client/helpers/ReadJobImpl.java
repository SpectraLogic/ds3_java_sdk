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

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Response;
import com.spectralogic.ds3client.helpers.ChunkTransferrer.ItemTransferrer;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.helpers.util.PartialObjectHelpers;
import com.spectralogic.ds3client.models.BlobApiBean;
import com.spectralogic.ds3client.models.JobChunkApiBean;
import com.spectralogic.ds3client.models.JobWithChunksApiBean;
import com.spectralogic.ds3client.models.Range;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.Guard;

import java.io.IOException;
import java.security.SignatureException;
import java.util.List;

class ReadJobImpl extends JobImpl {

    private final JobPartTracker partTracker;
    private final List<JobChunkApiBean> chunks;
    private final ImmutableMap<String, ImmutableMultimap<BlobApiBean, Range>> blobToRanges;

    public ReadJobImpl(
            final Ds3Client client,
            final JobWithChunksApiBean jobWithChunksApiBean,
            final ImmutableMultimap<String, Range> objectRanges) {
        super(client, jobWithChunksApiBean);

        this.chunks = this.jobWithChunksApiBean.getObjects();
        this.partTracker = JobPartTrackerFactory
                .buildPartTracker(Iterables.concat(chunks));
        this.blobToRanges = PartialObjectHelpers.mapRangesToBlob(jobWithChunksApiBean.getObjects(), objectRanges);
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
        try (final JobState jobState = new JobState(channelBuilder, this.jobWithChunksApiBean.getObjects(), partTracker, blobToRanges)) {
            final ChunkTransferrer chunkTransferrer = new ChunkTransferrer(
                new GetObjectTransferrer(jobState),
                this.client,
                jobState.getPartTracker(),
                this.maxParallelRequests
            );
            while (jobState.hasObjects()) {
                transferNextChunks(chunkTransferrer);
            }
        } catch (final RuntimeException | SignatureException | IOException | XmlProcessingException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void transferNextChunks(final ChunkTransferrer chunkTransferrer)
            throws IOException, SignatureException, XmlProcessingException, InterruptedException {
        final GetJobChunksReadyForClientProcessingSpectraS3Response availableJobChunks =
            this.client.getJobChunksReadyForClientProcessingSpectraS3(new GetJobChunksReadyForClientProcessingSpectraS3Request(this.jobWithChunksApiBean.getJobId()));
        switch(availableJobChunks.getStatus()) {
        case AVAILABLE: {
            final JobWithChunksApiBean availableMol = availableJobChunks.getJobWithChunksContainerApiBeanResult().getJob();
            chunkTransferrer.transferChunks(availableMol.getNodes(), availableMol.getObjects());
            break;
        }
        case RETRYLATER: {
            Thread.sleep(availableJobChunks.getRetryAfterSeconds() * 1000);
            break;
        }
        default:
            assert false : "This line of code should be impossible to hit.";
        }
    }

    private final class GetObjectTransferrer implements ItemTransferrer {
        private final JobState jobState;

        private GetObjectTransferrer(final JobState jobState) {
            this.jobState = jobState;
        }

        @Override
        public void transferItem(final Ds3Client client, final BlobApiBean ds3Object)
                throws SignatureException, IOException {

            final ImmutableCollection<Range> ranges = getRangesForBlob(blobToRanges, ds3Object);

            final GetObjectRequest request = new GetObjectRequest(
                    ReadJobImpl.this.jobWithChunksApiBean.getBucketName(),
                    ds3Object.getName(),
                    jobState.getChannel(ds3Object.getName(), ds3Object.getOffset(), ds3Object.getLength()),
                    ReadJobImpl.this.getJobId(),
                    ds3Object.getOffset()
            );

            if (Guard.isNotNullAndNotEmpty(ranges)) {
                request.withByteRanges(ranges);
            }

            client.getObject(request);
        }
    }

    private static ImmutableCollection<Range> getRangesForBlob(
            final ImmutableMap<String, ImmutableMultimap<BlobApiBean, Range>> blobToRanges,
            final BlobApiBean ds3Object) {
        final ImmutableMultimap<BlobApiBean, Range> ranges =  blobToRanges.get(ds3Object.getName());
        if (ranges == null) return null;
        return ranges.get(ds3Object);
    }
}
