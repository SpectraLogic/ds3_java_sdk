/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client;

import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.utils.collections.LazyIterable;

public class GetObjectsLoaderFactory implements LazyIterable.LazyLoaderFactory<Contents> {

    private final Ds3Client client;
    private final String bucket;
    private final String keyPrefix;
    private final String nextMarker;
    private final int maxKeys;
    private final int defaultListObjectsRetries;

    public GetObjectsLoaderFactory(final Ds3Client client, final String bucket, final String keyPrefix, final String nextMarker, final int maxKeys, final int defaultListObjectsRetries) {

        this.client = client;
        this.bucket = bucket;
        this.keyPrefix = keyPrefix;
        this.nextMarker = nextMarker;
        this.maxKeys = maxKeys;
        this.defaultListObjectsRetries = defaultListObjectsRetries;
    }

    @Override
    public LazyIterable.LazyLoader<Contents> create() {
        return new GetObjectsLoader(client, bucket, keyPrefix, nextMarker, maxKeys, defaultListObjectsRetries);
    }
}
