package com.spectralogic.ds3client.helpers.pagination;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.FileSystemKey;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.models.common.CommonPrefixes;
import com.spectralogic.ds3client.utils.collections.LazyIterable;

import java.util.List;

public class GetBucketKeyLoaderFactory<T> implements LazyIterable.LazyLoaderFactory<T> {

    private final Ds3Client client;
    private final String bucket;
    private final String keyPrefix;
    private final String nextMarker;
    private final String delimiter;
    private final int maxKeys;
    private final int defaultListObjectsRetries;
    private final Function<ListBucketResult, ImmutableList<T>> function;

    public GetBucketKeyLoaderFactory(final Ds3Client client, final String bucket, final String keyPrefix, final String delimiter, final String nextMarker, final int maxKeys, final int defaultListObjectsRetries, final Function<ListBucketResult, ImmutableList<T>> function) {
        this.client = client;
        this.bucket = bucket;
        this.keyPrefix = keyPrefix;
        this.delimiter = delimiter;
        this.nextMarker = nextMarker;
        this.maxKeys = maxKeys;
        this.defaultListObjectsRetries = defaultListObjectsRetries;
        this.function = function;
    }

    @Override
    public LazyIterable.LazyLoader<T> create() {
        return new GetBucketKeyLoader<>(client, bucket, keyPrefix, delimiter, nextMarker, maxKeys, defaultListObjectsRetries, function);
    }
}
