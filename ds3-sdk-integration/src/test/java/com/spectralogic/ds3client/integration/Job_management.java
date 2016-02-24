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
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.JobRecoveryException;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeThat;

public class Job_management {

    private static final Logger LOG = LoggerFactory.getLogger(Job_management.class);

    private static Ds3Client client;

    @BeforeClass
    public static void startup() {
        client = fromEnv();
    }

    @AfterClass
    public static void teardown() throws IOException {
        client.close();
    }

    @Test
    public void modifyJobPriority() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "test_modify_job";
        try {
            client.putBucket(new PutBucketRequest(bucketName));

            final List<Ds3Object> objects = new ArrayList<>();
            final Ds3Object obj = new Ds3Object("test", 2);
            objects.add(obj);

            final WriteJobOptions jobOptions = WriteJobOptions.create().withPriority(Priority.LOW);

            final Ds3ClientHelpers.Job job = Ds3ClientHelpers
                    .wrap(client).startWriteJob(bucketName, objects, jobOptions);

            client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(job.getJobId())
                    .withPriority(Priority.HIGH));

            final GetJobSpectraS3Response response = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId()));

            assertThat(response.getMasterObjectListResult().getPriority(), is(Priority.HIGH));

        } finally {
            deleteAllContents(client, bucketName);
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
    public void cancelAllJobs() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "test_cancel_all_jobs";
        try {
            client.putBucket(new PutBucketRequest(bucketName));

            final List<Ds3Object> objectsOne = new ArrayList<>();
            final Ds3Object obj = new Ds3Object("testOne", 2);
            objectsOne.add(obj);

            final Ds3ClientHelpers.Job jobOne = Ds3ClientHelpers
                    .wrap(client).startWriteJob(bucketName, objectsOne);

            final List<Ds3Object> objectsTwo = new ArrayList<>();
            final Ds3Object objTwo = new Ds3Object("testTwo", 2);
            objectsTwo.add(objTwo);

            final Ds3ClientHelpers.Job jobTwo = Ds3ClientHelpers
                    .wrap(client).startWriteJob(bucketName, objectsTwo);

            final CancelAllJobsSpectraS3Response response = client
                    .cancelAllJobsSpectraS3(new CancelAllJobsSpectraS3Request());
            response.checkStatusCode(204);

            assertTrue(client.getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request())
                    .getJobListResult().getJobs().isEmpty());

            } finally {
            deleteAllContents(client, bucketName);
        }
    }

        @Test
        public void getCanceledJobs() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
            final String bucketName = "test_get_canceled_jobs";
            try {
                client.putBucket(new PutBucketRequest(bucketName));

                final List<Ds3Object> objectsOne = new ArrayList<>();
                final Ds3Object obj = new Ds3Object("testOne", 2);
                objectsOne.add(obj);

                final Ds3ClientHelpers.Job jobOne = Ds3ClientHelpers
                        .wrap(client).startWriteJob(bucketName, objectsOne);

                final UUID jobOneId = jobOne.getJobId();

                client.cancelAllJobsSpectraS3(new CancelAllJobsSpectraS3Request());

                final GetCanceledJobsSpectraS3Response getCanceledResponse = client
                        .getCanceledJobsSpectraS3(new GetCanceledJobsSpectraS3Request());

                for (CanceledJob job : getCanceledResponse.getCanceledJobListResult().getCanceledJobs()) {
                    LOG.info("canceledJob: " + job.getName());
                }

                //assert(getCanceledResponse.getCanceledJobListResult().getCanceledJobs().(jobOne));
                //assert(getCanceledResponse.getCanceledJobListResult().getCanceledJobs().contains(jobTwo));

            } finally {
                deleteAllContents(client, bucketName);
            }
    }
}
