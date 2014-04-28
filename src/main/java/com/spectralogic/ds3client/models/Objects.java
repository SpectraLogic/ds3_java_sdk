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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Iterator;
import java.util.List;

@JacksonXmlRootElement(localName="objects")
public class Objects implements Iterable<Ds3Object> {

    @JacksonXmlProperty
    private String serverid; //This is not camel cased to match the xml attribute that is returned.

    @JsonProperty("")
    private List<Ds3Object> object;

    public List<Ds3Object> getObject() {
        return object;
    }

    public void setObject(final List<Ds3Object> object) {
        this.object = object;
    }

    public String getServerid() {
        return serverid;
    }

    public void setServerid(final String serverid) {
        this.serverid = serverid;
    }

    public String toString() {
        return object.toString();
    }

    @Override
    public Iterator<Ds3Object> iterator() {
        return object.iterator();
    }
}
