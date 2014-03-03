package com.spectralogic.ds3client.commands;


import com.spectralogic.ds3client.HttpVerb;

public class GetBucketRequest extends AbstractRequest {

    final private String bucket;

    public GetBucketRequest(final String bucket) {
        this.bucket = bucket;
    }

    @Override
    public String getPath() {
        return "/" + this.bucket;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }
}
