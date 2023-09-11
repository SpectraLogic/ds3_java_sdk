/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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
import java.nio.charset.StandardCharsets;

import com.spectralogic.ds3client.utils.Guard;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;

public class GetBlobPersistenceSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String requestPayload;
    private long size = 0;

    // Constructor
    
    
    public GetBlobPersistenceSpectraS3Request(final String requestPayload) {
        this.requestPayload = requestPayload;
        
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/blob_persistence";
    }
    @Override
    public InputStream getStream() {
        if (Guard.isStringNullOrEmpty(requestPayload)) {
            return null;
        }
        final byte[] stringBytes = requestPayload.getBytes(StandardCharsets.UTF_8);
        this.size = stringBytes.length;
        return new ByteArrayInputStream(stringBytes);
    }

    @Override
    public long getSize() {
        return this.size;
    }

    
    public String getRequestPayload() {
        return this.requestPayload;
    }

}
