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

package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.WritableByteChannel;

public class GetObjectResponse extends AbstractResponse {
    public GetObjectResponse(final WebResponse response, final WritableByteChannel destinationChannel, final int bufferSize) throws IOException {
        super(response);
        download(destinationChannel, bufferSize);
    }

    public Metadata getMetadata() {
        return new MetadataImpl(this.getResponse().getHeaders());
    }

    @Override
    protected void processResponse() throws IOException {
        this.checkStatusCode(200, 206);
    }

    protected void download(final WritableByteChannel destinationChannel, final int bufferSize) throws IOException {
        try (
                final WebResponse response = this.getResponse();
                final InputStream responseStream = response.getResponseStream()) {
            IOUtils.copy(responseStream, destinationChannel, bufferSize);
            destinationChannel.close();
        }
    }
}
