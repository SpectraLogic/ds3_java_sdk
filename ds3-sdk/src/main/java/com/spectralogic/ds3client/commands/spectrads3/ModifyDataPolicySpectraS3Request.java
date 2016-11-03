/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.spectralogic.ds3client.models.ChecksumType;
import java.lang.Long;
import com.spectralogic.ds3client.models.Priority;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.VersioningLevel;
import java.util.UUID;

public class ModifyDataPolicySpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String dataPolicyId;

    private boolean alwaysForcePutJobCreation;

    private boolean alwaysMinimizeSpanningAcrossMedia;

    private boolean alwaysReplicateDeletes;

    private boolean blobbingEnabled;

    private ChecksumType.Type checksumType;

    private Long defaultBlobSize;

    private Priority defaultGetJobPriority;

    private Priority defaultPutJobPriority;

    private boolean defaultVerifyAfterWrite;

    private Priority defaultVerifyJobPriority;

    private boolean endToEndCrcRequired;

    private String name;

    private Priority rebuildPriority;

    private VersioningLevel versioning;

    // Constructor
    
    
    public ModifyDataPolicySpectraS3Request(final UUID dataPolicyId) {
        this.dataPolicyId = dataPolicyId.toString();
        
    }

    
    public ModifyDataPolicySpectraS3Request(final String dataPolicyId) {
        this.dataPolicyId = dataPolicyId;
        
    }

    public ModifyDataPolicySpectraS3Request withAlwaysForcePutJobCreation(final boolean alwaysForcePutJobCreation) {
        this.alwaysForcePutJobCreation = alwaysForcePutJobCreation;
        this.updateQueryParam("always_force_put_job_creation", alwaysForcePutJobCreation);
        return this;
    }


    public ModifyDataPolicySpectraS3Request withAlwaysMinimizeSpanningAcrossMedia(final boolean alwaysMinimizeSpanningAcrossMedia) {
        this.alwaysMinimizeSpanningAcrossMedia = alwaysMinimizeSpanningAcrossMedia;
        this.updateQueryParam("always_minimize_spanning_across_media", alwaysMinimizeSpanningAcrossMedia);
        return this;
    }


    public ModifyDataPolicySpectraS3Request withAlwaysReplicateDeletes(final boolean alwaysReplicateDeletes) {
        this.alwaysReplicateDeletes = alwaysReplicateDeletes;
        this.updateQueryParam("always_replicate_deletes", alwaysReplicateDeletes);
        return this;
    }


    public ModifyDataPolicySpectraS3Request withBlobbingEnabled(final boolean blobbingEnabled) {
        this.blobbingEnabled = blobbingEnabled;
        this.updateQueryParam("blobbing_enabled", blobbingEnabled);
        return this;
    }


    public ModifyDataPolicySpectraS3Request withChecksumType(final ChecksumType.Type checksumType) {
        this.checksumType = checksumType;
        this.updateQueryParam("checksum_type", checksumType);
        return this;
    }


    public ModifyDataPolicySpectraS3Request withDefaultBlobSize(final Long defaultBlobSize) {
        this.defaultBlobSize = defaultBlobSize;
        this.updateQueryParam("default_blob_size", defaultBlobSize);
        return this;
    }


    public ModifyDataPolicySpectraS3Request withDefaultGetJobPriority(final Priority defaultGetJobPriority) {
        this.defaultGetJobPriority = defaultGetJobPriority;
        this.updateQueryParam("default_get_job_priority", defaultGetJobPriority);
        return this;
    }


    public ModifyDataPolicySpectraS3Request withDefaultPutJobPriority(final Priority defaultPutJobPriority) {
        this.defaultPutJobPriority = defaultPutJobPriority;
        this.updateQueryParam("default_put_job_priority", defaultPutJobPriority);
        return this;
    }


    public ModifyDataPolicySpectraS3Request withDefaultVerifyAfterWrite(final boolean defaultVerifyAfterWrite) {
        this.defaultVerifyAfterWrite = defaultVerifyAfterWrite;
        this.updateQueryParam("default_verify_after_write", defaultVerifyAfterWrite);
        return this;
    }


    public ModifyDataPolicySpectraS3Request withDefaultVerifyJobPriority(final Priority defaultVerifyJobPriority) {
        this.defaultVerifyJobPriority = defaultVerifyJobPriority;
        this.updateQueryParam("default_verify_job_priority", defaultVerifyJobPriority);
        return this;
    }


    public ModifyDataPolicySpectraS3Request withEndToEndCrcRequired(final boolean endToEndCrcRequired) {
        this.endToEndCrcRequired = endToEndCrcRequired;
        this.updateQueryParam("end_to_end_crc_required", endToEndCrcRequired);
        return this;
    }


    public ModifyDataPolicySpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }


    public ModifyDataPolicySpectraS3Request withRebuildPriority(final Priority rebuildPriority) {
        this.rebuildPriority = rebuildPriority;
        this.updateQueryParam("rebuild_priority", rebuildPriority);
        return this;
    }


    public ModifyDataPolicySpectraS3Request withVersioning(final VersioningLevel versioning) {
        this.versioning = versioning;
        this.updateQueryParam("versioning", versioning);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/data_policy/" + dataPolicyId;
    }
    
    public String getDataPolicyId() {
        return this.dataPolicyId;
    }


    public boolean getAlwaysForcePutJobCreation() {
        return this.alwaysForcePutJobCreation;
    }


    public boolean getAlwaysMinimizeSpanningAcrossMedia() {
        return this.alwaysMinimizeSpanningAcrossMedia;
    }


    public boolean getAlwaysReplicateDeletes() {
        return this.alwaysReplicateDeletes;
    }


    public boolean getBlobbingEnabled() {
        return this.blobbingEnabled;
    }


    public ChecksumType.Type getChecksumType() {
        return this.checksumType;
    }


    public Long getDefaultBlobSize() {
        return this.defaultBlobSize;
    }


    public Priority getDefaultGetJobPriority() {
        return this.defaultGetJobPriority;
    }


    public Priority getDefaultPutJobPriority() {
        return this.defaultPutJobPriority;
    }


    public boolean getDefaultVerifyAfterWrite() {
        return this.defaultVerifyAfterWrite;
    }


    public Priority getDefaultVerifyJobPriority() {
        return this.defaultVerifyJobPriority;
    }


    public boolean getEndToEndCrcRequired() {
        return this.endToEndCrcRequired;
    }


    public String getName() {
        return this.name;
    }


    public Priority getRebuildPriority() {
        return this.rebuildPriority;
    }


    public VersioningLevel getVersioning() {
        return this.versioning;
    }

}