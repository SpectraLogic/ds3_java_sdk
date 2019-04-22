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

package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.google.common.collect.Sets;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.JobState;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.helpers.events.SameThreadEventRunner;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.BlobStrategy;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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

        new MultiThreadedTransferStrategy(new ContinuallyReturnFailingBlobStrategy(),
                new JobState(chunksWithBlobs()),
                10,
                new EventDispatcherImpl(new SameThreadEventRunner()),
                masterObjectList(),
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
        final BulkObject blob1 = new BulkObject(); blob1.setName(BLOB1);
        final BulkObject blob2 = new BulkObject(); blob2.setName(BLOB2);
        final BulkObject blob3 = new BulkObject(); blob3.setName(BLOB3);
        final BulkObject blob4 = new BulkObject(); blob4.setName(BLOB4);

        final Objects chunk1 = new Objects();
        chunk1.setObjects(Arrays.asList(blob1, blob2));

        final Objects chunk2 = new Objects();
        chunk2.setObjects(Arrays.asList(blob3, blob4));

        return Arrays.asList(chunk1, chunk2);
    }

    private MasterObjectList masterObjectList() {
        final MasterObjectList masterObjectList = new MasterObjectList();
        masterObjectList.setObjects(chunksWithBlobs());

        return masterObjectList;
    }

    private TransferMethod transferMethod(final Set<String> blobsTransferredSuccessfully,
                                          final Set<String> blobsThatFailed,
                                          final AtomicInteger numTimesTransferCalled)
    {
        return new TransferMethod() {
            @Override
            public void transferJobPart(final JobPart jobPart) throws IOException {
                numTimesTransferCalled.incrementAndGet();

                if (jobPart.getBlob().getName().equals(BLOB2)) {
                    blobsThatFailed.add(jobPart.getBlob().getName());
                    throw new IOException(BLOB2);
                } else {
                    blobsTransferredSuccessfully.add(jobPart.getBlob().getName());
                }
            }
        };
    }
}
