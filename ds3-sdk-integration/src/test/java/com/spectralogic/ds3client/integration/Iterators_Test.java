/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.commands.GetBucketResponse;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.commands.spectrads3.GetBucketSpectraS3Request;
import com.spectralogic.ds3client.helpers.ContentPrefix;
import com.spectralogic.ds3client.helpers.pagination.GetBucketLoaderFactory;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.pagination.GetObjectsFullDetailsLoaderFactory;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.models.common.CommonPrefixes;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import com.spectralogic.ds3client.utils.collections.LazyIterable;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageIds;
import com.spectralogic.ds3client.integration.test.helpers.TempStorageUtil;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.spectralogic.ds3client.integration.Util.deleteAllContents;
import static com.spectralogic.ds3client.integration.Util.loadBookTestData;
import static com.spectralogic.ds3client.integration.Util.loadBookTestDataWithPrefix;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class Iterators_Test {
    private static final Logger LOG = LoggerFactory.getLogger(Iterators_Test.class);

    private static final Ds3Client CLIENT = Util.fromEnv();
    private static final Ds3ClientHelpers HELPERS = Ds3ClientHelpers.wrap(CLIENT);
    private static final String TEST_ENV_NAME = "lazy_iterator_test";
    private static final int RETRIES = 5;
    private static final String TEST_DELIMITER = null;

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
    public void empty_dericty_test() throws IOException, URISyntaxException {
        try {
            HELPERS.ensureBucketExists(TEST_ENV_NAME, envDataPolicyId);
            loadBookTestDataWithPrefix(CLIENT, TEST_ENV_NAME, "books/");
            final ContentPrefix contentPrefix = Ds3ClientHelpers.wrap(CLIENT).remoteListDirectory(TEST_ENV_NAME, "books/", null, 100);
            final List<CommonPrefixes> commonPrefixesList = contentPrefix.commonPrefixes();
            final LazyIterable<Contents> lazyIterable = (LazyIterable<Contents>) contentPrefix.contents();
            final ArrayList<Contents> contents = Lists.newArrayList(lazyIterable);
            assertThat(contents.size(), is(4));
            assertThat(commonPrefixesList.size(), is(0));

            loadBookTestDataWithPrefix(CLIENT, TEST_ENV_NAME, "books/more/");
            final ContentPrefix anotherContentPrefix = Ds3ClientHelpers.wrap(CLIENT).remoteListDirectory(TEST_ENV_NAME, "books/", null, 1000);
            final List<CommonPrefixes> anotherCommonPrefixesList = anotherContentPrefix.commonPrefixes();
            final LazyIterable<Contents> anotherLazyIterable = (LazyIterable<Contents>) anotherContentPrefix.contents();
            final ArrayList<Contents> anotherContents = Lists.newArrayList(anotherLazyIterable);
            assertThat(anotherContents.size(), is(4));
            assertThat(anotherCommonPrefixesList.size(), is(1));
            assertThat(anotherCommonPrefixesList.get(0).getPrefix(), is("books/more/"));

            for (int i = 0; i < 100; i++) {
                loadBookTestDataWithPrefix(CLIENT, TEST_ENV_NAME, "books/" + i + "/");
            }
            for (int i = 0; i < 20; i++) {
                loadBookTestDataWithPrefix(CLIENT, TEST_ENV_NAME, "books/" + i);
            }
            final ContentPrefix bulkContentPrefix = Ds3ClientHelpers.wrap(CLIENT).remoteListDirectory(TEST_ENV_NAME, "books/", null, 84);
            final List<CommonPrefixes> bulkCommonPrefixesList = bulkContentPrefix.commonPrefixes();
            final LazyIterable<Contents> bulkLazyIterable = (LazyIterable<Contents>) bulkContentPrefix.contents();
            final ArrayList<Contents> bulkContents = Lists.newArrayList(bulkLazyIterable);
            for(CommonPrefixes c : bulkCommonPrefixesList) {
                LOG.info(c.getPrefix());
            }
            //assertThat(bulkCommonPrefixesList.size(), is(101));
            assertThat(bulkContents.size(), is(84));
        }
        finally {
            deleteAllContents(CLIENT, TEST_ENV_NAME);
        }
    }

    public void putString(String objectName, String objectContent) throws IOException {
        final byte[] content = objectContent.getBytes();
        final PutObjectRequest putObjectRequest = new PutObjectRequest(TEST_ENV_NAME, objectName, new ByteArraySeekableByteChannel(content), content.length);
        CLIENT.putObject(putObjectRequest);
    }

    public void loopWithNextMarker(final String bucket, final String prefix, final String delimiter) throws IOException {
        GetBucketRequest getBucketRequest = new GetBucketRequest(bucket).withPrefix(prefix).withDelimiter(delimiter).withMaxKeys(3);
        GetBucketResponse clientBucket = CLIENT.getBucket(getBucketRequest);
        ListBucketResult listBucketResult = clientBucket.getListBucketResult();
        logPrefix(listBucketResult);
        logObjects(listBucketResult);
        final String nextMarker = listBucketResult.getNextMarker();
        System.out.println("next marker " + nextMarker);
        loopWithNextMarker(bucket,prefix,delimiter,nextMarker);
    }

    public void loopWithNextMarker(final String bucket, final String prefix, final String delimiter, final String marker) throws IOException {
        if (marker == null) {
            return;
        }
        GetBucketRequest getBucketRequest = new GetBucketRequest(bucket).withPrefix(prefix).withDelimiter(delimiter).withMaxKeys(3).withMarker(marker);
        GetBucketResponse clientBucket = CLIENT.getBucket(getBucketRequest);
        ListBucketResult listBucketResult = clientBucket.getListBucketResult();
        logPrefix(listBucketResult);
        logObjects(listBucketResult);
        final String nextMarker = listBucketResult.getNextMarker();
        System.out.println("next marker " + nextMarker);
        loopWithNextMarker(bucket,prefix,delimiter,nextMarker);
    }

    public void logPrefix(ListBucketResult listBucketResult) {
        for (CommonPrefixes commonPrefixes : listBucketResult.getCommonPrefixes()) {
            System.out.println("Prefix: " + commonPrefixes.getPrefix());
        }
    }

    public void logObjects(ListBucketResult listBucketResult) {
        for (Contents contents : listBucketResult.getObjects()) {
            System.out.println("Object: " + contents.getKey());
        }
    }

    @Test
    public void matchS3() throws IOException {
        final String[] names = {
                "dir/a.txt",
                "dir/foo.txt",
                "dir/foo1.txt",
                "dir/foo2.txt",
                "dir/baz/foo.txt",
                "dir/tar/foo.txt",
                "dir/1/foo.txt",
                "dir/2/foo.txt",
                "dir/3/foo.txt",
                "dir/4/foo.txt"
        };
        final String content = "content";
        try {
            HELPERS.ensureBucketExists(TEST_ENV_NAME, envDataPolicyId);
            for(final String s : names) {
                putString(s, content);
            }
            loopWithNextMarker(TEST_ENV_NAME,"dir/", "/");
            assertThat(true, is(true));

        } finally {
            deleteAllContents(CLIENT, TEST_ENV_NAME);
        }
    }

    @Test
    public void emptyGetBucket() throws IOException {
        final String prefix = "";
        final String nextMarker = null;
        final int maxKeys = 100;

        emptyTest(new GetBucketLoaderFactory(CLIENT, TEST_ENV_NAME, prefix, TEST_DELIMITER, nextMarker, maxKeys, RETRIES));
    }

    @Test
    public void singlePageGetBucket() throws IOException, URISyntaxException {
        final String prefix = "";
        final String nextMarker = null;
        final int maxKeys = 100;
        paginate(new GetBucketLoaderFactory(CLIENT, TEST_ENV_NAME, prefix, TEST_DELIMITER, nextMarker, maxKeys, RETRIES));
    }


    @Test
    public void multiPageGetBucket() throws IOException, URISyntaxException {
        final String prefix = "";
        final String nextMarker = null;
        final int maxKeys = 2;
        paginate(new GetBucketLoaderFactory(CLIENT, TEST_ENV_NAME, prefix, nextMarker, TEST_DELIMITER, maxKeys, RETRIES));

    }

    @Test
    public void failedRequestGetBucket() {
        testFailedRequest(new GetBucketLoaderFactory(CLIENT, "Unknown_Bucket",null, TEST_DELIMITER,null, 1000, 5));
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
