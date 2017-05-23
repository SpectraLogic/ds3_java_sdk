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

package com.spectralogic.ds3client.integration;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.Ds3ClientImpl;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.commands.spectrads3.notifications.*;
import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;
import com.spectralogic.ds3client.helpers.ChecksumFunction;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FailureEventListener;
import com.spectralogic.ds3client.helpers.FileObjectGetter;
import com.spectralogic.ds3client.helpers.FileObjectPutter;
import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.ObjectCompletedListener;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.helpers.events.SameThreadEventRunner;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.BlobStrategy;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.ChunkAttemptRetryBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.ChunkAttemptRetryDelayBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.ClientDefinedChunkAttemptRetryDelayBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.MaxChunkAttemptsRetryBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.PutSequentialBlobStrategy;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.ChannelStrategy;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.SequentialFileReaderChannelStrategy;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcher;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcherImpl;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.MaxNumObjectTransferAttemptsDecorator;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferMethod;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferRetryDecorator;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategy;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategyBuilder;
import com.spectralogic.ds3client.integration.test.helpers.ABMTestHelper;
import com.spectralogic.ds3client.integration.test.helpers.Ds3ClientShimFactory;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.metadata.MetadataAccessImpl;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import com.spectralogic.ds3client.utils.Platform;
import com.spectralogic.ds3client.utils.ResourceUtils;

import com.spectralogic.ds3client.integration.test.helpers.Ds3ClientShimFactory.ClientFailureType;

import com.spectralogic.ds3client.utils.hashing.ChecksumUtils;
import com.spectralogic.ds3client.utils.hashing.Hasher;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.spectralogic.ds3client.integration.test.helpers.Ds3ClientShim;

import static com.spectralogic.ds3client.integration.Util.RESOURCE_BASE_NAME;
import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import com.spectralogic.ds3client.helpers.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PutJobManagement_Test {
    private static final Logger LOG = LoggerFactory.getLogger(PutJobManagement_Test.class);

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
            assertThat(putObjectResponse, is(notNullValue()));
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
            assertThat(response, is(notNullValue()));

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
            assertThat(truncateJobSpectraS3Response, is(notNullValue()));

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
            assertThat(responseWithForce, is(notNullValue()));

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

            assertThat(truncateAllJobsSpectraS3Response, is(notNullValue()));

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

            assertThat(getJobChunkSpectraS3Response.getObjectsResult(), is(notNullValue()));

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
    public void testPutJobOptionsWithStreamingBehavior() throws IOException {

        try {
            final WriteJobOptions writeJobOptions = WriteJobOptions.create().withAggregating();

            final Ds3ClientHelpers.Job jobOne = HELPERS.startWriteJobUsingStreamedBehavior(BUCKET_NAME,
                    Lists.newArrayList(new Ds3Object("test", 2)),
                    writeJobOptions);
            final UUID jobOneId = jobOne.getJobId();

            final Ds3ClientHelpers.Job jobTwo = HELPERS.startWriteJobUsingStreamedBehavior(BUCKET_NAME,
                    Lists.newArrayList(new Ds3Object("test2", 2)),
                    writeJobOptions);
            final UUID jobTwoId = jobTwo.getJobId();

            assertThat(jobOneId, is(jobTwoId));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void testPutJobOptionsWithRandomAccessBehavior() throws IOException {

        try {
            final WriteJobOptions writeJobOptions = WriteJobOptions.create().withAggregating();

            final Ds3ClientHelpers.Job jobOne = HELPERS.startWriteJobUsingRandomAccessBehavior(BUCKET_NAME,
                    Lists.newArrayList(new Ds3Object("test", 2)),
                    writeJobOptions);
            final UUID jobOneId = jobOne.getJobId();

            final Ds3ClientHelpers.Job jobTwo = HELPERS.startWriteJobUsingRandomAccessBehavior(BUCKET_NAME,
                    Lists.newArrayList(new Ds3Object("test2", 2)),
                    writeJobOptions);
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
            final UUID chunkUUID = putBulkResponse.getMasterObjectList().getObjects().get(0).getChunkId();
            final AllocateJobChunkSpectraS3Response allocateResponse = client
                    .allocateJobChunkSpectraS3(new AllocateJobChunkSpectraS3Request(chunkUUID.toString()));

            assertThat(allocateResponse.getStatus(), is(AllocateJobChunkSpectraS3Response.Status.ALLOCATED));

        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void putObjectCachedNotification() throws IOException {
        final PutObjectCachedNotificationRegistrationSpectraS3Response putNotificationResponse = client
                .putObjectCachedNotificationRegistrationSpectraS3
                        (new PutObjectCachedNotificationRegistrationSpectraS3Request("test@test.test"));

        assertThat(putNotificationResponse.getS3ObjectCachedNotificationRegistrationResult(), is(notNullValue()));
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

        assertThat(getNotificationResponse.getS3ObjectCachedNotificationRegistrationResult(), is(notNullValue()));
    }

    @Test
    public void getCompletedJobs() throws IOException {
        final GetCompletedJobsSpectraS3Response getCompletedJobsResponse = client.
                getCompletedJobsSpectraS3(new GetCompletedJobsSpectraS3Request());

        assertThat(getCompletedJobsResponse.getCompletedJobListResult(), is(notNullValue()));
    }

    @Test
    public void clearCompletedJobs() throws IOException {
        final ClearAllCompletedJobsSpectraS3Response clearAllCompletedJobsResponse = client
                .clearAllCompletedJobsSpectraS3(new ClearAllCompletedJobsSpectraS3Request());

        assertThat(clearAllCompletedJobsResponse, is(notNullValue()));
    }

    @Test
    public void putJobCreatedNotification() throws IOException {
        UUID notificationUUID = null;
        try {
            final PutJobCreatedNotificationRegistrationSpectraS3Response response = client
                    .putJobCreatedNotificationRegistrationSpectraS3(new PutJobCreatedNotificationRegistrationSpectraS3Request("test@test.test"));
            notificationUUID = response.getJobCreatedNotificationRegistrationResult().getId();
            assertThat(response.getJobCreatedNotificationRegistrationResult(), is(notNullValue()));

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

            assertThat(response.getJobCreatedNotificationRegistrationResult(), is(notNullValue()));

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

        assertThat(response, is(notNullValue()));
    }

    @Test
    public void putJobCompletedNotification() throws IOException {
        UUID notificationUUID = null;
        try {
            final PutJobCompletedNotificationRegistrationSpectraS3Response response = client
                    .putJobCompletedNotificationRegistrationSpectraS3(
                            new PutJobCompletedNotificationRegistrationSpectraS3Request("test@test.test"));
            notificationUUID = response.getJobCompletedNotificationRegistrationResult().getId();
            assertThat(response.getJobCompletedNotificationRegistrationResult(), is(notNullValue()));
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

            assertThat(response.getJobCompletedNotificationRegistrationResult(), is(notNullValue()));

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

        assertThat(response, is(notNullValue()));
    }

    @Test
    public void initiateMultipartUpload() throws IOException {
        try {
            final InitiateMultiPartUploadResponse multiPartUploadResponse = client.initiateMultiPartUpload(
                    new InitiateMultiPartUploadRequest(BUCKET_NAME, "beowulf"));

            assertThat(multiPartUploadResponse.getInitiateMultipartUploadResult(), is(notNullValue()));
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

            assertThat(abortResponse, is(notNullValue()));

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

            assertThat(response.getListPartsResult(), is(notNullValue()));
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

            assertThat(response.getListMultiPartUploadsResult(), is(notNullValue()));
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
    public void testWriteJobWithRetries() throws Exception {
        final int maxNumObjectTransferAttempts = 3;
        final boolean computeChecksumWithUserSuppliedFunction = false;

        transferAndCheckFileContent(maxNumObjectTransferAttempts,
                new ObjectTransferExceptionHandler() {
                    @Override
                    public boolean handleException(final Throwable t) {
                        t.printStackTrace();
                        fail("Got unexpected exception: " + t.getMessage());
                        return false;
                    }
                }, computeChecksumWithUserSuppliedFunction);
    }

    private void transferAndCheckFileContent(final int maxNumObjectTransferAttempts,
                                             final ObjectTransferExceptionHandler objectTransferExceptionHandler,
                                             final boolean computeChecksumWithUserSuppliedFunction)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, URISyntaxException
    {
        final String userAgent = "Agent Provocateur";

        final Ds3Client clientWithUserAgent = Ds3ClientBuilder.fromEnv()
                .withUserAgent(userAgent)
                .withHttps(false)
                .build();

        final Ds3ClientShim ds3ClientShim = new Ds3ClientShim((Ds3ClientImpl) clientWithUserAgent);

        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final String DIR_NAME = "largeFiles/";
            final String[] FILE_NAMES = new String[]{"lesmis-copies.txt"};

            final Path dirPath = ResourceUtils.loadFileResource(DIR_NAME);

            final List<String> bookTitles = new ArrayList<>();
            final List<Ds3Object> objects = new ArrayList<>();
            for (final String book : FILE_NAMES) {
                final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
                final long bookSize = Files.size(objPath);
                final Ds3Object obj = new Ds3Object(book, bookSize);

                bookTitles.add(book);
                objects.add(obj);
            }

            final int maxNumBlockAllocationRetries = 1;
            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(ds3ClientShim,
                    maxNumBlockAllocationRetries,
                    maxNumObjectTransferAttempts);

            final AtomicInteger numTimesObjectCompletedEventCalled = new AtomicInteger(0);
            final AtomicInteger numTimesDataTransferredEventCalled = new AtomicInteger(0);
            final AtomicBoolean dataTransferredEventReceived = new AtomicBoolean(false);
            final AtomicBoolean objectCompletedEventReceived = new AtomicBoolean(false);
            final AtomicBoolean checksumEventReceived = new AtomicBoolean(false);
            final AtomicBoolean waitingForChunksEventReceived = new AtomicBoolean(false);
            final AtomicBoolean failureEventReceived = new AtomicBoolean(false);
            final AtomicBoolean checksumFunctionCalled = new AtomicBoolean(false);

            final WriteJobOptions writeJobOptions = WriteJobOptions.create()
                    .withChecksumType(ChecksumType.Type.MD5);

            final Ds3ClientHelpers.Job writeJob = ds3ClientHelpers.startWriteJob(BUCKET_NAME, objects, writeJobOptions);

            if (computeChecksumWithUserSuppliedFunction) {
                writeJob.withChecksum(new ChecksumFunction() {
                    @Override
                    public String compute(final BulkObject obj, final ByteChannel channel) {
                        checksumFunctionCalled.set(true);
                        try {
                            final InputStream dataStream = new BufferedInputStream(Channels.newInputStream(channel));

                            dataStream.mark(Integer.MAX_VALUE);

                            final Hasher hasher = ChecksumUtils.getHasher(ChecksumType.Type.MD5);

                            final String checksum = ChecksumUtils.hashInputStream(hasher, dataStream);

                            LOG.info("Computed checksum for blob: {}", checksum);

                            dataStream.reset();

                            return checksum;
                        } catch (final IOException e) {
                            LOG.error("Error calculating checksum for: " + obj.getName(), e);
                            fail("Error calculating checksum for: " + obj.getName());
                        }

                        return null;
                    }
                });
            }

            writeJob.attachFailureEventListener(new FailureEventListener() {
                @Override
                public void onFailure(final FailureEvent failureEvent) {
                    failureEventReceived.set(true);
                }
            });

            writeJob.attachChecksumListener(new ChecksumListener() {
                @Override
                public void value(final BulkObject obj, final ChecksumType.Type type, final String checksum) {
                    checksumEventReceived.set(true);
                    assertEquals("0feqCQBgdtmmgGs9pB/Huw==", checksum);
                }
            });

            writeJob.attachObjectCompletedListener(new ObjectCompletedListener() {
                private int numCompletedObjects = 0;

                @Override
                public void objectCompleted(final String name) {
                    objectCompletedEventReceived.set(true);
                    assertTrue(bookTitles.contains(name));
                    assertEquals(1, ++numCompletedObjects);
                    numTimesObjectCompletedEventCalled.getAndIncrement();
                }
            });

            writeJob.attachDataTransferredListener(new DataTransferredListener() {
                @Override
                public void dataTransferred(final long size) {
                    dataTransferredEventReceived.set(true);
                    assertEquals(objects.get(0).getSize(), size);
                    numTimesDataTransferredEventCalled.getAndIncrement();
                }
            });

            writeJob.attachWaitingForChunksListener(new WaitingForChunksListener() {
                @Override
                public void waiting(final int secondsToWait) {
                    waitingForChunksEventReceived.set(true);
                }
            });

            boolean shouldContinueTest = true;

            try {
                writeJob.transfer(new FileObjectPutter(dirPath));
            } catch (final Throwable t) {
                shouldContinueTest = objectTransferExceptionHandler.handleException(t);
            }

            if (!shouldContinueTest) {
                return;
            }

            if (computeChecksumWithUserSuppliedFunction) {
                assertTrue(checksumFunctionCalled.get());
            } else {
                assertFalse(checksumFunctionCalled.get());
            }
            assertTrue(dataTransferredEventReceived.get());
            assertTrue(objectCompletedEventReceived.get());
            assertTrue(checksumEventReceived.get());
            assertFalse(waitingForChunksEventReceived.get());
            assertFalse(failureEventReceived.get());

            assertEquals(1, numTimesObjectCompletedEventCalled.get());
            assertEquals(1, numTimesDataTransferredEventCalled.get());

            final GetBucketResponse request = ds3ClientShim.getBucket(new GetBucketRequest(BUCKET_NAME));
            final ListBucketResult result = request.getListBucketResult();

            assertEquals(bookTitles.size(), result.getObjects().size());

            for (final Contents contents : result.getObjects()) {
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
                    } catch (final URISyntaxException | IOException e) {
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

    private interface ObjectTransferExceptionHandler {
        boolean handleException(final Throwable t);
    }

    @Test
    public void testWriteJobWithRetriesAndUserDefinedChecksum() throws Exception {
        final int maxNumObjectTransferAttempts = 3;
        final boolean computeChecksumWithUserSuppliedFunction = true;

        transferAndCheckFileContent(maxNumObjectTransferAttempts,
                new ObjectTransferExceptionHandler() {
                    @Override
                    public boolean handleException(final Throwable t) {
                        t.printStackTrace();
                        fail("Got unexpected exception: " + t.getMessage());
                        return false;
                    }
                }, computeChecksumWithUserSuppliedFunction);
    }

    @Test
    public void testWriteJobWithRetriesThrowsDs3NoMoreRetriesException() throws Exception {
        final int maxNumObjectTransferAttempts = 1;
        final boolean computeChecksumWithUserSuppliedFunction = false;

        transferAndCheckFileContent(maxNumObjectTransferAttempts,
                new ObjectTransferExceptionHandler() {
                    @Override
                    public boolean handleException(final Throwable t) {
                        if (!(t instanceof Ds3NoMoreRetriesException)) {
                            fail("Got exception of unexpected type: " + t.getMessage());
                            return true;
                        }

                        return false;
                    }
                }, computeChecksumWithUserSuppliedFunction);
    }

    @Test
    public void testFiringOnFailureEventWithFailedChunkAllocation()
            throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final AtomicInteger numFailureEventsFired = new AtomicInteger(0);

            final int maxNumObjectTransferAttempts = 1;
            final Ds3ClientHelpers.Job writeJob = createWriteJobWithObjectsReadyToTransfer(maxNumObjectTransferAttempts,
                    ClientFailureType.ChunkAllocation);

            final FailureEventListener failureEventListener = new FailureEventListener() {
                @Override
                public void onFailure(final FailureEvent failureEvent) {
                    numFailureEventsFired.getAndIncrement();
                    assertEquals(FailureEvent.FailureActivity.PuttingObject, failureEvent.doingWhat());
                }
            };

            writeJob.attachFailureEventListener(failureEventListener);

            try {
                writeJob.transfer(new FileObjectPutter(tempDirectory));
            } catch (final Ds3NoMoreRetriesException e) {
                assertEquals(1, numFailureEventsFired.get());
            }
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    Ds3ClientHelpers.Job createWriteJobWithObjectsReadyToTransfer(final int maxNumObjectTransferAttempts,
                                                                  final ClientFailureType clientFailureType)
            throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final String DIR_NAME = "largeFiles/";
        final String[] FILE_NAMES = new String[]{"lesmis-copies.txt"};

        final List<String> bookTitles = new ArrayList<>();
        final List<Ds3Object> objects = new ArrayList<>();
        for (final String book : FILE_NAMES) {
            final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
            final long bookSize = Files.size(objPath);
            final Ds3Object obj = new Ds3Object(book, bookSize);

            bookTitles.add(book);
            objects.add(obj);
        }

        final Ds3Client ds3Client = Ds3ClientShimFactory.makeWrappedDs3Client(clientFailureType, client);

        final int maxNumBlockAllocationRetries = 3;
        final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(ds3Client,
                maxNumBlockAllocationRetries,
                maxNumObjectTransferAttempts);

        final Ds3ClientHelpers.Job writeJob = ds3ClientHelpers.startWriteJob(BUCKET_NAME, objects);

        return writeJob;
    }

    @Test
    public void testFiringWaitingForChunksEventWithFailedChunkAllocation()
            throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final AtomicInteger numFailureEventsFired = new AtomicInteger(0);
            final AtomicInteger numWaitingForChunksEventsFired = new AtomicInteger(0);

            final int maxNumObjectTransferAttempts = 3;
            final Ds3ClientHelpers.Job writeJob = createWriteJobWithObjectsReadyToTransfer(maxNumObjectTransferAttempts,
                    ClientFailureType.ChunkAllocation);

            final FailureEventListener failureEventListener = new FailureEventListener() {
                @Override
                public void onFailure(final FailureEvent failureEvent) {
                    numFailureEventsFired.getAndIncrement();
                    assertEquals(FailureEvent.FailureActivity.PuttingObject, failureEvent.doingWhat());
                }
            };

            writeJob.attachFailureEventListener(failureEventListener);

            writeJob.attachWaitingForChunksListener(new WaitingForChunksListener() {
                @Override
                public void waiting(final int secondsToWait) {
                    numWaitingForChunksEventsFired.getAndIncrement();
                }
            });

            try {
                writeJob.transfer(new FileObjectPutter(tempDirectory));
            } catch (final Ds3NoMoreRetriesException e) {
                assertEquals(1, numFailureEventsFired.get());
            }

            assertEquals(maxNumObjectTransferAttempts, numWaitingForChunksEventsFired.get());
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void testFiringOnFailureEventWithFailedPutObject()
            throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final AtomicInteger numFailureEventsFired = new AtomicInteger();

            final int maxNumObjectTransferAttempts = 1;
            final Ds3ClientHelpers.Job writeJob = createWriteJobWithObjectsReadyToTransfer(maxNumObjectTransferAttempts,
                    ClientFailureType.PutObject);

            final FailureEventListener failureEventListener = new FailureEventListener() {
                @Override
                public void onFailure(final FailureEvent failureEvent) {
                    numFailureEventsFired.incrementAndGet();
                    assertEquals(FailureEvent.FailureActivity.PuttingObject, failureEvent.doingWhat());
                }
            };

            writeJob.attachFailureEventListener(failureEventListener);

            try {
                writeJob.transfer(new FileObjectPutter(tempDirectory));
            } catch (final IOException | RuntimeException e) {
                assertEquals(1, numFailureEventsFired.get());
            }
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void testPutJobUsingTransferStrategy() throws IOException, InterruptedException, URISyntaxException {
        final String DIR_NAME = "books/";
        final String[] FILE_NAMES = new String[]{"beowulf.txt"};

        try {
            final Path dirPath = ResourceUtils.loadFileResource(DIR_NAME);

            final List<String> bookTitles = new ArrayList<>();
            final List<Ds3Object> objectsToWrite = new ArrayList<>();
            for (final String book : FILE_NAMES) {
                final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
                final long bookSize = Files.size(objPath);
                final Ds3Object obj = new Ds3Object(book, bookSize);

                bookTitles.add(book);
                objectsToWrite.add(obj);
            }

            final PutBulkJobSpectraS3Request request = new PutBulkJobSpectraS3Request(BUCKET_NAME, Lists.newArrayList(objectsToWrite));
            final PutBulkJobSpectraS3Response putBulkJobSpectraS3Response = client.putBulkJobSpectraS3(request);

            final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
                    .withDs3Client(client)
                    .withMasterObjectList(putBulkJobSpectraS3Response.getMasterObjectList())
                    .withChannelBuilder(new FileObjectPutter(dirPath));

            final TransferStrategy transferStrategy = transferStrategyBuilder.makePutTransferStrategy();

            transferStrategy.transfer();

            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client);
            final Iterable<Contents> bucketContentsIterable = ds3ClientHelpers.listObjects(BUCKET_NAME);

            for (final Contents bucketContents : bucketContentsIterable) {
                assertEquals(FILE_NAMES[0], bucketContents.getKey());
            }
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void testWriteJobUsingTransferStrategy() throws IOException, InterruptedException, URISyntaxException {
        final String DIR_NAME = "books/";
        final String[] FILE_NAMES = new String[]{"beowulf.txt"};

        try {
            final Path dirPath = ResourceUtils.loadFileResource(DIR_NAME);

            final List<String> bookTitles = new ArrayList<>();
            final List<Ds3Object> objectsToWrite = new ArrayList<>();
            for (final String book : FILE_NAMES) {
                final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
                final long bookSize = Files.size(objPath);
                final Ds3Object obj = new Ds3Object(book, bookSize);

                bookTitles.add(book);
                objectsToWrite.add(obj);
            }

            final PutBulkJobSpectraS3Request request = new PutBulkJobSpectraS3Request(BUCKET_NAME, Lists.newArrayList(objectsToWrite));
            final PutBulkJobSpectraS3Response putBulkJobSpectraS3Response = client.putBulkJobSpectraS3(request);

            final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
                    .withDs3Client(client)
                    .withMasterObjectList(putBulkJobSpectraS3Response.getMasterObjectList())
                    .withChannelBuilder(new FileObjectPutter(dirPath));

            final TransferStrategy transferStrategy = transferStrategyBuilder.makePutTransferStrategy();

            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client);

            final Ds3ClientHelpers.Job writeJob = ds3ClientHelpers.startWriteJob(transferStrategy);

            writeJob.transfer();

            final Iterable<Contents> bucketContentsIterable = ds3ClientHelpers.listObjects(BUCKET_NAME);

            for (final Contents bucketContents : bucketContentsIterable) {
                assertEquals(FILE_NAMES[0], bucketContents.getKey());
            }
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void testPutJobWithUserSuppliedBlobStrategy() throws IOException, InterruptedException, URISyntaxException {
        final String DIR_NAME = "books/";
        final String[] FILE_NAMES = new String[]{"beowulf.txt"};

        try {
            final Path dirPath = ResourceUtils.loadFileResource(DIR_NAME);

            final List<String> bookTitles = new ArrayList<>();
            final List<Ds3Object> objectsToWrite = new ArrayList<>();
            for (final String book : FILE_NAMES) {
                final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
                final long bookSize = Files.size(objPath);
                final Ds3Object obj = new Ds3Object(book, bookSize);

                bookTitles.add(book);
                objectsToWrite.add(obj);
            }

            final PutBulkJobSpectraS3Request request = new PutBulkJobSpectraS3Request(BUCKET_NAME, Lists.newArrayList(objectsToWrite));
            final PutBulkJobSpectraS3Response putBulkJobSpectraS3Response = client.putBulkJobSpectraS3(request);

            final MasterObjectList masterObjectList = putBulkJobSpectraS3Response.getMasterObjectList();

            final EventDispatcher eventDispatcher = new EventDispatcherImpl(new SameThreadEventRunner());

            final AtomicInteger numChunkAllocationAttempts = new AtomicInteger(0);

            final BlobStrategy blobStrategy = new UserSuppliedPutBlobStrategy(client,
                    masterObjectList,
                    eventDispatcher,
                    new MaxChunkAttemptsRetryBehavior(5),
                    new ClientDefinedChunkAttemptRetryDelayBehavior(1, eventDispatcher),
                    new Monitorable() {
                        @Override
                        public void monitor() {
                            numChunkAllocationAttempts.incrementAndGet();
                        }
                    });

            final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
                    .withDs3Client(client)
                    .withMasterObjectList(masterObjectList)
                    .withChannelBuilder(new FileObjectPutter(dirPath))
                    .withBlobStrategy(blobStrategy);

            final TransferStrategy transferStrategy = transferStrategyBuilder.makePutTransferStrategy();
            transferStrategy.transfer();

            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client);
            final Iterable<Contents> bucketContentsIterable = ds3ClientHelpers.listObjects(BUCKET_NAME);

            for (final Contents bucketContents : bucketContentsIterable) {
                assertEquals(FILE_NAMES[0], bucketContents.getKey());
            }

            assertEquals(1, numChunkAllocationAttempts.get());
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    private interface Monitorable {
        void monitor();
    }

    private class UserSuppliedPutBlobStrategy implements BlobStrategy {
        private final BlobStrategy wrappedBlobStrategy;
        private final Monitorable monitorable;

        private UserSuppliedPutBlobStrategy(final Ds3Client client,
                                            final MasterObjectList masterObjectList,
                                            final EventDispatcher eventDispatcher,
                                            final ChunkAttemptRetryBehavior retryBehavior,
                                            final ChunkAttemptRetryDelayBehavior chunkAttemptRetryDelayBehavior,
                                            final Monitorable monitorable)
        {
            this.monitorable = monitorable;

            wrappedBlobStrategy = new PutSequentialBlobStrategy(client, masterObjectList, eventDispatcher, retryBehavior, chunkAttemptRetryDelayBehavior);
        }

        @Override
        public Iterable<JobPart> getWork() throws IOException, InterruptedException {
            monitorable.monitor();

            return wrappedBlobStrategy.getWork();
        }
    }

    @Test
    public void testPutJobWithUserSuppliedChannelStrategy() throws IOException, InterruptedException, URISyntaxException {
        testPutJobWithUserSuppliedChannelStrategy(new TransferStrategyBuilderModifiable() {
            @Override
            public TransferStrategyBuilder modify(final TransferStrategyBuilder transferStrategyBuilder) {
                return transferStrategyBuilder;
            }
        });
    }

    private void testPutJobWithUserSuppliedChannelStrategy(final TransferStrategyBuilderModifiable strategyBuilderModifiable) throws IOException, InterruptedException, URISyntaxException {
        final String DIR_NAME = "books/";
        final String[] FILE_NAMES = new String[]{"beowulf.txt"};

        try {
            final Path dirPath = ResourceUtils.loadFileResource(DIR_NAME);

            final List<String> bookTitles = new ArrayList<>();
            final List<Ds3Object> objectsToWrite = new ArrayList<>();
            for (final String book : FILE_NAMES) {
                final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
                final long bookSize = Files.size(objPath);
                final Ds3Object obj = new Ds3Object(book, bookSize);

                bookTitles.add(book);
                objectsToWrite.add(obj);
            }

            final PutBulkJobSpectraS3Request request = new PutBulkJobSpectraS3Request(BUCKET_NAME, Lists.newArrayList(objectsToWrite));
            final PutBulkJobSpectraS3Response putBulkJobSpectraS3Response = client.putBulkJobSpectraS3(request);

            final MasterObjectList masterObjectList = putBulkJobSpectraS3Response.getMasterObjectList();

            final AtomicInteger numTimesChannelOpened = new AtomicInteger(0);
            final AtomicInteger numTimesChannelClosed = new AtomicInteger(0);

            final Ds3ClientHelpers.ObjectChannelBuilder objectChannelBuilder = new FileObjectPutter(dirPath);
            final ChannelStrategy channelStrategy = new UserSuppliedPutChannelStrategy(objectChannelBuilder,
                    new ChannelMonitorable() {
                        @Override
                        public void acquired() {
                            numTimesChannelOpened.incrementAndGet();
                        }

                        @Override
                        public void released() {
                            numTimesChannelClosed.incrementAndGet();
                        }
                    });

            TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
                    .withDs3Client(client)
                    .withMasterObjectList(masterObjectList)
                    .withChannelStrategy(channelStrategy);

            transferStrategyBuilder = strategyBuilderModifiable.modify(transferStrategyBuilder);

            final TransferStrategy transferStrategy = transferStrategyBuilder.makePutTransferStrategy();
            transferStrategy.transfer();

            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client);
            final Iterable<Contents> bucketContentsIterable = ds3ClientHelpers.listObjects(BUCKET_NAME);

            for (final Contents bucketContents : bucketContentsIterable) {
                assertEquals(FILE_NAMES[0], bucketContents.getKey());
            }

            assertEquals(1, numTimesChannelOpened.get());
            assertEquals(1, numTimesChannelClosed.get());
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    private interface TransferStrategyBuilderModifiable {
        TransferStrategyBuilder modify(final TransferStrategyBuilder transferStrategyBuilder);
    }

    private interface ChannelMonitorable {
        void acquired();
        void released();
    }

    private class UserSuppliedPutChannelStrategy implements ChannelStrategy {
        private final ChannelMonitorable channelMonitorable;
        private final ChannelStrategy wrappedPutStrategy;


        private UserSuppliedPutChannelStrategy(final Ds3ClientHelpers.ObjectChannelBuilder objectChannelBuilder,
                                               final ChannelMonitorable channelMonitorable)
        {
            this.channelMonitorable = channelMonitorable;
            wrappedPutStrategy = new SequentialFileReaderChannelStrategy(objectChannelBuilder);
        }

        @Override
        public SeekableByteChannel acquireChannelForBlob(final BulkObject blob) throws IOException {
            channelMonitorable.acquired();
            return wrappedPutStrategy.acquireChannelForBlob(blob);
        }

        @Override
        public void releaseChannelForBlob(final SeekableByteChannel seekableByteChannel, final BulkObject blob) throws IOException {
            channelMonitorable.released();
            wrappedPutStrategy.releaseChannelForBlob(seekableByteChannel, blob);
        }
    }

    @Test
    public void testStreamingPutJobWithUserSuppliedChannelStrategy() throws IOException, InterruptedException, URISyntaxException {
        testPutJobWithUserSuppliedChannelStrategy(new TransferStrategyBuilderModifiable() {
            @Override
            public TransferStrategyBuilder modify(final TransferStrategyBuilder transferStrategyBuilder) {
                return transferStrategyBuilder.usingStreamedTransferBehavior();
            }
        });
    }

    @Test
    public void testRandomAccessPutJobWithUserSuppliedChannelStrategy() throws IOException, InterruptedException, URISyntaxException {
        testPutJobWithUserSuppliedChannelStrategy(new TransferStrategyBuilderModifiable() {
            @Override
            public TransferStrategyBuilder modify(final TransferStrategyBuilder transferStrategyBuilder) {
                return transferStrategyBuilder.usingRandomAccessTransferBehavior();
            }
        });
    }

    @Test
    public void testPutJobWithUserSuppliedTransferRetryDecorator() throws IOException, InterruptedException, URISyntaxException {
        final String DIR_NAME = "books/";
        final String[] FILE_NAMES = new String[]{"beowulf.txt"};

        try {
            final Path dirPath = ResourceUtils.loadFileResource(DIR_NAME);

            final List<String> bookTitles = new ArrayList<>();
            final List<Ds3Object> objectsToWrite = new ArrayList<>();
            for (final String book : FILE_NAMES) {
                final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
                final long bookSize = Files.size(objPath);
                final Ds3Object obj = new Ds3Object(book, bookSize);

                bookTitles.add(book);
                objectsToWrite.add(obj);
            }

            final AtomicInteger numTimesTransferCalled = new AtomicInteger(0);

            final PutBulkJobSpectraS3Request request = new PutBulkJobSpectraS3Request(BUCKET_NAME, Lists.newArrayList(objectsToWrite));
            final PutBulkJobSpectraS3Response putBulkJobSpectraS3Response = client.putBulkJobSpectraS3(request);

            final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
                    .withDs3Client(client)
                    .withMasterObjectList(putBulkJobSpectraS3Response.getMasterObjectList())
                    .withChannelBuilder(new FileObjectPutter(dirPath))
                    .withTransferRetryDecorator(new UserSuppliedTransferRetryDecorator(new Monitorable() {
                        @Override
                        public void monitor() {
                            numTimesTransferCalled.getAndIncrement();
                        }
                    }));


            final TransferStrategy transferStrategy = transferStrategyBuilder.makePutTransferStrategy();

            transferStrategy.transfer();

            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client);
            final Iterable<Contents> bucketContentsIterable = ds3ClientHelpers.listObjects(BUCKET_NAME);

            for (final Contents bucketContents : bucketContentsIterable) {
                assertEquals(FILE_NAMES[0], bucketContents.getKey());
            }

            assertEquals(1, numTimesTransferCalled.get());
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    private class UserSuppliedTransferRetryDecorator implements TransferRetryDecorator {
        private final TransferRetryDecorator transferRetryDecorator;
        private final Monitorable monitorable;

        private UserSuppliedTransferRetryDecorator(final Monitorable monitorable) {
            this.transferRetryDecorator = new MaxNumObjectTransferAttemptsDecorator(5);
            this.monitorable = monitorable;
        }

        @Override
        public TransferMethod wrap(final TransferMethod transferMethod) {
            transferRetryDecorator.wrap(transferMethod);
            return this;
        }

        @Override
        public void transferJobPart(final JobPart jobPart) throws IOException {
            monitorable.monitor();
            transferRetryDecorator.transferJobPart(jobPart);
        }
    }

    @Test
    public void testPutJobWithUserSuppliedChunkAttemptRetryBehavior() throws IOException, InterruptedException, URISyntaxException {
        final String DIR_NAME = "books/";
        final String[] FILE_NAMES = new String[]{"beowulf.txt"};

        try {
            final Path dirPath = ResourceUtils.loadFileResource(DIR_NAME);

            final List<String> bookTitles = new ArrayList<>();
            final List<Ds3Object> objectsToWrite = new ArrayList<>();
            for (final String book : FILE_NAMES) {
                final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
                final long bookSize = Files.size(objPath);
                final Ds3Object obj = new Ds3Object(book, bookSize);

                bookTitles.add(book);
                objectsToWrite.add(obj);
            }

            final PutBulkJobSpectraS3Request request = new PutBulkJobSpectraS3Request(BUCKET_NAME, Lists.newArrayList(objectsToWrite));
            final PutBulkJobSpectraS3Response putBulkJobSpectraS3Response = client.putBulkJobSpectraS3(request);

            final AtomicInteger numTimesInvokeCalled = new AtomicInteger(0);
            final AtomicInteger numTimesResetCalled = new AtomicInteger(0);

            final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
                    .withDs3Client(client)
                    .withMasterObjectList(putBulkJobSpectraS3Response.getMasterObjectList())
                    .withChannelBuilder(new FileObjectPutter(dirPath))
                    .withChunkAttemptRetryBehavior(
                            new UserSuppliedChunkAttemptRetryBehavior(new ChunkAttemptRetryBehaviorMonitorable() {
                                @Override
                                public void invoke() {
                                    numTimesInvokeCalled.getAndIncrement();
                                }

                                @Override
                                public void reset() {
                                    numTimesResetCalled.getAndIncrement();
                                }
                            }));

            final TransferStrategy transferStrategy = transferStrategyBuilder.makePutTransferStrategy();

            transferStrategy.transfer();

            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client);
            final Iterable<Contents> bucketContentsIterable = ds3ClientHelpers.listObjects(BUCKET_NAME);

            for (final Contents bucketContents : bucketContentsIterable) {
                assertEquals(FILE_NAMES[0], bucketContents.getKey());
            }

            assertEquals(0, numTimesInvokeCalled.get());
            assertEquals(1, numTimesResetCalled.get());
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    private class UserSuppliedChunkAttemptRetryBehavior implements ChunkAttemptRetryBehavior {
        private final ChunkAttemptRetryBehaviorMonitorable chunkAttemptRetryBehaviorMonitorable;
        private final ChunkAttemptRetryBehavior wrappedChunkAttemptRetryBehavior;

        private UserSuppliedChunkAttemptRetryBehavior(final ChunkAttemptRetryBehaviorMonitorable chunkAttemptRetryBehaviorMonitorable) {
            this.chunkAttemptRetryBehaviorMonitorable = chunkAttemptRetryBehaviorMonitorable;
            wrappedChunkAttemptRetryBehavior = new MaxChunkAttemptsRetryBehavior(5);
        }

        @Override
        public void invoke() throws IOException {
            chunkAttemptRetryBehaviorMonitorable.invoke();
            wrappedChunkAttemptRetryBehavior.invoke();
        }

        @Override
        public void reset() {
            chunkAttemptRetryBehaviorMonitorable.reset();
            wrappedChunkAttemptRetryBehavior.reset();
        }
    }

    private interface ChunkAttemptRetryBehaviorMonitorable {
        void invoke();
        void reset();
    }

    @Test
    public void testPutJobUsingStreamedTransferStrategy() throws IOException, URISyntaxException {
        final String DIR_NAME = "books/";
        final String[] FILE_NAMES = new String[]{"beowulf.txt"};

        try {
            final Path dirPath = ResourceUtils.loadFileResource(DIR_NAME);

            final List<String> bookTitles = new ArrayList<>();
            final List<Ds3Object> objectsToWrite = new ArrayList<>();
            for (final String book : FILE_NAMES) {
                final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
                final long bookSize = Files.size(objPath);
                final Ds3Object obj = new Ds3Object(book, bookSize);

                bookTitles.add(book);
                objectsToWrite.add(obj);
            }

            final Ds3ClientHelpers.Job writeJob = HELPERS.startWriteJobUsingStreamedBehavior(BUCKET_NAME, objectsToWrite);
            writeJob.transfer(new FileObjectPutter(dirPath));

            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client);
            final Iterable<Contents> bucketContentsIterable = ds3ClientHelpers.listObjects(BUCKET_NAME);

            for (final Contents bucketContents : bucketContentsIterable) {
                assertEquals(FILE_NAMES[0], bucketContents.getKey());
            }
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void testPutJobUsingRandomAccessTransferStrategy() throws IOException, URISyntaxException {
        final String DIR_NAME = "books/";
        final String[] FILE_NAMES = new String[]{"beowulf.txt"};

        try {
            final Path dirPath = ResourceUtils.loadFileResource(DIR_NAME);

            final List<String> bookTitles = new ArrayList<>();
            final List<Ds3Object> objectsToWrite = new ArrayList<>();
            for (final String book : FILE_NAMES) {
                final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
                final long bookSize = Files.size(objPath);
                final Ds3Object obj = new Ds3Object(book, bookSize);

                bookTitles.add(book);
                objectsToWrite.add(obj);
            }

            final Ds3ClientHelpers.Job writeJob = HELPERS.startWriteJobUsingRandomAccessBehavior(BUCKET_NAME, objectsToWrite);
            writeJob.transfer(new FileObjectPutter(dirPath));

            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client);
            final Iterable<Contents> bucketContentsIterable = ds3ClientHelpers.listObjects(BUCKET_NAME);

            for (final Contents bucketContents : bucketContentsIterable) {
                assertEquals(FILE_NAMES[0], bucketContents.getKey());
            }
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void testThatMetadataAccessDoesNotTerminateTransfer() throws IOException, URISyntaxException, InterruptedException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String fileName = "Gracie.txt";

        final Path filePath = Paths.get(tempDirectory.toString(), fileName);

        final List<Ds3Object> ds3Objects = new ArrayList<>();
        ds3Objects.add(new Ds3Object(fileName));

        try {
            if ( ! Platform.isWindows()) {
                tempDirectory.toFile().setExecutable(false);
            } else {
                Runtime.getRuntime().exec("icacls " + tempDirectory.toString() + "/deny Everyone(RD)").waitFor();
            }

            final int maxNumBlockAllocationRetries = 3;
            final int maxNumObjectTransferAttempts = 3;

            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client,
                    maxNumBlockAllocationRetries,
                    maxNumObjectTransferAttempts);

            final AtomicInteger numTimesFailureHandlerCalled = new AtomicInteger(0);

            final Ds3ClientHelpers.Job writeJob = ds3ClientHelpers.startWriteJob(BUCKET_NAME, ds3Objects);
            writeJob.withMetadata(new MetadataAccessImpl(ImmutableMap.<String, Path>builder().put(fileName, filePath).build(),
                    new FailureEventListener() {
                        @Override
                        public void onFailure(final FailureEvent failureEvent) {
                            numTimesFailureHandlerCalled.incrementAndGet();
                            assertEquals(FailureEvent.FailureActivity.RecordingMetadata, failureEvent.doingWhat());
                            assertEquals(client.getConnectionDetails().getEndpoint(), failureEvent.usingSystemWithEndpoint());
                        }
                    },
                    client.getConnectionDetails().getEndpoint()));
            writeJob.transfer(new FileObjectGetter(Paths.get(".")));

            assertEquals(1, numTimesFailureHandlerCalled.get());
        } finally {
            if ( ! Platform.isWindows()) {
                tempDirectory.toFile().setExecutable(true);
            } else {
                Runtime.getRuntime().exec("icacls " + tempDirectory.toString() + "/grant Everyone(RD)").waitFor();
            }

            deleteAllContents(client, BUCKET_NAME);
            FileUtils.deleteDirectory(tempDirectory.toFile());
            Files.delete(Paths.get(fileName));
        }
    }

    @Test
    public void testPutting15000Files() throws IOException, URISyntaxException {
        final String tempPathPrefix = null;
        Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final String sourceDirectory = "little_files/";
            final String sourceFileName = "tape.png";

            final Path sourceFilePath = ResourceUtils.loadFileResource(sourceDirectory + sourceFileName);

            Files.copy(sourceFilePath, Paths.get(tempDirectory.toString(), sourceFileName));

            final List<String> fileNames = new ArrayList<>(15001);
            fileNames.add(sourceFileName);

            for (int i = 1; i <= 15000; ++i) {
                final String destinationFileName = "tape" + i + ".png";
                fileNames.add(destinationFileName);
                Files.copy(sourceFilePath, Paths.get(tempDirectory.toString(), destinationFileName));
            }

            final List<Ds3Object> ds3Objects = new ArrayList<>();

            for (final String fileName : fileNames) {
                final Path filePath = Paths.get(tempDirectory.toString(), fileName);
                final long fileSize = Files.size(filePath);
                final Ds3Object ds3Object = new Ds3Object(fileName, fileSize);

                ds3Objects.add(ds3Object);
            }

            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client);
            final Ds3ClientHelpers.Job writeJob = ds3ClientHelpers.startWriteJob(BUCKET_NAME, ds3Objects);

            writeJob.transfer(new FileObjectPutter(tempDirectory));

            FileUtils.deleteDirectory(tempDirectory.toFile());

            tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

            final Ds3ClientHelpers.Job readJob = ds3ClientHelpers.startReadJob(BUCKET_NAME, Lists.newArrayList(
                    new Ds3Object(sourceFileName, Files.size(sourceFilePath))));

            final GetJobSpectraS3Response jobSpectraS3Response = client.getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

            assertThat(jobSpectraS3Response.getMasterObjectListResult(), is(notNullValue()));

            readJob.transfer(new FileObjectGetter(tempDirectory));

            final File originalFile = ResourceUtils.loadFileResource(sourceDirectory + sourceFileName).toFile();
            final File fileCopiedFromBP = Paths.get(tempDirectory.toString(), sourceFileName).toFile();
            assertTrue(FileUtils.contentEquals(originalFile, fileCopiedFromBP));
        } catch (final org.apache.http.client.ClientProtocolException e) {
            fail("This test makes sure that we don't run out of connections when transferring lots of small files.  Oops");
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    @Test
    public void testThatFifoIsNotProcessed() throws IOException, InterruptedException {
        Assume.assumeFalse(Platform.isWindows());

        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String A_FILE_NAME = "aFile.txt";
        final String C_FILE_NAME = "cFile.txt";
        final String FIFO_NAME = "bFifo";

        final AtomicBoolean caughtException = new AtomicBoolean(false);

        try {
            Files.createFile(Paths.get(tempDirectory.toString(), A_FILE_NAME));
            Runtime.getRuntime().exec("mkfifo " + Paths.get(tempDirectory.toString(), FIFO_NAME)).waitFor();
            Files.createFile(Paths.get(tempDirectory.toString(), C_FILE_NAME));

            final List<Ds3Object> ds3Objects = Arrays.asList(new Ds3Object(A_FILE_NAME),
                    new Ds3Object(FIFO_NAME),
                    new Ds3Object(C_FILE_NAME));

            final Ds3ClientHelpers.Job writeJob = HELPERS.startWriteJob(BUCKET_NAME, ds3Objects);
            writeJob.transfer(new FileObjectPutter(tempDirectory));
        } catch (final UnrecoverableIOException e) {
            assertTrue(e.getMessage().contains(FIFO_NAME));
            caughtException.set(true);
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
            deleteAllContents(client, BUCKET_NAME);
        }

        assertTrue(caughtException.get());
    }

    @Test
    public void testThatNonExistentFileDoesNotStopPutJob() throws IOException {
        final String tempPathPrefix = null;
        final Path tempDirectoryForFilesToPut = Files.createTempDirectory(Paths.get("."), tempPathPrefix);
        final Path tempDirectoryForFilesToGet = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String A_FILE_NAME = "aFile.txt";
        final String B_FILE_NAME = "bFile.txt";
        final String C_FILE_NAME = "cFile.txt";

        final Path aFilePutPath = Files.createFile(Paths.get(tempDirectoryForFilesToPut.toString(), A_FILE_NAME));
        final Path cFilePutPath = Files.createFile(Paths.get(tempDirectoryForFilesToPut.toString(), C_FILE_NAME));

        final AtomicBoolean caughtNoSuchFileException = new AtomicBoolean(false);
        final AtomicBoolean getJobRan = new AtomicBoolean(false);

        try {
            FileUtils.writeStringToFile(aFilePutPath.toFile(), "aFile");
            FileUtils.writeStringToFile(cFilePutPath.toFile(), "cFile");

            final int sizeOfNonExistentFile = 5;

            final List<Ds3Object> ds3Objects = Arrays.asList(new Ds3Object(A_FILE_NAME, Files.size(aFilePutPath)),
                    new Ds3Object(B_FILE_NAME, sizeOfNonExistentFile),
                    new Ds3Object(C_FILE_NAME, Files.size(cFilePutPath)));

            final Ds3ClientHelpers.Job writeJob = HELPERS.startWriteJob(BUCKET_NAME, ds3Objects);
            writeJob.transfer(new FileObjectPutter(tempDirectoryForFilesToPut));
        } catch (final NoSuchFileException e) {
            caughtNoSuchFileException.set(true);

            final Path aFileGetPath = Files.createFile(Paths.get(tempDirectoryForFilesToGet.toString(), A_FILE_NAME));
            final Path cFileGetPath = Files.createFile(Paths.get(tempDirectoryForFilesToGet.toString(), C_FILE_NAME));

            final List<Ds3Object> ds3Objects = Arrays.asList(new Ds3Object(A_FILE_NAME), new Ds3Object(C_FILE_NAME));

            final Ds3ClientHelpers.Job readJob = HELPERS.startReadJob(BUCKET_NAME, ds3Objects);
            readJob.transfer(new FileObjectGetter(tempDirectoryForFilesToGet));

            getJobRan.set(true);

            assertTrue(FileUtils.contentEquals(aFileGetPath.toFile(), aFilePutPath.toFile()));
            assertTrue(FileUtils.contentEquals(cFileGetPath.toFile(), cFilePutPath.toFile()));
        } finally {
            FileUtils.deleteDirectory(tempDirectoryForFilesToGet.toFile());
            FileUtils.deleteDirectory(tempDirectoryForFilesToPut.toFile());
            deleteAllContents(client, BUCKET_NAME);
        }

        assertTrue(caughtNoSuchFileException.get());
        assertTrue(getJobRan.get());
    }
}
