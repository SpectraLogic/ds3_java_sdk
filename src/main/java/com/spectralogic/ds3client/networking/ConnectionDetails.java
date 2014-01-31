package com.spectralogic.ds3client.networking;

import com.spectralogic.ds3client.models.Credentials;

import java.net.URI;

public class ConnectionDetails {
    private final String endpoint;
    private final Credentials credentials;
    private final int port;
    private final boolean secure;
    private final URI proxy;

    public ConnectionDetails(final String endpoint, final Credentials credentials, final int port, final boolean secure) {
        this(endpoint, credentials, port, secure, null);
    }

    public ConnectionDetails(final String endpoint, final Credentials credentials, final int port, final boolean secure, final URI proxy) {
        this.endpoint = endpoint;
        this.credentials = credentials;
        this.port = port;
        this.secure = secure;
        this.proxy = proxy;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public int getPort() {
        return port;
    }

    public boolean isSecure() {
        return secure;
    }

    public URI getProxy() {
        return proxy;
    }

}
