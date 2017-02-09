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
import com.spectralogic.ds3client.IntValue;
import com.spectralogic.ds3client.commands.DeleteObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.commands.spectrads3.GetJobSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobSpectraS3Response;
import com.spectralogic.ds3client.helpers.*;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.helpers.options.ReadJobOptions;
import com.spectralogic.ds3client.integration.test.helpers.*;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.Priority;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.PartialDs3Object;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.utils.Platform;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.RESOURCE_BASE_NAME;
import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

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
            putBeowulf();
        } catch (final Exception e) {
            LOG.error("Setting up test environment failed: " + e.getMessage());
        }
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
        putBigFiles();

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

            final GetJobSpectraS3Response jobSpectraS3Response = ds3ClientShim
                    .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

            assertThat(jobSpectraS3Response.getMasterObjectListResult(), is(notNullValue()));

            readJob.transfer(new FileObjectGetter(tempDirectory));

            final File originalFile = ResourceUtils.loadFileResource(DIR_NAME + FILE_NAME).toFile();
            final File fileCopiedFromBP = Paths.get(tempDirectory.toString(), FILE_NAME).toFile();
            assertTrue(FileUtils.contentEquals(originalFile, fileCopiedFromBP));
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
            deleteBigFileFromBlackPearlBucket();
        }
    }

    private void putBigFiles() throws IOException, URISyntaxException {
        final String DIR_NAME = "largeFiles/";
        final String[] FILE_NAMES = new String[] { "lesmis-copies.txt", "GreatExpectations.txt" };

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

    private void deleteBigFileFromBlackPearlBucket() throws IOException {
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        final Iterable<Contents> objects = helpers.listObjects(BUCKET_NAME);
        for (final Contents contents : objects) {
            if (contents.getKey().equals("lesmis-copies.txt") || contents.getKey().equals("GreatExpectations.txt")) {
                client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, contents.getKey()));
            }
        }
    }

    @Test(expected = RuntimeException.class)
    public void testReadRetrybugFixWithUnwritableDirectory() throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InterruptedException {
        putBigFiles();

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
            deleteBigFileFromBlackPearlBucket();
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
        putBigFiles();

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
            deleteBigFileFromBlackPearlBucket();
        }
    }

    @Test
    public void testReadRetryBugWhenDiskIsFull() throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        putBigFiles();

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
            deleteBigFileFromBlackPearlBucket();
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
    public void createReadJobWithNameOption() throws IOException,
            URISyntaxException, InterruptedException {

        final Ds3ClientHelpers.Job readJob = HELPERS.startReadJob(BUCKET_NAME, Lists.newArrayList(
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
    public void testPartialRetriesWithInjectedFailures() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, URISyntaxException {
        putBigFiles();

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
            final IntValue intValue = new IntValue();

            job.attachObjectCompletedListener(new ObjectCompletedListener() {
                int numPartsCompleted = 0;

                @Override
                public void objectCompleted(final String name) {
                    assertEquals(1, ++numPartsCompleted);
                    intValue.increment();
                }
            });

            job.attachDataTransferredListener(new DataTransferredListener() {
                @Override
                public void dataTransferred(final long size) {
                    LOG.info("Data transferred size: {}", size);
                }
            });

            job.transfer(new FileObjectGetter(tempDirectory));

            assertEquals(1, intValue.getValue());

            try (final InputStream originalFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(DIR_NAME + FILE_NAME)) {
                final byte[] first300000Bytes = new byte[300000 - offsetIntoFirstRange];
                originalFileStream.skip(10);
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
            deleteBigFileFromBlackPearlBucket();
        }
    }


    @Test
    public void testFiringFailureHandlerWhenGettingChunks()
            throws URISyntaxException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException
    {
        putBigFiles();

        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final IntValue numFailuresRecorded = new IntValue();

            final FailureEventListener failureEventListener = new FailureEventListener() {
                @Override
                public void onFailure(final FailureEvent failureEvent) {
                    numFailuresRecorded.increment();
                    assertEquals(FailureEvent.FailureActivity.GettingObject, failureEvent.doingWhat());
                }
            };

            final Ds3ClientHelpers.Job readJob = createReadJobWithObjectsReadyToTransfer(Ds3ClientShimFactory.ClientFailureType.ChunkAllocation);

            readJob.attachFailureEventListener(failureEventListener);

            try {
                readJob.transfer(new FileObjectGetter(tempDirectory));
            } catch (final IOException e) {
                assertEquals(1, numFailuresRecorded.getValue());
            }
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
            deleteBigFileFromBlackPearlBucket();
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
        putBigFiles();

        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final IntValue numFailuresRecorded = new IntValue();

            final FailureEventListener failureEventListener = new FailureEventListener() {
                @Override
                public void onFailure(final FailureEvent failureEvent) {
                    numFailuresRecorded.increment();
                    assertEquals(FailureEvent.FailureActivity.GettingObject, failureEvent.doingWhat());
                }
            };

            final Ds3ClientHelpers.Job readJob = createReadJobWithObjectsReadyToTransfer(Ds3ClientShimFactory.ClientFailureType.GetObject);

            readJob.attachFailureEventListener(failureEventListener);

            try {
                readJob.transfer(new FileObjectGetter(tempDirectory));
            } catch (final IOException e) {
                assertEquals(1, numFailuresRecorded.getValue());
            }
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
            deleteBigFileFromBlackPearlBucket();
        }
    }
}
