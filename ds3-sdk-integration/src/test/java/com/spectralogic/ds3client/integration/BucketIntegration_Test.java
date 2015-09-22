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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.JobRecoveryException;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.S3Object;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.JobStatus;
import com.spectralogic.ds3client.models.bulk.Priority;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

public class BucketIntegration_Test {

    private static Ds3Client client;

    @BeforeClass
    public static void startup() {
        client = Util.fromEnv();
    }

    @AfterClass
    public static void teardown() throws IOException {
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
    public void modifyJob() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "test_modify_job";
        try {
            client.putBucket(new PutBucketRequest(bucketName));

            List<Ds3Object> objects = new ArrayList<>();
            final File objFile = ResourceUtils.loadFileResource("books/beowulf.txt");
            final Ds3Object obj = new Ds3Object("beowulf.txt", objFile.length());
            objects.add(obj);

            WriteJobOptions jobOptions = WriteJobOptions.create().withPriority(Priority.LOW);

            Ds3ClientHelpers.Job job = com.spectralogic.ds3client.helpers.Ds3ClientHelpers
                    .wrap(client).startWriteJob(bucketName, objects, jobOptions);

            client.modifyJob(new ModifyJobRequest(job.getJobId()).withPriority(Priority.HIGH));

            GetJobResponse response = client.getJob(new GetJobRequest(job.getJobId()));

            assertThat(response.getMasterObjectList().getPriority(), is(Priority.HIGH));

        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void getObjects() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "test_get_objs";
        try {
            client.putBucket(new PutBucketRequest(bucketName));
            Util.loadBookTestData(client, bucketName);

            final HeadObjectResponse headResponse = client.headObject(new HeadObjectRequest(
                    bucketName, "beowulf.txt"));
            assertThat(headResponse.getStatus(),
                    is(HeadObjectResponse.Status.EXISTS));

            final GetObjectsResponse response = client
                    .getObjects(new GetObjectsRequest().withBucket("test_get_objs"));

            assertFalse(response.getS3ObjectList().getObjects().isEmpty());
            assertThat(response.getS3ObjectList().getObjects().size(), is(4));
            assertTrue(s3ObjectExists(response.getS3ObjectList().getObjects(), "beowulf.txt"));

        } finally {
            Util.deleteAllContents(client,bucketName);
        }

    }

    private boolean s3ObjectExists(final List<S3Object> objects, final String fileName) {
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
            Util.loadBookTestDataWithPrefix(client, bucketName, "folder/");

            HeadObjectResponse response = client.headObject(new HeadObjectRequest(
                    bucketName, "folder/beowulf.txt"));
            assertThat(response.getStatus(),
                    is(HeadObjectResponse.Status.EXISTS));

            client.deleteFolder(new DeleteFolderRequest(bucketName, "folder"));

            response = client.headObject(new HeadObjectRequest(
                    bucketName, "folder/beowulf.txt"));
            assertThat(response.getStatus(),
                    is(HeadObjectResponse.Status.DOESNTEXIST));
        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void emptyBucket() throws IOException, SignatureException {
        final String bucketName = "test_empty_bucket";

        try {
            client.putBucket(new PutBucketRequest(bucketName));

            final GetBucketResponse request = client
                    .getBucket(new GetBucketRequest(bucketName));
            final ListBucketResult result = request.getResult();
            assertThat(result.getContentsList(), is(notNullValue()));
            assertTrue(result.getContentsList().isEmpty());
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
            Util.loadBookTestData(client, bucketName);

            final GetBucketResponse response = client
                    .getBucket(new GetBucketRequest(bucketName));

            final ListBucketResult result = response.getResult();

            assertFalse(result.getContentsList().isEmpty());
            assertThat(result.getContentsList().size(), is(4));
        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void getContents() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "test_get_contents";

        try {
            client.putBucket(new PutBucketRequest(bucketName));
            Util.loadBookTestData(client, bucketName);

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

            final GetJobResponse jobResponse = client.getJob(new GetJobRequest(jobId));
            assertThat(jobResponse.getMasterObjectList().getStatus(), is(JobStatus.COMPLETED));

        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void negativeDeleteNonEmptyBucket() throws IOException,
            SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "negative_test_delete_non_empty_bucket";

        try {
            // Create bucket and put objects (4 book .txt files) to it
            client.putBucket(new PutBucketRequest(bucketName));
            Util.loadBookTestData(client, bucketName);

            final GetBucketResponse get_response = client
                    .getBucket(new GetBucketRequest(bucketName));
            final ListBucketResult get_result = get_response.getResult();
            assertFalse(get_result.getContentsList().isEmpty());
            assertThat(get_result.getContentsList().size(), is(4));

            // Attempt to delete bucket and catch expected
            // FailedRequestException
            try {
                client.deleteBucket(new DeleteBucketRequest(bucketName));
                fail("Should have thrown a FailedRequestException when trying to delete a non-empty bucket.");
            } catch (FailedRequestException e) {
                assertTrue(409 == e.getStatusCode());
            }
        } finally {
            Util.deleteAllContents(client, bucketName);
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
        } catch (FailedRequestException e) {
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
        } catch (FailedRequestException e) {
            assertTrue(404 == e.getStatusCode());
        }
    }

    @Test
    public void negativePutDuplicateObject() throws SignatureException,
            IOException, XmlProcessingException, URISyntaxException {
        final String bucketName = "negative_test_put_duplicate_object";

        try {
            client.putBucket(new PutBucketRequest(bucketName));
            Util.loadBookTestData(client, bucketName);

            final GetBucketResponse response = client
                    .getBucket(new GetBucketRequest(bucketName));
            final ListBucketResult result = response.getResult();
            assertFalse(result.getContentsList().isEmpty());
            assertThat(result.getContentsList().size(), is(4));

            try {
                Util.loadBookTestData(client, bucketName);
                fail("Should have thrown a FailedRequestException when trying to put duplicate objects.");
            } catch (FailedRequestException e) {
                assertTrue(409 == e.getStatusCode());
            }
        } finally {
            Util.deleteAllContents(client, bucketName);
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
                public SeekableByteChannel buildChannel(String key) throws IOException {
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
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void multiObjectDeleteNotQuiet() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "multi_object_delete";
        final Ds3ClientHelpers wrapper = Ds3ClientHelpers.wrap(client);

        try {
            wrapper.ensureBucketExists(bucketName);
            Util.loadBookTestData(client, bucketName);

            final Iterable<Contents> objs = wrapper.listObjects(bucketName);
            final DeleteMultipleObjectsResponse response = client.deleteMultipleObjects(new DeleteMultipleObjectsRequest(bucketName, objs).withQuiet(false));
            assertThat(response, is(notNullValue()));
            assertThat(response.getResult(), is(notNullValue()));
            assertThat(response.getResult().getDeletedList().size(), is(4));

            final Iterable<Contents> filesLeft = wrapper.listObjects(bucketName);
            assertTrue(Iterables.size(filesLeft) == 0);
        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void multiObjectDeleteQuiet() throws IOException, SignatureException, URISyntaxException, XmlProcessingException {
        final String bucketName = "multi_object_delete";
        final Ds3ClientHelpers wrapper = Ds3ClientHelpers.wrap(client);

        try {
            wrapper.ensureBucketExists(bucketName);
            Util.loadBookTestData(client, bucketName);

            final Iterable<Contents> objs = wrapper.listObjects(bucketName);
            final DeleteMultipleObjectsResponse response = client.deleteMultipleObjects(new DeleteMultipleObjectsRequest(bucketName, objs).withQuiet(true));
            assertThat(response, is(notNullValue()));
            assertThat(response.getResult(), is(notNullValue()));
            assertThat(response.getResult().getDeletedList(), is(nullValue()));

            final Iterable<Contents> filesLeft = wrapper.listObjects(bucketName);
            assertTrue(Iterables.size(filesLeft) == 0);
        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void multiObjectDeleteOfUnknownObjects() throws IOException, SignatureException {
        final String bucketName = "unknown_objects_delete";
        final Ds3ClientHelpers wrapper = Ds3ClientHelpers.wrap(client);

        try {
            wrapper.ensureBucketExists(bucketName);

            final List<String> objList = Lists.newArrayList("badObj1.txt", "badObj2.txt", "badObj3.txt");
            final DeleteMultipleObjectsResponse response = client.deleteMultipleObjects(new DeleteMultipleObjectsRequest(bucketName, objList));
            assertThat(response, is(notNullValue()));
            assertThat(response.getResult(), is(notNullValue()));
            assertThat(response.getResult().getDeletedList(), is(nullValue()));
            assertThat(response.getResult().getErrorList(), is(notNullValue()));
            assertThat(response.getResult().getErrorList().size(), is(3));

        } finally {
            Util.deleteAllContents(client, bucketName);
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

            final File objFile1 = ResourceUtils.loadFileResource(Util.RESOURCE_BASE_NAME + book1);
            final File objFile2 = ResourceUtils.loadFileResource(Util.RESOURCE_BASE_NAME + book2);
            final Ds3Object obj1 = new Ds3Object(book1, objFile1.length());
            final Ds3Object obj2 = new Ds3Object(book2, objFile2.length());

            final Ds3ClientHelpers.Job job = Ds3ClientHelpers.wrap(client).startWriteJob(bucketName, Lists.newArrayList(obj1, obj2));

            final PutObjectResponse putResponse1 = client.putObject(new PutObjectRequest(
                    job.getBucketName(),
                    book1,
                    job.getJobId(),
                    objFile1.length(),
                    0,
                    new ResourceObjectPutter(Util.RESOURCE_BASE_NAME).buildChannel(book1)
            ));
            assertThat(putResponse1, is(notNullValue()));
            assertThat(putResponse1.getStatusCode(), is(equalTo(200)));

            // Interuption...
            final Ds3ClientHelpers.Job recoverJob = Ds3ClientHelpers.wrap(client).recoverWriteJob(job.getJobId());

            final PutObjectResponse putResponse2 = client.putObject(new PutObjectRequest(
                    recoverJob.getBucketName(),
                    book2,
                    recoverJob.getJobId(),
                    objFile2.length(),
                    0,
                    new ResourceObjectPutter(Util.RESOURCE_BASE_NAME).buildChannel(book2)
            ));
            assertThat(putResponse2, is(notNullValue()));
            assertThat(putResponse2.getStatusCode(), is(equalTo(200)));
        } finally {
            Util.deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void testRecoverReadJob() throws SignatureException, IOException, XmlProcessingException, JobRecoveryException, URISyntaxException {
        final String bucketName = "test_recover_read_job_bucket";
        final String book1 = "beowulf.txt";
        final String book2 = "ulysses.txt";
        final File objFile1 = ResourceUtils.loadFileResource(Util.RESOURCE_BASE_NAME + book1);
        final File objFile2 = ResourceUtils.loadFileResource(Util.RESOURCE_BASE_NAME + book2);
        final Ds3Object obj1 = new Ds3Object(book1, objFile1.length());
        final Ds3Object obj2 = new Ds3Object(book2, objFile2.length());

        final Path dirPath = FileSystems.getDefault().getPath("output");
        if (!Files.exists(dirPath)) {
            Files.createDirectory(dirPath);
        }

        try {
            client.putBucket(new PutBucketRequest(bucketName));
            Ds3ClientHelpers.wrap(client).ensureBucketExists(bucketName);

            final Ds3ClientHelpers.Job putJob = Ds3ClientHelpers.wrap(client).startWriteJob(bucketName, Lists.newArrayList(obj1, obj2));
            putJob.transfer(new ResourceObjectPutter(Util.RESOURCE_BASE_NAME));

            final FileChannel channel1 = FileChannel.open(
                    dirPath.resolve(book1),
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            final Ds3ClientHelpers.Job readJob = Ds3ClientHelpers.wrap(client).startReadJob(bucketName, Lists.newArrayList(obj1, obj2));
            final GetObjectResponse readResponse1 = client.getObject(new GetObjectRequest(bucketName, book1, 0, readJob.getJobId(), channel1));

            assertThat(readResponse1, is(notNullValue()));
            assertThat(readResponse1.getStatusCode(), is(equalTo(200)));

            // Interuption...
            final Ds3ClientHelpers.Job recoverJob = Ds3ClientHelpers.wrap(client).recoverReadJob(readJob.getJobId());

            final FileChannel channel2 = FileChannel.open(
                    dirPath.resolve(book2),
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            final GetObjectResponse readResponse2 = client.getObject(new GetObjectRequest(bucketName, book2, 0, recoverJob.getJobId(), channel2));
            assertThat(readResponse2, is(notNullValue()));
            assertThat(readResponse2.getStatusCode(), is(equalTo(200)));

        } finally {
            Util.deleteAllContents(client, bucketName);
            for( final Path tempFile : Files.newDirectoryStream(dirPath) ){
                Files.delete(tempFile);
            }
            Files.delete(dirPath);
        }
    }
}
