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

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.helpers.pagination.GetBucketLoaderFactory;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.pagination.GetObjectsFullDetailsLoaderFactory;
import com.spectralogic.ds3client.utils.collections.LazyIterable;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.networking.FailedRequestException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static com.spectralogic.ds3client.integration.Util.loadBookTestData;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class Iterators_Test {
    private static final Logger LOG = LoggerFactory.getLogger(Iterators_Test.class);

    private static final Ds3Client CLIENT = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(CLIENT);
    private static final String TEST_ENV_NAME = "lazy_iterator_test";
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
    public void emptyGetBucket() throws IOException {
        final String prefix = "";
        final String nextMarker = null;
        final int maxKeys = 100;

        emptyTest(new GetBucketLoaderFactory(CLIENT, TEST_ENV_NAME, prefix, nextMarker, maxKeys, RETRIES));
    }

    @Test
    public void singlePageGetBucket() throws IOException, URISyntaxException {
        final String prefix = "";
        final String nextMarker = null;
        final int maxKeys = 100;
        paginate(new GetBucketLoaderFactory(CLIENT, TEST_ENV_NAME, prefix, nextMarker, maxKeys, RETRIES));
    }


    @Test
    public void multiPageGetBucket() throws IOException, URISyntaxException {
        final String prefix = "";
        final String nextMarker = null;
        final int maxKeys = 2;
        paginate(new GetBucketLoaderFactory(CLIENT, TEST_ENV_NAME, prefix, nextMarker, maxKeys, RETRIES));

    }

    @Test
    public void failedRequestGetBucket() {
        testFailedRequest(new GetBucketLoaderFactory(CLIENT, "Unknown_Bucket",null, null, 1000, 5));
    }

   @Test
    public void emptyGetObjects() throws IOException {
        emptyTest(new GetObjectsFullDetailsLoaderFactory(CLIENT, TEST_ENV_NAME, "", 10, RETRIES, true));
    }

    @Test
    public void singlePageGetObjectsIterator() throws IOException, URISyntaxException {
        paginate(new GetObjectsFullDetailsLoaderFactory(CLIENT, TEST_ENV_NAME, "", 10, RETRIES, true));
    }

    @Test
    public void failedGetObjectsWithFullDetails() throws IOException, URISyntaxException {
        testFailedRequest(new GetObjectsFullDetailsLoaderFactory(CLIENT, TEST_ENV_NAME, "", 10, RETRIES, true));
    }

    @Test
    public void multiPageGetObjectsWithFullDetails() throws IOException, URISyntaxException {
        paginate(new GetObjectsFullDetailsLoaderFactory(CLIENT, TEST_ENV_NAME, "", 2, RETRIES, true));
    }

    private void emptyTest(final LazyIterable.LazyLoaderFactory<?> lazyLoaderFactory) throws IOException {
        HELPERS.ensureBucketExists(TEST_ENV_NAME, envDataPolicyId);
        try {
            final LazyIterable<?> iterable = new LazyIterable<>(lazyLoaderFactory);
            final Iterator<?> iterator = iterable.iterator();

            assertFalse(iterator.hasNext());

        } finally {
            deleteAllContents(CLIENT, TEST_ENV_NAME);
        }
    }

    private void paginate(final LazyIterable.LazyLoaderFactory<?> loaderFactory) throws IOException, URISyntaxException {
        HELPERS.ensureBucketExists(TEST_ENV_NAME, envDataPolicyId);
        loadBookTestData(CLIENT, TEST_ENV_NAME);
        try {

            final LazyIterable<?> iterable = new LazyIterable<>(loaderFactory);
            final Iterator<?> iterator = iterable.iterator();

            assertTrue(iterator.hasNext());
            assertThat(iterator.next(), is(notNullValue()));
            assertTrue(iterator.hasNext());
            assertThat(iterator.next(), is(notNullValue()));
            assertTrue(iterator.hasNext());
            assertThat(iterator.next(), is(notNullValue()));
            assertTrue(iterator.hasNext());
            assertThat(iterator.next(), is(notNullValue()));
            assertFalse(iterator.hasNext());
        } finally {
            deleteAllContents(CLIENT, TEST_ENV_NAME);
        }
    }

    private void testFailedRequest(final LazyIterable.LazyLoaderFactory<?> loaderFactory) {
        final LazyIterable<?> iterable = new LazyIterable<>(loaderFactory);
        final Iterator<?> iterator = iterable.iterator();

        boolean threwException = false;

        try {
            iterator.next();
        } catch (final RuntimeException e) {
            threwException = true;
            assertThat(e.getCause(), is(notNullValue()));
            assertThat(e.getCause(), is(instanceOf(FailedRequestException.class)));
            final FailedRequestException fre = (FailedRequestException) e.getCause();
            assertThat(fre.getStatusCode(), either(is(404)).or(is(400)));  // bug opened to correct the DS3 API to correctly return a 404 rather than a 400 in some cases.
        }
        assertTrue("The exception should be thrown", threwException);
    }
}
