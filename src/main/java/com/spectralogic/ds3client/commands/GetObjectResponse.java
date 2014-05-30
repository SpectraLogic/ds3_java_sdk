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

import java.io.IOException;
import java.io.InputStream;

import com.spectralogic.ds3client.networking.WebResponse;

public class GetObjectResponse extends AbstractResponse {

    private InputStream content;

    public GetObjectResponse(final WebResponse response) throws IOException {
        super(response);
    }

    public InputStream getContent() {
        return this.content;
    }

    @Override
    protected void processResponse() throws IOException {
        this.checkStatusCode(200);
        this.content = this.getResponse().getResponseStream();
    }

    @Override
    public void close() throws IOException {
        this.content.close();
        super.close();
    }
}
