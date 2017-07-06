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
import com.spectralogic.ds3client.models.JobChunkClientProcessingOrderGuarantee;
import com.spectralogic.ds3client.models.JobNode;
import com.spectralogic.ds3client.models.JobRequestType;
import com.spectralogic.ds3client.models.JobStatus;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.Priority;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import org.apache.commons.collections4.ListUtils;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MasterObjectListBuilder_Test {
    @Test
    public void testOneChunkContainingBlobsOfSameName() {
        final MasterObjectList originalMasterObjectList = makeMasterObjectList();

        final List<Ds3Object> objectsInJobCreation = BlobAndChunkHelper.makeObjectsInFirstChunk();

        final BulkObject trixieBlob = BlobAndChunkHelper.makeBlob(objectsInJobCreation.get(0).getSize(), objectsInJobCreation.get(0).getName());
        final BulkObject shastaBlob = BlobAndChunkHelper.makeBlob(objectsInJobCreation.get(1).getSize(), objectsInJobCreation.get(1).getName());
        final BulkObject gracieBlob = BlobAndChunkHelper.makeBlob(objectsInJobCreation.get(2).getSize(), objectsInJobCreation.get(2).getName());

        final Objects chunk = BlobAndChunkHelper.makeChunk(1, Arrays.asList(trixieBlob, shastaBlob, gracieBlob));
        final List<Objects> chunkList = Collections.singletonList(chunk);

        originalMasterObjectList.setObjects(chunkList);

        final MasterObjectListBuilder masterObjectListBuilder = new OriginatingBlobMasterObjectListBuilder(
                new OriginatingBlobChunkFilter(chunkList, objectsInJobCreation)
        );
        final MasterObjectList newMasterObjectList = masterObjectListBuilder.fromMasterObjectList(originalMasterObjectList);

        assertTrue(BlobAndChunkHelper.masterObjectListsAreEqual(originalMasterObjectList, newMasterObjectList));
    }

    private MasterObjectList makeMasterObjectList() {
        final MasterObjectList originalMasterObjectList = new MasterObjectList();
        originalMasterObjectList.setAggregating(true);
        originalMasterObjectList.setBucketName(BlobAndChunkHelper.bucketName);
        originalMasterObjectList.setCachedSizeInBytes(0);
        originalMasterObjectList.setChunkClientProcessingOrderGuarantee(JobChunkClientProcessingOrderGuarantee.IN_ORDER);
        originalMasterObjectList.setCompletedSizeInBytes(0);
        originalMasterObjectList.setEntirelyInCache(false);
        originalMasterObjectList.setJobId(UUID.randomUUID());
        originalMasterObjectList.setNaked(false);
        originalMasterObjectList.setName("Put job");

        final JobNode jobNode = new JobNode();
        jobNode.setEndPoint("Endpoint");
        jobNode.setHttpPort(80);
        jobNode.setHttpsPort(443);
        jobNode.setId(UUID.randomUUID());

        originalMasterObjectList.setNodes(Collections.singletonList(jobNode));
        originalMasterObjectList.setOriginalSizeInBytes(BlobAndChunkHelper.sizeOfObjectsInFirstChunk());
        originalMasterObjectList.setPriority(Priority.NORMAL);
        originalMasterObjectList.setRequestType(JobRequestType.PUT);
        originalMasterObjectList.setStartDate(new Date());
        originalMasterObjectList.setStatus(JobStatus.IN_PROGRESS);
        originalMasterObjectList.setUserId(UUID.randomUUID());
        originalMasterObjectList.setUserName(BlobAndChunkHelper.Gracie);

        return originalMasterObjectList;
    }

    @Test
    public void testOneChunkNotContainingBlobsOfSameName() {
        final List<Ds3Object> objectsInJobCreation = BlobAndChunkHelper.makeObjectsInFirstChunk();

        final BulkObject blob1 = BlobAndChunkHelper.makeBlob(1, "1");
        final BulkObject blob2 = BlobAndChunkHelper.makeBlob(2, "2");
        final BulkObject blob3 = BlobAndChunkHelper.makeBlob(3, "3");

        final Objects chunk = BlobAndChunkHelper.makeChunk(1, Arrays.asList(blob1, blob2, blob3));

        final MasterObjectList originalMasterObjectList = makeMasterObjectList();

        final List<Objects> chunkList = Collections.singletonList(chunk);

        originalMasterObjectList.setObjects(chunkList);

        final MasterObjectListBuilder masterObjectListBuilder = new OriginatingBlobMasterObjectListBuilder(
                new OriginatingBlobChunkFilter(chunkList, objectsInJobCreation)
        );
        final MasterObjectList newMasterObjectList = masterObjectListBuilder.fromMasterObjectList(originalMasterObjectList);

        assertFalse(BlobAndChunkHelper.masterObjectListsAreEqual(originalMasterObjectList, newMasterObjectList));
        assertEquals(0, Iterables.size(newMasterObjectList.getObjects()));
    }

    @Test
    public void testTwoChunksContainingBlobsOfSameName() {
        final List<Ds3Object> objectsInFirstChunk = BlobAndChunkHelper.makeObjectsInFirstChunk();

        final BulkObject trixieBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(0).getSize(), objectsInFirstChunk.get(0).getName());
        final BulkObject shastaBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(1).getSize(), objectsInFirstChunk.get(1).getName());
        final BulkObject gracieBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(2).getSize(), objectsInFirstChunk.get(2).getName());

        final Objects firstChunk = BlobAndChunkHelper.makeChunk(1, Arrays.asList(trixieBlob, shastaBlob, gracieBlob));

        final List<Ds3Object> objectsInSecondChunk = BlobAndChunkHelper.makeObjectsInSecondChunk();

        final BulkObject twitchBlob = BlobAndChunkHelper.makeBlob(objectsInSecondChunk.get(0).getSize(), objectsInSecondChunk.get(0).getName());
        final BulkObject marblesBlob = BlobAndChunkHelper.makeBlob(objectsInSecondChunk.get(1).getSize(), objectsInSecondChunk.get(1).getName());
        final BulkObject nibblesBlob = BlobAndChunkHelper.makeBlob(objectsInSecondChunk.get(2).getSize(), objectsInSecondChunk.get(2).getName());

        final Objects secondChunk = BlobAndChunkHelper.makeChunk(2, Arrays.asList(twitchBlob, marblesBlob, nibblesBlob));

        final List<Objects> chunkList = Arrays.asList(firstChunk, secondChunk);

        final MasterObjectList originalMasterObjectList = makeMasterObjectList();
        originalMasterObjectList.setObjects(chunkList);

        final MasterObjectListBuilder masterObjectListBuilder = new OriginatingBlobMasterObjectListBuilder(
                new OriginatingBlobChunkFilter(chunkList, ListUtils.union(objectsInFirstChunk, objectsInSecondChunk))
        );

        final MasterObjectList newMasterObjectList = masterObjectListBuilder.fromMasterObjectList(originalMasterObjectList);

        assertEquals(2, Iterables.size(newMasterObjectList.getObjects()));
        assertTrue(BlobAndChunkHelper.masterObjectListsAreEqual(originalMasterObjectList, newMasterObjectList));
    }

    @Test
    public void testTwoChunksContainingSplitBlobs() {
        final List<Ds3Object> objectsInFirstChunk = BlobAndChunkHelper.makeObjectsInFirstChunk();

        final BulkObject trixieBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(0).getSize(), objectsInFirstChunk.get(0).getName());
        final BulkObject shastaBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(1).getSize(), objectsInFirstChunk.get(1).getName());
        final BulkObject gracieBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(2).getSize(), objectsInFirstChunk.get(2).getName());

        final Objects firstChunk = BlobAndChunkHelper.makeChunk(1, Arrays.asList(trixieBlob, shastaBlob, gracieBlob));

        final BulkObject gracieBlob2 = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(2).getSize() + 1, objectsInFirstChunk.get(2).getName());
        gracieBlob2.setOffset(gracieBlob2.getLength());

        final Objects secondChunk = BlobAndChunkHelper.makeChunk(2, Collections.singletonList(gracieBlob2));

        final List<Objects> chunkList = Arrays.asList(firstChunk, secondChunk);

        final MasterObjectList originalMasterObjectList = makeMasterObjectList();
        originalMasterObjectList.setObjects(chunkList);

        final MasterObjectListBuilder masterObjectListBuilder = new OriginatingBlobMasterObjectListBuilder(
                new OriginatingBlobChunkFilter(chunkList, ListUtils.union(objectsInFirstChunk, objectsInFirstChunk))
        );

        final MasterObjectList newMasterObjectList = masterObjectListBuilder.fromMasterObjectList(originalMasterObjectList);

        assertEquals(2, Iterables.size(newMasterObjectList.getObjects()));
        assertTrue(BlobAndChunkHelper.masterObjectListsAreEqual(originalMasterObjectList, newMasterObjectList));
    }

    @Test
    public void testTwoChunksFromDifferentJobs() {
        final List<Ds3Object> objectsInFirstChunk = BlobAndChunkHelper.makeObjectsInFirstChunk();

        final BulkObject trixieBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(0).getSize(), objectsInFirstChunk.get(0).getName());
        final BulkObject shastaBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(1).getSize(), objectsInFirstChunk.get(1).getName());
        final BulkObject gracieBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(2).getSize(), objectsInFirstChunk.get(2).getName());

        final Objects chunk1 = BlobAndChunkHelper.makeChunk(1, Arrays.asList(trixieBlob, shastaBlob, gracieBlob));

        final BulkObject blob1 = BlobAndChunkHelper.makeBlob(1, "1");
        final BulkObject blob2 = BlobAndChunkHelper.makeBlob(2, "2");
        final BulkObject blob3 = BlobAndChunkHelper.makeBlob(3, "3");

        final Objects chunk2 = BlobAndChunkHelper.makeChunk(2, Arrays.asList(blob1, blob2, blob3));

        final List<Objects> chunkList = Arrays.asList(chunk1, chunk2);

        final MasterObjectList originalMasterObjectList = makeMasterObjectList();
        originalMasterObjectList.setObjects(chunkList);

        final MasterObjectListBuilder masterObjectListBuilder = new OriginatingBlobMasterObjectListBuilder(
                new OriginatingBlobChunkFilter(chunkList, ListUtils.union(objectsInFirstChunk, objectsInFirstChunk))
        );

        final MasterObjectList newMasterObjectList = masterObjectListBuilder.fromMasterObjectList(originalMasterObjectList);

        assertEquals(1, Iterables.size(newMasterObjectList.getObjects()));
        assertFalse(BlobAndChunkHelper.masterObjectListsAreEqual(originalMasterObjectList, newMasterObjectList));
    }
}
