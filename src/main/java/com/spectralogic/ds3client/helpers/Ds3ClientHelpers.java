package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.Contents;

public class Ds3ClientHelpers {
    private final ListeningExecutorService service;
    private final Ds3Client client;

    public Ds3ClientHelpers(final ListeningExecutorService service, final Ds3Client client) {
        this.service = service;
        this.client = client;
    }

    /**
     * Performs a bulk get of {@code objectsToPut} into {@code bucket}. 
     * 
     * @param bucket
     * @param objectsToGet
     * @return A future containing the number of objects put or the resulting exception.
     */
    public CheckedFuture<Integer, Ds3BulkException> readObjects(
            final String bucket,
            final Iterable<? extends GetObject> objectsToGet) {
        return checked(
            new BulkTransferExecutor<GetObject>(this.service, new BulkGetTransferrer(this.client))
                .transfer(bucket, objectsToGet)
        );
    }
    
    /**
     * Performs a bulk put of {@code objectsToPut} into {@code bucket}. 
     * 
     * @param bucket
     * @param objectsToPut
     * @return A future containing the number of objects put or the resulting exception.
     */
    public CheckedFuture<Integer, Ds3BulkException> writeObjects(
            final String bucket,
            final Iterable<? extends PutObject> objectsToPut) {
        return checked(
            new BulkTransferExecutor<PutObject>(this.service, new BulkPutTransferrer(this.client))
                .transfer(bucket, objectsToPut)
        );
    }
    
    public CheckedFuture<Integer, Ds3BulkException> readAllObjects(
            final String bucket,
            final Function<Contents, GetObject> getObjectFactory) {
        final BulkTransferExecutor<GetObject> bulkTransferExecutor =
                new BulkTransferExecutor<GetObject>(this.service, new BulkGetTransferrer(this.client));
        return checked(Futures.transform(
            new ObjectLister(this.service, this.client).getAllObjects(bucket),
            new AsyncFunction<Iterable<Contents>, Integer>() {
                @Override
                public ListenableFuture<Integer> apply(final Iterable<Contents> input) throws Exception {
                    return bulkTransferExecutor
                        .transfer(bucket, Iterables.transform(input, getObjectFactory));
                }
            }
        ));
    }
    
    public CheckedFuture<Iterable<Contents>, Ds3BulkException> listObjects(final String bucket)
            throws SignatureException, IOException {
        return checked(new ObjectLister(this.service, this.client).getAllObjects(bucket));
    }
    
    private static <T> CheckedFuture<T, Ds3BulkException> checked(final ListenableFuture<T> future) {
        return Futures.makeChecked(future, Ds3BulkException.buildMapper());
    }
}
