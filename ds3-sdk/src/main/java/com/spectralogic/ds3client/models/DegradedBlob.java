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

@JacksonXmlRootElement(namespace = "Data")
public class DegradedBlob {

    // Variables
    @JsonProperty("AzureReplicationRuleId")
    private UUID azureReplicationRuleId;

    @JsonProperty("BlobId")
    private UUID blobId;

    @JsonProperty("BucketId")
    private UUID bucketId;

    @JsonProperty("Ds3ReplicationRuleId")
    private UUID ds3ReplicationRuleId;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("PersistenceRuleId")
    private UUID persistenceRuleId;

    @JsonProperty("S3ReplicationRuleId")
    private UUID s3ReplicationRuleId;

    // Constructor
    public DegradedBlob() {
        //pass
    }

    // Getters and Setters
    
    public UUID getAzureReplicationRuleId() {
        return this.azureReplicationRuleId;
    }

    public void setAzureReplicationRuleId(final UUID azureReplicationRuleId) {
        this.azureReplicationRuleId = azureReplicationRuleId;
    }


    public UUID getBlobId() {
        return this.blobId;
    }

    public void setBlobId(final UUID blobId) {
        this.blobId = blobId;
    }


    public UUID getBucketId() {
        return this.bucketId;
    }

    public void setBucketId(final UUID bucketId) {
        this.bucketId = bucketId;
    }


    public UUID getDs3ReplicationRuleId() {
        return this.ds3ReplicationRuleId;
    }

    public void setDs3ReplicationRuleId(final UUID ds3ReplicationRuleId) {
        this.ds3ReplicationRuleId = ds3ReplicationRuleId;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public UUID getPersistenceRuleId() {
        return this.persistenceRuleId;
    }

    public void setPersistenceRuleId(final UUID persistenceRuleId) {
        this.persistenceRuleId = persistenceRuleId;
    }


    public UUID getS3ReplicationRuleId() {
        return this.s3ReplicationRuleId;
    }

    public void setS3ReplicationRuleId(final UUID s3ReplicationRuleId) {
        this.s3ReplicationRuleId = s3ReplicationRuleId;
    }

}