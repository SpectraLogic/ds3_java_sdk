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
import com.spectralogic.ds3client.utils.Guard;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import java.util.UUID;

public class GetBlobsOnTapeSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String tapeId;

    private final List<Ds3Object> objects;
    private long size = 0;

    // Constructor
    
    public GetBlobsOnTapeSpectraS3Request(final List<Ds3Object> objects, final UUID tapeId) {
        this.tapeId = tapeId.toString();
        this.objects = objects;
        
        this.getQueryParams().put("operation", "get_physical_placement");

    }

    public GetBlobsOnTapeSpectraS3Request(final List<Ds3Object> objects, final String tapeId) {
        this.tapeId = tapeId;
        this.objects = objects;
        
        this.getQueryParams().put("operation", "get_physical_placement");

    }


    @Override
    public InputStream getStream() {
        if (Guard.isNullOrEmpty(objects)) {
            return null;
        }
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
        return "/_rest_/tape/" + tapeId;
    }
    
    public String getTapeId() {
        return this.tapeId;
    }


    public List<Ds3Object> getObjects() {
        return this.objects;
    }


}