/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.models.bulk;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonFilter("sizeFilter")
public class Ds3Object  {
    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    private String name;
    @JacksonXmlProperty(isAttribute = true, localName = "Size")
    private long size;

    /**
     * This constructor is used for XML Serialization.
     * The preferred method is to use one of the other two constructors.
     */
    public Ds3Object() {
        //This constructor is used just for serialization.
    }

    /**
     * Use this constructor when putting files to DS3.
     * @param name The name of the object that will be put to DS3
     * @param size The size of the object that will be put to DS3
     */
    public Ds3Object(final String name, final long size) {
        this.name = name;
        this.size = size;
    }

    /**
     * Use this constructor when getting files from DS3.
     * @param name the name of the object to get from DS3
     */
    public Ds3Object(final String name) {
        this.name = name;
        this.size = 0;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(final long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.size;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.name, this.size);
    }

    @Override
    public boolean equals(final Object obj) {
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
