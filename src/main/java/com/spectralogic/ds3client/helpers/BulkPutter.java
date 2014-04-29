package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.io.InputStream;
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
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.BulkPutRequest;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;

public class BulkPutter {
    private static final int THREAD_COUNT = 10;
    
    private final Ds3Client client;

    public BulkPutter(final Ds3Client client) {
        this.client = client;
    }
    
    /**
     * Performs a bulk put of {@code objectsToPut} into {@code bucket}. 
     * 
     * @param bucket
     * @param objectsToPut
     * @return A future containing the number of objects put or the resulting exception.
     */
    public CheckedFuture<Integer, Ds3BulkException> execute(final String bucket, final Iterable<PutObject> objectsToPut) {
        final ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(THREAD_COUNT));
        return Futures.makeChecked(
            Futures.transform(
                buildBulkPutPrimer(service, bucket, buildDs3ObjectList(objectsToPut)),
                buildJobStarter(service, bucket, new ObjectStreamGetter(objectsToPut))
            ),
            Ds3BulkException.buildMapper()
        );
    }

    /**
     * Maps PutObject instances to Ds3Object instances.
     * 
     * @param objectsToPut
     * @return
     */
    private static List<Ds3Object> buildDs3ObjectList(final Iterable<PutObject> objectsToPut) {
        final List<Ds3Object> ds3Objects = new ArrayList<Ds3Object>();
        for (final PutObject putObject : objectsToPut) {
            ds3Objects.add(new Ds3Object(putObject.getKey(), putObject.getSize()));
        }
        return ds3Objects;
    }
    
    /**
     * @param service
     * @param bucket
     * @param ds3Objects
     * @return A bulk prime future.
     */
    private ListenableFuture<MasterObjectList> buildBulkPutPrimer(
            final ListeningExecutorService service,
            final String bucket,
            final List<Ds3Object> ds3Objects) {
        return service.submit(new Callable<MasterObjectList>() {
            @Override
            public MasterObjectList call() throws Exception {
                return client.bulkPut(new BulkPutRequest(bucket, ds3Objects)).getResult();
            }
        });
    }
    
    /**
     * @param service
     * @param bucket
     * @param objectStreamGetter
     * @return An async function that starts parallel put operations and returns the number of successful puts.
     */
    private AsyncFunction<MasterObjectList, Integer> buildJobStarter(
            final ListeningExecutorService service,
            final String bucket,
            final ObjectStreamGetter objectStreamGetter) {
        return new AsyncFunction<MasterObjectList, Integer>() {
            @Override
            public ListenableFuture<Integer> apply(final MasterObjectList input) throws Exception {
                final List<ListenableFuture<Integer>> results = new ArrayList<ListenableFuture<Integer>>();
                for (final Objects objects : input.getObjects()) {
                    results.add(buildObjectListPutter(bucket, input.getJobid(), service, objectStreamGetter, objects));
                }
                return Futures.transform(Futures.allAsList(results), buildSumFunction());
            }
        };
    }
    
    /**
     * @param bucket
     * @param jobId
     * @param service
     * @param objectStreamGetter
     * @param objects
     * @return A future that puts a list of objects and returns how many objects it put.
     */
    private ListenableFuture<Integer> buildObjectListPutter(
            final String bucket,
            final UUID jobId,
            final ListeningExecutorService service,
            final ObjectStreamGetter objectStreamGetter,
            final Objects objects) {
        return service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int objectCount = 0;
                for (final Ds3Object ds3Object : objects) {
                    client.putObject(new PutObjectRequest(
                        bucket,
                        ds3Object.getName(),
                        jobId,
                        ds3Object.getSize(),
                        objectStreamGetter.get(ds3Object.getName())
                    ));
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
    
    private final class ObjectStreamGetter {
        private final Map<String, PutObject> objectLookup;

        public ObjectStreamGetter(final Iterable<PutObject> objectsToPut) {
            objectLookup = new HashMap<String, PutObject>();
            for (final PutObject putObject : objectsToPut) {
                objectLookup.put(putObject.getKey(), putObject);
            }
        }
        
        public InputStream get(final String key) throws Ds3KeyNotFoundException, IOException {
            final PutObject putObject = this.objectLookup.get(key);
            if (putObject == null) {
                throw new Ds3KeyNotFoundException(key);
            }
            return putObject.getContents();
        }
    }
}
