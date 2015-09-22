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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.UUID;

public class TapeDrive {
    @JsonProperty("ErrorMessage")
    private String errorMessage;
    @JsonProperty("ForceTapeRemoval")
    private boolean forceTapeRemoval;
    @JsonProperty("Id")
    private UUID id;
    @JsonProperty("PartitionId")
    private UUID partitionId;
    @JsonProperty("SerialNumber")
    private String serialNumber;
    @JsonProperty("State")
    private State state;
    @JsonProperty("TapeId")
    private UUID tapeId;
    @JsonProperty("Type")
    private Type type;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isForceTapeRemoval() {
        return forceTapeRemoval;
    }

    public void setForceTapeRemoval(boolean forceTapeRemoval) {
        this.forceTapeRemoval = forceTapeRemoval;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(UUID partitionId) {
        this.partitionId = partitionId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public enum State {
        NORMAL, OFFLINE, ONLINE_PENDING, ONLINE_IN_PROGRESS, PENDING_INSPECTION, UNKNOWN, DATA_CHECKPOINT_FAILURE,
        DATA_CHECKPOINT_MISSING, LTFS_WITH_FOREIGN_DATA, FOREIGN, IMPORT_PENDING, IMPORT_IN_PROGRESS, LOST, BAD,
        SERIAL_NUMBER_MISMATCH, BAR_CODE_MISSING, FORMAT_PENDING, FORMAT_IN_PROGRESS, EJECT_TO_EE_IN_PROGRESS,
        EJECT_FROM_EE_PENDING, EJECTED
    }

    public enum Type {
        UNKNOWN, LTO5, LTO6, LTO_CLEANING_TAPE, TS_JC, TS_JY, TS_JK, TS_JD, TS_JZ, TS_JL, TS_CLEANING_TAPE, FORBIDDEN
    }
}
