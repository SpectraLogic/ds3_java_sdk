package com.spectralogic.ds3client.helpers;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.Contents;

public class BucketContentGetter {
    private static final int THREAD_COUNT = 10;

    private final boolean isManagingService;
    private final ListeningExecutorService service;    
    private final Ds3Client client;

    public interface GetObjectFactory extends Function<Contents, GetObject> {}

    public BucketContentGetter(final Ds3Client client) {
        this.isManagingService = true;
        this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(THREAD_COUNT));
        this.client = client;
    }

    public BucketContentGetter(final ListeningExecutorService service, final Ds3Client client) {
        this.isManagingService = false;
        this.service = service;
        this.client = client;
    }
    
    public CheckedFuture<Integer, Ds3BulkException> getBucketContents(final String bucket, final GetObjectFactory getObjectFactory) {
        final ListenableFuture<Integer> result = Futures.transform(
            this.listObjects(bucket),
            this.retrieveObjects(bucket, getObjectFactory)
        );
        this.attachServiceShutdown(result);
        return Futures.makeChecked(result, Ds3BulkException.buildMapper());
    }

    private ListenableFuture<Iterable<Contents>> listObjects(final String bucket) {
        final ObjectListGetter objectListGetter = new ObjectListGetter(this.client);
        return this.service.submit(new Callable<Iterable<Contents>>() {
            @Override
            public Iterable<Contents> call() throws Exception {
                return objectListGetter.getAllObjects(bucket);
            }
        });
    }

    private AsyncFunction<Iterable<Contents>, Integer> retrieveObjects(
            final String bucket,
            final GetObjectFactory getObjectFactory) {
        final BulkGetter bulkGetter = new BulkGetter(this.service, this.client);
        final AsyncFunction<Iterable<Contents>, Integer> function = new AsyncFunction<Iterable<Contents>, Integer>() {
            @Override
            public ListenableFuture<Integer> apply(final Iterable<Contents> input) throws Exception {
                return bulkGetter.execute(bucket, Iterables.transform(input, getObjectFactory));
            }
        };
        return function;
    }

    private void attachServiceShutdown(final ListenableFuture<Integer> result) {
        if (this.isManagingService) {
            Futures.addCallback(result, new FutureCallback<Integer>() {
                @Override
                public void onSuccess(final Integer result) {
                    BucketContentGetter.this.service.shutdown();
                }

                @Override
                public void onFailure(final Throwable t) {
                    BucketContentGetter.this.service.shutdown();
                }
            });
        }
    }
}
