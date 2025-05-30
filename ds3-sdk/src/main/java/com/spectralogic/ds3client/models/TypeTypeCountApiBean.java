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

@JacksonXmlRootElement(namespace = "TypeTypeCount")
public class TypeTypeCountApiBean {

    // Variables
    @JsonProperty("Count")
    private int count;

    @JsonProperty("FullOfData")
    private int fullOfData;

    @JsonProperty("Type")
    private String type;

    // Constructor
    public TypeTypeCountApiBean() {
        //pass
    }

    // Getters and Setters
    
    public int getCount() {
        return this.count;
    }

    public void setCount(final int count) {
        this.count = count;
    }


    public int getFullOfData() {
        return this.fullOfData;
    }

    public void setFullOfData(final int fullOfData) {
        this.fullOfData = fullOfData;
    }


    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

}