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

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientImpl;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.commands.spectrads3.GetBulkJobSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetBulkJobSpectraS3Response;
import com.spectralogic.ds3client.commands.spectrads3.GetJobSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobSpectraS3Response;
import com.spectralogic.ds3client.helpers.ChecksumListener;
import com.spectralogic.ds3client.helpers.DataTransferredListener;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FailureEventListener;
import com.spectralogic.ds3client.helpers.FileObjectGetter;
import com.spectralogic.ds3client.helpers.FileObjectPutter;

import com.spectralogic.ds3client.helpers.JobPart;
import com.spectralogic.ds3client.helpers.MetadataReceivedListener;
import com.spectralogic.ds3client.helpers.ObjectCompletedListener;

import com.spectralogic.ds3client.helpers.UnrecoverableIOException;
import com.spectralogic.ds3client.helpers.WaitingForChunksListener;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.helpers.events.SameThreadEventRunner;
import com.spectralogic.ds3client.helpers.options.ReadJobOptions;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.BlobStrategy;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.ChunkAttemptRetryBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.ChunkAttemptRetryDelayBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.ClientDefinedChunkAttemptRetryDelayBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.GetSequentialBlobStrategy;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.MaxChunkAttemptsRetryBehavior;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.ChannelStrategy;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.SequentialFileWriterChannelStrategy;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcher;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcherImpl;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.MaxNumObjectTransferAttemptsDecorator;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferMethod;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferRetryDecorator;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategy;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategyBuilder;
import com.spectralogic.ds3client.helpers.util.PartialObjectHelpers;
import com.spectralogic.ds3client.integration.test.helpers.ABMTestHelper;
import com.spectralogic.ds3client.integration.test.helpers.Ds3ClientShim;
import com.spectralogic.ds3client.integration.test.helpers.Ds3ClientShimFactory;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Priority;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.PartialDs3Object;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.Platform;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.spectralogic.ds3client.integration.Util.RESOURCE_BASE_NAME;
import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static com.spectralogic.ds3client.integration.Util.deleteBucketContents;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GetJobManagement_Test {

    private static final Logger LOG = LoggerFactory.getLogger(GetJobManagement_Test.class);

    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String BUCKET_NAME = "Get_Job_Management_Test";
    private static final String TEST_ENV_NAME = "GetJobManagement_Test";
    private static final String DISK_FULL_MESSAGE = "There is not enough space on the disk";
    private static TempStorageIds envStorageIds;
    private static UUID dataPolicyId;

    @BeforeClass
    public static void startup() throws Exception {
        dataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, false, ChecksumType.Type.MD5, client);
        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, dataPolicyId, client);
        setupBucket(dataPolicyId);
    }

    @AfterClass
    public static void teardown() throws IOException {
        try {
            deleteAllContents(client, BUCKET_NAME);
        } finally {
            TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, client);
            client.close();
        }
    }

    /**
     * Creates the test bucket with the specified data policy to prevent cascading test failure
     * when there are multiple data policies
     */
    private static void setupBucket(final UUID dataPolicy) {
        try {
            HELPERS.ensureBucketExists(BUCKET_NAME, dataPolicy);
        } catch (final Exception e) {
            LOG.error("Setting up test environment failed: " + e.getMessage());
        }
    }

    @Before
    public void setupForEachTest() throws Exception {
        LOG.info("Setting up before test.");
        putBigFiles();
        putBeowulf();
    }

    @After
    public void teardownForEachtest() throws IOException {
        LOG.info("Tearing down after test.");
        deleteBucketContents(client, BUCKET_NAME);
    }

    private static void putBeowulf() throws Exception {
        final String book1 = "beowulf.txt";
        final Path objPath1 = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book1);
        final Ds3Object obj = new Ds3Object(book1, Files.size(objPath1));
        final Ds3ClientHelpers.Job job = HELPERS.startWriteJob(BUCKET_NAME, Lists
                .newArrayList(obj));
        final UUID jobId = job.getJobId();
        final SeekableByteChannel book1Channel = new ResourceObjectPutter(RESOURCE_BASE_NAME).buildChannel(book1);
        client.putObject(new PutObjectRequest(BUCKET_NAME, book1, book1Channel, jobId, 0, Files.size(objPath1)));
        ABMTestHelper.waitForJobCachedSizeToBeMoreThanZero(jobId, client, 20);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void nakedS3Get() throws IOException,
            URISyntaxException, InterruptedException {

        final WritableByteChannel writeChannel = new NullChannel();

        final GetObjectResponse getObjectResponse = client.getObject(
                new GetObjectRequest(BUCKET_NAME, "beowulf.txt", writeChannel));

        assertThat(getObjectResponse, is(notNullValue()));
        assertThat(getObjectResponse.getObjectSize(), is(notNullValue()));
    }

    @Test
    public void createReadJob() throws IOException, InterruptedException, URISyntaxException {

        final Ds3ClientHelpers.Job readJob = HELPERS.startReadJob(BUCKET_NAME, Lists.newArrayList(
                new Ds3Object("beowulf.txt", 10)));

        final GetJobSpectraS3Response jobSpectraS3Response = client
                .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

        assertThat(jobSpectraS3Response.getMasterObjectListResult(), is(notNullValue()));
    }

    @Test
    public void createReadJobWithBigFile() throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final String DIR_NAME = "largeFiles/";
            final String FILE_NAME = "lesmis-copies.txt";

            final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + FILE_NAME);
            final long bookSize = Files.size(objPath);
            final Ds3Object obj = new Ds3Object(FILE_NAME, bookSize);

            final Ds3ClientShim ds3ClientShim = new Ds3ClientShim((Ds3ClientImpl)client);

            final int maxNumBlockAllocationRetries = 1;
            final int maxNumObjectTransferAttempts = 3;
            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(ds3ClientShim,
                    maxNumBlockAllocationRetries,
                    maxNumObjectTransferAttempts);

            final Ds3ClientHelpers.Job readJob = ds3ClientHelpers.startReadJob(BUCKET_NAME, Arrays.asList(obj));

            final AtomicBoolean dataTransferredEventReceived = new AtomicBoolean(false);
            final AtomicBoolean objectCompletedEventReceived = new AtomicBoolean(false);
            final AtomicBoolean checksumEventReceived = new AtomicBoolean(false);
            final AtomicBoolean metadataEventReceived = new AtomicBoolean(false);
            final AtomicBoolean waitingForChunksEventReceived = new AtomicBoolean(false);
            final AtomicBoolean failureEventReceived = new AtomicBoolean(false);

            readJob.attachDataTransferredListener(new DataTransferredListener() {
                @Override
                public void dataTransferred(final long size) {
                    dataTransferredEventReceived.set(true);
                    assertEquals(bookSize, size);
                }
            });
            readJob.attachObjectCompletedListener(new ObjectCompletedListener() {
                @Override
                public void objectCompleted(final String name) {
                    objectCompletedEventReceived.set(true);
                }
            });
            readJob.attachChecksumListener(new ChecksumListener() {
                @Override
                public void value(final BulkObject obj, final ChecksumType.Type type, final String checksum) {
                    checksumEventReceived.set(true);
                    assertEquals("0feqCQBgdtmmgGs9pB/Huw==", checksum);
                }
            });
            readJob.attachMetadataReceivedListener(new MetadataReceivedListener() {
                @Override
                public void metadataReceived(final String filename, final Metadata metadata) {
                    metadataEventReceived.set(true);
                }
            });
            readJob.attachWaitingForChunksListener(new WaitingForChunksListener() {
                @Override
                public void waiting(final int secondsToWait) {
                    waitingForChunksEventReceived.set(true);
                }
            });
            readJob.attachFailureEventListener(new FailureEventListener() {
                @Override
                public void onFailure(final FailureEvent failureEvent) {
                    failureEventReceived.set(true);
                }
            });

            final GetJobSpectraS3Response jobSpectraS3Response = ds3ClientShim
                    .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

            assertThat(jobSpectraS3Response.getMasterObjectListResult(), is(notNullValue()));

            readJob.transfer(new FileObjectGetter(tempDirectory));

            final File originalFile = ResourceUtils.loadFileResource(DIR_NAME + FILE_NAME).toFile();
            final File fileCopiedFromBP = Paths.get(tempDirectory.toString(), FILE_NAME).toFile();
            assertTrue(FileUtils.contentEquals(originalFile, fileCopiedFromBP));

            assertTrue(dataTransferredEventReceived.get());
            assertTrue(objectCompletedEventReceived.get());
            assertTrue(checksumEventReceived.get());
            assertTrue(metadataEventReceived.get());
            assertFalse(waitingForChunksEventReceived.get());
            assertFalse(failureEventReceived.get());
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }

    private static void putBigFiles() throws IOException, URISyntaxException {
        final String DIR_NAME = "largeFiles/";
        final String[] FILE_NAMES = new String[] {"lesmis.txt", "lesmis-copies.txt", "GreatExpectations.txt" };

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
        final int maxNumObjectTransferAttempts = 3;
        final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client,
                maxNumBlockAllocationRetries,
                maxNumObjectTransferAttempts);

        final Ds3ClientHelpers.Job writeJob = ds3ClientHelpers.startWriteJob(BUCKET_NAME, objects);
        writeJob.transfer(new FileObjectPutter(dirPath));
    }

    @Test(expected = AccessDeniedException.class)
    public void testReadRetrybugFixWithUnwritableDirectory() throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InterruptedException {
        final String tempPathPrefix = null;
        final Path tempDirectoryPath = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String tempDirectoryName = tempDirectoryPath.toString();

        if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS) {
            // Deny write data access to everyone, making the directory unwritable.
            Runtime.getRuntime().exec("icacls " + tempDirectoryName + " /deny Everyone:(WD)").waitFor();
        } else {
            Runtime.getRuntime().exec("chmod -w " + tempDirectoryName).waitFor();
        }

        try {
            disableWritePermissionForRoot(tempDirectoryName);

            final String DIR_NAME = "largeFiles/";
            final String FILE_NAME = "lesmis-copies.txt";

            final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + FILE_NAME);
            final long bookSize = Files.size(objPath);
            final Ds3Object obj = new Ds3Object(FILE_NAME, bookSize);

            final Ds3ClientShim ds3ClientShim = new Ds3ClientShim((Ds3ClientImpl)client);

            final int maxNumBlockAllocationRetries = 1;
            final int maxNumObjectTransferAttempts = 3;
            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(ds3ClientShim,
                    maxNumBlockAllocationRetries,
                    maxNumObjectTransferAttempts);

            final Ds3ClientHelpers.Job readJob = ds3ClientHelpers.startReadJob(BUCKET_NAME, Arrays.asList(obj));

            final GetJobSpectraS3Response jobSpectraS3Response = ds3ClientShim
                    .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

            assertThat(jobSpectraS3Response.getMasterObjectListResult(), is(notNullValue()));

            readJob.transfer(new FileObjectGetter(tempDirectoryPath));

            final File originalFile = ResourceUtils.loadFileResource(DIR_NAME + FILE_NAME).toFile();
            final File fileCopiedFromBP = Paths.get(tempDirectoryPath.toString(), FILE_NAME).toFile();
            assertTrue(FileUtils.contentEquals(originalFile, fileCopiedFromBP));
        } finally {
            enableWritePermissionForRoot(tempDirectoryName);

            if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS) {
                // Grant write data access to everyone, so we can delete the directory
                Runtime.getRuntime().exec("icacls " + tempDirectoryName + " /grant Everyone:(WD)").waitFor();
            } else {
                Runtime.getRuntime().exec("chmod +w " + tempDirectoryName).waitFor();
            }
            FileUtils.deleteDirectory(tempDirectoryPath.toFile());
        }
    }

    private void disableWritePermissionForRoot(final String fileOrDirName) {
        try {
            if (iAmRoot()) {
                Runtime.getRuntime().exec("chattr +i " + fileOrDirName).waitFor();
            }
        } catch (final IOException | InterruptedException e) {
            LOG.error("Error setting file immutable: " + fileOrDirName, e);
        }
    }

    private boolean iAmRoot() {
        if ( ! Platform.isLinux()) {
            return false;
        }

        try {
            final Process whoamiProcess = Runtime.getRuntime().exec("id | cut -d \"(\" -f 1 | cut -d \"=\" -f 2");
            whoamiProcess.waitFor();

            try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(whoamiProcess.getInputStream()))) {

                final String userId = bufferedReader.readLine();

                return userId.equals("0");
            }
        } catch (final IOException | InterruptedException e) {
            LOG.error("Error getting user id.", e);
        }

        return false;
    }

    private void enableWritePermissionForRoot(final String fileOrDirName) {
        try {
            if (iAmRoot()) {
                Runtime.getRuntime().exec("chattr -i " + fileOrDirName).waitFor();
            }
        } catch (final IOException | InterruptedException e) {
            LOG.error("Error setting file immutable: " + fileOrDirName, e);
        }
    }

    @Test(expected = AccessControlException.class)
    public void testReadRetrybugWhenChannelThrowsAccessException() throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final String tempPathPrefix = null;
        final Path tempDirectoryPath = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final String DIR_NAME = "largeFiles/";
            final String FILE_NAME = "lesmis-copies.txt";

            final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + FILE_NAME);
            final long bookSize = Files.size(objPath);
            final Ds3Object obj = new Ds3Object(FILE_NAME, bookSize);

            final Ds3ClientShim ds3ClientShim = new Ds3ClientShim((Ds3ClientImpl)client);

            final int maxNumBlockAllocationRetries = 1;
            final int maxNumObjectTransferAttempts = 3;
            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(ds3ClientShim,
                    maxNumBlockAllocationRetries,
                    maxNumObjectTransferAttempts);

            final Ds3ClientHelpers.Job readJob = ds3ClientHelpers.startReadJob(BUCKET_NAME, Arrays.asList(obj));

            final GetJobSpectraS3Response jobSpectraS3Response = ds3ClientShim
                    .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

            assertThat(jobSpectraS3Response.getMasterObjectListResult(), is(notNullValue()));

            readJob.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    throw new AccessControlException(key);
                }
            });
        } finally {
            FileUtils.deleteDirectory(tempDirectoryPath.toFile());
        }
    }

    @Test
    public void testReadRetryBugWhenDiskIsFull() throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final String tempPathPrefix = null;
        final Path tempDirectoryPath = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final String DIR_NAME = "largeFiles/";
            final String FILE_NAME = "lesmis-copies.txt";

            final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + FILE_NAME);
            final long bookSize = Files.size(objPath);
            final Ds3Object obj = new Ds3Object(FILE_NAME, bookSize);

            final Ds3ClientShim ds3ClientShim = new Ds3ClientShim((Ds3ClientImpl)client);

            final int maxNumBlockAllocationRetries = 1;
            final int maxNumObjectTransferAttempts = 3;
            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(ds3ClientShim,
                    maxNumBlockAllocationRetries,
                    maxNumObjectTransferAttempts);

            final Ds3ClientHelpers.Job readJob = ds3ClientHelpers.startReadJob(BUCKET_NAME, Arrays.asList(obj));

            final GetJobSpectraS3Response jobSpectraS3Response = ds3ClientShim
                    .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

            assertThat(jobSpectraS3Response.getMasterObjectListResult(), is(notNullValue()));

            try {
                readJob.transfer(new FailingChannelBuilder());
            } catch (final UnrecoverableIOException e) {
                assertEquals(DISK_FULL_MESSAGE, e.getCause().getMessage());
            }
        } finally {
            FileUtils.deleteDirectory(tempDirectoryPath.toFile());
        }
    }

    private static class FailingChannelBuilder implements Ds3ClientHelpers.ObjectChannelBuilder {
        @Override
        public SeekableByteChannel buildChannel(final String key) throws IOException {
            final String filePath = key;
            final SeekableByteChannel seekableByteChannel = Files.newByteChannel(Paths.get(filePath),
                    StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE,
                    StandardOpenOption.DELETE_ON_CLOSE);
            return new SeekableByteChannelWrapper(seekableByteChannel);
        }
    }

    private static class SeekableByteChannelWrapper implements SeekableByteChannel {
        private final SeekableByteChannel seekableByteChannel;

        private SeekableByteChannelWrapper(final SeekableByteChannel seekableByteChannel) {
            this.seekableByteChannel = seekableByteChannel;
        }

        @Override
        public int read(final ByteBuffer dst) throws IOException {
            return seekableByteChannel.read(dst);
        }

        @Override
        public int write(final ByteBuffer src) throws IOException {
            throw new IOException(DISK_FULL_MESSAGE);
        }

        @Override
        public long position() throws IOException {
            return seekableByteChannel.position();
        }

        @Override
        public SeekableByteChannel position(final long newPosition) throws IOException {
            return seekableByteChannel.position(newPosition);
        }

        @Override
        public long size() throws IOException {
            return seekableByteChannel.size();
        }

        @Override
        public SeekableByteChannel truncate(final long size) throws IOException {
            return seekableByteChannel.truncate(size);
        }

        @Override
        public boolean isOpen() {
            return seekableByteChannel.isOpen();
        }

        @Override
        public void close() throws IOException {
            seekableByteChannel.close();
        }
    }


    @Test
    public void createReadJobWithPriorityOption() throws IOException,
            InterruptedException, URISyntaxException {

        final Ds3ClientHelpers.Job readJob = HELPERS.startReadJob(BUCKET_NAME, Lists.newArrayList(
                new Ds3Object("beowulf.txt", 10)), ReadJobOptions.create().withPriority(Priority.LOW));
        final GetJobSpectraS3Response jobSpectraS3Response = client
                .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

        assertThat(jobSpectraS3Response.getMasterObjectListResult().getPriority(), is(Priority.LOW));
    }

    @Test
    public void testCreatingStreamedReadJobWithPriorityOption() throws IOException,
            InterruptedException, URISyntaxException {

        final Ds3ClientHelpers.Job readJob = HELPERS.startReadJobUsingStreamedBehavior(BUCKET_NAME, Lists.newArrayList(
                new Ds3Object("beowulf.txt", 10)), ReadJobOptions.create().withPriority(Priority.LOW));
        final GetJobSpectraS3Response jobSpectraS3Response = client
                .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

        assertThat(jobSpectraS3Response.getMasterObjectListResult().getPriority(), is(Priority.LOW));
    }

    @Test
    public void createReadJobWithNameOption() throws IOException,
            URISyntaxException, InterruptedException {

        final Ds3ClientHelpers.Job readJob = HELPERS.startReadJob(BUCKET_NAME, Lists.newArrayList(
                new Ds3Object("beowulf.txt", 10)), ReadJobOptions.create().withName("test_job"));
        final GetJobSpectraS3Response jobSpectraS3Response = client
                .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

        assertThat(jobSpectraS3Response.getMasterObjectListResult().getName(), is("test_job"));
    }

    @Test
    public void testCreatingStreamedReadJobWithNameOption() throws IOException,
            URISyntaxException, InterruptedException {

        final Ds3ClientHelpers.Job readJob = HELPERS.startReadJobUsingStreamedBehavior(BUCKET_NAME, Lists.newArrayList(
                new Ds3Object("beowulf.txt", 10)), ReadJobOptions.create().withName("test_job"));
        final GetJobSpectraS3Response jobSpectraS3Response = client
                .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

        assertThat(jobSpectraS3Response.getMasterObjectListResult().getName(), is("test_job"));
    }

    @Test
    public void createReadJobWithNameAndPriorityOptions() throws IOException,
            URISyntaxException, InterruptedException {

        final Ds3ClientHelpers.Job readJob = HELPERS.startReadJob(BUCKET_NAME, Lists.newArrayList(
                new Ds3Object("beowulf.txt", 10)), ReadJobOptions.create()
                .withName("test_job").withPriority(Priority.LOW));
        final GetJobSpectraS3Response jobSpectraS3Response = client
                .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

        assertThat(jobSpectraS3Response.getMasterObjectListResult().getName(), is("test_job"));
        assertThat(jobSpectraS3Response.getMasterObjectListResult().getPriority(), is(Priority.LOW));
    }

    @Test
    public void testCreatingStreamedReadJobWithNameAndPriorityOption() throws IOException,
            URISyntaxException, InterruptedException {

        final Ds3ClientHelpers.Job readJob = HELPERS.startReadJobUsingStreamedBehavior(BUCKET_NAME, Lists.newArrayList(
                new Ds3Object("beowulf.txt", 10)), ReadJobOptions.create()
                .withName("test_job").withPriority(Priority.LOW));
        final GetJobSpectraS3Response jobSpectraS3Response = client
                .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

        assertThat(jobSpectraS3Response.getMasterObjectListResult().getName(), is("test_job"));
        assertThat(jobSpectraS3Response.getMasterObjectListResult().getPriority(), is(Priority.LOW));
    }

    @Test
    public void testPartialRetriesWithInjectedFailures() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, URISyntaxException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final List<Ds3Object> filesToGet = new ArrayList<>();

            final String DIR_NAME = "largeFiles/";
            final String FILE_NAME = "GreatExpectations.txt";

            final int offsetIntoFirstRange = 10;

            filesToGet.add(new PartialDs3Object(FILE_NAME, Range.byLength(200000, 100000)));

            filesToGet.add(new PartialDs3Object(FILE_NAME, Range.byLength(100000, 100000)));

            filesToGet.add(new PartialDs3Object(FILE_NAME, Range.byLength(offsetIntoFirstRange, 100000)));

            final Ds3ClientShim ds3ClientShim = new Ds3ClientShim((Ds3ClientImpl) client);

            final int maxNumBlockAllocationRetries = 1;
            final int maxNumObjectTransferAttempts = 4;
            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(ds3ClientShim,
                    maxNumBlockAllocationRetries,
                    maxNumObjectTransferAttempts);

            final Ds3ClientHelpers.Job job = ds3ClientHelpers.startReadJob(BUCKET_NAME, filesToGet);
            final AtomicInteger intValue = new AtomicInteger();

            job.attachObjectCompletedListener(new ObjectCompletedListener() {
                int numPartsCompleted = 0;

                @Override
                public void objectCompleted(final String name) {
                    assertEquals(1, ++numPartsCompleted);
                    intValue.incrementAndGet();
                }
            });

            job.attachDataTransferredListener(new DataTransferredListener() {
                @Override
                public void dataTransferred(final long size) {
                    LOG.info("Data transferred size: {}", size);
                }
            });

            job.transfer(new FileObjectGetter(tempDirectory));

            assertEquals(1, intValue.get());

            try (final InputStream originalFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(DIR_NAME + FILE_NAME)) {
                final byte[] first300000Bytes = new byte[300000 - offsetIntoFirstRange];
                originalFileStream.skip(offsetIntoFirstRange);
                int numBytesRead = originalFileStream.read(first300000Bytes, 0, 300000 - offsetIntoFirstRange);

                assertThat(numBytesRead, is(300000 -offsetIntoFirstRange ));

                try (final InputStream fileReadFromBP = Files.newInputStream(Paths.get(tempDirectory.toString(), FILE_NAME))) {
                    final byte[] first300000BytesFromBP = new byte[300000 - offsetIntoFirstRange];

                    numBytesRead = fileReadFromBP.read(first300000BytesFromBP, 0, 300000 - offsetIntoFirstRange);
                    assertThat(numBytesRead, is(300000 - offsetIntoFirstRange));

                    assertTrue(Arrays.equals(first300000Bytes, first300000BytesFromBP));
                }
            }
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }


    @Test
    public void testFiringFailureHandlerWhenGettingChunks()
            throws URISyntaxException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException
    {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final AtomicInteger numFailuresRecorded = new AtomicInteger();

            final FailureEventListener failureEventListener = new FailureEventListener() {
                @Override
                public void onFailure(final FailureEvent failureEvent) {
                    numFailuresRecorded.incrementAndGet();
                    assertEquals(FailureEvent.FailureActivity.GettingObject, failureEvent.doingWhat());
                }
            };

            final Ds3ClientHelpers.Job readJob = createReadJobWithObjectsReadyToTransfer(Ds3ClientShimFactory.ClientFailureType.ChunkAllocation);

            readJob.attachFailureEventListener(failureEventListener);

            try {
                readJob.transfer(new FileObjectGetter(tempDirectory));
            } catch (final IOException e) {
                assertEquals(1, numFailuresRecorded.get());
            }
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }

    private Ds3ClientHelpers.Job createReadJobWithObjectsReadyToTransfer(final Ds3ClientShimFactory.ClientFailureType clientFailureType)
            throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        final String DIR_NAME = "largeFiles/";
        final String FILE_NAME = "lesmis-copies.txt";

        final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + FILE_NAME);
        final long bookSize = Files.size(objPath);
        final Ds3Object obj = new Ds3Object(FILE_NAME, bookSize);

        final Ds3Client ds3Client = Ds3ClientShimFactory.makeWrappedDs3Client(clientFailureType, client);

        final int maxNumBlockAllocationRetries = 3;
        final int maxNumObjectTransferAttempts = 3;
        final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(ds3Client,
                maxNumBlockAllocationRetries,
                maxNumObjectTransferAttempts);

        final Ds3ClientHelpers.Job readJob = ds3ClientHelpers.startReadJob(BUCKET_NAME, Arrays.asList(obj));

        return readJob;
    }

    @Test
    public void testFiringFailureHandlerWhenGettingObject()
            throws URISyntaxException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException
    {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final AtomicInteger numFailuresRecorded = new AtomicInteger();

            final FailureEventListener failureEventListener = new FailureEventListener() {
                @Override
                public void onFailure(final FailureEvent failureEvent) {
                    numFailuresRecorded.incrementAndGet();
                    assertEquals(FailureEvent.FailureActivity.GettingObject, failureEvent.doingWhat());
                }
            };

            final Ds3ClientHelpers.Job readJob = createReadJobWithObjectsReadyToTransfer(Ds3ClientShimFactory.ClientFailureType.GetObject);

            readJob.attachFailureEventListener(failureEventListener);

            try {
                readJob.transfer(new FileObjectGetter(tempDirectory));
            } catch (final IOException e) {
                assertEquals(1, numFailuresRecorded.get());
            }
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }

    @Test
    public void testCreatingReadJobWithStreamedBehavior() throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        doReadJobWithJobStarter(new ReadJobStarter() {
            @Override
            public Ds3ClientHelpers.Job startReadJob(final Ds3ClientHelpers ds3ClientHelpers, final String bucketName, final Iterable<Ds3Object> objectsToread)
                throws IOException
            {
                return ds3ClientHelpers.startReadJobUsingStreamedBehavior(BUCKET_NAME, objectsToread);
            }
        });
    }

    private interface ReadJobStarter {
        Ds3ClientHelpers.Job startReadJob(final Ds3ClientHelpers ds3ClientHelpers, final String bucketName, Iterable<Ds3Object> objectsToread) throws IOException;
    }

    private void doReadJobWithJobStarter(final ReadJobStarter readJobStarter) throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final String DIR_NAME = "largeFiles/";
            final String FILE_NAME = "lesmis.txt";

            final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + FILE_NAME);
            final long bookSize = Files.size(objPath);
            final Ds3Object obj = new Ds3Object(FILE_NAME, bookSize);

            final Ds3ClientShim ds3ClientShim = new Ds3ClientShim((Ds3ClientImpl)client);

            final int maxNumBlockAllocationRetries = 1;
            final int maxNumObjectTransferAttempts = 3;
            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(ds3ClientShim,
                    maxNumBlockAllocationRetries,
                    maxNumObjectTransferAttempts);

            final Ds3ClientHelpers.Job readJob = readJobStarter.startReadJob(ds3ClientHelpers, BUCKET_NAME, Arrays.asList(obj));

            final AtomicBoolean dataTransferredEventReceived = new AtomicBoolean(false);
            final AtomicBoolean objectCompletedEventReceived = new AtomicBoolean(false);
            final AtomicBoolean checksumEventReceived = new AtomicBoolean(false);
            final AtomicBoolean metadataEventReceived = new AtomicBoolean(false);
            final AtomicBoolean waitingForChunksEventReceived = new AtomicBoolean(false);
            final AtomicBoolean failureEventReceived = new AtomicBoolean(false);

            readJob.attachDataTransferredListener(new DataTransferredListener() {
                @Override
                public void dataTransferred(final long size) {
                    dataTransferredEventReceived.set(true);
                    assertEquals(bookSize, size);
                }
            });
            readJob.attachObjectCompletedListener(new ObjectCompletedListener() {
                @Override
                public void objectCompleted(final String name) {
                    objectCompletedEventReceived.set(true);
                }
            });
            readJob.attachChecksumListener(new ChecksumListener() {
                @Override
                public void value(final BulkObject obj, final ChecksumType.Type type, final String checksum) {
                    checksumEventReceived.set(true);
                    assertEquals("69+JXWeZuzl2HFTM6Lbo8A==", checksum);
                }
            });
            readJob.attachMetadataReceivedListener(new MetadataReceivedListener() {
                @Override
                public void metadataReceived(final String filename, final Metadata metadata) {
                    metadataEventReceived.set(true);
                }
            });
            readJob.attachWaitingForChunksListener(new WaitingForChunksListener() {
                @Override
                public void waiting(final int secondsToWait) {
                    waitingForChunksEventReceived.set(true);
                }
            });
            readJob.attachFailureEventListener(new FailureEventListener() {
                @Override
                public void onFailure(final FailureEvent failureEvent) {
                    failureEventReceived.set(true);
                }
            });

            readJob.transfer(new FileObjectGetter(tempDirectory));

            final File originalFile = ResourceUtils.loadFileResource(DIR_NAME + FILE_NAME).toFile();
            final File fileCopiedFromBP = Paths.get(tempDirectory.toString(), FILE_NAME).toFile();
            assertTrue(FileUtils.contentEquals(originalFile, fileCopiedFromBP));

            assertTrue(dataTransferredEventReceived.get());
            assertTrue(objectCompletedEventReceived.get());
            assertTrue(checksumEventReceived.get());
            assertTrue(metadataEventReceived.get());
            assertFalse(waitingForChunksEventReceived.get());
            assertFalse(failureEventReceived.get());
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }

    @Test
    public void testCreatingReadJobWithRandomAccessBehavior() throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        doReadJobWithJobStarter(new ReadJobStarter() {
            @Override
            public Ds3ClientHelpers.Job startReadJob(final Ds3ClientHelpers ds3ClientHelpers, final String bucketName, final Iterable<Ds3Object> objectsToread)
                    throws IOException
            {
                return ds3ClientHelpers.startReadJobUsingRandomAccessBehavior(BUCKET_NAME, objectsToread);
            }
        });
    }

    @Test
    public void testStartReadAllJobUsingStreamedBehavior() throws IOException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final AtomicInteger numFailuresRecorded = new AtomicInteger(0);

            final FailureEventListener failureEventListener = new FailureEventListener() {
                @Override
                public void onFailure(final FailureEvent failureEvent) {
                    numFailuresRecorded.incrementAndGet();
                    assertEquals(FailureEvent.FailureActivity.GettingObject, failureEvent.doingWhat());
                }
            };

            final Ds3ClientHelpers.Job readJob = HELPERS.startReadAllJobUsingStreamedBehavior(BUCKET_NAME);
            readJob.attachFailureEventListener(failureEventListener);
            readJob.transfer(new FileObjectGetter(tempDirectory));

            final Collection<File> filesInTempDirectory = FileUtils.listFiles(tempDirectory.toFile(), null, false);

            final List<String> filesWeExpectToBeInTempDirectory = Arrays.asList("beowulf.txt", "lesmis.txt", "lesmis-copies.txt", "GreatExpectations.txt");

            for (final File fileInTempDirectory : filesInTempDirectory) {
                assertTrue(filesWeExpectToBeInTempDirectory.contains(fileInTempDirectory.getName()));
            }

            assertEquals(0, numFailuresRecorded.get());
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }

    @Test
    public void testStartReadAllJobUsingRandomAccessBehavior() throws IOException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final AtomicInteger numFailuresRecorded = new AtomicInteger(0);

            final FailureEventListener failureEventListener = new FailureEventListener() {
                @Override
                public void onFailure(final FailureEvent failureEvent) {
                    numFailuresRecorded.incrementAndGet();
                    assertEquals(FailureEvent.FailureActivity.GettingObject, failureEvent.doingWhat());
                }
            };

            final Ds3ClientHelpers.Job readJob = HELPERS.startReadAllJobUsingRandomAccessBehavior(BUCKET_NAME);
            readJob.attachFailureEventListener(failureEventListener);
            readJob.transfer(new FileObjectGetter(tempDirectory));

            final Collection<File> filesInTempDirectory = FileUtils.listFiles(tempDirectory.toFile(), null, false);

            final List<String> filesWeExpectToBeInTempDirectory = Arrays.asList("beowulf.txt", "lesmis.txt", "lesmis-copies.txt", "GreatExpectations.txt");

            for (final File fileInTempDirectory : filesInTempDirectory) {
                assertTrue(filesWeExpectToBeInTempDirectory.contains(fileInTempDirectory.getName()));
            }

            assertEquals(0, numFailuresRecorded.get());
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }

    @Test
    public void testGetJobUsingTransferStrategy() throws IOException, InterruptedException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);
        final String fileName = "beowulf.txt";

        try {
            final List<Ds3Object> objects = Lists.newArrayList(new Ds3Object(fileName));

            final GetBulkJobSpectraS3Request getBulkJobSpectraS3Request = new GetBulkJobSpectraS3Request(BUCKET_NAME, objects);

            final GetBulkJobSpectraS3Response getBulkJobSpectraS3Response = client.getBulkJobSpectraS3(getBulkJobSpectraS3Request);

            final MasterObjectList masterObjectList = getBulkJobSpectraS3Response.getMasterObjectList();

            final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
                    .withDs3Client(client)
                    .withMasterObjectList(masterObjectList)
                    .withChannelBuilder(new FileObjectGetter(tempDirectory))
                    .withRangesForBlobs(PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(),
                            PartialObjectHelpers.getPartialObjectsRanges(objects)));

            final TransferStrategy transferStrategy = transferStrategyBuilder.makeGetTransferStrategy();

            transferStrategy.transfer();

            final Collection<File> filesInTempDirectory = FileUtils.listFiles(tempDirectory.toFile(), null, false);

            for (final File file : filesInTempDirectory) {
                assertEquals(fileName, file.getName());
            }
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }

    @Test
    public void testGetJobWithUserSuppliedBlobStrategy() throws IOException, InterruptedException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);
        final String fileName = "beowulf.txt";

        try {
            final List<Ds3Object> objects = Lists.newArrayList(new Ds3Object(fileName));

            final GetBulkJobSpectraS3Request getBulkJobSpectraS3Request = new GetBulkJobSpectraS3Request(BUCKET_NAME, objects);

            final GetBulkJobSpectraS3Response getBulkJobSpectraS3Response = client.getBulkJobSpectraS3(getBulkJobSpectraS3Request);

            final MasterObjectList masterObjectList = getBulkJobSpectraS3Response.getMasterObjectList();

            final EventDispatcher eventDispatcher = new EventDispatcherImpl(new SameThreadEventRunner());

            final AtomicInteger numChunkAllocationAttempts = new AtomicInteger(0);

            final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
                    .withDs3Client(client)
                    .withMasterObjectList(masterObjectList)
                    .withChannelBuilder(new FileObjectGetter(tempDirectory))
                    .withRangesForBlobs(PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(),
                            PartialObjectHelpers.getPartialObjectsRanges(objects)))
                    .withBlobStrategy(new UserSuppliedPutBlobStrategy(client,
                            masterObjectList,
                            eventDispatcher,
                            new MaxChunkAttemptsRetryBehavior(5),
                            new ClientDefinedChunkAttemptRetryDelayBehavior(1, eventDispatcher),
                            new Monitorable() {
                                @Override
                                public void monitor() {
                                    numChunkAllocationAttempts.getAndIncrement();
                                }
                            }));

            final TransferStrategy transferStrategy = transferStrategyBuilder.makeGetTransferStrategy();

            transferStrategy.transfer();

            final Collection<File> filesInTempDirectory = FileUtils.listFiles(tempDirectory.toFile(), null, false);

            for (final File file : filesInTempDirectory) {
                assertEquals(fileName, file.getName());
            }

            assertEquals(1, numChunkAllocationAttempts.get());
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
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

            wrappedBlobStrategy = new GetSequentialBlobStrategy(client, masterObjectList, eventDispatcher,
                    retryBehavior, chunkAttemptRetryDelayBehavior);
        }

        @Override
        public Iterable<JobPart> getWork() throws IOException, InterruptedException {
            monitorable.monitor();

            return wrappedBlobStrategy.getWork();
        }
    }

    @Test
    public void testGetJobWithUserSuppliedChannelStrategy() throws IOException, InterruptedException {
        testGetJobWithUserSuppliedChannelStrategy(new TransferStrategyBuilderModifiable() {
            @Override
            public TransferStrategyBuilder modify(final TransferStrategyBuilder transferStrategyBuilder) {
                return transferStrategyBuilder;
            }
        });
    }

    private void testGetJobWithUserSuppliedChannelStrategy(final TransferStrategyBuilderModifiable transferStrategyBuilderModifiable) throws IOException, InterruptedException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);
        final String fileName = "beowulf.txt";

        try {
            final List<Ds3Object> objects = Lists.newArrayList(new Ds3Object(fileName));

            final GetBulkJobSpectraS3Request getBulkJobSpectraS3Request = new GetBulkJobSpectraS3Request(BUCKET_NAME, objects);

            final GetBulkJobSpectraS3Response getBulkJobSpectraS3Response = client.getBulkJobSpectraS3(getBulkJobSpectraS3Request);

            final MasterObjectList masterObjectList = getBulkJobSpectraS3Response.getMasterObjectList();

            final AtomicInteger numTimesChannelOpened = new AtomicInteger(0);
            final AtomicInteger numTimesChannelClosed = new AtomicInteger(0);

            TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
                    .withDs3Client(client)
                    .withMasterObjectList(masterObjectList)
                    .withChannelBuilder(new FileObjectGetter(tempDirectory))
                    .withRangesForBlobs(PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(),
                            PartialObjectHelpers.getPartialObjectsRanges(objects)))
                    .withChannelStrategy(new UserSuppliedPutChannelStrategy(new FileObjectGetter(tempDirectory),
                            new ChannelMonitorable() {
                                @Override
                                public void acquired() {
                                    numTimesChannelOpened.getAndIncrement();
                                }

                                @Override
                                public void released() {
                                    numTimesChannelClosed.getAndIncrement();
                                }
                            }));

            transferStrategyBuilder = transferStrategyBuilderModifiable.modify(transferStrategyBuilder);

            final TransferStrategy transferStrategy = transferStrategyBuilder.makeGetTransferStrategy();

            transferStrategy.transfer();

            final Collection<File> filesInTempDirectory = FileUtils.listFiles(tempDirectory.toFile(), null, false);

            for (final File file : filesInTempDirectory) {
                assertEquals(fileName, file.getName());
            }

            assertEquals(1, numTimesChannelOpened.get());
            assertEquals(1, numTimesChannelClosed.get());
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
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
            wrappedPutStrategy = new SequentialFileWriterChannelStrategy(objectChannelBuilder);
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
    public void testStreamedGetJobWithUserSuppliedChannelStrategy() throws IOException, InterruptedException {
        testGetJobWithUserSuppliedChannelStrategy(new TransferStrategyBuilderModifiable() {
            @Override
            public TransferStrategyBuilder modify(final TransferStrategyBuilder transferStrategyBuilder) {
                return transferStrategyBuilder.usingStreamedTransferBehavior();
            }
        });
    }

    @Test
    public void testRandomAccessGetJobWithUserSuppliedChannelStrategy() throws IOException, InterruptedException {
        testGetJobWithUserSuppliedChannelStrategy(new TransferStrategyBuilderModifiable() {
            @Override
            public TransferStrategyBuilder modify(final TransferStrategyBuilder transferStrategyBuilder) {
                return transferStrategyBuilder.usingRandomAccessTransferBehavior();
            }
        });
    }

    @Test
    public void testGetJobUserSuppliedTransferRetryDecorator() throws IOException, InterruptedException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);
        final String fileName = "beowulf.txt";

        try {
            final List<Ds3Object> objects = Lists.newArrayList(new Ds3Object(fileName));

            final GetBulkJobSpectraS3Request getBulkJobSpectraS3Request = new GetBulkJobSpectraS3Request(BUCKET_NAME, objects);

            final GetBulkJobSpectraS3Response getBulkJobSpectraS3Response = client.getBulkJobSpectraS3(getBulkJobSpectraS3Request);

            final MasterObjectList masterObjectList = getBulkJobSpectraS3Response.getMasterObjectList();

            final AtomicInteger numTimesTransferCalled = new AtomicInteger(0);

            final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
                    .withDs3Client(client)
                    .withMasterObjectList(masterObjectList)
                    .withChannelBuilder(new FileObjectGetter(tempDirectory))
                    .withRangesForBlobs(PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(),
                            PartialObjectHelpers.getPartialObjectsRanges(objects)))
                    .withTransferRetryDecorator(new UserSuppliedTransferRetryDecorator(
                            new Monitorable() {
                                @Override
                                public void monitor() {
                                    numTimesTransferCalled.getAndIncrement();
                                }
                            }
                    ));

            final TransferStrategy transferStrategy = transferStrategyBuilder.makeGetTransferStrategy();

            transferStrategy.transfer();

            final Collection<File> filesInTempDirectory = FileUtils.listFiles(tempDirectory.toFile(), null, false);

            for (final File file : filesInTempDirectory) {
                assertEquals(fileName, file.getName());
            }

            assertEquals(1, numTimesTransferCalled.get());
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
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
    public void testGetJobUserSuppliedChunkAttemptRetryBehavior() throws IOException, InterruptedException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);
        final String fileName = "beowulf.txt";

        try {
            final List<Ds3Object> objects = Lists.newArrayList(new Ds3Object(fileName));

            final GetBulkJobSpectraS3Request getBulkJobSpectraS3Request = new GetBulkJobSpectraS3Request(BUCKET_NAME, objects);

            final GetBulkJobSpectraS3Response getBulkJobSpectraS3Response = client.getBulkJobSpectraS3(getBulkJobSpectraS3Request);

            final MasterObjectList masterObjectList = getBulkJobSpectraS3Response.getMasterObjectList();

            final AtomicInteger numTimesInvokeCalled = new AtomicInteger(0);
            final AtomicInteger numTimesResetCalled = new AtomicInteger(0);

            final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
                    .withDs3Client(client)
                    .withMasterObjectList(masterObjectList)
                    .withChannelBuilder(new FileObjectGetter(tempDirectory))
                    .withRangesForBlobs(PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(),
                            PartialObjectHelpers.getPartialObjectsRanges(objects)))
                    .withChunkAttemptRetryBehavior(new UserSuppliedChunkAttemptRetryBehavior(
                            new ChunkAttemptRetryBehaviorMonitorable() {
                                @Override
                                public void invoke() {
                                    numTimesInvokeCalled.getAndIncrement();
                                }

                                @Override
                                public void reset() {
                                    numTimesResetCalled.getAndIncrement();
                                }
                            }
                    ));

            final TransferStrategy transferStrategy = transferStrategyBuilder.makeGetTransferStrategy();

            transferStrategy.transfer();

            final Collection<File> filesInTempDirectory = FileUtils.listFiles(tempDirectory.toFile(), null, false);

            for (final File file : filesInTempDirectory) {
                assertEquals(fileName, file.getName());
            }

            assertEquals(0, numTimesInvokeCalled.get());
            assertEquals(1, numTimesResetCalled.get());
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
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
    public void testThatFifoIsNotProcessed() throws IOException, InterruptedException {
        Assume.assumeFalse(Platform.isWindows());

        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String BEOWULF_FILE_NAME  = "beowulf.txt";

        final AtomicBoolean caughtException = new AtomicBoolean(false);

        try {
            Runtime.getRuntime().exec("mkfifo " + Paths.get(tempDirectory.toString(), BEOWULF_FILE_NAME)).waitFor();

            final List<Ds3Object> ds3Objects = Arrays.asList(new Ds3Object(BEOWULF_FILE_NAME));

            final Ds3ClientHelpers.Job readJob = HELPERS.startReadJob(BUCKET_NAME, ds3Objects);
            readJob.transfer(new FileObjectPutter(tempDirectory));
        } catch (final UnrecoverableIOException e) {
            assertTrue(e.getMessage().contains(BEOWULF_FILE_NAME));
            caughtException.set(true);
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }

        assertTrue(caughtException.get());
    }
}
