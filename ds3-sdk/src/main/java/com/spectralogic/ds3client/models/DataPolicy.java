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
public class DataPolicy {

    // Variables
    @JsonProperty("AlwaysForcePutJobCreation")
    private boolean alwaysForcePutJobCreation;

    @JsonProperty("AlwaysMinimizeSpanningAcrossMedia")
    private boolean alwaysMinimizeSpanningAcrossMedia;

    @JsonProperty("AlwaysReplicateDeletes")
    private boolean alwaysReplicateDeletes;

    @JsonProperty("BlobbingEnabled")
    private boolean blobbingEnabled;

    @JsonProperty("ChecksumType")
    private ChecksumType.Type checksumType;

    @JsonProperty("CreationDate")
    private Date creationDate;

    @JsonProperty("DefaultBlobSize")
    private Long defaultBlobSize;

    @JsonProperty("DefaultGetJobPriority")
    private Priority defaultGetJobPriority;

    @JsonProperty("DefaultPutJobPriority")
    private Priority defaultPutJobPriority;

    @JsonProperty("DefaultVerifyAfterWrite")
    private boolean defaultVerifyAfterWrite;

    @JsonProperty("DefaultVerifyJobPriority")
    private Priority defaultVerifyJobPriority;

    @JsonProperty("EndToEndCrcRequired")
    private boolean endToEndCrcRequired;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("LtfsObjectNamingAllowed")
    private boolean ltfsObjectNamingAllowed;

    @JsonProperty("MaxVersionsToKeep")
    private int maxVersionsToKeep;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("RebuildPriority")
    private Priority rebuildPriority;

    @JsonProperty("Versioning")
    private VersioningLevel versioning;

    // Constructor
    public DataPolicy() {
        //pass
    }

    // Getters and Setters
    
    public boolean getAlwaysForcePutJobCreation() {
        return this.alwaysForcePutJobCreation;
    }

    public void setAlwaysForcePutJobCreation(final boolean alwaysForcePutJobCreation) {
        this.alwaysForcePutJobCreation = alwaysForcePutJobCreation;
    }


    public boolean getAlwaysMinimizeSpanningAcrossMedia() {
        return this.alwaysMinimizeSpanningAcrossMedia;
    }

    public void setAlwaysMinimizeSpanningAcrossMedia(final boolean alwaysMinimizeSpanningAcrossMedia) {
        this.alwaysMinimizeSpanningAcrossMedia = alwaysMinimizeSpanningAcrossMedia;
    }


    public boolean getAlwaysReplicateDeletes() {
        return this.alwaysReplicateDeletes;
    }

    public void setAlwaysReplicateDeletes(final boolean alwaysReplicateDeletes) {
        this.alwaysReplicateDeletes = alwaysReplicateDeletes;
    }


    public boolean getBlobbingEnabled() {
        return this.blobbingEnabled;
    }

    public void setBlobbingEnabled(final boolean blobbingEnabled) {
        this.blobbingEnabled = blobbingEnabled;
    }


    public ChecksumType.Type getChecksumType() {
        return this.checksumType;
    }

    public void setChecksumType(final ChecksumType.Type checksumType) {
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


    public Priority getDefaultGetJobPriority() {
        return this.defaultGetJobPriority;
    }

    public void setDefaultGetJobPriority(final Priority defaultGetJobPriority) {
        this.defaultGetJobPriority = defaultGetJobPriority;
    }


    public Priority getDefaultPutJobPriority() {
        return this.defaultPutJobPriority;
    }

    public void setDefaultPutJobPriority(final Priority defaultPutJobPriority) {
        this.defaultPutJobPriority = defaultPutJobPriority;
    }


    public boolean getDefaultVerifyAfterWrite() {
        return this.defaultVerifyAfterWrite;
    }

    public void setDefaultVerifyAfterWrite(final boolean defaultVerifyAfterWrite) {
        this.defaultVerifyAfterWrite = defaultVerifyAfterWrite;
    }


    public Priority getDefaultVerifyJobPriority() {
        return this.defaultVerifyJobPriority;
    }

    public void setDefaultVerifyJobPriority(final Priority defaultVerifyJobPriority) {
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


    public int getMaxVersionsToKeep() {
        return this.maxVersionsToKeep;
    }

    public void setMaxVersionsToKeep(final int maxVersionsToKeep) {
        this.maxVersionsToKeep = maxVersionsToKeep;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public Priority getRebuildPriority() {
        return this.rebuildPriority;
    }

    public void setRebuildPriority(final Priority rebuildPriority) {
        this.rebuildPriority = rebuildPriority;
    }


    public VersioningLevel getVersioning() {
        return this.versioning;
    }

    public void setVersioning(final VersioningLevel versioning) {
        this.versioning = versioning;
    }

}