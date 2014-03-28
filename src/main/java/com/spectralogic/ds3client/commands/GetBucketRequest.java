package com.spectralogic.ds3client.commands;


import com.spectralogic.ds3client.HttpVerb;

public class GetBucketRequest extends AbstractRequest {

    final private String bucket;
    private String nextMarker = null;
    private String prefix = null;
    private int maxKeys = 0;

    /**
     * @param bucket The name of the bucket that will have it's objects listed.
     */
    public GetBucketRequest(final String bucket) {
        this.bucket = bucket;
    }

    /**
     * If a GetBucketRequest has been paginated this method is used to get the next set of objects.
     * @param nextMarker The marker specified in {@link com.spectralogic.ds3client.commands.GetBucketResponse#getResult()}
     * @return The current request object.
     */
    public GetBucketRequest withNextMarker(final String nextMarker) {
        this.nextMarker = nextMarker;
        this.getQueryParams().put("marker", nextMarker);
        return this;
    }

    /**
     * Use the prefix method for getting a list of 'directories' without getting the objects within that directory.  For
     * example to get the root level directories the prefix should be set to '/'
     * @param prefix The prefix to filter the objects for.
     * @return The current request object.
     */
    public GetBucketRequest withPrefix(final String prefix) {
        this.prefix = prefix;
        this.getQueryParams().put("prefix", prefix);
        return this;
    }

    /**
     * Limit how many objects will be returned in a request before pagination is enforced.
     * @param maxKeys The number of objects to request in a single request from DS3.
     * @return The current request object.
     */
    public GetBucketRequest withMaxKeys(final int maxKeys) {
        this.maxKeys = maxKeys;
        this.getQueryParams().put("max-keys", Integer.toString(maxKeys));
        return this;
    }

    public String getNextMarker() {
        return nextMarker;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getMaxKeys() {
        return maxKeys;
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
