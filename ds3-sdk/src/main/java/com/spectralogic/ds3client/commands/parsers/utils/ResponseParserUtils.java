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

package com.spectralogic.ds3client.commands.parsers.utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.Error;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.FailedRequestUsingMgmtPortException;
import com.spectralogic.ds3client.networking.Headers;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;
import com.spectralogic.ds3client.utils.Guard;
import com.spectralogic.ds3client.utils.ResponseUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

public final class ResponseParserUtils {

    private final static Logger LOG = LoggerFactory.getLogger(ResponseParserUtils.class);
    final public static String UTF8 = "UTF-8";

    private ResponseParserUtils() {
        // pass
    }

    public static boolean validateStatusCode(final int statusCode, final int ... expectedStatuses) {
        Preconditions.checkElementIndex(0, 1);
        final ImmutableSet<Integer> expectedSet = createExpectedSet(expectedStatuses);
        return expectedSet.contains(statusCode);
    }

    public static ChecksumType.Type determineChecksumType(final Headers headers) {
        for (final ChecksumType.Type type : ChecksumType.Type.values()) {
            if (getFirstHeaderValue(headers, "content-" + type.toString().replace("_", "").toLowerCase()) != null) {
                return type;
            }
        }
        LOG.debug("Did not find a content checksum header");
        return null;
    }

    public static String getFirstHeaderValue(final Headers headers, final String key) {
        final List<String> valueList = headers.get(key);
        if (Guard.isNullOrEmpty(valueList)) {
            return null;
        } else {
            return valueList.get(0);
        }
    }

    public static long getSizeFromHeaders(final Headers headers) {
        if (headers == null) {
            LOG.debug("Could not get the headers to determine the content-length");
            return -1;
        }
        final List<String> contentLength = headers.get("Content-Length");
        if (Guard.isNullOrEmpty(contentLength) || contentLength.get(0) == null) {
            LOG.debug("Could not find the content-length header to determine the size of the request");
            return -1;
        }
        return Long.parseLong(contentLength.get(0));
    }

    public static boolean checkForManagementPortException(final int statusCode, final Headers headers) {
        return ((statusCode == FailedRequestUsingMgmtPortException.MGMT_PORT_STATUS_CODE)
                && (getFirstHeaderValue(headers, FailedRequestUsingMgmtPortException.MGMT_PORT_HEADER) != null));
    }

    public static Error parseErrorResponse(final String responseString) {
        if (Strings.isNullOrEmpty(responseString)) {
            return null;
        }
        try {
            return XmlOutput.fromXml(responseString, Error.class);
        } catch (final IOException e) {
            // It's likely the response string is not in a valid error format.
            LOG.error("Failed to parse error response: {}", e);
            return null;
        }
    }

    public static ImmutableSet<Integer> createExpectedSet(final int[] expectedStatuses) {
        final ImmutableSet.Builder<Integer> setBuilder = ImmutableSet.builder();
        for(final int status: expectedStatuses) {
            setBuilder.add(status);
        }
        return setBuilder.build();
    }

    public static FailedRequestException createFailedRequest(
            final WebResponse response,
            final int[] expectedStatusCodes) throws IOException {
        if (checkForManagementPortException(response.getStatusCode(), response.getHeaders())) {
            return new FailedRequestUsingMgmtPortException(ResponseUtils.toImmutableIntList(expectedStatusCodes));
        }
        final String responseString = readResponseString(response);
        return new FailedRequestException(
                ResponseUtils.toImmutableIntList(expectedStatusCodes),
                response.getStatusCode(),
                parseErrorResponse(responseString),
                responseString);
    }

    private static String readResponseString(final WebResponse response) throws IOException {
        if (response == null || response.getResponseStream() == null) {
            return "";
        }
        try(final StringWriter writer = new StringWriter();
            final InputStream content = response.getResponseStream()) {
            IOUtils.copy(content, writer, UTF8);
            return writer.toString();
        }
    }
}