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

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.commands.GetBucketResponse;
import com.spectralogic.ds3client.commands.spectrads3.DeleteBucketSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetBulkJobSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.StageObjectsJobSpectraS3Request;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.FileObjectGetter;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.VersioningLevel;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.spectralogic.ds3client.integration.Util.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class VersionedObject_Test {

    private static final Logger LOG = LoggerFactory.getLogger(SpectraS3PaginationLoader_Test.class);

    private static final Ds3Client CLIENT = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(CLIENT);
    private static final String TEST_ENV_NAME = "java_versioned_object_test";

    private static TempStorageIds envStorageIds;
    private static UUID envDataPolicyId;

    @BeforeClass
    public static void startup() throws IOException {
        LOG.info("Starting test Setup...");

        // Create data policy with versioning
        envDataPolicyId = TempStorageUtil.setupDataPolicy(
                TEST_ENV_NAME,
                false,
                ChecksumType.Type.MD5,
                VersioningLevel.KEEP_MULTIPLE_VERSIONS,
                CLIENT);

        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, envDataPolicyId, CLIENT);
        LOG.info("Finished test Setup...");
    }

    @AfterClass
    public static void teardown() throws IOException {
        LOG.info("Starting test teardown...");
        TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, CLIENT);
        CLIENT.close();
        LOG.info("Finished test teardown...");
    }

    @Test
    public void getObjectsWithVersioning() throws IOException, URISyntaxException {
        try {
            HELPERS.ensureBucketExists(TEST_ENV_NAME, envDataPolicyId);
            final String objectName = "object_with_versions";

            // Put different content for object twice
            loadTestBook(CLIENT, BOOKS[0], objectName, TEST_ENV_NAME); // putting beowulf as content
            loadTestBook(CLIENT, BOOKS[1], objectName, TEST_ENV_NAME); // putting sherlock holmes as content

            // Get the version of the objects
            final GetBucketRequest getBucketRequest = new GetBucketRequest(TEST_ENV_NAME).withVersions(true);
            final GetBucketResponse getBucketResponse = CLIENT.getBucket(getBucketRequest);

            assertThat(getBucketResponse.getListBucketResult().getObjects().size(), is(0));
            assertThat(getBucketResponse.getListBucketResult().getVersionedObjects().size(), is(2));

            // Create bulk get job with both versions of object specified
            final List<Ds3Object> objects = getBucketResponse.getListBucketResult().getVersionedObjects().stream()
                    .map(obj -> new Ds3Object(obj.getKey(), obj.getVersionId()))
                    .collect(Collectors.toList());

            final GetBulkJobSpectraS3Request getBulkRequest = new GetBulkJobSpectraS3Request(TEST_ENV_NAME, objects);
            CLIENT.getBulkJobSpectraS3(getBulkRequest);

        } finally {
            cancelAllJobsForBucket(CLIENT, TEST_ENV_NAME);
            deleteAllContents(CLIENT, TEST_ENV_NAME);
        }
    }

    @Test
    public void stageObjectsWithVersioning() throws IOException, URISyntaxException {
        try {
            HELPERS.ensureBucketExists(TEST_ENV_NAME, envDataPolicyId);
            final String objectName = "object_with_versions";

            // Put different content for object twice
            loadTestBook(CLIENT, BOOKS[0], objectName, TEST_ENV_NAME); // putting beowulf as content
            loadTestBook(CLIENT, BOOKS[1], objectName, TEST_ENV_NAME); // putting sherlock holmes as content

            // Get the version of the objects
            final GetBucketRequest getBucketRequest = new GetBucketRequest(TEST_ENV_NAME).withVersions(true);
            final GetBucketResponse getBucketResponse = CLIENT.getBucket(getBucketRequest);

            assertThat(getBucketResponse.getListBucketResult().getObjects().size(), is(0));
            assertThat(getBucketResponse.getListBucketResult().getVersionedObjects().size(), is(2));

            // Create bulk get job with both versions of object specified
            final List<Ds3Object> objects = getBucketResponse.getListBucketResult().getVersionedObjects().stream()
                    .map(obj -> new Ds3Object(obj.getKey(), obj.getVersionId()))
                    .collect(Collectors.toList());

            final StageObjectsJobSpectraS3Request stageRequest = new StageObjectsJobSpectraS3Request(TEST_ENV_NAME, objects);
            CLIENT.stageObjectsJobSpectraS3(stageRequest);

        } finally {
            CLIENT.deleteBucketSpectraS3(new DeleteBucketSpectraS3Request(TEST_ENV_NAME).withForce(true));
        }
    }

    @Test
    public void getObjectsWithVersioningUsingHelpers() throws IOException, URISyntaxException, InterruptedException {
        // Create temp directory for files we will retrieve from bp
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), null);

        try {
            HELPERS.ensureBucketExists(TEST_ENV_NAME, envDataPolicyId);
            final String objectName = "object_with_versions";

            // Put different content for object twice
            loadTestBook(CLIENT, BOOKS[0], objectName, TEST_ENV_NAME); // putting beowulf as content
            loadTestBook(CLIENT, BOOKS[1], objectName, TEST_ENV_NAME); // putting sherlock holmes as content

            // Get the version of the objects
            final GetBucketRequest getBucketRequest = new GetBucketRequest(TEST_ENV_NAME).withVersions(true);
            final GetBucketResponse getBucketResponse = CLIENT.getBucket(getBucketRequest);

            assertThat(getBucketResponse.getListBucketResult().getObjects().size(), is(0));
            assertThat(getBucketResponse.getListBucketResult().getVersionedObjects().size(), is(2));

            // Create bulk get job with the oldest version of the object
            final List<Ds3Object> objects = getBucketResponse.getListBucketResult().getVersionedObjects().stream()
                    .filter(obj -> !obj.getIsLatest()) // filters out the latest version of the object
                    .map(obj -> new Ds3Object(obj.getKey(), obj.getVersionId()))
                    .collect(Collectors.toList());

            final Ds3ClientHelpers.Job readJob = HELPERS.startReadJob(TEST_ENV_NAME, objects);

            readJob.transfer(new FileObjectGetter(tempDirectory));

            // Check content is beowulf, which was the first version of content
            final File originalFile = ResourceUtils.loadFileResource(RESOURCE_BASE_NAME + BOOKS[0]).toFile();
            final File fileCopiedFromBP = Paths.get(tempDirectory.toString(), objectName).toFile();
            assertTrue(FileUtils.contentEquals(originalFile, fileCopiedFromBP));

        } finally {
            cancelAllJobsForBucket(CLIENT, TEST_ENV_NAME);
            deleteAllContents(CLIENT, TEST_ENV_NAME);
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }
}
