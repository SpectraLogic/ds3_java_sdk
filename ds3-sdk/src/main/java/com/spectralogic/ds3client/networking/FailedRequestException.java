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

package com.spectralogic.ds3client.networking;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.models.Error;

import java.io.IOException;
import java.util.List;

public class FailedRequestException extends IOException {
    private static final long serialVersionUID = -2070737734216316074L;
    
    private final int statusCode;
    private final ImmutableList<Integer> expectedStatusCodes;
    private final Error error;
    private final String responseString;

    public FailedRequestException(final ImmutableList<Integer> expectedStatusCodes,
                                  final int statusCode,
                                  final Error error,
                                  final String responseString) {
        super(buildExceptionMessage(error, expectedStatusCodes, statusCode));
        this.statusCode = statusCode;
        this.expectedStatusCodes = expectedStatusCodes;
        this.error = error;
        this.responseString = responseString;
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
    
    private static String buildExceptionMessage(final Error error,
                                                final ImmutableList<Integer> expectedStatusCodes,
                                                final int statusCode) {

        final Joiner joiner = Joiner.on(", ");
        return error == null
            ? String.format(
                "Expected a status code of %s but got %d. Could not parse the response for additional information.",
                joiner.join(expectedStatusCodes),
                statusCode
            )
            : String.format(
                "Expected a status code of %s but got %d. Error message: \"%s\"",
                joiner.join(expectedStatusCodes),
                statusCode,
                error.getMessage()
            );
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
