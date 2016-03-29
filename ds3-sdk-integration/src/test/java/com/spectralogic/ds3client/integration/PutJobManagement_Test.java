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
import com.spectralogic.ds3client.commands.spectrads3.notifications.*;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.integration.test.helpers.ABMTestHelper;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.apache.commons.io.IOUtils;
import org.junit.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.RESOURCE_BASE_NAME;
import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class PutJobManagement_Test {

    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String BUCKET_NAME = "Put_Job_Management_Test";

    private static final String TEST_ENV_NAME = "PutJobManagement_Test";
    private static TempStorageIds envStorageIds;

    @BeforeClass
    public static void startup() throws IOException, SignatureException {
        final UUID dataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, false, ChecksumType.Type.MD5, client);
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

    private long getCacheBytesAvailable() throws IOException, SignatureException {
        long cacheAvailableBytes = 0;
        final List<CacheFilesystemInformation> cacheFilesystemInformationList = client.getCacheStateSpectraS3(
                new GetCacheStateSpectraS3Request()).getCacheInformationResult().getFilesystems();
        for (final CacheFilesystemInformation filesystemInformation : cacheFilesystemInformationList){
            if (filesystemInformation.getAvailableCapacityInBytes() > cacheAvailableBytes){
                cacheAvailableBytes = filesystemInformation.getAvailableCapacityInBytes();
            }
        }
        return cacheAvailableBytes;
    }

    @SuppressWarnings("deprecation")
    @Test
    public void nakedS3Put() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
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
    public void getActiveJobs() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        try {
            final UUID jobID = HELPERS
                    .startWriteJob(BUCKET_NAME, Lists.newArrayList( new Ds3Object("test", 2))).getJobId();
            final GetActiveJobsSpectraS3Response activeJobsResponse = client.
                    getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request());
            final ArrayList<UUID> activeJobsUUIDs = new ArrayList<>();
            for (final ActiveJob job : activeJobsResponse.getActiveJobListResult().getActiveJobs()){
                activeJobsUUIDs.add(job.getId());
            }
            assertThat(activeJobsUUIDs, contains(jobID));
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void getJobs() throws IOException, SignatureException, XmlProcessingException {
        try {
            final UUID jobID = HELPERS
                    .startWriteJob(BUCKET_NAME, Lists.newArrayList( new Ds3Object("test", 2))).getJobId();
            final GetJobsSpectraS3Response getJobsResponse = client.
                    getJobsSpectraS3(new GetJobsSpectraS3Request());
            final ArrayList<UUID> jobUUIDs = new ArrayList<>();
            for (final Job job : getJobsResponse.getJobListResult().getJobs()){
                jobUUIDs.add(job.getJobId());
            }
            assertThat(jobUUIDs, contains(jobID));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void modifyJobPriority() throws IOException, SignatureException, XmlProcessingException {
        try {
            final Ds3ClientHelpers.Job job =
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList( new Ds3Object("test", 2)),
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
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("testOne", 2)));

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
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("testOne", 2)));
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
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("testOne", 2)));

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
    public void clearAllCanceledJobs() throws IOException, SignatureException, XmlProcessingException {

        try {
            final Ds3ClientHelpers.Job job =
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("testOne", 2)));
            client.cancelJobSpectraS3(new CancelJobSpectraS3Request(job.getJobId()));
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
    public void truncateJobCancelWithOutForce() throws Exception {

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

            try {
                client.cancelJobSpectraS3(new CancelJobSpectraS3Request(jobId));
            } catch (final FailedRequestException e) {
                assertThat(e.getStatusCode(), is(400));
            }

            final GetJobSpectraS3Response truncatedJob = client.getJobSpectraS3(new GetJobSpectraS3Request(jobId));
            assertEquals(truncatedJob.getMasterObjectListResult().getOriginalSizeInBytes(), Files.size(objPath1));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void cancelJobWithForce() throws Exception {

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
                    .cancelJobSpectraS3(new CancelJobSpectraS3Request(jobId).withForce(true));
            assertEquals(responseWithForce.getStatusCode(), 204);

            //Allow for lag time before canceled job appears~1.5 seconds in unloaded system
            boolean jobCanceled = false;
            while (!jobCanceled) {
                Thread.sleep(500);
                final GetCanceledJobsSpectraS3Response canceledJobs = client.getCanceledJobsSpectraS3(new GetCanceledJobsSpectraS3Request());
                for (final CanceledJob canceledJob : canceledJobs.getCanceledJobListResult().getCanceledJobs()){
                    if (canceledJob.getId().equals(jobId)){
                        jobCanceled = true;
                    }
                }
            }

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void cancelAllJobs() throws IOException, SignatureException, XmlProcessingException {

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
    public void truncateCancelAllJobsWithoutForce() throws Exception {

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

            HELPERS.startWriteJob(BUCKET_NAME, Lists
                    .newArrayList(new Ds3Object("place_holder_3", 1000000)));

            ABMTestHelper.waitForJobCachedSizeToBeMoreThanZero(jobId1, client, 20);
            ABMTestHelper.waitForJobCachedSizeToBeMoreThanZero(jobId2, client, 20);

            try {
                client.cancelAllJobsSpectraS3(new CancelAllJobsSpectraS3Request());
            } catch (final FailedRequestException e) {
                assertThat(e.getStatusCode(), is(400));
            }

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
    public void cancelAllJobsWithForce () throws Exception {

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

            HELPERS.startWriteJob(BUCKET_NAME, Lists
                    .newArrayList(new Ds3Object("place_holder_3", 1000000)));

            ABMTestHelper.waitForJobCachedSizeToBeMoreThanZero(jobId1, client, 20);
            ABMTestHelper.waitForJobCachedSizeToBeMoreThanZero(jobId2, client, 20);

            client.cancelAllJobsSpectraS3(new CancelAllJobsSpectraS3Request().withForce(true));

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
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test", 2)));
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

    @Test
    public void getJobChunksReady() throws IOException, SignatureException, XmlProcessingException {

        try {
            final Ds3Object ds3Object = new Ds3Object("test", 2);
            final Ds3ClientHelpers.Job jobOne =
                    HELPERS.startWriteJob(BUCKET_NAME, Lists.newArrayList(ds3Object));
            final UUID jobOneId = jobOne.getJobId();

            final GetJobChunksReadyForClientProcessingSpectraS3Response response = client
                    .getJobChunksReadyForClientProcessingSpectraS3
                            (new GetJobChunksReadyForClientProcessingSpectraS3Request(jobOneId));

            final List<String> chunkNames = new ArrayList<>();
            for (final Objects objectList : response.getMasterObjectListResult().getObjects()) {
                for (final BulkObject bulkObject : objectList.getObjects()){
                    chunkNames.add(bulkObject.getName());
                }
            }

            assertThat(chunkNames, contains(ds3Object.getName()));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void getJobChunk() throws IOException, SignatureException, XmlProcessingException {

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
    public void aggregateTwoJobs() throws IOException, SignatureException, XmlProcessingException {

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
    public void allocateJobChunk() throws IOException, SignatureException, XmlProcessingException {

        try {
            final PutBulkJobSpectraS3Response putBulkResponse = client.
                    putBulkJobSpectraS3(new PutBulkJobSpectraS3Request(BUCKET_NAME, Lists.newArrayList(new Ds3Object("test", 2))));
            final UUID chunkUUID = putBulkResponse.getResult().getObjects().get(0).getChunkId();
            final AllocateJobChunkSpectraS3Response allocateResponse = client
                    .allocateJobChunkSpectraS3(new AllocateJobChunkSpectraS3Request(chunkUUID));

            assertThat(allocateResponse.getStatusCode(), is(200));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void putObjectCachedNotification() throws IOException, SignatureException, XmlProcessingException {
            final PutObjectCachedNotificationRegistrationSpectraS3Response putNotificationResponse = client
                    .putObjectCachedNotificationRegistrationSpectraS3
                            (new PutObjectCachedNotificationRegistrationSpectraS3Request("test@test.test"));

            assertThat(putNotificationResponse.getStatusCode(), is(201));
    }

    @Test
    public void getObjectCachedNotification() throws IOException, SignatureException, XmlProcessingException {

        final PutObjectCachedNotificationRegistrationSpectraS3Response putNotificationResponse = client
                .putObjectCachedNotificationRegistrationSpectraS3
                        (new PutObjectCachedNotificationRegistrationSpectraS3Request("test@test.test"));
        final GetObjectCachedNotificationRegistrationSpectraS3Response getNotificationResponse = client
                .getObjectCachedNotificationRegistrationSpectraS3(
                        (new GetObjectCachedNotificationRegistrationSpectraS3Request
                                (putNotificationResponse.getS3ObjectCachedNotificationRegistrationResult().getId())));

        assertThat(getNotificationResponse.getStatusCode(), is(200));
    }

    @Test
    public void getCompletedJobs() throws IOException, SignatureException, XmlProcessingException {
        final GetCompletedJobsSpectraS3Response getCompletedJobsResponse = client.
                getCompletedJobsSpectraS3(new GetCompletedJobsSpectraS3Request());

        assertThat(getCompletedJobsResponse.getStatusCode(), is(200));
    }

    @Test
    public void clearCompletedJobs() throws IOException, SignatureException, XmlProcessingException {
        final ClearAllCompletedJobsSpectraS3Response clearAllCompletedJobsResponse = client
                .clearAllCompletedJobsSpectraS3(new ClearAllCompletedJobsSpectraS3Request());

        assertThat(clearAllCompletedJobsResponse.getStatusCode(), is(204));
    }

    @Test
    public void putJobCreatedNotification() throws IOException, SignatureException {
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
    public void getJobCreatedNotification() throws IOException, SignatureException {
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
    public void deleteJobCreatedNotification() throws IOException, SignatureException {
        final UUID notificationUUID = client.putJobCreatedNotificationRegistrationSpectraS3(
                new PutJobCreatedNotificationRegistrationSpectraS3Request("test@test.test"))
                .getJobCreatedNotificationRegistrationResult().getId();

        final DeleteJobCreatedNotificationRegistrationSpectraS3Response response = client
                .deleteJobCreatedNotificationRegistrationSpectraS3(new DeleteJobCreatedNotificationRegistrationSpectraS3Request(notificationUUID));

        assertThat(response.getStatusCode(), is(204));
    }

    @Test
    public void putJobCompletedNotification() throws IOException, SignatureException {
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
    public void getJobCompletedNotification() throws IOException, SignatureException {
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
    public void deleteJobCompletedNotification() throws IOException, SignatureException {
        final UUID notificationUUID = client.putJobCompletedNotificationRegistrationSpectraS3(
                new PutJobCompletedNotificationRegistrationSpectraS3Request("test@test.test"))
                .getJobCompletedNotificationRegistrationResult().getId();

        final DeleteJobCompletedNotificationRegistrationSpectraS3Response response = client
                .deleteJobCompletedNotificationRegistrationSpectraS3(
                        new DeleteJobCompletedNotificationRegistrationSpectraS3Request(notificationUUID));

        assertThat(response.getStatusCode(), is(204));
    }

    @Test
    public void initiateMultipartUpload() throws IOException, SignatureException {
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
    public void abortMultipartUpload() throws IOException, SignatureException {
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
    public void listMultiPartUploadParts() throws IOException, SignatureException {
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
    public void completeMultiPartUpload() throws IOException, SignatureException {
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
    public void listMultiPartUploads() throws IOException, SignatureException {
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
    public void putMultiPartUploadPart() throws IOException, SignatureException {
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
}
