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

package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spectralogic.ds3client.commands.GetObjectsRequest.ObjectType;

import java.util.UUID;

public class S3Object {

    @JsonProperty("BucketId")
    private String bucketId;
    @JsonProperty("CreationDate")
    private String creationDate;
    @JsonProperty("Id")
    private UUID id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Type")
    private ObjectType type;
    @JsonProperty("Version")
    private long version;

    public S3Object() {
    }

    public S3Object(final String bucketId, final String creationDate, final UUID id, final String name,
                    final ObjectType type, final long version) {
        this.bucketId = bucketId;
        this.creationDate = creationDate;
        this.id = id;
        this.name = name;
        this.type = type;
        this.version = version;
    }

    public String getBucketId() { return this.bucketId; }
    public void setBucketId(final String bucketId) { this.bucketId = bucketId; }

    public String getCreationDate() { return this.creationDate; }
    public void setCreationDate(final String creationDate) { this.creationDate = creationDate; }

    public UUID getId() { return this.id; }
    public void setId(final UUID id) { this.id = id; }

    public String getName() { return this.name; }
    public void setName(final String name) { this.name = name; }

    public ObjectType getType() { return this.type; }
    public void setType(final ObjectType type) { this.type = type; }

    public long getVersion() { return this.version; }
    public void setVersion(final long version) { this.version = version; }

    @Override
    public String toString() {
        return "<S3Object>"
                + "<BucketID>" + bucketId + "</BucketID>"
                + "<CreationDate>" + creationDate + "</CreationDate>"
                + "<Id>" + id.toString() + "</Id>"
                + "<Name>" + name + "</Name>"
                + "<Type>" + type.toString() + "</Type>"
                + "<Version>" + Long.toString(version) + "</Version>"
                + "</S3Object>";
    }

    public boolean equals(final Object obj) {
        if (!(obj instanceof S3Object)) {
            return false;
        }

        final S3Object s3obj = (S3Object)obj;

        if (this.bucketId.equals(s3obj.getBucketId())
                && this.creationDate.equals(s3obj.getCreationDate())
                && this.id.equals(s3obj.getId())
                && this.name.equals(s3obj.getName())
                && this.type == s3obj.getType()
                && this.version == s3obj.getVersion()) {
            return true;
        }
        return false;
    }
}
