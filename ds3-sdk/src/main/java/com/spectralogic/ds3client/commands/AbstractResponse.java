/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.commands;

import com.google.common.collect.ImmutableSet;
import com.spectralogic.ds3client.models.Error;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

abstract class AbstractResponse implements Ds3Response{
    final static String UTF8 = "UTF-8";

    final private WebResponse response;
    final private String md5;

    AbstractResponse(final WebResponse response) throws IOException {
        this.response = response;
        if (response != null) {
            this.md5 = this.response.getHeaders().get("Content-MD5");
        }
        else {
            this.md5 = null;
        }
        this.processResponse();
    }

    protected abstract void processResponse() throws IOException;

    WebResponse getResponse() {
        return this.response;
    }

    void checkStatusCode(final int ... expectedStatuses) throws IOException {
        final ImmutableSet<Integer> expectedSet = this.createExpectedSet(expectedStatuses);
        final int statusCode = this.getStatusCode();
        if (!expectedSet.contains(statusCode)) {
            final String responseString = this.readResponseString();
            throw new FailedRequestException(
                expectedStatuses,
                statusCode,
                parseErrorResponse(responseString),
                responseString
            );
        }
    }

    public int getStatusCode() {
        return this.response.getStatusCode();
    }

    private ImmutableSet<Integer> createExpectedSet(final int[] expectedStatuses) {
        final ImmutableSet.Builder<Integer> setBuilder = ImmutableSet.builder();
        for(final int status: expectedStatuses) {
            setBuilder.add(status);
        }
        return setBuilder.build();
    }

    private static Error parseErrorResponse(final String responseString) {
        try {
            return XmlOutput.fromXml(responseString, Error.class);
        } catch (final IOException e) {
            // It's likely the response string is not in a valid error format.
            return null;
        }
    }
    
    private String readResponseString() throws IOException {
        try(final StringWriter writer = new StringWriter();
            final InputStream content = this.response.getResponseStream()) {
            IOUtils.copy(content, writer, UTF8);
            return writer.toString();
        }
    }

    public String getMd5() {
        return md5;
    }
}
