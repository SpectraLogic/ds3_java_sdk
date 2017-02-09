/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.networking;

import org.apache.http.client.methods.CloseableHttpResponse;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

class WebResponseImpl implements WebResponse {
    private final CloseableHttpResponse response;
    private final Headers headers;

    public WebResponseImpl(@Nonnull final CloseableHttpResponse response) {
        this.response = response;
        this.headers = new HeadersImpl(this.response.getAllHeaders());
    }

    @Override
    public InputStream getResponseStream() throws IOException {
        if (this.response.getEntity() == null) {
            return null;
        }
        return this.response.getEntity().getContent();
    }

    @Override
    public int getStatusCode() {
        return this.response.getStatusLine().getStatusCode();
    }

    @Override
    public Headers getHeaders() {
        return this.headers;
    }

    @Override
    public void close() throws IOException {
        this.response.close();
    }
}
