/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

    public GetObjectsFullDetailsLoader(final Ds3Client client, final String bucket, final boolean includePhysicalDetails, final int pageLength, final int retryCount) {
        paginator = new SpectraS3PaginationLoader<>(
                new GetObjectsFullDetailsPaginatingCommand(client, bucket, includePhysicalDetails),
                pageLength,
                retryCount);
    }

    @Override
    public List<DetailedS3Object> getNextValues() {
        return paginator.getNextValues();
    }
}
