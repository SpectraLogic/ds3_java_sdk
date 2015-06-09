package com.spectralogic.ds3client.utils;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetJobRequest;
import com.spectralogic.ds3client.commands.GetJobResponse;
import com.spectralogic.ds3client.commands.GetJobsRequest;
import com.spectralogic.ds3client.commands.GetJobsResponse;
import com.spectralogic.ds3client.models.bulk.*;
import com.spectralogic.ds3client.models.bulk.Objects;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.io.IOException;
import java.security.SignatureException;
import java.util.*;

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
        final GetJobsResponse jobsResponse = mock(GetJobsResponse.class);
        final GetJobResponse jobResponse = mock(GetJobResponse.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobInfo> jobs = new ArrayList<>();

        final JobInfo job1 = new JobInfo();
        job1.setJobId(realJobId);
        job1.setStatus(JobStatus.IN_PROGRESS);
        job1.setBucketName("bucket");
        job1.setRequestType(RequestType.PUT);
        jobs.add(job1);

        when(jobsResponse.getJobs()).thenReturn(jobs);

        final MasterObjectList mob = new MasterObjectList();
        mob.setStatus(JobStatus.IN_PROGRESS);
        mob.setBucketName("bucket");
        mob.setRequestType(RequestType.PUT);
        mob.setJobId(realJobId);

        final List<BulkObject> bulkObjects = Lists.newArrayList(new BulkObject("file1", 12, false, 0), new BulkObject("file2", 12, false, 0));

        final Objects chunk = new Objects();
        chunk.setObjects(bulkObjects);

        final List<Objects> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getMasterObjectList()).thenReturn(mob);

        when(client.getJobs(any(GetJobsRequest.class))).thenReturn(jobsResponse);
        when(client.getJob(new GetJobRequest(realJobId))).thenReturn(jobResponse);

        final Set<String> fileNames = Sets.newSet("file1");

        final List<UUID> jobId = JobUtils.findJob(client, RequestType.PUT, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(1));
        assertThat(jobId.get(0), is(realJobId));
    }

    @Test
    public void findFileWithTwoJobsWithDifferentBuckets() throws IOException, SignatureException {
        final Ds3Client client = mock(Ds3Client.class);
        final GetJobsResponse jobsResponse = mock(GetJobsResponse.class);
        final GetJobResponse jobResponse = mock(GetJobResponse.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobInfo> jobs = new ArrayList<>();

        final JobInfo job1 = new JobInfo();
        job1.setJobId(UUID.randomUUID());
        job1.setStatus(JobStatus.IN_PROGRESS);
        job1.setBucketName("bucket2");
        job1.setRequestType(RequestType.PUT);
        jobs.add(job1);

        final JobInfo job2 = new JobInfo();
        job2.setJobId(realJobId);
        job2.setStatus(JobStatus.IN_PROGRESS);
        job2.setBucketName("bucket");
        job2.setRequestType(RequestType.PUT);
        jobs.add(job2);

        when(jobsResponse.getJobs()).thenReturn(jobs);

        final MasterObjectList mob = new MasterObjectList();
        mob.setStatus(JobStatus.IN_PROGRESS);
        mob.setBucketName("bucket");
        mob.setJobId(realJobId);
        mob.setRequestType(RequestType.PUT);

        final List<BulkObject> bulkObjects = Lists.newArrayList(new BulkObject("file1", 12, false, 0), new BulkObject("file2", 12, false, 0));

        final Objects chunk = new Objects();
        chunk.setObjects(bulkObjects);

        final List<Objects> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getMasterObjectList()).thenReturn(mob);

        when(client.getJobs(any(GetJobsRequest.class))).thenReturn(jobsResponse);
        when(client.getJob(new GetJobRequest(realJobId))).thenReturn(jobResponse);

        final Set<String> fileNames = Sets.newSet("file1");

        final List<UUID> jobId = JobUtils.findJob(client, RequestType.PUT, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(1));
        assertThat(jobId.get(0), is(realJobId));
    }

    @Test
    public void findFileWithTwoJobsWithDifferentStatuses() throws IOException, SignatureException {
        final Ds3Client client = mock(Ds3Client.class);
        final GetJobsResponse jobsResponse = mock(GetJobsResponse.class);
        final GetJobResponse jobResponse = mock(GetJobResponse.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobInfo> jobs = new ArrayList<>();

        final JobInfo job1 = new JobInfo();
        job1.setJobId(UUID.randomUUID());
        job1.setStatus(JobStatus.COMPLETED);
        job1.setBucketName("bucket");
        job1.setRequestType(RequestType.PUT);
        jobs.add(job1);

        final JobInfo job2 = new JobInfo();
        job2.setJobId(realJobId);
        job2.setStatus(JobStatus.IN_PROGRESS);
        job2.setBucketName("bucket");
        job2.setRequestType(RequestType.PUT);
        jobs.add(job2);

        when(jobsResponse.getJobs()).thenReturn(jobs);

        final MasterObjectList mob = new MasterObjectList();
        mob.setStatus(JobStatus.IN_PROGRESS);
        mob.setBucketName("bucket");
        mob.setJobId(realJobId);
        mob.setRequestType(RequestType.PUT);

        final List<BulkObject> bulkObjects = Lists.newArrayList(new BulkObject("file1", 12, false, 0), new BulkObject("file2", 12, false, 0));

        final Objects chunk = new Objects();
        chunk.setObjects(bulkObjects);

        final List<Objects> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getMasterObjectList()).thenReturn(mob);

        when(client.getJobs(any(GetJobsRequest.class))).thenReturn(jobsResponse);
        when(client.getJob(new GetJobRequest(realJobId))).thenReturn(jobResponse);

        final Set<String> fileNames = Sets.newSet("file1");

        final List<UUID> jobId = JobUtils.findJob(client, RequestType.PUT, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(1));
        assertThat(jobId.get(0), is(realJobId));
    }

    @Test
    public void findFileNotInJob() throws IOException, SignatureException {
        final Ds3Client client = mock(Ds3Client.class);
        final GetJobsResponse jobsResponse = mock(GetJobsResponse.class);
        final GetJobResponse jobResponse = mock(GetJobResponse.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobInfo> jobs = new ArrayList<>();

        final JobInfo job1 = new JobInfo();
        job1.setJobId(UUID.randomUUID());
        job1.setStatus(JobStatus.COMPLETED);
        job1.setBucketName("bucket");
        job1.setRequestType(RequestType.PUT);
        jobs.add(job1);

        final JobInfo job2 = new JobInfo();
        job2.setJobId(realJobId);
        job2.setStatus(JobStatus.IN_PROGRESS);
        job2.setBucketName("bucket");
        job2.setRequestType(RequestType.PUT);
        jobs.add(job2);

        when(jobsResponse.getJobs()).thenReturn(jobs);

        final MasterObjectList mob = new MasterObjectList();
        mob.setStatus(JobStatus.IN_PROGRESS);
        mob.setBucketName("bucket");
        mob.setJobId(realJobId);
        mob.setRequestType(RequestType.PUT);

        final List<BulkObject> bulkObjects = Lists.newArrayList(new BulkObject("file1", 12, false, 0), new BulkObject("file2", 12, false, 0));

        final Objects chunk = new Objects();
        chunk.setObjects(bulkObjects);

        final List<Objects> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getMasterObjectList()).thenReturn(mob);

        when(client.getJobs(any(GetJobsRequest.class))).thenReturn(jobsResponse);
        when(client.getJob(new GetJobRequest(realJobId))).thenReturn(jobResponse);

        final Set<String> fileNames = Sets.newSet("file3");

        final List<UUID> jobId = JobUtils.findJob(client, RequestType.PUT, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(0));
    }

    @Test
    public void findFileInOtherJobWithMultipleChunks() throws IOException, SignatureException {
        final Ds3Client client = mock(Ds3Client.class);
        final GetJobsResponse jobsResponse = mock(GetJobsResponse.class);
        final GetJobResponse jobResponse1 = mock(GetJobResponse.class);
        final GetJobResponse jobResponse2 = mock(GetJobResponse.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobInfo> jobs = new ArrayList<>();

        final JobInfo job1 = new JobInfo();
        job1.setJobId(UUID.randomUUID());
        job1.setStatus(JobStatus.IN_PROGRESS);
        job1.setBucketName("bucket");
        job1.setRequestType(RequestType.PUT);
        jobs.add(job1);

        final MasterObjectList mob1 = new MasterObjectList();
        mob1.setStatus(JobStatus.IN_PROGRESS);
        mob1.setBucketName("bucket");
        mob1.setJobId(job1.getJobId());
        mob1.setRequestType(RequestType.PUT);

        final List<BulkObject> bulkObjects1 = Lists.newArrayList(new BulkObject("file.something", 125, false, 0));

        final Objects chunk1 = new Objects();
        chunk1.setObjects(bulkObjects1);

        final List<Objects> chunks1 = new ArrayList<>();
        chunks1.add(chunk1);

        mob1.setObjects(chunks1);

        final JobInfo job2 = new JobInfo();
        job2.setJobId(realJobId);
        job2.setStatus(JobStatus.IN_PROGRESS);
        job2.setBucketName("bucket");
        job2.setRequestType(RequestType.PUT);
        jobs.add(job2);

        when(jobsResponse.getJobs()).thenReturn(jobs);

        final MasterObjectList mob2 = new MasterObjectList();
        mob2.setStatus(JobStatus.IN_PROGRESS);
        mob2.setBucketName("bucket");
        mob2.setJobId(realJobId);
        mob2.setRequestType(RequestType.PUT);

        final List<BulkObject> bulkObjects2 = Lists.newArrayList(new BulkObject("file1", 12, false, 0), new BulkObject("file2", 12, false, 0));
        final List<BulkObject> bulkObjects3 = Lists.newArrayList(new BulkObject("file3", 12, false, 0));

        final Objects chunk2 = new Objects();
        chunk2.setObjects(bulkObjects2);

        final Objects chunk3 = new Objects();
        chunk3.setObjects(bulkObjects3);

        final List<Objects> chunks2 = new ArrayList<>();
        chunks2.add(chunk2);
        chunks2.add(chunk3);

        mob2.setObjects(chunks2);

        when(jobResponse1.getMasterObjectList()).thenReturn(mob1);
        when(jobResponse2.getMasterObjectList()).thenReturn(mob2);

        when(client.getJobs(any(GetJobsRequest.class))).thenReturn(jobsResponse);
        when(client.getJob(new GetJobRequest(job1.getJobId()))).thenReturn(jobResponse1);
        when(client.getJob(new GetJobRequest(job2.getJobId()))).thenReturn(jobResponse2);

        final Set<String> fileNames = Sets.newSet("file3");

        final List<UUID> jobId = JobUtils.findJob(client, RequestType.PUT, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(1));
        assertThat(jobId.get(0), is(realJobId));
    }

    @Test
    public void findFileInMultipleJobs() throws IOException, SignatureException {
        final Ds3Client client = mock(Ds3Client.class);
        final GetJobsResponse jobsResponse = mock(GetJobsResponse.class);
        final GetJobResponse jobResponse1 = mock(GetJobResponse.class);
        final GetJobResponse jobResponse2 = mock(GetJobResponse.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobInfo> jobs = new ArrayList<>();

        final JobInfo job1 = new JobInfo();
        job1.setJobId(UUID.randomUUID());
        job1.setStatus(JobStatus.IN_PROGRESS);
        job1.setBucketName("bucket");
        job1.setRequestType(RequestType.GET);
        jobs.add(job1);

        final MasterObjectList mob1 = new MasterObjectList();
        mob1.setStatus(JobStatus.IN_PROGRESS);
        mob1.setBucketName("bucket");
        mob1.setJobId(job1.getJobId());
        mob1.setRequestType(RequestType.GET);

        final List<BulkObject> bulkObjects1 = Lists.newArrayList(new BulkObject("file3", 12, false, 0));

        final Objects chunk1 = new Objects();
        chunk1.setObjects(bulkObjects1);

        final List<Objects> chunks1 = new ArrayList<>();
        chunks1.add(chunk1);

        mob1.setObjects(chunks1);

        final JobInfo job2 = new JobInfo();
        job2.setJobId(realJobId);
        job2.setStatus(JobStatus.IN_PROGRESS);
        job2.setBucketName("bucket");
        job2.setRequestType(RequestType.GET);
        jobs.add(job2);

        when(jobsResponse.getJobs()).thenReturn(jobs);

        final MasterObjectList mob2 = new MasterObjectList();
        mob2.setStatus(JobStatus.IN_PROGRESS);
        mob2.setBucketName("bucket");
        mob2.setJobId(realJobId);
        mob2.setRequestType(RequestType.GET);

        final List<BulkObject> bulkObjects2 = Lists.newArrayList(new BulkObject("file1", 12, false, 0), new BulkObject("file2", 12, false, 0));
        final List<BulkObject> bulkObjects3 = Lists.newArrayList(new BulkObject("file3", 12, false, 0));

        final Objects chunk2 = new Objects();
        chunk2.setObjects(bulkObjects2);

        final Objects chunk3 = new Objects();
        chunk3.setObjects(bulkObjects3);

        final List<Objects> chunks2 = new ArrayList<>();
        chunks2.add(chunk2);
        chunks2.add(chunk3);

        mob2.setObjects(chunks2);

        when(jobResponse1.getMasterObjectList()).thenReturn(mob1);
        when(jobResponse2.getMasterObjectList()).thenReturn(mob2);

        when(client.getJobs(any(GetJobsRequest.class))).thenReturn(jobsResponse);
        when(client.getJob(new GetJobRequest(job1.getJobId()))).thenReturn(jobResponse1);
        when(client.getJob(new GetJobRequest(job2.getJobId()))).thenReturn(jobResponse2);

        final Set<String> fileNames = Sets.newSet("file3");

        final List<UUID> jobId = JobUtils.findJob(client, RequestType.GET, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(2));
    }
 @Test
    public void findFileWithDifferentRequestTypes() throws IOException, SignatureException {
        final Ds3Client client = mock(Ds3Client.class);
        final GetJobsResponse jobsResponse = mock(GetJobsResponse.class);
        final GetJobResponse jobResponse = mock(GetJobResponse.class);

        final UUID realJobId = UUID.randomUUID();
        final List<JobInfo> jobs = new ArrayList<>();

        final JobInfo job1 = new JobInfo();
        job1.setJobId(UUID.randomUUID());
        job1.setStatus(JobStatus.IN_PROGRESS);
        job1.setBucketName("bucket");
        job1.setRequestType(RequestType.GET);
        jobs.add(job1);

        final JobInfo job2 = new JobInfo();
        job2.setJobId(realJobId);
        job2.setStatus(JobStatus.IN_PROGRESS);
        job2.setBucketName("bucket");
        job2.setRequestType(RequestType.PUT);
        jobs.add(job2);

        when(jobsResponse.getJobs()).thenReturn(jobs);

        final MasterObjectList mob = new MasterObjectList();
        mob.setStatus(JobStatus.IN_PROGRESS);
        mob.setBucketName("bucket");
        mob.setJobId(realJobId);
        mob.setRequestType(RequestType.PUT);

        final List<BulkObject> bulkObjects = Lists.newArrayList(new BulkObject("file1", 12, false, 0), new BulkObject("file2", 12, false, 0));

        final Objects chunk = new Objects();
        chunk.setObjects(bulkObjects);

        final List<Objects> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getMasterObjectList()).thenReturn(mob);

        when(client.getJobs(any(GetJobsRequest.class))).thenReturn(jobsResponse);
        when(client.getJob(new GetJobRequest(realJobId))).thenReturn(jobResponse);

        final Set<String> fileNames = Sets.newSet("file1");

        final List<UUID> jobId = JobUtils.findJob(client, RequestType.PUT, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(1));
        assertThat(jobId.get(0), is(realJobId));
    }
}
