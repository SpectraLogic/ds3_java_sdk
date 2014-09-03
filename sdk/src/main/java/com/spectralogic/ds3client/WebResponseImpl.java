package com.spectralogic.ds3client;

import com.spectralogic.ds3client.networking.Headers;
import com.spectralogic.ds3client.networking.WebResponse;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.io.InputStream;

class WebResponseImpl implements WebResponse {
    private final CloseableHttpResponse response;
    private final Headers headers;

    public WebResponseImpl(final CloseableHttpResponse response) {
        this.response = response;
        this.headers = new HeadersImpl(this.response.getAllHeaders());
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
    public Headers getHeaders() {
        return this.headers;
    }

    @Override
    public void close() throws IOException {
        this.response.close();
    }
}
