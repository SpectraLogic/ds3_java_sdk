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

package com.spectralogic.ds3client;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.spectralogic.ds3client.commands.interfaces.Ds3Request;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.networking.NetworkClient;
import com.spectralogic.ds3client.networking.WebResponse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockNetwork implements NetworkClient {
    
    private HttpVerb verb;
    private String path;
    private Map<String, String> queryParams;
    private String requestContent;
    private int statusCode;
    private String responseContent;
    private Map<String, String> headers;
    private Multimap <String, String> requestHeaders;
    
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
        mock.requestHeaders = null;
        return mock;
    }
    
    public static MockNetwork expecting(
            final HttpVerb verb,
            final String path,
            final Map<String, String> queryParams,
            final Multimap<String, String> requestHeaders,
            final String requestContent) {
        final MockNetwork mock = new MockNetwork();
        mock.verb = verb;
        mock.path = path;
        mock.queryParams = queryParams;
        mock.requestContent = requestContent;
        mock.requestHeaders = requestHeaders;
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
        return returning(statusCode, responseContent, new HashMap<>());
    }
    
    public Ds3Client asClient() {
        return new Ds3ClientImpl(this);
    }
    
    @Override
    public WebResponse getResponse(final Ds3Request request)
            throws IOException {
        assertThat(request.getVerb(), is(this.verb));
        assertThat(request.getPath(), is(this.path));
        if (this.queryParams != null) {
            this.assertMapsEqual(this.queryParams, request.getQueryParams());
        }
        
        if(this.requestHeaders != null){
        	assertThat(this.requestHeaders.size(), is(request.getHeaders().size()));
        	assertTrue(Iterables.elementsEqual(this.requestHeaders.keySet(), request.getHeaders().keySet()));
        	for (final String key : this.requestHeaders.keySet()){
        		assertThat(this.requestHeaders.get(key), is(notNullValue()));
        		assertThat(request.getHeaders().get(key), is(notNullValue()));
        		assertTrue(Iterables.elementsEqual(this.requestHeaders.get(key), request.getHeaders().get(key)));

        	}
        }
        
        if (this.requestContent != null) {
            final InputStream stream = request.getStream();
            assertThat(stream, is(notNullValue()));
            final String computedStream = IOUtils.toString(stream);
            assertThat(computedStream, is(this.requestContent));
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
        final ConnectionDetails connectionDetails = mock(ConnectionDetails.class);
        when(connectionDetails.getBufferSize()).thenReturn(1024 * 1024);
        return connectionDetails;
    }

    @Override
    public void close() throws IOException {

    }
}
