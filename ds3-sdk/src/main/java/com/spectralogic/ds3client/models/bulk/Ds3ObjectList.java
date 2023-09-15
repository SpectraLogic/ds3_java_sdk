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

package com.spectralogic.ds3client.models.bulk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.spectralogic.ds3client.utils.collections.StreamWrapper;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@JacksonXmlRootElement(localName = "Objects")
public class Ds3ObjectList {
    @JsonProperty("Object")
    private Stream<Ds3Object> objects;

    public Ds3ObjectList() {
    }

    public Ds3ObjectList(final Iterable<Ds3Object> objects) {
        this.objects = StreamSupport.stream(objects.spliterator(), false);
    }

    public Iterable<Ds3Object> getObjects() {
        return StreamWrapper.wrapStream(this.objects);
    }

    public void setObjects(final Iterable<Ds3Object> objects) {
        this.objects = StreamSupport.stream(objects.spliterator(), false);
    }
}
