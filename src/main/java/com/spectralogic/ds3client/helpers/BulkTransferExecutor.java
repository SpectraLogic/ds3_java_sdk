package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

class BulkTransferExecutor<T extends ObjectInfo> {
    private static final int THREAD_COUNT = 10;

    private final boolean isManagingService;
    private final ListeningExecutorService service;    
    private final Ds3Client client;
    
    public interface Transferrer<T extends ObjectInfo> {
        public MasterObjectList prime(final List<Ds3Object> ds3Objects)
                throws SignatureException, IOException, XmlProcessingException;
        public void transfer(final UUID jobId, final Ds3Object ds3Object, final T object)
                throws Ds3KeyNotFoundException, IOException, SignatureException;
    }

    public BulkTransferExecutor(final Ds3Client client) {
        this.isManagingService = true;
        this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(THREAD_COUNT));
        this.client = client;
    }

    public BulkTransferExecutor(final ListeningExecutorService service, final Ds3Client client) {
        this.isManagingService = false;
        this.service = service;
        this.client = client;
    }

    public Ds3Client getClient() {
        return this.client;
    }

    public CheckedFuture<Integer, Ds3BulkException> transfer(
            final String bucket,
            final Iterable<T> objects,
            final Transferrer<T> transferrer) {
        
        final ListenableFuture<Integer> result = Futures.transform(
            this.service.submit(this.prime(objects, transferrer)),
            this.buildJobStarter(bucket, transferrer, new BulkObjectLookup(objects))
        );
        this.attachServiceShutdown(result);
        return Futures.makeChecked(result, Ds3BulkException.buildMapper());
    }

    private void attachServiceShutdown(final ListenableFuture<Integer> result) {
        if (this.isManagingService) {
            Futures.addCallback(result, new FutureCallback<Integer>() {
                @Override
                public void onSuccess(final Integer result) {
                    BulkTransferExecutor.this.service.shutdown();
                }

                @Override
                public void onFailure(final Throwable t) {
                    BulkTransferExecutor.this.service.shutdown();
                }
            });
        }
    }

    private Callable<MasterObjectList> prime(final Iterable<T> objects, final Transferrer<T> transferrer) {
        return new Callable<MasterObjectList>() {
            @Override
            public MasterObjectList call() throws Exception {
                final List<Ds3Object> ds3Objects = new ArrayList<Ds3Object>();
                for (final ObjectInfo putObject : objects) {
                    ds3Objects.add(putObject.getObject());
                }
                return transferrer.prime(ds3Objects);
            }
        };
    }
    
    /**
     * @param bucket
     * @param transferrer
     * @param lookup
     * @return An async function that starts parallel put operations and returns the number of successful puts.
     */
    private AsyncFunction<MasterObjectList, Integer> buildJobStarter(
            final String bucket,
            final Transferrer<T> transferrer,
            final BulkObjectLookup lookup) {
        return new AsyncFunction<MasterObjectList, Integer>() {
            @Override
            public ListenableFuture<Integer> apply(final MasterObjectList input) throws Exception {
                final List<ListenableFuture<Integer>> results = new ArrayList<ListenableFuture<Integer>>();
                for (final Objects objects : input.getObjects()) {
                    results.add(BulkTransferExecutor.this.service.submit(BulkTransferExecutor.this.buildObjectListFuture(transferrer, lookup, input.getJobid(), objects)));
                }
                return Futures.transform(Futures.allAsList(results), buildSumFunction());
            }
        };
    }
    
    private Callable<Integer> buildObjectListFuture(
            final Transferrer<T> transferrer,
            final BulkObjectLookup lookup,
            final UUID jobid,
            final Objects objects) {
        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int objectCount = 0;
                for (final Ds3Object ds3Object : objects) {
                    transferrer.transfer(jobid, ds3Object, lookup.get(ds3Object.getName()));
                    objectCount++;
                }
                return objectCount;
            }
        };
    }

    /**
     * @return A function that sums the results of an integer iterable.
     */
    private static Function<Iterable<Integer>, Integer> buildSumFunction() {
        return new Function<Iterable<Integer>, Integer>() {
            @Override
            public Integer apply(final Iterable<Integer> input) {
                int total = 0;
                for (final int count : input) {
                    total += count;
                }
                return total;
            }
        };
    }
    
    class BulkObjectLookup {
        private final Map<String, T> objectLookup;

        public BulkObjectLookup(final Iterable<T> objects) {
            this.objectLookup = this.buildObjectLookup(objects);
        }
        
        protected T get(final String key) throws Ds3KeyNotFoundException {
            final T object = this.objectLookup.get(key);
            if (object == null) {
                throw new Ds3KeyNotFoundException(key);
            }
            return object;
        }

        private Map<String, T> buildObjectLookup(final Iterable<T> objects) {
            final Map<String, T> lookup = new HashMap<String, T>();
            for (final T object : objects) {
                lookup.put(object.getKey(), object);
            }
            return lookup;
        }
    }
}
