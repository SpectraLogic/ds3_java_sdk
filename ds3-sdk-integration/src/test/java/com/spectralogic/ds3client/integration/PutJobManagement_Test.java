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
import com.spectralogic.ds3client.Ds3ClientImpl;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.commands.spectrads3.notifications.*;
import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FileObjectGetter;
import com.spectralogic.ds3client.helpers.FileObjectPutter;
import com.spectralogic.ds3client.helpers.ObjectCompletedListener;
import com.spectralogic.ds3client.helpers.channels.RangedSeekableByteChannel;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.integration.test.helpers.ABMTestHelper;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.NetworkClient;
import com.spectralogic.ds3client.networking.NetworkClientImpl;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.RESOURCE_BASE_NAME;
import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import org.junit.Test;

public class PutJobManagement_Test {

    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String BUCKET_NAME = "Put_Job_Management_Test";

    private static final String TEST_ENV_NAME = "PutJobManagement_Test";
    private static TempStorageIds envStorageIds;
    private static UUID envDataPolicyId;

    @BeforeClass
    public static void startup() throws IOException {
        envDataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, false, ChecksumType.Type.MD5, client);
        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, envDataPolicyId, client);
    }

    @Before
    public void setupBucket() throws IOException {
        HELPERS.ensureBucketExists(BUCKET_NAME, envDataPolicyId);
    }

    @AfterClass
    public static void teardown() throws IOException {
        TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, client);
        client.close();
    }

    private long getCacheBytesAvailable() throws IOException {
        long cacheAvailableBytes = 0;
        final List<CacheFilesystemInformation> cacheFilesystemInformationList = client.getCacheStateSpectraS3(
                new GetCacheStateSpectraS3Request()).getCacheInformationResult().getFilesystems();
        for (final CacheFilesystemInformation filesystemInformation : cacheFilesystemInformationList) {
            if (filesystemInformation.getAvailableCapacityInBytes() > cacheAvailableBytes) {
                cacheAvailableBytes = filesystemInformation.getAvailableCapacityInBytes();
            }
        }
        return cacheAvailableBytes;
    }

    @SuppressWarnings("deprecation")
    @Test
    public void nakedS3Put() throws IOException, URISyntaxException {
        try {
            final Path beowulfPath = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + "beowulf.txt");
            final SeekableByteChannel beowulfChannel = new ResourceObjectPutter(RESOURCE_BASE_NAME).buildChannel("beowulf.txt");
            final PutObjectResponse putObjectResponse = client.putObject(new PutObjectRequest(BUCKET_NAME, "beowulf.txt",
                    beowulfChannel, Files.size(beowulfPath)));
            assertThat(putObjectResponse.getStatusCode(), is(200));
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void getActiveJobs() throws IOException, URISyntaxException {
        try {
            final UUID jobID = HELPERS
                    .startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test", 2))).getJobId();
            final GetActiveJobsSpectraS3Response activeJobsResponse = client.
                    getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request());
            final ArrayList<UUID> activeJobsUUIDs = new ArrayList<>();
            for (final ActiveJob job : activeJobsResponse.getActiveJobListResult().getActiveJobs()) {
                activeJobsUUIDs.add(job.getId());
            }
            assertThat(activeJobsUUIDs, contains(jobID));
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void getJobs() throws IOException {
        try {
            final UUID jobID = HELPERS
                    .startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test", 2))).getJobId();
            final GetJobsSpectraS3Response getJobsResponse = client.
                    getJobsSpectraS3(new GetJobsSpectraS3Request());
            final ArrayList<UUID> jobUUIDs = new ArrayList<>();
            for (final Job job : getJobsResponse.getJobListResult().getJobs()) {
                jobUUIDs.add(job.getJobId());
            }
            assertThat(jobUUIDs, contains(jobID));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void modifyJobPriority() throws IOException {
        try {
            final Ds3ClientHelpers.Job job =
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test", 2)),
                            WriteJobOptions.create().withPriority(Priority.LOW));

            client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(job.getJobId().toString())
                    .withPriority(Priority.HIGH));

            final GetJobSpectraS3Response response = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId().toString()));

            assertThat(response.getMasterObjectListResult().getPriority(), is(Priority.HIGH));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void modifyJobName() throws IOException {

        try {
            final Ds3ClientHelpers.Job job =
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("testOne", 2)));

            client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(job.getJobId().toString())
                    .withName("newName"));

            final GetJobSpectraS3Response response = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId().toString()));

            assertThat(response.getMasterObjectListResult().getName(), is("newName"));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void modifyJobCreationDate() throws IOException {

        try {
            final Ds3ClientHelpers.Job job =
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("testOne", 2)));
            final GetJobSpectraS3Response jobResponse = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId().toString()));

            final Date originalDate = jobResponse.getMasterObjectListResult().getStartDate();
            final Date newDate = new Date(originalDate.getTime() - 1000);

            client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(job.getJobId().toString())
                    .withCreatedAt(newDate));

            final GetJobSpectraS3Response responseAfterModify = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId().toString()));

            assertThat(responseAfterModify.getMasterObjectListResult().getStartDate(), is(newDate));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void cancelJob() throws IOException {

        try {
            final Ds3ClientHelpers.Job job =
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("testOne", 2)));

            final CancelJobSpectraS3Response response = client
                    .cancelJobSpectraS3(new CancelJobSpectraS3Request(job.getJobId().toString()));
            assertEquals(response.getStatusCode(), 204);

            assertTrue(client.getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request())
                    .getActiveJobListResult().getActiveJobs().isEmpty());

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void clearAllCanceledJobs() throws IOException {

        try {
            final Ds3ClientHelpers.Job job =
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("testOne", 2)));
            client.cancelJobSpectraS3(new CancelJobSpectraS3Request(job.getJobId().toString()));
            client.clearAllCanceledJobsSpectraS3(new ClearAllCanceledJobsSpectraS3Request());
            final List canceledJobsList = client.
                    getCanceledJobsSpectraS3(new GetCanceledJobsSpectraS3Request())
                    .getCanceledJobListResult().getCanceledJobs();

            assertTrue(canceledJobsList.isEmpty());

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void truncatePutJob() throws Exception {

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
            ABMTestHelper.waitForJobCachedSizeToBeMoreThanZero(jobId, client, 20);

            final TruncateJobSpectraS3Response truncateJobSpectraS3Response = client.truncateJobSpectraS3(
                    new TruncateJobSpectraS3Request(jobId.toString()));
            assertThat(truncateJobSpectraS3Response.getStatusCode(), is(204));

            final GetJobSpectraS3Response truncatedJob = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId.toString()));
            assertEquals(truncatedJob.getMasterObjectListResult().getOriginalSizeInBytes(), Files.size(objPath1));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void cancelJobWithForce() throws Exception {

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
            ABMTestHelper.waitForJobCachedSizeToBeMoreThanZero(jobId, client, 20);

            final CancelJobSpectraS3Response responseWithForce = client
                    .cancelJobSpectraS3(new CancelJobSpectraS3Request(jobId.toString()));
            assertEquals(responseWithForce.getStatusCode(), 204);

            //Allow for lag time before canceled job appears~1.5 seconds in unloaded system
            final long startTimeCanceledUpdate = System.nanoTime();
            boolean jobCanceled = false;
            while (!jobCanceled) {
                Thread.sleep(500);
                final GetCanceledJobsSpectraS3Response canceledJobs = client.getCanceledJobsSpectraS3(new GetCanceledJobsSpectraS3Request());
                for (final CanceledJob canceledJob : canceledJobs.getCanceledJobListResult().getCanceledJobs()) {
                    if (canceledJob.getId().equals(jobId)) {
                        jobCanceled = true;
                    }
                }
                assertThat((System.nanoTime() - startTimeCanceledUpdate) / 1000000000, lessThan((long) testTimeOutSeconds));
            }

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void cancelAllJobs() throws IOException {

        try {
            HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("testOne", 2)));
            final List<Ds3Object> objectsTwo = Lists.newArrayList(new Ds3Object("testTwo", 2));
            HELPERS.startWriteJob(BUCKET_NAME, objectsTwo);
            client.cancelAllJobsSpectraS3(new CancelAllJobsSpectraS3Request());

            assertTrue(client.getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request())
                    .getActiveJobListResult().getActiveJobs().isEmpty());
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void truncateAllJobs() throws Exception {

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
            client.putObject(new PutObjectRequest(BUCKET_NAME, book1, book1Channel, jobId1.toString(), 0, Files.size(objPath1)));

            final Ds3ClientHelpers.Job putJob2 = HELPERS.startWriteJob(BUCKET_NAME, Lists
                    .newArrayList(obj3, obj4));
            final UUID jobId2 = putJob2.getJobId();
            final SeekableByteChannel book2Channel = new ResourceObjectPutter(RESOURCE_BASE_NAME).buildChannel(book2);
            client.putObject(new PutObjectRequest(BUCKET_NAME, book2, book2Channel, jobId2.toString(), 0, Files.size(objPath2)));

            HELPERS.startWriteJob(BUCKET_NAME, Lists
                    .newArrayList(new Ds3Object("place_holder_3", 1000000)));

            ABMTestHelper.waitForJobCachedSizeToBeMoreThanZero(jobId1, client, 20);
            ABMTestHelper.waitForJobCachedSizeToBeMoreThanZero(jobId2, client, 20);

            final TruncateAllJobsSpectraS3Response truncateAllJobsSpectraS3Response = client
                    .truncateAllJobsSpectraS3(new TruncateAllJobsSpectraS3Request());

            assertThat(truncateAllJobsSpectraS3Response.getStatusCode(), is(204));

            final GetJobSpectraS3Response truncatedJob1 = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId1.toString()));
            assertEquals(truncatedJob1.getMasterObjectListResult().getOriginalSizeInBytes(), Files.size(objPath1));

            final GetJobSpectraS3Response truncatedJob2 = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId2.toString()));
            assertEquals(truncatedJob2.getMasterObjectListResult().getOriginalSizeInBytes(), Files.size(objPath2));

            assertThat(client.getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request())
                    .getActiveJobListResult().getActiveJobs().size(), is(2));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void cancelAllJobsWithForce() throws Exception {

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
            client.putObject(new PutObjectRequest(BUCKET_NAME, book1, book1Channel, jobId1.toString(), 0, Files.size(objPath1)));

            final Ds3ClientHelpers.Job putJob2 = HELPERS.startWriteJob(BUCKET_NAME, Lists
                    .newArrayList(obj3, obj4));
            final UUID jobId2 = putJob2.getJobId();
            final SeekableByteChannel book2Channel = new ResourceObjectPutter(RESOURCE_BASE_NAME).buildChannel(book2);
            client.putObject(new PutObjectRequest(BUCKET_NAME, book2, book2Channel, jobId2.toString(), 0, Files.size(objPath2)));

            HELPERS.startWriteJob(BUCKET_NAME, Lists
                    .newArrayList(new Ds3Object("place_holder_3", 1000000)));

            ABMTestHelper.waitForJobCachedSizeToBeMoreThanZero(jobId1, client, 20);
            ABMTestHelper.waitForJobCachedSizeToBeMoreThanZero(jobId2, client, 20);

            client.cancelAllJobsSpectraS3(new CancelAllJobsSpectraS3Request());

            assertTrue(client.getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request())
                    .getActiveJobListResult().getActiveJobs().isEmpty());

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void getCanceledJobs() throws IOException {

        try {
            final Ds3ClientHelpers.Job jobOne =
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test", 2)));
            final UUID jobOneId = jobOne.getJobId();
            client.cancelJobSpectraS3(new CancelJobSpectraS3Request(jobOneId.toString()));

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

    @Test
    public void getJobChunksReady() throws IOException {

        try {
            final Ds3Object ds3Object = new Ds3Object("test", 2);
            final Ds3ClientHelpers.Job jobOne =
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(ds3Object));
            final UUID jobOneId = jobOne.getJobId();

            final GetJobChunksReadyForClientProcessingSpectraS3Response response = client
                    .getJobChunksReadyForClientProcessingSpectraS3
                            (new GetJobChunksReadyForClientProcessingSpectraS3Request(jobOneId.toString()));

            final List<String> chunkNames = new ArrayList<>();
            for (final Objects objectList : response.getMasterObjectListResult().getObjects()) {
                for (final BulkObject bulkObject : objectList.getObjects()) {
                    chunkNames.add(bulkObject.getName());
                }
            }

            assertThat(chunkNames, contains(ds3Object.getName()));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void getJobChunk() throws IOException {

        try {
            final Ds3Object ds3Object = new Ds3Object("test", 2);
            final Ds3ClientHelpers.Job jobOne =
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(ds3Object));
            final UUID jobOneId = jobOne.getJobId();

            final GetJobChunksReadyForClientProcessingSpectraS3Response response = client
                    .getJobChunksReadyForClientProcessingSpectraS3
                            (new GetJobChunksReadyForClientProcessingSpectraS3Request(jobOneId));

            final UUID aJobChunkID = response.getMasterObjectListResult().getObjects().get(0).getChunkId();

            final GetJobChunkSpectraS3Response getJobChunkSpectraS3Response = client.getJobChunkSpectraS3(
                    new GetJobChunkSpectraS3Request(aJobChunkID));

            assertThat(getJobChunkSpectraS3Response.getStatusCode(), is(200));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void aggregateTwoJobs() throws IOException {

        try {
            final WriteJobOptions writeJobOptions = WriteJobOptions.create().withAggregating();

            final Ds3ClientHelpers.Job jobOne =
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test", 2)), writeJobOptions);
            final UUID jobOneId = jobOne.getJobId();

            final Ds3ClientHelpers.Job jobTwo =
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test2", 2)), writeJobOptions);
            final UUID jobTwoId = jobTwo.getJobId();

            assertThat(jobOneId, is(jobTwoId));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void allocateJobChunk() throws IOException {

        try {
            final PutBulkJobSpectraS3Response putBulkResponse = client.
                    putBulkJobSpectraS3(new PutBulkJobSpectraS3Request(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test", 2))));
            final UUID chunkUUID = putBulkResponse.getResult().getObjects().get(0).getChunkId();
            final AllocateJobChunkSpectraS3Response allocateResponse = client
                    .allocateJobChunkSpectraS3(new AllocateJobChunkSpectraS3Request(chunkUUID.toString()));

            assertThat(allocateResponse.getStatusCode(), is(200));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void putObjectCachedNotification() throws IOException {
        final PutObjectCachedNotificationRegistrationSpectraS3Response putNotificationResponse = client
                .putObjectCachedNotificationRegistrationSpectraS3
                        (new PutObjectCachedNotificationRegistrationSpectraS3Request("test@test.test"));

        assertThat(putNotificationResponse.getStatusCode(), is(201));
    }

    @Test
    public void getObjectCachedNotification() throws IOException {

        final PutObjectCachedNotificationRegistrationSpectraS3Response putNotificationResponse = client
                .putObjectCachedNotificationRegistrationSpectraS3
                        (new PutObjectCachedNotificationRegistrationSpectraS3Request("test@test.test"));
        final GetObjectCachedNotificationRegistrationSpectraS3Response getNotificationResponse = client
                .getObjectCachedNotificationRegistrationSpectraS3(
                        (new GetObjectCachedNotificationRegistrationSpectraS3Request
                                (putNotificationResponse.getS3ObjectCachedNotificationRegistrationResult().getId().toString())));

        assertThat(getNotificationResponse.getStatusCode(), is(200));
    }

    @Test
    public void getCompletedJobs() throws IOException {
        final GetCompletedJobsSpectraS3Response getCompletedJobsResponse = client.
                getCompletedJobsSpectraS3(new GetCompletedJobsSpectraS3Request());

        assertThat(getCompletedJobsResponse.getStatusCode(), is(200));
    }

    @Test
    public void clearCompletedJobs() throws IOException {
        final ClearAllCompletedJobsSpectraS3Response clearAllCompletedJobsResponse = client
                .clearAllCompletedJobsSpectraS3(new ClearAllCompletedJobsSpectraS3Request());

        assertThat(clearAllCompletedJobsResponse.getStatusCode(), is(204));
    }

    @Test
    public void putJobCreatedNotification() throws IOException {
        UUID notificationUUID = null;
        try {
            final PutJobCreatedNotificationRegistrationSpectraS3Response response = client
                    .putJobCreatedNotificationRegistrationSpectraS3(new PutJobCreatedNotificationRegistrationSpectraS3Request("test@test.test"));
            notificationUUID = response.getJobCreatedNotificationRegistrationResult().getId();
            assertThat(response.getStatusCode(), is(201));

        } finally {
            if (notificationUUID != null) {
                client.deleteJobCreatedNotificationRegistrationSpectraS3(
                        new DeleteJobCreatedNotificationRegistrationSpectraS3Request(notificationUUID));
            }
        }
    }

    @Test
    public void getJobCreatedNotification() throws IOException {
        UUID notificationUUID = null;
        try {
            notificationUUID = client.putJobCreatedNotificationRegistrationSpectraS3(
                    new PutJobCreatedNotificationRegistrationSpectraS3Request("test@test.test"))
                    .getJobCreatedNotificationRegistrationResult().getId();

            final GetJobCreatedNotificationRegistrationSpectraS3Response response = client
                    .getJobCreatedNotificationRegistrationSpectraS3(new
                            GetJobCreatedNotificationRegistrationSpectraS3Request(notificationUUID));

            assertThat(response.getStatusCode(), is(200));

        } finally {
            if (notificationUUID != null) {
                client.deleteJobCreatedNotificationRegistrationSpectraS3(
                        new DeleteJobCreatedNotificationRegistrationSpectraS3Request(notificationUUID));
            }
        }
    }

    @Test
    public void deleteJobCreatedNotification() throws IOException {
        final UUID notificationUUID = client.putJobCreatedNotificationRegistrationSpectraS3(
                new PutJobCreatedNotificationRegistrationSpectraS3Request("test@test.test"))
                .getJobCreatedNotificationRegistrationResult().getId();

        final DeleteJobCreatedNotificationRegistrationSpectraS3Response response = client
                .deleteJobCreatedNotificationRegistrationSpectraS3(new DeleteJobCreatedNotificationRegistrationSpectraS3Request(notificationUUID));

        assertThat(response.getStatusCode(), is(204));
    }

    @Test
    public void putJobCompletedNotification() throws IOException {
        UUID notificationUUID = null;
        try {
            final PutJobCompletedNotificationRegistrationSpectraS3Response response = client
                    .putJobCompletedNotificationRegistrationSpectraS3(
                            new PutJobCompletedNotificationRegistrationSpectraS3Request("test@test.test"));
            notificationUUID = response.getJobCompletedNotificationRegistrationResult().getId();
            assertThat(response.getStatusCode(), is(201));
        } finally {
            if (notificationUUID != null) {
                client.deleteJobCompletedNotificationRegistrationSpectraS3(
                        new DeleteJobCompletedNotificationRegistrationSpectraS3Request(notificationUUID));
            }
        }
    }

    @Test
    public void getJobCompletedNotification() throws IOException {
        UUID notificationUUID = null;
        try {
            notificationUUID = client.putJobCompletedNotificationRegistrationSpectraS3(
                    new PutJobCompletedNotificationRegistrationSpectraS3Request("test@test.test"))
                    .getJobCompletedNotificationRegistrationResult().getId();

            final GetJobCompletedNotificationRegistrationSpectraS3Response response = client
                    .getJobCompletedNotificationRegistrationSpectraS3(
                            new GetJobCompletedNotificationRegistrationSpectraS3Request(notificationUUID));

            assertThat(response.getStatusCode(), is(200));

        } finally {
            if (notificationUUID != null) {
                client.deleteJobCompletedNotificationRegistrationSpectraS3(
                        new DeleteJobCompletedNotificationRegistrationSpectraS3Request(notificationUUID));
            }
        }
    }

    @Test
    public void deleteJobCompletedNotification() throws IOException {
        final UUID notificationUUID = client.putJobCompletedNotificationRegistrationSpectraS3(
                new PutJobCompletedNotificationRegistrationSpectraS3Request("test@test.test"))
                .getJobCompletedNotificationRegistrationResult().getId();

        final DeleteJobCompletedNotificationRegistrationSpectraS3Response response = client
                .deleteJobCompletedNotificationRegistrationSpectraS3(
                        new DeleteJobCompletedNotificationRegistrationSpectraS3Request(notificationUUID));

        assertThat(response.getStatusCode(), is(204));
    }

    @Test
    public void initiateMultipartUpload() throws IOException {
        try {
            final InitiateMultiPartUploadResponse multiPartUploadResponse = client.initiateMultiPartUpload(
                    new InitiateMultiPartUploadRequest(BUCKET_NAME, "beowulf"));

            assertThat(multiPartUploadResponse.getStatusCode(), is(200));
        } catch (final FailedRequestException e) {

            assertThat(getCacheBytesAvailable(), lessThan(5000000000000L));
            assertThat(e.getStatusCode(), is(400));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void abortMultipartUpload() throws IOException {
        String uploadID = null;
        try {
            try {
                final InitiateMultiPartUploadResponse multiPartUploadResponse = client.initiateMultiPartUpload(
                        new InitiateMultiPartUploadRequest(BUCKET_NAME, "beowulf"));
                uploadID = multiPartUploadResponse.getInitiateMultipartUploadResult().getUploadId();
            } catch (final FailedRequestException e) {
                assertThat(getCacheBytesAvailable(), lessThan(5000000000000L));
                assertThat(e.getStatusCode(), is(400));
            }

            final UUID uuid;
            if (uploadID != null) {
                uuid = UUID.fromString(uploadID);
            } else {
                uuid = UUID.randomUUID();
            }

            final AbortMultiPartUploadResponse abortResponse = client.abortMultiPartUpload(
                    new AbortMultiPartUploadRequest(BUCKET_NAME, "beowulf", uuid));

            assertThat(abortResponse.getStatusCode(), is(204));

        } catch (final FailedRequestException e) {

            assertThat(e.getStatusCode(), is(404));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test //TODO expand positive test if test target >5 TB cache is available
    public void listMultiPartUploadParts() throws IOException {
        try {
            final ListMultiPartUploadPartsResponse response = client.listMultiPartUploadParts(
                    new ListMultiPartUploadPartsRequest(BUCKET_NAME, "beowulf", UUID.randomUUID()));

            assertThat(response.getStatusCode(), is(200));
            assertTrue(response.getListPartsResult().getParts().isEmpty());

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test //TODO expand positive test if test target >5 TB cache is available
    public void completeMultiPartUpload() throws IOException {
        try {
            client.completeMultiPartUpload(
                    //Passing in a null request payload, which is sufficient for checking error code
                    new CompleteMultiPartUploadRequest(BUCKET_NAME, "beowulf", null, UUID.randomUUID()));

            fail("Response should have failed because upload part does not exist");

        } catch (final FailedRequestException e) {

            assertThat(e.getStatusCode(), is(404));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test //TODO expand positive test if test target >5 TB cache is available
    public void listMultiPartUploads() throws IOException {
        try {
            final ListMultiPartUploadsResponse response = client.listMultiPartUploads(
                    new ListMultiPartUploadsRequest(BUCKET_NAME));

            assertThat(response.getStatusCode(), is(200));
            assertTrue(response.getListMultiPartUploadsResult().getUploads().isEmpty());

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test //TODO expand positive test if test target >5 TB cache is available
    public void putMultiPartUploadPart() throws IOException {
        try {
            final int length = 1024;
            final Ds3Object obj = new Ds3Object("obj.txt", length);
            final byte[] randomData = IOUtils.toByteArray(new RandomDataInputStream(System.currentTimeMillis(), obj.getSize()));
            final ByteBuffer randomBuffer = ByteBuffer.wrap(randomData);

            final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(length);
            channel.write(randomBuffer);

            client.putMultiPartUploadPart(
                    new PutMultiPartUploadPartRequest(BUCKET_NAME, obj.getName(), channel, 5, length, UUID.randomUUID()));

            fail("Response should have failed because part does not exist");

        } catch (final FailedRequestException e) {

            assertThat(e.getStatusCode(), is(404));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void testWriteJobWithRetries() throws Exception
    {
        final Ds3ClientShim ds3ClientShim = new Ds3ClientShim((Ds3ClientImpl)client);

        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), null /* prefix */);

        try {
            final String DIR_NAME = "largeFiles/";
            final String[] FILE_NAMES = new String[] { "lesmis-copies.txt" };

            final Path dirPath = ResourceUtils.loadFileResource(DIR_NAME);

            final List<String> bookTitles = new ArrayList<>();
            final List<Ds3Object> objects = new ArrayList<>();
            for(final String book : FILE_NAMES) {
                final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
                final long bookSize = Files.size(objPath);
                final Ds3Object obj = new Ds3Object(book, bookSize);

                bookTitles.add(book);
                objects.add(obj);
            }

            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(ds3ClientShim, 1 /* num retries for http response 503 */);

            final Ds3ClientHelpers.Job writeJob = ds3ClientHelpers.startWriteJob(BUCKET_NAME, objects);
            writeJob.attachObjectCompletedListener(new ObjectCompletedListener() {
                @Override
                public void objectCompleted(final String name) {
                    assertTrue(bookTitles.contains(name));
                }
            });

            writeJob.transfer(new FileObjectPutter(dirPath));

            final GetBucketResponse request = ds3ClientShim.getBucket(new GetBucketRequest(BUCKET_NAME));
            final ListBucketResult result = request.getListBucketResult();

            assertEquals(bookTitles.size(), result.getObjects().size());

            for(final Contents contents : result.getObjects()) {
                assertTrue(bookTitles.contains(contents.getKey()));
            }

            final Ds3ClientHelpers.Job readJob = Ds3ClientHelpers.wrap(ds3ClientShim, 1)
                    .startReadAllJob(BUCKET_NAME);
            readJob.attachObjectCompletedListener(new ObjectCompletedListener() {
                @Override
                public void objectCompleted(final String name) {
                    assertTrue(bookTitles.contains(name));

                    try {
                        final File originalFile = ResourceUtils.loadFileResource(DIR_NAME + FILE_NAMES[0]).toFile();
                        final File fileCopiedFromBP = Paths.get(tempDirectory.toString(), FILE_NAMES[0]).toFile();
                        assertTrue(FileUtils.contentEquals(originalFile, fileCopiedFromBP));
                    } catch (URISyntaxException | IOException e) {
                        fail("Failure trying to compare file we wrote to file we read.");
                    }
                }
            });

            readJob.transfer(new FileObjectGetter(tempDirectory));
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
            deleteAllContents(ds3ClientShim, BUCKET_NAME);
        }
    }

    private static class Ds3ClientShim extends Ds3ClientImpl {
        private static Method getNetClientMethod = null;

        int numRetries = 0;

        static {
            try {
                getNetClientMethod = Ds3ClientImpl.class.getDeclaredMethod("getNetClient");
            } catch (final NoSuchMethodException e) {
                fail("Could not find Ds3ClientImpl method getNetClient.");
            }

            getNetClientMethod.setAccessible(true);
        }

        public Ds3ClientShim(final NetworkClient netClient) {
            super(netClient);
        }

        public Ds3ClientShim(final Ds3ClientImpl ds3ClientImpl) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            this((NetworkClient)getNetClientMethod.invoke(ds3ClientImpl));
        }

        @Override
        public PutObjectResponse putObject(final PutObjectRequest request) throws IOException {
            if(numRetries++ >= 1) {
                return super.putObject(request);
            }

            throw new Ds3NoMoreRetriesException(1);
        }

        @Override
        public Ds3Client newForNode(final JobNode node) {
            final ConnectionDetails newConnectionDetails;
            try {
                newConnectionDetails = ((NetworkClient)getNetClientMethod.invoke(this)).getConnectionDetails();
                final NetworkClient newNetClient = new NetworkClientImpl(newConnectionDetails);
                return new Ds3ClientShim(newNetClient);
            } catch (IllegalAccessException | InvocationTargetException e) {
                fail("Failure trying to create Ds3Client used in verifying putObject retries: " + e.getMessage());
            }

            return null;
        }
    }
}
