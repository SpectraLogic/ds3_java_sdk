/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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

import com.spectralogic.ds3client.models.common.Credentials;
import com.spectralogic.ds3client.networking.NetworkClient;
import com.spectralogic.ds3client.networking.NetworkClientImpl;
import com.spectralogic.ds3client.utils.Builder;
import com.spectralogic.ds3client.utils.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A Builder class used to create a Ds3Client instance.  This allows you to customize the behavior of a {@link Ds3Client}.
 * For instance, the number of times that the Ds3Client instance will perform a 307 redirect before throwing an error
 * can be customized with the {@link Ds3ClientBuilder#withRedirectRetries(int)} as well as
 * setting a proxy with {@link Ds3ClientBuilder#withProxy(String)}.
 */
public class Ds3ClientBuilder implements Builder<Ds3Client> {

    static final private Logger LOG = LoggerFactory.getLogger(Ds3ClientBuilder.class);

    static final private String ENDPOINT = "DS3_ENDPOINT";
    static final private String ACCESS_KEY = "DS3_ACCESS_KEY";
    static final private String SECRET_KEY = "DS3_SECRET_KEY";

    final private String endpoint;
    final private Credentials credentials;

    private boolean https = true;
    private boolean certificateVerification = true;
    private URI proxy = null;
    private int retries = 5;
    private int connectionTimeoutInMillis = 5 * 1000;
    private int bufferSizeInBytes = 1024 * 1024;
    private int socketTimeoutInMillis = 1000 * 60 * 60;
    private String userAgent;

    private Ds3ClientBuilder(final String endpoint, final Credentials credentials) throws IllegalArgumentException {
        if (Guard.isStringNullOrEmpty(endpoint)) {
            throw new IllegalArgumentException("Endpoint must be non empty");
        }
        if(credentials == null || !credentials.isValid()) {
            throw new IllegalArgumentException("Credentials must be filled out.");
        }
        this.endpoint = endpoint.trim();
        this.credentials = credentials;
    }

    /**
     * Returns a Builder which is used to customize the behavior of the Ds3Client library.
     * @param endpoint The DS3 endpoint the library should connect to.
     * @param creds The {@link Credentials} used for connecting to a DS3 endpoint.
     * @return The Builder for the {@link Ds3ClientImpl} object.
     */
    public static Ds3ClientBuilder create(final String endpoint, final Credentials creds) {
        return new Ds3ClientBuilder(endpoint, creds);
    }

    /**
     * Returns a Build which already has the endpoint and credentials populated from environment variables.
     * DS3_ENDPOINT, DS3_ACCESS_KEY, and DS3_SECRET_KEY are all used when creating the builder.  This will
     * also detect if http_proxy is set, and if it is will us it when creating the client and set the proxy
     * variable accordingly.
     * @return
     * @throws IllegalArgumentException
     */
    public static Ds3ClientBuilder fromEnv() throws IllegalArgumentException {
        final String endpoint = System.getenv(ENDPOINT);
        if (Guard.isStringNullOrEmpty(endpoint)) {
            throw new IllegalArgumentException("Missing " + ENDPOINT + " environment variable");
        }
        final String accessKey = System.getenv(ACCESS_KEY);
        if (Guard.isStringNullOrEmpty(accessKey)) {
            throw new IllegalArgumentException("Missing " + ACCESS_KEY + " environment variable");
        }
        final String secretKey = System.getenv(SECRET_KEY);
        if (Guard.isStringNullOrEmpty(secretKey)) {
            throw new IllegalArgumentException("Missing " + SECRET_KEY + " environment variable");
        }

        final Ds3ClientBuilder builder = create(endpoint, new Credentials(accessKey, secretKey));

        final String httpProxy = System.getenv("http_proxy");

        if (httpProxy != null) {
            builder.withProxy(httpProxy);
        }
        return builder;
    }

    /**
     * Specifies if the library should use HTTP or HTTPS.  The default is HTTP.
     * @param secure True will use HTTPS, false will use HTTP.
     * @return The current builder.
     */
    public Ds3ClientBuilder withHttps(final boolean secure) {
        this.https = secure;
        return this;
    }

    /**
     * @param bufferSizeInBytes The size of the buffer to be used when writing content out to DS3.
     * @return The current builder.
     */
    public Ds3ClientBuilder withBufferSize(final int bufferSizeInBytes) {
        this.bufferSizeInBytes = bufferSizeInBytes;
        return this;
    }

    /**
     * Specifies if the library should perform SSL certificate validation.
     */
    public Ds3ClientBuilder withCertificateVerification(final boolean certificateVerification) {
        this.certificateVerification = certificateVerification;
        return this;
    }

    /**
     * Sets a HTTP proxy.
     * @param proxy The endpoint of the HTTP proxy.
     * @return The current builder.
     * @throws IllegalArgumentException This will be thrown if the proxy endpoint is not a valid URI.
     */
    public Ds3ClientBuilder withProxy(final String proxy) throws IllegalArgumentException {
        if (proxy == null) {
            LOG.info("Proxy was null");
            return this;
        }
        try {
            final URI proxyUri;
            if(!proxy.startsWith("http")) {
                throw new IllegalArgumentException("Invalid proxy format.  The web address must start with either http or https.");
            }
            proxyUri = new URI(proxy);

            this.proxy = proxyUri;
        } catch (final URISyntaxException e) {
            throw new IllegalArgumentException("Invalid proxy format.  Must be a web address.");
        }

        return this;
    }

    /**
     * Sets the number of retries the library will attempt to perform when it receives 307 redirects from a
     * DS3 appliance.  The default is 5.
     * @param retries The number of times the library should perform retries on 307.
     * @return The current builder.
     */
    public Ds3ClientBuilder withRedirectRetries(final int retries) {
        this.retries = retries;
        return this;
    }

    /**
     * Sets the number of milliseconds to wait for a connection to be established before timing out.
     *
     * Default: 5 minutes
     */
    public Ds3ClientBuilder withConnectionTimeout(final int timeoutInMillis) {
        this.connectionTimeoutInMillis = timeoutInMillis;
        return this;
    }

    /**
     * Sets the number of milliseconds to wait between data packets before timing out a request.
     *
     * Default: 60 minutes
     */
    public Ds3ClientBuilder withSocketTimeout(final int timeoutInMillis) {
        this.socketTimeoutInMillis = timeoutInMillis;
        return this;
    }

    /**
     * The value to send in the http User-Agent header field.
     * @param userAgent If null or empty, the User-Agent header field will contain a default value.
     */
    public Ds3ClientBuilder withUserAgent(final String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    /**
     * Returns a new Ds3Client instance.
     */
    @Override
    public Ds3Client build() {
        LOG.info("Making connection details for endpoint [{}] using this authorization id [{}]",
                this.endpoint, this.credentials.getClientId());
        final ConnectionDetailsImpl.Builder connBuilder = ConnectionDetailsImpl
                .builder(this.endpoint, this.credentials)
                .withProxy(this.proxy)
                .withHttps(this.https)
                .withCertificateVerification(this.certificateVerification)
                .withRedirectRetries(this.retries)
                .withBufferSize(this.bufferSizeInBytes)
                .withConnectionTimeout(this.connectionTimeoutInMillis)
                .withSocketTimeout(this.socketTimeoutInMillis)
                .withUserAgent(this.userAgent);

        final NetworkClient netClient = new NetworkClientImpl(connBuilder.build());
        return new Ds3ClientImpl(netClient);
    }
}
