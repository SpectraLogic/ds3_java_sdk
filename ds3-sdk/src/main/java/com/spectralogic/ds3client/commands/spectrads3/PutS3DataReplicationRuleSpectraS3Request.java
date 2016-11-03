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
import java.util.UUID;
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.DataReplicationRuleType;
import com.spectralogic.ds3client.models.S3InitialDataPlacementPolicy;

public class PutS3DataReplicationRuleSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String dataPolicyId;

    private final String targetId;

    private final DataReplicationRuleType type;

    private S3InitialDataPlacementPolicy initialDataPlacement;

    private long maxBlobPartSizeInBytes;

    // Constructor
    
    
    public PutS3DataReplicationRuleSpectraS3Request(final UUID dataPolicyId, final UUID targetId, final DataReplicationRuleType type) {
        this.dataPolicyId = dataPolicyId.toString();
        this.targetId = targetId.toString();
        this.type = type;
        
        this.getQueryParams().put("data_policy_id", dataPolicyId.toString());
        this.getQueryParams().put("target_id", targetId.toString());
        this.getQueryParams().put("type", type.toString());
    }

    
    public PutS3DataReplicationRuleSpectraS3Request(final String dataPolicyId, final String targetId, final DataReplicationRuleType type) {
        this.dataPolicyId = dataPolicyId;
        this.targetId = targetId;
        this.type = type;
        
        this.getQueryParams().put("data_policy_id", UrlEscapers.urlFragmentEscaper().escape(dataPolicyId).replace("+", "%2B"));
        this.getQueryParams().put("target_id", UrlEscapers.urlFragmentEscaper().escape(targetId).replace("+", "%2B"));
        this.getQueryParams().put("type", type.toString());
    }

    public PutS3DataReplicationRuleSpectraS3Request withInitialDataPlacement(final S3InitialDataPlacementPolicy initialDataPlacement) {
        this.initialDataPlacement = initialDataPlacement;
        this.updateQueryParam("initial_data_placement", initialDataPlacement);
        return this;
    }


    public PutS3DataReplicationRuleSpectraS3Request withMaxBlobPartSizeInBytes(final long maxBlobPartSizeInBytes) {
        this.maxBlobPartSizeInBytes = maxBlobPartSizeInBytes;
        this.updateQueryParam("max_blob_part_size_in_bytes", maxBlobPartSizeInBytes);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/s3_data_replication_rule";
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


    public S3InitialDataPlacementPolicy getInitialDataPlacement() {
        return this.initialDataPlacement;
    }


    public long getMaxBlobPartSizeInBytes() {
        return this.maxBlobPartSizeInBytes;
    }

}