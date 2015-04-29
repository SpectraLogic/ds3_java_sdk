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

package com.spectralogic.ds3client;

import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.models.bulk.Node;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.sql.Connection;

class ConnectionDetailsImpl implements ConnectionDetails {
    static private final Logger LOG = LoggerFactory.getLogger(ConnectionDetailsImpl.class);

    static class Builder implements com.spectralogic.ds3client.utils.Builder<ConnectionDetailsImpl> {

        private final String endpoint;
        private final Credentials credentials;
        private boolean https = false;
        private URI proxy = null;
        private int retries = 5;
        private int bufferSize = 1024 * 1024;
        private boolean certificateVerification;

        private Builder(final String endpoint, final Credentials credentials) {
            this.endpoint = endpoint;
            this.credentials = credentials;
        }

        public Builder withHttps(final boolean secure) {
            this.https = secure;
            return this;
        }

        public Builder withProxy(final URI proxy) {
            this.proxy = proxy;
            return this;
        }

        public Builder withRedirectRetries(final int retries) {
            this.retries = retries;
            return this;
        }

        public Builder withBufferSize(final int bufferSize) {
            this.bufferSize = bufferSize;
            return this;
        }

        public Builder withCertificateVerification(final boolean certificateVerification) {
            this.certificateVerification = certificateVerification;
            return this;
        }

        @Override
        public ConnectionDetailsImpl build() {
            return new ConnectionDetailsImpl(this);
        }

    }

    public static ConnectionDetails newForNode(final Node node, final ConnectionDetails connectionDetails) {
        final Builder connectionBuilder;
        if (node.getEndpoint() == null || node.getEndpoint().equals("FAILED_TO_DETERMINE_DATAPATH_IP_ADDRESS")) {
            LOG.trace("Running against an old version of the DS3 API, reusing existing endpoint configuration");
            connectionBuilder = builder(connectionDetails.getEndpoint(), connectionDetails.getCredentials());
        }
        else {
            LOG.trace("Creating new Connection Details for endpoint: " + node.getEndpoint());
            connectionBuilder = builder(buildAuthority(node, connectionDetails), connectionDetails.getCredentials());
        }
        connectionBuilder.withRedirectRetries(connectionDetails.getRetries())
            .withHttps(connectionDetails.isHttps())
            .withCertificateVerification(connectionDetails.isCertificateVerification())
            .withBufferSize(connectionDetails.getBufferSize())
            .withProxy(connectionDetails.getProxy());

        return connectionBuilder.build();
    }

    private static String buildAuthority(final Node node, final ConnectionDetails connectionDetails) {
        return node.getEndpoint() + ":" + Integer.toString(
                (connectionDetails.isHttps() ? node.getHttpsPort() : node.getHttpPort()));
    }

    private final String endpoint;
    private final Credentials credentials;
    private final boolean https;
    private final URI proxy;
    private final int retries;
    private final int bufferSize;
    private final boolean certificateVerification;

    static Builder builder(final String uriEndpoint, final Credentials credentials) {
        return new Builder(uriEndpoint, credentials);
    }

    private ConnectionDetailsImpl(final Builder builder) {
        this.endpoint = builder.endpoint;
        this.credentials = builder.credentials;
        this.https = builder.https;
        this.proxy = builder.proxy;
        this.retries = builder.retries;
        this.bufferSize = builder.bufferSize;
        this.certificateVerification = builder.certificateVerification;
    }

    @Override
    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public boolean isHttps() {
        return https;
    }

    @Override
    public URI getProxy() {
        return proxy;
    }

    @Override
    public int getRetries() {
        return retries;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public boolean isCertificateVerification() {
        return certificateVerification;
    }

    public String toString() {
        return "Endpoint: " + this.endpoint + " | Https?: " + this.https;
    }

}
