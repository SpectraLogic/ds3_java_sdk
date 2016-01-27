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

import com.spectralogic.ds3client.commands.BulkRequest;
import java.util.List;
import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.models.JobChunkClientProcessingOrderGuarantee;
import java.lang.String;
import com.spectralogic.ds3client.models.BlobStoreTaskPriority;

public class CreateGetJobSpectraS3Request extends BulkRequest {


    
    private boolean aggregating;
    private JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee;
    private String name;
    

    // Constructor
    public CreateGetJobSpectraS3Request(final String bucketName, final List<Ds3Object> objects) throws XmlProcessingException {
        super(bucketName, objects);
        this.getQueryParams().put("operation", "start_bulk_get");
        
    }

    public CreateGetJobSpectraS3Request withAggregating(final boolean aggregating) {
        this.aggregating = aggregating;
        this.updateQueryParam("aggregating", null);
        return this;
    }

    public CreateGetJobSpectraS3Request withChunkClientProcessingOrderGuarantee(final JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee) {
        this.chunkClientProcessingOrderGuarantee = chunkClientProcessingOrderGuarantee;
        this.updateQueryParam("chunk_client_processing_order_guarantee", chunkClientProcessingOrderGuarantee.toString());
        return this;
    }

    public CreateGetJobSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }

    @Override
    public CreateGetJobSpectraS3Request withPriority(final BlobStoreTaskPriority priority) {
        super.withPriority(priority);
        return this;
    }



    public boolean getAggregating() {
        return this.aggregating;
    }

    public JobChunkClientProcessingOrderGuarantee getChunkClientProcessingOrderGuarantee() {
        return this.chunkClientProcessingOrderGuarantee;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public BulkCommand getCommand() {
        return BulkCommand.GET;
    }
}