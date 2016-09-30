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
import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class BucketAcl {

    // Variables
    @JsonProperty("BucketId")
    private UUID bucketId;

    @JsonProperty("GroupId")
    private UUID groupId;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("Permission")
    private BucketAclPermission permission;

    @JsonProperty("UserId")
    private UUID userId;

    // Constructor
    public BucketAcl() {
        //pass
    }

    // Getters and Setters
    
    public UUID getBucketId() {
        return this.bucketId;
    }

    public void setBucketId(final UUID bucketId) {
        this.bucketId = bucketId;
    }


    public UUID getGroupId() {
        return this.groupId;
    }

    public void setGroupId(final UUID groupId) {
        this.groupId = groupId;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public BucketAclPermission getPermission() {
        return this.permission;
    }

    public void setPermission(final BucketAclPermission permission) {
        this.permission = permission;
    }


    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(final UUID userId) {
        this.userId = userId;
    }

}