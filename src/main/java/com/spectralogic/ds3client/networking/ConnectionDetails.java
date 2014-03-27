package com.spectralogic.ds3client.networking;

import com.spectralogic.ds3client.models.Credentials;

import java.net.URI;


public class ConnectionDetails {
	
    public static class Builder implements com.spectralogic.ds3client.utils.Builder<ConnectionDetails> {

        private final String endpoint;
        private final Credentials credentials;
        private boolean secure = false;
        private URI proxy = null;
        private int retries = 5;

        public Builder(final String endpoint, final Credentials credentials) {
            this.endpoint = endpoint;
            this.credentials = credentials;
        }

        public Builder withSecure(final boolean secure) {
            this.secure = secure;
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

        @Override
        public ConnectionDetails build() {
            return new ConnectionDetails(this);
        }
    }
	
    private final String endpoint;
    private final Credentials credentials;
    private final boolean secure;
    private final URI proxy;
    private final int retries;

    public static Builder builder(final String uriEndpoint, final Credentials credentials) {
        return new Builder(uriEndpoint, credentials);
    }

    private ConnectionDetails(final Builder builder) {
        this.endpoint = builder.endpoint;
        this.credentials = builder.credentials;
        this.secure = builder.secure;
        this.proxy = builder.proxy;
        this.retries = builder.retries;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public boolean isSecure() {
        return secure;
    }

    public URI getProxy() {
        return proxy;
    }

    public int getRetries() {
    	return retries;
    }

}
