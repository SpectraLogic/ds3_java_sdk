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

import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.commands.interfaces.AbstractResponse;

public class AllocateJobChunkSpectraS3Response extends AbstractResponse {

    public enum Status { ALLOCATED, RETRYLATER }
    
    private final Objects objectsResult;

    private final int retryAfterSeconds;

    private final Status status;

    public AllocateJobChunkSpectraS3Response(final Objects objectsResult, final int retryAfterSeconds, final Status status, final String checksum, final ChecksumType.Type checksumType) {
        super(checksum, checksumType);
        this.objectsResult = objectsResult;
        this.retryAfterSeconds = retryAfterSeconds;
        this.status = status;
    }

    public Objects getObjectsResult() {
        return this.objectsResult;
    }

    public int getRetryAfterSeconds() {
        return this.retryAfterSeconds;
    }

    public Status getStatus() {
        return this.status;
    }

}