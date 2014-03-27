package com.spectralogic.ds3client;

import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.networking.NetworkClient;
import com.spectralogic.ds3client.utils.Builder;

import java.net.URI;
import java.net.URISyntaxException;

public class Ds3ClientBuilder implements Builder<Ds3Client> {

    final private String endpoint;
    final private Credentials credentials;

    private boolean secure = true;
    private URI proxy = null;
    private int retries = 5;

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

    public Ds3ClientBuilder withProxy(final String proxy) throws IllegalArgumentException {
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

    public Ds3ClientBuilder withRedirectRetries(final int retries) {
        this.retries = retries;
        return this;
    }

    public Ds3Client build() {
        final ConnectionDetails.Builder connBuilder = ConnectionDetails.builder(endpoint, credentials)
            .withProxy(proxy).withSecure(secure).withRedirectRetries(retries);

        final NetworkClient netClient = new NetworkClient(connBuilder.build());
        return new Ds3Client(netClient);
    }


}
