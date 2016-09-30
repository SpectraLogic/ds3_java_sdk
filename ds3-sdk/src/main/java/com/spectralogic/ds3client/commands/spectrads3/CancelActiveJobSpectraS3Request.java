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

public class CancelActiveJobSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String activeJobId;

    // Constructor
    
    
    public CancelActiveJobSpectraS3Request(final UUID activeJobId) {
        this.activeJobId = activeJobId.toString();
        
        this.getQueryParams().put("force", null);
    }

    
    public CancelActiveJobSpectraS3Request(final String activeJobId) {
        this.activeJobId = activeJobId;
        
        this.getQueryParams().put("force", null);
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.DELETE;
    }

    @Override
    public String getPath() {
        return "/_rest_/active_job/" + activeJobId;
    }
    
    public String getActiveJobId() {
        return this.activeJobId;
    }

}