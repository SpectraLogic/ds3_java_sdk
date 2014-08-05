package com.spectralogic.ds3client;

import com.spectralogic.ds3client.networking.Headers;
import org.apache.http.Header;

public class HeadersImpl implements Headers {

    private final Header[] headers;

    HeadersImpl(final Header[] allHeaders){
        this.headers = allHeaders;
    }

    @Override
    public String get(final String key) {
        return findHeader(key);
    }

    private String findHeader(final String key) {

        for (final Header header : headers) {
            if (header.getName().equalsIgnoreCase(key)) {
                return header.getValue();
            }
        }
        return null;
    }
}
