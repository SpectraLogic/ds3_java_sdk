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
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.Ds3ObjectList;
import com.spectralogic.ds3client.serializer.XmlOutput;
import com.spectralogic.ds3client.utils.Guard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.nio.charset.Charset;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;

public class GetPhysicalPlacementForObjectsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String bucketName;

    private final List<Ds3Object> objects;

    private String storageDomain;
    private long size = 0;

    // Constructor
    
    
    public GetPhysicalPlacementForObjectsSpectraS3Request(final String bucketName, final List<Ds3Object> objects) {
        this.bucketName = bucketName;
        this.objects = objects;
        
        this.getQueryParams().put("operation", "get_physical_placement");

    }

    public GetPhysicalPlacementForObjectsSpectraS3Request withStorageDomain(final String storageDomain) {
        this.storageDomain = storageDomain;
        this.updateQueryParam("storage_domain", storageDomain);
        return this;
    }



    @Override
    public InputStream getStream() {
        if (Guard.isNullOrEmpty(objects)) {
            return null;
        }
        final Ds3ObjectList objects = new Ds3ObjectList();
        objects.setObjects(this.objects);

        final String xmlOutput = XmlOutput.toXml(objects, false);

        final byte[] stringBytes = xmlOutput.getBytes(StandardCharsets.UTF_8);
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


    public String getStorageDomain() {
        return this.storageDomain;
    }


}