package com.spectralogic.ds3client;

import com.spectralogic.ds3client.networking.Headers;

public class MockedHeaders implements Headers {
    @Override
    public String get(final String key) {
        return null;
    }
}
