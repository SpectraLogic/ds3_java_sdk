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

import com.google.common.collect.Iterables;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import org.apache.commons.collections4.ListUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ChunkFilter_Test {
    private static final String Trixie = "Trixie";
    private static final String Shasta = "Shasta";
    private static final String Gracie = "Gracie";
    private static final String Twitch = "Twitch";
    private static final String Marbles = "Marbles";
    private static final String Nibbles = "Nibbles";

    private final String bucketName = "bucket";

    @Test
    public void testOneChunkContainingBlobsOfSameName() {
        final List<Ds3Object> objectsInJobCreation = makeObjectsInFirstChunk();
        
        final BulkObject trixieBlob = makeBlob(objectsInJobCreation.get(0).getSize(), objectsInJobCreation.get(0).getName());

        final BulkObject shastaBlob = makeBlob(objectsInJobCreation.get(1).getSize(), objectsInJobCreation.get(1).getName());

        final BulkObject gracieBlob = makeBlob(objectsInJobCreation.get(2).getSize(), objectsInJobCreation.get(2).getName());

        final Objects chunk = makeChunk(1, Arrays.asList(trixieBlob, shastaBlob, gracieBlob));

        final OriginatingBlobChunkFilter originatingBlobChunkFilter = new OriginatingBlobChunkFilter(Collections.singletonList(chunk), objectsInJobCreation);
        final Iterable<Objects> chunksWithBlobsFromJobCreation = originatingBlobChunkFilter.apply();

        assertEquals(1, Iterables.size(chunksWithBlobsFromJobCreation));
    }

    private List<Ds3Object> makeObjectsInFirstChunk() {
        final Ds3Object trixie = new Ds3Object(Trixie, 1);
        final Ds3Object shasta = new Ds3Object(Shasta, 2);
        final Ds3Object gracie = new Ds3Object(Gracie, 3);

        return Arrays.asList(trixie, shasta, gracie);
    }

    private BulkObject makeBlob(final long size, final String name) {
        final BulkObject blob = new BulkObject();
        blob.setBucket(bucketName);
        blob.setId(UUID.randomUUID());
        blob.setInCache(false);
        blob.setLatest(true);
        blob.setLength(size);
        blob.setName(name);
        blob.setOffset(0);
        blob.setVersion(1);

        return blob;
    }

    private Objects makeChunk(final int chunkNumber, final List<BulkObject> blobs) {
        final Objects chunk = new Objects();
        chunk.setChunkId(UUID.randomUUID());
        chunk.setChunkNumber(chunkNumber);
        chunk.setNodeId(UUID.randomUUID());
        chunk.setObjects(blobs);

        return chunk;
    }

    @Test
    public void testOneChunkNotContainingBlobsOfSameName() {
        final List<Ds3Object> objectsInJobCreation = makeObjectsInFirstChunk();

        final BulkObject blob1 = makeBlob(1, "1");

        final BulkObject blob2 = makeBlob(2, "2");

        final BulkObject blob3 = makeBlob(3, "3");

        final Objects chunk = makeChunk(1, Arrays.asList(blob1, blob2, blob3));

        final OriginatingBlobChunkFilter originatingBlobChunkFilter = new OriginatingBlobChunkFilter(Collections.singletonList(chunk), objectsInJobCreation);
        final Iterable<Objects> chunksWithBlobsFromJobCreation = originatingBlobChunkFilter.apply();

        assertEquals(0, Iterables.size(chunksWithBlobsFromJobCreation));
    }

    @Test
    public void testTwoChunksContainingBlobsOfSameName() {
        final List<Ds3Object> objectsInFirstChunk = makeObjectsInFirstChunk();

        final BulkObject trixieBlob = makeBlob(objectsInFirstChunk.get(0).getSize(), objectsInFirstChunk.get(0).getName());
        final BulkObject shastaBlob = makeBlob(objectsInFirstChunk.get(1).getSize(), objectsInFirstChunk.get(1).getName());
        final BulkObject gracieBlob = makeBlob(objectsInFirstChunk.get(2).getSize(), objectsInFirstChunk.get(2).getName());

        final Objects firstChunk = makeChunk(1, Arrays.asList(trixieBlob, shastaBlob, gracieBlob));

        final List<Ds3Object> objectsInSecondChunk = makeObjectsInSecondChunk();

        final BulkObject twitchBlob = makeBlob(objectsInSecondChunk.get(0).getSize(), objectsInSecondChunk.get(0).getName());
        final BulkObject marblesBlob = makeBlob(objectsInSecondChunk.get(1).getSize(), objectsInSecondChunk.get(1).getName());
        final BulkObject nibblesBlob = makeBlob(objectsInSecondChunk.get(2).getSize(), objectsInSecondChunk.get(2).getName());

        final Objects secondChunk = makeChunk(2, Arrays.asList(twitchBlob, marblesBlob, nibblesBlob));

        final OriginatingBlobChunkFilter originatingBlobChunkFilter = new OriginatingBlobChunkFilter(Arrays.asList(firstChunk, secondChunk),
                org.apache.commons.collections4.ListUtils.union(objectsInFirstChunk, objectsInSecondChunk));
        final Iterable<Objects> chunks = originatingBlobChunkFilter.apply();

        assertEquals(2, Iterables.size(chunks));
    }

    private List<Ds3Object> makeObjectsInSecondChunk() {
        final Ds3Object twitch = new Ds3Object(Twitch, 4);
        final Ds3Object marbles = new Ds3Object(Marbles, 5);
        final Ds3Object nibbles = new Ds3Object(Nibbles, 6);

        return Arrays.asList(twitch, marbles, nibbles);
    }

    @Test
    public void testTwoChunksContainingSplitBlobs() {
        final List<Ds3Object> objectsInFirstChunk = makeObjectsInFirstChunk();

        final BulkObject trixieBlob = makeBlob(objectsInFirstChunk.get(0).getSize(), objectsInFirstChunk.get(0).getName());
        final BulkObject shastaBlob = makeBlob(objectsInFirstChunk.get(1).getSize(), objectsInFirstChunk.get(1).getName());
        final BulkObject gracieBlob = makeBlob(objectsInFirstChunk.get(2).getSize(), objectsInFirstChunk.get(2).getName());

        final Objects firstChunk = makeChunk(1, Arrays.asList(trixieBlob, shastaBlob, gracieBlob));

        final BulkObject gracieBlob2 = makeBlob(objectsInFirstChunk.get(2).getSize() + 1, objectsInFirstChunk.get(2).getName());
        gracieBlob2.setOffset(gracieBlob2.getLength());

        final Objects secondChunk = makeChunk(2, Collections.singletonList(gracieBlob2));

        final OriginatingBlobChunkFilter originatingBlobChunkFilter = new OriginatingBlobChunkFilter(Arrays.asList(firstChunk, secondChunk),
                objectsInFirstChunk);
        final Iterable<Objects> chunks = originatingBlobChunkFilter.apply();

        assertEquals(2, Iterables.size(chunks));
    }

    @Test
    public void testTwoChunksFromDifferentJobs() {
        final List<Ds3Object> objectsInFirstChunk = makeObjectsInFirstChunk();

        final BulkObject trixieBlob = makeBlob(objectsInFirstChunk.get(0).getSize(), objectsInFirstChunk.get(0).getName());
        final BulkObject shastaBlob = makeBlob(objectsInFirstChunk.get(1).getSize(), objectsInFirstChunk.get(1).getName());
        final BulkObject gracieBlob = makeBlob(objectsInFirstChunk.get(2).getSize(), objectsInFirstChunk.get(2).getName());

        final Objects chunk1 = makeChunk(1, Arrays.asList(trixieBlob, shastaBlob, gracieBlob));

        final BulkObject blob1 = makeBlob(1, "1");
        final BulkObject blob2 = makeBlob(2, "2");
        final BulkObject blob3 = makeBlob(3, "3");

        final Objects chunk2 = makeChunk(2, Arrays.asList(blob1, blob2, blob3));

        final OriginatingBlobChunkFilter originatingBlobChunkFilter = new OriginatingBlobChunkFilter(Arrays.asList(chunk1, chunk2),
                ListUtils.union(objectsInFirstChunk, makeObjectsInSecondChunk()));
        final Iterable<Objects> chunksWithBlobsFromJobCreation = originatingBlobChunkFilter.apply();

        assertEquals(1, Iterables.size(chunksWithBlobsFromJobCreation));
    }
}