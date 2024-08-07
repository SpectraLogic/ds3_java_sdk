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
import com.spectralogic.ds3client.models.Priority;

public class PutBulkJobSpectraS3Request extends BulkRequest {

    private static final String MAX_UPLOAD_SIZE_IN_BYTES = "100000000000";
    public static final int MIN_UPLOAD_SIZE_IN_BYTES = 10485760;

    
    private boolean aggregating;

    private boolean deadJobCleanupAllowed;

    private boolean force;

    private boolean ignoreNamingConflicts;

    private boolean implicitJobIdResolution;

    private boolean minimizeSpanningAcrossMedia;

    private String name;

    private boolean preAllocateJobSpace;

    private boolean protectedFlag;

    private boolean verifyAfterWrite;

    // Constructor
    
    public PutBulkJobSpectraS3Request(final String bucketName, final Iterable<Ds3Object> objects) {
        super(bucketName, objects);
        
        this.getQueryParams().put("operation", "start_bulk_put");

    }

    public PutBulkJobSpectraS3Request withAggregating(final boolean aggregating) {
        this.aggregating = aggregating;
        this.updateQueryParam("aggregating", aggregating);
        return this;
    }


    public PutBulkJobSpectraS3Request withDeadJobCleanupAllowed(final boolean deadJobCleanupAllowed) {
        this.deadJobCleanupAllowed = deadJobCleanupAllowed;
        this.updateQueryParam("dead_job_cleanup_allowed", deadJobCleanupAllowed);
        return this;
    }


    public PutBulkJobSpectraS3Request withForce(final boolean force) {
        this.force = force;
        if (this.force) {
            this.getQueryParams().put("force", null);
        } else {
            this.getQueryParams().remove("force");
        }
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


    public PutBulkJobSpectraS3Request withImplicitJobIdResolution(final boolean implicitJobIdResolution) {
        this.implicitJobIdResolution = implicitJobIdResolution;
        this.updateQueryParam("implicit_job_id_resolution", implicitJobIdResolution);
        return this;
    }


    public PutBulkJobSpectraS3Request withMaxUploadSize(final long maxUploadSize) {
        if (maxUploadSize >= MIN_UPLOAD_SIZE_IN_BYTES) {
            this.getQueryParams().put("max_upload_size", Long.toString(maxUploadSize));
        } else {
            this.getQueryParams().put("max_upload_size", MAX_UPLOAD_SIZE_IN_BYTES);
        }
        return this;
    }


    public PutBulkJobSpectraS3Request withMinimizeSpanningAcrossMedia(final boolean minimizeSpanningAcrossMedia) {
        this.minimizeSpanningAcrossMedia = minimizeSpanningAcrossMedia;
        this.updateQueryParam("minimize_spanning_across_media", minimizeSpanningAcrossMedia);
        return this;
    }


    public PutBulkJobSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }


    public PutBulkJobSpectraS3Request withPreAllocateJobSpace(final boolean preAllocateJobSpace) {
        this.preAllocateJobSpace = preAllocateJobSpace;
        if (this.preAllocateJobSpace) {
            this.getQueryParams().put("pre_allocate_job_space", null);
        } else {
            this.getQueryParams().remove("pre_allocate_job_space");
        }
        return this;
    }


    @Override
    public PutBulkJobSpectraS3Request withPriority(final Priority priority) {
        super.withPriority(priority);
        return this;
    }


    public PutBulkJobSpectraS3Request withProtected(final boolean protectedFlag) {
        this.protectedFlag = protectedFlag;
        this.updateQueryParam("protected", protectedFlag);
        return this;
    }


    public PutBulkJobSpectraS3Request withVerifyAfterWrite(final boolean verifyAfterWrite) {
        this.verifyAfterWrite = verifyAfterWrite;
        this.updateQueryParam("verify_after_write", verifyAfterWrite);
        return this;
    }



    
    public boolean getAggregating() {
        return this.aggregating;
    }


    public boolean getDeadJobCleanupAllowed() {
        return this.deadJobCleanupAllowed;
    }


    public boolean getForce() {
        return this.force;
    }


    public boolean getIgnoreNamingConflicts() {
        return this.ignoreNamingConflicts;
    }


    public boolean getImplicitJobIdResolution() {
        return this.implicitJobIdResolution;
    }


    public boolean getMinimizeSpanningAcrossMedia() {
        return this.minimizeSpanningAcrossMedia;
    }


    public String getName() {
        return this.name;
    }


    public boolean getPreAllocateJobSpace() {
        return this.preAllocateJobSpace;
    }


    public boolean getProtected() {
        return this.protectedFlag;
    }


    public boolean getVerifyAfterWrite() {
        return this.verifyAfterWrite;
    }


    @Override
    public BulkCommand getCommand() {
        return BulkCommand.PUT;
    }
}