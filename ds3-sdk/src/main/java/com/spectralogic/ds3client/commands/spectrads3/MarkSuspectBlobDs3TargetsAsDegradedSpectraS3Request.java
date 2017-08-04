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
import com.spectralogic.ds3client.commands.interfaces.AbstractIdsPayloadRequest;

import java.util.List;

public class MarkSuspectBlobDs3TargetsAsDegradedSpectraS3Request extends AbstractIdsPayloadRequest {

    // Variables
    
    private boolean force;

    // Constructor
    
    public MarkSuspectBlobDs3TargetsAsDegradedSpectraS3Request(final List<String> ids) {
        super(ids);
        
    }

    public MarkSuspectBlobDs3TargetsAsDegradedSpectraS3Request withForce(final boolean force) {
        this.force = force;
        if (this.force) {
            this.getQueryParams().put("force", null);
        } else {
            this.getQueryParams().remove("force");
        }
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/suspect_blob_ds3_target";
    }
    
    public boolean getForce() {
        return this.force;
    }

}