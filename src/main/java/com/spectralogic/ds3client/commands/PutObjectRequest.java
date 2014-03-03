package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

import java.io.InputStream;

public class PutObjectRequest extends AbstractRequest {

    private final String bucketName;
    private final String objectName;
    private final InputStream stream;

    public PutObjectRequest(final String bucketName, final String objectName, final InputStream stream) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.stream = stream;
    }

    @Override
    public String getPath() {
        return "/" + bucketName + "/" + objectName;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public InputStream getStream() {
        return stream;
    }
}
