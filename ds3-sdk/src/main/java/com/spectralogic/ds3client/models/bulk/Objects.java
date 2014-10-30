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

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Objects implements Iterable<BulkObject> {

    @JsonProperty("Object")
    private List<BulkObject> objects;

    @JsonProperty("ChunkNumber")
    private long chunkNumber;

    @JsonProperty("ChunkId")
    private UUID chunkId;

    @JsonProperty("NodeId")
    private UUID nodeId;
    
    public List<BulkObject> getObjects() {
        return this.objects;
    }

    public void setObjects(final List<BulkObject> object) {
        this.objects = object;
    }

    @Override
    public String toString() {
        return this.objects.toString();
    }

    @Override
    public Iterator<BulkObject> iterator() {
        return this.objects.iterator();
    }

    public long getChunkNumber() {
        return chunkNumber;
    }

    public void setChunkNumber(final long chunkNumber) {
        this.chunkNumber = chunkNumber;
    }

    public UUID getChunkId() {
        return chunkId;
    }

    public void setChunkId(final UUID chunkId) {
        this.chunkId = chunkId;
    }

    public UUID getNodeId() {
        return nodeId;
    }

    public void setNodeId(final UUID nodeId) {
        this.nodeId = nodeId;
    }
}
