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
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.Priority;

public class PutBulkJobSpectraS3Request extends BulkRequest {

    private static final String MAX_UPLOAD_SIZE_IN_BYTES = "100000000000";
    public static final int MIN_UPLOAD_SIZE_IN_BYTES = 10485760;

    
    private boolean aggregating;

    private boolean ignoreNamingConflicts;

    private String name;

    // Constructor
    public PutBulkJobSpectraS3Request(final String bucketName, final List<Ds3Object> objects) throws XmlProcessingException {
        super(bucketName, objects);
        
        this.getQueryParams().put("operation", "start_bulk_put");
    }

    public PutBulkJobSpectraS3Request withAggregating(final boolean aggregating) {
        this.aggregating = aggregating;
        this.updateQueryParam("aggregating", aggregating);
        return this;
    }

    public PutBulkJobSpectraS3Request withIgnoreNamingConflicts(final boolean ignoreNamingConflicts) {
        this.ignoreNamingConflicts = ignoreNamingConflicts;
        if (this.ignoreNamingConflicts) {
            this.getQueryParams().put("ignore_naming_conflicts", null);
        } else {
            this.getQueryParams().remove("ignore_naming_conflicts");
        }
        return this;
    }

    public PutBulkJobSpectraS3Request withMaxUploadSize(final long maxUploadSize) {
        if (maxUploadSize > MIN_UPLOAD_SIZE_IN_BYTES) {
            this.getQueryParams().put("max_upload_size", Long.toString(maxUploadSize));
        } else {
            this.getQueryParams().put("max_upload_size", MAX_UPLOAD_SIZE_IN_BYTES);
        }
        return this;
    }

    public PutBulkJobSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }

    @Override
    public PutBulkJobSpectraS3Request withPriority(final Priority priority) {
        super.withPriority(priority);
        return this;
    }


    
    public boolean getAggregating() {
        return this.aggregating;
    }


    public boolean getIgnoreNamingConflicts() {
        return this.ignoreNamingConflicts;
    }


    public String getName() {
        return this.name;
    }


    @Override
    public BulkCommand getCommand() {
        return BulkCommand.PUT;
    }
}