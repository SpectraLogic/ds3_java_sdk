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

import com.spectralogic.ds3client.commands.AbstractRequest;
import com.spectralogic.ds3client.HttpVerb;
import java.util.UUID;
import java.lang.String;

public class EjectStorageDomainBlobsSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final UUID tapeId;

    private final UUID bucketId;

    private final UUID storageDomainId;

    private String ejectLabel;
    private String ejectLocation;

    // Constructor
    public EjectStorageDomainBlobsSpectraS3Request(final UUID bucketId, final UUID storageDomainId, final UUID tapeId) {
        this.tapeId = tapeId;
        this.bucketId = bucketId;
        this.storageDomainId = storageDomainId;
        this.getQueryParams().put("operation", "eject");
        
        this.getQueryParams().put("blobs", null);

    }
    public EjectStorageDomainBlobsSpectraS3Request withEjectLabel(final String ejectLabel) {
        this.ejectLabel = ejectLabel;
        this.updateQueryParam("eject_label", ejectLabel);
        return this;
    }

    public EjectStorageDomainBlobsSpectraS3Request withEjectLocation(final String ejectLocation) {
        this.ejectLocation = ejectLocation;
        this.updateQueryParam("eject_location", ejectLocation);
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/tape/" + tapeId.toString();
    }
    
    public UUID getTapeId() {
        return this.tapeId;
    }


    public UUID getBucketId() {
        return this.bucketId;
    }


    public UUID getStorageDomainId() {
        return this.storageDomainId;
    }


    public String getEjectLabel() {
        return this.ejectLabel;
    }

    public String getEjectLocation() {
        return this.ejectLocation;
    }

}