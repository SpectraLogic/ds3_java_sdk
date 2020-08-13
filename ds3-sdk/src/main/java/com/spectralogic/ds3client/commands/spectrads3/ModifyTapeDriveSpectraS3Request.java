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
import com.spectralogic.ds3client.models.Priority;
import com.spectralogic.ds3client.models.Quiesced;
import com.spectralogic.ds3client.models.ReservedTaskType;
import java.util.UUID;
import com.google.common.net.UrlEscapers;

public class ModifyTapeDriveSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String tapeDriveId;

    private Priority minimumTaskPriority;

    private Quiesced quiesced;

    private ReservedTaskType reservedTaskType;

    // Constructor
    
    
    public ModifyTapeDriveSpectraS3Request(final UUID tapeDriveId) {
        this.tapeDriveId = tapeDriveId.toString();
        
    }

    
    public ModifyTapeDriveSpectraS3Request(final String tapeDriveId) {
        this.tapeDriveId = tapeDriveId;
        
    }

    public ModifyTapeDriveSpectraS3Request withMinimumTaskPriority(final Priority minimumTaskPriority) {
        this.minimumTaskPriority = minimumTaskPriority;
        this.updateQueryParam("minimum_task_priority", minimumTaskPriority);
        return this;
    }


    public ModifyTapeDriveSpectraS3Request withQuiesced(final Quiesced quiesced) {
        this.quiesced = quiesced;
        this.updateQueryParam("quiesced", quiesced);
        return this;
    }


    public ModifyTapeDriveSpectraS3Request withReservedTaskType(final ReservedTaskType reservedTaskType) {
        this.reservedTaskType = reservedTaskType;
        this.updateQueryParam("reserved_task_type", reservedTaskType);
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


    public Priority getMinimumTaskPriority() {
        return this.minimumTaskPriority;
    }


    public Quiesced getQuiesced() {
        return this.quiesced;
    }


    public ReservedTaskType getReservedTaskType() {
        return this.reservedTaskType;
    }

}