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

public enum TapePartitionFailureType {
    CLEANING_TAPE_REQUIRED,
    DUPLICATE_TAPE_BAR_CODES_DETECTED,
    EJECT_STALLED_DUE_TO_OFFLINE_TAPES,
    MINIMUM_DRIVE_COUNT_NOT_MET,
    MOVE_FAILED,
    MOVE_FAILED_DUE_TO_PREPARE_TAPE_FOR_REMOVAL_FAILURE,
    NO_USABLE_DRIVES,
    ONLINE_STALLED_DUE_TO_NO_STORAGE_SLOTS,
    TAPE_DRIVE_IN_ERROR,
    TAPE_DRIVE_MISSING,
    TAPE_DRIVE_QUIESCED,
    TAPE_DRIVE_TYPE_MISMATCH,
    TAPE_EJECTION_BY_OPERATOR_REQUIRED,
    TAPE_MEDIA_TYPE_INCOMPATIBLE,
    TAPE_REMOVAL_UNEXPECTED,
    TAPE_IN_INVALID_PARTITION
}