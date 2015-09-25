package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.models.tape.Tape;

import java.util.UUID;

public class GetTapesRequest extends AbstractRequest {

    private boolean assignedToBucket;
    private long availableRawCapacity;
    private String barCode;
    private String bucketId;
    private String descriptionForIdentification;
    private String ejectLabel;
    private String ejectLocation;
    private boolean fullOfData;
    private UUID id;
    private String lastCheckpoint;
    private String partitionId;
    private Tape.State previousState;
    private String serialNumber;
    private Tape.State state;
    private long totalRawCapacity;
    private Tape.TapeType type;
    private boolean writeProtected;
    private int pageLength;
    private int pageOffset;
    private boolean fullDetails;

    public GetTapesRequest() { }

    @Override
    public String getPath() {
        return "/_rest_/tape/";
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    public GetTapesRequest withIsAssignedToBucket(final boolean assignedToBucket) {
        this.assignedToBucket = assignedToBucket;
        this.updateQueryParam("assigned_to_bucket", Boolean.toString(assignedToBucket));
        return this;
    }

    public boolean isAssignedToBucket() {
        return assignedToBucket;
    }

    public GetTapesRequest withAvailableRawCapacity(final long rawCapacity) {
        this.availableRawCapacity = rawCapacity;
        this.updateQueryParam("available_raw_capacity", Long.toString(rawCapacity));
        return this;
    }

    public long getAvailableRawCapacity() {
        return availableRawCapacity;
    }

    public GetTapesRequest withBarCode(final String barCode) {
        this.barCode = barCode;
        this.updateQueryParam("bar_code", barCode);
        return this;
    }

    public String getBarCode() {
        return barCode;
    }

    public GetTapesRequest withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }

    public String getBucketId() {
        return bucketId;
    }

    public GetTapesRequest withDescriptionForIdentification(final String descriptionForIdentification) {
        this.descriptionForIdentification = descriptionForIdentification;
        this.updateQueryParam("description_for_identification", descriptionForIdentification);
        return this;
    }

    public String getDescriptionForIdentification() {
        return descriptionForIdentification;
    }

    public GetTapesRequest withEjectLabel(final String ejectLabel) {
        this.ejectLabel = ejectLabel;
        this.updateQueryParam("eject_label", ejectLabel);
        return this;
    }

    public String getEjectLabel() {
        return ejectLabel;
    }

    public GetTapesRequest withEjectLocation(final String ejectLocation) {
        this.ejectLocation = ejectLocation;
        this.updateQueryParam("eject_location", ejectLocation);
        return this;
    }

    public String getEjectLocation() {
        return ejectLocation;
    }

    public GetTapesRequest withFullOfData(final boolean full) {
        this.fullOfData = full;
        this.updateQueryParam("full_of_date", Boolean.toString(full));
        return this;
    }

    public boolean isFullOfData() {
        return fullOfData;
    }

    public GetTapesRequest withId(final UUID id) {
        this.id = id;
        this.updateQueryParam("id", id.toString());
        return this;
    }

    public UUID getId() {
        return id;
    }

    public GetTapesRequest withLastCheckpoint(final String lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
        this.updateQueryParam("last_checkpoint", lastCheckpoint);
        return this;
    }

    public String getLastCheckpoint() {
        return lastCheckpoint;
    }

    public GetTapesRequest withParitionId(final String partitionId) {
        this.partitionId = partitionId;
        this.updateQueryParam("partition_id", partitionId);
        return this;
    }

    public String getPartitionId() {
        return partitionId;
    }

    public GetTapesRequest withPreviousState(final Tape.State state) {
        this.previousState = state;
        this.updateQueryParam("previous_state", state.toString());
        return this;
    }

    public Tape.State getPreviousState() {
        return previousState;
    }

    public GetTapesRequest withSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
        this.updateQueryParam("serial_number", serialNumber);
        return this;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public GetTapesRequest withState(final Tape.State state) {
        this.state = state;
        this.updateQueryParam("state", state.toString());
        return this;
    }

    public Tape.State getState() {
        return state;
    }

    public GetTapesRequest withTotalRawCapacity(final long totalRawCapacity) {
        this.totalRawCapacity = totalRawCapacity;
        this.updateQueryParam("total_raw_capacity", Long.toString(totalRawCapacity));
        return this;
    }

    public long getTotalRawCapacity() {
        return totalRawCapacity;
    }

    public GetTapesRequest withType(final Tape.TapeType type) {
        this.type = type;
        this.updateQueryParam("type", type.toString());
        return this;
    }

    public Tape.TapeType getType() {
        return type;
    }

    public GetTapesRequest withWriteProtected(final boolean writeProtected) {
        this.writeProtected = writeProtected;
        this.updateQueryParam("write_protected", Boolean.toString(writeProtected));
        return this;
    }

    public boolean isWriteProtected() {
        return writeProtected;
    }

    public GetTapesRequest withPageLength(final int pageLength) {
        this.pageLength = pageLength;
        this.updateQueryParam("page_length", Integer.toString(pageLength));
        return this;
    }

    public int getPageLength() {
        return pageLength;
    }

    public GetTapesRequest withPageOffset(final int pageOffset) {
        this.pageOffset = pageOffset;
        this.updateQueryParam("page_offset", Integer.toString(pageOffset));
        return this;
    }

    public int getPageOffset() {
        return pageOffset;
    }

    public GetTapesRequest withFullDetails(final boolean fullDetails) {
        this.fullDetails = fullDetails;
        if (this.fullDetails) {
            this.getQueryParams().put("full_details", null);
        } else {
            this.getQueryParams().remove("full_details");
        }
        return this;
    }

    public boolean isFullDetails() {
        return fullDetails;
    }
}
