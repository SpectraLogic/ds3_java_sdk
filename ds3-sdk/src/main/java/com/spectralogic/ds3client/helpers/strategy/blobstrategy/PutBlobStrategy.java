package com.spectralogic.ds3client.helpers.strategy.blobstrategy;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Response;
import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcher;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PutBlobStrategy extends AbstractBlobStrategy {
    private final boolean streamingStrategyEnabled;

    public PutBlobStrategy(final Ds3Client client,
                           final MasterObjectList masterObjectList,
                           final EventDispatcher eventDispatcher,
                           final ChunkAttemptRetryBehavior retryBehavior,
                           final ChunkAttemptRetryDelayBehavior chunkAttemptRetryDelayBehavior,
                           final boolean streamingStrategy)
    {
        super(client, masterObjectList, eventDispatcher, retryBehavior, chunkAttemptRetryDelayBehavior);
        this.streamingStrategyEnabled = streamingStrategy;
    }
    @Override
    public void blobCompleted(BulkObject bulkObject) {
        // Intentionally not implemented
    }

    @Override
    public Iterable<JobPart> getWork() throws IOException, InterruptedException {
        do {
            final GetJobChunksReadyForClientProcessingSpectraS3Response response = getJobChunksReadyForClientProcessingSpectraS3WithRetry();

            final Set<UUID> objectIds = new HashSet<>();
            final Iterable<JobPart> work = FluentIterable.from(response.getMasterObjectListResult().getObjects())
                    .transformAndConcat(new Function<Objects, Iterable<? extends JobPart>>() {
                        @Nullable
                        @Override
                        public Iterable<? extends JobPart> apply(@Nullable final Objects chunk) {
                            return FluentIterable.from(chunk.getObjects())
                                    .filter(input -> {
                                        if (input.getInCache()) {
                                            return false;
                                        }
                                        if (streamingStrategyEnabled) {
                                            return objectIds.add(input.getId());
                                        }
                                        return true;
                                    })
                                    .transform(new Function<BulkObject, JobPart>() {
                                        @Nullable
                                        @Override
                                        public JobPart apply(@Nullable final BulkObject blob) {
                                            return new JobPart(client(), blob);
                                        }
                                    });
                        }
                    });
            if (work.iterator().hasNext()) {
                retryBehavior().reset();
                return work;
            }
            try {
                retryBehavior().invoke();
            } catch (final Ds3NoMoreRetriesException e) {
                return ImmutableList.of();
            }
            chunkAttemptRetryDelayBehavior().delay(1); //TODO change to response.retryAfterSeconds once all tests are passing with this hard coded value
        } while (true);
    }

    private GetJobChunksReadyForClientProcessingSpectraS3Response getJobChunksReadyForClientProcessingSpectraS3WithRetry() throws IOException, InterruptedException {
        while (true) {
            final GetJobChunksReadyForClientProcessingSpectraS3Response response = client().getJobChunksReadyForClientProcessingSpectraS3(new GetJobChunksReadyForClientProcessingSpectraS3Request(this.masterObjectList().getJobId()));
            if (response.getStatus() == GetJobChunksReadyForClientProcessingSpectraS3Response.Status.RETRYLATER) {
                try {
                    retryBehavior().invoke();
                } catch (final Ds3NoMoreRetriesException e) {
                    return response;
                }
                chunkAttemptRetryDelayBehavior().delay(response.getRetryAfterSeconds());
            } else {
                return response;
            }
        }
    }
}
