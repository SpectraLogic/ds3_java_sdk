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
import com.google.common.net.UrlEscapers;

public class EjectStorageDomainSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final UUID storageDomainId;

    private String bucketId;

    private String ejectLabel;

    private String ejectLocation;

    // Constructor
    
    public EjectStorageDomainSpectraS3Request(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId;
        
        this.getQueryParams().put("operation", "eject");
        this.getQueryParams().put("storage_domain_id", storageDomainId.toString());
    }

    public EjectStorageDomainSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }

    public EjectStorageDomainSpectraS3Request withEjectLabel(final String ejectLabel) {
        this.ejectLabel = ejectLabel;
        this.updateQueryParam("eject_label", UrlEscapers.urlFragmentEscaper().escape(ejectLabel).replace('+', ' '));
        return this;
    }

    public EjectStorageDomainSpectraS3Request withEjectLocation(final String ejectLocation) {
        this.ejectLocation = ejectLocation;
        this.updateQueryParam("eject_location", UrlEscapers.urlFragmentEscaper().escape(ejectLocation).replace('+', ' '));
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/tape";
    }
    
    public UUID getStorageDomainId() {
        return this.storageDomainId;
    }


    public String getBucketId() {
        return this.bucketId;
    }


    public String getEjectLabel() {
        return this.ejectLabel;
    }


    public String getEjectLocation() {
        return this.ejectLocation;
    }

}