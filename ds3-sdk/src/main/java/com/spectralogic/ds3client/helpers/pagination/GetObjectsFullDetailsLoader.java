package com.spectralogic.ds3client.helpers.pagination;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetObjectsWithFullDetailsSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetObjectsWithFullDetailsSpectraS3Response;
import com.spectralogic.ds3client.models.DetailedS3Object;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.TooManyRetriesException;
import com.spectralogic.ds3client.utils.Guard;
import com.spectralogic.ds3client.utils.collections.LazyIterable;

import java.io.IOException;
import java.util.List;

public class GetObjectsFullDetailsLoader implements LazyIterable.LazyIterableLoader<DetailedS3Object> {

    private final Ds3Client client;
    private final String bucket;
    private final String folder;
    private final boolean includePhysicalDetails;
    private final int pageLength;
    private final int retryCount;


    public GetObjectsFullDetailsLoader(final Ds3Client client, final String bucket, final String folder, final boolean includePhysicalDetails, final int pageLength, final int retryCount) {
        this.client = client;
        this.bucket = bucket;
        this.folder = folder;
        this.includePhysicalDetails = includePhysicalDetails;
        this.pageLength = pageLength;
        this.retryCount = retryCount;
    }

    @Override
    public List<DetailedS3Object> getNextValues() {

        int retryAttempt = 0;

        while(true) {
            final GetObjectsWithFullDetailsSpectraS3Request request = new GetObjectsWithFullDetailsSpectraS3Request()
                    .withIncludePhysicalPlacement(includePhysicalDetails)
                    .withBucketId(bucket)
                    .withPageLength(pageLength);
            if (!Guard.isStringNullOrEmpty(folder)) {
                request.withFolder(folder);
            }
            try {
                final GetObjectsWithFullDetailsSpectraS3Response response = client.getObjectsWithFullDetailsSpectraS3(request);
                final Integer pagingTotalResultCount = response.getPagingTotalResultCount();
                final Integer pagingTruncated = response.getPagingTruncated();
            } catch (final FailedRequestException e) {
                // failure
                throw new RuntimeException("Encountered a failure when attempting to get object list", e);
            } catch (final IOException e) {
                //error
                if (retryAttempt >= retryCount) {
                    //TODO need a proxied test to validate this retry logic
                    throw new TooManyRetriesException("Failed to get the next set of objects from the getBucket request after " + retryCount + " retries", e);
                }
                retryAttempt++;
            }
        }
    }
}
