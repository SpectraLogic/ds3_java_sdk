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

package com.spectralogic.ds3client.helpers.strategy;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.AllocateJobChunkSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.AllocateJobChunkSpectraS3Response;
import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.JobNode;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import static com.spectralogic.ds3client.helpers.strategy.StrategyUtils.buildNodeMap;
import static com.spectralogic.ds3client.helpers.strategy.StrategyUtils.filterChunks;

public class PutStreamerStrategy extends BlobStrategy {

    private final static Logger LOG = LoggerFactory.getLogger(PutStreamerStrategy.class);

    private final ImmutableMap<UUID, JobNode> uuidJobNodeImmutableMap;
    private final Iterator<Objects> filteredChunkIterator;

    private int retryAfterLeft;



    public PutStreamerStrategy(final Ds3Client client, final MasterObjectList masterObjectList, final int retryAfter, final int retryDelay, final ChunkEventHandler chunkEventHandler) {
        super(client, masterObjectList, retryAfter, retryDelay, chunkEventHandler);
        this.filteredChunkIterator = filterChunks(masterObjectList.getObjects()).iterator();
        this.uuidJobNodeImmutableMap = buildNodeMap(masterObjectList.getNodes());
    }

    @Override
    public Iterable<JobPart> getWork() throws IOException, InterruptedException {
        final Objects nextChunk = allocateChunk(filteredChunkIterator.next());

        LOG.debug("Allocating chunk: {}", nextChunk.getChunkId().toString());
        return FluentIterable.from(nextChunk.getObjects()).filter(new Predicate<BulkObject>() {
            @Override
            public boolean apply(@Nullable final BulkObject input) {
                return !input.getInCache();
            }
        }).transform(new Function<BulkObject, JobPart>() {
            @Nullable
            @Override
            public JobPart apply(@Nullable final BulkObject input) {

                return new JobPart(StrategyUtils.getClient(uuidJobNodeImmutableMap,nextChunk.getNodeId(), getClient()), input);
            }
        });
    }

    @Override
    public void blobCompleted(final BulkObject bulkObject) {

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
                getClient().allocateJobChunkSpectraS3(new AllocateJobChunkSpectraS3Request(filtered.getChunkId().toString()));

        LOG.info("AllocatedJobChunkResponse status: {}", response.getStatus().toString());
        switch (response.getStatus()) {
        case ALLOCATED:
            retryAfterLeft = getRetryAfter(); // Reset the number of retries to the initial value
            return response.getObjectsResult();
        case RETRYLATER:
            try {
                if (getRetryAfter() != -1 && retryAfterLeft == 0) {
                    throw new Ds3NoMoreRetriesException(getRetryAfter());
                }
                retryAfterLeft--;

                final int retryDelay = computeDelay(response.getRetryAfterSeconds());
                getChunkEventHandler().emitWaitingForChunksEvents(retryDelay);

                LOG.debug("Will retry allocate chunk call after {} seconds", retryDelay);
                Thread.sleep(retryDelay * 1000);
                return null;
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        default:
            assert false : "This line of code should be impossible to hit."; return null;
        }
    }
}
