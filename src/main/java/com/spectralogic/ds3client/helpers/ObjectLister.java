package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.ListBucketResult;

class ObjectLister {
    private final ListeningExecutorService service;
    private final Ds3Client client;

    public ObjectLister(final ListeningExecutorService service, final Ds3Client client) {
        this.service = service;
        this.client = client;
    }

    public ListenableFuture<Iterable<Contents>> getAllObjects(final String bucket) {
        return this.service.submit(new Callable<Iterable<Contents>>() {
            @Override
            public Iterable<Contents> call() throws Exception {
                return ObjectLister.this.getAllObjectsSynchronous(bucket);
            }
        });
    }
    
    private Iterable<Contents> getAllObjectsSynchronous(final String bucket) throws SignatureException, IOException {
        // Create a result array.
        final ArrayList<Contents> result = new ArrayList<Contents>();
        
        // Create paging state.
        boolean isTruncated = false;
        String marker = null;
        
        // Start the loop.
        do {
            // Build the request.
            final GetBucketRequest request = new GetBucketRequest(bucket);
            if (isTruncated) {
                request.withNextMarker(marker);
            }
            
            // Submit the request.
            final ListBucketResult response = this.client.getBucket(request).getResult();
            
            // Update paging state.
            isTruncated = response.isTruncated();
            marker = response.getNextMarker();
            
            // Add response items to result list.
            for (final Contents contents : response.getContentsList()) {
                result.add(contents);
            }
            
        // Continue if there are still more things to get.
        } while (isTruncated);
        
        // Return the result list.
        return result;
    }
}
