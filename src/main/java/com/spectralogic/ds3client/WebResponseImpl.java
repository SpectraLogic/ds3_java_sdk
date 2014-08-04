package com.spectralogic.ds3client;

import com.spectralogic.ds3client.networking.WebResponse;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.io.InputStream;

class WebResponseImpl implements WebResponse {
    private final CloseableHttpResponse response;
    private final String md5;

    public WebResponseImpl(final CloseableHttpResponse response) {
        this.response = response;
        this.md5 = getMd5FromResponse();
    }

    private String getMd5FromResponse() {
        final Header header = response.getFirstHeader("Content-MD5");
        if (header == null) {
            return null;
        }
        return header.getValue();
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
    public String getMd5() {
        return this.md5;
    }

    @Override
    public void close() throws IOException {
        this.response.close();
    }
}
