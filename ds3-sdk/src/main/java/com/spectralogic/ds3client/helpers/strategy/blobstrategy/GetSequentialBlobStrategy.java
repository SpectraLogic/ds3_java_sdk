/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers.strategy.blobstrategy;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Response;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcher;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * A subclass of {@link BlobStrategy} used in get transfers.
 */
public class GetSequentialBlobStrategy extends AbstractBlobStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(GetSequentialBlobStrategy.class);

    public GetSequentialBlobStrategy(final Ds3Client client,
                                     final MasterObjectList masterObjectList,
                                     final EventDispatcher eventDispatcher,
                                     final ChunkAttemptRetryBehavior retryBehavior,
                                     final ChunkAttemptRetryDelayBehavior chunkAttemptRetryDelayBehavior)
    {
        super(client, masterObjectList, eventDispatcher, retryBehavior, chunkAttemptRetryDelayBehavior);
    }

    @Override
    public synchronized Iterable<JobPart> getWork() throws IOException, InterruptedException {
        LOG.info("---> Getting available blobs.");

        // get chunks that have blobs ready for transfer from black pearl
        final MasterObjectList masterObjectListWithAvailableChunks = masterObjectListWithAvailableChunks();

        final FluentIterable<Objects> chunks = FluentIterable.from(masterObjectListWithAvailableChunks.getObjects());

        return chunks.transformAndConcat(new Function<Objects, Iterable<? extends JobPart>>() {
            @Nullable
            @Override
            public Iterable<? extends JobPart> apply(@Nullable final Objects chunk) {
                return FluentIterable.from(chunk.getObjects())
                        .transform(new Function<BulkObject, JobPart>() {
                            @Nullable
                            @Override
                            public JobPart apply(@Nullable final BulkObject blob) {
                                return new JobPart(client(), blob);
                            }
                        });
            }
        });
    }

    private MasterObjectList masterObjectListWithAvailableChunks() throws IOException, InterruptedException {
        LOG.info("---> Getting blobs from black pearl.");

        do {
            final GetJobChunksReadyForClientProcessingSpectraS3Response availableJobChunks =
                    client().getJobChunksReadyForClientProcessingSpectraS3(new GetJobChunksReadyForClientProcessingSpectraS3Request(masterObjectList().getJobId().toString()));

            switch (availableJobChunks.getStatus()) {
                case AVAILABLE: {
                    retryBehavior().reset();
                    return availableJobChunks.getMasterObjectListResult();
                }
                case RETRYLATER: {
                    retryBehavior().invoke();

                    chunkAttemptRetryDelayBehavior().delay(availableJobChunks.getRetryAfterSeconds());

                    continue;
                }
                default:
                    assert false : "This line of code should be impossible to hit.";
            }
        } while(true);
    }

    /**
     * Emit an event when a blob is transferred.
     * @param bulkObject The transferred blob.
     */
    @Override
    public void blobCompleted(final BulkObject bulkObject) {
        // Intentionally not implemented
    }
}
