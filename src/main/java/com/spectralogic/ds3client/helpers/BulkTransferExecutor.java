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
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

class BulkTransferExecutor {
    private final Transferrer transferrer;    
    
    public interface Transferrer {
        public MasterObjectList prime(final String bucket, final Iterable<Ds3Object> ds3Objects)
                throws SignatureException, IOException, XmlProcessingException;
        public void transfer(final UUID jobId, final String bucket, final Ds3Object ds3Object)
                throws SignatureException, IOException;
    }

    public BulkTransferExecutor(final Transferrer transferrer) {
        this.transferrer = transferrer;
    }

    /**
     * Performs a bulk get or put depending on the Transferrer that was passed to the constructor.
     * 
     * @param bucket
     * @param ds3Objects
     * @return The number of objects transferred.
     * @throws SignatureException
     * @throws IOException
     * @throws XmlProcessingException
     */
    public int transfer(final String bucket, final Iterable<Ds3Object> ds3Objects)
            throws SignatureException, IOException, XmlProcessingException {
        // Perform the bulk prime.
        final MasterObjectList prime = this.transferrer.prime(bucket, ds3Objects);

        // Create a thread pool on which to execute the object list get sequences.
        final ListeningExecutorService service = this.buildListeningExecutorService();
        
        // Start the tasks to get the lists of objects.
        final List<ListenableFuture<Integer>> tasks = new ArrayList<ListenableFuture<Integer>>();
        for (final Objects objects : prime.getObjects()) {
            tasks.add(service.submit(this.buildObjectListLoop(prime.getJobid(), bucket, objects)));
        }

        // Await the tasks and handle getting the exceptions out of the future.
        final Iterable<Integer> counts = this.executeWithExceptionHandling(tasks);
        
        // Shut down the thread pool.
        service.shutdown();
        
        // Sum the number of objects transfered in each task.
        return this.sum(counts);
    }

    private static final int THREAD_COUNT = 10;
    
    /**
     * @return The thread pool to use when creating object list retrieval tasks.
     */
    protected ListeningExecutorService buildListeningExecutorService() {
        return MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(THREAD_COUNT));
    }

    /**
     * Please tell me this exists somewhere in the standard library and I just haven't found it yet.
     * 
     * @param counts
     * @return
     */
    private int sum(final Iterable<Integer> counts) {
        int total = 0;
        for (final int count : counts) {
            total += count;
        }
        return total;
    }

    /**
     * Creates a callable to transfer the given {@code objects}.
     * 
     * @param jobid
     * @param bucket
     * @param objects
     * @return
     */
    private Callable<Integer> buildObjectListLoop(
            final UUID jobid,
            final String bucket,
            final Objects objects) {
        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int objectCount = 0;
                for (final Ds3Object ds3Object : objects) {
                    BulkTransferExecutor.this.transferrer.transfer(jobid, bucket, ds3Object);
                    objectCount++;
                }
                return objectCount;
            }
        };
    }

    /**
     * Synchronously blocks on all of the futures in the list and unwraps any exceptions thrown from them.
     * 
     * @param tasks
     * @return
     * @throws IOException
     * @throws SignatureException
     * @throws XmlProcessingException
     */
    private Iterable<Integer> executeWithExceptionHandling(final List<ListenableFuture<Integer>> tasks)
            throws IOException, SignatureException, XmlProcessingException {
        try {
            // Block on the asynchronous result.
            return Futures.allAsList(tasks).get();
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
