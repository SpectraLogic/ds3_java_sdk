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
import com.spectralogic.ds3client.models.TapeDriveType;
import java.util.UUID;

public class PutTapeDensityDirectiveSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final TapeDriveType density;

    private final String partitionId;

    private final String tapeType;

    // Constructor
    
    
    public PutTapeDensityDirectiveSpectraS3Request(final TapeDriveType density, final UUID partitionId, final String tapeType) {
        this.density = density;
        this.partitionId = partitionId.toString();
        this.tapeType = tapeType;
        
        this.updateQueryParam("density", density);

        this.updateQueryParam("partition_id", partitionId);

        this.updateQueryParam("tape_type", tapeType);

    }

    
    public PutTapeDensityDirectiveSpectraS3Request(final TapeDriveType density, final String partitionId, final String tapeType) {
        this.density = density;
        this.partitionId = partitionId;
        this.tapeType = tapeType;
        
        this.updateQueryParam("density", density);

        this.updateQueryParam("partition_id", partitionId);

        this.updateQueryParam("tape_type", tapeType);

    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/tape_density_directive";
    }
    
    public TapeDriveType getDensity() {
        return this.density;
    }


    public String getPartitionId() {
        return this.partitionId;
    }


    public String getTapeType() {
        return this.tapeType;
    }

}