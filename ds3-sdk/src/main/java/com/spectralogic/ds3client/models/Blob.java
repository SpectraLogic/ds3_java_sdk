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
public class Blob {

    // Variables
    @JsonProperty("ByteOffset")
    private long byteOffset;

    @JsonProperty("Checksum")
    private String checksum;

    @JsonProperty("ChecksumType")
    private ChecksumType.Type checksumType;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("Length")
    private long length;

    @JsonProperty("ObjectId")
    private UUID objectId;

    // Constructor
    public Blob() {
        //pass
    }

    // Getters and Setters
    
    public long getByteOffset() {
        return this.byteOffset;
    }

    public void setByteOffset(final long byteOffset) {
        this.byteOffset = byteOffset;
    }


    public String getChecksum() {
        return this.checksum;
    }

    public void setChecksum(final String checksum) {
        this.checksum = checksum;
    }


    public ChecksumType.Type getChecksumType() {
        return this.checksumType;
    }

    public void setChecksumType(final ChecksumType.Type checksumType) {
        this.checksumType = checksumType;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public long getLength() {
        return this.length;
    }

    public void setLength(final long length) {
        this.length = length;
    }


    public UUID getObjectId() {
        return this.objectId;
    }

    public void setObjectId(final UUID objectId) {
        this.objectId = objectId;
    }

}