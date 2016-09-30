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
import java.util.UUID;
import java.lang.String;

@JacksonXmlRootElement(namespace = "Data")
public class Group {

    // Variables
    @JsonProperty("BuiltIn")
    private boolean builtIn;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("Name")
    private String name;

    // Constructor
    public Group() {
        //pass
    }

    // Getters and Setters
    
    public boolean getBuiltIn() {
        return this.builtIn;
    }

    public void setBuiltIn(final boolean builtIn) {
        this.builtIn = builtIn;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}