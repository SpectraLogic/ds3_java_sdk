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

package com.spectralogic.ds3client.integration;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.spectrads3.PutBulkJobSpectraS3Request;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.pagination.GetObjectsFullDetailsLoaderFactory;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.DetailedS3Object;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.utils.collections.LazyIterable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static com.spectralogic.ds3client.integration.Util.insecureFromEnv;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class SpectraS3PaginationLoader_Test {

    private static final Logger LOG = LoggerFactory.getLogger(SpectraS3PaginationLoader_Test.class);

    private static final Ds3Client CLIENT = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(CLIENT);
    private static final String TEST_ENV_NAME = "s3_pagination_test";
    private static final int RETRIES = 5;

    private static TempStorageIds envStorageIds;
    private static UUID envDataPolicyId;

    @BeforeClass
    public static void startup() throws IOException {
        LOG.info("Starting test Setup...");
        envDataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, false, ChecksumType.Type.MD5, CLIENT);
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
    public void basicPagination() throws IOException {
        try {
            // test setup
            HELPERS.ensureBucketExists(TEST_ENV_NAME);
            final int numObjects = 50;
            CLIENT.putBulkJobSpectraS3(new PutBulkJobSpectraS3Request(TEST_ENV_NAME, createTestList(numObjects)));

            final GetObjectsFullDetailsLoaderFactory loaderFactory = new GetObjectsFullDetailsLoaderFactory(CLIENT, TEST_ENV_NAME, "", 10, RETRIES, false);

            final Set<String> foundItems = new HashSet<>();

            for (final DetailedS3Object detailedS3Object : new LazyIterable<>(loaderFactory)) {
                LOG.info("Testing: {}", detailedS3Object.getName());
                assertFalse(foundItems.contains(detailedS3Object.getName()));
                foundItems.add(detailedS3Object.getName());
            }

            for (int i = 0; i < numObjects; i++) {
                assertTrue(foundItems.contains("obj." + i));
            }

        } finally {
            deleteAllContents(CLIENT, TEST_ENV_NAME);
        }
    }

    private List<Ds3Object> createTestList(final int numObjects) {
        final ImmutableList.Builder<Ds3Object> builder = ImmutableList.builder();
        for (int i = 0; i < numObjects; i++) {
            builder.add(new Ds3Object("obj." + i, i+1));
        }

        return builder.build();
    }
}
