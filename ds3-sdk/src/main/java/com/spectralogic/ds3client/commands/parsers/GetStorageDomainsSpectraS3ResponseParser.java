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

import com.spectralogic.ds3client.commands.parsers.interfaces.AbstractResponseParser;
import com.spectralogic.ds3client.commands.parsers.utils.ResponseParserUtils;
import com.spectralogic.ds3client.commands.spectrads3.GetStorageDomainsSpectraS3Response;
import com.spectralogic.ds3client.models.StorageDomainList;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;
import java.io.IOException;
import java.io.InputStream;

public class GetStorageDomainsSpectraS3ResponseParser extends AbstractResponseParser<GetStorageDomainsSpectraS3Response> {
    private final int[] expectedStatusCodes = new int[]{200};

    @Override
    public GetStorageDomainsSpectraS3Response parseXmlResponse(final WebResponse response) throws IOException {
        final int statusCode = response.getStatusCode();
        final Integer pagingTruncated = parseIntHeader("page-truncated");
        final Integer pagingTotalResultCount = parseIntHeader("total-result-count");
        if (ResponseParserUtils.validateStatusCode(statusCode, expectedStatusCodes)) {
            switch (statusCode) {
            case 200:
                try (final InputStream inputStream = response.getResponseStream()) {
                    final StorageDomainList result = XmlOutput.fromXml(inputStream, StorageDomainList.class);
                    return new GetStorageDomainsSpectraS3Response(result, pagingTotalResultCount, pagingTruncated, this.getChecksum(), this.getChecksumType());
                }

            default:
                assert false: "validateStatusCode should have made it impossible to reach this line";
            }
        }

        throw ResponseParserUtils.createFailedRequest(response, expectedStatusCodes);
    }
}