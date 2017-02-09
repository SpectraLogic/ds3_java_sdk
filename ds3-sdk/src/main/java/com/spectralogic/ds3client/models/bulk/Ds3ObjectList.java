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

package com.spectralogic.ds3client.models.bulk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.spectralogic.ds3client.models.Priority;
import com.spectralogic.ds3client.models.JobChunkClientProcessingOrderGuarantee;
import com.spectralogic.ds3client.models.WriteOptimization;

@JacksonXmlRootElement(localName = "Objects")
public class Ds3ObjectList {
    @JsonProperty("Object")
    private Iterable<Ds3Object> objects;

    @JacksonXmlProperty(isAttribute = true, namespace = "", localName = "Priority")
    private Priority priority;

    @JacksonXmlProperty(isAttribute = true, namespace = "", localName = "WriteOptimization")
    private WriteOptimization writeOptimization;

    @JacksonXmlProperty(isAttribute = true, namespace = "", localName = "ChunkClientProcessingOrderGuarantee")
    private JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee;

    public Ds3ObjectList() {
    }

    public Ds3ObjectList(final Iterable<Ds3Object> objects) {
        this.objects = objects;
    }

    public Iterable<Ds3Object> getObjects() {
        return objects;
    }

    public void setObjects(final Iterable<Ds3Object> objects) {
        this.objects = objects;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }

    public WriteOptimization getWriteOptimization() {
        return this.writeOptimization;
    }

    public void setWriteOptimization(final WriteOptimization writeOptimization) {
        this.writeOptimization = writeOptimization;
    }

    public JobChunkClientProcessingOrderGuarantee getChunkClientProcessingOrderGuarantee() {
        return this.chunkClientProcessingOrderGuarantee;
    }

    public void setChunkClientProcessingOrderGuarantee(final JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee) {
        this.chunkClientProcessingOrderGuarantee = chunkClientProcessingOrderGuarantee;
    }
}
