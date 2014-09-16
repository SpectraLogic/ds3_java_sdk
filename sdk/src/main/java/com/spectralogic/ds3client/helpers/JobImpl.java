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
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.Job;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.models.bulk.BulkObject;
import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

abstract class JobImpl implements Job {
    private int maxParallelRequests = 20;
    
    private final Ds3ClientFactory clientFactory;
    private final UUID jobId;
    private final String bucketName;
    private final Iterable<? extends Objects> objectLists;

    public JobImpl(
            final Ds3ClientFactory clientFactory,
            final UUID jobId,
            final String bucketName,
            final Iterable<? extends Objects> objectLists) {
        this.clientFactory = clientFactory;
        this.jobId = jobId;
        this.bucketName = bucketName;
        this.objectLists = objectLists;
    }
    
    @Override
    public UUID getJobId() {
        return this.jobId;
    }

    @Override
    public String getBucketName() {
        return this.bucketName;
    }
    
    @Override
    public Job withMaxParallelRequests(final int maxParallelRequests) {
        this.maxParallelRequests = maxParallelRequests;
        return this;
    }

    @Override
    public void transfer(final ObjectChannelBuilder transferrer)
            throws SignatureException, IOException, XmlProcessingException {
        for (final Objects chunk : this.objectLists) {
            this.transferChunk(transferrer, chunk);
        }
    }
    
    protected abstract void transferItem(
            final Ds3Client client,
            final UUID jobId,
            final String bucketName,
            final BulkObject ds3Object,
            final ObjectChannelBuilder transferrer) throws SignatureException, IOException;

    private void transferChunk(final ObjectChannelBuilder transferrer, final Objects objects)
            throws SignatureException, IOException, XmlProcessingException {
        final ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(this.maxParallelRequests));
        try {
            final Ds3Client client = this.clientFactory.getClientForNodeId(objects.getNodeId());
            final List<ListenableFuture<?>> tasks = new ArrayList<>();
            for (final BulkObject ds3Object : objects) {
                tasks.add(this.createTransferTask(transferrer, service, client, ds3Object));
            }
            this.executeWithExceptionHandling(tasks);
        } finally {
            service.shutdown();
        }
    }

    private ListenableFuture<?> createTransferTask(
            final ObjectChannelBuilder transferrer,
            final ListeningExecutorService service,
            final Ds3Client client,
            final BulkObject ds3Object) {
        return service.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                transferItem(client, JobImpl.this.jobId, JobImpl.this.bucketName, ds3Object, transferrer);
                return null;
            }
        });
    }
    
    private void executeWithExceptionHandling(final List<ListenableFuture<?>> tasks)
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
