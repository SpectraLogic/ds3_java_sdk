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

package com.spectralogic.ds3client.models.multipart;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Part {

    @JsonProperty("PartNumber")
    private int partNumber;

    @JsonProperty("ETag")
    private String eTag;

    public Part() {
        //Pass
    }

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(final int partNumber) {
        this.partNumber = partNumber;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(final String eTag) {
        this.eTag = eTag;
    }
}
