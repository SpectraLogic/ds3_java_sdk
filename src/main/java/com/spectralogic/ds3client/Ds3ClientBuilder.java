package com.spectralogic.ds3client;

import com.spectralogic.ds3client.models.ConnectionDetails;
import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.networking.NetworkClient;

public class Ds3ClientBuilder {

    final private String endpoint;
    final private Credentials credentials;
    private int port = 0;
    private boolean secure = true;

    public Ds3ClientBuilder(final String endpoint, final Credentials credentials) throws IllegalArgumentException {
        if (endpoint == null || endpoint.isEmpty()) {
            throw new IllegalArgumentException("Endpoint must be non empty");
        }
        if(credentials == null || !credentials.isValid()) {
            throw new IllegalArgumentException("Credentials must be filled out.");
        }
        this.endpoint = endpoint;
        this.credentials = credentials;
    }

    public Ds3ClientBuilder withHttpSecure(final boolean secure) {
        this.secure = secure;
        return this;
    }

    public Ds3ClientBuilder withPort(final int port) throws IllegalArgumentException {
        if (port > Short.MAX_VALUE) {
            throw new IllegalArgumentException("Port must be less than " + Short.MAX_VALUE + ", it was " + port);
        }

        if (port < 3) {
            throw new IllegalArgumentException("Port must be greater than 3");
        }

        this.port = port;
        return this;
    }

    public Ds3Client build() {
        final NetworkClient netClient = new NetworkClient(new ConnectionDetails(endpoint, credentials, getPort(), secure));
        final Ds3Client client = new Ds3Client(netClient);

        return client;
    }

    private int getPort() {
        if(port == 0) {
            return secure ? 443 : 80;
        }
        return port;
    }
}
