package com.spectralogic.ds3client;

import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.utils.collections.LazyIterable;

public class GetObjectsLoaderFactory implements LazyIterable.LazyLoaderFactory<Contents> {

    private final Ds3Client client;
    private final String bucket;
    private final String keyPrefix;
    private final String nextMarker;
    private final int maxKeys;
    private final int defaultListObjectsRetries;

    public GetObjectsLoaderFactory(final Ds3Client client, final String bucket, final String keyPrefix, final String nextMarker, final int maxKeys, final int defaultListObjectsRetries) {

        this.client = client;
        this.bucket = bucket;
        this.keyPrefix = keyPrefix;
        this.nextMarker = nextMarker;
        this.maxKeys = maxKeys;
        this.defaultListObjectsRetries = defaultListObjectsRetries;
    }

    @Override
    public LazyIterable.LazyLoader<Contents> create() {
        return new GetObjectsLoader(client, bucket, keyPrefix, nextMarker, maxKeys, defaultListObjectsRetries);
    }
}
