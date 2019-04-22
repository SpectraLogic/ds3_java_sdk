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

package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.ChecksumType;

/**
 * The event emitted when we have computed the checksum for a blob to be transferred to a Black Pearl.
 */
public class ChecksumEvent {
    private final BulkObject blob;
    private final ChecksumType.Type checksumType;
    private final String checksum;

    public ChecksumEvent(final BulkObject blob, final ChecksumType.Type checksumType, final String checksum) {
        this.blob = blob;
        this.checksumType = checksumType;
        this.checksum = checksum;
    }

    public BulkObject getBlob() {
        return blob;
    }

    public ChecksumType.Type getChecksumType() {
        return checksumType;
    }

    public String getChecksum() {
        return checksum;
    }
}
