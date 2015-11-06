package com.spectralogic.ds3client.networking;

import java.io.IOException;

public class ResponseProcessingException extends IOException {
    public ResponseProcessingException(final String message) {
        super(message);
    }
}
