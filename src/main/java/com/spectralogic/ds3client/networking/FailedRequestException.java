package com.spectralogic.ds3client.networking;

import java.io.IOException;

public class FailedRequestException extends IOException {
    public FailedRequestException(String message) {
        super(message);
    }
}
