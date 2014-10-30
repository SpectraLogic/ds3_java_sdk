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

import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class AllocateJobChunkResponse extends AbstractResponse {
    private Status status;
    private Objects objects;
    private int retryAfterSeconds;

    static public enum Status {
        ALLOCATED, RETRYLATER
    }

    public AllocateJobChunkResponse(final WebResponse response) throws IOException {
        super(response);
    }
    
    public Status getStatus() {
        return this.status;
    }

    public Objects getObjects() {
        return this.objects;
    }
    
    public int getRetryAfterSeconds() {
        return this.retryAfterSeconds;
    }

    @Override
    protected void processResponse() throws IOException {
        try (final WebResponse response = this.getResponse()) {
            checkStatusCode(200, 503);
            switch (this.getStatusCode()) {
            case 200:
                this.status = Status.ALLOCATED;
                this.objects = parseChunk(response);
                break;
            case 503:
                this.status = Status.RETRYLATER;
                this.retryAfterSeconds = parseRetryAfter(response);
                break;
            default:
                assert false : "checkStatusCode should have made it impossible to reach this line.";
            }
        }
    }

    private static Objects parseChunk(final WebResponse webResponse) throws IOException {
        try (final InputStream content = webResponse.getResponseStream();
             final StringWriter writer = new StringWriter()) {
            IOUtils.copy(content, writer, UTF8);
            return XmlOutput.fromXml(writer.toString(), Objects.class);
        }
    }

    private static int parseRetryAfter(final WebResponse webResponse) {
        final String retryAfter = webResponse.getHeaders().get("Retry-After");
        if (retryAfter == null) {
            throw new RetryAfterExpectedException();
        }
        return Integer.parseInt(retryAfter);
    }
}
