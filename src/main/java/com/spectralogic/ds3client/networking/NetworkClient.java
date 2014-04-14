package com.spectralogic.ds3client.networking;

import com.spectralogic.ds3client.commands.AbstractRequest;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.security.SignatureException;

public interface NetworkClient {
    public CloseableHttpResponse getResponse(final AbstractRequest request) throws IOException, SignatureException;
    public ConnectionDetails getConnectionDetails();
}
