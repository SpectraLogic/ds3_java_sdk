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
import java.lang.Long;
import java.lang.String;
import java.util.UUID;
import java.util.Date;
import com.spectralogic.ds3client.models.TapeFailure;
import com.spectralogic.ds3client.models.TapeState;
import com.spectralogic.ds3client.models.TapeType;
import com.spectralogic.ds3client.models.BlobStoreTaskPriority;

public class TapeApiBean {

    // Variables
    @JsonProperty("AssignedToStorageDomain")
    private boolean assignedToStorageDomain;

    @JsonProperty("AvailableRawCapacity")
    private Long availableRawCapacity;

    @JsonProperty("BarCode")
    private String barCode;

    @JsonProperty("BucketId")
    private UUID bucketId;

    @JsonProperty("DescriptionForIdentification")
    private String descriptionForIdentification;

    @JsonProperty("EjectDate")
    private Date ejectDate;

    @JsonProperty("EjectLabel")
    private String ejectLabel;

    @JsonProperty("EjectLocation")
    private String ejectLocation;

    @JsonProperty("EjectPending")
    private Date ejectPending;

    @JsonProperty("FullOfData")
    private boolean fullOfData;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("LastAccessed")
    private Date lastAccessed;

    @JsonProperty("LastCheckpoint")
    private String lastCheckpoint;

    @JsonProperty("LastModified")
    private Date lastModified;

    @JsonProperty("LastVerified")
    private Date lastVerified;

    @JsonProperty("MostRecentFailure")
    private TapeFailure mostRecentFailure;

    @JsonProperty("PartitionId")
    private UUID partitionId;

    @JsonProperty("PreviousState")
    private TapeState previousState;

    @JsonProperty("SerialNumber")
    private String serialNumber;

    @JsonProperty("State")
    private TapeState state;

    @JsonProperty("StorageDomainId")
    private UUID storageDomainId;

    @JsonProperty("TakeOwnershipPending")
    private boolean takeOwnershipPending;

    @JsonProperty("TotalRawCapacity")
    private Long totalRawCapacity;

    @JsonProperty("Type")
    private TapeType type;

    @JsonProperty("VerifyPending")
    private BlobStoreTaskPriority verifyPending;

    @JsonProperty("WriteProtected")
    private boolean writeProtected;

    // Constructor
    public TapeApiBean() {
        //pass
    }

    // Getters and Setters
    
    public boolean getAssignedToStorageDomain() {
        return this.assignedToStorageDomain;
    }

    public void setAssignedToStorageDomain(final boolean assignedToStorageDomain) {
        this.assignedToStorageDomain = assignedToStorageDomain;
    }


    public Long getAvailableRawCapacity() {
        return this.availableRawCapacity;
    }

    public void setAvailableRawCapacity(final Long availableRawCapacity) {
        this.availableRawCapacity = availableRawCapacity;
    }


    public String getBarCode() {
        return this.barCode;
    }

    public void setBarCode(final String barCode) {
        this.barCode = barCode;
    }


    public UUID getBucketId() {
        return this.bucketId;
    }

    public void setBucketId(final UUID bucketId) {
        this.bucketId = bucketId;
    }


    public String getDescriptionForIdentification() {
        return this.descriptionForIdentification;
    }

    public void setDescriptionForIdentification(final String descriptionForIdentification) {
        this.descriptionForIdentification = descriptionForIdentification;
    }


    public Date getEjectDate() {
        return this.ejectDate;
    }

    public void setEjectDate(final Date ejectDate) {
        this.ejectDate = ejectDate;
    }


    public String getEjectLabel() {
        return this.ejectLabel;
    }

    public void setEjectLabel(final String ejectLabel) {
        this.ejectLabel = ejectLabel;
    }


    public String getEjectLocation() {
        return this.ejectLocation;
    }

    public void setEjectLocation(final String ejectLocation) {
        this.ejectLocation = ejectLocation;
    }


    public Date getEjectPending() {
        return this.ejectPending;
    }

    public void setEjectPending(final Date ejectPending) {
        this.ejectPending = ejectPending;
    }


    public boolean getFullOfData() {
        return this.fullOfData;
    }

    public void setFullOfData(final boolean fullOfData) {
        this.fullOfData = fullOfData;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public Date getLastAccessed() {
        return this.lastAccessed;
    }

    public void setLastAccessed(final Date lastAccessed) {
        this.lastAccessed = lastAccessed;
    }


    public String getLastCheckpoint() {
        return this.lastCheckpoint;
    }

    public void setLastCheckpoint(final String lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }


    public Date getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(final Date lastModified) {
        this.lastModified = lastModified;
    }


    public Date getLastVerified() {
        return this.lastVerified;
    }

    public void setLastVerified(final Date lastVerified) {
        this.lastVerified = lastVerified;
    }


    public TapeFailure getMostRecentFailure() {
        return this.mostRecentFailure;
    }

    public void setMostRecentFailure(final TapeFailure mostRecentFailure) {
        this.mostRecentFailure = mostRecentFailure;
    }


    public UUID getPartitionId() {
        return this.partitionId;
    }

    public void setPartitionId(final UUID partitionId) {
        this.partitionId = partitionId;
    }


    public TapeState getPreviousState() {
        return this.previousState;
    }

    public void setPreviousState(final TapeState previousState) {
        this.previousState = previousState;
    }


    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public TapeState getState() {
        return this.state;
    }

    public void setState(final TapeState state) {
        this.state = state;
    }


    public UUID getStorageDomainId() {
        return this.storageDomainId;
    }

    public void setStorageDomainId(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId;
    }


    public boolean getTakeOwnershipPending() {
        return this.takeOwnershipPending;
    }

    public void setTakeOwnershipPending(final boolean takeOwnershipPending) {
        this.takeOwnershipPending = takeOwnershipPending;
    }


    public Long getTotalRawCapacity() {
        return this.totalRawCapacity;
    }

    public void setTotalRawCapacity(final Long totalRawCapacity) {
        this.totalRawCapacity = totalRawCapacity;
    }


    public TapeType getType() {
        return this.type;
    }

    public void setType(final TapeType type) {
        this.type = type;
    }


    public BlobStoreTaskPriority getVerifyPending() {
        return this.verifyPending;
    }

    public void setVerifyPending(final BlobStoreTaskPriority verifyPending) {
        this.verifyPending = verifyPending;
    }


    public boolean getWriteProtected() {
        return this.writeProtected;
    }

    public void setWriteProtected(final boolean writeProtected) {
        this.writeProtected = writeProtected;
    }

}