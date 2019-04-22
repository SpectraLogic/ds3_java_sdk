/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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

@JacksonXmlRootElement(namespace = "Data")
public class DeleteObjectError {

    // Variables
    @JsonProperty("Code")
    private String code;

    @JsonProperty("Key")
    private String key;

    @JsonProperty("Message")
    private String message;

    // Constructor
    public DeleteObjectError() {
        //pass
    }

    // Getters and Setters
    
    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }


    public String getKey() {
        return this.key;
    }

    public void setKey(final String key) {
        this.key = key;
    }


    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

}