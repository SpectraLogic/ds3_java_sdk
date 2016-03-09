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
import java.lang.String;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.UUID;
import java.lang.Boolean;

@JacksonXmlRootElement(namespace = "Data")
public class BulkObject {

    // Variables
    @JacksonXmlProperty(isAttribute = true, localName = "Bucket")
    private String bucket;

    @JsonProperty("Id")
    private UUID id;

    @JacksonXmlProperty(isAttribute = true, localName = "InCache")
    private Boolean inCache;

    @JacksonXmlProperty(isAttribute = true, localName = "Latest")
    private boolean latest;

    @JacksonXmlProperty(isAttribute = true, localName = "Length")
    private long length;

    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    private String name;

    @JacksonXmlProperty(isAttribute = true, localName = "Offset")
    private long offset;

    @JsonProperty("PhysicalPlacement")
    private PhysicalPlacement physicalPlacement;

    @JacksonXmlProperty(isAttribute = true, localName = "Version")
    private long version;

    // Constructor
    public BulkObject() {
        //pass
    }

    // Getters and Setters
    
    public String getBucket() {
        return this.bucket;
    }

    public void setBucket(final String bucket) {
        this.bucket = bucket;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public Boolean getInCache() {
        return this.inCache;
    }

    public void setInCache(final Boolean inCache) {
        this.inCache = inCache;
    }


    public boolean getLatest() {
        return this.latest;
    }

    public void setLatest(final boolean latest) {
        this.latest = latest;
    }


    public long getLength() {
        return this.length;
    }

    public void setLength(final long length) {
        this.length = length;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    public long getOffset() {
        return this.offset;
    }

    public void setOffset(final long offset) {
        this.offset = offset;
    }


    public PhysicalPlacement getPhysicalPlacement() {
        return this.physicalPlacement;
    }

    public void setPhysicalPlacement(final PhysicalPlacement physicalPlacement) {
        this.physicalPlacement = physicalPlacement;
    }


    public long getVersion() {
        return this.version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }


    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, inCache, latest, length, name, offset, physicalPlacement, version);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof BulkObject)) {
            return false;
        }

        final BulkObject bulkObject = (BulkObject) obj;

        return (((this.getId() == null) && (bulkObject.getId() == null))
                || ((this.getId() != null) && (bulkObject.getId() != null) && this.getId().equals(bulkObject.getId())))
                && this.getInCache().equals(bulkObject.getInCache())
                && this.getLatest() == bulkObject.getLatest()
                && this.getLength() == bulkObject.getLength()
                && this.getName().equals(bulkObject.getName())
                && this.getOffset() == bulkObject.getOffset()
                && this.getPhysicalPlacement() == bulkObject.getPhysicalPlacement()
                && this.getVersion() == bulkObject.getVersion();
    }

    @Override
    public String toString() {
        return String.format("name = %s, offset = %d, length %d", name, offset, length);
    }
}