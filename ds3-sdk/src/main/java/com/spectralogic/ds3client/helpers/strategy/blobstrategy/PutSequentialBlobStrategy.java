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

package com.spectralogic.ds3client.helpers.strategy.blobstrategy;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.AllocateJobChunkSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.AllocateJobChunkSpectraS3Response;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcher;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Iterator;

import static com.spectralogic.ds3client.helpers.strategy.StrategyUtils.filterChunks;

/**
 * A subclass of {@link BlobStrategy} used in put transfers.
 */
public class PutSequentialBlobStrategy extends AbstractBlobStrategy {
    private final static Logger LOG = LoggerFactory.getLogger(PutSequentialBlobStrategy.class);

    private final Iterator<Objects> chunksThatContainBlobs;

    public PutSequentialBlobStrategy(final Ds3Client client,
                                     final MasterObjectList masterObjectList,
                                     final EventDispatcher eventDispatcher,
                                     final ChunkAttemptRetryBehavior retryBehavior,
                                     final ChunkAttemptRetryDelayBehavior chunkAttemptRetryDelayBehavior)
    {
        super(client, masterObjectList, eventDispatcher, retryBehavior, chunkAttemptRetryDelayBehavior);
        this.chunksThatContainBlobs = filterChunks(masterObjectList.getObjects()).iterator();
    }

    @Override
    public Iterable<JobPart> getWork() throws IOException, InterruptedException {
        final Objects nextChunk = allocateChunk(chunksThatContainBlobs.next());

        return FluentIterable.from(nextChunk.getObjects())
                .filter(input -> !input.getInCache())
                .transform(new Function<BulkObject, JobPart>() {
                    @Nullable
                    @Override
                    public JobPart apply(@Nullable final BulkObject blob) {
                        return new JobPart(client(), blob);

                        // TODO: When we get to the point where BP enables clustering, we'll want to be able to get the
                        // client connection info correct for the server on which a chunk resides. StrategyUtils.getClient
                        // appears to work to support the clustering scenario, but we don't need it right now, and holding
                        // connection info in a collection potentially exposes lifetime management issues that we haven't
                        // fully explored.
                        // return new JobPart(StrategyUtils.getClient(uuidJobNodeImmutableMap,nextChunk.getNodeId(), getClient()), input);
                    }
                });
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
                client().allocateJobChunkSpectraS3(new AllocateJobChunkSpectraS3Request(filtered.getChunkId().toString()));

        LOG.info("AllocatedJobChunkResponse status: {}", response.getStatus().toString());

        switch (response.getStatus()) {
            case ALLOCATED:
                retryBehavior().reset();
                return response.getObjectsResult();
            case RETRYLATER:
                retryBehavior().invoke();

                try {
                    chunkAttemptRetryDelayBehavior().delay(response.getRetryAfterSeconds());
                } catch (final InterruptedException e) {
                    throw new RuntimeException(e);
                }

                return null;
            default:
                assert false : "This line of code should be impossible to hit."; return null;
        }
    }

    @Override
    public void blobCompleted(final BulkObject bulkObject) {
        // Intentionally not implemented
    }
}
