 /*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3client.helpers.pagination.commands;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetObjectsWithFullDetailsSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetObjectsWithFullDetailsSpectraS3Response;
import com.spectralogic.ds3client.helpers.pagination.PaginatingCommand;
import com.spectralogic.ds3client.models.DetailedS3Object;

import java.io.IOException;
import java.util.List;

public class GetObjectsFullDetailsPaginatingCommand implements PaginatingCommand<DetailedS3Object, GetObjectsWithFullDetailsSpectraS3Request, GetObjectsWithFullDetailsSpectraS3Response> {
    private final Ds3Client client;
    private final String bucket;
    private final boolean includePhysicalDetails;

    public GetObjectsFullDetailsPaginatingCommand(final Ds3Client client, final String bucket, final boolean includePhysicalDetails) {
        this.client = client;
        this.bucket = bucket;
        this.includePhysicalDetails = includePhysicalDetails;
    }

    @Override
    public GetObjectsWithFullDetailsSpectraS3Request createRequest() {
        return new GetObjectsWithFullDetailsSpectraS3Request().withBucketId(bucket).withIncludePhysicalPlacement(includePhysicalDetails);
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
