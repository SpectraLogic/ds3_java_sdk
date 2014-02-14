package com.spectralogic.ds3client;

import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.networking.NetworkClient;

import java.net.URI;
import java.net.URISyntaxException;

public class Ds3ClientBuilder {

    final private String endpoint;
    final private Credentials credentials;
    private boolean secure = true;
    private URI proxy = null;

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

    public Ds3Client build() {
        final NetworkClient netClient = new NetworkClient(new ConnectionDetails(endpoint, credentials, secure, proxy));
        final Ds3Client client = new Ds3Client(netClient);

        return client;
    }


}
