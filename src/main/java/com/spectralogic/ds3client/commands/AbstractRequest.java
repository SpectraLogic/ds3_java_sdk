package com.spectralogic.ds3client.commands;

import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractRequest implements Ds3Request {

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> queryParams = new HashMap<>();

    @Override
    public ContentType getContentType() {
        return ContentType.APPLICATION_XML;
    }

    @Override
    public InputStream getStream() {
        return null;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public String getMd5() {
        return "";
    }

    @Override
    public final Map<String, String> getQueryParams() {
        return queryParams;
    }

    @Override
    public final Map<String, String> getHeaders() {
        return headers;
    }
}
