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
import java.lang.String;
import com.spectralogic.ds3client.models.JobChunkClientProcessingOrderGuarantee;
import java.util.UUID;
import com.spectralogic.ds3client.models.NodeApiBean;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.spectralogic.ds3client.models.BlobStoreTaskPriority;
import com.spectralogic.ds3client.models.JobRequestType;
import java.util.Date;
import com.spectralogic.ds3client.models.JobStatus;
import com.spectralogic.ds3client.models.WriteOptimization;

public class JobApiBean {

    // Variables
    @JsonProperty("BucketName")
    private String bucketName;

    @JsonProperty("CachedSizeInBytes")
    private long cachedSizeInBytes;

    @JsonProperty("ChunkClientProcessingOrderGuarantee")
    private JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee;

    @JsonProperty("CompletedSizeInBytes")
    private long completedSizeInBytes;

    @JsonProperty("JobId")
    private UUID jobId;

    @JsonProperty("Nodes")
    @JacksonXmlElementWrapper
    private List<NodeApiBean> nodes;

    @JsonProperty("OriginalSizeInBytes")
    private long originalSizeInBytes;

    @JsonProperty("Priority")
    private BlobStoreTaskPriority priority;

    @JsonProperty("RequestType")
    private JobRequestType requestType;

    @JsonProperty("StartDate")
    private Date startDate;

    @JsonProperty("Status")
    private JobStatus status;

    @JsonProperty("UserId")
    private UUID userId;

    @JsonProperty("UserName")
    private String userName;

    @JsonProperty("WriteOptimization")
    private WriteOptimization writeOptimization;

    // Constructor
    public JobApiBean(final String bucketName, final long cachedSizeInBytes, final JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee, final long completedSizeInBytes, final UUID jobId, final List<NodeApiBean> nodes, final long originalSizeInBytes, final BlobStoreTaskPriority priority, final JobRequestType requestType, final Date startDate, final JobStatus status, final UUID userId, final String userName, final WriteOptimization writeOptimization) {
        this.bucketName = bucketName;
        this.cachedSizeInBytes = cachedSizeInBytes;
        this.chunkClientProcessingOrderGuarantee = chunkClientProcessingOrderGuarantee;
        this.completedSizeInBytes = completedSizeInBytes;
        this.jobId = jobId;
        this.nodes = nodes;
        this.originalSizeInBytes = originalSizeInBytes;
        this.priority = priority;
        this.requestType = requestType;
        this.startDate = startDate;
        this.status = status;
        this.userId = userId;
        this.userName = userName;
        this.writeOptimization = writeOptimization;
    }

    // Getters and Setters
    
    public String getBucketName() {
        return this.bucketName;
    }

    public void setBucketName(final String bucketName) {
        this.bucketName = bucketName;
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


    public UUID getJobId() {
        return this.jobId;
    }

    public void setJobId(final UUID jobId) {
        this.jobId = jobId;
    }


    public List<NodeApiBean> getNodes() {
        return this.nodes;
    }

    public void setNodes(final List<NodeApiBean> nodes) {
        this.nodes = nodes;
    }


    public long getOriginalSizeInBytes() {
        return this.originalSizeInBytes;
    }

    public void setOriginalSizeInBytes(final long originalSizeInBytes) {
        this.originalSizeInBytes = originalSizeInBytes;
    }


    public BlobStoreTaskPriority getPriority() {
        return this.priority;
    }

    public void setPriority(final BlobStoreTaskPriority priority) {
        this.priority = priority;
    }


    public JobRequestType getRequestType() {
        return this.requestType;
    }

    public void setRequestType(final JobRequestType requestType) {
        this.requestType = requestType;
    }


    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }


    public JobStatus getStatus() {
        return this.status;
    }

    public void setStatus(final JobStatus status) {
        this.status = status;
    }


    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(final UUID userId) {
        this.userId = userId;
    }


    public String getUserName() {
        return this.userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }


    public WriteOptimization getWriteOptimization() {
        return this.writeOptimization;
    }

    public void setWriteOptimization(final WriteOptimization writeOptimization) {
        this.writeOptimization = writeOptimization;
    }

}