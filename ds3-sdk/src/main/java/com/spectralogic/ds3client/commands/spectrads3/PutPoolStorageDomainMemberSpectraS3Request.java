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
import com.spectralogic.ds3client.models.WritePreferenceLevel;

public class PutPoolStorageDomainMemberSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String poolPartitionId;

    private final String storageDomainId;

    private WritePreferenceLevel writePreference;

    // Constructor
    
    
    public PutPoolStorageDomainMemberSpectraS3Request(final UUID poolPartitionId, final UUID storageDomainId) {
        this.poolPartitionId = poolPartitionId.toString();
        this.storageDomainId = storageDomainId.toString();
        
        this.getQueryParams().put("pool_partition_id", poolPartitionId.toString());
        this.getQueryParams().put("storage_domain_id", storageDomainId.toString());
    }

    
    public PutPoolStorageDomainMemberSpectraS3Request(final String poolPartitionId, final String storageDomainId) {
        this.poolPartitionId = poolPartitionId;
        this.storageDomainId = storageDomainId;
        
        this.getQueryParams().put("pool_partition_id", UrlEscapers.urlFragmentEscaper().escape(poolPartitionId).replace("+", "%2B"));
        this.getQueryParams().put("storage_domain_id", UrlEscapers.urlFragmentEscaper().escape(storageDomainId).replace("+", "%2B"));
    }

    public PutPoolStorageDomainMemberSpectraS3Request withWritePreference(final WritePreferenceLevel writePreference) {
        this.writePreference = writePreference;
        this.updateQueryParam("write_preference", writePreference);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/storage_domain_member";
    }
    
    public String getPoolPartitionId() {
        return this.poolPartitionId;
    }


    public String getStorageDomainId() {
        return this.storageDomainId;
    }


    public WritePreferenceLevel getWritePreference() {
        return this.writePreference;
    }

}