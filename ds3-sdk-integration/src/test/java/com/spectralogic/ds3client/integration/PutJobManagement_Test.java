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

package com.spectralogic.ds3client.integration;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.ResourceUtils;
import static org.hamcrest.Matchers.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.spectralogic.ds3client.helpers.Ds3ClientHelpers.*;
import static com.spectralogic.ds3client.integration.Util.*;
import static org.junit.Assert.*;

public class PutJobManagement_Test {

    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String BUCKET_NAME = "Put_Job_Management_Test";

    private static final String TEST_ENV_NAME = "PutJobManagement_Test";
    private static TempStorageIds envStorageIds;

    @BeforeClass
    public static void startup() throws IOException, SignatureException {
        //client = Util.fromEnv();
        final UUID dataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, true, ChecksumType.Type.MD5, client);
        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, dataPolicyId, client);
    }

    @Before
    public void setupBucket() throws IOException, SignatureException {
        HELPERS.ensureBucketExists(BUCKET_NAME);
    }

    @AfterClass
    public static void teardown() throws IOException, SignatureException {
        TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, client);
        client.close();
    }

    private void checkTimeOut(final long startTime, final int testTimeOutSeconds){
        assertTrue((System.nanoTime() - startTime)/1000000000 <= testTimeOutSeconds);
        assertThat((System.nanoTime() - startTime)/1000000000, lessThan((long) testTimeOutSeconds));
    }

    //todo verify naked s3 somewhere

    @Test
    public void modifyJobPriority() throws IOException, SignatureException, XmlProcessingException {
        try {
            final Ds3ClientHelpers.Job job =
                    wrap(client).startWriteJob(BUCKET_NAME, Lists.newArrayList( new Ds3Object("test", 2)),
                            WriteJobOptions.create().withPriority(Priority.LOW));

            client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(job.getJobId())
                    .withPriority(Priority.HIGH));

            final GetJobSpectraS3Response response = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId()));

            assertThat(response.getMasterObjectListResult().getPriority(), is(Priority.HIGH));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void modifyJobName() throws IOException, SignatureException, XmlProcessingException {
        
        try {
            final Ds3ClientHelpers.Job job =
                    wrap(client).startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("testOne", 2)));

            client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(job.getJobId())
                    .withName("newName"));

            final GetJobSpectraS3Response response = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId()));

            assertThat(response.getMasterObjectListResult().getName(), is("newName"));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void modifyJobCreationDate() throws IOException, SignatureException, XmlProcessingException {
        
        try {
            final Ds3ClientHelpers.Job job =
                    wrap(client).startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("testOne", 2)));
            final GetJobSpectraS3Response jobResponse = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId()));

            final Date originalDate = jobResponse.getMasterObjectListResult().getStartDate();
            final Date newDate = new Date(originalDate.getTime() - 1000);

            client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(job.getJobId())
                    .withCreatedAt(newDate));

            final GetJobSpectraS3Response responseAfterModify = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId()));

            assertThat(responseAfterModify.getMasterObjectListResult().getStartDate(), is(newDate));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void cancelJob() throws IOException, SignatureException, XmlProcessingException {
        
        try {
            final Ds3ClientHelpers.Job job =
                    wrap(client).startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("testOne", 2)));

            final CancelJobSpectraS3Response response = client
                    .cancelJobSpectraS3(new CancelJobSpectraS3Request(job.getJobId()));
            assertEquals(response.getStatusCode(),204);

            assertTrue(client.getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request())
                    .getActiveJobListResult().getActiveJobs().isEmpty());

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void truncateJobCancelWithOutForce() throws IOException, SignatureException, XmlProcessingException, URISyntaxException, InterruptedException {

        final int testTimeOutSeconds = 5;
        final String book1 = "beowulf.txt";
        final Path objPath1 = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book1);
        final Ds3Object obj1 = new Ds3Object(book1, Files.size(objPath1));
        final Ds3Object obj2 = new Ds3Object("place_holder", 5000000);

        try {
            final Ds3ClientHelpers.Job putJob = HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(obj1, obj2));
            final UUID jobId = putJob.getJobId();
            final SeekableByteChannel book1Channel = new ResourceObjectPutter(RESOURCE_BASE_NAME).buildChannel(book1);
            client.putObject(new PutObjectRequest(BUCKET_NAME, book1, book1Channel, jobId, 0, Files.size(objPath1)));

            //make sure black pearl has updated it's job to show 1 object in cache
            final long startTime = System.nanoTime();
            long cachedSize = 0;
            while (cachedSize == 0) {
                Thread.sleep(500);
                final MasterObjectList mol = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId)).getMasterObjectListResult();
                cachedSize = mol.getCachedSizeInBytes();
                checkTimeOut(startTime, testTimeOutSeconds);
            }

            final CancelJobSpectraS3Response failedResponse = client.cancelJobSpectraS3(new CancelJobSpectraS3Request(jobId));
            assertThat(failedResponse.getStatusCode(),is(400));

            final GetJobSpectraS3Response truncatedJob = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId));
            assertEquals(truncatedJob.getMasterObjectListResult().getOriginalSizeInBytes(), Files.size(objPath1));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void cancelJobWithForce() throws IOException, SignatureException, XmlProcessingException, URISyntaxException, InterruptedException {

        final int testTimeOutSeconds = 5;
        
        final String book1 = "beowulf.txt";
        final Path objPath1 = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book1);
        final Ds3Object obj1 = new Ds3Object(book1, Files.size(objPath1));
        final Ds3Object obj2 = new Ds3Object("place_holder", 5000000);

        try {
            final Ds3ClientHelpers.Job putJob = HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(obj1, obj2));
            final UUID jobId = putJob.getJobId();
            final SeekableByteChannel book1Channel = new ResourceObjectPutter(RESOURCE_BASE_NAME).buildChannel(book1);
            client.putObject(new PutObjectRequest(BUCKET_NAME, book1, book1Channel, jobId, 0, Files.size(objPath1)));

            //make sure black pearl has updated it's job to show 1 object in cache
            final long startTimePutObject = System.nanoTime();
            long cachedSize = 0;
            while (cachedSize == 0) {
                Thread.sleep(500);
                final MasterObjectList mol = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId)).getMasterObjectListResult();
                cachedSize = mol.getCachedSizeInBytes();
               checkTimeOut(startTimePutObject, testTimeOutSeconds);
            }

            final CancelJobSpectraS3Response responseWithForce = client
                    .cancelJobSpectraS3(new CancelJobSpectraS3Request(jobId).withForce(true));
            assertEquals(responseWithForce.getStatusCode(), 204);

            //Allow for lag time before canceled job appears~1.5 seconds in unloaded system
            final long startTimeCanceledUpdate = System.nanoTime();
            boolean jobCanceled = false;
            while (!jobCanceled) {
                Thread.sleep(500);
                final GetCanceledJobsSpectraS3Response canceledJobs = client.getCanceledJobsSpectraS3(new GetCanceledJobsSpectraS3Request());
                for (final CanceledJob canceledJob : canceledJobs.getCanceledJobListResult().getCanceledJobs()){
                    if (canceledJob.getId().equals(jobId)){
                        jobCanceled = true;
                    }
                }
                checkTimeOut(startTimeCanceledUpdate, testTimeOutSeconds);
            }

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void cancelAllJobs() throws IOException, SignatureException, XmlProcessingException {
        
        try {
            final List<Ds3Object> objectsOne = Lists.newArrayList(new Ds3Object("testOne", 2));

            wrap(client).startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("testOne", 2)));

            final List<Ds3Object> objectsTwo = Lists.newArrayList(new Ds3Object("testTwo", 2));

            wrap(client).startWriteJob(BUCKET_NAME, objectsTwo);

            final CancelAllJobsSpectraS3Response response = client
                    .cancelAllJobsSpectraS3(new CancelAllJobsSpectraS3Request());

            assertTrue(client.getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request())
                    .getActiveJobListResult().getActiveJobs().isEmpty());
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void truncateCancelAllJobsWithoutForce() throws IOException, SignatureException, XmlProcessingException, InterruptedException, URISyntaxException {

        final int testTimeOutSeconds = 5;
        final String book1 = "beowulf.txt";
        final Path objPath1 = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book1);
        final String book2 = "ulysses.txt";
        final Path objPath2 = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book2);
        final Ds3Object obj1 = new Ds3Object(book1, Files.size(objPath1));
        final Ds3Object obj2 = new Ds3Object("place_holder_1", 5000000);
        final Ds3Object obj3 = new Ds3Object(book2, Files.size(objPath2));
        final Ds3Object obj4 = new Ds3Object("place_holder_2", 5000000);

        try {
            

            final Ds3ClientHelpers.Job putJob1 = HELPERS.startWriteJob(BUCKET_NAME, Lists
                    .newArrayList(obj1, obj2));
            final UUID jobId1 = putJob1.getJobId();
            final SeekableByteChannel book1Channel = new ResourceObjectPutter(RESOURCE_BASE_NAME).buildChannel(book1);
            client.putObject(new PutObjectRequest(BUCKET_NAME, book1, book1Channel, jobId1, 0, Files.size(objPath1)));

            final Ds3ClientHelpers.Job putJob2 = HELPERS.startWriteJob(BUCKET_NAME, Lists
                    .newArrayList(obj3, obj4));
            final UUID jobId2 = putJob2.getJobId();
            final SeekableByteChannel book2Channel = new ResourceObjectPutter(RESOURCE_BASE_NAME).buildChannel(book2);
            client.putObject(new PutObjectRequest(BUCKET_NAME, book2, book2Channel, jobId2, 0, Files.size(objPath2)));

            final Ds3ClientHelpers.Job putJob3 = HELPERS.startWriteJob(BUCKET_NAME, Lists
                    .newArrayList(new Ds3Object("place_holder_3", 1000000)));
            final UUID jobId3 = putJob3.getJobId();

            //make sure black pearl has updated the first 2 jobs to show 1 object in cache each
            final long startTime = System.nanoTime();
            boolean cachedSizeUpdated = false;
            while (!cachedSizeUpdated) {
                Thread.sleep(500);
                final MasterObjectList job1mol = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId1)).getMasterObjectListResult();
                final long job1CachedSize = job1mol.getCachedSizeInBytes();
                final MasterObjectList job2mol = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId2)).getMasterObjectListResult();
                final long job2CachedSize = job1mol.getCachedSizeInBytes();
                if (job1CachedSize > 0 && job2CachedSize > 0) {
                    cachedSizeUpdated = true;
                }
                checkTimeOut(startTime, testTimeOutSeconds);
            }

            final CancelAllJobsSpectraS3Response failedResponse = client
                    .cancelAllJobsSpectraS3(new CancelAllJobsSpectraS3Request());

            assertThat(failedResponse.getStatusCode(), is(400));

            final GetJobSpectraS3Response truncatedJob1 = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId1));
            assertEquals(truncatedJob1.getMasterObjectListResult().getOriginalSizeInBytes(), Files.size(objPath1));

            final GetJobSpectraS3Response truncatedJob2 = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId2));
            assertEquals(truncatedJob2.getMasterObjectListResult().getOriginalSizeInBytes(), Files.size(objPath2));

            assertThat(client.getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request())
                    .getActiveJobListResult().getActiveJobs().size(), is(2));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void cancelAllJobsWithForce ()throws IOException, SignatureException, XmlProcessingException, InterruptedException, URISyntaxException {

        final int testTimeOutSeconds = 5;
        final String book1 = "beowulf.txt";
        final Path objPath1 = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book1);
        final String book2 = "ulysses.txt";
        final Path objPath2 = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book2);
        final Ds3Object obj1 = new Ds3Object(book1, Files.size(objPath1));
        final Ds3Object obj2 = new Ds3Object("place_holder_1", 5000000);
        final Ds3Object obj3 = new Ds3Object(book2, Files.size(objPath2));
        final Ds3Object obj4 = new Ds3Object("place_holder_2", 5000000);

        try {
            final Ds3ClientHelpers.Job putJob1 = HELPERS.startWriteJob(BUCKET_NAME, Lists
                    .newArrayList(obj1, obj2));
            final UUID jobId1 = putJob1.getJobId();
            final SeekableByteChannel book1Channel = new ResourceObjectPutter(RESOURCE_BASE_NAME).buildChannel(book1);
            client.putObject(new PutObjectRequest(BUCKET_NAME, book1, book1Channel, jobId1, 0, Files.size(objPath1)));

            final Ds3ClientHelpers.Job putJob2 = HELPERS.startWriteJob(BUCKET_NAME, Lists
                    .newArrayList(obj3, obj4));
            final UUID jobId2 = putJob2.getJobId();
            final SeekableByteChannel book2Channel = new ResourceObjectPutter(RESOURCE_BASE_NAME).buildChannel(book2);
            client.putObject(new PutObjectRequest(BUCKET_NAME, book2, book2Channel, jobId2, 0, Files.size(objPath2)));

            final Ds3ClientHelpers.Job putJob3 = HELPERS.startWriteJob(BUCKET_NAME, Lists
                    .newArrayList(new Ds3Object("place_holder_3", 1000000)));
            final UUID jobId3 = putJob3.getJobId();

            //make sure black pearl has updated the first 2 jobs to show 1 object in cache each
            final long startTime = System.nanoTime();
            boolean cachedSizeUpdated = false;
            while (!cachedSizeUpdated) {
                Thread.sleep(500);
                final MasterObjectList job1mol = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId1)).getMasterObjectListResult();
                final long job1CachedSize = job1mol.getCachedSizeInBytes();
                final MasterObjectList job2mol = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId2)).getMasterObjectListResult();
                final long job2CachedSize = job1mol.getCachedSizeInBytes();
                if (job1CachedSize > 0 && job2CachedSize > 0) {
                    cachedSizeUpdated = true;
                }
                checkTimeOut(startTime, testTimeOutSeconds);
            }

            final CancelAllJobsSpectraS3Response response = client
                    .cancelAllJobsSpectraS3(new CancelAllJobsSpectraS3Request().withForce(true));

            assertTrue(client.getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request())
                    .getActiveJobListResult().getActiveJobs().isEmpty());

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
        public void getCanceledJobs() throws IOException, SignatureException, XmlProcessingException {
            
            try {
                

                final Ds3ClientHelpers.Job jobOne =
                        wrap(client).startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test", 2)));
                final UUID jobOneId = jobOne.getJobId();
                client.cancelJobSpectraS3(new CancelJobSpectraS3Request(jobOneId));

                final GetCanceledJobsSpectraS3Response getCanceledJobsResponse = client
                        .getCanceledJobsSpectraS3(new GetCanceledJobsSpectraS3Request());

                final List<UUID> canceledJobsUUIDs = new ArrayList<>();
                for (final CanceledJob job : getCanceledJobsResponse.getCanceledJobListResult().getCanceledJobs()) {
                    canceledJobsUUIDs.add(job.getId());
                }
                assertTrue(canceledJobsUUIDs.contains(jobOneId));

            } finally {
                deleteAllContents(client, BUCKET_NAME);
            }
    }

    @Test  //todo
    public void getJobChunksReady() throws IOException, SignatureException, XmlProcessingException {
        
        try {
            

            final WriteJobOptions writeJobOptions = WriteJobOptions.create().withAggregating();

            final Ds3ClientHelpers.Job jobOne =
                    wrap(client).startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test", 2)), writeJobOptions);
            final UUID jobOneId = jobOne.getJobId();

            final Ds3ClientHelpers.Job jobTwo =
                    wrap(client).startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test2", 2)), writeJobOptions);
            final UUID jobTwoId = jobTwo.getJobId();

            assertThat(jobOneId, is(jobTwoId));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }



    @Test
    public void aggregateTwoJobs() throws IOException, SignatureException, XmlProcessingException {
        
        try {
            

            final WriteJobOptions writeJobOptions = WriteJobOptions.create().withAggregating();

            final Ds3ClientHelpers.Job jobOne =
                    wrap(client).startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test", 2)), writeJobOptions);
            final UUID jobOneId = jobOne.getJobId();

            final Ds3ClientHelpers.Job jobTwo =
                    wrap(client).startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test2", 2)), writeJobOptions);
            final UUID jobTwoId = jobTwo.getJobId();

            assertThat(jobOneId, is(jobTwoId));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test //todo
    public void allocateJobChunk() throws IOException, SignatureException, XmlProcessingException {
        
        try {
            

            final WriteJobOptions writeJobOptions = WriteJobOptions.create().withAggregating();

            final Ds3ClientHelpers.Job jobOne =
                    wrap(client).startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test", 2)), writeJobOptions);
            final UUID jobOneId = jobOne.getJobId();

            final Ds3ClientHelpers.Job jobTwo =
                    wrap(client).startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test2", 2)), writeJobOptions);
            final UUID jobTwoId = jobTwo.getJobId();

            assertThat(jobOneId, is(jobTwoId));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }
}
