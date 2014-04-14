package com.spectralogic.ds3client.commands;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.serializer.XmlOutput;
import com.spectralogic.ds3client.models.Error;

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

    protected void checkStatusCode(final int expectedStatus) throws IOException {
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != expectedStatus) {
            final String responseString = readResponseString();
            throw new FailedRequestException(
                expectedStatus,
                statusCode,
                parseErrorResponse(responseString),
                responseString
            );
        }
    }
    
    private static Error parseErrorResponse(final String responseString) {
        try {
            return XmlOutput.fromXml(responseString, Error.class);
        } catch (final IOException e) {
            // It's likely the response string is not in a valid error format.
            return null;
        }
    }
    
    private String readResponseString() throws IOException {
        try(final StringWriter writer = new StringWriter();
            final InputStream content = response.getEntity().getContent()) {
            IOUtils.copy(content, writer, UTF8);
            return writer.toString();
        }
    }

    @Override
    public void close() throws IOException {
        response.close();
    }
}
