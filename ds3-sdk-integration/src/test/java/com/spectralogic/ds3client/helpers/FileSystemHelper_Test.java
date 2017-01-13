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

package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.helpers.events.SameThreadEventRunner;
import com.spectralogic.ds3client.integration.Util;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.spectralogic.ds3client.integration.Util.deleteAllContents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class FileSystemHelper_Test {
    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String BUCKET_NAME = "File_System_Helper_Test";
    private static final String TEST_ENV_NAME = "FileSystem_Helper_Test";
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

    @Test
    public void testObjectsFitBucketThatHasContent() throws IOException, URISyntaxException {
        putObjectThenRunVerification(new FileSystemHelperImpl(), new ResultVerifier() {
            @Override
            public void verifyResult(final ObjectStorageSpaceVerificationResult result, final long totalRequiredSize) {
                assertEquals(ObjectStorageSpaceVerificationResult.VerificationStatus.OK, result.getVerificationStatus());
                assertEquals(result.getRequiredSpace(), totalRequiredSize);
                assertTrue(result.getAvailableSpace() > 0);
                assertTrue(result.containsSufficientSpace());
                assertNull(result.getIoException());
            }
        });
    }

    @Test
    public void testObjectsFitBucketWithNonExistentBucket() {
        final int maxNumBlockAllocationRetries = 1;
        final int maxNumObjectTransferAttempts = 1;
        final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client,
                maxNumBlockAllocationRetries,
                maxNumObjectTransferAttempts);

        final ObjectStorageSpaceVerificationResult result = ds3ClientHelpers.objectsFromBucketWillFitInDirectory(
                "bad bucket name", Arrays.asList(new String[] {}), Paths.get("."));

        assertEquals(ObjectStorageSpaceVerificationResult.VerificationStatus.BucketDoesNotExist, result.getVerificationStatus());
        assertEquals(0, result.getRequiredSpace());
        assertEquals(0, result.getAvailableSpace());
        assertFalse(result.containsSufficientSpace());
        assertNull(result.getIoException());
    }

    @Test
    public void testObjectsFitBucketWithPathNotDirectory() throws IOException {
        final int maxNumBlockAllocationRetries = 1;
        final int maxNumObjectTransferAttempts = 1;
        final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client,
                maxNumBlockAllocationRetries,
                maxNumObjectTransferAttempts);

        final Path textFile = Files.createFile(Paths.get("Gracie.txt"));

        try {
            final ObjectStorageSpaceVerificationResult result = ds3ClientHelpers.objectsFromBucketWillFitInDirectory(
                    "bad bucket name", Arrays.asList(new String[]{}), textFile);

            assertEquals(ObjectStorageSpaceVerificationResult.VerificationStatus.PathIsNotADirectory, result.getVerificationStatus());
            assertEquals(0, result.getRequiredSpace());
            assertEquals(0, result.getAvailableSpace());
            assertFalse(result.containsSufficientSpace());
            assertNull(result.getIoException());
        } finally {
            Files.delete(textFile);
        }
    }

    @Test
    public void testObjectsFitBucketPathDoesNotExist() throws IOException {
        final int maxNumBlockAllocationRetries = 1;
        final int maxNumObjectTransferAttempts = 1;
        final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client,
                maxNumBlockAllocationRetries,
                maxNumObjectTransferAttempts);

        final Path directory = Files.createDirectory(Paths.get("dir"));
        FileUtils.deleteDirectory(directory.toFile());

        final ObjectStorageSpaceVerificationResult result = ds3ClientHelpers.objectsFromBucketWillFitInDirectory(
                "bad bucket name", Arrays.asList(new String[]{}), directory);

        assertEquals(ObjectStorageSpaceVerificationResult.VerificationStatus.PathDoesNotExist, result.getVerificationStatus());
        assertEquals(0, result.getRequiredSpace());
        assertEquals(0, result.getAvailableSpace());
        assertFalse(result.containsSufficientSpace());
        assertNull(result.getIoException());
    }

    @Test
    public void testObjectsFitBucketPathLacksAccess() throws IOException, InterruptedException {
        final int maxNumBlockAllocationRetries = 1;
        final int maxNumObjectTransferAttempts = 1;
        final Ds3ClientHelpers ds3ClientHelpers = Ds3ClientHelpers.wrap(client,
                maxNumBlockAllocationRetries,
                maxNumObjectTransferAttempts);

        final Path directory = Files.createDirectory(Paths.get("dir"));
        if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS) {
            // Deny write data access to everyone, making the directory unwritable
            Runtime.getRuntime().exec("icacls dir /deny Everyone:(WD)").waitFor();
        } else {
            directory.toFile().setWritable(false);
        }

        try {
            final ObjectStorageSpaceVerificationResult result = ds3ClientHelpers.objectsFromBucketWillFitInDirectory(
                    "bad bucket name", Arrays.asList(new String[]{}), directory);

            assertEquals(ObjectStorageSpaceVerificationResult.VerificationStatus.PathLacksAccess, result.getVerificationStatus());
            assertEquals(0, result.getRequiredSpace());
            assertEquals(0, result.getAvailableSpace());
            assertFalse(result.containsSufficientSpace());
            assertNull(result.getIoException());
        } finally {
            if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS) {
                // Grant write data access to everyone, making the directory writable, so we can delete it.
                Runtime.getRuntime().exec("icacls dir /grant Everyone:(WD)").waitFor();
            } else {
                directory.toFile().setWritable(true);
            }

            FileUtils.deleteDirectory(directory.toFile());
        }
    }

    @Test
    public void testObjectsFitBucketPathLacksSpace() throws IOException, URISyntaxException {
        putObjectThenRunVerification(new MockedFileSystemHelper(), new ResultVerifier() {
            @Override
            public void verifyResult(final ObjectStorageSpaceVerificationResult result,
                                     final long totalRequiredSize)
            {
                assertEquals(ObjectStorageSpaceVerificationResult.VerificationStatus.PathLacksSufficientStorageSpace, result.getVerificationStatus());
                assertEquals(totalRequiredSize, result.getRequiredSpace());
                assertEquals(-1, result.getAvailableSpace());
                assertFalse(result.containsSufficientSpace());
                assertNull(result.getIoException());
            }
        });
    }

    private interface ResultVerifier {
        void verifyResult(final ObjectStorageSpaceVerificationResult result, final long totalRequiredSize);
    }

    private void putObjectThenRunVerification(final FileSystemHelper fileSystemHelper,
                                              final ResultVerifier resultVerifier)
                                              throws IOException, URISyntaxException
    {
        try {
            final String DIR_NAME = "largeFiles/";
            final String[] FILE_NAMES = new String[]{"lesmis-copies.txt"};

            final Path dirPath = ResourceUtils.loadFileResource(DIR_NAME);

            final AtomicLong totalBookSizes = new AtomicLong(0);

            final List<String> bookTitles = new ArrayList<>();
            final List<Ds3Object> objects = new ArrayList<>();
            for (final String book : FILE_NAMES) {
                final Path objPath = ResourceUtils.loadFileResource(DIR_NAME + book);
                final long bookSize = Files.size(objPath);
                totalBookSizes.getAndAdd(bookSize);
                final Ds3Object obj = new Ds3Object(book, bookSize);

                bookTitles.add(book);
                objects.add(obj);
            }

            final int maxNumBlockAllocationRetries = 1;
            final int maxNumObjectTransferAttempts = 1;
            final int retryDelay = -1;
            final Ds3ClientHelpers ds3ClientHelpers = new Ds3ClientHelpersImpl(client,
                    maxNumBlockAllocationRetries,
                    maxNumObjectTransferAttempts,
                    retryDelay,
                    new SameThreadEventRunner(),
                    fileSystemHelper);

            final AtomicInteger numTimesCallbackCalled = new AtomicInteger(0);

            final Ds3ClientHelpers.Job writeJob = ds3ClientHelpers.startWriteJob(BUCKET_NAME, objects);
            writeJob.attachObjectCompletedListener(new ObjectCompletedListener() {
                @Override
                public void objectCompleted(final String name) {
                    numTimesCallbackCalled.getAndIncrement();

                    final ObjectStorageSpaceVerificationResult result =
                            ds3ClientHelpers.objectsFromBucketWillFitInDirectory(BUCKET_NAME,
                                    Arrays.asList(FILE_NAMES),
                                    Paths.get("."));

                    resultVerifier.verifyResult(result, totalBookSizes.get());
                }
            });

            writeJob.transfer(new FileObjectPutter(dirPath));

            assertEquals(1, numTimesCallbackCalled.get());
        } finally {
            deleteAllContents(client, BUCKET_NAME);
        }
    }

    private static class MockedFileSystemHelper extends FileSystemHelperImpl {
        @Override
        public long getAvailableFileSpace(final Path path) throws IOException {
            return -1L;
        }
    }

    @Test
    public void testObjectsFitBucketPathThrows() throws IOException, URISyntaxException {
        putObjectThenRunVerification(new MockedFileSystemHelperThrows(),
                new ResultVerifier() {
                    @Override
                    public void verifyResult(final ObjectStorageSpaceVerificationResult result,
                                             final long totalRequiredSize)
                    {
                        assertEquals(ObjectStorageSpaceVerificationResult.VerificationStatus.CaughtIOException, result.getVerificationStatus());
                        assertEquals(totalRequiredSize, result.getRequiredSpace());
                        assertEquals(0, result.getAvailableSpace());
                        assertFalse(result.containsSufficientSpace());
                        assertNotNull(result.getIoException());
                    }
                });
    }

    private static class MockedFileSystemHelperThrows extends FileSystemHelperImpl {
        @Override
        public long getAvailableFileSpace(final Path path) throws IOException {
            throw new IOException("IOExceptionAtor");
        }
    }
}
