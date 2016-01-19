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
import com.spectralogic.ds3client.models.S3ObjectToDeleteApiBean;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.spectralogic.ds3client.models.DeleteObjectErrorResultApiBean;

public class DeleteResultApiBean {

    // Variables
    @JsonProperty("DeletedObjects")
    @JacksonXmlElementWrapper
    private List<S3ObjectToDeleteApiBean> deletedObjects;

    @JsonProperty("Errors")
    @JacksonXmlElementWrapper
    private List<DeleteObjectErrorResultApiBean> errors;

    // Constructor
    public DeleteResultApiBean() {
        //pass
    }

    // Getters and Setters
    
    public List<S3ObjectToDeleteApiBean> getDeletedObjects() {
        return this.deletedObjects;
    }

    public void setDeletedObjects(final List<S3ObjectToDeleteApiBean> deletedObjects) {
        this.deletedObjects = deletedObjects;
    }


    public List<DeleteObjectErrorResultApiBean> getErrors() {
        return this.errors;
    }

    public void setErrors(final List<DeleteObjectErrorResultApiBean> errors) {
        this.errors = errors;
    }

}