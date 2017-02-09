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
import com.spectralogic.ds3client.models.S3InitialDataPlacementPolicy;
import com.spectralogic.ds3client.models.DataReplicationRuleType;
import com.google.common.net.UrlEscapers;

public class ModifyS3DataReplicationRuleSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String s3DataReplicationRule;

    private S3InitialDataPlacementPolicy initialDataPlacement;

    private long maxBlobPartSizeInBytes;

    private boolean replicateDeletes;

    private DataReplicationRuleType type;

    // Constructor
    
    
    public ModifyS3DataReplicationRuleSpectraS3Request(final String s3DataReplicationRule) {
        this.s3DataReplicationRule = s3DataReplicationRule;
        
    }

    public ModifyS3DataReplicationRuleSpectraS3Request withInitialDataPlacement(final S3InitialDataPlacementPolicy initialDataPlacement) {
        this.initialDataPlacement = initialDataPlacement;
        this.updateQueryParam("initial_data_placement", initialDataPlacement);
        return this;
    }


    public ModifyS3DataReplicationRuleSpectraS3Request withMaxBlobPartSizeInBytes(final long maxBlobPartSizeInBytes) {
        this.maxBlobPartSizeInBytes = maxBlobPartSizeInBytes;
        this.updateQueryParam("max_blob_part_size_in_bytes", maxBlobPartSizeInBytes);
        return this;
    }


    public ModifyS3DataReplicationRuleSpectraS3Request withReplicateDeletes(final boolean replicateDeletes) {
        this.replicateDeletes = replicateDeletes;
        this.updateQueryParam("replicate_deletes", replicateDeletes);
        return this;
    }


    public ModifyS3DataReplicationRuleSpectraS3Request withType(final DataReplicationRuleType type) {
        this.type = type;
        this.updateQueryParam("type", type);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/s3_data_replication_rule/" + s3DataReplicationRule;
    }
    
    public String getS3DataReplicationRule() {
        return this.s3DataReplicationRule;
    }


    public S3InitialDataPlacementPolicy getInitialDataPlacement() {
        return this.initialDataPlacement;
    }


    public long getMaxBlobPartSizeInBytes() {
        return this.maxBlobPartSizeInBytes;
    }


    public boolean getReplicateDeletes() {
        return this.replicateDeletes;
    }


    public DataReplicationRuleType getType() {
        return this.type;
    }

}