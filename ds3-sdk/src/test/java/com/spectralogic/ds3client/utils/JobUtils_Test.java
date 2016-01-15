package com.spectralogic.ds3client.utils;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.GetJobSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobSpectraS3Response;
import com.spectralogic.ds3client.commands.spectrads3.GetJobsSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobsSpectraS3Response;
import com.spectralogic.ds3client.models.*;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JobUtils_Test {

    @Test
    public void findFile() throws IOException, SignatureException {
        final Ds3Client client = mock(Ds3Client.class);
        final GetJobsSpectraS3Response jobsResponse = mock(GetJobsSpectraS3Response.class);
        final GetJobSpectraS3Response jobResponse = mock(GetJobSpectraS3Response.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobApiBean> jobs = new ArrayList<>();

        final JobApiBean job1 = new JobApiBean("bucket", 0, null, 0, realJobId, null, null, 0, null,
                JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);

        jobs.add(job1);

        when(jobsResponse.getJobsApiBeanResult().getJobs()).thenReturn(jobs);

        final JobWithChunksApiBean mob = new JobWithChunksApiBean("bucket", 0, null, 0, realJobId, null, null,
                null, 0, null, JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);

        final List<BlobApiBean> bulkObjects = Lists.newArrayList(
                new BlobApiBean(null, false, true, 12, "file1", 0, null, 1),
                new BlobApiBean(null, false, true, 12, "file2", 0, null, 1));

        final JobChunkApiBean chunk = new JobChunkApiBean(null, 0, null, bulkObjects);

        final List<JobChunkApiBean> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getJobWithChunksContainerApiBeanResult().getJob()).thenReturn(mob);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(realJobId))).thenReturn(jobResponse);

        final Set<String> fileNames = Sets.newSet("file1");

        final List<UUID> jobId = JobUtils.findJob(client, JobRequestType.PUT, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(1));
        assertThat(jobId.get(0), is(realJobId));
    }

    @Test
    public void findFileWithTwoJobsWithDifferentBuckets() throws IOException, SignatureException {
        final Ds3Client client = mock(Ds3Client.class);
        final GetJobsSpectraS3Response jobsResponse = mock(GetJobsSpectraS3Response.class);
        final GetJobSpectraS3Response jobResponse = mock(GetJobSpectraS3Response.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobApiBean> jobs = new ArrayList<>();

        final JobApiBean job1 = new JobApiBean(null, 0, null, 0, UUID.randomUUID(), "bucket2", null, 0, null,
                JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);
        jobs.add(job1);

        final JobApiBean job2 = new JobApiBean(null, 0, null, 0, realJobId, "bucket", null, 0, null,
                JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);
        jobs.add(job2);

        when(jobsResponse.getJobsApiBeanResult().getJobs()).thenReturn(jobs);

        final JobWithChunksApiBean mob = new JobWithChunksApiBean("bucket", 0, null, 0, realJobId, null, null,
                null, 0, null, JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);

        final List<BlobApiBean> bulkObjects = Lists.newArrayList(
                new BlobApiBean(null, false, true, 12, "file1", 0, null, 1),
                new BlobApiBean(null, false, true, 12, "file2", 0, null, 1));

        final JobChunkApiBean chunk = new JobChunkApiBean(null, 0, null, bulkObjects);

        final List<JobChunkApiBean> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getJobWithChunksContainerApiBeanResult().getJob()).thenReturn(mob);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(realJobId))).thenReturn(jobResponse);

        final Set<String> fileNames = Sets.newSet("file1");

        final List<UUID> jobId = JobUtils.findJob(client, JobRequestType.PUT, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(1));
        assertThat(jobId.get(0), is(realJobId));
    }

    @Test
    public void findFileWithTwoJobsWithDifferentStatuses() throws IOException, SignatureException {
        final Ds3Client client = mock(Ds3Client.class);
        final GetJobsSpectraS3Response jobsResponse = mock(GetJobsSpectraS3Response.class);
        final GetJobSpectraS3Response jobResponse = mock(GetJobSpectraS3Response.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobApiBean> jobs = new ArrayList<>();

        final JobApiBean job1 = new JobApiBean(null, 0, null, 0, UUID.randomUUID(), "bucket", null, 0, null,
                JobRequestType.PUT, null, JobStatus.COMPLETED, null, null, null);
        jobs.add(job1);

        final JobApiBean job2 = new JobApiBean(null, 0, null, 0, realJobId, "bucket", null, 0, null,
                JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);
        jobs.add(job2);

        when(jobsResponse.getJobsApiBeanResult().getJobs()).thenReturn(jobs);

        final JobWithChunksApiBean mob = new JobWithChunksApiBean("bucket", 0, null, 0, realJobId, null, null,
                null, 0, null, JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);

        final List<BlobApiBean> bulkObjects = Lists.newArrayList(
                new BlobApiBean(null, false, true, 12, "file1", 0, null, 1),
                new BlobApiBean(null, false, true, 12, "file2", 0, null, 1));

        final JobChunkApiBean chunk = new JobChunkApiBean(null, 0, null, bulkObjects);

        final List<JobChunkApiBean> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getJobWithChunksContainerApiBeanResult().getJob()).thenReturn(mob);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(realJobId))).thenReturn(jobResponse);

        final Set<String> fileNames = Sets.newSet("file1");

        final List<UUID> jobId = JobUtils.findJob(client, JobRequestType.PUT, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(1));
        assertThat(jobId.get(0), is(realJobId));
    }

    @Test
    public void findFileNotInJob() throws IOException, SignatureException {
        final Ds3Client client = mock(Ds3Client.class);
        final GetJobsSpectraS3Response jobsResponse = mock(GetJobsSpectraS3Response.class);
        final GetJobSpectraS3Response jobResponse = mock(GetJobSpectraS3Response.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobApiBean> jobs = new ArrayList<>();

        final JobApiBean job1 = new JobApiBean(null, 0, null, 0, UUID.randomUUID(), "bucket", null, 0, null,
                JobRequestType.PUT, null, JobStatus.COMPLETED, null, null, null);
        jobs.add(job1);

        final JobApiBean job2 = new JobApiBean(null, 0, null, 0, realJobId, "bucket", null, 0, null,
                JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);
        jobs.add(job2);

        when(jobsResponse.getJobsApiBeanResult().getJobs()).thenReturn(jobs);

        final JobWithChunksApiBean mob = new JobWithChunksApiBean("bucket", 0, null, 0, realJobId, null, null,
                null, 0, null, JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);

        final List<BlobApiBean> bulkObjects = Lists.newArrayList(
                new BlobApiBean(null, false, true, 12, "file1", 0, null, 1),
                new BlobApiBean(null, false, true, 12, "file2", 0, null, 1)
        );

        final JobChunkApiBean chunk = new JobChunkApiBean(null, 0, null, bulkObjects);
        chunk.setObjects(bulkObjects);

        final List<JobChunkApiBean> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getJobWithChunksContainerApiBeanResult().getJob()).thenReturn(mob);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(realJobId))).thenReturn(jobResponse);

        final Set<String> fileNames = Sets.newSet("file3");

        final List<UUID> jobId = JobUtils.findJob(client, JobRequestType.PUT, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(0));
    }

    @Test
    public void findFileInOtherJobWithMultipleChunks() throws IOException, SignatureException {
        final Ds3Client client = mock(Ds3Client.class);
        final GetJobsSpectraS3Response jobsResponse = mock(GetJobsSpectraS3Response.class);
        final GetJobSpectraS3Response jobResponse1 = mock(GetJobSpectraS3Response.class);
        final GetJobSpectraS3Response jobResponse2 = mock(GetJobSpectraS3Response.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobApiBean> jobs = new ArrayList<>();

        final JobApiBean job1 = new JobApiBean(null, 0, null, 0, UUID.randomUUID(), "bucket", null, 0, null,
                JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);
        jobs.add(job1);

        final JobWithChunksApiBean mob1 = new JobWithChunksApiBean("bucket", 0, null, 0, job1.getJobId(), null, null,
                null, 0, null, JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);

        final List<BlobApiBean> bulkObjects1 = Lists.newArrayList(
                new BlobApiBean(null, false, true, 125, "file.something", 0, null, 1));

        final JobChunkApiBean chunk1 = new JobChunkApiBean(null, 0, null, bulkObjects1);

        final List<JobChunkApiBean> chunks1 = new ArrayList<>();
        chunks1.add(chunk1);

        mob1.setObjects(chunks1);

        final JobApiBean job2 = new JobApiBean(null, 0, null, 0, realJobId, "bucket", null, 0, null,
                JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);
        jobs.add(job2);

        when(jobsResponse.getJobsApiBeanResult().getJobs()).thenReturn(jobs);

        final JobWithChunksApiBean mob2 = new JobWithChunksApiBean("bucket", 0, null, 0, realJobId, null, null,
                null, 0, null, JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);

        final List<BlobApiBean> bulkObjects2 = Lists.newArrayList(
                new BlobApiBean(null, false, true, 12, "file1", 0, null, 1),
                new BlobApiBean(null, false, true, 12, "file2", 0, null, 1));
        final List<BlobApiBean> bulkObjects3 = Lists.newArrayList(
                new BlobApiBean(null, false, true, 12, "file3", 0, null, 1));

        final JobChunkApiBean chunk2 = new JobChunkApiBean(null, 0, null, bulkObjects2);

        final JobChunkApiBean chunk3 = new JobChunkApiBean(null, 0, null, bulkObjects3);

        final List<JobChunkApiBean> chunks2 = new ArrayList<>();
        chunks2.add(chunk2);
        chunks2.add(chunk3);

        mob2.setObjects(chunks2);

        when(jobResponse1.getJobWithChunksContainerApiBeanResult().getJob()).thenReturn(mob1);
        when(jobResponse2.getJobWithChunksContainerApiBeanResult().getJob()).thenReturn(mob2);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(job1.getJobId()))).thenReturn(jobResponse1);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(job2.getJobId()))).thenReturn(jobResponse2);

        final Set<String> fileNames = Sets.newSet("file3");

        final List<UUID> jobId = JobUtils.findJob(client, JobRequestType.PUT, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(1));
        assertThat(jobId.get(0), is(realJobId));
    }

    @Test
    public void findFileInMultipleJobs() throws IOException, SignatureException {
        final Ds3Client client = mock(Ds3Client.class);
        final GetJobsSpectraS3Response jobsResponse = mock(GetJobsSpectraS3Response.class);
        final GetJobSpectraS3Response jobResponse1 = mock(GetJobSpectraS3Response.class);
        final GetJobSpectraS3Response jobResponse2 = mock(GetJobSpectraS3Response.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobApiBean> jobs = new ArrayList<>();

        final JobApiBean job1 = new JobApiBean(null, 0, null, 0, UUID.randomUUID(), "bucket", null, 0, null,
                JobRequestType.GET, null, JobStatus.IN_PROGRESS, null, null, null);
        jobs.add(job1);

        final JobWithChunksApiBean mob1 = new JobWithChunksApiBean("bucket", 0, null, 0, job1.getJobId(), null, null,
                null, 0, null, JobRequestType.GET, null, JobStatus.IN_PROGRESS, null, null, null);

        final List<BlobApiBean> bulkObjects1 = Lists.newArrayList(
                new BlobApiBean(null, false, true, 12, "file3", 0, null, 1));

        final JobChunkApiBean chunk1 = new JobChunkApiBean(null, 0, null, bulkObjects1);

        final List<JobChunkApiBean> chunks1 = new ArrayList<>();
        chunks1.add(chunk1);

        mob1.setObjects(chunks1);

        final JobApiBean job2 = new JobApiBean(null, 0, null, 0, realJobId, "bucket", null, 0, null,
                JobRequestType.GET, null, JobStatus.IN_PROGRESS, null, null, null);
        jobs.add(job2);

        when(jobsResponse.getJobsApiBeanResult().getJobs()).thenReturn(jobs);

        final JobWithChunksApiBean mob2 = new JobWithChunksApiBean("bucket", 0, null, 0, realJobId, null, null,
                null, 0, null, JobRequestType.GET, null, JobStatus.IN_PROGRESS, null, null, null);

        final List<BlobApiBean> bulkObjects2 = Lists.newArrayList(
                new BlobApiBean(null, false, true, 12, "file1", 0, null, 1),
                new BlobApiBean(null, false, true, 12, "file2", 0, null, 1));
        final List<BlobApiBean> bulkObjects3 = Lists.newArrayList(
                new BlobApiBean(null, false, true, 12, "file3", 0, null, 1));

        final JobChunkApiBean chunk2 = new JobChunkApiBean(null, 0, null, bulkObjects2);

        final JobChunkApiBean chunk3 = new JobChunkApiBean(null, 0, null, bulkObjects3);

        final List<JobChunkApiBean> chunks2 = new ArrayList<>();
        chunks2.add(chunk2);
        chunks2.add(chunk3);

        mob2.setObjects(chunks2);

        when(jobResponse1.getJobWithChunksContainerApiBeanResult().getJob()).thenReturn(mob1);
        when(jobResponse2.getJobWithChunksContainerApiBeanResult().getJob()).thenReturn(mob2);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(job1.getJobId()))).thenReturn(jobResponse1);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(job2.getJobId()))).thenReturn(jobResponse2);

        final Set<String> fileNames = Sets.newSet("file3");

        final List<UUID> jobId = JobUtils.findJob(client, JobRequestType.GET, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(2));
    }
 @Test
    public void findFileWithDifferentRequestTypes() throws IOException, SignatureException {
        final Ds3Client client = mock(Ds3Client.class);
        final GetJobsSpectraS3Response jobsResponse = mock(GetJobsSpectraS3Response.class);
        final GetJobSpectraS3Response jobResponse = mock(GetJobSpectraS3Response.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobApiBean> jobs = new ArrayList<>();

        final JobApiBean job1 = new JobApiBean(null, 0, null, 0, UUID.randomUUID(), "bucket", null, 0, null,
                JobRequestType.GET, null, JobStatus.IN_PROGRESS, null, null, null);
        jobs.add(job1);

        final JobApiBean job2 = new JobApiBean(null, 0, null, 0, realJobId, "bucket", null, 0, null,
                JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);
        jobs.add(job2);

        when(jobsResponse.getJobsApiBeanResult().getJobs()).thenReturn(jobs);

        final JobWithChunksApiBean mob = new JobWithChunksApiBean("bucket", 0, null, 0, realJobId, null, null,
                null, 0, null, JobRequestType.PUT, null, JobStatus.IN_PROGRESS, null, null, null);

        final List<BlobApiBean> bulkObjects = Lists.newArrayList(
                new BlobApiBean(null, false, true, 12, "file1", 0, null, 1),
                new BlobApiBean(null, false, true, 12, "file2", 0, null, 1));

        final JobChunkApiBean chunk = new JobChunkApiBean(null, 0, null, bulkObjects);

        final List<JobChunkApiBean> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getJobWithChunksContainerApiBeanResult().getJob()).thenReturn(mob);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(realJobId))).thenReturn(jobResponse);

        final Set<String> fileNames = Sets.newSet("file1");

        final List<UUID> jobId = JobUtils.findJob(client, JobRequestType.PUT, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(1));
        assertThat(jobId.get(0), is(realJobId));
    }
}
