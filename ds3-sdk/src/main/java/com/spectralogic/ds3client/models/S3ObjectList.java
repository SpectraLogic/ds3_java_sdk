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
import com.spectralogic.ds3client.models.S3Object;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class S3ObjectList {

    // Variables
    @JsonProperty("S3Object")
    @JacksonXmlElementWrapper
    private List<S3Object> s3Object;

    // Constructor
    public S3ObjectList() {
        //pass
    }

    // Getters and Setters
    
    public List<S3Object> getS3Object() {
        return this.s3Object;
    }

    public void setS3Object(final List<S3Object> s3Object) {
        this.s3Object = s3Object;
    }

}