package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

class BulkTransferExecutor<T extends ObjectInfo> {
    private final ListeningExecutorService service;
    private final Transferrer<T> transferrer;    
    
    public interface Transferrer<T extends ObjectInfo> {
        public MasterObjectList prime(final String bucket, final List<Ds3Object> ds3Objects)
                throws SignatureException, IOException, XmlProcessingException;
        public void transfer(final UUID jobId, final String bucket, final Ds3Object ds3Object, final T object)
                throws Ds3KeyNotFoundException, IOException, SignatureException;
    }

    public BulkTransferExecutor(final ListeningExecutorService service, final Transferrer<T> transferrer) {
        this.service = service;
        this.transferrer = transferrer;
    }

    public ListenableFuture<Integer> transfer(final String bucket, final Iterable<? extends T> objects) {
        return Futures.transform(
            this.service.submit(this.prime(bucket, objects)),
            this.buildJobStarter(bucket, new BulkObjectLookup(objects))
        );
    }

    private Callable<MasterObjectList> prime(final String bucket, final Iterable<? extends T> objects) {
        return new Callable<MasterObjectList>() {
            @Override
            public MasterObjectList call() throws Exception {
                final List<Ds3Object> ds3Objects = new ArrayList<Ds3Object>();
                for (final ObjectInfo putObject : objects) {
                    ds3Objects.add(putObject.getObject());
                }
                return BulkTransferExecutor.this.transferrer.prime(bucket, ds3Objects);
            }
        };
    }
    
    /**
     * @param bucket
     * @param transferrer
     * @param lookup
     * @return An async function that starts parallel put operations and returns the number of successful puts.
     */
    private AsyncFunction<MasterObjectList, Integer> buildJobStarter(final String bucket, final BulkObjectLookup lookup) {
        return new AsyncFunction<MasterObjectList, Integer>() {
            @Override
            public ListenableFuture<Integer> apply(final MasterObjectList input) throws Exception {
                final List<ListenableFuture<Integer>> results = new ArrayList<ListenableFuture<Integer>>();
                for (final Objects objects : input.getObjects()) {
                    results.add(BulkTransferExecutor.this.buildObjectListFuture(
                        lookup,
                        input.getJobid(),
                        bucket,
                        objects
                    ));
                }
                return Futures.transform(Futures.allAsList(results), buildSumFunction());
            }
        };
    }
    
    private ListenableFuture<Integer> buildObjectListFuture(
            final BulkObjectLookup lookup,
            final UUID jobid,
            final String bucket,
            final Objects objects) {
        return this.service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int objectCount = 0;
                for (final Ds3Object ds3Object : objects) {
                    BulkTransferExecutor.this.transferrer.transfer(
                        jobid,
                        bucket,
                        ds3Object,
                        lookup.get(ds3Object.getName())
                    );
                    objectCount++;
                }
                return objectCount;
            }
        });
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

        public BulkObjectLookup(final Iterable<? extends T> objects) {
            this.objectLookup = this.buildObjectLookup(objects);
        }
        
        protected T get(final String key) throws Ds3KeyNotFoundException {
            final T object = this.objectLookup.get(key);
            if (object == null) {
                throw new Ds3KeyNotFoundException(key);
            }
            return object;
        }

        private Map<String, T> buildObjectLookup(final Iterable<? extends T> objects) {
            final Map<String, T> lookup = new HashMap<String, T>();
            for (final T object : objects) {
                lookup.put(object.getKey(), object);
            }
            return lookup;
        }
    }
}
