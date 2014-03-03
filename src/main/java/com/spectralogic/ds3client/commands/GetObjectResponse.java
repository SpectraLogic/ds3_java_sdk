package com.spectralogic.ds3client.commands;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.io.InputStream;

public class GetObjectResponse extends AbstractResponse {

    private InputStream content;

    public GetObjectResponse(CloseableHttpResponse response) throws IOException {
        super(response);
    }

    public InputStream getContent() {
        return content;
    }

    @Override
    protected void processResponse() throws IOException {
        checkStatusCode(200);
        this.content = getResponse().getEntity().getContent();
    }

    @Override
    public void close() throws IOException {
        content.close();
        super.close();
    }
}
