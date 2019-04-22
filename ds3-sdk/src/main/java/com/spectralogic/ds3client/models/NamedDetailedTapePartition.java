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
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.util.UUID;

@JacksonXmlRootElement(namespace = "TapePartition")
public class NamedDetailedTapePartition {

    // Variables
    @JsonProperty("AutoCompactionEnabled")
    private boolean autoCompactionEnabled;

    @JsonProperty("DriveType")
    private TapeDriveType driveType;

    @JsonProperty("DriveTypes")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<TapeDriveType> driveTypes = new ArrayList<>();

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("ImportExportConfiguration")
    private ImportExportConfiguration importExportConfiguration;

    @JsonProperty("LibraryId")
    private UUID libraryId;

    @JsonProperty("MinimumReadReservedDrives")
    private int minimumReadReservedDrives;

    @JsonProperty("MinimumWriteReservedDrives")
    private int minimumWriteReservedDrives;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Quiesced")
    private Quiesced quiesced;

    @JsonProperty("SerialNumber")
    private String serialNumber;

    @JsonProperty("State")
    private TapePartitionState state;

    @JsonProperty("TapeTypes")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<String> tapeTypes = new ArrayList<>();

    // Constructor
    public NamedDetailedTapePartition() {
        //pass
    }

    // Getters and Setters
    
    public boolean getAutoCompactionEnabled() {
        return this.autoCompactionEnabled;
    }

    public void setAutoCompactionEnabled(final boolean autoCompactionEnabled) {
        this.autoCompactionEnabled = autoCompactionEnabled;
    }


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


    public int getMinimumReadReservedDrives() {
        return this.minimumReadReservedDrives;
    }

    public void setMinimumReadReservedDrives(final int minimumReadReservedDrives) {
        this.minimumReadReservedDrives = minimumReadReservedDrives;
    }


    public int getMinimumWriteReservedDrives() {
        return this.minimumWriteReservedDrives;
    }

    public void setMinimumWriteReservedDrives(final int minimumWriteReservedDrives) {
        this.minimumWriteReservedDrives = minimumWriteReservedDrives;
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


    public List<String> getTapeTypes() {
        return this.tapeTypes;
    }

    public void setTapeTypes(final List<String> tapeTypes) {
        this.tapeTypes = tapeTypes;
    }

}