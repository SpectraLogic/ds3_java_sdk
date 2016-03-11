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
import com.spectralogic.ds3client.commands.AbstractRequest;
import com.spectralogic.ds3client.models.ChecksumType;
import java.lang.Long;
import com.spectralogic.ds3client.models.Priority;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.VersioningLevel;

public class ModifyDataPolicySpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String dataPolicy;

    private boolean blobbingEnabled;

    private ChecksumType.Type checksumType;

    private Long defaultBlobSize;

    private Priority defaultGetJobPriority;

    private Priority defaultPutJobPriority;

    private Priority defaultVerifyJobPriority;

    private boolean endToEndCrcRequired;

    private String name;

    private Priority rebuildPriority;

    private VersioningLevel versioning;

    // Constructor
    
    public ModifyDataPolicySpectraS3Request(final String dataPolicy) {
        this.dataPolicy = dataPolicy;
            }

    public ModifyDataPolicySpectraS3Request withBlobbingEnabled(final boolean blobbingEnabled) {
        this.blobbingEnabled = blobbingEnabled;
        this.updateQueryParam("blobbing_enabled", String.valueOf(blobbingEnabled));
        return this;
    }

    public ModifyDataPolicySpectraS3Request withChecksumType(final ChecksumType.Type checksumType) {
        this.checksumType = checksumType;
        this.updateQueryParam("checksum_type", checksumType.toString());
        return this;
    }

    public ModifyDataPolicySpectraS3Request withDefaultBlobSize(final Long defaultBlobSize) {
        this.defaultBlobSize = defaultBlobSize;
        this.updateQueryParam("default_blob_size", Long.toString(defaultBlobSize));
        return this;
    }

    public ModifyDataPolicySpectraS3Request withDefaultGetJobPriority(final Priority defaultGetJobPriority) {
        this.defaultGetJobPriority = defaultGetJobPriority;
        this.updateQueryParam("default_get_job_priority", defaultGetJobPriority.toString());
        return this;
    }

    public ModifyDataPolicySpectraS3Request withDefaultPutJobPriority(final Priority defaultPutJobPriority) {
        this.defaultPutJobPriority = defaultPutJobPriority;
        this.updateQueryParam("default_put_job_priority", defaultPutJobPriority.toString());
        return this;
    }

    public ModifyDataPolicySpectraS3Request withDefaultVerifyJobPriority(final Priority defaultVerifyJobPriority) {
        this.defaultVerifyJobPriority = defaultVerifyJobPriority;
        this.updateQueryParam("default_verify_job_priority", defaultVerifyJobPriority.toString());
        return this;
    }

    public ModifyDataPolicySpectraS3Request withEndToEndCrcRequired(final boolean endToEndCrcRequired) {
        this.endToEndCrcRequired = endToEndCrcRequired;
        this.updateQueryParam("end_to_end_crc_required", String.valueOf(endToEndCrcRequired));
        return this;
    }

    public ModifyDataPolicySpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", UrlEscapers.urlFragmentEscaper().escape(name).replace("+", "%2B"));
        return this;
    }

    public ModifyDataPolicySpectraS3Request withRebuildPriority(final Priority rebuildPriority) {
        this.rebuildPriority = rebuildPriority;
        this.updateQueryParam("rebuild_priority", rebuildPriority.toString());
        return this;
    }

    public ModifyDataPolicySpectraS3Request withVersioning(final VersioningLevel versioning) {
        this.versioning = versioning;
        this.updateQueryParam("versioning", versioning.toString());
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/data_policy/" + dataPolicy;
    }
    
    public String getDataPolicy() {
        return this.dataPolicy;
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