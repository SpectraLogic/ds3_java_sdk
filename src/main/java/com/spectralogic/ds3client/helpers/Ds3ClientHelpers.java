package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.helpers.BucketContentGetter.GetObjectFactory;
import com.spectralogic.ds3client.models.Contents;

public class Ds3ClientHelpers {
    private static final int THREAD_COUNT = 10;

    private final boolean isManagingService;
    private final ListeningExecutorService service;    
    private final Ds3Client client;

    public Ds3ClientHelpers(final Ds3Client client) {
        this.isManagingService = true;
        this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(THREAD_COUNT));
        this.client = client;
    }

    public Ds3ClientHelpers(final ListeningExecutorService service, final Ds3Client client) {
        this.isManagingService = false;
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
    public CheckedFuture<Integer, Ds3BulkException> readObjects(final String bucket, final Iterable<GetObject> objectsToGet) {
        return new BulkGetter(this.service, this.client).execute(bucket, objectsToGet);
    }
    
    /**
     * Performs a bulk put of {@code objectsToPut} into {@code bucket}. 
     * 
     * @param bucket
     * @param objectsToPut
     * @return A future containing the number of objects put or the resulting exception.
     */
    public CheckedFuture<Integer, Ds3BulkException> writeObjects(final String bucket, final Iterable<PutObject> objectsToPut) {
        return new BulkPutter(this.service, this.client).execute(bucket, objectsToPut);
    }
    
    public CheckedFuture<Integer, Ds3BulkException> readAllObjects(final String bucket, final GetObjectFactory getObjectFactory) {
        return new BucketContentGetter(this.service, this.client).getBucketContents(bucket, getObjectFactory);
    }
    
    public Iterable<Contents> listObjects(final String bucket) throws SignatureException, IOException {
        return new ObjectListGetter(this.client).getAllObjects(bucket);
    }
}
