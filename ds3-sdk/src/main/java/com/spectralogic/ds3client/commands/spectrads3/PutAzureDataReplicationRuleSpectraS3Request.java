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

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import java.util.UUID;
import com.spectralogic.ds3client.models.DataReplicationRuleType;

public class PutAzureDataReplicationRuleSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String dataPolicyId;

    private final String targetId;

    private final DataReplicationRuleType type;

    private long maxBlobPartSizeInBytes;

    private boolean replicateDeletes;

    // Constructor
    
    
    public PutAzureDataReplicationRuleSpectraS3Request(final UUID dataPolicyId, final UUID targetId, final DataReplicationRuleType type) {
        this.dataPolicyId = dataPolicyId.toString();
        this.targetId = targetId.toString();
        this.type = type;
        
        this.updateQueryParam("data_policy_id", dataPolicyId);

        this.updateQueryParam("target_id", targetId);

        this.updateQueryParam("type", type);

    }

    
    public PutAzureDataReplicationRuleSpectraS3Request(final String dataPolicyId, final String targetId, final DataReplicationRuleType type) {
        this.dataPolicyId = dataPolicyId;
        this.targetId = targetId;
        this.type = type;
        
        this.updateQueryParam("data_policy_id", dataPolicyId);

        this.updateQueryParam("target_id", targetId);

        this.updateQueryParam("type", type);

    }

    public PutAzureDataReplicationRuleSpectraS3Request withMaxBlobPartSizeInBytes(final long maxBlobPartSizeInBytes) {
        this.maxBlobPartSizeInBytes = maxBlobPartSizeInBytes;
        this.updateQueryParam("max_blob_part_size_in_bytes", maxBlobPartSizeInBytes);
        return this;
    }


    public PutAzureDataReplicationRuleSpectraS3Request withReplicateDeletes(final boolean replicateDeletes) {
        this.replicateDeletes = replicateDeletes;
        this.updateQueryParam("replicate_deletes", replicateDeletes);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/azure_data_replication_rule";
    }
    
    public String getDataPolicyId() {
        return this.dataPolicyId;
    }


    public String getTargetId() {
        return this.targetId;
    }


    public DataReplicationRuleType getType() {
        return this.type;
    }


    public long getMaxBlobPartSizeInBytes() {
        return this.maxBlobPartSizeInBytes;
    }


    public boolean getReplicateDeletes() {
        return this.replicateDeletes;
    }

}