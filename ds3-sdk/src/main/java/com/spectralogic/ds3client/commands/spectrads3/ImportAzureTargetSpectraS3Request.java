/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.ImportConflictResolutionMode;
import java.util.UUID;
import com.spectralogic.ds3client.models.Priority;

public class ImportAzureTargetSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String azureTarget;

    private final String cloudBucketName;

    private ImportConflictResolutionMode conflictResolutionMode;

    private String dataPolicyId;

    private Priority priority;

    private String userId;

    // Constructor
    
    
    public ImportAzureTargetSpectraS3Request(final String azureTarget, final String cloudBucketName) {
        this.azureTarget = azureTarget;
        this.cloudBucketName = cloudBucketName;
        
        this.getQueryParams().put("operation", "import");

        this.updateQueryParam("cloud_bucket_name", cloudBucketName);

    }

    public ImportAzureTargetSpectraS3Request withConflictResolutionMode(final ImportConflictResolutionMode conflictResolutionMode) {
        this.conflictResolutionMode = conflictResolutionMode;
        this.updateQueryParam("conflict_resolution_mode", conflictResolutionMode);
        return this;
    }


    public ImportAzureTargetSpectraS3Request withDataPolicyId(final UUID dataPolicyId) {
        this.dataPolicyId = dataPolicyId.toString();
        this.updateQueryParam("data_policy_id", dataPolicyId);
        return this;
    }


    public ImportAzureTargetSpectraS3Request withDataPolicyId(final String dataPolicyId) {
        this.dataPolicyId = dataPolicyId;
        this.updateQueryParam("data_policy_id", dataPolicyId);
        return this;
    }


    public ImportAzureTargetSpectraS3Request withPriority(final Priority priority) {
        this.priority = priority;
        this.updateQueryParam("priority", priority);
        return this;
    }


    public ImportAzureTargetSpectraS3Request withUserId(final UUID userId) {
        this.userId = userId.toString();
        this.updateQueryParam("user_id", userId);
        return this;
    }


    public ImportAzureTargetSpectraS3Request withUserId(final String userId) {
        this.userId = userId;
        this.updateQueryParam("user_id", userId);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/azure_target/" + azureTarget;
    }
    
    public String getAzureTarget() {
        return this.azureTarget;
    }


    public String getCloudBucketName() {
        return this.cloudBucketName;
    }


    public ImportConflictResolutionMode getConflictResolutionMode() {
        return this.conflictResolutionMode;
    }


    public String getDataPolicyId() {
        return this.dataPolicyId;
    }


    public Priority getPriority() {
        return this.priority;
    }


    public String getUserId() {
        return this.userId;
    }

}