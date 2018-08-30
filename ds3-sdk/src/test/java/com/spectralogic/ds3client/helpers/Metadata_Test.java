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

package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.AllocateJobChunkSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.AllocateJobChunkSpectraS3Response;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategy;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategyBuilder;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.JobChunkClientProcessingOrderGuarantee;
import com.spectralogic.ds3client.models.JobNode;
import com.spectralogic.ds3client.models.JobRequestType;
import com.spectralogic.ds3client.models.JobStatus;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.Priority;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Metadata_Test {
    @Test
    public void testThatWeSendMetadataWithEachBlob() throws IOException {
        final JobNode jobNode = new JobNode();
        jobNode.setEndPoint("endpoint");
        jobNode.setHttpPort(80);
        jobNode.setHttpsPort(443);
        jobNode.setId(UUID.randomUUID());

        final String bucketName = "bucket";
        final String fileName = "aFile.txt";

        final BulkObject blob1 = new BulkObject();
        blob1.setBucket(bucketName);
        blob1.setId(UUID.randomUUID());
        blob1.setInCache(false);
        blob1.setLatest(true);
        blob1.setLength(2);
        blob1.setOffset(0);
        blob1.setVersionId(UUID.randomUUID());
        blob1.setName(fileName);

        final BulkObject blob2 = new BulkObject();
        blob2.setBucket(bucketName);
        blob2.setId(UUID.randomUUID());
        blob2.setInCache(false);
        blob2.setLatest(true);
        blob2.setLength(2);
        blob2.setOffset(2);
        blob2.setVersionId(UUID.randomUUID());
        blob2.setName(fileName);

        final Objects chunk = new Objects();
        chunk.setChunkId(UUID.randomUUID());
        chunk.setChunkNumber(1);
        chunk.setNodeId(UUID.randomUUID());
        chunk.setObjects(Arrays.asList(blob1, blob2));

        final MasterObjectList masterObjectList = new MasterObjectList();
        masterObjectList.setAggregating(false);
        masterObjectList.setBucketName(bucketName);
        masterObjectList.setCachedSizeInBytes(4);
        masterObjectList.setChunkClientProcessingOrderGuarantee(JobChunkClientProcessingOrderGuarantee.IN_ORDER);
        masterObjectList.setCompletedSizeInBytes(4);
        masterObjectList.setJobId(UUID.randomUUID());
        masterObjectList.setEntirelyInCache(false);
        masterObjectList.setName("name");
        masterObjectList.setNaked(false);
        masterObjectList.setOriginalSizeInBytes(4);
        masterObjectList.setPriority(Priority.NORMAL);
        masterObjectList.setRequestType(JobRequestType.PUT);
        masterObjectList.setStartDate(new Date());
        masterObjectList.setStatus(JobStatus.IN_PROGRESS);
        masterObjectList.setUserId(UUID.randomUUID());
        masterObjectList.setUserName("Gracie");
        masterObjectList.setNodes(Collections.singletonList(jobNode));
        masterObjectList.setObjects(Collections.singletonList(chunk));

        final Map<String, String> metadata = new HashMap<>();
        metadata.put("fileType", "text");

        final Ds3ClientHelpers.ObjectChannelBuilder objectChannelBuilder = new Ds3ClientHelpers.ObjectChannelBuilder() {
            @Override
            public SeekableByteChannel buildChannel(final String key) throws IOException {
                return new ByteArraySeekableByteChannel(4);
            }
        };

        final AllocateJobChunkSpectraS3Response allocateJobChunkSpectraS3Response = new AllocateJobChunkSpectraS3Response(
                chunk, 0, AllocateJobChunkSpectraS3Response.Status.ALLOCATED, "checksum", ChecksumType.Type.NONE
        );

        final Ds3Client ds3Client = Mockito.mock(Ds3Client.class);
        Mockito.when(ds3Client.allocateJobChunkSpectraS3(Mockito.any(AllocateJobChunkSpectraS3Request.class)))
                .thenReturn(allocateJobChunkSpectraS3Response);

        final AtomicInteger numBlobsForWhichWeGotMetadata = new AtomicInteger(0);

        final TransferStrategy transferStrategy = new TransferStrategyBuilder()
                .withDs3Client(ds3Client)
                .withMasterObjectList(masterObjectList)
                .withChannelBuilder(objectChannelBuilder)
                .withMetadataAccess(new MetadataAccess() {
                    @Override
                    public Map<String, String> getMetadataValue(final String filename) {
                        numBlobsForWhichWeGotMetadata.incrementAndGet();
                        return metadata;
                    }
                })
                .makePutTransferStrategy();

        transferStrategy.transfer();

        assertEquals(chunk.getObjects().size(), numBlobsForWhichWeGotMetadata.get());
    }
}
