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

package com.spectralogic.ds3client.helpers.strategy;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import org.apache.commons.collections4.SetUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

public class OriginatingBlobChunkFilter implements ChunkFilter {
    private final Iterable<Ds3Object> ds3ObjectsInJobCreation;

    public OriginatingBlobChunkFilter(final Iterable<Ds3Object> ds3ObjectsInJobCreation) {
        this.ds3ObjectsInJobCreation = ds3ObjectsInJobCreation;
    }

    @Override
    public Iterable<Objects> apply(final Collection<Objects> chunksFromMasterObjectList) {
        final Set<String> blobNamesFromJobCreation = FluentIterable.from(ds3ObjectsInJobCreation)
                .transform(new Function<Ds3Object, String>() {
                    @Nullable
                    @Override
                    public String apply(@Nullable final Ds3Object blob) {
                        return blob.getName();
                    }
                })
                .toSet();

        return FluentIterable.from(chunksFromMasterObjectList)
                .filter(new Predicate<Objects>() {
                    @Override
                    public boolean apply(@Nullable final Objects chunk) {
                        final Set<String> blobNamesFromMasterObjectList =  FluentIterable.from(chunk.getObjects())
                                .transform(new Function<BulkObject, String>() {
                                    @Nullable
                                    @Override
                                    public String apply(@Nullable final BulkObject blob) {
                                        return blob.getName();
                                    }
                                })
                                .toSet();
                        return SetUtils.intersection(blobNamesFromJobCreation, blobNamesFromMasterObjectList).size() > 0;
                    }
                });
    }
}
