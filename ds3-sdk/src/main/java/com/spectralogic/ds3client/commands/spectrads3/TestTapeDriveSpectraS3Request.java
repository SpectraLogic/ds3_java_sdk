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

public class TestTapeDriveSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String tapeDriveId;

    private boolean skipClean;

    private String tapeId;

    // Constructor
    
    
    public TestTapeDriveSpectraS3Request(final UUID tapeDriveId) {
        this.tapeDriveId = tapeDriveId.toString();
        
        this.getQueryParams().put("operation", "test");

    }

    
    public TestTapeDriveSpectraS3Request(final String tapeDriveId) {
        this.tapeDriveId = tapeDriveId;
        
        this.getQueryParams().put("operation", "test");

    }

    public TestTapeDriveSpectraS3Request withSkipClean(final boolean skipClean) {
        this.skipClean = skipClean;
        if (this.skipClean) {
            this.getQueryParams().put("skip_clean", null);
        } else {
            this.getQueryParams().remove("skip_clean");
        }
        return this;
    }


    public TestTapeDriveSpectraS3Request withTapeId(final UUID tapeId) {
        this.tapeId = tapeId.toString();
        this.updateQueryParam("tape_id", tapeId);
        return this;
    }


    public TestTapeDriveSpectraS3Request withTapeId(final String tapeId) {
        this.tapeId = tapeId;
        this.updateQueryParam("tape_id", tapeId);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/tape_drive/" + tapeDriveId;
    }
    
    public String getTapeDriveId() {
        return this.tapeDriveId;
    }


    public boolean getSkipClean() {
        return this.skipClean;
    }


    public String getTapeId() {
        return this.tapeId;
    }

}