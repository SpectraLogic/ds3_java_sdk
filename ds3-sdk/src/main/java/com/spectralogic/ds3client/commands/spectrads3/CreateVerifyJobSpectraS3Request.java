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

import com.spectralogic.ds3client.commands.AbstractRequest;
import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.Ds3ObjectList;
import com.spectralogic.ds3client.serializer.XmlOutput;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import com.spectralogic.ds3client.models.BlobStoreTaskPriority;

public class CreateVerifyJobSpectraS3Request extends AbstractRequest {

    // Variables
    private final List<Ds3Object> objects;
    
    private final String bucketName;

    private BlobStoreTaskPriority priority;

    // Constructor
    public CreateVerifyJobSpectraS3Request(final String bucketName, final List<Ds3Object> objects) {
        this.objects = objects;
        this.bucketName = bucketName;
        this.getQueryParams().put("operation", "start_bulk_verify");
            }

    public CreateVerifyJobSpectraS3Request withPriority(final BlobStoreTaskPriority priority) {
        this.priority = priority;
        this.updateQueryParam("priority", priority.toString());
        return this;
    }


    public InputStream getContentStream() {
        final Ds3ObjectList objects = new Ds3ObjectList();
        objects.setObjects(this.objects);

        final String xmlOutput = XmlOutput.toXml(objects, false);

        final byte[] stringBytes = xmlOutput.getBytes();
        return new ByteArrayInputStream(stringBytes);
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/bucket/" + this.bucketName;
    }
    
    public String getBucketName() {
        return this.bucketName;
    }


    public BlobStoreTaskPriority getPriority() {
        return this.priority;
    }


    public List<Ds3Object> getObjects() {
        return this.objects;
    }

}