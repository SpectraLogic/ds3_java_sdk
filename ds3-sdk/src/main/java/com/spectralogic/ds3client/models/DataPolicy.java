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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.spectralogic.ds3client.models.ChecksumType;
import java.util.Date;
import java.lang.Long;
import com.spectralogic.ds3client.models.BlobStoreTaskPriority;
import java.util.UUID;
import java.lang.String;
import com.spectralogic.ds3client.models.VersioningLevel;

@JacksonXmlRootElement(namespace = "Data")
public class DataPolicy {

    // Variables
    @JsonProperty("BlobbingEnabled")
    private boolean blobbingEnabled;

    @JsonProperty("ChecksumType")
    private ChecksumType checksumType;

    @JsonProperty("CreationDate")
    private Date creationDate;

    @JsonProperty("DefaultBlobSize")
    private Long defaultBlobSize;

    @JsonProperty("DefaultGetJobPriority")
    private BlobStoreTaskPriority defaultGetJobPriority;

    @JsonProperty("DefaultPutJobPriority")
    private BlobStoreTaskPriority defaultPutJobPriority;

    @JsonProperty("DefaultVerifyJobPriority")
    private BlobStoreTaskPriority defaultVerifyJobPriority;

    @JsonProperty("EndToEndCrcRequired")
    private boolean endToEndCrcRequired;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("LtfsObjectNamingAllowed")
    private boolean ltfsObjectNamingAllowed;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("RebuildPriority")
    private BlobStoreTaskPriority rebuildPriority;

    @JsonProperty("Versioning")
    private VersioningLevel versioning;

    // Constructor
    public DataPolicy() {
        //pass
    }

    // Getters and Setters
    
    public boolean getBlobbingEnabled() {
        return this.blobbingEnabled;
    }

    public void setBlobbingEnabled(final boolean blobbingEnabled) {
        this.blobbingEnabled = blobbingEnabled;
    }


    public ChecksumType getChecksumType() {
        return this.checksumType;
    }

    public void setChecksumType(final ChecksumType checksumType) {
        this.checksumType = checksumType;
    }


    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }


    public Long getDefaultBlobSize() {
        return this.defaultBlobSize;
    }

    public void setDefaultBlobSize(final Long defaultBlobSize) {
        this.defaultBlobSize = defaultBlobSize;
    }


    public BlobStoreTaskPriority getDefaultGetJobPriority() {
        return this.defaultGetJobPriority;
    }

    public void setDefaultGetJobPriority(final BlobStoreTaskPriority defaultGetJobPriority) {
        this.defaultGetJobPriority = defaultGetJobPriority;
    }


    public BlobStoreTaskPriority getDefaultPutJobPriority() {
        return this.defaultPutJobPriority;
    }

    public void setDefaultPutJobPriority(final BlobStoreTaskPriority defaultPutJobPriority) {
        this.defaultPutJobPriority = defaultPutJobPriority;
    }


    public BlobStoreTaskPriority getDefaultVerifyJobPriority() {
        return this.defaultVerifyJobPriority;
    }

    public void setDefaultVerifyJobPriority(final BlobStoreTaskPriority defaultVerifyJobPriority) {
        this.defaultVerifyJobPriority = defaultVerifyJobPriority;
    }


    public boolean getEndToEndCrcRequired() {
        return this.endToEndCrcRequired;
    }

    public void setEndToEndCrcRequired(final boolean endToEndCrcRequired) {
        this.endToEndCrcRequired = endToEndCrcRequired;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public boolean getLtfsObjectNamingAllowed() {
        return this.ltfsObjectNamingAllowed;
    }

    public void setLtfsObjectNamingAllowed(final boolean ltfsObjectNamingAllowed) {
        this.ltfsObjectNamingAllowed = ltfsObjectNamingAllowed;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public BlobStoreTaskPriority getRebuildPriority() {
        return this.rebuildPriority;
    }

    public void setRebuildPriority(final BlobStoreTaskPriority rebuildPriority) {
        this.rebuildPriority = rebuildPriority;
    }


    public VersioningLevel getVersioning() {
        return this.versioning;
    }

    public void setVersioning(final VersioningLevel versioning) {
        this.versioning = versioning;
    }

}