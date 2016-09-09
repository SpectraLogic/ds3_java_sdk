package com.spectralogic.ds3client.helpers.pagination.commands;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetObjectsWithFullDetailsSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetObjectsWithFullDetailsSpectraS3Response;
import com.spectralogic.ds3client.helpers.pagination.PaginatingCommand;
import com.spectralogic.ds3client.models.DetailedS3Object;
import com.spectralogic.ds3client.utils.Guard;

import java.io.IOException;
import java.util.List;

public class GetObjectsFullDetailsPaginatingCommand implements PaginatingCommand<DetailedS3Object, GetObjectsWithFullDetailsSpectraS3Request, GetObjectsWithFullDetailsSpectraS3Response> {
    private final Ds3Client client;
    private final String bucket;
    private final String folder;
    private final boolean includePhysicalDetails;

    public GetObjectsFullDetailsPaginatingCommand(final Ds3Client client, final String bucket, final String folder, final boolean includePhysicalDetails) {
        this.client = client;
        this.bucket = bucket;
        this.folder = folder;
        this.includePhysicalDetails = includePhysicalDetails;
    }

    @Override
    public GetObjectsWithFullDetailsSpectraS3Request createRequest() {
        final GetObjectsWithFullDetailsSpectraS3Request getObjectsWithFullDetailsSpectraS3Request = new GetObjectsWithFullDetailsSpectraS3Request().withBucketId(bucket).withIncludePhysicalPlacement(includePhysicalDetails);
        if (Guard.isStringNullOrEmpty(folder)) {
            return getObjectsWithFullDetailsSpectraS3Request;
        } else {
            return getObjectsWithFullDetailsSpectraS3Request.withFolder(folder);
        }
    }

    @Override
    public GetObjectsWithFullDetailsSpectraS3Response invokeCommand(final GetObjectsWithFullDetailsSpectraS3Request paginationRequest) throws IOException {
        return client.getObjectsWithFullDetailsSpectraS3(paginationRequest);
    }

    @Override
    public List<DetailedS3Object> getResponseContents(final GetObjectsWithFullDetailsSpectraS3Response response) {
        return response.getDetailedS3ObjectListResult().getDetailedS3Objects();
    }
}
