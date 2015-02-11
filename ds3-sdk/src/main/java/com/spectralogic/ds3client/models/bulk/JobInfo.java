/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.models.bulk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;
import java.util.UUID;

public class JobInfo {
    @JsonProperty("Nodes")
    @JacksonXmlElementWrapper
    private List<Node> nodes;

    @JsonProperty("CachedSizeInBytes")
    private long cachedSizeInBytes;

    @JsonProperty("CompletedSizeInBytes")
    private long completedSizeInBytes;

    @JsonProperty("OriginalSizeInBytes")
    private long originalSizeInBytes;

    @JsonProperty("BucketName")
    private String bucketName;

    @JsonProperty("JobId")
    private UUID jobId;

    @JsonProperty("UserId")
    private UUID userId;

    @JsonProperty("UserName")
    private String userName;

    @JsonProperty("WriteOptimization")
    private WriteOptimization writeOptimization;

    @JsonProperty("Priority")
    private Priority priority;

    @JsonProperty("RequestType")
    private RequestType requestType;

    @JsonProperty("StartDate")
    private String startDate;

    @JsonProperty("ChunkClientProcessingOrderGuarantee")
    private ChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee;

    @JsonProperty("Status")
    private JobStatus status;

    public UUID getJobId() {
        return this.jobId;
    }

    public void setJobId(final UUID jobId) {
        this.jobId = jobId;
    }

    public ChunkClientProcessingOrderGuarantee getChunkClientProcessingOrderGuarantee() {
        return chunkClientProcessingOrderGuarantee;
    }

    public void setChunkClientProcessingOrderGuarantee(final ChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee) {
        this.chunkClientProcessingOrderGuarantee = chunkClientProcessingOrderGuarantee;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(final List<Node> nodes) {
        this.nodes = nodes;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(final String bucketName) {
        this.bucketName = bucketName;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(final RequestType requestType) {
        this.requestType = requestType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    public long getCachedSizeInBytes() {
        return cachedSizeInBytes;
    }

    public void setCachedSizeInBytes(final long cachedSizeInBytes) {
        this.cachedSizeInBytes = cachedSizeInBytes;
    }

    public long getCompletedSizeInBytes() {
        return completedSizeInBytes;
    }

    public void setCompletedSizeInBytes(final long completedSizeInBytes) {
        this.completedSizeInBytes = completedSizeInBytes;
    }

    public long getOriginalSizeInBytes() {
        return originalSizeInBytes;
    }

    public void setOriginalSizeInBytes(final long originalSizeInBytes) {
        this.originalSizeInBytes = originalSizeInBytes;
    }

    public WriteOptimization getWriteOptimization() {
        return writeOptimization;
    }

    public void setWriteOptimization(final WriteOptimization writeOptimization) {
        this.writeOptimization = writeOptimization;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(final UUID userId) {
        this.userId = userId;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }
}
