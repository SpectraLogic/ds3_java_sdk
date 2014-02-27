package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.FailedRequestException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.Closeable;
import java.io.IOException;

public abstract class AbstractResponse implements Closeable {
    final static protected String UTF8 = "UTF-8";
    final CloseableHttpResponse response;

    public AbstractResponse(final CloseableHttpResponse response) throws IOException {
        this.response = response;
        processResponse();
    }


    protected abstract void processResponse() throws IOException;

    protected CloseableHttpResponse getResponse() {
        return response;
    }

    protected void checkStatusCode(int expectedStatus) throws FailedRequestException {
        final StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != expectedStatus) {
            throw new FailedRequestException("Request failed with a non-200 status code.  Actual status code: " + statusLine.getStatusCode());
        }
    }

    @Override
    public void close() throws IOException {
        response.close();
    }
}
