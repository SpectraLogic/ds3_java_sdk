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
import com.spectralogic.ds3client.commands.spectrads3.notifications.PutTapePartitionFailureNotificationRegistrationSpectraS3Response;
import com.spectralogic.ds3client.models.TapePartitionFailureNotificationRegistration;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;
import java.io.IOException;
import java.io.InputStream;

public class PutTapePartitionFailureNotificationRegistrationSpectraS3ResponseParser extends AbstractResponseParser<PutTapePartitionFailureNotificationRegistrationSpectraS3Response> {
    private final int[] expectedStatusCodes = new int[]{201};

    @Override
    public PutTapePartitionFailureNotificationRegistrationSpectraS3Response parseXmlResponse(final WebResponse response) throws IOException {
        final int statusCode = response.getStatusCode();
        if (ResponseParserUtils.validateStatusCode(statusCode, expectedStatusCodes)) {
            switch (statusCode) {
            case 201:
                try (final InputStream inputStream = response.getResponseStream()) {
                    final TapePartitionFailureNotificationRegistration result = XmlOutput.fromXml(inputStream, TapePartitionFailureNotificationRegistration.class);
                    return new PutTapePartitionFailureNotificationRegistrationSpectraS3Response(result, this.getChecksum(), this.getChecksumType());
                }

            default:
                assert false: "validateStatusCode should have made it impossible to reach this line";
            }
        }

        throw ResponseParserUtils.createFailedRequest(response, expectedStatusCodes);
    }
}