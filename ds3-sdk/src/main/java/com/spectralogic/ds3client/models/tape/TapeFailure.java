/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.models.tape;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class TapeFailure {
    @JsonProperty("Date")
    private String date;
    @JsonProperty("ErrorMessage")
    private String errorMessage;
    @JsonProperty("Id")
    private UUID id;
    @JsonProperty("TapeDriveId")
    private UUID tapeDriveId;
    @JsonProperty("TapeId")
    private UUID tapeId;
    @JsonProperty("Type")
    private Type type;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTapeDriveId() {
        return tapeDriveId;
    }

    public void setTapeDriveId(UUID tapeDriveId) {
        this.tapeDriveId = tapeDriveId;
    }

    public UUID getTapeId() {
        return tapeId;
    }

    public void setTapeId(UUID tapeId) {
        this.tapeId = tapeId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        BLOB_READ_FAILED, CLEAN_DRIVE_FAILED, DATA_CHECKPOINT_FAILURE, DATA_CHECKPOINT_MISSING, FORMAT_FAILED,
        GET_TAPE_INFORMATION_FAILED, IMPORT_FAILED, IMPORT_FAILED_DUE_TO_TAKE_OWNERSHIP_FAILURE, IMPORT_FAILED_DUE_TO_DATA_INTEGRITY,
        INSPECT_FAILED, READ_FAILED, VERIFY_FAILED, WRITE_FAILED
    }
}
