/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

import mockit.FullVerificationsInOrder;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.apache.commons.io.IOUtils;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.BulkGetRequest;
import com.spectralogic.ds3client.commands.BulkGetResponse;
import com.spectralogic.ds3client.commands.BulkPutRequest;
import com.spectralogic.ds3client.commands.BulkPutResponse;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.commands.GetBucketResponse;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.commands.PutObjectResponse;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectGetter;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectPutter;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.Owner;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

public class Ds3ClientHelpers_Test {
    @Mocked
    private Ds3Client ds3Client;
    private static final int THREAD_COUNT = 10;
    
    @Test
    public void testReadObjects() throws SignatureException, IOException, XmlProcessingException {
        new NonStrictExpectations() {{
            // Mock the bulk get method.
            Ds3ClientHelpers_Test.this.ds3Client.bulkGet(this.withInstanceOf(BulkGetRequest.class));
            result = new StubBulkGetResponse();
            
            // Mock the get object method.
            Ds3ClientHelpers_Test.this.ds3Client.getObject(this.withInstanceOf(GetObjectRequest.class));
            this.returns(
                new StubGetObjectResponse(0),
                new StubGetObjectResponse(1),
                new StubGetObjectResponse(2)
            );
            times = 3;
        }};
        
        // Build input list.
        final ArrayList<Ds3Object> objectsToGet = Lists.newArrayList(
            new Ds3Object("foo"),
            new Ds3Object("bar"),
            new Ds3Object("baz")
        );

        // Create a list to keep track of the keys written.
        final List<String> objectsGotten = new ArrayList<String>();
        
        // Run readObjects.
        final int objectCount = new Ds3ClientHelpers(this.ds3Client)
            .readObjects("mybucket", objectsToGet, new ObjectGetterImplementation(objectsGotten));
        
        // Check the results, including the call order.
        assertThat(objectCount, is(3));
        
        new FullVerificationsInOrder() {{
            Ds3ClientHelpers_Test.this.ds3Client.bulkGet(this.withInstanceOf(BulkGetRequest.class));
            Ds3ClientHelpers_Test.this.ds3Client.getObject(this.withInstanceOf(GetObjectRequest.class));
            Ds3ClientHelpers_Test.this.ds3Client.getObject(this.withInstanceOf(GetObjectRequest.class));
            Ds3ClientHelpers_Test.this.ds3Client.getObject(this.withInstanceOf(GetObjectRequest.class));
        }};
    }

    @Test
    public void testWriteObjects() throws SignatureException, IOException, XmlProcessingException {
        // Set up the mock responses.
        new NonStrictExpectations() {{
            // Mock the bulk put method.
            Ds3ClientHelpers_Test.this.ds3Client.bulkPut(this.withInstanceOf(BulkPutRequest.class));
            result = new StubBulkPutResponse();
            
            // Mock the put object method.
            Ds3ClientHelpers_Test.this.ds3Client.putObject(this.withInstanceOf(PutObjectRequest.class));
            result = new StubPutObjectResponse();
            times = 3;
        }};
        
        // Perform the bulk put.
        final Iterable<Ds3Object> objectsToPut = Lists.newArrayList(
                new Ds3Object("foo", 12),
                new Ds3Object("bar", 12),
                new Ds3Object("baz", 12)
        );
        final int objectCount = new Ds3ClientHelpers(this.ds3Client).writeObjects("mybucket", objectsToPut, new ObjectPutter() {
            @Override
            public InputStream getContent(final String key) {
                return streamFromString(key + " contents");
            }
        });
        
        // Assert the return value.
        assertThat(objectCount, is(3));
        
        // Verify the ds3 calls.
        new FullVerificationsInOrder() {{
            Ds3ClientHelpers_Test.this.ds3Client.bulkPut(this.withInstanceOf(BulkPutRequest.class));
            Ds3ClientHelpers_Test.this.ds3Client.putObject(this.<PutObjectRequest>with(new PutObjectRequestMatcher("baz", "baz contents")));
            Ds3ClientHelpers_Test.this.ds3Client.putObject(this.<PutObjectRequest>with(new PutObjectRequestMatcher("foo", "foo contents")));
            Ds3ClientHelpers_Test.this.ds3Client.putObject(this.<PutObjectRequest>with(new PutObjectRequestMatcher("bar", "bar contents")));
        }};
    }

    @Test
    public void testListObjects() throws SignatureException, IOException, XmlProcessingException {
        new NonStrictExpectations() {{
            // Mock the bulk get method.
            Ds3ClientHelpers_Test.this.ds3Client.getBucket(this.withInstanceOf(GetBucketRequest.class));
            this.returns(
                new StubGetBucketResponse(0),
                new StubGetBucketResponse(1)
            );
        }};
        
        // Create the thread pool.
        final ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(THREAD_COUNT));

        // Call the list objects method.
        final List<Contents> contentList = Lists.newArrayList(new Ds3ClientHelpers(this.ds3Client).listObjects("mybucket"));
        
        // Check the results.
        assertThat(contentList.size(), is(3));
        checkContents(contentList.get(0), "foo", "2cde576e5f5a613e6cee466a681f4929", "2009-10-12T17:50:30.000Z", 12);
        checkContents(contentList.get(1), "bar", "f3f98ff00be128139332bcf4b772be43", "2009-10-14T17:50:31.000Z", 12);
        checkContents(contentList.get(2), "baz", "802d45fcb9a3f7d00f1481362edc0ec9", "2009-10-18T17:50:35.000Z", 12);
        
        // Shut down the thread pool.
        service.shutdown();
    }
    
    private static void checkContents(
            final Contents contents,
            final String key,
            final String eTag,
            final String lastModified,
            final long size) {
        assertThat(contents.getKey(), is(key));
        assertThat(contents.geteTag(), is(eTag));
        assertThat(contents.getLastModified(), is(lastModified));
        assertThat(contents.getSize(), is(size));
    }
    
    private final class PutObjectRequestMatcher extends BaseMatcher<PutObjectRequest> {
        private final String key;
        private final String contents;

        public PutObjectRequestMatcher(final String key, final String contents) {
            this.key = key;
            this.contents = contents;
        }
        
        @Override
        public boolean matches(final Object item) {
            if (item instanceof PutObjectRequest) {
                final PutObjectRequest request = (PutObjectRequest) item;
                return
                    request.getPath().substring("/mybucket/".length()).equals(this.key)
                    && request.getSize() == this.contents.length()
                    && streamToString(request.getStream()).equals(this.contents);
            } else {
                return false;
            }
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("key: " + this.key + "\ncontents: " + this.contents);
        }
    }

    private final class ObjectGetterImplementation implements ObjectGetter {
        private final List<String> objectsGotten;

        private ObjectGetterImplementation(final List<String> objectsGotten) {
            this.objectsGotten = objectsGotten;
        }

        @Override
        public void writeContents(final String key, final InputStream contents) throws IOException {
            // Check the content.
            assertThat(streamToString(contents), is(key + " contents"));
            
            // Record the key.
            this.objectsGotten.add(key);
        }
    }
    
    private static final class StubGetBucketResponse extends GetBucketResponse {
        private final int invocationIndex;

        public StubGetBucketResponse(final int invocationIndex) throws IOException {
            super(null);
            this.invocationIndex = invocationIndex;
        }
        
        @Override
        protected void processResponse() throws IOException {
        }
        
        @Override
        public void close() throws IOException {
        }

        @Override
        public ListBucketResult getResult() {
            switch (this.invocationIndex) {
            case 0: return buildListBucketResult(buildContentList0(), "", "baz", true);
            case 1: return buildListBucketResult(buildContentList1(), "baz", "", false);
            default:
                Assert.fail("List objects in bucket called too many times");
                return null;
            }
        }

        private static ListBucketResult buildListBucketResult(
                final List<Contents> contentList,
                final String marker,
                final String nextMarker,
                final boolean isTruncated) {
            final ListBucketResult listBucketResult = new ListBucketResult();
            listBucketResult.setContentsList(contentList);
            listBucketResult.setCreationDate("");
            listBucketResult.setDelimiter("");
            listBucketResult.setMarker(marker);
            listBucketResult.setMaxKeys(2);
            listBucketResult.setName("mybucket");
            listBucketResult.setNextMarker(nextMarker);
            listBucketResult.setPrefix("");
            listBucketResult.setTruncated(isTruncated);
            return listBucketResult;
        }
        
        private static List<Contents> buildContentList0() {
            return Lists.newArrayList(
                buildContents("foo", "2cde576e5f5a613e6cee466a681f4929", "2009-10-12T17:50:30.000Z", 12),
                buildContents("bar", "f3f98ff00be128139332bcf4b772be43", "2009-10-14T17:50:31.000Z", 12)
            );
        }
        
        private static List<Contents> buildContentList1() {
            return Lists.newArrayList(
                buildContents("baz", "802d45fcb9a3f7d00f1481362edc0ec9", "2009-10-18T17:50:35.000Z", 12)
            );
        }
        
        private static Contents buildContents(
                final String key,
                final String eTag,
                final String lastModified,
                final long size) {
            final Contents contents = new Contents();
            contents.seteTag(eTag);
            contents.setKey(key);
            contents.setLastModified(lastModified);
            final Owner owner = new Owner();
            owner.setDisplayName("person@spectralogic.com");
            owner.setId("75aa57f09aa0c8caeab4f8c24e99d10f8e7faeebf76c078efc7c6caea54ba06a");
            contents.setOwner(owner);
            contents.setSize(size);
            contents.setStorageClass("STANDARD");
            return contents;
        }
    }
    
    private static final class StubBulkGetResponse extends BulkGetResponse {
        public StubBulkGetResponse() throws IOException {
            super(null);
        }

        @Override
        protected void processResponse() throws IOException {
        }
        
        @Override
        public void close() throws IOException {
        }
        
        @Override
        public MasterObjectList getResult() {
            return buildMasterObjectList();
        }
    }
    
    private static final class StubBulkPutResponse extends BulkPutResponse {
        public StubBulkPutResponse() throws IOException {
            super(null);
        }

        @Override
        protected void processResponse() throws IOException {
        }
        
        @Override
        public void close() throws IOException {
        }
        
        @Override
        public MasterObjectList getResult() {
            return buildMasterObjectList();
        }
    }

    private static MasterObjectList buildMasterObjectList() {
        final MasterObjectList masterObjectList = new MasterObjectList();
        masterObjectList.setJobid(new UUID(0x0123456789abcdefl, 0xfedcba9876543210l));
        masterObjectList.setObjects(makeObjectLists());
        return masterObjectList;
    }

    private static List<Objects> makeObjectLists() {
        final List<Objects> objectLists = new ArrayList<Objects>();
        objectLists.add(makeObjects1());
        return objectLists;
    }

    private static Objects makeObjects1() {
        final Objects objects1 = new Objects();
        objects1.setServerid("192.168.56.100");
        objects1.setObject(makeObjectList1());
        return objects1;
    }

    private static ArrayList<Ds3Object> makeObjectList1() {
        final ArrayList<Ds3Object> objectList1 = new ArrayList<Ds3Object>();
        objectList1.add(new Ds3Object("baz", 12));
        objectList1.add(new Ds3Object("foo", 12));
        objectList1.add(new Ds3Object("bar", 12));
        return objectList1;
    }
    
    private static final class StubGetObjectResponse extends GetObjectResponse {
        private final int invocationIndex;

        public StubGetObjectResponse(final int invocationIndex) throws IOException {
            super(null);
            this.invocationIndex = invocationIndex;
        }

        @Override
        protected void processResponse() throws IOException {
        }
        
        @Override
        public void close() throws IOException {
        }
        
        @Override
        public InputStream getContent() {
            final String[] contentItems = {
                "baz contents",
                "foo contents",
                "bar contents"
            };
            return streamFromString(contentItems[this.invocationIndex]);
        }
    }
    
    private static final class StubPutObjectResponse extends PutObjectResponse {

        public StubPutObjectResponse() throws IOException {
            super(null);
        }

        @Override
        protected void processResponse() throws IOException {
        }
        
        @Override
        public void close() throws IOException {
        }
    }

    private static String streamToString(final InputStream content) {
        final StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(content, writer, "UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    private static InputStream streamFromString(final String contents) {
        try {
            return new ByteArrayInputStream(contents.getBytes("UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
