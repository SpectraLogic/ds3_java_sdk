package com.spectralogic.ds3client.commands;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

public class PutObjectResponse extends AbstractResponse {
    public PutObjectResponse(CloseableHttpResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {
        checkStatusCode(200);
    }
}
