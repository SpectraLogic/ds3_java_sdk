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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.Date;
import java.lang.String;
import com.spectralogic.ds3client.models.UserApiBean;
import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class MultiPartUpload {

    // Variables
    @JsonProperty("Initiated")
    private Date initiated;

    @JsonProperty("Key")
    private String key;

    @JsonProperty("Owner")
    private UserApiBean owner;

    @JsonProperty("UploadId")
    private UUID uploadId;

    // Constructor
    public MultiPartUpload() {
        //pass
    }

    // Getters and Setters
    
    public Date getInitiated() {
        return this.initiated;
    }

    public void setInitiated(final Date initiated) {
        this.initiated = initiated;
    }


    public String getKey() {
        return this.key;
    }

    public void setKey(final String key) {
        this.key = key;
    }


    public UserApiBean getOwner() {
        return this.owner;
    }

    public void setOwner(final UserApiBean owner) {
        this.owner = owner;
    }


    public UUID getUploadId() {
        return this.uploadId;
    }

    public void setUploadId(final UUID uploadId) {
        this.uploadId = uploadId;
    }

}