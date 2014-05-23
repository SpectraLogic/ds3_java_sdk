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

import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties("ChunkNumber")
public class Objects implements Iterable<Ds3Object> {

    @JacksonXmlProperty(isAttribute = true, localName = "ServerId")
    private String serverId;

    @JsonProperty("Object")
    private List<Ds3Object> object;
    
    public String getServerId() {
        return this.serverId;
    }

    public void setServerId(final String serverId) {
        this.serverId = serverId;
    }

    public List<Ds3Object> getObject() {
        return this.object;
    }

    public void setObject(final List<Ds3Object> object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return this.object.toString();
    }

    @Override
    public Iterator<Ds3Object> iterator() {
        return this.object.iterator();
    }
}
