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
package com.spectralogic.ds3client.commands.parsers;

import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.commands.interfaces.MetadataImpl;
import com.spectralogic.ds3client.commands.parsers.interfaces.AbstractResponseParser;
import com.spectralogic.ds3client.commands.parsers.utils.ResponseParserUtils;
import com.spectralogic.ds3client.exceptions.ContentLengthNotMatchException;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.utils.IOUtils;
import com.spectralogic.ds3client.utils.PerformanceUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.WritableByteChannel;

import static com.spectralogic.ds3client.commands.parsers.utils.ResponseParserUtils.getSizeFromHeaders;

public class GetObjectResponseParser extends AbstractResponseParser<GetObjectResponse> {
    private final int[] expectedStatusCodes = new int[]{200, 206};

    private final WritableByteChannel destinationChannel;
    private final int bufferSize;
    private final String objName;

    public GetObjectResponseParser(final WritableByteChannel destinationChannel,
                                   final int bufferSize,
                                   final String objName) {
        this.destinationChannel = destinationChannel;
        this.bufferSize = bufferSize;
        this.objName = objName;
    }

    @Override
    public GetObjectResponse parseXmlResponse(final WebResponse response) throws IOException {
        final int statusCode = response.getStatusCode();
        if (ResponseParserUtils.validateStatusCode(statusCode, expectedStatusCodes)) {
            final Metadata metadata = new MetadataImpl(this.getResponse().getHeaders());
            final  long objectSize = getSizeFromHeaders(this.getResponse().getHeaders());
            download(objectSize, this.getResponse());
            return new GetObjectResponse(metadata, objectSize, this.getChecksum(), this.getChecksumType());
        }

        throw ResponseParserUtils.createFailedRequest(response, expectedStatusCodes);
    }

    protected void download(final long objectSize, final WebResponse response) throws IOException {
        try (final InputStream responseStream = response.getResponseStream()) {
            final long startTime = PerformanceUtils.getCurrentTime();
            final long totalBytes = IOUtils.copy(responseStream, destinationChannel, bufferSize, objName, false);
            destinationChannel.close();
            final long endTime = PerformanceUtils.getCurrentTime();

            if (objectSize != -1 && totalBytes != objectSize) {
                throw new ContentLengthNotMatchException(objName, objectSize, totalBytes);
            }

            PerformanceUtils.logMbps(startTime, endTime, totalBytes, objName, false);
        }
    }
}