/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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
package com.spectralogic.ds3client.commands.spectrads3.notifications;

import com.spectralogic.ds3client.networking.WebResponse;
import java.io.IOException;
import com.spectralogic.ds3client.models.S3ObjectPersistedNotificationRegistration;
import java.io.InputStream;
import com.spectralogic.ds3client.serializer.XmlOutput;
import com.spectralogic.ds3client.commands.AbstractResponse;

public class PutObjectPersistedNotificationRegistrationSpectraS3Response extends AbstractResponse {

    private S3ObjectPersistedNotificationRegistration s3ObjectPersistedNotificationRegistrationResult;

    public PutObjectPersistedNotificationRegistrationSpectraS3Response(final WebResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {
        try {
            this.checkStatusCode(201);

            switch (this.getStatusCode()) {
            case 201:
                try (final InputStream content = getResponse().getResponseStream()) {
                    this.s3ObjectPersistedNotificationRegistrationResult = XmlOutput.fromXml(content, S3ObjectPersistedNotificationRegistration.class);
                }
                break;
            default:
                assert false : "checkStatusCode should have made it impossible to reach this line.";
            }
        } finally {
            this.getResponse().close();
        }
    }

    public S3ObjectPersistedNotificationRegistration getS3ObjectPersistedNotificationRegistrationResult() {
        return this.s3ObjectPersistedNotificationRegistrationResult;
    }

}