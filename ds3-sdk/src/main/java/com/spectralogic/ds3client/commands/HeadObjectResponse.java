package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.networking.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HeadObjectResponse extends AbstractResponse {

    private static final Logger LOG = LoggerFactory.getLogger(HeadObjectResponse.class);

    private Status status;
    private Metadata metadata;
    private long objectSize;

    public enum Status {
        EXISTS, DOESNTEXIST, NOTAUTHORIZED, UNKNOWN
    }

    public HeadObjectResponse(final WebResponse response) throws IOException {
        super(response);
    }

    public Status getStatus() {
        return this.status;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public long getObjectSize() {
        return this.objectSize;
    }

    @Override
    protected void processResponse() throws IOException {
        try {
            this.checkStatusCode(200, 403, 404);
            this.metadata = new MetadataImpl(this.getResponse().getHeaders());
            this.objectSize = getSizeFromHeaders(this.getResponse().getHeaders());
            this.setStatus(this.getStatusCode());
        } finally {
            this.getResponse().close();
        }
    }

    private void setStatus(final int statusCode) {
        switch(statusCode) {
            case 200: this.status = Status.EXISTS; break;
            case 403: this.status = Status.NOTAUTHORIZED; break;
            case 404: this.status = Status.DOESNTEXIST; break;
            default: {
                LOG.error("Unexpected status code: " + Integer.toString(statusCode));
                this.status = Status.UNKNOWN;
                break;

            }
        }
    }
}
