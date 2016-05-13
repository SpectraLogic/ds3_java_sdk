/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.commands.spectrads3;

import com.spectralogic.ds3client.networking.HttpVerb;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import com.spectralogic.ds3client.utils.Guard;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.spectralogic.ds3client.models.Priority;
import com.google.common.net.UrlEscapers;

public class ReplicatePutJobSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String bucketName;

    private final String requestPayload;

    private Priority priority;
    private long size = 0;

    // Constructor
    
    public ReplicatePutJobSpectraS3Request(final String bucketName, final String requestPayload) {
        this.bucketName = bucketName;
        this.requestPayload = requestPayload;
        
        this.getQueryParams().put("operation", "start_bulk_put");

        this.getQueryParams().put("replicate", null);
    }

    public ReplicatePutJobSpectraS3Request withPriority(final Priority priority) {
        this.priority = priority;
        this.updateQueryParam("priority", priority);
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/bucket/" + this.bucketName;
    }
    @Override
    public InputStream getStream() {
        if (Guard.isStringNullOrEmpty(requestPayload)) {
            return null;
        }
        final byte[] stringBytes = requestPayload.getBytes(Charset.forName("UTF-8"));
        this.size = stringBytes.length;
        return new ByteArrayInputStream(stringBytes);
    }

    @Override
    public long getSize() {
        return this.size;
    }

    
    public String getBucketName() {
        return this.bucketName;
    }


    public String getRequestPayload() {
        return this.requestPayload;
    }


    public Priority getPriority() {
        return this.priority;
    }

}
