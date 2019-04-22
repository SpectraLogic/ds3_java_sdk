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

import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.commands.interfaces.BulkRequest;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.Priority;

public class StageObjectsJobSpectraS3Request extends BulkRequest {


    
    private String name;

    // Constructor
    
    public StageObjectsJobSpectraS3Request(final String bucketName, final Iterable<Ds3Object> objects) {
        super(bucketName, objects);
        
        this.getQueryParams().put("operation", "start_bulk_stage");

    }

    public StageObjectsJobSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }


    @Override
    public StageObjectsJobSpectraS3Request withPriority(final Priority priority) {
        super.withPriority(priority);
        return this;
    }



    
    public String getName() {
        return this.name;
    }


    @Override
    public BulkCommand getCommand() {
        return BulkCommand.GET;
    }
}