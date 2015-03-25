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

package com.spectralogic.ds3client.models.notification;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class NotificationRegistration {
    @JsonProperty("CreationDate")
    private String creationDate;

    @JsonProperty("Id")
    private UUID id;

    @JsonProperty("JobId")
    private UUID jobId;

    @JsonProperty("LastHttpResponseCode")
    private int lastHttpResponseCode;

    @JsonProperty("LastNotification")
    private String lastNotification;

    @JsonProperty("NamingConvention")
    private String namingConvention;

    @JsonProperty("Format")
    private String format;

    @JsonProperty("NotificationEndPoint")
    private String notificationEndPoint;

    @JsonProperty("NotificationHttpMethod")
    private String notificationHttpMethod;

    @JsonProperty("NumberOfFailuresSinceLastSuccess")
    private int numberOfFailuresSinceLastSuccess;

    @JsonProperty("UserId")
    private UUID userId;

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final String creationDate) {
        this.creationDate = creationDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(final UUID jobId) {
        this.jobId = jobId;
    }

    public int getLastHttpResponseCode() {
        return lastHttpResponseCode;
    }

    public void setLastHttpResponseCode(final int lastHttpResponseCode) {
        this.lastHttpResponseCode = lastHttpResponseCode;
    }

    public String getLastNotification() {
        return lastNotification;
    }

    public void setLastNotification(final String lastNotification) {
        this.lastNotification = lastNotification;
    }

    public String getNamingConvention() {
        return namingConvention;
    }

    public void setNamingConvention(final String namingConvention) {
        this.namingConvention = namingConvention;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public String getNotificationEndPoint() {
        return notificationEndPoint;
    }

    public void setNotificationEndPoint(final String notificationEndPoint) {
        this.notificationEndPoint = notificationEndPoint;
    }

    public String getNotificationHttpMethod() {
        return notificationHttpMethod;
    }

    public void setNotificationHttpMethod(final String notificationHttpMethod) {
        this.notificationHttpMethod = notificationHttpMethod;
    }

    public int getNumberOfFailuresSinceLastSuccess() {
        return numberOfFailuresSinceLastSuccess;
    }

    public void setNumberOfFailuresSinceLastSuccess(final int numberOfFailuresSinceLastSuccess) {
        this.numberOfFailuresSinceLastSuccess = numberOfFailuresSinceLastSuccess;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(final UUID userId) {
        this.userId = userId;
    }
}
