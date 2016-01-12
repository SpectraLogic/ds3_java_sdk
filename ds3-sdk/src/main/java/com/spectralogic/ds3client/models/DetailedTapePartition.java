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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spectralogic.ds3client.models.TapeDriveType;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.lang.String;
import java.util.UUID;
import com.spectralogic.ds3client.models.ImportExportConfiguration;
import com.spectralogic.ds3client.models.Quiesced;
import com.spectralogic.ds3client.models.TapePartitionState;
import com.spectralogic.ds3client.models.TapeType;

public class DetailedTapePartition {

    // Variables
    @JsonProperty("DriveType")
    private TapeDriveType driveType;

    @JsonProperty("DriveTypes")
    @JacksonXmlElementWrapper
    private List<TapeDriveType> driveTypes;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("ImportExportConfiguration")
    private ImportExportConfiguration importExportConfiguration;

    @JsonProperty("LibraryId")
    private UUID libraryId;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Quiesced")
    private Quiesced quiesced;

    @JsonProperty("SerialNumber")
    private String serialNumber;

    @JsonProperty("State")
    private TapePartitionState state;

    @JsonProperty("TapeTypes")
    @JacksonXmlElementWrapper
    private List<TapeType> tapeTypes;

    // Constructor
    public DetailedTapePartition(final TapeDriveType driveType, final List<TapeDriveType> driveTypes, final String errorMessage, final UUID id, final ImportExportConfiguration importExportConfiguration, final UUID libraryId, final String name, final Quiesced quiesced, final String serialNumber, final TapePartitionState state, final List<TapeType> tapeTypes) {
        this.driveType = driveType;
        this.driveTypes = driveTypes;
        this.errorMessage = errorMessage;
        this.id = id;
        this.importExportConfiguration = importExportConfiguration;
        this.libraryId = libraryId;
        this.name = name;
        this.quiesced = quiesced;
        this.serialNumber = serialNumber;
        this.state = state;
        this.tapeTypes = tapeTypes;
    }

    // Getters and Setters
    
    public TapeDriveType getDriveType() {
        return this.driveType;
    }

    public void setDriveType(final TapeDriveType driveType) {
        this.driveType = driveType;
    }


    public List<TapeDriveType> getDriveTypes() {
        return this.driveTypes;
    }

    public void setDriveTypes(final List<TapeDriveType> driveTypes) {
        this.driveTypes = driveTypes;
    }


    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public ImportExportConfiguration getImportExportConfiguration() {
        return this.importExportConfiguration;
    }

    public void setImportExportConfiguration(final ImportExportConfiguration importExportConfiguration) {
        this.importExportConfiguration = importExportConfiguration;
    }


    public UUID getLibraryId() {
        return this.libraryId;
    }

    public void setLibraryId(final UUID libraryId) {
        this.libraryId = libraryId;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public Quiesced getQuiesced() {
        return this.quiesced;
    }

    public void setQuiesced(final Quiesced quiesced) {
        this.quiesced = quiesced;
    }


    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public TapePartitionState getState() {
        return this.state;
    }

    public void setState(final TapePartitionState state) {
        this.state = state;
    }


    public List<TapeType> getTapeTypes() {
        return this.tapeTypes;
    }

    public void setTapeTypes(final List<TapeType> tapeTypes) {
        this.tapeTypes = tapeTypes;
    }

}