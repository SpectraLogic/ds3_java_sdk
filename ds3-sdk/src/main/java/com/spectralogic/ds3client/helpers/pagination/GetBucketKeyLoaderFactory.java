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
import com.google.common.collect.FluentIterable;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.FileSystemKey;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.models.common.CommonPrefixes;
import com.spectralogic.ds3client.utils.collections.LazyIterable;

import javax.annotation.Nullable;
import java.util.List;

public class GetBucketKeyLoaderFactory<T> implements LazyIterable.LazyLoaderFactory<T> {

    public static final Function<ListBucketResult, List<Contents>> contentsFunction = new Function<ListBucketResult, List<Contents>>() {
        @Override
        public List<Contents> apply(@Nullable final ListBucketResult input) {

            return FluentIterable.from(input.getObjects()).toList();
        }
    };

    public static final Function<ListBucketResult,List<FileSystemKey>> getFileSystemKeysFunction = new Function<ListBucketResult, List<FileSystemKey>>() {
        @Nullable
        @Override
        public List<FileSystemKey> apply(@Nullable final ListBucketResult result) {
            final FluentIterable<FileSystemKey> contentIterable = FluentIterable.from(result.getObjects())
                    .transform(new Function<Contents, FileSystemKey>() {
                        @Override
                        public FileSystemKey apply(@Nullable final Contents input) {
                            return new FileSystemKey(input);
                        }
                    });
            return FluentIterable.from(result.getCommonPrefixes())
                    .transform( new Function<CommonPrefixes, FileSystemKey>() {
                        @Override
                        public FileSystemKey apply(@Nullable final CommonPrefixes input) {
                            return new FileSystemKey(input);
                        }
                    })
                    .append(contentIterable)
                    .toList();
        }
    };

    private final Ds3Client client;
    private final String bucket;
    private final String keyPrefix;
    private final String nextMarker;
    private final String delimiter;
    private final int maxKeys;
    private final int defaultListObjectsRetries;
    private final Function<ListBucketResult, List<T>> function;

    public GetBucketKeyLoaderFactory(final Ds3Client client, final String bucket, final String keyPrefix, final String delimiter, final String nextMarker, final int maxKeys, final int defaultListObjectsRetries, final Function<ListBucketResult, List<T>> function) {
        this.client = client;
        this.bucket = bucket;
        this.keyPrefix = keyPrefix;
        this.delimiter = delimiter;
        this.nextMarker = nextMarker;
        this.maxKeys = maxKeys;
        this.defaultListObjectsRetries = defaultListObjectsRetries;
        this.function = function;
    }

    @Override
    public LazyIterable.LazyLoader<T> create() {
        return new GetBucketKeyLoader<>(client, bucket, keyPrefix, delimiter, nextMarker, maxKeys, defaultListObjectsRetries, function);
    }
}
