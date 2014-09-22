/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientFactory;
import com.spectralogic.ds3client.models.bulk.BulkObject;
import com.spectralogic.ds3client.models.bulk.Node;
import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

class ChunkTransferrer {
    private final ItemTransferrer itemTransferrer;
    private final Ds3Client mainClient;
    private final JobPartTracker partTracker;
    private final int maxParallelRequests;

    public static interface ItemTransferrer {
        void transferItem(Ds3Client client, BulkObject ds3Object) throws SignatureException, IOException;
    }
    
    public ChunkTransferrer(
            final ItemTransferrer transferrer,
            final Ds3Client mainClient,
            final JobPartTracker partTracker,
            final int maxParallelRequests) {
        this.itemTransferrer = transferrer;
        this.mainClient = mainClient;
        this.partTracker = partTracker;
        this.maxParallelRequests = maxParallelRequests;
    }
    
    public void transferChunks(
            final Iterable<Node> nodes,
            final Iterable<Objects> chunks)
                throws SignatureException, IOException, XmlProcessingException {
        final ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(maxParallelRequests));
        try {
            final List<ListenableFuture<?>> tasks = new ArrayList<>();
            final Ds3ClientFactory clientFactory = this.mainClient.buildFactory(nodes);
            for (final Objects chunk : chunks) {
                final Ds3Client client = clientFactory.getClientForNodeId(chunk.getNodeId());
                for (final BulkObject ds3Object : chunk) {
                    final ObjectPart part = new ObjectPart(ds3Object.getOffset(), ds3Object.getLength());
                    if (this.partTracker.containsPart(ds3Object.getName(), part)) {
                        tasks.add(executor.submit(new Callable<Object>() {
                            @Override
                            public Object call() throws Exception {
                                ChunkTransferrer.this.itemTransferrer.transferItem(client, ds3Object);
                                ChunkTransferrer.this.partTracker.completePart(ds3Object.getName(), part);
                                return null;
                            }
                        }));
                    }
                }
            }
            executeWithExceptionHandling(tasks);
        } finally {
            executor.shutdown();
        }
    }

    private static void executeWithExceptionHandling(final List<ListenableFuture<?>> tasks)
            throws IOException, SignatureException, XmlProcessingException {
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
            } else if (cause instanceof SignatureException) {
                throw (SignatureException)cause;
            } else if (cause instanceof XmlProcessingException) {
                throw (XmlProcessingException)cause;
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
