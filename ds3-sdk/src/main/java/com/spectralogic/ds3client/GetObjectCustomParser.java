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

package com.spectralogic.ds3client;

import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.commands.parsers.interfaces.AbstractResponseParser;
import com.spectralogic.ds3client.commands.parsers.interfaces.GetObjectCustomParserParameters;
import com.spectralogic.ds3client.commands.parsers.utils.Function;
import com.spectralogic.ds3client.commands.parsers.utils.ResponseParserUtils;
import com.spectralogic.ds3client.networking.WebResponse;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/**
 * Creates a custom parser which is used in {@link com.spectralogic.ds3client.Ds3Client#getObject(GetObjectRequest, Function)} )}
 */
class GetObjectCustomParser extends AbstractResponseParser<GetObjectResponse> {

    private final int[] expectedStatusCodes = new int[]{200, 206};

    private final WritableByteChannel destinationChannel;
    private final int bufferSize;
    private final String objectName;
    private final Function<GetObjectCustomParserParameters, GetObjectResponse> parsingFunction;

    public GetObjectCustomParser(
            final WritableByteChannel destinationChannel,
            final int bufferSize,
            final String objectName,
            final Function<GetObjectCustomParserParameters, GetObjectResponse> parsingFunction) {

        this.destinationChannel = destinationChannel;
        this.bufferSize = bufferSize;
        this.objectName = objectName;
        this.parsingFunction = parsingFunction;
    }

    @Override
    public GetObjectResponse parseXmlResponse(final WebResponse response) throws IOException {
        final int statusCode = response.getStatusCode();
        if (ResponseParserUtils.validateStatusCode(statusCode, expectedStatusCodes)) {
            final GetObjectCustomParserParameters params = new GetObjectCustomParserParameters(
                    response,
                    destinationChannel,
                    bufferSize,
                    objectName);

            return parsingFunction.apply(params);
        }
        throw ResponseParserUtils.createFailedRequest(response, expectedStatusCodes);
    }
}
