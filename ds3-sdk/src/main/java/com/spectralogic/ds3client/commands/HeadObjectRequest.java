package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

public class HeadObjectRequest extends AbstractRequest {

    private final String bucketName;
    private final String objectName;

    public HeadObjectRequest(final String bucketName, final String objectName) {
        this.bucketName = bucketName;
        this.objectName = objectName;
    }

    @Override
    public String getPath() {
        return "/" + bucketName + "/" + objectName;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.HEAD;
    }
}
