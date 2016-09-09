package com.spectralogic.ds3client.helpers.pagination;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.DetailedS3Object;
import com.spectralogic.ds3client.utils.collections.LazyIterable;

public class GetObjectsFullDetailsLoaderFactory implements LazyIterable.LazyLoaderFactory<DetailedS3Object> {
    private final Ds3Client client;
    private final String bucket;
    private final String folder;
    private final int pageLength;
    private final int retryCount;
    private final boolean includePhysicalDetails;

    public GetObjectsFullDetailsLoaderFactory(final Ds3Client client, final String bucket, final String folder, final int pageLength, final int retryCount, final boolean includePhysicalDetails) {
        this.client = client;
        this.bucket = bucket;
        this.folder = folder;
        this.pageLength = pageLength;
        this.includePhysicalDetails = includePhysicalDetails;
        this.retryCount = retryCount;
    }

    @Override
    public LazyIterable.LazyLoader<DetailedS3Object> create() {
        return new GetObjectsFullDetailsLoader(client, bucket, folder, includePhysicalDetails, pageLength, retryCount);
    }
}
