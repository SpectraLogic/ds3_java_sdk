/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
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
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FailureEventListener;
import com.spectralogic.ds3client.helpers.FileObjectGetter;
import com.spectralogic.ds3client.helpers.FileObjectPutter;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.helpers.options.ReadJobOptions;
import com.spectralogic.ds3client.integration.test.helpers.ABMTestHelper;
import com.spectralogic.ds3client.integration.test.helpers.Ds3ClientShim;
import com.spectralogic.ds3client.integration.test.helpers.Ds3ClientShimFactory;
import com.spectralogic.ds3client.integration.test.helpers.Ds3ClientShimWithFailedChunkAllocation;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.Priority;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.PartialDs3Object;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.spectralogic.ds3client.integration.Util.RESOURCE_BASE_NAME;
import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GetJobManagement_Test {

    private static final Logger LOG = LoggerFactory.getLogger(GetJobManagement_Test.class);

    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String BUCKET_NAME = "Get_Job_Management_Test";
    private static final String TEST_ENV_NAME = "GetJobManagement_Test";
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

        assertThat(getObjectResponse.getStatusCode(), is(200));
    }

    @Test
    public void createReadJob() throws IOException, InterruptedException, URISyntaxException {

        final Ds3ClientHelpers.Job readJob = HELPERS.startReadJob(BUCKET_NAME, Lists.newArrayList(
                new Ds3Object("beowulf.txt", 10)));

        final GetJobSpectraS3Response jobSpectraS3Response = client
                .getJobSpectraS3(new GetJobSpectraS3Request(readJob.getJobId()));

        assertThat(jobSpectraS3Response.getStatusCode(), is(200));
    }

    @Test
    public void createReadJobWithBigFile() throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        putBigFile();

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

            assertThat(jobSpectraS3Response.getStatusCode(), is(200));

            readJob.transfer(new FileObjectGetter(tempDirectory));

            final File originalFile = ResourceUtils.loadFileResource(DIR_NAME + FILE_NAME).toFile();
            final File fileCopiedFromBP = Paths.get(tempDirectory.toString(), FILE_NAME).toFile();
            assertTrue(FileUtils.contentEquals(originalFile, fileCopiedFromBP));

        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
            deleteBigFileFromBlackPearlBucket();
        }
    }

    private void putBigFile() throws IOException, URISyntaxException {
        final String DIR_NAME = "largeFiles/";
        final String[] FILE_NAMES = new String[] { "lesmis-copies.txt" };

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
            if (contents.getKey().equals("lesmis-copies.txt")) {
                client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, contents.getKey()));
            }
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
        putBigFile();

        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        try {
            final List<Ds3Object> filesToGet = new ArrayList<>();

            final String DIR_NAME = "largeFiles/";
            final String FILE_NAME = "lesmis-copies.txt";

            filesToGet.add(new PartialDs3Object(FILE_NAME, Range.byLength(0, 100)));

            filesToGet.add(new PartialDs3Object(FILE_NAME, Range.byLength(100, 100)));

            final Ds3ClientShim ds3ClientShim = new Ds3ClientShim((Ds3ClientImpl) client);

            final int maxNumBlockAllocationRetries = 1;
            final int maxNumObjectTransferAttempts = 3;
            final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(ds3ClientShim,
                    maxNumBlockAllocationRetries,
                    maxNumObjectTransferAttempts);

            final Ds3ClientHelpers.Job job = ds3ClientHelpers.startReadJob(BUCKET_NAME, filesToGet);

            job.transfer(new FileObjectGetter(tempDirectory));

            try (final InputStream originalFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(DIR_NAME + FILE_NAME)) {
                final byte[] first200Bytes = new byte[200];
                int numBytesRead = originalFileStream.read(first200Bytes, 0, 200);

                assertThat(numBytesRead, is(200));

                try (final InputStream fileReadFromBP = Files.newInputStream(Paths.get(tempDirectory.toString(), FILE_NAME))) {
                    final byte[] first200BytesFromBP = new byte[200];

                    numBytesRead = fileReadFromBP.read(first200BytesFromBP, 0, 200);
                    assertThat(numBytesRead, is(200));

                    assertTrue(Arrays.equals(first200Bytes, first200BytesFromBP));
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
        putBigFile();

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
        putBigFile();

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
