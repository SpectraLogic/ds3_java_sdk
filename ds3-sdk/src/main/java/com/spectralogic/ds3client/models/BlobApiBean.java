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
import java.lang.Boolean;
import java.lang.String;
import com.spectralogic.ds3client.models.PhysicalPlacementApiBean;

public class BlobApiBean {

    // Variables
    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("InCache")
    private Boolean inCache;

    @JsonProperty("Latest")
    private boolean latest;

    @JsonProperty("Length")
    private long length;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Offset")
    private long offset;

    @JsonProperty("PhysicalPlacement")
    private PhysicalPlacementApiBean physicalPlacement;

    @JsonProperty("Version")
    private long version;

    // Constructor
    public BlobApiBean() {
        //pass
    }

    // Getters and Setters
    
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


    public PhysicalPlacementApiBean getPhysicalPlacement() {
        return this.physicalPlacement;
    }

    public void setPhysicalPlacement(final PhysicalPlacementApiBean physicalPlacement) {
        this.physicalPlacement = physicalPlacement;
    }


    public long getVersion() {
        return this.version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }

}