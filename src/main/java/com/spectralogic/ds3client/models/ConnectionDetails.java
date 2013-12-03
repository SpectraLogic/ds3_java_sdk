package com.spectralogic.ds3client.models;

public class ConnectionDetails {
    private final String endpoint;
    private final Credentials credentials;
    private final int port;
    private final boolean secure;

    public ConnectionDetails(String endpoint, Credentials credentials, int port, boolean secure) {
        this.endpoint = endpoint;
        this.credentials = credentials;
        this.port = port;
        this.secure = secure;
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
}
