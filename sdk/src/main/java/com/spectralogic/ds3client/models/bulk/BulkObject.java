package com.spectralogic.ds3client.models.bulk;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class BulkObject {
    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    private String name;
    @JacksonXmlProperty(isAttribute = true, localName = "Length")
    private long length;
    @JacksonXmlProperty(isAttribute = true, localName = "InCache")
    private boolean inCache;
    @JacksonXmlProperty(isAttribute = true, localName = "Offset")
    private long offset;

    public BulkObject() {
    }

    public BulkObject(final String name, final long length, final boolean inCache, final long offset) {
        this.name = name;
        this.length = length;
        this.inCache = inCache;
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getLength() {
        return length;
    }

    public void setLength(final long length) {
        this.length = length;
    }

    public boolean isInCache() {
        return inCache;
    }

    public void setInCache(final boolean inCache) {
        this.inCache = inCache;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(final long offset) {
        this.offset = offset;
    }
}
