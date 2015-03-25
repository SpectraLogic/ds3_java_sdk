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

import com.spectralogic.ds3client.networking.WebResponse;

import java.io.IOException;

public class HeadBucketResponse extends AbstractResponse {

    static public enum Status {
        EXISTS, DOESNTEXIST, NOTAUTHORIZED
    }

    private Status status;

    public HeadBucketResponse(final WebResponse response) throws IOException {
        super(response);
    }

    public Status getStatus() {
        return this.status;
    }

    @Override
    protected void processResponse() throws IOException {
        try {
            this.checkStatusCode(200, 403, 404);
            this.setStatus(this.getStatusCode());
        } finally {
            this.getResponse().close();
        }
    }

    private void setStatus(final int statusCode) {
        switch(statusCode) {
            case 200: this.status = Status.EXISTS; break;
            case 403: this.status = Status.NOTAUTHORIZED; break;
            case 404: this.status = Status.DOESNTEXIST; break;
        }
    }
}
