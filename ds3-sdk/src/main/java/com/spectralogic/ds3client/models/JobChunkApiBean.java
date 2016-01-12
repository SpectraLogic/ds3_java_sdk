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
import java.util.UUID;
import com.spectralogic.ds3client.models.BlobApiBean;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class JobChunkApiBean {

    // Variables
    @JsonProperty("ChunkId")
    private UUID chunkId;

    @JsonProperty("ChunkNumber")
    private int chunkNumber;

    @JsonProperty("NodeId")
    private UUID nodeId;

    @JsonProperty("Objects")
    @JacksonXmlElementWrapper
    private List<BlobApiBean> objects;

    // Constructor
    public JobChunkApiBean(final UUID chunkId, final int chunkNumber, final UUID nodeId, final List<BlobApiBean> objects) {
        this.chunkId = chunkId;
        this.chunkNumber = chunkNumber;
        this.nodeId = nodeId;
        this.objects = objects;
    }

    // Getters and Setters
    
    public UUID getChunkId() {
        return this.chunkId;
    }

    public void setChunkId(final UUID chunkId) {
        this.chunkId = chunkId;
    }


    public int getChunkNumber() {
        return this.chunkNumber;
    }

    public void setChunkNumber(final int chunkNumber) {
        this.chunkNumber = chunkNumber;
    }


    public UUID getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(final UUID nodeId) {
        this.nodeId = nodeId;
    }


    public List<BlobApiBean> getObjects() {
        return this.objects;
    }

    public void setObjects(final List<BlobApiBean> objects) {
        this.objects = objects;
    }

}