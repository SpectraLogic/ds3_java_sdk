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

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonFilter("sizeFilter")
public class Ds3Object  {
    @JacksonXmlProperty(isAttribute=true)
    private String name;
    @JacksonXmlProperty(isAttribute=true)
    private long size;

    public Ds3Object() {

    }

    public Ds3Object(final String name, long size) {

        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(final long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return name + ":" + size;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ds3Object) {
            final Ds3Object ds3Obj = (Ds3Object) obj;
            if (ds3Obj.getName().equals(this.getName()) &&
                    ds3Obj.getSize() == this.getSize()) {
                return true;
            }
        }
        return false;
    }
}
