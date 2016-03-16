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
import com.spectralogic.ds3client.models.ReplicationConflictResolutionMode;
import com.spectralogic.ds3client.models.Priority;
import com.google.common.net.UrlEscapers;

public class ReplicatePutJobSpectraS3Request extends BulkRequest {


    
    private ReplicationConflictResolutionMode conflictResolutionMode;

    // Constructor
    public ReplicatePutJobSpectraS3Request(final String bucketName, final List<Ds3Object> objects) throws XmlProcessingException {
        super(bucketName, objects);
        
        this.getQueryParams().put("operation", "start_bulk_put");

        this.getQueryParams().put("replicate", null);
    }

    public ReplicatePutJobSpectraS3Request withConflictResolutionMode(final ReplicationConflictResolutionMode conflictResolutionMode) {
        this.conflictResolutionMode = conflictResolutionMode;
        this.updateQueryParam("conflict_resolution_mode", conflictResolutionMode);
        return this;
    }

    @Override
    public ReplicatePutJobSpectraS3Request withPriority(final Priority priority) {
        super.withPriority(priority);
        return this;
    }


    
    public ReplicationConflictResolutionMode getConflictResolutionMode() {
        return this.conflictResolutionMode;
    }


    @Override
    public BulkCommand getCommand() {
        return BulkCommand.PUT;
    }
}