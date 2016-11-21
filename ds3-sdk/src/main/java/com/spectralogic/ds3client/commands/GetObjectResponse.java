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
package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.WebResponse;
import java.io.IOException;
import com.spectralogic.ds3client.models.Error;
import java.io.InputStream;
import com.spectralogic.ds3client.serializer.XmlOutput;
import com.spectralogic.ds3client.commands.interfaces.AbstractResponse;
import com.spectralogic.ds3client.commands.interfaces.MetadataImpl;
import com.spectralogic.ds3client.networking.Metadata;
import java.nio.channels.WritableByteChannel;
import com.spectralogic.ds3client.utils.IOUtils;
import com.spectralogic.ds3client.utils.PerformanceUtils;
import com.spectralogic.ds3client.exceptions.ContentLengthNotMatchException;

public class GetObjectResponse extends AbstractResponse {

    private Metadata metadata;
    private long objectSize;

    public GetObjectResponse(final WebResponse response, final WritableByteChannel destinationChannel, final int bufferSize, final String objName) throws IOException {
        super(response);
        download(destinationChannel, bufferSize, objName);
    }

    @Override
    protected void processResponse() throws IOException {
        this.checkStatusCode(200, 206, 307);
        this.metadata = new MetadataImpl(this.getResponse().getHeaders());
        this.objectSize = getSizeFromHeaders(this.getResponse().getHeaders());
    }

    protected void download(final WritableByteChannel destinationChannel, final int bufferSize, final String objName) throws IOException {
        try (
                final WebResponse response = this.getResponse();
                final InputStream responseStream = response.getResponseStream()) {
            final long startTime = PerformanceUtils.getCurrentTime();
            final long totalBytes = IOUtils.copy(responseStream, destinationChannel, bufferSize, objName, false);
            destinationChannel.close();
            final long endTime = PerformanceUtils.getCurrentTime();

            if (this.objectSize != -1 && totalBytes != this.objectSize) {
                throw new ContentLengthNotMatchException(objName, objectSize, totalBytes);
            }

            PerformanceUtils.logMbps(startTime, endTime, totalBytes, objName, false);
        }
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public long getObjectSize() {
        return objectSize;
    }
}