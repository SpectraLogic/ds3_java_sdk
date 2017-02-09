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
import java.lang.Integer;
import java.util.UUID;
import java.util.Date;
import java.lang.String;

@JacksonXmlRootElement(namespace = "Object")
public class DetailedS3Object {

    // Variables
    @JsonProperty("Blobs")
    private BulkObjectList blobs;

    @JsonProperty("BlobsBeingPersisted")
    private Integer blobsBeingPersisted;

    @JsonProperty("BlobsDegraded")
    private Integer blobsDegraded;

    @JsonProperty("BlobsInCache")
    private Integer blobsInCache;

    @JsonProperty("BlobsTotal")
    private Integer blobsTotal;

    @JsonProperty("BucketId")
    private UUID bucketId;

    @JsonProperty("CreationDate")
    private Date creationDate;

    @JsonProperty("ETag")
    private String eTag;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("Latest")
    private boolean latest;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Owner")
    private String owner;

    @JsonProperty("Size")
    private long size;

    @JsonProperty("Type")
    private S3ObjectType type;

    @JsonProperty("Version")
    private long version;

    // Constructor
    public DetailedS3Object() {
        //pass
    }

    // Getters and Setters
    
    public BulkObjectList getBlobs() {
        return this.blobs;
    }

    public void setBlobs(final BulkObjectList blobs) {
        this.blobs = blobs;
    }


    public Integer getBlobsBeingPersisted() {
        return this.blobsBeingPersisted;
    }

    public void setBlobsBeingPersisted(final Integer blobsBeingPersisted) {
        this.blobsBeingPersisted = blobsBeingPersisted;
    }


    public Integer getBlobsDegraded() {
        return this.blobsDegraded;
    }

    public void setBlobsDegraded(final Integer blobsDegraded) {
        this.blobsDegraded = blobsDegraded;
    }


    public Integer getBlobsInCache() {
        return this.blobsInCache;
    }

    public void setBlobsInCache(final Integer blobsInCache) {
        this.blobsInCache = blobsInCache;
    }


    public Integer getBlobsTotal() {
        return this.blobsTotal;
    }

    public void setBlobsTotal(final Integer blobsTotal) {
        this.blobsTotal = blobsTotal;
    }


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


    public String getETag() {
        return this.eTag;
    }

    public void setETag(final String eTag) {
        this.eTag = eTag;
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


    public String getOwner() {
        return this.owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }


    public long getSize() {
        return this.size;
    }

    public void setSize(final long size) {
        this.size = size;
    }


    public S3ObjectType getType() {
        return this.type;
    }

    public void setType(final S3ObjectType type) {
        this.type = type;
    }


    public long getVersion() {
        return this.version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }

}