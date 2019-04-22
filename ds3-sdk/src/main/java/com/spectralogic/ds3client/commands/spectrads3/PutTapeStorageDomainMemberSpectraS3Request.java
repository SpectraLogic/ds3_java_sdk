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
import com.google.common.net.UrlEscapers;
import java.lang.Integer;
import com.spectralogic.ds3client.models.WritePreferenceLevel;

public class PutTapeStorageDomainMemberSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String storageDomainId;

    private final String tapePartitionId;

    private final String tapeType;

    private Integer autoCompactionThreshold;

    private WritePreferenceLevel writePreference;

    // Constructor
    
    
    public PutTapeStorageDomainMemberSpectraS3Request(final UUID storageDomainId, final UUID tapePartitionId, final String tapeType) {
        this.storageDomainId = storageDomainId.toString();
        this.tapePartitionId = tapePartitionId.toString();
        this.tapeType = tapeType;
        
        this.updateQueryParam("storage_domain_id", storageDomainId);

        this.updateQueryParam("tape_partition_id", tapePartitionId);

        this.updateQueryParam("tape_type", tapeType);

    }

    
    public PutTapeStorageDomainMemberSpectraS3Request(final String storageDomainId, final String tapePartitionId, final String tapeType) {
        this.storageDomainId = storageDomainId;
        this.tapePartitionId = tapePartitionId;
        this.tapeType = tapeType;
        
        this.updateQueryParam("storage_domain_id", storageDomainId);

        this.updateQueryParam("tape_partition_id", tapePartitionId);

        this.updateQueryParam("tape_type", tapeType);

    }

    public PutTapeStorageDomainMemberSpectraS3Request withAutoCompactionThreshold(final Integer autoCompactionThreshold) {
        this.autoCompactionThreshold = autoCompactionThreshold;
        this.updateQueryParam("auto_compaction_threshold", autoCompactionThreshold);
        return this;
    }


    public PutTapeStorageDomainMemberSpectraS3Request withWritePreference(final WritePreferenceLevel writePreference) {
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
    
    public String getStorageDomainId() {
        return this.storageDomainId;
    }


    public String getTapePartitionId() {
        return this.tapePartitionId;
    }


    public String getTapeType() {
        return this.tapeType;
    }


    public Integer getAutoCompactionThreshold() {
        return this.autoCompactionThreshold;
    }


    public WritePreferenceLevel getWritePreference() {
        return this.writePreference;
    }

}