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

package com.spectralogic.ds3client.models.requestpayloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

/**
 * Represents a request payload that is used by {@link com.spectralogic.ds3client.commands.interfaces.AbstractIdsPayloadRequest}
 * and is used to marshal to xml a list of IDs in proper format
 */
public class Ids {

    @JsonProperty("Id")
    @JacksonXmlElementWrapper( useWrapping = false)
    private List<String> ids;

    public Ids(final List<String> ids) {
        this.ids = ids;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(final List<String> ids) {
        this.ids = ids;
    }
}
