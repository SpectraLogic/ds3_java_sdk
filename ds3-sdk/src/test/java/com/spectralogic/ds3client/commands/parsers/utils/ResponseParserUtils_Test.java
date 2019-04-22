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

package com.spectralogic.ds3client.commands.parsers.utils;

import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.MockedHeaders;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ResponseParserUtils_Test {

    @Test
    public void getRequestIdTest() {
        final String requestId = "123";
        final ImmutableMap<String, String> headers = ImmutableMap.of(
                "x-amz-request-id", requestId,
                "Content-Type", "Text/xml");

        final String result = ResponseParserUtils.getRequestId(new MockedHeaders(headers));
        assertThat(result, is(requestId));
    }

    @Test
    public void getRequestIdWithoutRequestIdTest() {
        final ImmutableMap<String, String> headers = ImmutableMap.of(
                "Content-Type", "Text/xml");

        final String result = ResponseParserUtils.getRequestId(new MockedHeaders(headers));
        assertThat(result, is(nullValue()));
    }

    @Test
    public void getRequestIdEmptyHeadersTest() {
        final String result = ResponseParserUtils.getRequestId(new MockedHeaders(ImmutableMap.of()));
        assertThat(result, is(nullValue()));
    }
}
