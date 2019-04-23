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

package com.spectralogic.ds3client.integration;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.PutBulkJobSpectraS3Request;
import com.spectralogic.ds3client.helpers.ChecksumFunction;
import com.spectralogic.ds3client.helpers.ChecksumListener;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FileObjectGetter;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DataIntegrity_Test {

    private static final Ds3Client client = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(client);
    private static final String TEST_ENV_NAME = "data_integrity_test";
    private static TempStorageIds envStorageIds;
    private static UUID envDataPolicyId;

    @BeforeClass
    public static void startup() throws IOException {
        envDataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, false, ChecksumType.Type.MD5, client);
        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, envDataPolicyId, client);
    }

    @AfterClass
    public static void teardown() throws IOException {
        TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, client);
        client.close();
    }

    @Test
    public void singleFilePut() throws IOException, URISyntaxException {
        final String bucketName = "single_file_put_test";
        final String book = "beowulf.txt";

        try {
            HELPERS.ensureBucketExists(bucketName, envDataPolicyId);

            final Path objPath = ResourceUtils.loadFileResource(Util.RESOURCE_BASE_NAME + book);
            final String digest = DigestUtils.sha256Hex(Files.newInputStream(objPath));
            final Ds3Object obj = new Ds3Object(book, Files.size(objPath));

            final Ds3ClientHelpers.Job putJob = HELPERS.startWriteJob(bucketName, Lists.newArrayList(obj));
            putJob.transfer(new ResourceObjectPutter(Util.RESOURCE_BASE_NAME));

            final Path tempDir = Files.createTempDirectory("ds3_test_");

            final Ds3ClientHelpers.Job getJob = HELPERS.startReadAllJob(bucketName);
            getJob.transfer(new FileObjectGetter(tempDir));

            final String secondDigest = DigestUtils.sha256Hex(Files.newInputStream(tempDir.resolve(book)));
            assertThat(secondDigest, is(equalTo(digest)));
        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void randomDataFile() throws IOException, URISyntaxException {
        final String bucketName = "random_data_file_test";
        final String randomFileName = "random.txt";
        final long seed = 12345689;
        final int length = 2048;

        sendAndVerifySingleFile(bucketName, randomFileName, seed, length);
    }

    @Test
    public void randomSingleByte() throws IOException {
        final String bucketName = "random_single_byte_test";
        final String randomFileName = "random.txt";
        final long seed = 12345689;
        final int length = 1;

        sendAndVerifySingleFile(bucketName, randomFileName, seed, length);
    }

    @Test
    public void multiBlob() throws IOException {
        final String bucketName = "multi_blob_test";
        final String randomFileName = "random.txt";
        final long seed = 12345689;
        final int length = 2 * PutBulkJobSpectraS3Request.MIN_UPLOAD_SIZE_IN_BYTES;

        sendAndVerifySingleFile(bucketName, randomFileName, seed, length);
    }

    @Test
    public void fullBlobPlusOneByte() throws IOException {
        final String bucketName = "full_blob_plus_one_byte_test";
        final String randomFileName = "random.txt";
        final long seed = 12345;
        final int length = PutBulkJobSpectraS3Request.MIN_UPLOAD_SIZE_IN_BYTES + 1;

        sendAndVerifySingleFile(bucketName, randomFileName, seed, length);
    }

    @Test
    public void threeFullBlobs() throws IOException {
        final String bucketName = "three_full_blobs_test";
        final String randomFileName = "random.txt";
        final long seed = 12345;
        final int length = 3 * PutBulkJobSpectraS3Request.MIN_UPLOAD_SIZE_IN_BYTES;

        sendAndVerifySingleFile(bucketName, randomFileName, seed, length);
    }

    @Test
    public void twoFullBlobsPlusOneByte() throws IOException {
        final String bucketName = "two_full_blobs_plus_one_byte_test";
        final String randomFileName = "random.txt";
        final long seed = 12345;
        final int length = 2 * PutBulkJobSpectraS3Request.MIN_UPLOAD_SIZE_IN_BYTES + 1;

        sendAndVerifySingleFile(bucketName, randomFileName, seed, length);
    }

    @Test
    public void autoChecksumming() throws IOException {
        final String bucketName = "auto_checksumming_test";

        try {
            HELPERS.ensureBucketExists(bucketName, envDataPolicyId);

            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object("beowulf.txt", 294059));

            final Ds3ClientHelpers.Job job = HELPERS.startWriteJob(bucketName, objs, WriteJobOptions.create().withChecksumType(ChecksumType.Type.MD5));

            final SingleChecksumListener listener = new SingleChecksumListener();

            job.attachChecksumListener(listener);
            job.removeChecksumListener(listener);
            job.attachChecksumListener(listener);

            job.transfer(new ResourceObjectPutter("books/"));

            final String checksum = listener.getChecksum();

            assertThat(checksum, is(notNullValue()));
            assertThat(checksum, is("rCu751L6xhB5zyL+soa3fg=="));

        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void callerSuppliedChecksum() throws IOException {
        final String bucketName = "caller_supplied_checksum_test";

        try {
            HELPERS.ensureBucketExists(bucketName, envDataPolicyId);

            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object("beowulf.txt", 294059));
            final Ds3ClientHelpers.Job job = HELPERS.startWriteJob(bucketName, objs, WriteJobOptions.create().withChecksumType(ChecksumType.Type.MD5));

            final AtomicBoolean callbackCalled = new AtomicBoolean(false);

            job.withChecksum(new ChecksumFunction() {
                @Override
                public String compute(final BulkObject obj, final ByteChannel channel) {
                    if (obj.getName().equals("beowulf.txt")) {
                        callbackCalled.set(true);
                        return "rCu751L6xhB5zyL+soa3fg==";
                    }
                    return null;
                }
            });

            final SingleChecksumListener listener = new SingleChecksumListener();

            job.attachChecksumListener(listener);

            job.transfer(new ResourceObjectPutter("books/"));

            final String checksum = listener.getChecksum();

            assertThat(checksum, is(notNullValue()));
            assertThat(checksum, is("rCu751L6xhB5zyL+soa3fg=="));
            assertTrue(callbackCalled.get());

        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void getChecksum() throws IOException {
        final String bucketName = "get_checksum_test";

        try {
            HELPERS.ensureBucketExists(bucketName, envDataPolicyId);

            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object("beowulf.txt", 294059));
            final Ds3ClientHelpers.Job job = HELPERS.startWriteJob(bucketName, objs, WriteJobOptions.create().withChecksumType(ChecksumType.Type.MD5));

            final SingleChecksumListener listener = new SingleChecksumListener();

            job.attachChecksumListener(listener);

            job.transfer(new ResourceObjectPutter("books/"));

            final String checksum = listener.getChecksum();

            assertThat(checksum, is(notNullValue()));
            assertThat(checksum, is("rCu751L6xhB5zyL+soa3fg=="));

            final Ds3ClientHelpers.Job getJob = HELPERS.startReadJob(bucketName, objs);
            final SingleChecksumListener getListener = new SingleChecksumListener();
            getJob.attachChecksumListener(getListener);

            getJob.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    return new NullChannel();
                }
            });

            final String getChecksum = getListener.getChecksum();
            assertThat(getChecksum, is(notNullValue()));
            assertThat(getChecksum, is(listener.getChecksum()));
        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void getChecksumComputedByBp() throws IOException, URISyntaxException {
        final String bucketName = "get_checksum_computed_by_bp_test";

        try {
            HELPERS.ensureBucketExists(bucketName, envDataPolicyId);
            Util.loadBookTestData(client, bucketName);
            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object("beowulf.txt"));

            final Ds3ClientHelpers.Job getJob = HELPERS.startReadJob(bucketName, objs);
            final SingleChecksumListener getListener = new SingleChecksumListener();
            getJob.attachChecksumListener(getListener);

            getJob.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    return new NullChannel();
                }
            });

            final String getChecksum = getListener.getChecksum();
            assertThat(getChecksum, is(notNullValue()));
            assertThat(getChecksum, is("rCu751L6xhB5zyL+soa3fg=="));

        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void getMultiFileChecksum() throws IOException, URISyntaxException {
        final String bucketName = "get_multi_file_checksum_test";

        try {
            HELPERS.ensureBucketExists(bucketName, envDataPolicyId);

            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object("beowulf.txt", 294059), new Ds3Object("ulysses.txt", 1540095));
            final Ds3ClientHelpers.Job job = HELPERS.startWriteJob(bucketName, objs, WriteJobOptions.create().withChecksumType(ChecksumType.Type.MD5));

            final MultiFileChecksumListener putListener = new MultiFileChecksumListener();
            job.attachChecksumListener(putListener);

            job.transfer(new ResourceObjectPutter("books/"));

            final Ds3ClientHelpers.Job getJob = HELPERS.startReadJob(bucketName, objs);
            final MultiFileChecksumListener getListener = new MultiFileChecksumListener();
            getJob.attachChecksumListener(getListener);

            getJob.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    return new NullChannel();
                }
            });

            String getChecksum = getListener.getChecksum("beowulf.txt");
            assertThat(getChecksum, is(notNullValue()));
            assertThat(getChecksum, is(putListener.getChecksum("beowulf.txt")));

            getChecksum = getListener.getChecksum("ulysses.txt");
            assertThat(getChecksum, is(notNullValue()));
            assertThat(getChecksum, is(putListener.getChecksum("ulysses.txt")));

        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void setAndGetMultiFileChecksum() throws IOException, URISyntaxException {
        final String bucketName = "set_and_get_multi_file_checksum_test";

        try {
            HELPERS.ensureBucketExists(bucketName, envDataPolicyId);

            Util.loadBookTestData(client, bucketName);
            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object("beowulf.txt"), new Ds3Object("ulysses.txt"));

            final Ds3ClientHelpers.Job getJob = HELPERS.startReadJob(bucketName, objs);
            final MultiFileChecksumListener getListener = new MultiFileChecksumListener();
            getJob.attachChecksumListener(getListener);

            getJob.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    return new NullChannel();
                }
            });

            String getChecksum = getListener.getChecksum("beowulf.txt");
            assertThat(getChecksum, is(notNullValue()));
            assertThat(getChecksum, is("rCu751L6xhB5zyL+soa3fg=="));

            getChecksum = getListener.getChecksum("ulysses.txt");
            assertThat(getChecksum, is(notNullValue()));
            assertThat(getChecksum, is("tdNMk41OHvMAAMjJXyYOjg=="));

        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    private class MultiFileChecksumListener implements ChecksumListener {

        private final Map<String, String> checksumMap;

        public MultiFileChecksumListener() {
            this.checksumMap = new HashMap<>();
        }

        @Override
        public synchronized void value(final BulkObject obj, final ChecksumType.Type type, final String checksum) {
            checksumMap.put(obj.getName(), checksum);
        }

        public String getChecksum(final String filename) {
            return checksumMap.get(filename);
        }
    }

    private class SingleChecksumListener implements ChecksumListener {

        public String getChecksum() {
            return checksum;
        }

        private String checksum = null;

        @Override
        public void value(final BulkObject obj, final ChecksumType.Type type, final String checksum) {
            this.checksum = checksum;
        }
    }

    public void sendAndVerifySingleFile(final String bucketName, final String fileName, final long seed, final int length) throws IOException {
        try {
            HELPERS.ensureBucketExists(bucketName, envDataPolicyId);

            final String digest = DigestUtils.sha256Hex(new RandomDataInputStream(seed, length));
            final Ds3Object obj = new Ds3Object(fileName, length);

            final Ds3ClientHelpers.Job putJob = HELPERS.startWriteJob(bucketName, Lists.newArrayList(obj),
                    WriteJobOptions.create().withMaxUploadSize(PutBulkJobSpectraS3Request.MIN_UPLOAD_SIZE_IN_BYTES));
            putJob.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {

                    final byte[] randomData = IOUtils.toByteArray(new RandomDataInputStream(seed, length));
                    final ByteBuffer randomBuffer = ByteBuffer.wrap(randomData);

                    final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(length);
                    channel.write(randomBuffer);

                    return channel;
                }
            });

            final Path tempDir = Files.createTempDirectory("ds3_test_");

            final Ds3ClientHelpers.Job getJob = HELPERS.startReadAllJob(bucketName);
            getJob.transfer(new FileObjectGetter(tempDir));

            final String secondDigest = DigestUtils.sha256Hex(Files.newInputStream(tempDir.resolve(fileName)));
            assertThat(secondDigest, is(equalTo(digest)));
        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }
}
