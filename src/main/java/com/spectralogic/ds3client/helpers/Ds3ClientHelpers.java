package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

public class Ds3ClientHelpers {
    private final Ds3Client client;

    public Ds3ClientHelpers(final Ds3Client client) {
        this.client = client;
    }
    
    public interface ObjectGetter {
        public void writeContents(String key, InputStream contents) throws IOException;
    }
    
    public interface ObjectPutter {
        public InputStream getContent(String key);
    }

    /**
     * Performs a bulk get of {@code objectsToGet} from {@code bucket} using the given {@code getter}.
     * 
     * @param bucket
     * @param objectsToGet
     * @param getter
     * @return The number of objects read.
     * @throws SignatureException
     * @throws IOException
     * @throws XmlProcessingException
     */
    public int readObjects(final String bucket, final Iterable<Ds3Object> objectsToGet, final ObjectGetter getter)
            throws SignatureException, IOException, XmlProcessingException {
        return new BulkTransferExecutor(new BulkGetTransferrer(this.client, getter))
            .transfer(bucket, objectsToGet);
    }
    
    /**
     * Performs a bulk put of {@code objectsToPut} into {@code bucket} using the given {@code putter}.
     * 
     * @param bucket
     * @param objectsToPut
     * @param putter
     * @return The number of objects put.
     * @throws SignatureException
     * @throws IOException
     * @throws XmlProcessingException
     */
    public int writeObjects(final String bucket, final Iterable<Ds3Object> objectsToPut, final ObjectPutter putter)
            throws SignatureException, IOException, XmlProcessingException {
        return new BulkTransferExecutor(new BulkPutTransferrer(this.client, putter))
            .transfer(bucket, objectsToPut);
    }
    
    /**
     * Reads all objects from the {@code bucket} using the given {@code getter}.
     * 
     * @param bucket
     * @param getter
     * @return The number of objects read.
     * @throws SignatureException
     * @throws IOException
     * @throws XmlProcessingException
     */
    public int readAllObjects(final String bucket, final ObjectGetter getter)
            throws SignatureException, IOException, XmlProcessingException {
        // Get all of the Contents objects..
        final Iterable<Contents> contentsList = this.listObjects(bucket);
        
        // Convert them all to Ds3Objects. (OMG Java)
        final List<Ds3Object> ds3Objects = new ArrayList<>();
        for (final Contents contents : contentsList) {
            ds3Objects.add(new Ds3Object(contents.getKey()));
        }
        
        // Perform the bulk read.
        return this.readObjects(bucket, ds3Objects, getter);
    }
    
    /**
     * @param bucket
     * @return All of the objects in the bucket.
     * @throws SignatureException
     * @throws IOException
     */
    public Iterable<Contents> listObjects(final String bucket) throws SignatureException, IOException {
        // Create a result array.
        final List<Contents> result = new ArrayList<>();
        
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
