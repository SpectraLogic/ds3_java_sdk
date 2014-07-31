package com.spectralogic.ds3client;

import com.spectralogic.ds3client.networking.WebResponse;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.io.InputStream;

class WebResponseImpl implements WebResponse {
    private final CloseableHttpResponse response;

    public WebResponseImpl(final CloseableHttpResponse response) {
        this.response = response;
    }
    
    @Override
    public InputStream getResponseStream() throws IOException {
        return this.response.getEntity().getContent();
    }

    @Override
    public int getStatusCode() {
        return this.response.getStatusLine().getStatusCode();
    }

    @Override
    public void close() throws IOException {
        this.response.close();
    }
}
