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

package com.spectralogic.ds3client.models.bulk;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class BulkObject {
    @JacksonXmlProperty(isAttribute = true, localName = "Name")
    private String name;
    @JacksonXmlProperty(isAttribute = true, localName = "Length")
    private long length;
    @JacksonXmlProperty(isAttribute = true, localName = "InCache")
    private boolean inCache;
    @JacksonXmlProperty(isAttribute = true, localName = "Offset")
    private long offset;

    public BulkObject() {
    }

    public BulkObject(final String name, final long length, final boolean inCache, final long offset) {
        this.name = name;
        this.length = length;
        this.inCache = inCache;
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getLength() {
        return length;
    }

    public void setLength(final long length) {
        this.length = length;
    }

    public boolean isInCache() {
        return inCache;
    }

    public void setInCache(final boolean inCache) {
        this.inCache = inCache;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(final long offset) {
        this.offset = offset;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, length, offset);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof BulkObject) {
            final BulkObject bulkObject = (BulkObject) obj;

            if (this.getName().equals(bulkObject.getName()) &&
                this.getLength() == bulkObject.getLength() &&
                this.getOffset() == bulkObject.getOffset()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("name = %s, offset = %d, length %d", name, offset, length);
    }
}
