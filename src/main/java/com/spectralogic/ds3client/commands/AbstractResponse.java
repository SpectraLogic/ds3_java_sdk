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

    protected void checkStatusCode(int expectedStatus) throws FailedRequestException {
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != expectedStatus) {
        	Error error;
	        try(final StringWriter writer = new StringWriter();
	            final InputStream content = response.getEntity().getContent()) {
	            IOUtils.copy(content, writer, UTF8);
	            error = XmlOutput.fromXml(writer.toString(), Error.class);
	        } catch (IOException e) {
	        	error = null;
			}
	        throw new FailedRequestException(expectedStatus, statusCode, error);
        }
    }

    @Override
    public void close() throws IOException {
        response.close();
    }
}
