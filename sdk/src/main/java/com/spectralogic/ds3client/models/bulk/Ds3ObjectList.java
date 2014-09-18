package com.spectralogic.ds3client.models.bulk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "Objects")
public class Ds3ObjectList {
    @JsonProperty("Object")
    private List<Ds3Object> objects;

    @JacksonXmlProperty(isAttribute = true, namespace = "", localName = "Priority")
    private Priority priority;

    @JacksonXmlProperty(isAttribute = true, namespace = "", localName = "WriteOptimization")
    private WriteOptimization writeOptimization;

    @JacksonXmlProperty(isAttribute = true, namespace = "", localName = "ChunkClientProcessingOrderGuarantee")
    private ChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee;

    public Ds3ObjectList() {
    }

    public Ds3ObjectList(final List<Ds3Object> objects) {
        this.objects = objects;
    }

    public List<Ds3Object> getObjects() {
        return objects;
    }

    public void setObjects(final List<Ds3Object> objects) {
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

    public ChunkClientProcessingOrderGuarantee getChunkClientProcessingOrderGuarantee() {
        return this.chunkClientProcessingOrderGuarantee;
    }

    public void setChunkClientProcessingOrderGuarantee(final ChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee) {
        this.chunkClientProcessingOrderGuarantee = chunkClientProcessingOrderGuarantee;
    }
}
