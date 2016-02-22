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
import com.spectralogic.ds3client.models.Ds3Bucket;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.spectralogic.ds3client.models.User;

@JacksonXmlRootElement(namespace = "ListAllMyBucketsResult")
public class ListAllMyBucketsResult {

    // Variables
    @JsonProperty("Buckets")
    @JacksonXmlElementWrapper(useWrapping = true)
    private List<Ds3Bucket> buckets = new ArrayList<>();

    @JsonProperty("Owner")
    private User owner;

    // Constructor
    public ListAllMyBucketsResult() {
        //pass
    }

    // Getters and Setters
    
    public List<Ds3Bucket> getBuckets() {
        return this.buckets;
    }

    public void setBuckets(final List<Ds3Bucket> buckets) {
        this.buckets = buckets;
    }


    public User getOwner() {
        return this.owner;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }

}