package com.spectralogic.ds3client.models.tape;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class Tape {

    @JsonProperty("AssignedToBucket")
    private boolean assignedToBucket;

    @JsonProperty("AvailableRawCapacity")
    private long availableRawCapacity;

    @JsonProperty("BarCode")
    private String barcode;

    @JsonProperty("BucketId")
    private UUID bucketId;

    @JsonProperty("DescriptionForIdentification")
    private String descriptionForIdentification;

    @JsonProperty("EjectDate")
    private String ejectDate;

    @JsonProperty("EjectLabel")
    private String ejectLabel;

    @JsonProperty("EjectLocation")
    private String ejectLocation;

    @JsonProperty("EjectPending")
    private String ejectPending;

    @JsonProperty("FullOfData")
    private boolean fullOfData;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("LastAccessed")
    private String lastAccessed;

    @JsonProperty("LastCheckpoint")
    private String lastCheckpoint;

    @JsonProperty("LastModified")
    private String lastModified;

    @JsonProperty("LastVerified")
    private String lastVerified;

    @JsonProperty("MostRecentFailure")
    private String mostRecentFailure;

    @JsonProperty("PartitionId")
    private UUID partitionId;

    @JsonProperty("PreviousState")
    private State previousState;

    @JsonProperty("SerialNumber")
    private String serialNumber;

    @JsonProperty("State")
    private State state;

    @JsonProperty("TotalRawCapacity")
    private long totalRawCapacity;

    @JsonProperty("Type")
    private TapeType type;

    @JsonProperty("WriteProtected")
    private boolean writeProtected;

    public boolean isAssignedToBucket() {
        return assignedToBucket;
    }

    public void setAssignedToBucket(final boolean assignedToBucket) {
        this.assignedToBucket = assignedToBucket;
    }

    public long getAvailableRawCapacity() {
        return availableRawCapacity;
    }

    public void setAvailableRawCapacity(final long availableRawCapacity) {
        this.availableRawCapacity = availableRawCapacity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(final String barcode) {
        this.barcode = barcode;
    }

    public UUID getBucketId() {
        return bucketId;
    }

    public void setBucketId(final UUID bucketId) {
        this.bucketId = bucketId;
    }

    public String getDescriptionForIdentification() {
        return descriptionForIdentification;
    }

    public void setDescriptionForIdentification(final String descriptionForIdentification) {
        this.descriptionForIdentification = descriptionForIdentification;
    }

    public String getEjectDate() {
        return ejectDate;
    }

    public void setEjectDate(final String ejectDate) {
        this.ejectDate = ejectDate;
    }

    public String getEjectLabel() {
        return ejectLabel;
    }

    public void setEjectLabel(final String ejectLabel) {
        this.ejectLabel = ejectLabel;
    }

    public String getEjectLocation() {
        return ejectLocation;
    }

    public void setEjectLocation(final String ejectLocation) {
        this.ejectLocation = ejectLocation;
    }

    public String getEjectPending() {
        return ejectPending;
    }

    public void setEjectPending(final String ejectPending) {
        this.ejectPending = ejectPending;
    }

    public boolean isFullOfData() {
        return fullOfData;
    }

    public void setFullOfData(final boolean fullOfData) {
        this.fullOfData = fullOfData;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(final String lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public String getLastCheckpoint() {
        return lastCheckpoint;
    }

    public void setLastCheckpoint(final String lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(final String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastVerified() {
        return lastVerified;
    }

    public void setLastVerified(final String lastVerified) {
        this.lastVerified = lastVerified;
    }

    public UUID getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(final UUID partitionId) {
        this.partitionId = partitionId;
    }

    public State getPreviousState() {
        return previousState;
    }

    public void setPreviousState(final State previousState) {
        this.previousState = previousState;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public State getState() {
        return state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public long getTotalRawCapacity() {
        return totalRawCapacity;
    }

    public void setTotalRawCapacity(final long totalRawCapacity) {
        this.totalRawCapacity = totalRawCapacity;
    }

    public TapeType getType() {
        return type;
    }

    public void setType(final TapeType type) {
        this.type = type;
    }

    public boolean isWriteProtected() {
        return writeProtected;
    }

    public void setWriteProtected(final boolean writeProtected) {
        this.writeProtected = writeProtected;
    }

    public String getMostRecentFailure() {
        return mostRecentFailure;
    }

    public void setMostRecentFailure(String mostRecentFailure) {
        this.mostRecentFailure = mostRecentFailure;
    }

    public enum State {
        NORMAL, BAD, BARCODE_MISSING, DATA_CHECKPOINT_MISSING, DATA_CHECKPOINT_FAILURE, EJECT_FROM_EE_PENDING, EJECT_TO_EE_IN_PROGRESS, FORMAT_PENDING, IMPORT_IN_PROGRESS, LOST, LTFS_WITH_FOREIGN_DATA, OFFLINE, ONLINE_IN_PROGRESS, ONLINE_PENDING, PENDING_INSPECTION, SERIAL_NUMBER_MISMATCH, UNKNOWN
    }

    public enum TapeType {
        LTO5, LTO6, LTO7, LTO_CLEANING_TAPE, TS_JC, TS_JY, TS_JK, TS_JD, TS_JZ, TS_JL, TS_CLEANING_TAPE, UNKNOWN, FORBIDDEN
    }
}
