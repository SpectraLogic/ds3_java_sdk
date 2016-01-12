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

package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.models.BlobStoreTaskPriority;
import com.spectralogic.ds3client.models.JobChunkClientProcessingOrderGuarantee;
import com.spectralogic.ds3client.models.WriteOptimization;
import com.spectralogic.ds3client.models.bulk.*;
import com.spectralogic.ds3client.serializer.XmlOutput;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public abstract class BulkRequest extends AbstractRequest {

    private final String bucket;
    private final List<Ds3Object> ds3Objects;
    private InputStream stream;
    private long size;
    private BlobStoreTaskPriority priority;
    private WriteOptimization writeOptimization;
    protected JobChunkClientProcessingOrderGuarantee chunkOrdering;

    public BulkRequest(final String bucket, final List<Ds3Object> objects) {
        this.bucket = bucket;
        this.ds3Objects = objects;
    }

    public BulkRequest withPriority(final BlobStoreTaskPriority priority) {
        this.priority = priority;
        return this;
    }

    public BulkRequest withWriteOptimization(final WriteOptimization writeOptimization) {
        this.writeOptimization = writeOptimization;
        return this;
    }

    private InputStream generateStream() {
        final Ds3ObjectList objects =
                new Ds3ObjectList();
        objects.setObjects(this.ds3Objects);
        objects.setPriority(this.priority);
        objects.setWriteOptimization(this.writeOptimization);
        objects.setChunkClientProcessingOrderGuarantee(this.chunkOrdering);
        
        final StringBuilder xmlOutputBuilder = new StringBuilder();
        if (this.getCommand() == BulkCommand.PUT) {
            xmlOutputBuilder.append(XmlOutput.toXml(objects, true));
        } else {
            xmlOutputBuilder.append(XmlOutput.toXml(objects, false));
        }

        final byte[] stringBytes = xmlOutputBuilder.toString().getBytes(Charset.forName("UTF-8"));
        this.size = stringBytes.length;
        return new ByteArrayInputStream(stringBytes);
    }
    
    public String getBucket() {
        return this.bucket;
    }

    public List<Ds3Object> getObjects() {
        return this.ds3Objects;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public String getPath() {
        return "/_rest_/bucket/" + this.bucket;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    public abstract BulkCommand getCommand ();

    @Override
    public InputStream getStream() {
        if (this.stream == null) {
            this.stream = generateStream();
        }
        return this.stream;
    }

    public BlobStoreTaskPriority getPriority() {
        return priority;
    }

    public WriteOptimization getWriteOptimization() {
        return writeOptimization;
    }
}
