package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;
import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRequest {

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> queryParams = new HashMap<>();

    public abstract String getPath();
    public abstract HttpVerb getVerb();

    public ContentType getContentType() {
        return ContentType.APPLICATION_XML;
    }

    public InputStream getStream() {
        return null;
    }

    public String getMd5() {
        return "";
    }

    public final Map<String, String> getQueryParams() {
        return queryParams;
    }

    public final Map<String, String> getHeaders() {
        return headers;
    }
}
