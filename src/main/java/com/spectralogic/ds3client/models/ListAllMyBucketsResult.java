/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

public class ListAllMyBucketsResult {

    @JsonProperty("Owner")
    private Owner owner;

    @JsonProperty("Buckets")
    @JacksonXmlElementWrapper(useWrapping = true)
    private List<Bucket> buckets;

    public ListAllMyBucketsResult() {}

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(final Owner owner) {
        this.owner = owner;
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(final List<Bucket> buckets) {
        this.buckets = buckets;
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("{{owner:: ").append(owner.toString()).append("},\n");
        if(buckets != null) {
            builder.append("{buckets:: ").append(buckets.toString()).append("}}");
        }

        return builder.toString();
    }
}
