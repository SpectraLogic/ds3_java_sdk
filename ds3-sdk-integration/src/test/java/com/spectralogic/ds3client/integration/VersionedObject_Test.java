/*
 * ******************************************************************************
 *   Copyright 2014-2018 Spectra Logic Corporation. All Rights Reserved.
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
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.VersioningLevel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.*;

public class VersionedObject_Test {

    private static final Logger LOG = LoggerFactory.getLogger(SpectraS3PaginationLoader_Test.class);

    private static final Ds3Client CLIENT = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(CLIENT);
    private static final String TEST_ENV_NAME = "java_versioned_object_test";
    private static final int RETRIES = 5;

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

            // TODO Get the version of the objects

            // TODO Create bulk get job with both versions of object specified


        } finally {
            cancelAllJobsForBucket(CLIENT, TEST_ENV_NAME);
            deleteAllContents(CLIENT, TEST_ENV_NAME);
        }
    }
}
