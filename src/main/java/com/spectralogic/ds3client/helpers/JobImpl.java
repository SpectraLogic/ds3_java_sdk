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

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.Job;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

abstract class JobImpl implements Job {
    private static final int THREAD_COUNT = 10;
    
    private final Ds3Client client;
    private final UUID jobId;
    private final String bucketName;
    private final Iterable<Objects> objectLists;

    //TODO: client factory
    public JobImpl(
            final Ds3Client client,
            final UUID jobId,
            final String bucketName,
            final Iterable<Objects> objectLists) {
        this.client = client;
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
    
    interface Transferrer {
        public void Transfer(Ds3Client client, UUID jobId, String bucketName, Ds3Object ds3Object)
                throws SignatureException, IOException;
    }
    
    protected void transferAll(final Transferrer transferrer)
            throws SignatureException, IOException, XmlProcessingException {
        final ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(THREAD_COUNT));
        try {
            final List<ListenableFuture<?>> tasks = new ArrayList<ListenableFuture<?>>();
            for (final Objects objects : this.objectLists) {
                tasks.add(service.submit(this.createObjectListTask(transferrer, objects)));
            }
            this.executeWithExceptionHandling(tasks);
        } finally {
            service.shutdown();
        }
    }

    private Callable<?> createObjectListTask(final Transferrer transferrer, final Objects objects) {
        return new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                final Ds3Client client = JobImpl.this.getClient(objects.getServerId());
                for (final Ds3Object ds3Object : objects) {
                    transferrer.Transfer(client, JobImpl.this.jobId, JobImpl.this.bucketName, ds3Object);
                }
                return null;
            }
        };
    }
    
    //TODO: this needs to come from a Ds3Client factory that knows how to update the server id.
    private Ds3Client getClient(final String serverId) {
        return this.client;
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
