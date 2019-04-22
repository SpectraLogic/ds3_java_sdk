/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.networking.Headers;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.spectralogic.ds3client.commands.parsers.utils.ResponseParserUtils.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class ResponseParserUtils_Test {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseParserUtils_Test.class);

    @Test
    public void getRequestIdTest() {
        final String requestId = "123";
        final ImmutableMap<String, String> headers = ImmutableMap.of(
                "x-amz-request-id", requestId,
                "Content-Type", "Text/xml");

        final String result = getRequestId(new MockedHeaders(headers));
        assertThat(result, is(requestId));
    }

    @Test
    public void getRequestIdWithoutRequestIdTest() {
        final ImmutableMap<String, String> headers = ImmutableMap.of(
                "Content-Type", "Text/xml");

        final String result = getRequestId(new MockedHeaders(headers));
        assertThat(result, is(nullValue()));
    }

    @Test
    public void getRequestIdEmptyHeadersTest() {
        final String result = getRequestId(new MockedHeaders(ImmutableMap.of()));
        assertThat(result, is(nullValue()));
    }

    @Test
    public void getBlobChecksumMapNoHeadersTest() {
        final ImmutableMap<Long, String> result = getBlobChecksumMap(new MockedHeaders(ImmutableMap.of()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void getBlobChecksumMapNoChecksumHeadersTest() {
        final ImmutableMap<String, String> headers = ImmutableMap.of("key1", "value1", "key2", "value2");
        final ImmutableMap<Long, String> result = getBlobChecksumMap(new MockedHeaders(headers));
        assertThat(result.size(), is(0));
    }

    private static final ImmutableMap<String, String> TEST_RESPONSE_HEADERS = ImmutableMap.of(
            "ds3-blob-checksum-type", "MD5",
            "ds3-blob-checksum-offset-0", "4nQGNX4nyz0pi8Hvap79PQ==",
            "ds3-blob-checksum-offset-10485760", "965Aa0/n8DlO1IwXYFh4bg==",
            "ds3-blob-checksum-offset-20971520", "iV2OqJaXJ/jmqgRSb1HmFA==",
            "x-amz-request-id", "895767"
    );

    @Test
    public void getBlobChecksumMapWithChecksumHeadersTest() {
        final ImmutableMap<Long, String> expectedMap = ImmutableMap.of(
                0L, "4nQGNX4nyz0pi8Hvap79PQ==",
                10485760L, "965Aa0/n8DlO1IwXYFh4bg==",
                20971520L, "iV2OqJaXJ/jmqgRSb1HmFA=="
        );

        final ImmutableMap<Long, String> result = getBlobChecksumMap(new MockedHeaders(TEST_RESPONSE_HEADERS));
        assertThat(result.size(), is(expectedMap.size()));

        for (final Map.Entry<Long, String> entry : expectedMap.entrySet()) {
            assertTrue(result.containsKey(entry.getKey()));
            assertThat(result.get(entry.getKey()), is(entry.getValue()));
        }
    }

    @Test
    public void toBlobChecksumTypeTest() {
        assertThat(toBlobChecksumType("CRC_32"), is(ChecksumType.Type.CRC_32));
        assertThat(toBlobChecksumType("CRC_32C"), is(ChecksumType.Type.CRC_32C));
        assertThat(toBlobChecksumType("MD5"), is(ChecksumType.Type.MD5));
        assertThat(toBlobChecksumType("SHA_256"), is(ChecksumType.Type.SHA_256));
        assertThat(toBlobChecksumType("SHA_512"), is(ChecksumType.Type.SHA_512));
        assertThat(toBlobChecksumType(""), is(ChecksumType.Type.NONE));
    }

    @Test (expected = IllegalArgumentException.class)
    public void toBlobChecksumTypeUnknownChecksumType() {
        assertThat(toBlobChecksumType("unexpected text"), is(ChecksumType.Type.NONE));
    }

    @Test
    public void getBlobChecksumTypeNoHeaderTest() {
        final ChecksumType.Type result = getBlobChecksumType(new MockedHeaders(ImmutableMap.of()));
        assertThat(result, is(ChecksumType.Type.NONE));
    }

    @Test
    public void getBlobChecksumTypeTest() {
        final ChecksumType.Type result = getBlobChecksumType(new MockedHeaders(TEST_RESPONSE_HEADERS));
        assertThat(result, is(ChecksumType.Type.MD5));
    }
}
