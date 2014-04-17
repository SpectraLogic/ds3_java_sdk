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

package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlOutput;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;


abstract class BulkRequest extends AbstractRequest {

    private final String bucket;
    private final List<Ds3Object> ds3Objects;
    private final InputStream stream;
    private long size;

    public BulkRequest(final String bucket, final List<Ds3Object> objects) throws XmlProcessingException {
        this.bucket = bucket;
        this.ds3Objects = objects;
        this.stream = generateStream();
    }

    private InputStream generateStream() throws XmlProcessingException {
        final com.spectralogic.ds3client.models.Objects objects =
                new com.spectralogic.ds3client.models.Objects();
        objects.setObject(ds3Objects);
        final String xmlOutput = XmlOutput.toXml(objects, getCommand());

        final byte[] stringBytes = xmlOutput.getBytes();
        this.size = stringBytes.length;
        return new ByteArrayInputStream(stringBytes);
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public String getPath() {
        return "/_rest_/buckets/" + bucket;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    public abstract BulkCommand getCommand ();

    @Override
    public InputStream getStream() {
        return stream;
    }
}
