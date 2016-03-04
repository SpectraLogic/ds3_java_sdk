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
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

@JacksonXmlRootElement(namespace = "Data")
public class NamedDetailedTapePartitionList {

    // Variables
    @JsonProperty("NamedDetailedTapePartition")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<NamedDetailedTapePartition> namedDetailedTapePartitions = new ArrayList<>();

    // Constructor
    public NamedDetailedTapePartitionList() {
        //pass
    }

    // Getters and Setters
    
    public List<NamedDetailedTapePartition> getNamedDetailedTapePartitions() {
        return this.namedDetailedTapePartitions;
    }

    public void setNamedDetailedTapePartitions(final List<NamedDetailedTapePartition> namedDetailedTapePartitions) {
        this.namedDetailedTapePartitions = namedDetailedTapePartitions;
    }

}