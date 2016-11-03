/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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
import java.util.Date;
import java.lang.String;
import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class S3TargetFailure {

    // Variables
    @JsonProperty("Date")
    private Date date;

    @JsonProperty("ErrorMessage")
    private String errorMessage;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("TargetId")
    private UUID targetId;

    @JsonProperty("Type")
    private TargetFailureType type;

    // Constructor
    public S3TargetFailure() {
        //pass
    }

    // Getters and Setters
    
    public Date getDate() {
        return this.date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }


    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public UUID getTargetId() {
        return this.targetId;
    }

    public void setTargetId(final UUID targetId) {
        this.targetId = targetId;
    }


    public TargetFailureType getType() {
        return this.type;
    }

    public void setType(final TargetFailureType type) {
        this.type = type;
    }

}