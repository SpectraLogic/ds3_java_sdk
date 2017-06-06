package com.spectralogic.ds3client.helpers.pagination;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.FileSystemKey;
import com.spectralogic.ds3client.utils.collections.LazyIterable;

public class GetBucketKeyLoaderFactory implements LazyIterable.LazyLoaderFactory<FileSystemKey> {

    private final Ds3Client client;
    private final String bucket;
    private final String keyPrefix;
    private final String nextMarker;
    private final String delimiter;
    private final int maxKeys;
    private final int defaultListObjectsRetries;

    public GetBucketKeyLoaderFactory(final Ds3Client client, final String bucket, final String keyPrefix, final String delimiter, final String nextMarker, final int maxKeys, final int defaultListObjectsRetries) {
        this.client = client;
        this.bucket = bucket;
        this.keyPrefix = keyPrefix;
        this.delimiter = delimiter;
        this.nextMarker = nextMarker;
        this.maxKeys = maxKeys;
        this.defaultListObjectsRetries = defaultListObjectsRetries;
    }

    @Override
    public LazyIterable.LazyLoader<FileSystemKey> create() {
        return new GetBucketKeyLoader(client, bucket, keyPrefix, delimiter, nextMarker, maxKeys, defaultListObjectsRetries);
    }
}
