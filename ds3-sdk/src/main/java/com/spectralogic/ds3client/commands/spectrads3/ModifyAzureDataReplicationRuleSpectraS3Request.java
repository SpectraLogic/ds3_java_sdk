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
import com.spectralogic.ds3client.models.DataReplicationRuleType;
import com.google.common.net.UrlEscapers;

public class ModifyAzureDataReplicationRuleSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String azureDataReplicationRule;

    private long maxBlobPartSizeInBytes;

    private DataReplicationRuleType type;

    // Constructor
    
    
    public ModifyAzureDataReplicationRuleSpectraS3Request(final String azureDataReplicationRule) {
        this.azureDataReplicationRule = azureDataReplicationRule;
        
    }

    public ModifyAzureDataReplicationRuleSpectraS3Request withMaxBlobPartSizeInBytes(final long maxBlobPartSizeInBytes) {
        this.maxBlobPartSizeInBytes = maxBlobPartSizeInBytes;
        this.updateQueryParam("max_blob_part_size_in_bytes", maxBlobPartSizeInBytes);
        return this;
    }


    public ModifyAzureDataReplicationRuleSpectraS3Request withType(final DataReplicationRuleType type) {
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
        return "/_rest_/azure_data_replication_rule/" + azureDataReplicationRule;
    }
    
    public String getAzureDataReplicationRule() {
        return this.azureDataReplicationRule;
    }


    public long getMaxBlobPartSizeInBytes() {
        return this.maxBlobPartSizeInBytes;
    }


    public DataReplicationRuleType getType() {
        return this.type;
    }

}