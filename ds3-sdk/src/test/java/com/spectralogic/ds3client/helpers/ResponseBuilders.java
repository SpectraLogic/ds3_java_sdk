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

import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseBuilders {
    public static CreateGetJobSpectraS3Response bulkGetResponse(final JobWithChunksApiBean jobWithChunksApiBean) {
        final CreateGetJobSpectraS3Response response = mock(CreateGetJobSpectraS3Response.class);
        when(response.getResult()).thenReturn(jobWithChunksApiBean);
        return response;
    }

    public static CreatePutJobSpectraS3Response bulkPutResponse(final JobWithChunksApiBean masterObjectList) {
        final CreatePutJobSpectraS3Response response = mock(CreatePutJobSpectraS3Response.class);
        when(response.getResult()).thenReturn(masterObjectList);
        return response;
    }

    public static ModifyJobSpectraS3Response modifyJobResponse(final JobWithChunksApiBean masterObjectList) {
        final ModifyJobSpectraS3Response response = mock(ModifyJobSpectraS3Response.class);
        when(response.getJobWithChunksContainerApiBeanResult().getJob()).thenReturn(masterObjectList);
        return response;
    }

    public static GetJobChunksReadyForClientProcessingSpectraS3Response availableJobChunks(final JobWithChunksApiBean jobWithChunksApiBean) {
        final GetJobChunksReadyForClientProcessingSpectraS3Response response = mock(GetJobChunksReadyForClientProcessingSpectraS3Response.class);
        when(response.getStatus()).thenReturn(GetJobChunksReadyForClientProcessingSpectraS3Response.Status.AVAILABLE);
        when(response.getJobWithChunksContainerApiBeanResult().getJob()).thenReturn(jobWithChunksApiBean);
        return response;
    }
    
    public static GetJobChunksReadyForClientProcessingSpectraS3Response retryGetAvailableAfter(final int retryAfterInSeconds) {
        final GetJobChunksReadyForClientProcessingSpectraS3Response response = mock(GetJobChunksReadyForClientProcessingSpectraS3Response.class);
        when(response.getStatus()).thenReturn(GetJobChunksReadyForClientProcessingSpectraS3Response.Status.RETRYLATER);
        when(response.getRetryAfterSeconds()).thenReturn(retryAfterInSeconds);
        return response;
    }
    
    public static AllocateJobChunkSpectraS3Response allocated(final JobChunkApiBean chunk) {
        final AllocateJobChunkSpectraS3Response response = mock(AllocateJobChunkSpectraS3Response.class);
        when(response.getStatus()).thenReturn(AllocateJobChunkSpectraS3Response.Status.ALLOCATED);
        when(response.getJobChunkContainerApiBeanResult().getJobChunk()).thenReturn(chunk);
        return response;
    }
    
    public static AllocateJobChunkSpectraS3Response retryAllocateLater(final int retryAfterInSeconds) {
        final AllocateJobChunkSpectraS3Response response = mock(AllocateJobChunkSpectraS3Response.class);
        when(response.getStatus()).thenReturn(AllocateJobChunkSpectraS3Response.Status.RETRYLATER);
        when(response.getRetryAfterSeconds()).thenReturn(retryAfterInSeconds);
        return response;
    }
    
    public static JobWithChunksApiBean jobResponse(
            final UUID jobId,
            final String bucketName,
            final JobRequestType requestType,
            final long originalSizeInBytes,
            final long cachedSizeInBytes,
            final long completedSizeInBytes,
            final JobChunkClientProcessingOrderGuarantee chunkClientProcessingOrderGuarantee,
            final BlobStoreTaskPriority priority,
            final String startDate,
            final UUID userId,
            final String userName,
            final WriteOptimization writeOptimization,
            final List<NodeApiBean> nodes,
            final List<JobChunkApiBean> objects) {
        return new JobWithChunksApiBean(
                bucketName,
                cachedSizeInBytes,
                chunkClientProcessingOrderGuarantee,
                completedSizeInBytes,
                jobId,
                null, //TODO verify assumption of default value
                nodes,
                objects,
                originalSizeInBytes,
                priority,
                requestType,
                Date.from(Instant.parse(startDate)),
                null, //TODO verify assumption of default value
                userId,
                userName,
                writeOptimization
        );
    }
    
    public static NodeApiBean basicNode(final UUID nodeId, final String endpoint) {
        return node(nodeId, endpoint, 80, 443);
    }

    public static NodeApiBean node(
            final UUID nodeId,
            final String endpoint,
            final int httpPort,
            final int httpsPort) {
        return new NodeApiBean(
                endpoint, httpPort, httpsPort, nodeId);
    }

    public static JobChunkApiBean chunk(
            final int chunkNumber,
            final UUID chunkId,
            final UUID nodeId,
            final BlobApiBean ... chunkList) {
        return new JobChunkApiBean(
                chunkId, chunkNumber, nodeId, Arrays.asList(chunkList));
    }
    
    public static BlobApiBean object(
            final String name,
            final long offset,
            final long length,
            final boolean inCache) {
        return new BlobApiBean(
                UUID.randomUUID(), //TODO verify that this is valid default value for testing
                inCache,
                true,
                length,
                name,
                offset,
                null, //TODO verify that this is valid default value for testing
                1
        );
    }
    
    public static Answer<GetObjectResponse> getObjectAnswer(final String result) {
        return new Answer<GetObjectResponse>() {
            @Override
            public GetObjectResponse answer(final InvocationOnMock invocation) throws Throwable {
                writeToChannel(result, ((GetObjectRequest)invocation.getArguments()[0]).getChannel());
                return mock(GetObjectResponse.class);
            }
        };
    }
    
    public static SeekableByteChannel channelWithContents(final String contents) {
        return writeToChannel(contents, new ByteArraySeekableByteChannel());
    }

    private static <T extends WritableByteChannel> T writeToChannel(final String contents, final T channel) {
        final Writer writer = Channels.newWriter(channel, "UTF-8");
        try {
            writer.write(contents);
            writer.flush();
        } catch (final IOException e) {
            throw new RuntimeException();
        }
        return channel;
    }
}
