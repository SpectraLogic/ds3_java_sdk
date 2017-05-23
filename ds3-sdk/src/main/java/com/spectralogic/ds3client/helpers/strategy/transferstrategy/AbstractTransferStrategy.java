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
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.JobState;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.BlobStrategy;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link TransferStrategy} that provides a default implementation {@code transfer}
 * implementation.
 */
abstract class AbstractTransferStrategy implements TransferStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTransferStrategy.class);

    private final BlobStrategy blobStrategy;
    private final ListeningExecutorService executorService;
    private final EventDispatcher eventDispatcher;
    private final MasterObjectList masterObjectList;
    private final FailureEvent.FailureActivity failureActivity;
    private final AtomicInteger totalNumBlobsToTransfer;

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
        totalNumBlobsToTransfer = new AtomicInteger(jobState.numBlobsToTransfer());
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
        do {
            try {
                final Iterable<JobPart> jobParts = blobStrategy.getWork();
                final CountDownLatch countDownLatch = new CountDownLatch(Iterables.size(jobParts));
                transferJobParts(jobParts, countDownLatch);
                countDownLatch.await();
            } catch (final InterruptedException e) {
                LOG.info("Error getting entries from work queue.", e);
            }
        } while (totalNumBlobsToTransfer.get() > 0);
    }

    private void transferJobParts(final Iterable<JobPart> jobParts, final CountDownLatch countDownLatch) throws IOException {
        try {
            try {
                for (final JobPart jobPart : jobParts) {
                    executorService.submit(new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            try {
                                transferMethod.transferJobPart(jobPart);
                            } finally {
                                totalNumBlobsToTransfer.decrementAndGet();
                                countDownLatch.countDown();
                                return null;
                            }
                        }
                    });
                }
            } catch (final RuntimeException e) {
                throw e;
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        } catch (final Throwable t) {
            emitFailureEvent(makeFailureEvent(failureActivity, t, firstChunk()));
            throw t;
        }
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
