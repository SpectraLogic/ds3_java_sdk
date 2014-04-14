package com.spectralogic.ds3client.networking;

import com.spectralogic.ds3client.models.Credentials;

import java.net.URI;

public interface ConnectionDetails {
    public String getEndpoint();

    public Credentials getCredentials();

    public boolean isSecure();

    public URI getProxy();

    public int getRetries();
}
