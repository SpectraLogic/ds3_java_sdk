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
package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.commands.interfaces.AbstractResponse;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.models.ChecksumType;

public class HeadObjectResponse extends AbstractResponse {

    public enum Status { EXISTS, DOESNTEXIST, UNKNOWN }
    
    private final Metadata metadata;

    private final long objectSize;

    private final Status status;

    public HeadObjectResponse(final Metadata metadata, final long objectSize, final Status status, final String checksum, final ChecksumType.Type checksumType) {
        super(checksum, checksumType);
        this.metadata = metadata;
        this.objectSize = objectSize;
        this.status = status;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public long getObjectSize() {
        return this.objectSize;
    }

    public Status getStatus() {
        return this.status;
    }

}