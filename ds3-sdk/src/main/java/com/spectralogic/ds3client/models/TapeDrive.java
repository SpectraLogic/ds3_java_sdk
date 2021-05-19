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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.UUID;
import java.util.Date;

@JacksonXmlRootElement(namespace = "Data")
public class TapeDrive {

    // Variables
    @JsonProperty("CleaningRequired")
    private boolean cleaningRequired;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    @JsonProperty("ForceTapeRemoval")
    private boolean forceTapeRemoval;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("LastCleaned")
    private Date lastCleaned;

    @JsonProperty("MaxFailedTapes")
    private Integer maxFailedTapes;

    @JsonProperty("MfgSerialNumber")
    private String mfgSerialNumber;

    @JsonProperty("MinimumTaskPriority")
    private Priority minimumTaskPriority;

    @JsonProperty("PartitionId")
    private UUID partitionId;

    @JsonProperty("Quiesced")
    private Quiesced quiesced;

    @JsonProperty("ReservedTaskType")
    private ReservedTaskType reservedTaskType;

    @JsonProperty("SerialNumber")
    private String serialNumber;

    @JsonProperty("State")
    private TapeDriveState state;

    @JsonProperty("TapeId")
    private UUID tapeId;

    @JsonProperty("Type")
    private TapeDriveType type;

    // Constructor
    public TapeDrive() {
        //pass
    }

    // Getters and Setters
    
    public boolean getCleaningRequired() {
        return this.cleaningRequired;
    }

    public void setCleaningRequired(final boolean cleaningRequired) {
        this.cleaningRequired = cleaningRequired;
    }


    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public boolean getForceTapeRemoval() {
        return this.forceTapeRemoval;
    }

    public void setForceTapeRemoval(final boolean forceTapeRemoval) {
        this.forceTapeRemoval = forceTapeRemoval;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public Date getLastCleaned() {
        return this.lastCleaned;
    }

    public void setLastCleaned(final Date lastCleaned) {
        this.lastCleaned = lastCleaned;
    }


    public Integer getMaxFailedTapes() {
        return this.maxFailedTapes;
    }

    public void setMaxFailedTapes(final Integer maxFailedTapes) {
        this.maxFailedTapes = maxFailedTapes;
    }


    public String getMfgSerialNumber() {
        return this.mfgSerialNumber;
    }

    public void setMfgSerialNumber(final String mfgSerialNumber) {
        this.mfgSerialNumber = mfgSerialNumber;
    }


    public Priority getMinimumTaskPriority() {
        return this.minimumTaskPriority;
    }

    public void setMinimumTaskPriority(final Priority minimumTaskPriority) {
        this.minimumTaskPriority = minimumTaskPriority;
    }


    public UUID getPartitionId() {
        return this.partitionId;
    }

    public void setPartitionId(final UUID partitionId) {
        this.partitionId = partitionId;
    }


    public Quiesced getQuiesced() {
        return this.quiesced;
    }

    public void setQuiesced(final Quiesced quiesced) {
        this.quiesced = quiesced;
    }


    public ReservedTaskType getReservedTaskType() {
        return this.reservedTaskType;
    }

    public void setReservedTaskType(final ReservedTaskType reservedTaskType) {
        this.reservedTaskType = reservedTaskType;
    }


    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public TapeDriveState getState() {
        return this.state;
    }

    public void setState(final TapeDriveState state) {
        this.state = state;
    }


    public UUID getTapeId() {
        return this.tapeId;
    }

    public void setTapeId(final UUID tapeId) {
        this.tapeId = tapeId;
    }


    public TapeDriveType getType() {
        return this.type;
    }

    public void setType(final TapeDriveType type) {
        this.type = type;
    }

}