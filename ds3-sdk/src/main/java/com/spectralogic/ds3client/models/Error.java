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

package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {
    @JsonProperty("Code")
    private String code;
    
    @JsonProperty("Message")
    private String message;
    
    @JsonProperty("Resource")
    private String resource;
    
    @JsonProperty("RequestId")
    private String requestId;
    
    public String getCode() {
        return code;
    }
    public void setCode(final String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(final String message) {
        this.message = message;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(final String resource) {
        this.resource = resource;
    }
    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "Status Code (" + code + ") Message: " + message;
    }
}
