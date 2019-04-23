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

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.helpers.DeleteBucket;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.channelbuilders.PrefixAdderObjectChannelBuilder;
import com.spectralogic.ds3client.models.Bucket;
import com.spectralogic.ds3client.models.Job;
import com.spectralogic.ds3client.models.JobStatus;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeThat;
import static org.junit.Assume.assumeTrue;


public final class Util {
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);
    public static final String RESOURCE_BASE_NAME = "books/";
    public static final String[] BOOKS = {"beowulf.txt", "sherlock_holmes.txt", "tale_of_two_cities.txt", "ulysses.txt"};

    private Util() {}

    public static Ds3Client fromEnv() {
        final Ds3ClientBuilder builder = Ds3ClientBuilder.fromEnv();
        builder.withHttps(false);
        return builder.build();
    }

    public static Ds3Client insecureFromEnv() {
        final Ds3ClientBuilder builder = Ds3ClientBuilder.fromEnv();
        builder.withCertificateVerification(false);
        return builder.build();
    }

    /**
     * Goes through all buckets on a BP and attempts to delete them and all their contents. It cancels all active jobs
     * associated with this bucket. Created for cleaning up after cascade failures in functional tests.
     *
     * USE ONLY WHEN DELETING ALL BUCKETS AND ALL CONTENT IS YOUR GOAL.
     */
    public static void forceCleanupBucketsOnBP(final Ds3Client client) throws IOException {
        final GetBucketsSpectraS3Request request = new GetBucketsSpectraS3Request();
        final GetBucketsSpectraS3Response response = client.getBucketsSpectraS3(request);

        for (final Bucket bucket : response.getBucketListResult().getBuckets()) {
            cancelAllJobsForBucket(client, bucket.getName());
            client.deleteBucketSpectraS3(new DeleteBucketSpectraS3Request(bucket.getName()).withForce(true));
        }
    }

    public static void assumeVersionOneDotTwo(final Ds3Client client) throws IOException {
        final int majorVersion = Integer.parseInt(client.getSystemInformationSpectraS3(
                new GetSystemInformationSpectraS3Request()).getSystemInformationResult().getBuildInformation().getVersion().split("\\.")[0]);
        assumeThat(majorVersion, is(1));
    }

    public static void assumeVersion4orHigher(final Ds3Client client) throws IOException {
        final int majorVersion = Integer.parseInt(client.getSystemInformationSpectraS3(
                new GetSystemInformationSpectraS3Request()).getSystemInformationResult().getBuildInformation().getVersion().split("\\.")[0]);
        assumeTrue(majorVersion >= 4);
    }

    public static void loadBookTestData(final Ds3Client client, final String bucketName) throws IOException, URISyntaxException {
        LOG.info("Loading test data...");
        getLoadJob(client, bucketName, RESOURCE_BASE_NAME)
            .transfer(new ResourceObjectPutter(RESOURCE_BASE_NAME));
        LOG.info("Finished loading test data...");
    }

    /**
     * Loads a single test book to a BP with the specified object name.
     */
    public static void loadTestBook(final Ds3Client client, final String fileName, final String objectName, final String bucketName) throws IOException, URISyntaxException {
        final Path filePath = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + fileName);
        final Ds3Object obj = new Ds3Object(objectName, Files.size(filePath));

        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        final Ds3ClientHelpers.Job job = helpers.startWriteJob(bucketName, ImmutableList.of(obj));
        job.transfer(key -> FileChannel.open(filePath, StandardOpenOption.READ));
    }

    public static Ds3ClientHelpers.Job getLoadJob(final Ds3Client client, final String bucketName, final String resourceBaseName) throws IOException, URISyntaxException {
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        final List<Ds3Object> objects = new ArrayList<>();

        for(final String book : BOOKS) {
            final Path objPath = ResourceUtils.loadFileResource(resourceBaseName + book);
            final Ds3Object obj = new Ds3Object(book, Files.size(objPath));

            objects.add(obj);
        }

        return helpers
                .startWriteJob(bucketName, objects);
    }

    public static void loadBookTestDataWithPrefix(final Ds3Client client, final String bucketName, final String prefix) throws IOException, URISyntaxException {
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);

        final List<Ds3Object> objects = new ArrayList<>();

        for(final String book : BOOKS) {
            final Path objPath = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + book);
            final Ds3Object obj = new Ds3Object(prefix + book, Files.size(objPath));

            objects.add(obj);
        }

        helpers.startWriteJob(bucketName, objects).transfer(new PrefixAdderObjectChannelBuilder(new ResourceObjectPutter(RESOURCE_BASE_NAME), prefix));
    }

    public static void deleteAllContents(final Ds3Client client, final String bucketName) throws IOException {
        Ds3ClientHelpers.wrap(client).deleteBucket(bucketName);
    }

    public static void deleteBucketContents(final Ds3Client client, final String bucketName) throws IOException {
        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(client);
        DeleteBucket.INSTANCE.deleteBucketContents(helpers, bucketName);
    }

    // Cancels all in-progress jobs associated with a bucket
    public static void cancelAllJobsForBucket(final Ds3Client client, final String bucketName) {
        try {
            final GetJobsSpectraS3Response getJobs = client.getJobsSpectraS3(new GetJobsSpectraS3Request().withBucketId(bucketName));

            for (final Job job : getJobs.getJobListResult().getJobs()) {
                if (job.getStatus() == JobStatus.IN_PROGRESS) {
                    client.cancelJobSpectraS3(new CancelJobSpectraS3Request(job.getJobId()));
                }
            }
        } catch (final IOException e) {
            LOG.debug("Could not cancel jobs for bucket '%s': %s", bucketName, e.getMessage());
        }
    }
}
