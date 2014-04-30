package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.ListBucketResult;

public class ObjectListGetter {
    private final Ds3Client client;

    public ObjectListGetter(final Ds3Client client) {
        this.client = client;
    }
    
    public Iterable<Contents> getAllObjects(final String bucket) throws SignatureException, IOException {
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
