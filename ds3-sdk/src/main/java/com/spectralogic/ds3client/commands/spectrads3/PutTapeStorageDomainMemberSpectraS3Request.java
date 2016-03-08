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
import java.util.UUID;
import com.spectralogic.ds3client.models.TapeType;
import com.spectralogic.ds3client.models.WritePreferenceLevel;

public class PutTapeStorageDomainMemberSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final UUID storageDomainId;

    private final UUID tapePartitionId;

    private final TapeType tapeType;

    private WritePreferenceLevel writePreference;

    // Constructor
    
    public PutTapeStorageDomainMemberSpectraS3Request(final UUID storageDomainId, final UUID tapePartitionId, final TapeType tapeType) {
        this.storageDomainId = storageDomainId;
        this.tapePartitionId = tapePartitionId;
        this.tapeType = tapeType;
                this.getQueryParams().put("storage_domain_id", storageDomainId.toString());
        this.getQueryParams().put("tape_partition_id", tapePartitionId.toString());
        this.getQueryParams().put("tape_type", tapeType.toString());
    }

    public PutTapeStorageDomainMemberSpectraS3Request withWritePreference(final WritePreferenceLevel writePreference) {
        this.writePreference = writePreference;
        this.updateQueryParam("write_preference", writePreference.toString());
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
    
    public UUID getStorageDomainId() {
        return this.storageDomainId;
    }


    public UUID getTapePartitionId() {
        return this.tapePartitionId;
    }


    public TapeType getTapeType() {
        return this.tapeType;
    }


    public WritePreferenceLevel getWritePreference() {
        return this.writePreference;
    }

}