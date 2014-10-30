package com.spectralogic.ds3client;

import com.spectralogic.ds3client.networking.Headers;

import java.util.HashMap;
import java.util.Map;

public class MockedHeaders implements Headers {
    private final Map<String, String> headerValues;

    public MockedHeaders(final Map<String, String> headerValues) {
        this.headerValues = normalizeHeaderValues(headerValues);
    }

    private static Map<String, String> normalizeHeaderValues(final Map<String, String> headerValues) {
        final HashMap<String, String> headers = new HashMap<String, String>();
        for (final Map.Entry<String, String> entry : headerValues.entrySet()) {
            headers.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        return headers;
    }

    @Override
    public String get(final String key) {
        return this.headerValues.get(key.toLowerCase());
    }
}
