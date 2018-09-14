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

package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.google.common.collect.Sets;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.JobState;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.helpers.events.SameThreadEventRunner;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.BlobStrategy;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransferStrategy_Test {
    private static final String BLOB1 = "blob1";
    private static final String BLOB2 = "blob2";
    private static final String BLOB3 = "blob3";
    private static final String BLOB4 = "blob4";

    @Test
    public void testContinuallyGettingBlobThatFailsToTransfer() throws IOException {
        final Set<String> blobsTransferredSuccessfully = Sets.newConcurrentHashSet();
        final Set<String> blobsThatFailed = Sets.newConcurrentHashSet();
        final AtomicInteger numTimesTransferCalled = new AtomicInteger(0);

        final Ds3Client ds3Client = Mockito.mock(Ds3Client.class);

        new MultiThreadedTransferStrategy(new ContinuallyReturnFailingBlobStrategy(),
                new JobState(chunksWithBlobs()),
                10,
                new EventDispatcherImpl(new SameThreadEventRunner()),
                masterObjectList(),
                ds3Client,
                FailureEvent.FailureActivity.GettingObject)
                .withTransferMethod(transferMethod(blobsTransferredSuccessfully, blobsThatFailed, numTimesTransferCalled))
                .transfer();

        assertEquals(1, blobsThatFailed.size());
        assertTrue(blobsThatFailed.contains(BLOB2));
        assertEquals(3, blobsTransferredSuccessfully.size());
        assertTrue(blobsTransferredSuccessfully.contains(BLOB1));
        assertTrue(blobsTransferredSuccessfully.contains(BLOB3));
        assertTrue(blobsTransferredSuccessfully.contains(BLOB4));
        assertEquals(4, numTimesTransferCalled.get());
    }

    private class ContinuallyReturnFailingBlobStrategy implements BlobStrategy {
        private final AtomicBoolean getWorkCalledAlready = new AtomicBoolean(false);

        @Override
        public Iterable<JobPart> getWork() throws IOException, InterruptedException {
            if (getWorkCalledAlready.compareAndSet(false, true)) {
                return Arrays.asList(firstChunkWithFailingBlob());
            }

            return Arrays.asList(remainingChunkWithFailingBlob());
        }

        private JobPart[] firstChunkWithFailingBlob() {
            final BulkObject blob1 = new BulkObject(); blob1.setName(BLOB1);
            final BulkObject blob2 = new BulkObject(); blob2.setName(BLOB2);

            return new JobPart[] {
                    new JobPart(null, blob1),
                    new JobPart(null, blob2)
            };
        }

        private JobPart[] remainingChunkWithFailingBlob() {
            final BulkObject blob2 = new BulkObject(); blob2.setName(BLOB2);
            final BulkObject blob3 = new BulkObject(); blob3.setName(BLOB3);
            final BulkObject blob4 = new BulkObject(); blob4.setName(BLOB4);

            return new JobPart[] {
                    new JobPart(null, blob2),
                    new JobPart(null, blob3),
                    new JobPart(null, blob4)
            };
        }
    }

    private List<Objects> chunksWithBlobs() {
        final BulkObject blob1 = new BulkObject(); blob1.setName(BLOB1); blob1.setInCache(false);
        final BulkObject blob2 = new BulkObject(); blob2.setName(BLOB2); blob2.setInCache(false);
        final BulkObject blob3 = new BulkObject(); blob3.setName(BLOB3); blob3.setInCache(false);
        final BulkObject blob4 = new BulkObject(); blob4.setName(BLOB4); blob4.setInCache(false);

        final Objects chunk1 = new Objects();
        chunk1.setObjects(Arrays.asList(blob1, blob2));

        final Objects chunk2 = new Objects();
        chunk2.setObjects(Arrays.asList(blob3, blob4));

        return Arrays.asList(chunk1, chunk2);
    }

    private MasterObjectList masterObjectList() {
        final MasterObjectList masterObjectList = new MasterObjectList();
        masterObjectList.setObjects(chunksWithBlobs());
        masterObjectList.setJobId(UUID.randomUUID());
        masterObjectList.setBucketName("bucket");

        return masterObjectList;
    }

    private TransferMethod transferMethod(final Set<String> blobsTransferredSuccessfully,
                                          final Set<String> blobsThatFailed,
                                          final AtomicInteger numTimesTransferCalled)
    {
        return jobPart -> {
            numTimesTransferCalled.incrementAndGet();

            if (jobPart.getBlob().getName().equals(BLOB2)) {
                blobsThatFailed.add(jobPart.getBlob().getName());
                throw new IOException(BLOB2);
            } else {
                blobsTransferredSuccessfully.add(jobPart.getBlob().getName());
            }
        };
    }

    @Test
    public void testCanceledTransferDoesNotTryToGetBlobs() throws IOException {
        final Ds3Client ds3Client = Mockito.mock(Ds3Client.class);

        final CanceledBlobStrategy blobStrategy = new CanceledBlobStrategy();

        final TransferStrategy transferStrategy = new TransferStrategyBuilder()
                .withMasterObjectList(masterObjectList())
                .withBlobStrategy(blobStrategy)
                .withChannelBuilder(new AChannelBuilder())
                .withDs3Client(ds3Client)
                .makePutTransferStrategy();

        transferStrategy.transfer();

        assertEquals(4, blobStrategy.numAttemptsToUse());

        blobStrategy.reset();

        new Thread(() -> {
            try {
                transferStrategy.cancel();
            } catch (final IOException e) {
                System.out.println(e.getMessage());
                assert(false);
            }
        }).start();

        transferStrategy.cancel();

        transferStrategy.transfer();

        assertEquals(0, blobStrategy.numAttemptsToUse());
    }

    private interface CanceledBehavior {
        int numAttemptsToUse();
        void reset();
        void transferStrategy(final TransferStrategy transferStrategy);
        void onGetWork(final OnGetWork onGetWork);
    }

    private interface OnGetWork {
        void onGetWork(final TransferStrategy transferStrategy) throws IOException;
    }

    private static class CanceledBlobStrategy implements BlobStrategy, CanceledBehavior {
        private final Ds3Client ds3Client = Mockito.mock(Ds3Client.class);
        private final AtomicInteger numAttemptsToUse = new AtomicInteger(0);
        private TransferStrategy transferStrategy;
        private final AtomicReference<OnGetWork> onGetWork = new AtomicReference<>((transferStrategy) -> { });

        @Override
        public Iterable<JobPart> getWork() throws IOException, InterruptedException {
            final int numAttempts = numAttemptsToUse.incrementAndGet();
            onGetWork.get().onGetWork(transferStrategy);
            final BulkObject blob = new BulkObject(); blob.setName("blob" + numAttempts); blob.setInCache(false);
            return Collections.singletonList(new JobPart(ds3Client, blob));
        }

        @Override
        public int numAttemptsToUse() {
            return numAttemptsToUse.get();
        }

        @Override
        public void reset() {
            numAttemptsToUse.set(0);
        }

        @Override
        public void transferStrategy(final TransferStrategy transferStrategy) {
            this.transferStrategy = transferStrategy;
        }

        @Override
        public void onGetWork(final OnGetWork onGetWork) {
            this.onGetWork.set(onGetWork);
        }
    }

    private static class AChannelBuilder implements Ds3ClientHelpers.ObjectChannelBuilder {
        @Override
        public SeekableByteChannel buildChannel(final String key) throws IOException {
            return null;
        }
    }

    @Test
    public void testCancelingTransferWhileGettingBlobs() throws IOException {
        final Ds3Client ds3Client = Mockito.mock(Ds3Client.class);

        final CanceledBlobStrategy blobStrategy = new CanceledBlobStrategy();

        final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
                .withMasterObjectList(masterObjectList())
                .withBlobStrategy(blobStrategy)
                .withChannelBuilder(new AChannelBuilder())
                .withDs3Client(ds3Client);

        final AtomicInteger canceledCount = new AtomicInteger(0);

        transferStrategyBuilder.eventDispatcher()
                .attachCanceledEventObserver(new CanceledEventObserver(eventData -> canceledCount.incrementAndGet()));

        final TransferStrategy transferStrategy = transferStrategyBuilder.makePutTransferStrategy();

        blobStrategy.transferStrategy(transferStrategy);
        blobStrategy.onGetWork(TransferStrategy::cancel);

        transferStrategy.transfer();

        assertEquals(1, blobStrategy.numAttemptsToUse());
        assertEquals(1, canceledCount.get());
    }
}
