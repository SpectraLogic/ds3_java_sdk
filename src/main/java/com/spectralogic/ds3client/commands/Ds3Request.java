package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;
import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.util.Map;

public interface Ds3Request {

    public String getPath();
    public HttpVerb getVerb();

    public ContentType getContentType();

    public InputStream getStream();

    public long getSize();

    public String getMd5();

    public Map<String, String> getQueryParams();

    public Map<String, String> getHeaders();
}
