package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.HttpVerb;

public class PutBucketRequest extends AbstractRequest {

    final private String bucket;

    public PutBucketRequest(final String bucket) {
        this.bucket = bucket;
    }

    @Override
    public String getPath() {
        return "/" + bucket;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }
}
