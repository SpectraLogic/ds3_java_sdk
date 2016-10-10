/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.helpers.strategy.BlobStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

class ChunkTransferrer implements Closeable {
    private final static Logger LOG = LoggerFactory.getLogger(ChunkTransferrer.class);

    private final ItemTransferrer itemTransferrer;
    private final JobPartTracker partTracker;
    private final ListeningExecutorService executor;

    public interface ItemTransferrer {
        void transferItem(Ds3Client client, BulkObject ds3Object) throws IOException;
    }

    public ChunkTransferrer(
            final ItemTransferrer transferrer,
            final JobPartTracker partTracker,
            final int maxParallelRequests) {
        this.itemTransferrer = transferrer;
        this.partTracker = partTracker;
        LOG.debug("Starting executor service");
        executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(maxParallelRequests));
        LOG.debug("Executor service started");
    }

    public void transferChunks(
            final BlobStrategy blobStrategy)
            throws IOException, InterruptedException {
        final List<ListenableFuture<?>> tasks = new ArrayList<>();

        final Iterable<JobPart> work = blobStrategy.getWork();

        for (final JobPart jobPart : work) {
            final BulkObject blob = jobPart.getBulkObject();
            final ObjectPart part = new ObjectPart(blob.getOffset(), blob.getLength());

            if (this.partTracker.containsPart(blob.getName(), part)) {
                LOG.debug("Adding {} to executor for processing", blob.getName());
                tasks.add(executor.submit(new Callable<Object>() {
                                      @Override
                                      public Object call() throws Exception {
                        LOG.debug("Processing {}", blob.getName());
                        blobStrategy.blobCompleted(blob);
                        ChunkTransferrer.this.itemTransferrer.transferItem(jobPart.getClient(), blob);
                        ChunkTransferrer.this.partTracker.completePart(blob.getName(), part);
                        return null;
                }
            }));
            }
        }

        executeWithExceptionHandling(tasks);
    }

    @Override
    public void close() throws IOException {
        LOG.debug("Shutting down executor");
        executor.shutdown();
    }

    private static void executeWithExceptionHandling(final List<ListenableFuture<?>> tasks)
            throws IOException {
        try {
            // Block on the asynchronous result.
            Futures.allAsList(tasks).get();
        } catch (final InterruptedException e) {
            // This is a checked exception, but we don't want to expose that this is implemented with futures.
            throw new RuntimeException(e);
        } catch (final ExecutionException e) {
            // The future throws a wrapper exception, but we want don't want to expose that this was implemented with futures.
            final Throwable cause = e.getCause();

            // Throw each of the advertised thrown exceptions.
            if (cause instanceof IOException) {
                throw (IOException)cause;
            }

            // The rest we don't know about, so we'll just forward them.
            if (cause instanceof RuntimeException) {
                throw (RuntimeException)cause;
            } else {
                throw new RuntimeException(cause);
            }
        }
    }
}
