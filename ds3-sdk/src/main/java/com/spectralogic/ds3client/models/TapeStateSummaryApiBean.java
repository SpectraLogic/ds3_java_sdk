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
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

@JacksonXmlRootElement(namespace = "TapeStateSummary")
public class TapeStateSummaryApiBean {

    // Variables
    @JsonProperty("Count")
    private int count;

    @JsonProperty("FullOfData")
    private int fullOfData;

    @JsonProperty("TapeState")
    private TapeState tapeState;

    @JsonProperty("TypeCounts")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<TypeTypeCountApiBean> typeCounts = new ArrayList<>();

    // Constructor
    public TapeStateSummaryApiBean() {
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


    public TapeState getTapeState() {
        return this.tapeState;
    }

    public void setTapeState(final TapeState tapeState) {
        this.tapeState = tapeState;
    }


    public List<TypeTypeCountApiBean> getTypeCounts() {
        return this.typeCounts;
    }

    public void setTypeCounts(final List<TypeTypeCountApiBean> typeCounts) {
        this.typeCounts = typeCounts;
    }

}