/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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
import java.util.List;
import java.nio.charset.Charset;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.google.common.net.UrlEscapers;
import java.util.UUID;

public class EjectStorageDomainBlobsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String bucketId;

    private final String storageDomainId;

    private final List<Ds3Object> objects;

    private String ejectLabel;

    private String ejectLocation;
    private long size = 0;

    // Constructor
    
    
    public EjectStorageDomainBlobsSpectraS3Request(final String bucketId, final List<Ds3Object> objects, final UUID storageDomainId) {
        this.bucketId = bucketId;
        this.storageDomainId = storageDomainId.toString();
        this.objects = objects;
        
        this.getQueryParams().put("operation", "eject");

        this.getQueryParams().put("blobs", null);
        this.updateQueryParam("bucket_id", bucketId);

        this.updateQueryParam("storage_domain_id", storageDomainId);

    }

    
    public EjectStorageDomainBlobsSpectraS3Request(final String bucketId, final List<Ds3Object> objects, final String storageDomainId) {
        this.bucketId = bucketId;
        this.storageDomainId = storageDomainId;
        this.objects = objects;
        
        this.getQueryParams().put("operation", "eject");

        this.getQueryParams().put("blobs", null);
        this.updateQueryParam("bucket_id", bucketId);

        this.updateQueryParam("storage_domain_id", storageDomainId);

    }

    public EjectStorageDomainBlobsSpectraS3Request withEjectLabel(final String ejectLabel) {
        this.ejectLabel = ejectLabel;
        this.updateQueryParam("eject_label", ejectLabel);
        return this;
    }


    public EjectStorageDomainBlobsSpectraS3Request withEjectLocation(final String ejectLocation) {
        this.ejectLocation = ejectLocation;
        this.updateQueryParam("eject_location", ejectLocation);
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

        final byte[] stringBytes = xmlOutput.getBytes(Charset.forName("UTF-8"));
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
        return "/_rest_/tape";
    }
    
    public String getBucketId() {
        return this.bucketId;
    }


    public String getStorageDomainId() {
        return this.storageDomainId;
    }


    public List<Ds3Object> getObjects() {
        return this.objects;
    }


    public String getEjectLabel() {
        return this.ejectLabel;
    }


    public String getEjectLocation() {
        return this.ejectLocation;
    }


}