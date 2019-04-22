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
import com.google.common.net.UrlEscapers;
import com.spectralogic.ds3client.models.DataReplicationRuleType;

public class ModifyDs3DataReplicationRuleSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String ds3DataReplicationRule;

    private boolean replicateDeletes;

    private String targetDataPolicy;

    private DataReplicationRuleType type;

    // Constructor
    
    
    public ModifyDs3DataReplicationRuleSpectraS3Request(final String ds3DataReplicationRule) {
        this.ds3DataReplicationRule = ds3DataReplicationRule;
        
    }

    public ModifyDs3DataReplicationRuleSpectraS3Request withReplicateDeletes(final boolean replicateDeletes) {
        this.replicateDeletes = replicateDeletes;
        this.updateQueryParam("replicate_deletes", replicateDeletes);
        return this;
    }


    public ModifyDs3DataReplicationRuleSpectraS3Request withTargetDataPolicy(final String targetDataPolicy) {
        this.targetDataPolicy = targetDataPolicy;
        this.updateQueryParam("target_data_policy", targetDataPolicy);
        return this;
    }


    public ModifyDs3DataReplicationRuleSpectraS3Request withType(final DataReplicationRuleType type) {
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
        return "/_rest_/ds3_data_replication_rule/" + ds3DataReplicationRule;
    }
    
    public String getDs3DataReplicationRule() {
        return this.ds3DataReplicationRule;
    }


    public boolean getReplicateDeletes() {
        return this.replicateDeletes;
    }


    public String getTargetDataPolicy() {
        return this.targetDataPolicy;
    }


    public DataReplicationRuleType getType() {
        return this.type;
    }

}