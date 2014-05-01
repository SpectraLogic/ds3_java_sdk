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
import com.spectralogic.ds3client.models.Ds3Object;

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
     * @param objectGetter
     * @return A future containing the number of objects put or the resulting exception.
     */
    public CheckedFuture<Integer, Ds3BulkException> readObjects(
            final String bucket,
            final Iterable<Ds3Object> objectsToGet,
            final ObjectGetter objectGetter) {
        return checked(
            new BulkTransferExecutor(this.service, new BulkGetTransferrer(this.client, objectGetter))
                .transfer(bucket, objectsToGet)
        );
    }
    
    /**
     * Performs a bulk put of {@code objectsToPut} into {@code bucket}. 
     * 
     * @param bucket
     * @param objectsToPut
     * @param putter
     * @return A future containing the number of objects put or the resulting exception.
     */
    public CheckedFuture<Integer, Ds3BulkException> writeObjects(
            final String bucket,
            final Iterable<Ds3Object> objectsToPut,
            final ObjectPutter putter) {
        return checked(
            new BulkTransferExecutor(this.service, new BulkPutTransferrer(this.client, putter))
                .transfer(bucket, objectsToPut)
        );
    }
    
    public CheckedFuture<Integer, Ds3BulkException> readAllObjects(final String bucket, final ObjectGetter getter) {
        final BulkTransferExecutor bulkTransferExecutor =
                new BulkTransferExecutor(this.service, new BulkGetTransferrer(this.client, getter));
        return checked(Futures.transform(
            new ObjectLister(this.service, this.client).getAllObjects(bucket),
            new AsyncFunction<Iterable<Contents>, Integer>() {
                @Override
                public ListenableFuture<Integer> apply(final Iterable<Contents> input) throws Exception {
                    return bulkTransferExecutor.transfer(bucket, Iterables.transform(input, new ContentsToDs3ObjectConverter()));
                }
            }
        ));
    }
    
    private final class ContentsToDs3ObjectConverter implements Function<Contents, Ds3Object> {
        @Override
        public Ds3Object apply(final Contents input) {
            return new Ds3Object(input.getKey());
        }
    }
    
    public CheckedFuture<Iterable<Contents>, Ds3BulkException> listObjects(final String bucket)
            throws SignatureException, IOException {
        return checked(new ObjectLister(this.service, this.client).getAllObjects(bucket));
    }
    
    private static <T> CheckedFuture<T, Ds3BulkException> checked(final ListenableFuture<T> future) {
        return Futures.makeChecked(future, Ds3BulkException.buildMapper());
    }
}
