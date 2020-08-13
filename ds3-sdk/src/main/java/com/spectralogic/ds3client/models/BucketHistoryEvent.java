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
public class BucketHistoryEvent {

    // Variables
    @JsonProperty("BucketId")
    private UUID bucketId;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("ObjectCreationDate")
    private Date objectCreationDate;

    @JsonProperty("ObjectName")
    private String objectName;

    @JsonProperty("SequenceNumber")
    private Long sequenceNumber;

    @JsonProperty("Type")
    private BucketHistoryEventType type;

    @JsonProperty("VersionId")
    private UUID versionId;

    // Constructor
    public BucketHistoryEvent() {
        //pass
    }

    // Getters and Setters
    
    public UUID getBucketId() {
        return this.bucketId;
    }

    public void setBucketId(final UUID bucketId) {
        this.bucketId = bucketId;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public Date getObjectCreationDate() {
        return this.objectCreationDate;
    }

    public void setObjectCreationDate(final Date objectCreationDate) {
        this.objectCreationDate = objectCreationDate;
    }


    public String getObjectName() {
        return this.objectName;
    }

    public void setObjectName(final String objectName) {
        this.objectName = objectName;
    }


    public Long getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(final Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }


    public BucketHistoryEventType getType() {
        return this.type;
    }

    public void setType(final BucketHistoryEventType type) {
        this.type = type;
    }


    public UUID getVersionId() {
        return this.versionId;
    }

    public void setVersionId(final UUID versionId) {
        this.versionId = versionId;
    }

}