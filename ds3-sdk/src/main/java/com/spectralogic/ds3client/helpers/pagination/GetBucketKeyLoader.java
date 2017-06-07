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

import com.google.common.base.Function;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.commands.GetBucketResponse;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.TooManyRetriesException;
import com.spectralogic.ds3client.utils.Guard;
import com.spectralogic.ds3client.utils.collections.LazyIterable;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GetBucketKeyLoader<T> implements LazyIterable.LazyLoader<T>{
    private static final int DEFAULT_MAX_KEYS = 1000;


    private final Ds3Client client;
    private final String bucket;
    private final String prefix;
    private final String delimiter;
    private final int maxKeys;
    private final int retryCount;
    private final Function<ListBucketResult,List<T>> function;
    private String nextMarker;
    private boolean truncated;
    private boolean endOfInput = false;

    public GetBucketKeyLoader(final Ds3Client client, final String bucket, final String prefix, final String delimiter, final String nextMarker, final int maxKeys, final int retryCount, final Function<ListBucketResult, List<T>> function) {
        this.client = client;
        this.bucket = bucket;
        this.prefix = prefix;
        this.delimiter = delimiter;
        this.nextMarker = nextMarker;
        this.maxKeys = Math.min(maxKeys, DEFAULT_MAX_KEYS);
        this.retryCount = retryCount;
        this.nextMarker = nextMarker;
        this.truncated  = nextMarker != null;
        this.function = function;
    }

    @Override
    public List<T> getNextValues() {
        if (endOfInput) {
            return (List<T>) Collections.emptyList();
        }
        int retryAttempt = 0;
        while (true) {
            final GetBucketRequest request = new GetBucketRequest(bucket);
            request.withMaxKeys(maxKeys);
            if (prefix != null) { request.withPrefix(prefix); }
            if (truncated) { request.withMarker(nextMarker); }
            if (delimiter != null) { request.withDelimiter(delimiter); }
            GetBucketResponse response;
            try {
                response = this.client.getBucket(request);
            } catch (final FailedRequestException e) {
                throw new RuntimeException("Failed to get the list of objects due to a failed request", e);
            } catch (final IOException e) {
               if (retryAttempt >= retryCount) {
                   throw new TooManyRetriesException("Failed to get the next set of objects from the getBucketKey request after " + retryCount + " retries", e);
               }
               response = null;
            }
            if (response == null) {
                retryAttempt++;
            } else {
                final ListBucketResult result = response.getListBucketResult();

                truncated = result.getTruncated();
                this.nextMarker = result.getNextMarker();
                if (Guard.isStringNullOrEmpty(nextMarker) && !truncated) {
                    endOfInput = true;
                }
                return function.apply(result);

            }
        }
    }
}
