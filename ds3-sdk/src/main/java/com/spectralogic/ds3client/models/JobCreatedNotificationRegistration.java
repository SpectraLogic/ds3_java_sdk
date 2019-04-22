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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.Date;
import java.util.UUID;

@JacksonXmlRootElement(namespace = "Data")
public class JobCreatedNotificationRegistration {

    // Variables
    @JsonProperty("CreationDate")
    private Date creationDate;

    @JsonProperty("Format")
    private HttpResponseFormatType format;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("LastFailure")
    private String lastFailure;

    @JsonProperty("LastHttpResponseCode")
    private Integer lastHttpResponseCode;

    @JsonProperty("LastNotification")
    private Date lastNotification;

    @JsonProperty("NamingConvention")
    private NamingConventionType namingConvention;

    @JsonProperty("NotificationEndPoint")
    private String notificationEndPoint;

    @JsonProperty("NotificationHttpMethod")
    private RequestType notificationHttpMethod;

    @JsonProperty("NumberOfFailuresSinceLastSuccess")
    private int numberOfFailuresSinceLastSuccess;

    @JsonProperty("UserId")
    private UUID userId;

    // Constructor
    public JobCreatedNotificationRegistration() {
        //pass
    }

    // Getters and Setters
    
    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }


    public HttpResponseFormatType getFormat() {
        return this.format;
    }

    public void setFormat(final HttpResponseFormatType format) {
        this.format = format;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }


    public String getLastFailure() {
        return this.lastFailure;
    }

    public void setLastFailure(final String lastFailure) {
        this.lastFailure = lastFailure;
    }


    public Integer getLastHttpResponseCode() {
        return this.lastHttpResponseCode;
    }

    public void setLastHttpResponseCode(final Integer lastHttpResponseCode) {
        this.lastHttpResponseCode = lastHttpResponseCode;
    }


    public Date getLastNotification() {
        return this.lastNotification;
    }

    public void setLastNotification(final Date lastNotification) {
        this.lastNotification = lastNotification;
    }


    public NamingConventionType getNamingConvention() {
        return this.namingConvention;
    }

    public void setNamingConvention(final NamingConventionType namingConvention) {
        this.namingConvention = namingConvention;
    }


    public String getNotificationEndPoint() {
        return this.notificationEndPoint;
    }

    public void setNotificationEndPoint(final String notificationEndPoint) {
        this.notificationEndPoint = notificationEndPoint;
    }


    public RequestType getNotificationHttpMethod() {
        return this.notificationHttpMethod;
    }

    public void setNotificationHttpMethod(final RequestType notificationHttpMethod) {
        this.notificationHttpMethod = notificationHttpMethod;
    }


    public int getNumberOfFailuresSinceLastSuccess() {
        return this.numberOfFailuresSinceLastSuccess;
    }

    public void setNumberOfFailuresSinceLastSuccess(final int numberOfFailuresSinceLastSuccess) {
        this.numberOfFailuresSinceLastSuccess = numberOfFailuresSinceLastSuccess;
    }


    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(final UUID userId) {
        this.userId = userId;
    }

}