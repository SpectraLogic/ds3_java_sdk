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
public class JobChunk {

    // Variables
    @JsonProperty("BlobStoreState")
    private JobChunkBlobStoreState blobStoreState;

    @JsonProperty("ChunkNumber")
    private int chunkNumber;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("JobCreationDate")
    private Date jobCreationDate;

    @JsonProperty("JobId")
    private UUID jobId;

    @JsonProperty("NodeId")
    private UUID nodeId;

    @JsonProperty("PendingTargetCommit")
    private boolean pendingTargetCommit;

    @JsonProperty("ReadFromAzureTargetId")
    private UUID readFromAzureTargetId;

    @JsonProperty("ReadFromDs3TargetId")
    private UUID readFromDs3TargetId;

    @JsonProperty("ReadFromPoolId")
    private UUID readFromPoolId;

    @JsonProperty("ReadFromS3TargetId")
    private UUID readFromS3TargetId;

    @JsonProperty("ReadFromTapeId")
    private UUID readFromTapeId;

    // Constructor
    public JobChunk() {
        //pass
    }

    // Getters and Setters
    
    public JobChunkBlobStoreState getBlobStoreState() {
        return this.blobStoreState;
    }

    public void setBlobStoreState(final JobChunkBlobStoreState blobStoreState) {
        this.blobStoreState = blobStoreState;
    }


    public int getChunkNumber() {
        return this.chunkNumber;
    }

    public void setChunkNumber(final int chunkNumber) {
        this.chunkNumber = chunkNumber;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public Date getJobCreationDate() {
        return this.jobCreationDate;
    }

    public void setJobCreationDate(final Date jobCreationDate) {
        this.jobCreationDate = jobCreationDate;
    }


    public UUID getJobId() {
        return this.jobId;
    }

    public void setJobId(final UUID jobId) {
        this.jobId = jobId;
    }


    public UUID getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(final UUID nodeId) {
        this.nodeId = nodeId;
    }


    public boolean getPendingTargetCommit() {
        return this.pendingTargetCommit;
    }

    public void setPendingTargetCommit(final boolean pendingTargetCommit) {
        this.pendingTargetCommit = pendingTargetCommit;
    }


    public UUID getReadFromAzureTargetId() {
        return this.readFromAzureTargetId;
    }

    public void setReadFromAzureTargetId(final UUID readFromAzureTargetId) {
        this.readFromAzureTargetId = readFromAzureTargetId;
    }


    public UUID getReadFromDs3TargetId() {
        return this.readFromDs3TargetId;
    }

    public void setReadFromDs3TargetId(final UUID readFromDs3TargetId) {
        this.readFromDs3TargetId = readFromDs3TargetId;
    }


    public UUID getReadFromPoolId() {
        return this.readFromPoolId;
    }

    public void setReadFromPoolId(final UUID readFromPoolId) {
        this.readFromPoolId = readFromPoolId;
    }


    public UUID getReadFromS3TargetId() {
        return this.readFromS3TargetId;
    }

    public void setReadFromS3TargetId(final UUID readFromS3TargetId) {
        this.readFromS3TargetId = readFromS3TargetId;
    }


    public UUID getReadFromTapeId() {
        return this.readFromTapeId;
    }

    public void setReadFromTapeId(final UUID readFromTapeId) {
        this.readFromTapeId = readFromTapeId;
    }

}