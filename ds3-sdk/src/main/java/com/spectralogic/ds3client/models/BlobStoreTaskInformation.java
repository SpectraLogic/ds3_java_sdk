/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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
import java.util.Date;
import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class BlobStoreTaskInformation {

    // Variables
    @JsonProperty("DateScheduled")
    private Date dateScheduled;

    @JsonProperty("DateStarted")
    private Date dateStarted;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("DriveId")
    private UUID driveId;

    @JsonProperty("Id")
    private long id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("PoolId")
    private UUID poolId;

    @JsonProperty("Priority")
    private Priority priority;

    @JsonProperty("State")
    private BlobStoreTaskState state;

    @JsonProperty("TapeId")
    private UUID tapeId;

    @JsonProperty("TargetId")
    private UUID targetId;

    @JsonProperty("TargetType")
    private String targetType;

    // Constructor
    public BlobStoreTaskInformation() {
        //pass
    }

    // Getters and Setters
    
    public Date getDateScheduled() {
        return this.dateScheduled;
    }

    public void setDateScheduled(final Date dateScheduled) {
        this.dateScheduled = dateScheduled;
    }


    public Date getDateStarted() {
        return this.dateStarted;
    }

    public void setDateStarted(final Date dateStarted) {
        this.dateStarted = dateStarted;
    }


    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }


    public UUID getDriveId() {
        return this.driveId;
    }

    public void setDriveId(final UUID driveId) {
        this.driveId = driveId;
    }


    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public UUID getPoolId() {
        return this.poolId;
    }

    public void setPoolId(final UUID poolId) {
        this.poolId = poolId;
    }


    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }


    public BlobStoreTaskState getState() {
        return this.state;
    }

    public void setState(final BlobStoreTaskState state) {
        this.state = state;
    }


    public UUID getTapeId() {
        return this.tapeId;
    }

    public void setTapeId(final UUID tapeId) {
        this.tapeId = tapeId;
    }


    public UUID getTargetId() {
        return this.targetId;
    }

    public void setTargetId(final UUID targetId) {
        this.targetId = targetId;
    }


    public String getTargetType() {
        return this.targetType;
    }

    public void setTargetType(final String targetType) {
        this.targetType = targetType;
    }

}