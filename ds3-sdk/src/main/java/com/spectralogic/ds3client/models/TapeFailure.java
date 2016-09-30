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
import java.lang.String;

@JacksonXmlRootElement(namespace = "Data")
public class TapeFailure {

    // Variables
    @JsonProperty("Cause")
    private String cause;

    @JsonProperty("Tape")
    private Tape tape;

    // Constructor
    public TapeFailure() {
        //pass
    }

    // Getters and Setters
    
    public String getCause() {
        return this.cause;
    }

    public void setCause(final String cause) {
        this.cause = cause;
    }


    public Tape getTape() {
        return this.tape;
    }

    public void setTape(final Tape tape) {
        this.tape = tape;
    }

}