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
package com.spectralogic.ds3client.commands.spectrads3.notifications;

import com.spectralogic.ds3client.commands.interfaces.AbstractCreateNotificationRequest;
import com.spectralogic.ds3client.models.HttpResponseFormatType;
import com.spectralogic.ds3client.models.NamingConventionType;
import com.spectralogic.ds3client.models.RequestType;

public class PutTapePartitionFailureNotificationRegistrationSpectraS3Request extends AbstractCreateNotificationRequest {

    // Variables
    
    private HttpResponseFormatType format;

    private NamingConventionType namingConvention;

    private RequestType notificationHttpMethod;

    public PutTapePartitionFailureNotificationRegistrationSpectraS3Request(final String notificationEndPoint) {
        super(notificationEndPoint);

        

    }

    public PutTapePartitionFailureNotificationRegistrationSpectraS3Request withFormat(final HttpResponseFormatType format) {
        this.format = format;
        this.updateQueryParam("format", format);
        return this;
    }

    public PutTapePartitionFailureNotificationRegistrationSpectraS3Request withNamingConvention(final NamingConventionType namingConvention) {
        this.namingConvention = namingConvention;
        this.updateQueryParam("naming_convention", namingConvention);
        return this;
    }

    public PutTapePartitionFailureNotificationRegistrationSpectraS3Request withNotificationHttpMethod(final RequestType notificationHttpMethod) {
        this.notificationHttpMethod = notificationHttpMethod;
        this.updateQueryParam("notification_http_method", notificationHttpMethod);
        return this;
    }


    @Override
    public String getPath() {
        return "/_rest_/tape_partition_failure_notification_registration";
    }

    
    public HttpResponseFormatType getFormat() {
        return this.format;
    }


    public NamingConventionType getNamingConvention() {
        return this.namingConvention;
    }


    public RequestType getNotificationHttpMethod() {
        return this.notificationHttpMethod;
    }

}