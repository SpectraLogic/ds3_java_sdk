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

import java.util.List;
import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.commands.BulkRequest;
import com.spectralogic.ds3client.models.JobChunkClientProcessingOrderGuarantee;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.Priority;

public class GetBulkJobSpectraS3Request extends BulkRequest {


    
    private boolean aggregating;

    private JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee;

    private String name;

    // Constructor
    public GetBulkJobSpectraS3Request(final String bucketName, final List<Ds3Object> objects) throws XmlProcessingException {
        super(bucketName, objects);
        
        this.getQueryParams().put("operation", "start_bulk_get");
    }

    public GetBulkJobSpectraS3Request withAggregating(final boolean aggregating) {
        this.aggregating = aggregating;
        this.updateQueryParam("aggregating", String.valueOf(aggregating));
        return this;
    }

    public GetBulkJobSpectraS3Request withChunkClientProcessingOrderGuarantee(final JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee) {
        this.chunkClientProcessingOrderGuarantee = chunkClientProcessingOrderGuarantee;
        this.updateQueryParam("chunk_client_processing_order_guarantee", chunkClientProcessingOrderGuarantee.toString());
        return this;
    }

    public GetBulkJobSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", UrlEscapers.urlFragmentEscaper().escape(name));
        return this;
    }

    @Override
    public GetBulkJobSpectraS3Request withPriority(final Priority priority) {
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