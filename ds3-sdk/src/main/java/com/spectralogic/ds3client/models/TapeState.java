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
package com.spectralogic.ds3client.models;

public enum TapeState {
    NORMAL,
    OFFLINE,
    ONLINE_PENDING,
    ONLINE_IN_PROGRESS,
    PENDING_INSPECTION,
    UNKNOWN,
    DATA_CHECKPOINT_FAILURE,
    DATA_CHECKPOINT_FAILURE_DUE_TO_READ_ONLY,
    DATA_CHECKPOINT_MISSING,
    LTFS_WITH_FOREIGN_DATA,
    FOREIGN,
    IMPORT_PENDING,
    IMPORT_IN_PROGRESS,
    INCOMPATIBLE,
    LOST,
    BAD,
    CANNOT_FORMAT_DUE_TO_WRITE_PROTECTION,
    SERIAL_NUMBER_MISMATCH,
    BAR_CODE_MISSING,
    FORMAT_PENDING,
    FORMAT_IN_PROGRESS,
    EJECT_TO_EE_IN_PROGRESS,
    EJECT_FROM_EE_PENDING,
    EJECTED
}