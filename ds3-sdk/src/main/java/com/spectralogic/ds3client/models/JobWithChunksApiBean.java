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
import java.lang.String;
import com.spectralogic.ds3client.models.JobChunkClientProcessingOrderGuarantee;
import java.util.UUID;
import com.spectralogic.ds3client.models.NodeApiBean;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.spectralogic.ds3client.models.JobChunkApiBean;
import com.spectralogic.ds3client.models.BlobStoreTaskPriority;
import com.spectralogic.ds3client.models.JobRequestType;
import java.util.Date;
import com.spectralogic.ds3client.models.JobStatus;
import com.spectralogic.ds3client.models.WriteOptimization;

@JacksonXmlRootElement(namespace = "MasterObjectList")
public class JobWithChunksApiBean {

    // Variables
    @JacksonXmlProperty(isAttribute = true, localName = "Aggregating")
    private boolean aggregating;

    @JacksonXmlProperty(isAttribute = true, localName = "BucketName")
    private String bucketName;

    @JacksonXmlProperty(isAttribute = true, localName = "CachedSizeInBytes")
    private long cachedSizeInBytes;

    @JacksonXmlProperty(isAttribute = true, localName = "ChunkClientProcessingOrderGuarantee")
    private JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee;

    @JacksonXmlProperty(isAttribute = true, localName = "CompletedSizeInBytes")
    private long completedSizeInBytes;

    @JacksonXmlProperty(isAttribute = true, localName = "JobId")
    private UUID jobId;

    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    private String name;

    @JsonProperty("Nodes")
    @JacksonXmlElementWrapper(useWrapping = true)
    private List<NodeApiBean> nodes = new ArrayList<>();

    @JsonProperty("Objects")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<JobChunkApiBean> objects = new ArrayList<>();

    @JacksonXmlProperty(isAttribute = true, localName = "OriginalSizeInBytes")
    private long originalSizeInBytes;

    @JacksonXmlProperty(isAttribute = true, localName = "Priority")
    private BlobStoreTaskPriority priority;

    @JacksonXmlProperty(isAttribute = true, localName = "RequestType")
    private JobRequestType requestType;

    @JacksonXmlProperty(isAttribute = true, localName = "StartDate")
    private Date startDate;

    @JacksonXmlProperty(isAttribute = true, localName = "Status")
    private JobStatus status;

    @JacksonXmlProperty(isAttribute = true, localName = "UserId")
    private UUID userId;

    @JacksonXmlProperty(isAttribute = true, localName = "UserName")
    private String userName;

    @JacksonXmlProperty(isAttribute = true, localName = "WriteOptimization")
    private WriteOptimization writeOptimization;

    // Constructor
    public JobWithChunksApiBean() {
        //pass
    }

    // Getters and Setters
    
    public boolean getAggregating() {
        return this.aggregating;
    }

    public void setAggregating(final boolean aggregating) {
        this.aggregating = aggregating;
    }


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


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public List<NodeApiBean> getNodes() {
        return this.nodes;
    }

    public void setNodes(final List<NodeApiBean> nodes) {
        this.nodes = nodes;
    }


    public List<JobChunkApiBean> getObjects() {
        return this.objects;
    }

    public void setObjects(final List<JobChunkApiBean> objects) {
        this.objects = objects;
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