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
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.LazyObjectIterable;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.SignatureException;
import java.util.Iterator;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static com.spectralogic.ds3client.integration.Util.loadBookTestData;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LazyIterator_Test {
    private static final Logger LOG = LoggerFactory.getLogger(LazyIterator_Test.class);

    private static final Ds3Client CLIENT = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(CLIENT);
    private static final String TEST_ENV_NAME = "lazy_iterator_test";
    private static TempStorageIds envStorageIds;
    private static UUID envDataPolicyId;

    @BeforeClass
    public static void startup() throws IOException, SignatureException {
        LOG.info("Starting test Setup...");
        envDataPolicyId = TempStorageUtil.setupDataPolicy(TEST_ENV_NAME, false, ChecksumType.Type.MD5, CLIENT);
        envStorageIds = TempStorageUtil.setup(TEST_ENV_NAME, envDataPolicyId, CLIENT);
        LOG.info("Finished test Setup...");
    }

    @AfterClass
    public static void teardown() throws IOException, SignatureException {
        LOG.info("Starting test teardown...");
        TempStorageUtil.teardown(TEST_ENV_NAME, envStorageIds, CLIENT);
        CLIENT.close();
        LOG.info("Finished test teardown...");
    }

    @Test
    public void emptyTest() throws IOException, SignatureException {
        HELPERS.ensureBucketExists(TEST_ENV_NAME, envDataPolicyId);
        try {
            final String prefix = "";
            final String nextMarker = null;
            final int maxKeys = 100;


            final LazyObjectIterable iterable = new LazyObjectIterable(CLIENT, TEST_ENV_NAME, prefix, nextMarker, maxKeys);
            final Iterator<Contents> iterator = iterable.iterator();

            assertFalse(iterator.hasNext());

        } finally {
            deleteAllContents(CLIENT, TEST_ENV_NAME);
        }
    }

    @Test
    public void singlePageTest() throws IOException, XmlProcessingException, SignatureException, URISyntaxException {
        HELPERS.ensureBucketExists(TEST_ENV_NAME, envDataPolicyId);
        loadBookTestData(CLIENT, TEST_ENV_NAME);
        try {
            final String prefix = "";
            final String nextMarker = null;
            final int maxKeys = 100;

            final LazyObjectIterable iterable = new LazyObjectIterable(CLIENT, TEST_ENV_NAME, prefix, nextMarker, maxKeys);
            final Iterator<Contents> iterator = iterable.iterator();

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

    @Test
    public void multiPageTest() throws IOException, URISyntaxException, XmlProcessingException {
            HELPERS.ensureBucketExists(TEST_ENV_NAME, envDataPolicyId);
        loadBookTestData(CLIENT, TEST_ENV_NAME);
        try {
            final String prefix = "";
            final String nextMarker = null;
            final int maxKeys = 2;

            final LazyObjectIterable iterable = new LazyObjectIterable(CLIENT, TEST_ENV_NAME, prefix, nextMarker, maxKeys);
            final Iterator<Contents> iterator = iterable.iterator();

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
}
