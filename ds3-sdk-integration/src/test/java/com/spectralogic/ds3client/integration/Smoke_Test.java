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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.interfaces.BulkResponse;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.JobRecoveryException;
import com.spectralogic.ds3client.helpers.ObjectCompletedListener;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.PartialDs3Object;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.spectralogic.ds3client.integration.Util.*;
import static com.spectralogic.ds3client.integration.test.helpers.ABMTestHelper.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeThat;

public class Smoke_Test {

    private static final Logger LOG = LoggerFactory.getLogger(Smoke_Test.class);

    private static Ds3Client client;
    private static final String TEST_ENV_NAME = "smoke_test";
    private static TempStorageIds envStorageIds;

    @BeforeClass
    public static void startup() throws IOException, SignatureException {
        client = Util.fromEnv();
        final UUID dataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, false, ChecksumType.Type.MD5, client);
        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, dataPolicyId, client);
    }

    @AfterClass
    public static void teardown() throws IOException, SignatureException {
        TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, client);
        client.close();
    }

    @Test
    public void createBucket() throws IOException, SignatureException {
        final String bucketName = "test_create_bucket";
        client.putBucket(new PutBucketRequest(bucketName));

        HeadBucketResponse response = null;
        try {
            response = client.headBucket(new HeadBucketRequest(bucketName));
            assertThat(response.getStatus(),
                    is(HeadBucketResponse.Status.EXISTS));
        } finally {
            if (response != null) {
                client.deleteBucket(new DeleteBucketRequest(bucketName));
            }
        }
    }

    @Test
    public void deleteBucket() throws IOException, SignatureException {
        final String bucketName = "test_delete_bucket";
        client.putBucket(new PutBucketRequest(bucketName));

        HeadBucketResponse response = client.headBucket(new HeadBucketRequest(
                bucketName));
        assertThat(response.getStatus(), is(HeadBucketResponse.Status.EXISTS));

        client.deleteBucket(new DeleteBucketRequest(bucketName));

        response = client.headBucket(new HeadBucketRequest(bucketName));
        assertThat(response.getStatus(),
                is(HeadBucketResponse.Status.DOESNTEXIST));
    }

    @Test
    public void getObjects() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "test_get_objs";
        try {
            client.putBucket(new PutBucketRequest(bucketName));
            loadBookTestData(client, bucketName);

            final HeadObjectResponse headResponse = client.headObject(new HeadObjectRequest(
                    bucketName, "beowulf.txt"));
            assertThat(headResponse.getStatus(),
                    is(HeadObjectResponse.Status.EXISTS));
            assertThat(headResponse.getObjectSize(), is(294059L));

            final GetObjectsSpectraS3Response response = client
                    .getObjectsSpectraS3(new GetObjectsSpectraS3Request().withBucketId("test_get_objs"));

            assertFalse(response.getS3ObjectListResult().getS3Objects().isEmpty());
            assertThat(response.getS3ObjectListResult().getS3Objects().size(), is(4));
            assertTrue(s3ObjectExists(response.getS3ObjectListResult().getS3Objects(), "beowulf.txt"));

        } finally {
            deleteAllContents(client,bucketName);
        }

    }

    private static boolean s3ObjectExists(final List<S3Object> objects, final String fileName) {
        for (final S3Object obj : objects) {
            if (obj.getName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void deleteFolder() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "test_delete_folder";
        try {
            client.putBucket(new PutBucketRequest(bucketName));
            loadBookTestDataWithPrefix(client, bucketName, "folder/");

            HeadObjectResponse response = client.headObject(new HeadObjectRequest(
                    bucketName, "folder/beowulf.txt"));
            assertThat(response.getStatus(),
                    is(HeadObjectResponse.Status.EXISTS));

            client.deleteFolderRecursivelySpectraS3(
                    new DeleteFolderRecursivelySpectraS3Request(bucketName, "folder"));

            response = client.headObject(new HeadObjectRequest(
                    bucketName, "folder/beowulf.txt"));
            assertThat(response.getStatus(),
                    is(HeadObjectResponse.Status.DOESNTEXIST));
        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void emptyBucket() throws IOException, SignatureException {
        final String bucketName = "test_empty_bucket";

        try {
            client.putBucket(new PutBucketRequest(bucketName));

            final GetBucketResponse request = client
                    .getBucket(new GetBucketRequest(bucketName));
            final ListBucketResult result = request.getListBucketResult();
            assertThat(result.getObjects(), is(notNullValue()));
            assertTrue(result.getObjects().isEmpty());
        } finally {
            client.deleteBucket(new DeleteBucketRequest(bucketName));
        }
    }

    @Test
    public void listContents() throws IOException, SignatureException,
            XmlProcessingException, URISyntaxException {
        final String bucketName = "test_contents_bucket";

        try {
            client.putBucket(new PutBucketRequest(bucketName));
            loadBookTestData(client, bucketName);

            final GetBucketResponse response = client
                    .getBucket(new GetBucketRequest(bucketName));

            final ListBucketResult result = response.getListBucketResult();

            assertFalse(result.getObjects().isEmpty());
            assertThat(result.getObjects().size(), is(4));
        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void getContents() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "test_get_contents";

        try {
            client.putBucket(new PutBucketRequest(bucketName));
            loadBookTestData(client, bucketName);

            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

            final Ds3ClientHelpers.Job job = helpers.startReadAllJob(bucketName);

            final UUID jobId = job.getJobId();

            job.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    final Path filePath = Files.createTempFile("ds3", key);
                    return Files.newByteChannel(filePath, StandardOpenOption.DELETE_ON_CLOSE, StandardOpenOption.WRITE);
                }
            });

            final GetJobSpectraS3Response jobResponse = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(jobId));
            assertThat(jobResponse.getMasterObjectListResult().getStatus(), is(JobStatus.COMPLETED));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void negativeDeleteNonEmptyBucket() throws IOException,
            SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "negative_test_delete_non_empty_bucket";

        try {
            // Create bucket and put objects (4 book .txt files) to it
            client.putBucket(new PutBucketRequest(bucketName));
            loadBookTestData(client, bucketName);

            final GetBucketResponse get_response = client
                    .getBucket(new GetBucketRequest(bucketName));
            final ListBucketResult get_result = get_response.getListBucketResult();
            assertFalse(get_result.getObjects().isEmpty());
            assertThat(get_result.getObjects().size(), is(4));

            // Attempt to delete bucket and catch expected
            // FailedRequestException
            try {
                client.deleteBucket(new DeleteBucketRequest(bucketName));
                fail("Should have thrown a FailedRequestException when trying to delete a non-empty bucket.");
            } catch (final FailedRequestException e) {
                assertTrue(409 == e.getStatusCode());
            }
        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void negativeCreateBucketNameConflict() throws SignatureException,
            IOException {
        final String bucketName = "negative_test_create_bucket_duplicate_name";

        client.putBucket(new PutBucketRequest(bucketName));

        // Attempt to create a bucket with a name conflicting with an existing
        // bucket
        try {
            client.putBucket(new PutBucketRequest(bucketName));
            fail("Should have thrown a FailedRequestException when trying to create a bucket with a duplicate name.");
        } catch (final FailedRequestException e) {
            assertTrue(409 == e.getStatusCode());
        } finally {
            client.deleteBucket(new DeleteBucketRequest(bucketName));
        }
    }

    @Test
    public void negativeDeleteNonExistentBucket() throws IOException,
            SignatureException {
        final String bucketName = "negative_test_delete_non_existent_bucket";

        // Attempt to delete bucket and catch expected FailedRequestException
        try {
            client.deleteBucket(new DeleteBucketRequest(bucketName));
            fail("Should have thrown a FailedRequestException when trying to delete a non-existent bucket.");
        } catch (final FailedRequestException e) {
            assertTrue(404 == e.getStatusCode());
        }
    }

    @Test
    public void deleteDirectory() throws IOException, SignatureException, XmlProcessingException {
        final String bucketName = "delete_directory";
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        try {
            helpers.ensureBucketExists(bucketName);

            final List<Ds3Object> objects = Lists.newArrayList(
                    new Ds3Object("dirA/obj1.txt", 1024),
                    new Ds3Object("dirA/obj2.txt", 1024),
                    new Ds3Object("dirA/obj3.txt", 1024),
                    new Ds3Object("obj1.txt", 1024));

            final Ds3ClientHelpers.Job putJob = helpers.startWriteJob(bucketName, objects);

            putJob.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                final public SeekableByteChannel buildChannel(final String key) throws IOException {
                    final byte[] randomData = IOUtils.toByteArray(new RandomDataInputStream(120, 1024));
                    final ByteBuffer randomBuffer = ByteBuffer.wrap(randomData);

                    final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(1024);
                    channel.write(randomBuffer);

                    return channel;
                }
            });

            final Iterable<Contents> objs = helpers.listObjects(bucketName, "dirA");

            for (final Contents objContents : objs) {
                client.deleteObject(new DeleteObjectRequest(bucketName, objContents.getKey()));
            }

            final Iterable<Contents> filesLeft = helpers.listObjects(bucketName);

            assertTrue(Iterables.size(filesLeft) == 1);
        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void multiObjectDeleteNotQuiet() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "multi_object_delete";
        final Ds3ClientHelpers wrapper = Ds3ClientHelpers.wrap(client);

        try {
            wrapper.ensureBucketExists(bucketName);
            loadBookTestData(client, bucketName);

            final Iterable<Contents> objs = wrapper.listObjects(bucketName);
            final DeleteObjectsResponse response = client
                    .deleteObjects(new DeleteObjectsRequest(bucketName, objs).withQuiet(false));
            assertThat(response, is(notNullValue()));
            assertThat(response.getDeleteResult(), is(notNullValue()));
            assertThat(response.getDeleteResult().getDeletedObjects().size(), is(4));

            final Iterable<Contents> filesLeft = wrapper.listObjects(bucketName);
            assertTrue(Iterables.size(filesLeft) == 0);
        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void multiObjectDeleteQuiet() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "multi_object_delete";
        final Ds3ClientHelpers wrapper = Ds3ClientHelpers.wrap(client);

        try {
            wrapper.ensureBucketExists(bucketName);
            loadBookTestData(client, bucketName);

            final Iterable<Contents> objs = wrapper.listObjects(bucketName);
            final DeleteObjectsResponse response = client
                    .deleteObjects(new DeleteObjectsRequest(bucketName, objs).withQuiet(true));
            assertThat(response, is(notNullValue()));
            assertThat(response.getDeleteResult(), is(notNullValue()));
            assertThat(response.getDeleteResult().getDeletedObjects().size(), is(0));

            final Iterable<Contents> filesLeft = wrapper.listObjects(bucketName);
            assertTrue(Iterables.size(filesLeft) == 0);
        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void multiObjectDeleteOfUnknownObjects() throws IOException, SignatureException {
        final String bucketName = "unknown_objects_delete";
        final Ds3ClientHelpers wrapper = Ds3ClientHelpers.wrap(client);

        try {
            wrapper.ensureBucketExists(bucketName);

            final List<String> objList = Lists.newArrayList("badObj1.txt", "badObj2.txt", "badObj3.txt");
            final DeleteObjectsResponse response = client
                    .deleteObjects(new DeleteObjectsRequest(bucketName, objList));
            assertThat(response, is(notNullValue()));
            assertThat(response.getDeleteResult(), is(notNullValue()));
            assertThat(response.getDeleteResult().getDeletedObjects().size(), is(0));
            assertThat(response.getDeleteResult().getErrors(), is(notNullValue()));
            assertThat(response.getDeleteResult().getErrors().size(), is(3));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void testRecoverWriteJob() throws SignatureException, IOException, XmlProcessingException, JobRecoveryException, URISyntaxException {
        final String bucketName = "test_recover_write_job_bucket";
        final String book1 = "beowulf.txt";
        final String book2 = "ulysses.txt";
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        try {
            client.putBucket(new PutBucketRequest(bucketName));
            helpers.ensureBucketExists(bucketName);

            final Path objPath1 = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book1);
            final Path objPath2 = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book2);
            final Ds3Object obj1 = new Ds3Object(book1, Files.size(objPath1));
            final Ds3Object obj2 = new Ds3Object(book2, Files.size(objPath2));

            final Ds3ClientHelpers.Job job = Ds3ClientHelpers.wrap(client).startWriteJob(bucketName, Lists.newArrayList(obj1, obj2));

            final PutObjectResponse putResponse1 = client.putObject(new PutObjectRequest(
                    job.getBucketName(),
                    book1,
                    new ResourceObjectPutter(RESOURCE_BASE_NAME).buildChannel(book1),
                    job.getJobId(),
                    0,
                    Files.size(objPath1)));
            assertThat(putResponse1, is(notNullValue()));
            assertThat(putResponse1.getStatusCode(), is(equalTo(200)));

            // Interuption...
            final Ds3ClientHelpers.Job recoverJob = Ds3ClientHelpers.wrap(client).recoverWriteJob(job.getJobId());

            final PutObjectResponse putResponse2 = client.putObject(new PutObjectRequest(
                    recoverJob.getBucketName(),
                    book2,
                    new ResourceObjectPutter(RESOURCE_BASE_NAME).buildChannel(book2),
                    recoverJob.getJobId(),
                    0,
                    Files.size(objPath2)));
            assertThat(putResponse2, is(notNullValue()));
            assertThat(putResponse2.getStatusCode(), is(equalTo(200)));
        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void verifySendCrc32cChecksum() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "crc_32_bucket";
        final String dataPolicyName = "crc_32_dp";
        final String storageDomainName = "crc_32_sd";
        final String poolPartitionName = "crc_32_pp";

        UUID storageDomainMemberId = null;
        UUID dataPersistenceRuleId = null;
        try {
            //Create data policy
            final PutDataPolicySpectraS3Response dataPolicyResponse = createDataPolicyWithVersioningAndCrcRequired(
                    dataPolicyName,
                    VersioningLevel.NONE,
                    ChecksumType.Type.CRC_32C,
                    client);

            //Create storage domain
            final PutStorageDomainSpectraS3Response storageDomainResponse = createStorageDomain(
                    storageDomainName,
                    client);

            //Create pool partition
            final PutPoolPartitionSpectraS3Response poolPartitionResponse = createPoolPartition(
                    poolPartitionName,
                    PoolType.ONLINE,
                    client);

            //Create storage domain member linking pool partition to storage domain
            final PutPoolStorageDomainMemberSpectraS3Response memberResponse = createPoolStorageDomainMember(
                    storageDomainResponse.getStorageDomainResult().getId(),
                    poolPartitionResponse.getPoolPartitionResult().getId(),
                    client);
            storageDomainMemberId = memberResponse.getStorageDomainMemberResult().getId();

            //create data persistence rule
            final PutDataPersistenceRuleSpectraS3Response dataPersistenceResponse = createDataPersistenceRule(
                    dataPolicyResponse.getDataPolicyResult().getId(),
                    storageDomainResponse.getStorageDomainResult().getId(),
                    client);
            dataPersistenceRuleId = dataPersistenceResponse.getDataPersistenceRuleResult().getDataPolicyId();

            //Create bucket with data policy
            client.putBucketSpectraS3(new PutBucketSpectraS3Request(bucketName)
                    .withDataPolicyId(dataPolicyResponse.getDataPolicyResult().getId()));

            //Verify send CRC 32c checksum
            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

            helpers.ensureBucketExists(bucketName);

            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object("beowulf.txt", 294059));

            final MasterObjectList mol = client
                    .putBulkJobSpectraS3(new PutBulkJobSpectraS3Request(bucketName, objs)).getResult();

            final FileChannel channel = FileChannel
                    .open(ResourceUtils.loadFileResource("books/beowulf.txt"), StandardOpenOption.READ);

            final PutObjectResponse response = client.putObject(new PutObjectRequest(
                    bucketName,
                    "beowulf.txt",
                    channel,
                    mol.getJobId(),
                    0,
                    294059)
                    .withChecksum(ChecksumType.compute(), ChecksumType.Type.CRC_32C));

            assertThat(response.getChecksumType(), is(ChecksumType.Type.CRC_32C));
            assertThat(response.getChecksum(), is("+ZBZbQ=="));

        } finally {
            deleteAllContents(client, bucketName);
            deleteDataPersistenceRule(dataPersistenceRuleId, client);
            deleteDataPolicy(dataPolicyName, client);
            deleteStorageDomainMember(storageDomainMemberId, client);
            deleteStorageDomain(storageDomainName, client);
            deletePoolPartition(poolPartitionName, client);
        }
    }

    @Test
    public void getTapes() throws IOException, SignatureException {
        final GetTapesSpectraS3Response response = client
                .getTapesSpectraS3(new GetTapesSpectraS3Request());
        final TapeList tapes = response.getTapeListResult();

        assumeThat(tapes, is(notNullValue()));
        assumeThat(tapes.getTapes(), is(notNullValue()));
        assumeThat(tapes.getTapes().size(), is(not(0)));

        assertThat(tapes.getTapes().get(0).getId(), is(notNullValue()));
    }

    @Test
    public void getTape() throws IOException, SignatureException {
        final GetTapesSpectraS3Response tapesResponse = client
                .getTapesSpectraS3(new GetTapesSpectraS3Request());
        final TapeList tapes = tapesResponse.getTapeListResult();

        assumeThat(tapes, is(notNullValue()));
        assumeThat(tapes.getTapes(), is(notNullValue()));
        assumeThat(tapes.getTapes().size(), is(not(0)));

        final GetTapeSpectraS3Response tapeResponse = client
                .getTapeSpectraS3(new GetTapeSpectraS3Request(tapes.getTapes().get(0).getId()));

        final Tape tape = tapeResponse.getTapeResult();

        assertThat(tape, is(notNullValue()));
        assertThat(tape.getId(), is(notNullValue()));

    }

    @Test
    public void testRecoverReadJob() throws SignatureException, IOException, XmlProcessingException, JobRecoveryException, URISyntaxException {
        final String bucketName = "test_recover_read_job_bucket";
        final String book1 = "beowulf.txt";
        final String book2 = "ulysses.txt";
        final Path objPath1 = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book1);
        final Path objPath2 = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book2);
        final Ds3Object obj1 = new Ds3Object(book1, Files.size(objPath1));
        final Ds3Object obj2 = new Ds3Object(book2, Files.size(objPath2));

        final Path dirPath = FileSystems.getDefault().getPath("output");
        if (!Files.exists(dirPath)) {
            Files.createDirectory(dirPath);
        }

        try {
            client.putBucket(new PutBucketRequest(bucketName));
            Ds3ClientHelpers.wrap(client).ensureBucketExists(bucketName);

            final Ds3ClientHelpers.Job putJob = Ds3ClientHelpers.wrap(client).startWriteJob(bucketName, Lists.newArrayList(obj1, obj2));
            putJob.transfer(new ResourceObjectPutter(RESOURCE_BASE_NAME));

            final FileChannel channel1 = FileChannel.open(
                    dirPath.resolve(book1),
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            final Ds3ClientHelpers.Job readJob = Ds3ClientHelpers.wrap(client).startReadJob(bucketName, Lists.newArrayList(obj1, obj2));
            final GetObjectResponse readResponse1 = client.getObject(
                    new GetObjectRequest(
                            bucketName,
                            book1,
                            channel1,
                            readJob.getJobId(),
                            0));

            assertThat(readResponse1, is(notNullValue()));
            assertThat(readResponse1.getStatusCode(), is(equalTo(200)));

            // Interruption...
            final Ds3ClientHelpers.Job recoverJob = Ds3ClientHelpers.wrap(client).recoverReadJob(readJob.getJobId());

            final FileChannel channel2 = FileChannel.open(
                    dirPath.resolve(book2),
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            final GetObjectResponse readResponse2 = client.getObject(
                    new GetObjectRequest(
                            bucketName,
                            book2,
                            channel2,
                            recoverJob.getJobId(),
                            0));
            assertThat(readResponse2, is(notNullValue()));
            assertThat(readResponse2.getStatusCode(), is(equalTo(200)));

        } finally {
            deleteAllContents(client, bucketName);
            for( final Path tempFile : Files.newDirectoryStream(dirPath) ){
                Files.delete(tempFile);
            }
            Files.delete(dirPath);
        }
    }

    @Test
    public void putDirectory() throws IOException, SignatureException, XmlProcessingException {
        assumeVersion1_2(client);

        final String bucketName = "putDir";

        try {

            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object("dir/"));

            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

            helpers.ensureBucketExists(bucketName);

            final Ds3ClientHelpers.Job job = helpers.startWriteJob(bucketName, objs);

            job.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    fail("This should not be called");
                    return null;
                }
            });

            final GetBucketResponse response = client.getBucket(new GetBucketRequest(bucketName));

            assertThat(response.getListBucketResult().getObjects().size(), is(1));
            assertThat(response.getListBucketResult().getObjects().get(0).getKey(), is("dir/"));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void putDirectoryWithOtherObjects() throws IOException, SignatureException, XmlProcessingException {
        assumeVersion1_2(client);

        final String bucketName = "mixedPutDir";

        try {

            final byte[] content = "I'm text with some more data so that it gets flushed to the output cache.".getBytes(Charset.forName("UTF-8"));

            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object("dir/"), new Ds3Object("obj.txt", content.length));

            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

            helpers.ensureBucketExists(bucketName);

            final Ds3ClientHelpers.Job job = helpers.startWriteJob(bucketName, objs);

            final AtomicInteger counter = new AtomicInteger(0);

            job.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    counter.incrementAndGet();
                    return new ByteArraySeekableByteChannel(content);
                }
            });

            final GetBucketResponse response = client.getBucket(new GetBucketRequest(bucketName));

            assertThat(response.getListBucketResult().getObjects().size(), is(2));
            assertThat(counter.get(), is(1));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void eventHandlerTriggers() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "eventBucket";

        try {

            final AtomicInteger counter = new AtomicInteger(0);

            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

            helpers.ensureBucketExists(bucketName);

            loadBookTestData(client, bucketName);

            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object("beowulf.txt"));

            final Ds3ClientHelpers.Job job = helpers.startReadJob(bucketName, objs);

            job.attachObjectCompletedListener(new ObjectCompletedListener() {
                @Override
                public void objectCompleted(final String name) {
                    LOG.info("finished getting: " + name);
                    counter.incrementAndGet();
                }
            });

            job.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    return new NullChannel();
                }
            });

            assertThat(counter.get(), is(1));
        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void eventHandlerRegistrationAndDeregistration() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "eventBucket";

        try {

            final AtomicInteger counter = new AtomicInteger(0);

            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

            helpers.ensureBucketExists(bucketName);

            loadBookTestData(client, bucketName);

            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object("beowulf.txt"));

            final Ds3ClientHelpers.Job job = helpers.startReadJob(bucketName, objs);

            final ObjectCompletedListener eventHandler = new ObjectCompletedListener() {
                @Override
                public void objectCompleted(final String name) {
                    LOG.info("finished getting: " + name);
                    counter.incrementAndGet();
                }
            };

            job.attachObjectCompletedListener(eventHandler);

            job.removeObjectCompletedListener(eventHandler);

            job.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    return new NullChannel();
                }
            });

            assertThat(counter.get(), is(0));
        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void partialObjectGet() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "partialObjectGet";

        try {
            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);
            helpers.ensureBucketExists(bucketName);

            loadBookTestData(client, bucketName);

            final List<Ds3Object> objs = Lists.newArrayList();
            objs.add(new PartialDs3Object("beowulf.txt", Range.byLength(100, 100)));

            final Ds3ClientHelpers.Job job = helpers.startReadJob(bucketName, objs);

            final ByteArraySeekableByteChannel contents = new ByteArraySeekableByteChannel();

            job.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    return contents;
                }
            });

            assertThat(contents.size(), is(100L));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void partialObjectMultiRangeGet() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "partialObjectGet";

        try {
            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);
            helpers.ensureBucketExists(bucketName);

            loadBookTestData(client, bucketName);

            final List<Ds3Object> objs = Lists.newArrayList();
            objs.add(new PartialDs3Object("beowulf.txt", Range.byLength(100, 100)));
            objs.add(new PartialDs3Object("beowulf.txt", Range.byLength(1000, 200)));

            final Ds3ClientHelpers.Job job = helpers.startReadJob(bucketName, objs);

            final ByteArraySeekableByteChannel contents = new ByteArraySeekableByteChannel();

            job.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    return contents;
                }
            });

            assertThat(contents.size(), is(300L));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void partialObjectGetOverChunkBoundry() throws IOException, SignatureException, XmlProcessingException {
        final String bucketName = "partialGetOverBoundry";
        final String testFile = "testObject.txt";
        final Path filePath = Files.createTempFile("ds3", testFile);
        final int seed = 12345;
        LOG.info("Test file: " + filePath.toAbsolutePath());
        try {
            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

            helpers.ensureBucketExists(bucketName);

            final int objectSize = PutBulkJobSpectraS3Request.MIN_UPLOAD_SIZE_IN_BYTES * 2;

            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object(testFile, objectSize));

            final Ds3ClientHelpers.Job putJob = helpers
                    .startWriteJob(bucketName, objs, WriteJobOptions.create()
                            .withMaxUploadSize(PutBulkJobSpectraS3Request.MIN_UPLOAD_SIZE_IN_BYTES));

            putJob.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    final byte[] randomData = IOUtils.toByteArray(new RandomDataInputStream(seed, objectSize));
                    final ByteBuffer randomBuffer = ByteBuffer.wrap(randomData);

                    final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(objectSize);
                    channel.write(randomBuffer);

                    return channel;

                }
            });

            final List<Ds3Object> partialObjectGet = Lists.newArrayList();
            partialObjectGet.add(new PartialDs3Object(testFile, Range.byPosition(
                    PutBulkJobSpectraS3Request.MIN_UPLOAD_SIZE_IN_BYTES - 100,
                    PutBulkJobSpectraS3Request.MIN_UPLOAD_SIZE_IN_BYTES + 99)));

            final Ds3ClientHelpers.Job getJob = helpers.startReadJob(bucketName, partialObjectGet);

            getJob.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    return Files.newByteChannel(filePath, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                }
            });

            assertThat(Files.size(filePath), is(200L));

        } finally {
            Files.delete(filePath);
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void partialGetWithBookOverChunkBoundry() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "partialGetOnBook";
        final Path filePath = Files.createTempFile("ds3", "lesmis-copies.txt");
        LOG.info("TempFile for partial get of book: " + filePath.toAbsolutePath().toString());

        try {

            final Ds3ClientHelpers wrapper = Ds3ClientHelpers.wrap(client);

            wrapper.ensureBucketExists(bucketName);

            final List<Ds3Object> putObjects = Lists.newArrayList(new Ds3Object("lesmis-copies.txt", 13290604));

            final Ds3ClientHelpers.Job putJob = wrapper
                    .startWriteJob(bucketName, putObjects, WriteJobOptions.create()
                            .withMaxUploadSize(PutBulkJobSpectraS3Request.MIN_UPLOAD_SIZE_IN_BYTES));

            putJob.transfer(new ResourceObjectPutter("largeFiles/"));

            final List<Ds3Object> getObjects = Lists.newArrayList();
            getObjects.add(new PartialDs3Object("lesmis-copies.txt", Range.byLength(1048476, 200)));

            final Ds3ClientHelpers.Job getJob = wrapper.startReadJob(bucketName, getObjects);

            getJob.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    return Files.newByteChannel(filePath, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                }
            });

            final Path expectedResultPath = Paths.get(Smoke_Test.class.getResource("/largeFiles/output").toURI());

            assertThat(Files.size(filePath), is(200L));
            final String partialFile = new String(Files.readAllBytes(filePath), Charset.forName("UTF-8"));
            final String expectedResult = new String(Files.readAllBytes(expectedResultPath), Charset.forName("UTF-8"));
            assertThat(partialFile, is(expectedResult.substring(0, expectedResult.length() - 1))); // need the trim to remove a newline that is added by the os
        } finally {
            deleteAllContents(client, bucketName);
            Files.delete(filePath);
        }
    }

    @Test
    public void getObjectSize() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "getObjectSize";

        try {
            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

            helpers.ensureBucketExists(bucketName);

            loadBookTestData(client, bucketName);

            final List<Ds3Object> objects = Lists.newArrayList(new Ds3Object("beowulf.txt"));

            final BulkResponse bulkResponse = client
                    .getBulkJobSpectraS3(new GetBulkJobSpectraS3Request(bucketName, objects));

            final UUID jobId = bulkResponse.getResult().getJobId();

            final GetObjectResponse getObjectResponse = client.getObject(
                    new GetObjectRequest(
                            bucketName,
                            "beowulf.txt",
                            new NullChannel(),
                            jobId,
                            0));

            assertThat(getObjectResponse.getObjectSize(), is(294059L));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void headObjectSize() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "headObjectSize";

        try {
            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

            helpers.ensureBucketExists(bucketName);

            loadBookTestData(client, bucketName);

            final HeadObjectResponse headObjectResponse = client.headObject(new HeadObjectRequest(bucketName, "beowulf.txt"));

            assertThat(headObjectResponse.getObjectSize(), is(294059L));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void attachDataTransferredListenerTest() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "test_attachDataTransferredListener";
        try {
            client.putBucket(new PutBucketRequest(bucketName));

            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

            final List<Ds3Object> objects = new ArrayList<>();
            long booksSize = 0;
            for(final String book : BOOKS) {
                final Path objPath = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book);
                final long bookSize = Files.size(objPath);
                booksSize += bookSize;
                final Ds3Object obj = new Ds3Object(book, bookSize);

                objects.add(obj);
            }

            final Ds3ClientHelpers.Job job = helpers.startWriteJob(bucketName, objects);
            final TransferredListener transferredListener = new TransferredListener();

            job.attachObjectCompletedListener(transferredListener);
            job.attachDataTransferredListener(transferredListener);

            job.transfer(new ResourceObjectPutter(RESOURCE_BASE_NAME));

            assertThat(transferredListener.getTotalBytes(), is(booksSize));
            assertThat(transferredListener.getNumberOfFiles(), is(BOOKS.length));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void testHelperMetadata() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "helper_metadata";
        try {
            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);
            helpers.ensureBucketExists(bucketName);

            final List<Ds3Object> objects = new ArrayList<>();
            for(final String book : BOOKS) {
                final Path objPath = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book);
                final long bookSize = Files.size(objPath);
                final Ds3Object obj = new Ds3Object(book, bookSize);

                objects.add(obj);
            }

            final Ds3ClientHelpers.Job job = helpers.startWriteJob(bucketName, objects);

            final AtomicBoolean calledWithMetadata = new AtomicBoolean(false);

            job.withMetadata(new Ds3ClientHelpers.MetadataAccess() {
                @Override
                public Map<String, String> getMetadataValue(final String filename) {
                    if (filename.equals("beowulf.txt")) {
                        calledWithMetadata.set(true);
                        return ImmutableMap.of("fileType", "text");
                    }

                    return null;
                }
            });

            job.transfer(new ResourceObjectPutter(RESOURCE_BASE_NAME));

            assertTrue(calledWithMetadata.get());

            final HeadObjectResponse response = client.headObject(new HeadObjectRequest(bucketName, "beowulf.txt"));
            final Metadata metadata = response.getMetadata();
            final List<String> values = metadata.get("fileType");
            assertThat(values.size(), is(1));
            assertThat(values.get(0), is("text"));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void nameWithSpace() throws IOException, SignatureException, XmlProcessingException {
        final String bucketName = "test_space_bucket";

        try {
            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

            helpers.ensureBucketExists(bucketName);

            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object("space object.txt", 10));

            final Ds3ClientHelpers.Job job = helpers.startWriteJob(bucketName, objs);

            job.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {

                    final byte[] randomData = IOUtils.toByteArray(new RandomDataInputStream(124345, 10));
                    final ByteBuffer randomBuffer = ByteBuffer.wrap(randomData);

                    final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(10);
                    channel.write(randomBuffer);

                    return channel;
                }
            });

            final Iterable<Contents> contents = helpers.listObjects(bucketName);

            assertThat(Iterables.size(contents), is(1));
            assertThat(Iterables.get(contents, 0).getKey(), is("space object.txt"));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void TestPlusCharacterInQueryParam() throws IOException, SignatureException, XmlProcessingException {
        final String bucketName = "TestPlusCharacterInQueryParam";
        final String objectName = "Test+Plus+Character";
        try {
            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

            helpers.ensureBucketExists(bucketName);

            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object(objectName, 10));

            final Ds3ClientHelpers.Job job = helpers.startWriteJob(bucketName, objs);

            job.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {

                    final byte[] randomData = IOUtils.toByteArray(new RandomDataInputStream(124345, 10));
                    final ByteBuffer randomBuffer = ByteBuffer.wrap(randomData);

                    final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(10);
                    channel.write(randomBuffer);

                    return channel;
                }
            });

            final GetObjectsSpectraS3Response getObjectsSpectraS3Response = client
                    .getObjectsSpectraS3(new GetObjectsSpectraS3Request().withName(objectName));

            assertThat(getObjectsSpectraS3Response.getS3ObjectListResult().getS3Objects().size(), is(1));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void TestSpecialCharacterInObjectName() throws IOException, SignatureException, XmlProcessingException {
        final String bucketName = "TestSpecialCharacterInObjectName";
        final String objectName = "varsity1314/_projects/VARSITY 13-14/_versions/Varsity 13-14 (2015-10-05 1827)/_project/Trash/PC\uF022MAC HD.avb";
        try {
            final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

            helpers.ensureBucketExists(bucketName);

            final List<Ds3Object> objs = Lists.newArrayList(new Ds3Object(objectName, 10));

            final Ds3ClientHelpers.Job job = helpers.startWriteJob(bucketName, objs);

            job.transfer(new Ds3ClientHelpers.ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {

                    final byte[] randomData = IOUtils.toByteArray(new RandomDataInputStream(124345, 10));
                    final ByteBuffer randomBuffer = ByteBuffer.wrap(randomData);

                    final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(10);
                    channel.write(randomBuffer);

                    return channel;
                }
            });

            final GetObjectsSpectraS3Response getObjectsSpectraS3Response = client
                    .getObjectsSpectraS3(new GetObjectsSpectraS3Request().withName(objectName));

            assertThat(getObjectsSpectraS3Response.getS3ObjectListResult().getS3Objects().size(), is(1));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void testGetObjectDetails() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "TestGetObjectDetails";
        final String objectName = "beowulf.txt";
        try {
            client.putBucket(new PutBucketRequest(bucketName));
            loadBookTestData(client, bucketName);

            final GetObjectDetailsSpectraS3Response response = client.getObjectDetailsSpectraS3(
                    new GetObjectDetailsSpectraS3Request(objectName, bucketName));
            final S3Object object = response.getS3ObjectResult();
            assertThat(object.getName(), is(objectName));
            assertThat(object.getType(), is(S3ObjectType.DATA));
        } finally {
            deleteAllContents(client,bucketName);
        }
    }

    @Test
    public void testGetObjectsWithFullDetails() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "TestGetObjectsWithFullDetails";
        try {
            client.putBucket(new PutBucketRequest(bucketName));
            loadBookTestData(client, bucketName);

            final GetObjectsWithFullDetailsSpectraS3Request request = new GetObjectsWithFullDetailsSpectraS3Request()
                    .withIncludePhysicalPlacement(true);
            final GetObjectsWithFullDetailsSpectraS3Response response = client.getObjectsWithFullDetailsSpectraS3(request);
            assertThat(response.getDetailedS3ObjectListResult().getDetailedS3Objects().size(), is(4));
            assertThat(response.getDetailedS3ObjectListResult().getDetailedS3Objects().get(0).getName(), is("beowulf.txt"));
            assertThat(response.getDetailedS3ObjectListResult().getDetailedS3Objects().get(1).getName(), is("sherlock_holmes.txt"));
            assertThat(response.getDetailedS3ObjectListResult().getDetailedS3Objects().get(2).getName(), is("tale_of_two_cities.txt"));
            assertThat(response.getDetailedS3ObjectListResult().getDetailedS3Objects().get(3).getName(), is("ulysses.txt"));
        } finally {
            deleteAllContents(client, bucketName);
        }
    }
}
