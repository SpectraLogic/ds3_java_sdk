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
import javafx.util.converter.DateTimeStringConverter;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.spectralogic.ds3client.helpers.Ds3ClientHelpers.*;
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

//    @Test
//    public void createAggregatingJob() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
//        final String bucketName = "test_cancel_all_jobs";
//        try {
//            client.putBucket(new PutBucketRequest(bucketName));
//
//            final List<Ds3Object> objectsOne = new ArrayList<>();
//            final Ds3Object obj = new Ds3Object("testOne", 2);
//            objectsOne.add(obj);
//
//            WriteJobOptions aggregatingOption = new WriteJobOptions();
//
//            final Ds3ClientHelpers.Job jobOne =
//                    wrap(client).startWriteJob(bucketName, objectsOne, );
//
//            final List<Ds3Object> objectsTwo = new ArrayList<>();
//            final Ds3Object objTwo = new Ds3Object("testTwo", 2);
//            objectsTwo.add(objTwo);
//
//            final Ds3ClientHelpers.Job jobTwo =
//                    wrap(client).startWriteJob(bucketName, objectsTwo);
//
//            final CancelAllJobsSpectraS3Response response = client
//                    .cancelAllJobsSpectraS3(new CancelAllJobsSpectraS3Request());
//            response.checkStatusCode(204);
//
//            assertTrue(client.getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request())
//                    .getJobListResult().getJobs().isEmpty());
//        } finally {
//            deleteAllContents(client, bucketName);
//        }
//    }

    @Test
    public void modifyJobPriority() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "test_modify_job_priority";
        try {
            client.putBucket(new PutBucketRequest(bucketName));

            final List<Ds3Object> objects = new ArrayList<>();
            final Ds3Object obj = new Ds3Object("test", 2);
            objects.add(obj);

            final WriteJobOptions jobOptions = WriteJobOptions.create().withPriority(Priority.LOW);

            final Ds3ClientHelpers.Job job =
                    wrap(client).startWriteJob(bucketName, objects, jobOptions);

            client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(job.getJobId())
                    .withPriority(Priority.HIGH));

            final GetJobSpectraS3Response response = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId()));

            assertThat(response.getMasterObjectListResult().getPriority(), is(Priority.HIGH));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void modifyJobName() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "test_modify_job_name";
        try {
            client.putBucket(new PutBucketRequest(bucketName));

            final List<Ds3Object> objects = new ArrayList<>();
            final Ds3Object obj = new Ds3Object("test", 2);
            objects.add(obj);

            final WriteJobOptions jobOptions = WriteJobOptions.create();

            final Ds3ClientHelpers.Job job =
                    wrap(client).startWriteJob(bucketName, objects, jobOptions);

            client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(job.getJobId())
                    .withName("newName"));

            final GetJobSpectraS3Response response = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId()));

            assertThat(response.getMasterObjectListResult().getName(), is("newName"));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void modifyJobCreationDate() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "test_modify_job_creation_date";
        try {
            client.putBucket(new PutBucketRequest(bucketName));

            final List<Ds3Object> objects = new ArrayList<>();
            final Ds3Object obj = new Ds3Object("test", 2);
            objects.add(obj);

            final Ds3ClientHelpers.Job job =
                    wrap(client).startWriteJob(bucketName, objects);
            final GetJobSpectraS3Response jobResponse = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId()));

            final Date originalDate = jobResponse.getMasterObjectListResult().getStartDate();
            final Date newDate = new Date(originalDate.getTime() - 1000);
            LOG.info("date: " + newDate);

            client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(job.getJobId())
                    .withCreatedAt(newDate));

            final GetJobSpectraS3Response responseAfterModify = client
                    .getJobSpectraS3(new GetJobSpectraS3Request(job.getJobId()));

            assertThat(responseAfterModify.getMasterObjectListResult().getStartDate(), is(newDate));

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void cancelJob() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "test_cancel_job";
        try {
            client.putBucket(new PutBucketRequest(bucketName));

            final List<Ds3Object> objectsOne = new ArrayList<>();
            final Ds3Object obj = new Ds3Object("testOne", 2);
            objectsOne.add(obj);

            final Ds3ClientHelpers.Job job =
                    wrap(client).startWriteJob(bucketName, objectsOne);
            final UUID jobId = job.getJobId();

            final CancelJobSpectraS3Response response = client
                    .cancelJobSpectraS3(new CancelJobSpectraS3Request(jobId));
            response.checkStatusCode(204);

            assertTrue(client.getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request())
                    .getJobListResult().getJobs().isEmpty());

        } finally {
            deleteAllContents(client, bucketName);
        }
    }

    @Test
    public void cancelJobWithForce() throws IOException, SignatureException, XmlProcessingException, URISyntaxException {
        final String bucketName = "test_cancel_job";
        try {
            client.putBucket(new PutBucketRequest(bucketName));

            final List<Ds3Object> objects = new ArrayList<>();
            final Ds3Object objOne = new Ds3Object("testOne", 2);
            objects.add(objOne);
            final Ds3Object obj = new Ds3Object("testTwo", 2);
            objects.add(objOne);

            final Ds3ClientHelpers.Job job =
                    wrap(client).startWriteJob(bucketName, objects);
            job.transfer(new ResourceObjectPutter)
            final UUID jobId = job.getJobId();

            final CancelJobSpectraS3Response response = client
                    .cancelJobSpectraS3(new CancelJobSpectraS3Request(jobId));
            response.checkStatusCode(204);

            assertTrue(client.getActiveJobsSpectraS3(new GetActiveJobsSpectraS3Request())
                    .getJobListResult().getJobs().isEmpty());

        } finally {
            deleteAllContents(client, bucketName);
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

            final Ds3ClientHelpers.Job jobOne =
                    wrap(client).startWriteJob(bucketName, objectsOne);

            final List<Ds3Object> objectsTwo = new ArrayList<>();
            final Ds3Object objTwo = new Ds3Object("testTwo", 2);
            objectsTwo.add(objTwo);

            final Ds3ClientHelpers.Job jobTwo =
                    wrap(client).startWriteJob(bucketName, objectsTwo);

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

                final Ds3ClientHelpers.Job jobOne =
                        wrap(client).startWriteJob(bucketName, objectsOne);
                final UUID jobOneId = jobOne.getJobId();

                client.cancelAllJobsSpectraS3(new CancelAllJobsSpectraS3Request());

                final GetCanceledJobsSpectraS3Response getCanceledJobsResponse = client
                        .getCanceledJobsSpectraS3(new GetCanceledJobsSpectraS3Request());

                ArrayList<UUID> canceledJobsUUIDs = new ArrayList<>();
                for (CanceledJob job : getCanceledJobsResponse.getCanceledJobListResult().getCanceledJobs()) {
                    if (job.getId().equals(jobOneId)){
                        return;
                    }
                }

                assertTrue(false);

            } finally {
                deleteAllContents(client, bucketName);
            }
    }
}
