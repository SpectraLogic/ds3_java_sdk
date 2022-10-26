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
package com.spectralogic.ds3client.models;

public enum TapeFailureType {
    BAR_CODE_CHANGED,
    BAR_CODE_DUPLICATE,
    BLOB_READ_FAILED,
    CLEANING_TAPE_EXPIRED,
    DATA_CHECKPOINT_FAILURE,
    DATA_CHECKPOINT_FAILURE_DUE_TO_READ_ONLY,
    DATA_CHECKPOINT_MISSING,
    DELAYED_OWNERSHIP_FAILURE,
    DRIVE_CLEAN_FAILED,
    DRIVE_CLEANED,
    DRIVE_TEST_FAILED,
    DRIVE_TEST_FAILED_ALL_WRITES_TOO_SLOW,
    DRIVE_TEST_FAILED_FORWARD_WRITES_TOO_SLOW,
    DRIVE_TEST_FAILED_REVERSE_WRITES_TOO_SLOW,
    DRIVE_TEST_SUCCEEDED,
    ENCRYPTION_ERROR,
    FORMAT_FAILED,
    GET_TAPE_INFORMATION_FAILED,
    HARDWARE_ERROR,
    IMPORT_FAILED,
    IMPORT_INCOMPLETE,
    IMPORT_FAILED_DUE_TO_TAKE_OWNERSHIP_FAILURE,
    IMPORT_FAILED_DUE_TO_DATA_INTEGRITY,
    INCOMPATIBLE,
    INSPECT_FAILED,
    QUIESCING_DRIVE,
    READ_FAILED,
    REIMPORT_REQUIRED,
    SERIAL_NUMBER_MISMATCH,
    VERIFY_FAILED,
    WRITE_FAILED
}