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
import com.spectralogic.ds3client.models.SignatureDetails;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.networking.NetUtils;
import com.spectralogic.ds3client.networking.NetworkClient;
import com.spectralogic.ds3client.utils.DateFormatter;
import com.spectralogic.ds3client.utils.Signature;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.SignatureException;
import java.util.Map;

class NetworkClientImpl implements NetworkClient {
    final static private String HOST = "HOST";
    final static private String DATE = "DATE";
    final static private String AUTHORIZATION = "Authorization";
    final static private String CONTENT_TYPE = "Content-Type";

    final private ConnectionDetails connectionDetails;

    NetworkClientImpl(final ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public ConnectionDetails getConnectionDetails() {
        return connectionDetails;
    }

    public CloseableHttpResponse getResponse(final Ds3Request request) throws IOException, SignatureException {
        final HttpHost host = getHost(connectionDetails);
        final HttpRequest httpRequest = getHttpRequest(request);
        final String date = DateFormatter.dateToRfc882();

        final CloseableHttpClient httpClient = HttpClients.createDefault();

        httpRequest.addHeader(HOST, NetUtils.buildHostField(connectionDetails));
        httpRequest.addHeader(DATE, date);
        httpRequest.addHeader(CONTENT_TYPE, request.getContentType().toString());
        for(final Map.Entry<String, String> header: request.getHeaders().entrySet()) {
            httpRequest.addHeader(header.getKey(), header.getValue());
        }

        final SignatureDetails sigDetails = new SignatureDetails(request.getVerb(), request.getMd5(), request.getContentType().toString(), date, "", request.getPath(),connectionDetails.getCredentials());
        httpRequest.addHeader(AUTHORIZATION, getSignature(sigDetails));

        return httpClient.execute(host, httpRequest, getContext());
    }

    private String getSignature(final SignatureDetails details) throws SignatureException {
        return "AWS " + connectionDetails.getCredentials().getClientId() + ':' + Signature.signature(details);
    }

    private HttpHost getHost(final ConnectionDetails connectionDetails) throws MalformedURLException {
        final URI proxyUri = connectionDetails.getProxy();
        if(proxyUri != null) {
            return new HttpHost(proxyUri.getHost(), proxyUri.getPort(), proxyUri.getScheme());
        }

        final URL url = NetUtils.buildUrl(connectionDetails, "/");
        final int port = getPort(url);
        return new HttpHost(url.getHost(), port, url.getProtocol());
    }

    private HttpClientContext getContext() {
        final HttpClientContext context = new HttpClientContext();
        final RequestConfig config = RequestConfig.custom().setCircularRedirectsAllowed(true).setMaxRedirects(connectionDetails.getRetries()).build();

        context.setRequestConfig(config);
        return context;
    }

    private int getPort(final URL url) {
        final int port = url.getPort();
        if(port < 0) {
            return 80;
        }
        return port;
    }

    private HttpRequest getHttpRequest(final Ds3Request request) {
        final String verb = request.getVerb().toString();
        final InputStream stream = request.getStream();
        final Map<String, String> queryParams = request.getQueryParams();
        final String path;

        if(queryParams.isEmpty()) {
            path = request.getPath();
        }
        else {
            path = request.getPath() + "?" + NetUtils.buildQueryString(queryParams);
        }

        if(stream != null) {
            final HttpEntity entity = new InputStreamEntity(stream, request.getSize(), request.getContentType());
            final BasicHttpEntityEnclosingRequest httpRequest = new BasicHttpEntityEnclosingRequest(verb, path);
            httpRequest.setEntity(entity);
            return httpRequest;
        }

        return new BasicHttpRequest(verb, path);
    }
}
