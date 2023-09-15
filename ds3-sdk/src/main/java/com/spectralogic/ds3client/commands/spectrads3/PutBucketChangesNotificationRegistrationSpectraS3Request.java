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
package com.spectralogic.ds3client.commands.spectrads3;

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.spectralogic.ds3client.models.HttpResponseFormatType;
import com.spectralogic.ds3client.models.NamingConventionType;
import com.spectralogic.ds3client.models.RequestType;

public class PutBucketChangesNotificationRegistrationSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String notificationEndPoint;

    private String bucketId;

    private HttpResponseFormatType format;

    private NamingConventionType namingConvention;

    private RequestType notificationHttpMethod;

    // Constructor
    
    
    public PutBucketChangesNotificationRegistrationSpectraS3Request(final String notificationEndPoint) {
        this.notificationEndPoint = notificationEndPoint;
        
        this.updateQueryParam("notification_end_point", notificationEndPoint);

    }

    public PutBucketChangesNotificationRegistrationSpectraS3Request withBucketId(final String bucketId) {
        this.bucketId = bucketId;
        this.updateQueryParam("bucket_id", bucketId);
        return this;
    }


    public PutBucketChangesNotificationRegistrationSpectraS3Request withFormat(final HttpResponseFormatType format) {
        this.format = format;
        this.updateQueryParam("format", format);
        return this;
    }


    public PutBucketChangesNotificationRegistrationSpectraS3Request withNamingConvention(final NamingConventionType namingConvention) {
        this.namingConvention = namingConvention;
        this.updateQueryParam("naming_convention", namingConvention);
        return this;
    }


    public PutBucketChangesNotificationRegistrationSpectraS3Request withNotificationHttpMethod(final RequestType notificationHttpMethod) {
        this.notificationHttpMethod = notificationHttpMethod;
        this.updateQueryParam("notification_http_method", notificationHttpMethod);
        return this;
    }



    @Override
    public HttpVerb getVerb() {
        return HttpVerb.POST;
    }

    @Override
    public String getPath() {
        return "/_rest_/bucket_changes_notification_registration";
    }
    
    public String getNotificationEndPoint() {
        return this.notificationEndPoint;
    }


    public String getBucketId() {
        return this.bucketId;
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