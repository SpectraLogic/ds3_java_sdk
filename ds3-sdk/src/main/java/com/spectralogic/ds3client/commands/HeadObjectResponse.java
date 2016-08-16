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
package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.WebResponse;
import java.io.IOException;
import com.spectralogic.ds3client.commands.interfaces.AbstractResponse;
import com.spectralogic.ds3client.commands.interfaces.MetadataImpl;
import com.spectralogic.ds3client.networking.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeadObjectResponse extends AbstractResponse {

    private static final Logger LOG = LoggerFactory.getLogger(HeadObjectResponse.class);
    private Status status;

    private Metadata metadata;
    private long objectSize;

    public enum Status {
        EXISTS, DOESNTEXIST, UNKNOWN
    }

    public Status getStatus() {
        return this.status;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public long getObjectSize() {
        return this.objectSize;
    }




    public HeadObjectResponse(final WebResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {
        try {
            this.checkStatusCode(200, 404);
            this.metadata = new MetadataImpl(this.getResponse().getHeaders());
            this.objectSize = getSizeFromHeaders(this.getResponse().getHeaders());
            this.setStatus(this.getStatusCode());
        } finally {
            this.getResponse().close();
        }
    }

    private void setStatus(final int statusCode) {
        switch(statusCode) {
            case 200: this.status = Status.EXISTS; break;
            case 404: this.status = Status.DOESNTEXIST; break;
            default: {
                LOG.error("Unexpected status code: {}", Integer.toString(statusCode));
                this.status = Status.UNKNOWN;
                break;
            }
        }
    }


}