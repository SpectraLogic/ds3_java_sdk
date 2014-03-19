package com.spectralogic.ds3client.commands;


import com.spectralogic.ds3client.HttpVerb;

public class GetBucketRequest extends AbstractRequest {

    final private String bucket;
    private String nextMarker = null;
    private String prefix = null;

    public GetBucketRequest(final String bucket) {
        this.bucket = bucket;
    }

    public GetBucketRequest withNextMarker(final String nextMarker) {
        this.nextMarker = nextMarker;
        this.getQueryParams().put("marker", nextMarker);
        return this;
    }

    public GetBucketRequest withPrefix(final String prefix) {
        this.prefix = prefix;
        this.getQueryParams().put("prefix", prefix);
        return this;
    }

    public String getNextMarker() {
        return nextMarker;
    }

    public String getPrefix() {
        return prefix;
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
