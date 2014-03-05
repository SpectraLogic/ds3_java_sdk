package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

import java.io.InputStream;

public class PutObjectRequest extends AbstractRequest {

    private final String bucketName;
    private final String objectName;
    private final InputStream stream;
    private final int size;

    public PutObjectRequest(final String bucketName, final String objectName, final int size, final InputStream stream) {
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.stream = stream;
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
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
