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

package com.spectralogic.ds3client.helpers.strategy.blobstrategy;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.bulk.Ds3Object;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * When aggregating jobs from more than one process, it is possible that one process will see a
 * {@link com.spectralogic.ds3client.models.MasterObjectList}
 * that contains blobs defined in the other process.  To prevent one process from trying to transfer
 * blobs defined in another process, we apply a filter to master object lists to eliminate blobs not
 * originally defined in a particular process.  The ChunkFilter interface implements the behavior
 * in a {@link MasterObjectListFilter} filter that decides what chunks to include in the resultant
 * master object list.  This implementation filters out chunks containing blob names not contained in the
 * list of {@link Ds3Object} used in originally creating a job in a particular process.
 */
public class OriginatingBlobChunkFilter implements ChunkFilter {
    private final Set<String> blobNamesFromJobCreation;

    public OriginatingBlobChunkFilter(final Iterable<Ds3Object> ds3ObjectsInJobCreation) {
        blobNamesFromJobCreation = FluentIterable.from(ds3ObjectsInJobCreation)
                .transform(new Function<Ds3Object, String>() {
                    @Nullable
                    @Override
                    public String apply(@Nullable final Ds3Object blob) {
                        return blob.getName();
                    }
                })
                .toSet();
    }

    @Override
    public Iterable<Objects> apply(final Collection<Objects> chunksFromMasterObjectList) {
        return FluentIterable.from(chunksFromMasterObjectList)
                .transform(new Function<Objects, BlobsChunkPair>() {
                    @Nullable
                    @Override
                    public BlobsChunkPair apply(@Nullable final Objects chunkFromMasterObjectList) {
                        final Iterable<BulkObject> blobsInJobCreation = blobsInJobCreation(chunkFromMasterObjectList.getObjects());
                        return new BlobsChunkPair(blobsInJobCreation, chunkFromMasterObjectList);
                    }
                })
                .filter(new Predicate<BlobsChunkPair>() {
                    @Override
                    public boolean apply(@Nullable final BlobsChunkPair blobsInJobCreationChunkPair) {
                        return Iterables.size(blobsInJobCreationChunkPair.blobs()) > 0;
                    }
                })
                .transform(new Function<BlobsChunkPair, Objects>() {
                    @Nullable
                    @Override
                    public Objects apply(@Nullable final BlobsChunkPair blobsInJobCreationChunkPair) {
                        final Objects chunkFromMasterObjectList = blobsInJobCreationChunkPair.chunk();

                        if (Iterables.size(blobsInJobCreationChunkPair.blobs()) == chunkFromMasterObjectList.getObjects().size()) {
                            return chunkFromMasterObjectList;
                        }

                        final Objects newChunk = new Objects();
                        newChunk.setNodeId(chunkFromMasterObjectList.getNodeId());
                        newChunk.setChunkNumber(chunkFromMasterObjectList.getChunkNumber());
                        newChunk.setChunkId(chunkFromMasterObjectList.getChunkId());
                        newChunk.setObjects(Lists.newArrayList(blobsInJobCreationChunkPair.blobs()));

                        return newChunk;
                    }
                });
    }

    private Iterable<BulkObject> blobsInJobCreation(final List<BulkObject> blobsFromMasterObjectList) {
        return FluentIterable.from(blobsFromMasterObjectList)
                .filter(new Predicate<BulkObject>() {
                    @Override
                    public boolean apply(@Nullable final BulkObject blob) {
                        return blobNamesFromJobCreation.contains(blob.getName());
                    }
                });
    }
}
