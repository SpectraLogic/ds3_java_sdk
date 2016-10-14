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
import com.spectralogic.ds3client.models.ImportConflictResolutionMode;
import java.util.UUID;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.Priority;

public class ImportTapeSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String tapeId;

    private ImportConflictResolutionMode conflictResolutionMode;

    private String dataPolicyId;

    private Priority priority;

    private String storageDomainId;

    private String userId;

    private Priority verifyDataAfterImport;

    private boolean verifyDataPriorToImport;

    // Constructor
    
    
    public ImportTapeSpectraS3Request(final UUID tapeId) {
        this.tapeId = tapeId.toString();
        
        this.getQueryParams().put("operation", "import");

    }

    
    public ImportTapeSpectraS3Request(final String tapeId) {
        this.tapeId = tapeId;
        
        this.getQueryParams().put("operation", "import");

    }

    public ImportTapeSpectraS3Request withConflictResolutionMode(final ImportConflictResolutionMode conflictResolutionMode) {
        this.conflictResolutionMode = conflictResolutionMode;
        this.updateQueryParam("conflict_resolution_mode", conflictResolutionMode);
        return this;
    }


    public ImportTapeSpectraS3Request withDataPolicyId(final UUID dataPolicyId) {
        this.dataPolicyId = dataPolicyId.toString();
        this.updateQueryParam("data_policy_id", dataPolicyId);
        return this;
    }


    public ImportTapeSpectraS3Request withDataPolicyId(final String dataPolicyId) {
        this.dataPolicyId = dataPolicyId;
        this.updateQueryParam("data_policy_id", dataPolicyId);
        return this;
    }


    public ImportTapeSpectraS3Request withPriority(final Priority priority) {
        this.priority = priority;
        this.updateQueryParam("priority", priority);
        return this;
    }


    public ImportTapeSpectraS3Request withStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId.toString();
        this.updateQueryParam("storage_domain_id", storageDomainId);
        return this;
    }


    public ImportTapeSpectraS3Request withStorageDomainId(final String storageDomainId) {
        this.storageDomainId = storageDomainId;
        this.updateQueryParam("storage_domain_id", storageDomainId);
        return this;
    }


    public ImportTapeSpectraS3Request withUserId(final UUID userId) {
        this.userId = userId.toString();
        this.updateQueryParam("user_id", userId);
        return this;
    }


    public ImportTapeSpectraS3Request withUserId(final String userId) {
        this.userId = userId;
        this.updateQueryParam("user_id", userId);
        return this;
    }


    public ImportTapeSpectraS3Request withVerifyDataAfterImport(final Priority verifyDataAfterImport) {
        this.verifyDataAfterImport = verifyDataAfterImport;
        this.updateQueryParam("verify_data_after_import", verifyDataAfterImport);
        return this;
    }


    public ImportTapeSpectraS3Request withVerifyDataPriorToImport(final boolean verifyDataPriorToImport) {
        this.verifyDataPriorToImport = verifyDataPriorToImport;
        this.updateQueryParam("verify_data_prior_to_import", verifyDataPriorToImport);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/tape/" + tapeId;
    }
    
    public String getTapeId() {
        return this.tapeId;
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


    public String getStorageDomainId() {
        return this.storageDomainId;
    }


    public String getUserId() {
        return this.userId;
    }


    public Priority getVerifyDataAfterImport() {
        return this.verifyDataAfterImport;
    }


    public boolean getVerifyDataPriorToImport() {
        return this.verifyDataPriorToImport;
    }

}