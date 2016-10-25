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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.commands.parsers;

import com.spectralogic.ds3client.commands.interfaces.MetadataImpl;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.commands.HeadObjectResponse;
import com.spectralogic.ds3client.commands.parsers.interfaces.AbstractResponseParser;
import com.spectralogic.ds3client.commands.parsers.utils.ResponseParserUtils;
import com.spectralogic.ds3client.networking.WebResponse;
import java.io.IOException;

import static com.spectralogic.ds3client.commands.parsers.utils.ResponseParserUtils.getSizeFromHeaders;

public class HeadObjectResponseParser extends AbstractResponseParser<HeadObjectResponse> {
    private final int[] expectedStatusCodes = new int[]{200, 404};

    @Override
    public HeadObjectResponse parseXmlResponse(final WebResponse response) throws IOException {
        final int statusCode = response.getStatusCode();
        if (ResponseParserUtils.validateStatusCode(statusCode, expectedStatusCodes)) {
            final Metadata metadata = new MetadataImpl(response.getHeaders());
            final long objectSize = getSizeFromHeaders(response.getHeaders());
            switch (statusCode) {
            case 200:
                return new HeadObjectResponse(metadata, objectSize, HeadObjectResponse.Status.EXISTS, this.getChecksum(), this.getChecksumType());

            case 404:
                return new HeadObjectResponse(metadata, objectSize, HeadObjectResponse.Status.DOESNTEXIST, this.getChecksum(), this.getChecksumType());

            default:
                assert false: "validateStatusCode should have made it impossible to reach this line";
            }
        }

        throw ResponseParserUtils.createFailedRequest(response, expectedStatusCodes);
    }
}