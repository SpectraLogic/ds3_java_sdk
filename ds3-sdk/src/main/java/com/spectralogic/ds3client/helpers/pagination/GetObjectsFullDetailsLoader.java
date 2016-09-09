package com.spectralogic.ds3client.helpers.pagination;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetObjectsWithFullDetailsSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetObjectsWithFullDetailsSpectraS3Response;
import com.spectralogic.ds3client.helpers.pagination.commands.GetObjectsFullDetailsPaginatingCommand;
import com.spectralogic.ds3client.models.DetailedS3Object;
import com.spectralogic.ds3client.utils.collections.LazyIterable;

import java.util.List;

public class GetObjectsFullDetailsLoader implements LazyIterable.LazyLoader<DetailedS3Object> {

    private final SpectraS3PaginationLoader<DetailedS3Object, GetObjectsWithFullDetailsSpectraS3Request, GetObjectsWithFullDetailsSpectraS3Response> paginator;

    public GetObjectsFullDetailsLoader(final Ds3Client client, final String bucket, final String folder, final boolean includePhysicalDetails, final int pageLength, final int retryCount) {
        paginator = new SpectraS3PaginationLoader<>(
                new GetObjectsFullDetailsPaginatingCommand(client, bucket, folder, includePhysicalDetails),
                pageLength,
                retryCount);
    }

    @Override
    public List<DetailedS3Object> getNextValues() {
        return paginator.getNextValues();
    }
}
