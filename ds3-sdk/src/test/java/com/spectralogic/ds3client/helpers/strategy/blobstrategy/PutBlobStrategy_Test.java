package com.spectralogic.ds3client.helpers.strategy.blobstrategy;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Response;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.JobState;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcher;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PutBlobStrategy_Test {

    @Test
    public void testGetWorkWithDuplicates() throws IOException, InterruptedException {
        final Ds3Client client = mock(Ds3Client.class);
        final MasterObjectList mol = mock(MasterObjectList.class);
        final EventDispatcher eventDispatcher = mock(EventDispatcher.class);
        final ChunkAttemptRetryBehavior retryBehavior = mock(ChunkAttemptRetryBehavior.class);
        final ChunkAttemptRetryDelayBehavior retryDelayBehavior = mock(ChunkAttemptRetryDelayBehavior.class);

        final UUID jobId = UUID.randomUUID();
        when(mol.getJobId()).thenReturn(jobId);

        final UUID objectId = UUID.randomUUID();
        final BulkObject blob1 = new BulkObject();
        blob1.setId(objectId);
        blob1.setOffset(0);
        blob1.setInCache(false);

        final BulkObject blob2 = new BulkObject();
        blob2.setId(objectId);
        blob2.setOffset(100);
        blob2.setInCache(false);

        final Objects chunk = new Objects();
        chunk.setObjects(Lists.newArrayList(blob1, blob2));

        final MasterObjectList molResult = mock(MasterObjectList.class);
        when(molResult.getObjects()).thenReturn(Lists.newArrayList(chunk));

        final GetJobChunksReadyForClientProcessingSpectraS3Response realResponse = new GetJobChunksReadyForClientProcessingSpectraS3Response(molResult, 0, GetJobChunksReadyForClientProcessingSpectraS3Response.Status.AVAILABLE, null, null);
        when(client.getJobChunksReadyForClientProcessingSpectraS3(any(GetJobChunksReadyForClientProcessingSpectraS3Request.class)))
                .thenReturn(realResponse);

        final PutBlobStrategy strategy = new PutBlobStrategy(client, mol, eventDispatcher, retryBehavior, retryDelayBehavior, new JobState(molResult.getObjects()), false);

        final List<JobPart> work = Lists.newArrayList(strategy.getWork());

        assertThat(work.size(), is(2));
        assertThat(work.get(0).getBlob().getId(), is(objectId));
        assertThat(work.get(1).getBlob().getId(), is(objectId));
    }

    @Test
    public void testGetWorkWithDuplicatesSequential() throws IOException, InterruptedException {
        final Ds3Client client = mock(Ds3Client.class);
        final MasterObjectList mol = mock(MasterObjectList.class);
        final EventDispatcher eventDispatcher = mock(EventDispatcher.class);
        final ChunkAttemptRetryBehavior retryBehavior = mock(ChunkAttemptRetryBehavior.class);
        final ChunkAttemptRetryDelayBehavior retryDelayBehavior = mock(ChunkAttemptRetryDelayBehavior.class);

        final UUID jobId = UUID.randomUUID();
        when(mol.getJobId()).thenReturn(jobId);

        final UUID objectId = UUID.randomUUID();
        final BulkObject blob1 = new BulkObject();
        blob1.setId(objectId);
        blob1.setOffset(0);
        blob1.setInCache(false);

        final BulkObject blob2 = new BulkObject();
        blob2.setId(objectId);
        blob2.setOffset(100);
        blob2.setInCache(false);

        final Objects chunk = new Objects();
        chunk.setObjects(Lists.newArrayList(blob1, blob2));

        final MasterObjectList molResult = mock(MasterObjectList.class);
        when(molResult.getObjects()).thenReturn(Lists.newArrayList(chunk));

        final GetJobChunksReadyForClientProcessingSpectraS3Response realResponse = new GetJobChunksReadyForClientProcessingSpectraS3Response(molResult, 0, GetJobChunksReadyForClientProcessingSpectraS3Response.Status.AVAILABLE, null, null);

        when(client.getJobChunksReadyForClientProcessingSpectraS3(any(GetJobChunksReadyForClientProcessingSpectraS3Request.class)))
                .thenReturn(realResponse);

        final PutBlobStrategy strategy = new PutBlobStrategy(client, mol, eventDispatcher, retryBehavior, retryDelayBehavior, new JobState(molResult.getObjects()), true);

        final List<JobPart> work = Lists.newArrayList(strategy.getWork());

        assertThat(work.size(), is(1));
        assertThat(work.get(0).getBlob().getId(), is(objectId));
        assertThat(work.get(0).getBlob().getOffset(), is(0L));
    }

    @Test
    public void testGetWorkRetryLater() throws IOException, InterruptedException {
        final Ds3Client client = mock(Ds3Client.class);
        final MasterObjectList mol = mock(MasterObjectList.class);
        final EventDispatcher eventDispatcher = mock(EventDispatcher.class);
        final ChunkAttemptRetryBehavior retryBehavior = mock(ChunkAttemptRetryBehavior.class);
        final ChunkAttemptRetryDelayBehavior retryDelayBehavior = mock(ChunkAttemptRetryDelayBehavior.class);

        final UUID jobId = UUID.randomUUID();
        when(mol.getJobId()).thenReturn(jobId);

        final int retryAfterSeconds = 10;
        final GetJobChunksReadyForClientProcessingSpectraS3Response retryLaterResponse = new GetJobChunksReadyForClientProcessingSpectraS3Response(
                null, retryAfterSeconds, GetJobChunksReadyForClientProcessingSpectraS3Response.Status.RETRYLATER, null, null);

        final MasterObjectList molResult = mock(MasterObjectList.class);
        when(molResult.getObjects()).thenReturn(Lists.newArrayList());
        final GetJobChunksReadyForClientProcessingSpectraS3Response availableResponse = new GetJobChunksReadyForClientProcessingSpectraS3Response(
                molResult, 0, GetJobChunksReadyForClientProcessingSpectraS3Response.Status.AVAILABLE, null, null);

        when(client.getJobChunksReadyForClientProcessingSpectraS3(any(GetJobChunksReadyForClientProcessingSpectraS3Request.class)))
                .thenReturn(retryLaterResponse)
                .thenReturn(availableResponse);

        final PutBlobStrategy strategy = new PutBlobStrategy(client, mol, eventDispatcher, retryBehavior, retryDelayBehavior, new JobState(molResult.getObjects()), false);

        final List<JobPart> work = Lists.newArrayList(strategy.getWork());

        assertThat(work.isEmpty(), is(true));
        verify(retryBehavior).invoke();
        verify(retryDelayBehavior).delay(retryAfterSeconds);
        verify(retryBehavior).reset();
    }

    @Test
    public void testGetWorkFiltersInCache() throws IOException, InterruptedException {
        final Ds3Client client = mock(Ds3Client.class);
        final MasterObjectList mol = mock(MasterObjectList.class);
        final EventDispatcher eventDispatcher = mock(EventDispatcher.class);
        final ChunkAttemptRetryBehavior retryBehavior = mock(ChunkAttemptRetryBehavior.class);
        final ChunkAttemptRetryDelayBehavior retryDelayBehavior = mock(ChunkAttemptRetryDelayBehavior.class);

        final UUID jobId = UUID.randomUUID();
        when(mol.getJobId()).thenReturn(jobId);

        final BulkObject blob1 = new BulkObject();
        blob1.setId(UUID.randomUUID());
        blob1.setInCache(true);

        final BulkObject blob2 = new BulkObject();
        blob2.setId(UUID.randomUUID());
        blob2.setInCache(false);

        final Objects chunk = new Objects();
        chunk.setObjects(Lists.newArrayList(blob1, blob2));

        final MasterObjectList molResult = mock(MasterObjectList.class);
        when(molResult.getObjects()).thenReturn(Lists.newArrayList(chunk));

        final GetJobChunksReadyForClientProcessingSpectraS3Response response = new GetJobChunksReadyForClientProcessingSpectraS3Response(
                molResult, 0, GetJobChunksReadyForClientProcessingSpectraS3Response.Status.AVAILABLE, null, null);

        when(client.getJobChunksReadyForClientProcessingSpectraS3(any(GetJobChunksReadyForClientProcessingSpectraS3Request.class)))
                .thenReturn(response);

        final PutBlobStrategy strategy = new PutBlobStrategy(client, mol, eventDispatcher, retryBehavior, retryDelayBehavior, new JobState(molResult.getObjects()), false);

        final List<JobPart> work = Lists.newArrayList(strategy.getWork());

        assertThat(work.size(), is(1));
        assertThat(work.get(0).getBlob().getId(), is(blob2.getId()));
        verify(retryBehavior).reset();
    }

    @Test
    public void testBlobCompletedDoesNothing() {
        final Ds3Client client = mock(Ds3Client.class);
        final MasterObjectList mol = mock(MasterObjectList.class);
        final EventDispatcher eventDispatcher = mock(EventDispatcher.class);
        final ChunkAttemptRetryBehavior retryBehavior = mock(ChunkAttemptRetryBehavior.class);
        final ChunkAttemptRetryDelayBehavior retryDelayBehavior = mock(ChunkAttemptRetryDelayBehavior.class);

        final PutBlobStrategy strategy = new PutBlobStrategy(client, mol, eventDispatcher, retryBehavior, retryDelayBehavior, new JobState(mol.getObjects()), false);
        strategy.blobCompleted(new BulkObject());

        // We expect an interaction in the constructor of AbstractBlobStrategy
        verify(eventDispatcher).attachBlobTransferredEventObserver(any());

        verify(mol).getObjects();

        // No other interactions
        verifyNoMoreInteractions(client, mol, retryBehavior, retryDelayBehavior);
    }
    @Test
    public void testGetWorkEmptyResponse() throws IOException, InterruptedException {
        final Ds3Client client = mock(Ds3Client.class);
        final MasterObjectList mol = mock(MasterObjectList.class);
        final EventDispatcher eventDispatcher = mock(EventDispatcher.class);
        final ChunkAttemptRetryBehavior retryBehavior = mock(ChunkAttemptRetryBehavior.class);
        final ChunkAttemptRetryDelayBehavior retryDelayBehavior = mock(ChunkAttemptRetryDelayBehavior.class);

        final UUID jobId = UUID.randomUUID();
        when(mol.getJobId()).thenReturn(jobId);

        final MasterObjectList molResult = mock(MasterObjectList.class);
        when(molResult.getObjects()).thenReturn(Lists.newArrayList());

        final GetJobChunksReadyForClientProcessingSpectraS3Response response = new GetJobChunksReadyForClientProcessingSpectraS3Response(
                molResult, 0, GetJobChunksReadyForClientProcessingSpectraS3Response.Status.AVAILABLE, null, null);

        when(client.getJobChunksReadyForClientProcessingSpectraS3(any(GetJobChunksReadyForClientProcessingSpectraS3Request.class)))
                .thenReturn(response);

        final PutBlobStrategy strategy = new PutBlobStrategy(client, mol, eventDispatcher, retryBehavior, retryDelayBehavior, new JobState(molResult.getObjects()), false);

        final List<JobPart> work = Lists.newArrayList(strategy.getWork());

        assertThat(work.isEmpty(), is(true));
        verify(retryBehavior).reset();
    }
}
