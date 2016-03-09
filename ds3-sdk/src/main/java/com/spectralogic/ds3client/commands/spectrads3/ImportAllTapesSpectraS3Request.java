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
import com.spectralogic.ds3client.models.ImportConflictResolutionMode;
import java.util.UUID;

public class ImportAllTapesSpectraS3Request extends AbstractRequest {

    // Variables
    
    private ImportConflictResolutionMode conflictResolutionMode;

    private UUID dataPolicyId;

    private UUID storageDomainId;

    private UUID userId;

    // Constructor
    
    public ImportAllTapesSpectraS3Request() {
        
        this.getQueryParams().put("operation", "import");
    }

    public ImportAllTapesSpectraS3Request withConflictResolutionMode(final ImportConflictResolutionMode conflictResolutionMode) {
        this.conflictResolutionMode = conflictResolutionMode;
        this.updateQueryParam("conflict_resolution_mode", conflictResolutionMode.toString());
        return this;
    }

    public ImportAllTapesSpectraS3Request withDataPolicyId(final UUID dataPolicyId) {
        this.dataPolicyId = dataPolicyId;
        this.updateQueryParam("data_policy_id", dataPolicyId.toString());
        return this;
    }

    public ImportAllTapesSpectraS3Request withStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId;
        this.updateQueryParam("storage_domain_id", storageDomainId.toString());
        return this;
    }

    public ImportAllTapesSpectraS3Request withUserId(final UUID userId) {
        this.userId = userId;
        this.updateQueryParam("user_id", userId.toString());
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/tape";
    }
    
    public ImportConflictResolutionMode getConflictResolutionMode() {
        return this.conflictResolutionMode;
    }


    public UUID getDataPolicyId() {
        return this.dataPolicyId;
    }


    public UUID getStorageDomainId() {
        return this.storageDomainId;
    }


    public UUID getUserId() {
        return this.userId;
    }

}