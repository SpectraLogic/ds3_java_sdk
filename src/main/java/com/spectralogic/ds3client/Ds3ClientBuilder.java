package com.spectralogic.ds3client;

import java.net.URI;
import java.net.URISyntaxException;

import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.networking.NetworkClient;

/**
 * A Builder class used to create a Ds3Client instance.
 */
public class Ds3ClientBuilder implements com.spectralogic.ds3client.utils.Builder<Ds3Client> {

    final private String endpoint;
    final private Credentials credentials;

    private boolean secure = true;
    private URI proxy = null;
    private int retries = 5;

    private Ds3ClientBuilder(final String endpoint, final Credentials credentials) throws IllegalArgumentException {
        if (endpoint == null || endpoint.isEmpty()) {
            throw new IllegalArgumentException("Endpoint must be non empty");
        }
        if(credentials == null || !credentials.isValid()) {
            throw new IllegalArgumentException("Credentials must be filled out.");
        }
        this.endpoint = endpoint;
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
     * Specifies if the library should use HTTP or HTTPS.  The default is HTTP.
     * @param secure True will use HTTPS, false will use HTTP.
     * @return The current builder.
     */
    public Ds3ClientBuilder withHttpSecure(final boolean secure) {
        this.secure = secure;
        return this;
    }

    /**
     * Sets a HTTP proxy.
     * @param proxy The endpoint of the HTTP proxy.
     * @return The current builder.
     * @throws IllegalArgumentException This will be thrown if the proxy endpoint is not a valid URI.
     */
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
     * Returns a new Ds3Client instance.
     */
    @Override
    public Ds3Client build() {
        final ConnectionDetailsImpl.Builder connBuilder = ConnectionDetailsImpl.builder(this.endpoint, this.credentials)
            .withProxy(this.proxy).withSecure(this.secure).withRedirectRetries(this.retries);

        final NetworkClient netClient = new NetworkClientImpl(connBuilder.build());
        return new Ds3ClientImpl(netClient);
    }
}
