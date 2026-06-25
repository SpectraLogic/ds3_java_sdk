package com.spectralogic.ds3client.helpers.strategy.blobstrategy;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Response;
import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.JobState;
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

    private final JobState jobState;

    public PutBlobStrategy(final Ds3Client client,
                           final MasterObjectList masterObjectList,
                           final EventDispatcher eventDispatcher,
                           final ChunkAttemptRetryBehavior retryBehavior,
                           final ChunkAttemptRetryDelayBehavior chunkAttemptRetryDelayBehavior,
                           final JobState jobState,
                           final boolean streamingStrategy)
    {
        super(client, masterObjectList, eventDispatcher, retryBehavior, chunkAttemptRetryDelayBehavior);
        this.jobState = jobState;
        this.streamingStrategyEnabled = streamingStrategy;
    }
    @Override
    public void blobCompleted(BulkObject bulkObject) {
        // Intentionally not implemented
    }

    @Override
    public Iterable<JobPart> getWork() throws IOException, InterruptedException {
        do {
            final GetJobChunksReadyForClientProcessingSpectraS3Response response = client().getJobChunksReadyForClientProcessingSpectraS3(
                    new GetJobChunksReadyForClientProcessingSpectraS3Request(this.masterObjectList().getJobId()));

            if (response.getStatus() == GetJobChunksReadyForClientProcessingSpectraS3Response.Status.AVAILABLE) {
                final Set<UUID> objectIds = new HashSet<>();
                final ImmutableList<JobPart> work = FluentIterable.from(response.getMasterObjectListResult().getObjects())
                        .transformAndConcat(new Function<Objects, Iterable<? extends JobPart>>() {
                            @Nullable
                            @Override
                            public Iterable<? extends JobPart> apply(@Nullable final Objects chunk) {
                                return FluentIterable.from(chunk.getObjects())
                                        .filter(input -> {
                                            if (input.getInCache()) {
                                                jobState.blobTransferredOrFailed(input);
                                                return false;
                                            }
                                            if (!jobState.contains(input)) {
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
                        }).toList();
                if (!work.isEmpty()) {
                    retryBehavior().reset();
                    return work;
                }

                if (jobState.isEmpty()) {
                    retryBehavior().reset();
                    return Collections.emptyList();
                }
            }

            retryBehavior().invoke();
            chunkAttemptRetryDelayBehavior().delay(response.getRetryAfterSeconds());
        } while (true);
    }
}
