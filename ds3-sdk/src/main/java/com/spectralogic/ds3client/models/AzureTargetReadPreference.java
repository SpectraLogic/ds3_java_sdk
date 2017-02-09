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
public class AzureTargetReadPreference {

    // Variables
    @JsonProperty("BucketId")
    private UUID bucketId;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("ReadPreference")
    private TargetReadPreferenceType readPreference;

    @JsonProperty("TargetId")
    private UUID targetId;

    // Constructor
    public AzureTargetReadPreference() {
        //pass
    }

    // Getters and Setters
    
    public UUID getBucketId() {
        return this.bucketId;
    }

    public void setBucketId(final UUID bucketId) {
        this.bucketId = bucketId;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public TargetReadPreferenceType getReadPreference() {
        return this.readPreference;
    }

    public void setReadPreference(final TargetReadPreferenceType readPreference) {
        this.readPreference = readPreference;
    }


    public UUID getTargetId() {
        return this.targetId;
    }

    public void setTargetId(final UUID targetId) {
        this.targetId = targetId;
    }

}