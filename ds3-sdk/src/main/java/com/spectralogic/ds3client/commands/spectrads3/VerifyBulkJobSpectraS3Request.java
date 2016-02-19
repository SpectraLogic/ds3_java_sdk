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

import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.Ds3ObjectList;
import com.spectralogic.ds3client.serializer.XmlOutput;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import com.spectralogic.ds3client.commands.AbstractRequest;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.Priority;

public class VerifyBulkJobSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String bucketName;

    private final List<Ds3Object> objects;

    private boolean aggregating;

    private String name;

    private Priority priority;
    private long size = 0;

    // Constructor
    
    public VerifyBulkJobSpectraS3Request(final String bucketName, final List<Ds3Object> objects) {
        this.bucketName = bucketName;
        this.objects = objects;
        
        this.getQueryParams().put("operation", "start_bulk_verify");
    }

    public VerifyBulkJobSpectraS3Request withAggregating(final boolean aggregating) {
        this.aggregating = aggregating;
        this.updateQueryParam("aggregating", null);
        return this;
    }

    public VerifyBulkJobSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", UrlEscapers.urlFragmentEscaper().escape(name));
        return this;
    }

    public VerifyBulkJobSpectraS3Request withPriority(final Priority priority) {
        this.priority = priority;
        this.updateQueryParam("priority", priority.toString());
        return this;
    }


    @Override
    public InputStream getStream() {
        final Ds3ObjectList objects = new Ds3ObjectList();
        objects.setObjects(this.objects);

        final String xmlOutput = XmlOutput.toXml(objects, false);

        final byte[] stringBytes = xmlOutput.getBytes();
        this.size = stringBytes.length;
        return new ByteArrayInputStream(stringBytes);
    }

    @Override
    public long getSize() {
        return this.size;
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


    public List<Ds3Object> getObjects() {
        return this.objects;
    }


    public boolean getAggregating() {
        return this.aggregating;
    }


    public String getName() {
        return this.name;
    }


    public Priority getPriority() {
        return this.priority;
    }


}