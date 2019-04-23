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

package com.spectralogic.ds3client.helpers.pagination;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.utils.collections.LazyIterable;

/**
 *  This class is a child of {@link GetBucketKeyLoaderFactory}
 *  that provides a sane default for mapping {@link com.spectralogic.ds3client.models.ListBucketResult}
 *  "/" will be used for the delimiter, and {@link com.spectralogic.ds3client.models.ListBucketResult} will be mapped to
 *  {@link LazyIterable.LazyLoader<Contents>} which was the old behavior.
 */
public class GetBucketLoaderFactory extends GetBucketKeyLoaderFactory<Contents> {

    /**
     *
     * @param client
     * @param bucket
     * @param keyPrefix
     * @param nextMarker
     * @param maxKeys
     * @param defaultListObjectsRetries
     */
    public GetBucketLoaderFactory(final Ds3Client client, final String bucket, final String keyPrefix, final String nextMarker, final int maxKeys, final int defaultListObjectsRetries) {
        super(client, bucket, keyPrefix, "/" , nextMarker, maxKeys, defaultListObjectsRetries, contentsFunction);
    }

    /**
     * @return {@link LazyIterable.LazyLoader<Contents>}
     */
    @Override
    public LazyIterable.LazyLoader<Contents> create() {
        return super.create();
    }
}