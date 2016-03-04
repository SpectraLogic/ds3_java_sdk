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
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.Ds3ObjectList;
import com.spectralogic.ds3client.serializer.XmlOutput;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import com.spectralogic.ds3client.commands.AbstractRequest;
import java.util.UUID;
import com.google.common.net.UrlEscapers;

public class VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String bucketName;

    private final List<Ds3Object> objects;

    private UUID storageDomainId;
    private long size = 0;

    // Constructor
    
    public VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Request(final String bucketName, final List<Ds3Object> objects) {
        this.bucketName = bucketName;
        this.objects = objects;
        
        this.getQueryParams().put("operation", "verify_physical_placement");
        this.getQueryParams().put("full_details", null);
    }

    public VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Request withStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId;
        this.updateQueryParam("storage_domain_id", storageDomainId.toString());
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
        return HttpVerb.GET;
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


    public UUID getStorageDomainId() {
        return this.storageDomainId;
    }


}