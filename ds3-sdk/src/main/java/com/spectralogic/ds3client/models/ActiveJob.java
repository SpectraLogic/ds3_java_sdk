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
public class ActiveJob {

    // Variables
    @JsonProperty("Aggregating")
    private boolean aggregating;

    @JsonProperty("BucketId")
    private UUID bucketId;

    @JsonProperty("CachedSizeInBytes")
    private long cachedSizeInBytes;

    @JsonProperty("ChunkClientProcessingOrderGuarantee")
    private JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee;

    @JsonProperty("CompletedSizeInBytes")
    private long completedSizeInBytes;

    @JsonProperty("CreatedAt")
    private Date createdAt;

    @JsonProperty("DeadJobCleanupAllowed")
    private boolean deadJobCleanupAllowed;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("ImplicitJobIdResolution")
    private boolean implicitJobIdResolution;

    @JsonProperty("MinimizeSpanningAcrossMedia")
    private boolean minimizeSpanningAcrossMedia;

    @JsonProperty("Naked")
    private boolean naked;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("OriginalSizeInBytes")
    private long originalSizeInBytes;

    @JsonProperty("Priority")
    private Priority priority;

    @JsonProperty("Rechunked")
    private Date rechunked;

    @JsonProperty("Replicating")
    private boolean replicating;

    @JsonProperty("RequestType")
    private JobRequestType requestType;

    @JsonProperty("Restore")
    private JobRestore restore;

    @JsonProperty("Truncated")
    private boolean truncated;

    @JsonProperty("TruncatedDueToTimeout")
    private boolean truncatedDueToTimeout;

    @JsonProperty("UserId")
    private UUID userId;

    @JsonProperty("VerifyAfterWrite")
    private boolean verifyAfterWrite;

    // Constructor
    public ActiveJob() {
        //pass
    }

    // Getters and Setters
    
    public boolean getAggregating() {
        return this.aggregating;
    }

    public void setAggregating(final boolean aggregating) {
        this.aggregating = aggregating;
    }


    public UUID getBucketId() {
        return this.bucketId;
    }

    public void setBucketId(final UUID bucketId) {
        this.bucketId = bucketId;
    }


    public long getCachedSizeInBytes() {
        return this.cachedSizeInBytes;
    }

    public void setCachedSizeInBytes(final long cachedSizeInBytes) {
        this.cachedSizeInBytes = cachedSizeInBytes;
    }


    public JobChunkClientProcessingOrderGuarantee getChunkClientProcessingOrderGuarantee() {
        return this.chunkClientProcessingOrderGuarantee;
    }

    public void setChunkClientProcessingOrderGuarantee(final JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee) {
        this.chunkClientProcessingOrderGuarantee = chunkClientProcessingOrderGuarantee;
    }


    public long getCompletedSizeInBytes() {
        return this.completedSizeInBytes;
    }

    public void setCompletedSizeInBytes(final long completedSizeInBytes) {
        this.completedSizeInBytes = completedSizeInBytes;
    }


    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }


    public boolean getDeadJobCleanupAllowed() {
        return this.deadJobCleanupAllowed;
    }

    public void setDeadJobCleanupAllowed(final boolean deadJobCleanupAllowed) {
        this.deadJobCleanupAllowed = deadJobCleanupAllowed;
    }


    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public boolean getImplicitJobIdResolution() {
        return this.implicitJobIdResolution;
    }

    public void setImplicitJobIdResolution(final boolean implicitJobIdResolution) {
        this.implicitJobIdResolution = implicitJobIdResolution;
    }


    public boolean getMinimizeSpanningAcrossMedia() {
        return this.minimizeSpanningAcrossMedia;
    }

    public void setMinimizeSpanningAcrossMedia(final boolean minimizeSpanningAcrossMedia) {
        this.minimizeSpanningAcrossMedia = minimizeSpanningAcrossMedia;
    }


    public boolean getNaked() {
        return this.naked;
    }

    public void setNaked(final boolean naked) {
        this.naked = naked;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public long getOriginalSizeInBytes() {
        return this.originalSizeInBytes;
    }

    public void setOriginalSizeInBytes(final long originalSizeInBytes) {
        this.originalSizeInBytes = originalSizeInBytes;
    }


    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }


    public Date getRechunked() {
        return this.rechunked;
    }

    public void setRechunked(final Date rechunked) {
        this.rechunked = rechunked;
    }


    public boolean getReplicating() {
        return this.replicating;
    }

    public void setReplicating(final boolean replicating) {
        this.replicating = replicating;
    }


    public JobRequestType getRequestType() {
        return this.requestType;
    }

    public void setRequestType(final JobRequestType requestType) {
        this.requestType = requestType;
    }


    public JobRestore getRestore() {
        return this.restore;
    }

    public void setRestore(final JobRestore restore) {
        this.restore = restore;
    }


    public boolean getTruncated() {
        return this.truncated;
    }

    public void setTruncated(final boolean truncated) {
        this.truncated = truncated;
    }


    public boolean getTruncatedDueToTimeout() {
        return this.truncatedDueToTimeout;
    }

    public void setTruncatedDueToTimeout(final boolean truncatedDueToTimeout) {
        this.truncatedDueToTimeout = truncatedDueToTimeout;
    }


    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(final UUID userId) {
        this.userId = userId;
    }


    public boolean getVerifyAfterWrite() {
        return this.verifyAfterWrite;
    }

    public void setVerifyAfterWrite(final boolean verifyAfterWrite) {
        this.verifyAfterWrite = verifyAfterWrite;
    }

}