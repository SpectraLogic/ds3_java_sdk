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
import com.spectralogic.ds3client.commands.BulkPutRequest;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FileObjectGetter;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SignatureException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DataIntegrity_Test {
    private static Ds3Client client;

    @BeforeClass
    public static void startup() {
        client = Util.fromEnv();
    }

    @Test
    public void singleFilePut() throws IOException, URISyntaxException, XmlProcessingException, SignatureException {
        final String bucketName = "java_integration_test";
        final String book = "beowulf.txt";

        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        try {
            helpers.ensureBucketExists(bucketName);

            final File objFile = ResourceUtils.loadFileResource(Util.RESOURCE_BASE_NAME + book);
            final String digest = DigestUtils.sha256Hex(new FileInputStream(objFile));
            final Ds3Object obj = new Ds3Object(book, objFile.length());

            final Ds3ClientHelpers.Job putJob = helpers.startWriteJob(bucketName, Lists.newArrayList(obj));
            putJob.transfer(new ResourceObjectPutter(Util.RESOURCE_BASE_NAME));

            final Path tempDir = Files.createTempDirectory("ds3_test_");

            final Ds3ClientHelpers.Job getJob = helpers.startReadAllJob(bucketName);
            getJob.transfer(new FileObjectGetter(tempDir));

            final String secondDigest = DigestUtils.sha256Hex(Files.newInputStream(tempDir.resolve(book)));
            assertThat(secondDigest, is(equalTo(digest)));
        }
        finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void randomDataFile() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "java_integration_test";
        final String randomFileName = "random.txt";
        final long seed = 12345689;
        final int length = 2048;

       sendAndVerifySingleFile(bucketName, randomFileName, seed, length);
    }

    @Test
    public void randomSingleByte() throws XmlProcessingException, SignatureException, IOException {
        final String bucketName = "java_integration_test";
        final String randomFileName = "random.txt";
        final long seed = 12345689;
        final int length = 1;

        sendAndVerifySingleFile(bucketName, randomFileName, seed, length);
    }

    @Test
    public void multiBlob() throws IOException, SignatureException, XmlProcessingException {
        final String bucketName = "java_integration_test";
        final String randomFileName = "random.txt";
        final long seed = 12345689;
        final int length = 2 * BulkPutRequest.MIN_UPLOAD_SIZE_IN_BYTES;

        sendAndVerifySingleFile(bucketName, randomFileName, seed, length);
    }

    @Test
    public void fullBlobPlusOneByte() throws XmlProcessingException, SignatureException, IOException {
        final String bucketName = "java_integration_test";
        final String randomFileName = "random.txt";
        final long seed = 12345;
        final int length = BulkPutRequest.MIN_UPLOAD_SIZE_IN_BYTES + 1;

        sendAndVerifySingleFile(bucketName, randomFileName, seed, length);
    }

    @Test
    public void threeFullBlobs() throws XmlProcessingException, SignatureException, IOException {
        final String bucketName = "java_integration_test";
        final String randomFileName = "random.txt";
        final long seed = 12345;
        final int length = 3 * BulkPutRequest.MIN_UPLOAD_SIZE_IN_BYTES;

        sendAndVerifySingleFile(bucketName, randomFileName, seed, length);
    }

    @Test
    public void twoFullBlobsPlusOneByte() throws XmlProcessingException, SignatureException, IOException {
        final String bucketName = "java_integration_test";
        final String randomFileName = "random.txt";
        final long seed = 12345;
        final int length = 2 * BulkPutRequest.MIN_UPLOAD_SIZE_IN_BYTES + 1;

        sendAndVerifySingleFile(bucketName, randomFileName, seed, length);
    }

    public void sendAndVerifySingleFile(final String bucketName, final String fileName, final long seed, final int length) throws IOException, SignatureException, XmlProcessingException {
        try {
            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);
            helpers.ensureBucketExists(bucketName);

            final String digest = DigestUtils.sha256Hex(new RandomDataInputStream(seed, length));
            final Ds3Object obj = new Ds3Object(fileName, length);

            final Ds3ClientHelpers.Job putJob = helpers.startWriteJob(bucketName, Lists.newArrayList(obj),
                    WriteJobOptions.create().withMaxUploadSize(BulkPutRequest.MIN_UPLOAD_SIZE_IN_BYTES));
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

            final Ds3ClientHelpers.Job getJob = helpers.startReadAllJob(bucketName);
            getJob.transfer(new FileObjectGetter(tempDir));

            final String secondDigest = DigestUtils.sha256Hex(Files.newInputStream(tempDir.resolve(fileName)));
            assertThat(secondDigest, is(equalTo(digest)));
        }
        finally {
            Util.deleteAllContents(client, bucketName);
        }
    }
}
