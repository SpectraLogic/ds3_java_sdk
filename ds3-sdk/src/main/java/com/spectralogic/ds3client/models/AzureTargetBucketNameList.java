/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

@JacksonXmlRootElement(namespace = "Data")
public class AzureTargetBucketNameList {

    // Variables
    @JsonProperty("AzureTargetBucketName")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<AzureTargetBucketName> azureTargetBucketNames = new ArrayList<>();

    // Constructor
    public AzureTargetBucketNameList() {
        //pass
    }

    // Getters and Setters
    
    public List<AzureTargetBucketName> getAzureTargetBucketNames() {
        return this.azureTargetBucketNames;
    }

    public void setAzureTargetBucketNames(final List<AzureTargetBucketName> azureTargetBucketNames) {
        this.azureTargetBucketNames = azureTargetBucketNames;
    }

}