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
import java.lang.String;

@JacksonXmlRootElement(namespace = "Error")
public class HttpError {

    // Variables
    @JsonProperty("Code")
    private String code;

    @JsonProperty("HttpErrorCode")
    private int httpErrorCode;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Resource")
    private String resource;

    @JsonProperty("ResourceId")
    private long resourceId;

    // Constructor
    public HttpError() {
        //pass
    }

    // Getters and Setters
    
    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }


    public int getHttpErrorCode() {
        return this.httpErrorCode;
    }

    public void setHttpErrorCode(final int httpErrorCode) {
        this.httpErrorCode = httpErrorCode;
    }


    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }


    public String getResource() {
        return this.resource;
    }

    public void setResource(final String resource) {
        this.resource = resource;
    }


    public long getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(final long resourceId) {
        this.resourceId = resourceId;
    }

}