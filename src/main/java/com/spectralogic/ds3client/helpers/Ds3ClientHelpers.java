/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.BulkGetRequest;
import com.spectralogic.ds3client.commands.BulkGetResponse;
import com.spectralogic.ds3client.commands.BulkPutRequest;
import com.spectralogic.ds3client.commands.BulkPutResponse;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

public class Ds3ClientHelpers {
    private final Ds3Client client;
    
    public interface ObjectGetter {
        public void writeContents(String key, InputStream contents) throws IOException;
    }
    
    public interface ObjectPutter {
        public InputStream getContent(String key) throws IOException;
    }
    
    public interface IJob {
        public UUID getJobId();
        public String getBucketName();
    }
    
    public interface IWriteJob extends IJob {
        public void write(ObjectPutter putter) throws SignatureException, IOException, XmlProcessingException;
    }
    
    public interface IReadJob extends IJob {
        public void read(ObjectGetter getter) throws SignatureException, IOException, XmlProcessingException;
    }

    public Ds3ClientHelpers(final Ds3Client client) {
        this.client = client;
    }
    
    public IWriteJob startWriteJob(final String bucket, final Iterable<Ds3Object> objectsToWrite)
            throws SignatureException, IOException, XmlProcessingException {
        try(final BulkPutResponse prime = this.client.bulkPut(new BulkPutRequest(bucket, Lists.newArrayList(objectsToWrite)))) {
            final MasterObjectList result = prime.getResult();
            return new WriteJob(this.client, result.getJobid(), bucket, result.getObjects());
        }
    }
    
    public IReadJob startReadJob(final String bucket, final Iterable<Ds3Object> objectsToRead)
            throws SignatureException, IOException, XmlProcessingException {
        try(final BulkGetResponse prime = this.client.bulkGet(new BulkGetRequest(bucket, Lists.newArrayList(objectsToRead)))) {
            final MasterObjectList result = prime.getResult();
            return new ReadJob(this.client, result.getJobid(), bucket, result.getObjects());
        }
    }
    
    public IReadJob startReadAllJob(final String bucket)
            throws SignatureException, IOException, XmlProcessingException {
        final Iterable<Contents> contentsList = this.listObjects(bucket);
        
        final List<Ds3Object> ds3Objects = new ArrayList<>();
        for (final Contents contents : contentsList) {
            ds3Objects.add(new Ds3Object(contents.getKey()));
        }
        
        return this.startReadJob(bucket, ds3Objects);
    }

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
