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

package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.commands.GetBucketResponse;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.ListBucketResult;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class LazyObjectIterable implements Iterable<Contents> {

    private static final int DEFAULT_MAX_KEYS = 1000;
    private final Ds3Client client;
    private final String bucket;
    private final String prefix;
    private final String nextMarker;
    private final int maxKeys;

    public LazyObjectIterable(final Ds3Client client, final String bucket, final String keyPrefix, final String nextMarker, final int maxKeys) {
        this.client = client;
        this.bucket = bucket;
        this.prefix = keyPrefix;
        this.nextMarker = nextMarker;
        this.maxKeys = maxKeys;

    }

    @Override
    public Iterator<Contents> iterator() {
        return new LazyObjectIterator(client, bucket, prefix, nextMarker, maxKeys);
    }

    private class LazyObjectIterator implements Iterator<Contents> {

        private final Ds3Client client;
        private final String bucket;
        private final String prefix;
        private final int maxKeys;

        private String nextMarker;
        private List<Contents> cache = null;
        private int cachePointer;
        private boolean truncated;

        /*
        int remainingKeys = maxKeys;
        boolean isTruncated = false;
        String marker = nextMarker;
        if (nextMarker != null) isTruncated = true;

        do {
            final GetBucketRequest request = new GetBucketRequest(bucket);
            request.withMaxKeys(Math.min(remainingKeys, DEFAULT_MAX_KEYS));
            if (keyPrefix != null) {
                request.withPrefix(keyPrefix);
            }
            if (isTruncated) {
                request.withMarker(marker);
            }

            final GetBucketResponse response = this.client.getBucket(request);
            final ListBucketResult result = response.getListBucketResult();

            isTruncated = result.getTruncated();
            marker = result.getNextMarker();
            remainingKeys -= result.getObjects().size();

            for (final Contents objectApiBean : result.getObjects()) {
                objectApiBeans.add(objectApiBean);
            }
        } while (isTruncated && remainingKeys > 0);


         */

        protected LazyObjectIterator(final Ds3Client client, final String bucket, final String prefix, final String nextMarker, final int maxKeys) {
            this.client = client;
            this.bucket = bucket;
            this.prefix = prefix;
            this.maxKeys = maxKeys;

            this.nextMarker = nextMarker;
            this.truncated = nextMarker != null;
        }

        @Override
        public boolean hasNext() {
            if (cache == null) {
                loadCache();
            }
            return cachePointer < cache.size() || truncated;
        }

        @Override
        public Contents next() {
            if (cache == null || cachePointer >= cache.size()) {
                loadCache();
            }
            final Contents contents = cache.get(cachePointer);
            cachePointer++;
            return contents;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("The remove method on the LazyObjectIterator is not supported");
        }

        private void loadCache() {
            final GetBucketRequest request = new GetBucketRequest(bucket);
            request.withMaxKeys(Math.min(maxKeys, DEFAULT_MAX_KEYS));
            if (prefix!= null) {
                request.withPrefix(prefix);
            }
            if (truncated) {
                request.withMarker(nextMarker);
            }

            final GetBucketResponse response;
            try {
                response = this.client.getBucket(request);
                final ListBucketResult result = response.getListBucketResult();

                truncated = result.getTruncated();
                nextMarker= result.getNextMarker();

                this.cache = result.getObjects();
                this.cachePointer = 0;

            } catch (final IOException e) {
                throw new RuntimeException("Failed to get the next set of objects from the getBucket request", e);
            }
        }
    }
}
