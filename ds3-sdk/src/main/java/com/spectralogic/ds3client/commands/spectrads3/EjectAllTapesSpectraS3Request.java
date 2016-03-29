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
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.google.common.net.UrlEscapers;

public class EjectAllTapesSpectraS3Request extends AbstractRequest {

    // Variables
    
    private String ejectLabel;

    private String ejectLocation;

    // Constructor
    
    public EjectAllTapesSpectraS3Request() {
        
        this.getQueryParams().put("operation", "eject");

    }

    public EjectAllTapesSpectraS3Request withEjectLabel(final String ejectLabel) {
        this.ejectLabel = ejectLabel;
        this.updateQueryParam("eject_label", ejectLabel);
        return this;
    }

    public EjectAllTapesSpectraS3Request withEjectLocation(final String ejectLocation) {
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
        return "/_rest_/tape";
    }
    
    public String getEjectLabel() {
        return this.ejectLabel;
    }


    public String getEjectLocation() {
        return this.ejectLocation;
    }

}