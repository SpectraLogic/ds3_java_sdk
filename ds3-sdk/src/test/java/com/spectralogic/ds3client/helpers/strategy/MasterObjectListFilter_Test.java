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
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.MockedHeaders;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.commands.interfaces.MetadataImpl;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Response;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.events.SameThreadEventRunner;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.BlobStrategy;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.ClientDefinedChunkAttemptRetryDelayBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.ContinueForeverChunkAttemptsRetryBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.GetSequentialBlobStrategy;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcher;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcherImpl;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategyBuilder;
import com.spectralogic.ds3client.helpers.util.PartialObjectHelpers;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.ChecksumType;
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
import org.mockito.Mockito;

import javax.annotation.Nullable;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MasterObjectListFilter_Test {
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

        final MasterObjectListFilter masterObjectListFilter = new OriginatingBlobMasterObjectListFilter(
                new OriginatingBlobChunkFilter(objectsInJobCreation)
        );
        final MasterObjectList newMasterObjectList = masterObjectListFilter.apply(originalMasterObjectList);

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

        final MasterObjectListFilter masterObjectListFilter = new OriginatingBlobMasterObjectListFilter(
                new OriginatingBlobChunkFilter(objectsInJobCreation)
        );
        final MasterObjectList newMasterObjectList = masterObjectListFilter.apply(originalMasterObjectList);

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

        final MasterObjectListFilter masterObjectListFilter = new OriginatingBlobMasterObjectListFilter(
                new OriginatingBlobChunkFilter(ListUtils.union(objectsInFirstChunk, objectsInSecondChunk))
        );

        final MasterObjectList newMasterObjectList = masterObjectListFilter.apply(originalMasterObjectList);

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

        final MasterObjectListFilter masterObjectListFilter = new OriginatingBlobMasterObjectListFilter(
                new OriginatingBlobChunkFilter(ListUtils.union(objectsInFirstChunk, objectsInFirstChunk))
        );

        final MasterObjectList newMasterObjectList = masterObjectListFilter.apply(originalMasterObjectList);

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

        final MasterObjectListFilter masterObjectListFilter = new OriginatingBlobMasterObjectListFilter(
                new OriginatingBlobChunkFilter(ListUtils.union(objectsInFirstChunk, objectsInFirstChunk))
        );

        final MasterObjectList newMasterObjectList = masterObjectListFilter.apply(originalMasterObjectList);

        assertEquals(1, Iterables.size(newMasterObjectList.getObjects()));
        assertFalse(BlobAndChunkHelper.masterObjectListsAreEqual(originalMasterObjectList, newMasterObjectList));
    }

    @Test
    public void testGetBlobStrategyWithAggregatingMasterObjectList() throws IOException, InterruptedException {
        final List<Ds3Object> objectsInFirstChunk = BlobAndChunkHelper.makeObjectsInFirstChunk();

        // Blobs whose corresponding ds3 objects are defined as part of a job we create in our own process.
        final BulkObject trixieBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(0).getSize(), objectsInFirstChunk.get(0).getName());
        final BulkObject shastaBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(1).getSize(), objectsInFirstChunk.get(1).getName());
        final BulkObject gracieBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(2).getSize(), objectsInFirstChunk.get(2).getName());

        final Objects chunk1 = BlobAndChunkHelper.makeChunk(1, Arrays.asList(trixieBlob, shastaBlob, gracieBlob));

        // Make the result of a call to getJobChunksReadyForClientProcessingSpectraS3 return a master object list containing
        // blobs not defined in the list of ds3 objects originally included in the job definition.
        final BulkObject blob1 = BlobAndChunkHelper.makeBlob(1, "1");
        final BulkObject blob2 = BlobAndChunkHelper.makeBlob(2, "2");

        final Objects chunk2 = BlobAndChunkHelper.makeChunk(2, Arrays.asList(blob1, blob2));

        final List<Objects> chunkList = Arrays.asList(chunk1, chunk2);

        final MasterObjectList masterObjectList = makeMasterObjectList();
        masterObjectList.setObjects(chunkList);

        final GetJobChunksReadyForClientProcessingSpectraS3Response getJobChunksResponse =
                new GetJobChunksReadyForClientProcessingSpectraS3Response(masterObjectList, 0,
                        GetJobChunksReadyForClientProcessingSpectraS3Response.Status.AVAILABLE, "", ChecksumType.Type.NONE);

        final Ds3Client mockedDs3Client = Mockito.mock(Ds3Client.class);
        Mockito.when(mockedDs3Client.getJobChunksReadyForClientProcessingSpectraS3(Mockito.any(GetJobChunksReadyForClientProcessingSpectraS3Request.class)))
                .thenReturn(getJobChunksResponse);

        final EventDispatcher eventDispatcher = new EventDispatcherImpl(new SameThreadEventRunner());

        final BlobStrategy blobStrategy = new GetSequentialBlobStrategy(mockedDs3Client, masterObjectList,
                eventDispatcher, new ContinueForeverChunkAttemptsRetryBehavior(),
                new ClientDefinedChunkAttemptRetryDelayBehavior(0, eventDispatcher),
                new OriginatingBlobMasterObjectListFilter(new OriginatingBlobChunkFilter(objectsInFirstChunk)));

        final Iterable<JobPart> jobParts = blobStrategy.getWork();

        assertEquals(objectsInFirstChunk.size(), Iterables.size(jobParts));

        final Set<String> ds3ObjectNamesFromJobCreation = getNamesOfObjectsInFirstChunk(objectsInFirstChunk);

        final Set<String> jobPartNamesFromBlobStrategy = FluentIterable.from(jobParts)
                .transform(new Function<JobPart, String>() {
                    @Nullable
                    @Override
                    public String apply(@Nullable final JobPart jobPart) {
                        return jobPart.getBlob().getName();
                    }
                })
                .toSet();

        assertEquals(ds3ObjectNamesFromJobCreation, jobPartNamesFromBlobStrategy);
    }

    private Set<String> getNamesOfObjectsInFirstChunk(final List<Ds3Object> objectsInFirstChunk) {
        return FluentIterable.from(objectsInFirstChunk)
                    .transform(new Function<Ds3Object, String>() {
                        @Nullable
                        @Override
                        public String apply(@Nullable final Ds3Object blob) {
                            return blob.getName();
                        }
                    })
                    .toSet();
    }

    @Test
    public void testGetStrategyBuildsCorrectMasterObjectListFilter() throws IOException {
        final List<Ds3Object> objectsInFirstChunk = BlobAndChunkHelper.makeObjectsInFirstChunk();

        // Blobs whose corresponding ds3 objects are defined as part of a job we create in our own process.
        final BulkObject trixieBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(0).getSize(), objectsInFirstChunk.get(0).getName());
        final BulkObject shastaBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(1).getSize(), objectsInFirstChunk.get(1).getName());
        final BulkObject gracieBlob = BlobAndChunkHelper.makeBlob(objectsInFirstChunk.get(2).getSize(), objectsInFirstChunk.get(2).getName());

        final Objects chunk1 = BlobAndChunkHelper.makeChunk(1, Arrays.asList(trixieBlob, shastaBlob, gracieBlob));

        // Make the result of a call to getJobChunksReadyForClientProcessingSpectraS3 return a master object list containing
        // blobs not defined in the list of ds3 objects originally included in the job definition.
        final BulkObject blob1 = BlobAndChunkHelper.makeBlob(1, "1");
        final BulkObject blob2 = BlobAndChunkHelper.makeBlob(2, "2");

        final Objects chunk2 = BlobAndChunkHelper.makeChunk(2, Arrays.asList(blob1, blob2));

        final List<Objects> chunkList = Arrays.asList(chunk1, chunk2);

        final MasterObjectList masterObjectList = makeMasterObjectList();
        masterObjectList.setObjects(chunkList);

        final GetJobChunksReadyForClientProcessingSpectraS3Response getJobChunksResponse =
                new GetJobChunksReadyForClientProcessingSpectraS3Response(masterObjectList, 0,
                        GetJobChunksReadyForClientProcessingSpectraS3Response.Status.AVAILABLE, "", ChecksumType.Type.NONE);
        final Ds3Client mockedDs3Client = Mockito.mock(Ds3Client.class);
        Mockito.when(mockedDs3Client.getJobChunksReadyForClientProcessingSpectraS3(Mockito.any(GetJobChunksReadyForClientProcessingSpectraS3Request.class)))
                .thenReturn(getJobChunksResponse);

        final GetObjectResponse getObjectResponse = new GetObjectResponse(new MetadataImpl(new MockedHeaders(new HashMap<String, String>())),
                0, "", ChecksumType.Type.NONE);
        Mockito.when(mockedDs3Client.getObject(Mockito.any(GetObjectRequest.class)))
                .thenReturn(getObjectResponse);

        final Set<String> fileNamesBlobStrategyWantsToOpen = Collections.synchronizedSet(new HashSet<String>());

        final SeekableByteChannel mockedSeekableByteChannel = Mockito.mock(SeekableByteChannel.class);
        Mockito.when(mockedSeekableByteChannel.truncate(Mockito.anyLong()))
                .thenReturn(mockedSeekableByteChannel);

        final Ds3ClientHelpers.ObjectChannelBuilder objectChannelBuilder = new Ds3ClientHelpers.ObjectChannelBuilder() {
            @Override
            public SeekableByteChannel buildChannel(final String key) throws IOException {
                fileNamesBlobStrategyWantsToOpen.add(key);
                return mockedSeekableByteChannel;
            }
        };

        final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder();
        transferStrategyBuilder
                .withDs3Client(mockedDs3Client)
                .withMasterObjectList(masterObjectList)
                .withChannelBuilder(objectChannelBuilder)
                .withRangesForBlobs(PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(),
                        PartialObjectHelpers.getPartialObjectsRanges(objectsInFirstChunk)))
                .withObjectsInJob(objectsInFirstChunk)
                .makeGetTransferStrategy()
                .transfer();

        final Set<String> ds3ObjectNamesFromJobCreation = getNamesOfObjectsInFirstChunk(objectsInFirstChunk);

        assertEquals(ds3ObjectNamesFromJobCreation, fileNamesBlobStrategyWantsToOpen);
    }
}
