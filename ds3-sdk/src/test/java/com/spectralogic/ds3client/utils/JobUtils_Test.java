/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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
        final List<Job> jobs = new ArrayList<>();

        final Job job1 = new Job();
        job1.setJobId(realJobId);
        job1.setStatus(JobStatus.IN_PROGRESS);
        job1.setBucketName("bucket");
        job1.setRequestType(JobRequestType.PUT);
        jobs.add(job1);

        final JobList container = new JobList();
        container.setJobs(jobs);
        when(jobsResponse.getJobListResult()).thenReturn(container);

        final MasterObjectList mob = new MasterObjectList();
        mob.setStatus(JobStatus.IN_PROGRESS);
        mob.setBucketName("bucket");
        mob.setRequestType(JobRequestType.PUT);
        mob.setJobId(realJobId);

        final BulkObject blob1 = new BulkObject();
        blob1.setName("file1");
        blob1.setLength(12);
        blob1.setInCache(false);
        blob1.setOffset(0);

        final BulkObject blob2 = new BulkObject();
        blob2.setName("file2");
        blob2.setLength(12);
        blob2.setInCache(false);
        blob2.setOffset(0);

        final List<BulkObject> bulkObjects = Lists.newArrayList(blob1, blob2);

        final Objects chunk = new Objects();
        chunk.setObjects(bulkObjects);

        final List<Objects> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getMasterObjectListResult()).thenReturn(mob);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(realJobId.toString()))).thenReturn(jobResponse);

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
        final List<Job> jobs = new ArrayList<>();

        final Job job1 = new Job();
        job1.setJobId(UUID.randomUUID());
        job1.setStatus(JobStatus.IN_PROGRESS);
        job1.setBucketName("bucket2");
        job1.setRequestType(JobRequestType.PUT);
        jobs.add(job1);

        final Job job2 = new Job();
        job2.setJobId(realJobId);
        job2.setStatus(JobStatus.IN_PROGRESS);
        job2.setBucketName("bucket");
        job2.setRequestType(JobRequestType.PUT);
        jobs.add(job2);

        final JobList container = new JobList();
        container.setJobs(jobs);

        when(jobsResponse.getJobListResult()).thenReturn(container);

        final MasterObjectList mob = new MasterObjectList();
        mob.setStatus(JobStatus.IN_PROGRESS);
        mob.setBucketName("bucket");
        mob.setJobId(realJobId);
        mob.setRequestType(JobRequestType.PUT);

        final BulkObject blob1 = new BulkObject();
        blob1.setName("file1");
        blob1.setLength(12);
        blob1.setInCache(false);
        blob1.setOffset(0);

        final BulkObject blob2 = new BulkObject();
        blob2.setName("file2");
        blob2.setLength(12);
        blob2.setInCache(false);
        blob2.setOffset(0);

        final List<BulkObject> bulkObjects = Lists.newArrayList(blob1, blob2);

        final Objects chunk = new Objects();
        chunk.setObjects(bulkObjects);

        final List<Objects> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getMasterObjectListResult()).thenReturn(mob);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(realJobId.toString()))).thenReturn(jobResponse);

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
        final List<Job> jobs = new ArrayList<>();

        final Job job1 = new Job();
        job1.setJobId(UUID.randomUUID());
        job1.setStatus(JobStatus.COMPLETED);
        job1.setBucketName("bucket");
        job1.setRequestType(JobRequestType.PUT);
        jobs.add(job1);

        final Job job2 = new Job();
        job2.setJobId(realJobId);
        job2.setStatus(JobStatus.IN_PROGRESS);
        job2.setBucketName("bucket");
        job2.setRequestType(JobRequestType.PUT);
        jobs.add(job2);

        final JobList container = new JobList();
        container.setJobs(jobs);

        when(jobsResponse.getJobListResult()).thenReturn(container);

        final MasterObjectList mob = new MasterObjectList();
        mob.setStatus(JobStatus.IN_PROGRESS);
        mob.setBucketName("bucket");
        mob.setJobId(realJobId);
        mob.setRequestType(JobRequestType.PUT);

        final BulkObject blob1 = new BulkObject();
        blob1.setName("file1");
        blob1.setLength(12);
        blob1.setInCache(false);
        blob1.setOffset(0);

        final BulkObject blob2 = new BulkObject();
        blob2.setName("file2");
        blob2.setLength(12);
        blob2.setInCache(false);
        blob2.setOffset(0);

        final List<BulkObject> bulkObjects = Lists.newArrayList(blob1, blob2);

        final Objects chunk = new Objects();
        chunk.setObjects(bulkObjects);

        final List<Objects> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getMasterObjectListResult()).thenReturn(mob);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(realJobId.toString()))).thenReturn(jobResponse);

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
        final List<Job> jobs = new ArrayList<>();

        final Job job1 = new Job();
        job1.setJobId(UUID.randomUUID());
        job1.setStatus(JobStatus.COMPLETED);
        job1.setBucketName("bucket");
        job1.setRequestType(JobRequestType.PUT);
        jobs.add(job1);

        final Job job2 = new Job();
        job2.setJobId(realJobId);
        job2.setStatus(JobStatus.IN_PROGRESS);
        job2.setBucketName("bucket");
        job2.setRequestType(JobRequestType.PUT);
        jobs.add(job2);

        final JobList container = new JobList();
        container.setJobs(jobs);

        when(jobsResponse.getJobListResult()).thenReturn(container);

        final MasterObjectList mob = new MasterObjectList();
        mob.setStatus(JobStatus.IN_PROGRESS);
        mob.setBucketName("bucket");
        mob.setJobId(realJobId);
        mob.setRequestType(JobRequestType.PUT);

        final BulkObject blob1 = new BulkObject();
        blob1.setName("file1");
        blob1.setLength(12);
        blob1.setInCache(false);
        blob1.setOffset(0);

        final BulkObject blob2 = new BulkObject();
        blob2.setName("file2");
        blob2.setLength(12);
        blob2.setInCache(false);
        blob2.setOffset(0);

        final List<BulkObject> bulkObjects = Lists.newArrayList(blob1, blob2);

        final Objects chunk = new Objects();
        chunk.setObjects(bulkObjects);

        final List<Objects> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getMasterObjectListResult()).thenReturn(mob);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(realJobId.toString()))).thenReturn(jobResponse);

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
        final List<Job> jobs = new ArrayList<>();

        final Job job1 = new Job();
        job1.setJobId(UUID.randomUUID());
        job1.setStatus(JobStatus.IN_PROGRESS);
        job1.setBucketName("bucket");
        job1.setRequestType(JobRequestType.PUT);
        jobs.add(job1);

        final MasterObjectList mob1 = new MasterObjectList();
        mob1.setStatus(JobStatus.IN_PROGRESS);
        mob1.setBucketName("bucket");
        mob1.setJobId(job1.getJobId());
        mob1.setRequestType(JobRequestType.PUT);

        final BulkObject blob0 = new BulkObject();
        blob0.setName("file.something");
        blob0.setLength(125);
        blob0.setInCache(false);
        blob0.setOffset(0);

        final List<BulkObject> bulkObjects1 = Lists.newArrayList(blob0);

        final Objects chunk1 = new Objects();
        chunk1.setObjects(bulkObjects1);

        final List<Objects> chunks1 = new ArrayList<>();
        chunks1.add(chunk1);

        mob1.setObjects(chunks1);

        final Job job2 = new Job();
        job2.setJobId(realJobId);
        job2.setStatus(JobStatus.IN_PROGRESS);
        job2.setBucketName("bucket");
        job2.setRequestType(JobRequestType.PUT);
        jobs.add(job2);

        final JobList container = new JobList();
        container.setJobs(jobs);

        when(jobsResponse.getJobListResult()).thenReturn(container);

        final MasterObjectList mob2 = new MasterObjectList();
        mob2.setStatus(JobStatus.IN_PROGRESS);
        mob2.setBucketName("bucket");
        mob2.setJobId(realJobId);
        mob2.setRequestType(JobRequestType.PUT);

        final BulkObject blob1 = new BulkObject();
        blob1.setName("file1");
        blob1.setLength(12);
        blob1.setInCache(false);
        blob1.setOffset(0);

        final BulkObject blob2 = new BulkObject();
        blob2.setName("file2");
        blob2.setLength(12);
        blob2.setInCache(false);
        blob2.setOffset(0);

        final List<BulkObject> bulkObjects2 = Lists.newArrayList(blob1, blob2);

        final BulkObject blob3 = new BulkObject();
        blob3.setName("file3");
        blob3.setLength(12);
        blob3.setInCache(false);
        blob3.setOffset(0);

        final List<BulkObject> bulkObjects3 = Lists.newArrayList(blob3);

        final Objects chunk2 = new Objects();
        chunk2.setObjects(bulkObjects2);

        final Objects chunk3 = new Objects();
        chunk3.setObjects(bulkObjects3);

        final List<Objects> chunks2 = new ArrayList<>();
        chunks2.add(chunk2);
        chunks2.add(chunk3);

        mob2.setObjects(chunks2);

        when(jobResponse1.getMasterObjectListResult()).thenReturn(mob1);
        when(jobResponse2.getMasterObjectListResult()).thenReturn(mob2);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(job1.getJobId().toString()))).thenReturn(jobResponse1);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(job2.getJobId().toString()))).thenReturn(jobResponse2);

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
        final List<Job> jobs = new ArrayList<>();

        final Job job1 = new Job();
        job1.setJobId(UUID.randomUUID());
        job1.setStatus(JobStatus.IN_PROGRESS);
        job1.setBucketName("bucket");
        job1.setRequestType(JobRequestType.GET);
        jobs.add(job1);

        final MasterObjectList mob1 = new MasterObjectList();
        mob1.setStatus(JobStatus.IN_PROGRESS);
        mob1.setBucketName("bucket");
        mob1.setJobId(job1.getJobId());
        mob1.setRequestType(JobRequestType.GET);

        final BulkObject blob3 = new BulkObject();
        blob3.setName("file3");
        blob3.setLength(12);
        blob3.setInCache(false);
        blob3.setOffset(0);

        final List<BulkObject> bulkObjects1 = Lists.newArrayList(blob3);

        final Objects chunk1 = new Objects();
        chunk1.setObjects(bulkObjects1);

        final List<Objects> chunks1 = new ArrayList<>();
        chunks1.add(chunk1);

        mob1.setObjects(chunks1);

        final Job job2 = new Job();
        job2.setJobId(realJobId);
        job2.setStatus(JobStatus.IN_PROGRESS);
        job2.setBucketName("bucket");
        job2.setRequestType(JobRequestType.GET);
        jobs.add(job2);

        final JobList container = new JobList();
        container.setJobs(jobs);

        when(jobsResponse.getJobListResult()).thenReturn(container);

        final MasterObjectList mob2 = new MasterObjectList();
        mob2.setStatus(JobStatus.IN_PROGRESS);
        mob2.setBucketName("bucket");
        mob2.setJobId(realJobId);
        mob2.setRequestType(JobRequestType.GET);

        final BulkObject blob1 = new BulkObject();
        blob1.setName("file1");
        blob1.setLength(12);
        blob1.setInCache(false);
        blob1.setOffset(0);

        final BulkObject blob2 = new BulkObject();
        blob2.setName("file2");
        blob2.setLength(12);
        blob2.setInCache(false);
        blob2.setOffset(0);

        final List<BulkObject> bulkObjects2 = Lists.newArrayList(blob1, blob2);
        final List<BulkObject> bulkObjects3 = Lists.newArrayList(blob3);

        final Objects chunk2 = new Objects();
        chunk2.setObjects(bulkObjects2);

        final Objects chunk3 = new Objects();
        chunk3.setObjects(bulkObjects3);

        final List<Objects> chunks2 = new ArrayList<>();
        chunks2.add(chunk2);
        chunks2.add(chunk3);

        mob2.setObjects(chunks2);

        when(jobResponse1.getMasterObjectListResult()).thenReturn(mob1);
        when(jobResponse2.getMasterObjectListResult()).thenReturn(mob2);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(job1.getJobId().toString()))).thenReturn(jobResponse1);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(job2.getJobId().toString()))).thenReturn(jobResponse2);

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
        final List<Job> jobs = new ArrayList<>();

        final Job job1 = new Job();
        job1.setJobId(UUID.randomUUID());
        job1.setStatus(JobStatus.IN_PROGRESS);
        job1.setBucketName("bucket");
        job1.setRequestType(JobRequestType.GET);
        jobs.add(job1);

        final Job job2 = new Job();
        job2.setJobId(realJobId);
        job2.setStatus(JobStatus.IN_PROGRESS);
        job2.setBucketName("bucket");
        job2.setRequestType(JobRequestType.PUT);
        jobs.add(job2);

        final JobList container = new JobList();
        container.setJobs(jobs);

        when(jobsResponse.getJobListResult()).thenReturn(container);

        final MasterObjectList mob = new MasterObjectList();
        mob.setStatus(JobStatus.IN_PROGRESS);
        mob.setBucketName("bucket");
        mob.setJobId(realJobId);
        mob.setRequestType(JobRequestType.PUT);

        final BulkObject blob1 = new BulkObject();
        blob1.setName("file1");
        blob1.setLength(12);
        blob1.setInCache(false);
        blob1.setOffset(0);

        final BulkObject blob2 = new BulkObject();
        blob2.setName("file2");
        blob2.setLength(12);
        blob2.setInCache(false);
        blob2.setOffset(0);

        final List<BulkObject> bulkObjects = Lists.newArrayList(blob1, blob2);

        final Objects chunk = new Objects();
        chunk.setObjects(bulkObjects);

        final List<Objects> chunks = new ArrayList<>();
        chunks.add(chunk);

        mob.setObjects(chunks);

        when(jobResponse.getMasterObjectListResult()).thenReturn(mob);

        when(client.getJobsSpectraS3(any(GetJobsSpectraS3Request.class))).thenReturn(jobsResponse);
        when(client.getJobSpectraS3(new GetJobSpectraS3Request(realJobId.toString()))).thenReturn(jobResponse);

        final Set<String> fileNames = Sets.newSet("file1");

        final List<UUID> jobId = JobUtils.findJob(client, JobRequestType.PUT, "bucket", fileNames);

        assertThat(jobId, is(notNullValue()));
        assertThat(jobId.size(), is(1));
        assertThat(jobId.get(0), is(realJobId));
    }
}
