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

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.io.InputStream;

public class GetObjectResponse extends AbstractResponse {

    private InputStream content;

    public GetObjectResponse(CloseableHttpResponse response) throws IOException {
        super(response);
    }

    public InputStream getContent() {
        return content;
    }

    @Override
    protected void processResponse() throws IOException {
        checkStatusCode(200);
        this.content = getResponse().getEntity().getContent();
    }

    @Override
    public void close() throws IOException {
        content.close();
        super.close();
    }
}
