package com.spectralogic.ds3client.networking;

import java.io.IOException;

public class FailedRequestException extends IOException {
	private static final long serialVersionUID = -2070737734216316074L;
	private final int statusCode;
    public FailedRequestException(final String message, final int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
