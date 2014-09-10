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

package com.spectralogic.ds3client;

import com.spectralogic.ds3client.commands.Ds3Request;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.networking.NetworkClient;
import com.spectralogic.ds3client.networking.WebResponse;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MockNetwork implements NetworkClient {
    
    private HttpVerb verb;
    private String path;
    private Map<String, String> queryParams;
    private String requestContent;
    private int statusCode;
    private String responseContent;
    private Map<String, String> headers;
    
    private MockNetwork() {
    }

    public static MockNetwork expecting(
            final HttpVerb verb,
            final String path,
            final Map<String, String> queryParams,
            final String requestContent) {
        final MockNetwork mock = new MockNetwork();
        mock.verb = verb;
        mock.path = path;
        mock.queryParams = queryParams;
        mock.requestContent = requestContent;
        return mock;
    }
    
    public MockNetwork returning(
            final int statusCode,
            final String responseContent,
            final Map<String, String> headers) {
        this.statusCode = statusCode;
        this.responseContent = responseContent;
        this.headers = headers;
        return this;
    }
     
    public MockNetwork returning(
            final int statusCode,
            final String responseContent) {
        return returning(statusCode, responseContent, new HashMap<String, String>());
    }
    
    public Ds3Client asClient() {
        return new Ds3ClientImpl(this);
    }
    
    @Override
    public WebResponse getResponse(final Ds3Request request)
            throws IOException, SignatureException {
        assertThat(request.getVerb(), is(this.verb));
        assertThat(request.getPath(), is(this.path));
        if (this.queryParams != null) {
            this.assertMapsEqual(this.queryParams, request.getQueryParams());
        }
        if (this.requestContent != null) {
            final InputStream stream = request.getStream();
            assertThat(stream, is(notNullValue()));
            assertThat(IOUtils.toString(stream), is(this.requestContent));
        }
        return new MockedWebResponse(this.responseContent, this.statusCode, this.headers);
    }

    private void assertMapsEqual(final Map<String, String> expectedMap, final Map<String, String> actualMap) {
        assertThat(actualMap, is(notNullValue()));
        assertThat(actualMap.size(), is(expectedMap.size()));
        for (final Map.Entry<String, String> entry : expectedMap.entrySet()) {
            assertThat(actualMap.get(entry.getKey()), is(entry.getValue()));
        }
    }

    @Override
    public ConnectionDetails getConnectionDetails() {
        throw new UnsupportedOperationException("Mock network doesn't need to support connection details.");
    }
}
