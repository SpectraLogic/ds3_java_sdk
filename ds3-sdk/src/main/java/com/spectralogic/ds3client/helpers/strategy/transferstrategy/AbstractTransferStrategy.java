/*
 * ****************************************************************************
 *    Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetBulkJobSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.PutBulkJobSpectraS3Request;
import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.JobState;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.BlobStrategy;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import com.spectralogic.ds3client.networking.FailedRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link TransferStrategy} that provides a default implementation {@code transfer}
 * implementation.
 */
abstract class AbstractTransferStrategy implements TransferStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTransferStrategy.class);

    private final BlobStrategy blobStrategy;
    private final JobState jobState;
    private final ListeningExecutorService executorService;
    private final EventDispatcher eventDispatcher;
    private final MasterObjectList masterObjectList;
    private final FailureEvent.FailureActivity failureActivity;

    private final AtomicReference<IOException> cachedException = new AtomicReference<>();

    private TransferMethod transferMethod;

    /**
     * @param blobStrategy Most likely one of {@link com.spectralogic.ds3client.helpers.strategy.blobstrategy.PutSequentialBlobStrategy}
     *                     or {@link com.spectralogic.ds3client.helpers.strategy.blobstrategy.GetSequentialBlobStrategy}.
     * @param jobState An instance of {@link JobState} that keeps track of the blobs we need to process.
     * @param executorService An instance of {@link java.util.concurrent.Executor} used to run transfers.  Streamed behavior
     *                        uses a single-threaded executor, and random access uses a multi-threaded executor.
     * @param eventDispatcher An instance of {@link EventDispatcher} used to emit events as transfers proceed.
     * @param masterObjectList The {@link MasterObjectList} returned primarily retrieved from a call to {@link Ds3Client#putBulkJobSpectraS3(PutBulkJobSpectraS3Request)}
     *                         or {@link Ds3Client#getBulkJobSpectraS3(GetBulkJobSpectraS3Request)}, used primarily
     *                         to add contextual information to events.
     * @param failureActivity Either {@link com.spectralogic.ds3client.helpers.events.FailureEvent.FailureActivity#PuttingObject} or
     *                        {@link com.spectralogic.ds3client.helpers.events.FailureEvent.FailureActivity#PuttingObject}, used
     *                        to add contextual information to events.
     */
    public AbstractTransferStrategy(final BlobStrategy blobStrategy,
                                    final JobState jobState,
                                    final ListeningExecutorService executorService,
                                    final EventDispatcher eventDispatcher,
                                    final MasterObjectList masterObjectList,
                                    final FailureEvent.FailureActivity failureActivity)
    {
        this.blobStrategy = blobStrategy;
        this.jobState = jobState;
        this.executorService = executorService;
        this.eventDispatcher = eventDispatcher;
        this.masterObjectList = masterObjectList;
        this.failureActivity = failureActivity;
    }

    /**
     * Provide a delegate that moves each blob.
     * @param transferMethod An instance of {@link TransferMethod} that moves data for a given blob.
     * @return The instance of this class, with the intent that you can string together the transfer method composition
     * and a call to {@code transfer}.
     */
    public AbstractTransferStrategy withTransferMethod(final TransferMethod transferMethod) {
        this.transferMethod = transferMethod;
        return this;
    }

    /**
     * Perform data movement according to the properties you specify in {@link TransferStrategyBuilder}.
     * @throws IOException
     */
    @Override
    public void transfer() throws IOException {
        cachedException.set(null);

        while (jobState.hasMoreBlobsToTransfer() && ! Thread.currentThread().isInterrupted()) {
            try {
                final Iterable<JobPart> jobParts = blobStrategy.getWork();
                final CountDownLatch countDownLatch = new CountDownLatch(Iterables.size(jobParts));
                transferJobParts(jobParts, countDownLatch);
                countDownLatch.await();
            } catch (final Ds3NoMoreRetriesException | FailedRequestException e) {
                LOG.info("---> Caught FailedRequestException");
                emitFailureEvent(makeFailureEvent(failureActivity, e, firstChunk()));
                throw e;
            } catch (final InterruptedException e) {
                LOG.info("Error getting entries from work queue.", e);
                Thread.currentThread().interrupt();
            } catch (final Throwable t) {
                emitFailureAndSetCachedException(t);
            }
        }

        if (cachedException.get() != null) {
            throw cachedException.get();
        }
    }

    private void emitFailureAndSetCachedException(final Throwable t) {
        emitFailureEvent(makeFailureEvent(failureActivity, t, firstChunk()));
        maybeSetCachedException(t);
    }

    private synchronized void maybeSetCachedException(final Throwable t) {
        if (cachedException.get() == null) {
            if (t instanceof IOException) {
                cachedException.set((IOException)t);
            } else {
                cachedException.set(new IOException(t));
            }
        }
    }

    private void transferJobParts(final Iterable<JobPart> jobParts, final CountDownLatch countDownLatch) throws IOException {
        for (final JobPart jobPart : jobParts) {
            if ( ! jobState.contains(jobPart.getBlob())) {
                LOG.info("---> Could not find: {}", jobPart.getBlob());
                countDownLatch.countDown();
            } else {
                executorService.submit(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        try {
                            transferMethod.transferJobPart(jobPart);
                        } catch (final RuntimeException e) {
                            LOG.error("---> Caught RuntimeException", e);
                            emitFailureAndSetCachedException(e);
                            throw e;
                        } catch (final Exception e) {
                            LOG.error("---> Caught Exception", e);
                            emitFailureAndSetCachedException(e);
                            throw new RuntimeException(e);
                        } finally {
                            LOG.info("---> Transferred: {}", jobPart.getBlob());
                            jobState.blobTransferredOrFailed(jobPart.getBlob());
                            countDownLatch.countDown();
                            return null;
                        }
                    }
                });
            }
        }
    }

    private void handleJobParrtProcessed(final CountDownLatch countDownLatch, final JobPart jobPart) {
        jobState.blobTransferredOrFailed(jobPart.getBlob());
        countDownLatch.countDown();
    }

    private void emitFailureEvent(final FailureEvent failureEvent) {
        eventDispatcher.emitFailureEvent(failureEvent);
    }

    private FailureEvent makeFailureEvent(final FailureEvent.FailureActivity failureActivity,
                                            final Throwable causalException,
                                            final Objects chunk)
    {
        return new FailureEvent.Builder()
                .doingWhat(failureActivity)
                .withCausalException(causalException)
                .withObjectNamed(labelForChunk(chunk))
                .usingSystemWithEndpoint(masterObjectList.getNodes().get(0).getEndPoint())
                .build();
    }

    private String labelForChunk(final Objects chunk) {
        try {
            return chunk.getObjects().get(0).getName();
        } catch (final Throwable t) {
            LOG.error("Failed to get label for chunk.", t);
        }

        return "unnamed object";
    }

    private Objects firstChunk() {
        return masterObjectList.getObjects().get(0);
    }

    @Override
    public void close() throws IOException {
        executorService.shutdown();
    }
}
