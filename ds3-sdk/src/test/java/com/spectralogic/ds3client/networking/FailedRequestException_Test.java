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

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.models.Error;
import org.junit.Test;

import static com.spectralogic.ds3client.networking.FailedRequestException.buildExceptionMessage;
import static com.spectralogic.ds3client.networking.FailedRequestException.buildRequestIdMessage;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FailedRequestException_Test {

    private static Error createTestError() {
        final Error error = new Error();
        error.setMessage("Error message");
        return error;
    }

    @Test
    public void buildRequestIdMessageTest() {
        final String result = buildRequestIdMessage("123");
        assertThat(result, is("for request #123"));
    }

    @Test
    public void buildRequestIdMessageEmptyIdTest() {
        final String result = buildRequestIdMessage("");
        assertThat(result, is("for unknown request"));
    }

    @Test
    public void buildRequestIdMessageNullIdTest() {
        final String result = buildRequestIdMessage(null);
        assertThat(result, is("for unknown request"));
    }

    @Test
    public void buildExceptionMessageNullErrorAndRequestIdTest() {
        final String expected = "Expected a status code of 200, 203 but got 400 for unknown request. Could not parse the response for additional information.";
        final String result = buildExceptionMessage(null, ImmutableList.of(200, 203), 400, null);
        assertThat(result, is(expected));
    }

    @Test
    public void buildExceptionMessageNullErrorTest() {
        final String expected = "Expected a status code of 200, 203 but got 400 for request #123. Could not parse the response for additional information.";
        final String result = buildExceptionMessage(null, ImmutableList.of(200, 203), 400, "123");
        assertThat(result, is(expected));
    }

    @Test
    public void buildExceptionMessageNullRequestIdTest() {
        final String expected = "Expected a status code of 200, 203 but got 400 for unknown request. Error message: \"Error message\"";
        final String result = buildExceptionMessage(createTestError(), ImmutableList.of(200, 203), 400, null);
        assertThat(result, is(expected));
    }

    @Test
    public void buildExceptionMessageTest() {
        final String expected = "Expected a status code of 200, 203 but got 400 for request #123. Error message: \"Error message\"";
        final String result = buildExceptionMessage(createTestError(), ImmutableList.of(200, 203), 400, "123");
        assertThat(result, is(expected));
    }
}
