package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.networking.WebResponse;
import com.spectralogic.ds3client.serializer.XmlOutput;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class AllocateJobChunkResponse extends AbstractResponse {
    private Status status;
    private Objects objects;
    private int retryAfterSeconds;

    static public enum Status {
        ALLOCATED, RETRYLATER, NOTFOUND
    }

    public AllocateJobChunkResponse(final WebResponse response) throws IOException {
        super(response);
    }
    
    public Status getStatus() {
        return this.status;
    }

    public Objects getObjects() {
        return this.objects;
    }
    
    public int getRetryAfterSeconds() {
        return this.retryAfterSeconds;
    }

    @Override
    protected void processResponse() throws IOException {
        checkStatusCode(200, 503, 404);
        final WebResponse response = this.getResponse();
        switch (this.getStatusCode()) {
        case 200:
            this.status = Status.ALLOCATED;
            this.objects = parseChunk(response);
            break;
        case 503:
            this.status = Status.RETRYLATER;
            this.retryAfterSeconds = parseRetryAfter(response);
            break;
        case 404:
            this.status = Status.NOTFOUND;
            break;
        default:
            assert false : "checkStatusCode should have made it impossible to reach this line.";
        }
    }

    private static Objects parseChunk(final WebResponse webResponse) throws IOException {
        try (final InputStream content = webResponse.getResponseStream();
             final StringWriter writer = new StringWriter()) {
            IOUtils.copy(content, writer, UTF8);
            return XmlOutput.fromXml(writer.toString(), Objects.class);
        }
    }

    private static int parseRetryAfter(final WebResponse response) {
        return Integer.parseInt(response.getHeaders().get("Retry-After"));
    }
}
