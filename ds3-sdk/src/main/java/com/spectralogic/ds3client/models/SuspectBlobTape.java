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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class SuspectBlobTape {

    // Variables
    @JsonProperty("BlobId")
    private UUID blobId;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("ObsoletionId")
    private UUID obsoletionId;

    @JsonProperty("OrderIndex")
    private int orderIndex;

    @JsonProperty("TapeId")
    private UUID tapeId;

    // Constructor
    public SuspectBlobTape() {
        //pass
    }

    // Getters and Setters
    
    public UUID getBlobId() {
        return this.blobId;
    }

    public void setBlobId(final UUID blobId) {
        this.blobId = blobId;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public UUID getObsoletionId() {
        return this.obsoletionId;
    }

    public void setObsoletionId(final UUID obsoletionId) {
        this.obsoletionId = obsoletionId;
    }


    public int getOrderIndex() {
        return this.orderIndex;
    }

    public void setOrderIndex(final int orderIndex) {
        this.orderIndex = orderIndex;
    }


    public UUID getTapeId() {
        return this.tapeId;
    }

    public void setTapeId(final UUID tapeId) {
        this.tapeId = tapeId;
    }

}