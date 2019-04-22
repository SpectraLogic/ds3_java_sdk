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

package com.spectralogic.ds3client.networking;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.models.Error;
import com.spectralogic.ds3client.utils.Guard;

import java.io.IOException;
import java.util.List;

public class FailedRequestException extends IOException {
    private static final long serialVersionUID = -2070737734216316074L;
    
    private final int statusCode;
    private final ImmutableList<Integer> expectedStatusCodes;
    private final Error error;
    private final String responseString;
    private final String requestId;

    public FailedRequestException(final int[] expectedStatusCodes,
                                  final int statusCode,
                                  final Error error,
                                  final String responseString,
                                  final String requestId) {
        this(toList(expectedStatusCodes), statusCode, error, responseString, requestId);
    }

    private static ImmutableList<Integer> toList(final int[] expectedStatusCodes) {
        final ImmutableList.Builder<Integer> builder = ImmutableList.builder();

        for (final int expectedStatusCode : expectedStatusCodes) {
            builder.add(expectedStatusCode);
        }
        return builder.build();
    }

    public FailedRequestException(final ImmutableList<Integer> expectedStatusCodes,
                                  final int statusCode,
                                  final Error error,
                                  final String responseString,
                                  final String requestId) {
        super(buildExceptionMessage(error, expectedStatusCodes, statusCode, requestId));
        this.statusCode = statusCode;
        this.expectedStatusCodes = expectedStatusCodes;
        this.error = error;
        this.responseString = responseString;
        this.requestId = requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<Integer> getExpectedStatusCodes() {

        final ImmutableList.Builder<Integer> builder = ImmutableList.builder();
        for (final int status : expectedStatusCodes) {
            builder.add(status);
        }

        return builder.build();
    }
    public Error getError() {
        return error;
    }

    public String getResponseString() {
        return responseString;
    }

    public String getRequestId() {
        return requestId;
    }
    
    protected static String buildExceptionMessage(final Error error,
                                                final ImmutableList<Integer> expectedStatusCodes,
                                                final int statusCode,
                                                final String requestId) {

        final Joiner joiner = Joiner.on(", ");
        return error == null
            ? String.format(
                "Expected a status code of %s but got %d %s. Could not parse the response for additional information.",
                joiner.join(expectedStatusCodes),
                statusCode,
                buildRequestIdMessage(requestId)
            )
            : String.format(
                "Expected a status code of %s but got %d %s. Error message: \"%s\"",
                joiner.join(expectedStatusCodes),
                statusCode,
                buildRequestIdMessage(requestId),
                error.getMessage()
            );
    }

    /**
     * Creates the request-ID portion of the exception message. The message specifies the
     * request-ID if requestId is non-null, else it specifies an unknown request.
     */
    protected static String buildRequestIdMessage(final String requestId) {
        if (Guard.isStringNullOrEmpty(requestId)) {
            return "for unknown request";
        }
        return "for request #" + requestId;
    }

    @Override
    public String toString() {
        if(error == null) {
            return responseString;
        }
        else {
            return error.toString();
        }
    }
}
