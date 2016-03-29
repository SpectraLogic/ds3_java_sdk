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
package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.models.multipart.CompleteMultipartUpload;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import com.spectralogic.ds3client.serializer.XmlOutput;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import java.util.UUID;
import com.google.common.net.UrlEscapers;

public class CompleteMultiPartUploadRequest extends AbstractRequest {

    // Variables
    
    private final String bucketName;

    private final String objectName;

    private final String uploadId;

    private final CompleteMultipartUpload requestPayload;
    private long size = 0;

    // Constructor
    
    public CompleteMultiPartUploadRequest(final String bucketName, final String objectName, final CompleteMultipartUpload requestPayload, final UUID uploadId) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.uploadId = uploadId.toString();
        this.requestPayload = requestPayload;
        
        this.getQueryParams().put("upload_id", uploadId.toString());
    }

    public CompleteMultiPartUploadRequest(final String bucketName, final String objectName, final CompleteMultipartUpload requestPayload, final String uploadId) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.uploadId = uploadId;
        this.requestPayload = requestPayload;
        
        this.getQueryParams().put("upload_id", UrlEscapers.urlFragmentEscaper().escape(uploadId).replace("+", "%2B"));
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/" + this.bucketName + "/" + this.objectName;
    }
    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public InputStream getStream() {
        if (requestPayload == null) {
            return null;
        }

        final String xmlOutput = XmlOutput.toXml(requestPayload);

        final byte[] stringBytes = xmlOutput.getBytes();
        this.size = stringBytes.length;
        return new ByteArrayInputStream(stringBytes);
    }

    
    public String getBucketName() {
        return this.bucketName;
    }


    public String getObjectName() {
        return this.objectName;
    }


    public String getUploadId() {
        return this.uploadId;
    }


    public CompleteMultipartUpload getRequestPayload() {
        return this.requestPayload;
    }

}