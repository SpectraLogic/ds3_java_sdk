package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

public class DeleteBucketRequest extends AbstractRequest {

    private final String bucket;
    public DeleteBucketRequest(final String bucket) {
        this.bucket  = bucket;
    }

    @Override
    public String getPath() {
        return "/"+ bucket;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.DELETE;
    }
}
