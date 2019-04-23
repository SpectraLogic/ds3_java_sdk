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

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.UUID;
import java.util.Date;

@JacksonXmlRootElement(namespace = "Data")
public class S3Object {

    // Variables
    @JsonProperty("BucketId")
    private UUID bucketId;

    @JsonProperty("CreationDate")
    private Date creationDate;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("Latest")
    private boolean latest;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Type")
    private S3ObjectType type;

    // Constructor
    public S3Object() {
        //pass
    }

    // Getters and Setters
    
    public UUID getBucketId() {
        return this.bucketId;
    }

    public void setBucketId(final UUID bucketId) {
        this.bucketId = bucketId;
    }


    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public boolean getLatest() {
        return this.latest;
    }

    public void setLatest(final boolean latest) {
        this.latest = latest;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public S3ObjectType getType() {
        return this.type;
    }

    public void setType(final S3ObjectType type) {
        this.type = type;
    }


    @Override
    public int hashCode() {
        return Objects.hash(bucketId, name, id, latest, creationDate, type);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof S3Object)) {
            return false;
        }

        final S3Object s3obj = (S3Object)obj;

        return this.bucketId.equals(s3obj.getBucketId())
                && this.creationDate.equals(s3obj.getCreationDate())
                && this.id.equals(s3obj.getId())
                && this.latest == s3obj.getLatest()
                && this.name.equals(s3obj.getName())
                && this.type == s3obj.getType();
    }
}