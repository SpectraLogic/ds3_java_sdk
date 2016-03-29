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
import com.spectralogic.ds3client.commands.interfaces.AbstractResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;

public class DeleteObjectLostNotificationRegistrationSpectraS3Response extends AbstractResponse {



    public DeleteObjectLostNotificationRegistrationSpectraS3Response(final WebResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {
        try {
            this.checkStatusCode(204);

            switch (this.getStatusCode()) {
            case 204:
                //Do nothing, payload is null
                break;
            default:
                assert false : "checkStatusCode should have made it impossible to reach this line.";
            }
        } finally {
            this.getResponse().close();
        }
    }


}