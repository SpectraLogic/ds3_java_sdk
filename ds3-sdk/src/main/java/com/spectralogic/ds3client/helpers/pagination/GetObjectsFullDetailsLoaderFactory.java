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
import com.spectralogic.ds3client.models.DetailedS3Object;
import com.spectralogic.ds3client.utils.collections.LazyIterable;

/**
 * A lazy implementation for paginating the Spectra S3 {@link com.spectralogic.ds3client.commands.spectrads3.GetObjectsWithFullDetailsSpectraS3Request} api call
 */
public class GetObjectsFullDetailsLoaderFactory implements LazyIterable.LazyLoaderFactory<DetailedS3Object> {
    private final Ds3Client client;
    private final String bucket;
    private final int pageLength;
    private final int retryCount;
    private final boolean includePhysicalDetails;

    public GetObjectsFullDetailsLoaderFactory(final Ds3Client client, final String bucket, final int pageLength, final int retryCount, final boolean includePhysicalDetails) {
        this.client = client;
        this.bucket = bucket;
        this.pageLength = pageLength;
        this.includePhysicalDetails = includePhysicalDetails;
        this.retryCount = retryCount;
    }

    @Override
    public LazyIterable.LazyLoader<DetailedS3Object> create() {
        return new GetObjectsFullDetailsLoader(client, bucket, includePhysicalDetails, pageLength, retryCount);
    }
}
