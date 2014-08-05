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
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

import java.io.*;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.spectralogic.ds3client.models.bulk.BulkObject;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.MasterObjectList;
import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.utils.Md5Hash;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectGetter;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectPutter;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ReadJob;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.WriteJob;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

public class Ds3ClientHelpers_Test {
    private static final String MYBUCKET = "mybucket";

    @Test
    public void testReadObjects() throws SignatureException, IOException, XmlProcessingException {
        final Ds3Client ds3Client = mock(Ds3Client.class);
        when(ds3Client.bulkGet(any(BulkGetRequest.class))).thenReturn(new StubBulkGetResponse());
        when(ds3Client.getObject(getRequestHas("foo"))).thenReturn(new StubGetObjectResponse("foo contents"));
        when(ds3Client.getObject(getRequestHas("bar"))).thenReturn(new StubGetObjectResponse("bar contents"));
        when(ds3Client.getObject(getRequestHas("baz"))).thenReturn(new StubGetObjectResponse("baz contents"));
        
        final List<Ds3Object> objectsToGet = Lists.newArrayList(
            new Ds3Object("foo"),
            new Ds3Object("bar"),
            new Ds3Object("baz")
        );
        final ReadJob readJob = Ds3ClientHelpers.wrap(ds3Client).startReadJob(MYBUCKET, objectsToGet);
        
        assertThat(readJob.getJobId(), is(jobId));
        assertThat(readJob.getBucketName(), is(MYBUCKET));
        
        readJob.read(new ObjectGetter() {
            @Override
            public void writeContents(final String key, final InputStream contents, final Md5Hash md5) throws IOException {
                assertThat(streamToString(contents), is(key + " contents"));
            }
        });
        
        final InOrder clientInOrder = inOrder(ds3Client);
        clientInOrder.verify(ds3Client).bulkGet(any(BulkGetRequest.class));
        clientInOrder.verify(ds3Client).getObject(getRequestHas("baz"));
        clientInOrder.verify(ds3Client).getObject(getRequestHas("foo"));
        clientInOrder.verify(ds3Client).getObject(getRequestHas("bar"));
        clientInOrder.verifyNoMoreInteractions();
    }
    
    @Test
    public void testWriteObjects() throws SignatureException, IOException, XmlProcessingException {
        final Ds3Client ds3Client = mock(Ds3Client.class);
        when(ds3Client.bulkPut(any(BulkPutRequest.class))).thenReturn(new StubBulkPutResponse());
        when(ds3Client.putObject(any(PutObjectRequest.class))).thenReturn(new StubPutObjectResponse());
        
        final Iterable<Ds3Object> objectsToPut = Lists.newArrayList(
                new Ds3Object("foo", 12),
                new Ds3Object("bar", 12),
                new Ds3Object("baz", 12)
        );
        final WriteJob job = Ds3ClientHelpers.wrap(ds3Client).startWriteJob(MYBUCKET, objectsToPut);
        
        assertThat(job.getJobId(), is(jobId));
        assertThat(job.getBucketName(), is(MYBUCKET));
        
        job.write(new ObjectPutter() {
            @Override
            public InputStream getContent(final String key) {
                return streamFromString(key + " contents");
            }
        });
        
        final InOrder clientInOrder = inOrder(ds3Client);
        clientInOrder.verify(ds3Client).bulkPut(any(BulkPutRequest.class));
        clientInOrder.verify(ds3Client).putObject(putRequestHas("baz", "baz contents"));
        clientInOrder.verify(ds3Client).putObject(putRequestHas("foo", "foo contents"));
        clientInOrder.verify(ds3Client).putObject(putRequestHas("bar", "bar contents"));
        clientInOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testListObjects() throws SignatureException, IOException, XmlProcessingException {
        final Ds3Client ds3Client = mock(Ds3Client.class);
        when(ds3Client.getBucket(getBucketHas(null))).thenReturn(new StubGetBucketResponse(0));
        when(ds3Client.getBucket(getBucketHas("baz"))).thenReturn(new StubGetBucketResponse(1));
        
        // Call the list objects method.
        final List<Contents> contentList = Lists.newArrayList(Ds3ClientHelpers.wrap(ds3Client).listObjects(MYBUCKET));
        
        // Check the results.
        assertThat(contentList.size(), is(3));
        checkContents(contentList.get(0), "foo", "2cde576e5f5a613e6cee466a681f4929", "2009-10-12T17:50:30.000Z", 12);
        checkContents(contentList.get(1), "bar", "f3f98ff00be128139332bcf4b772be43", "2009-10-14T17:50:31.000Z", 12);
        checkContents(contentList.get(2), "baz", "802d45fcb9a3f7d00f1481362edc0ec9", "2009-10-18T17:50:35.000Z", 12);
    }

    @Test
    public void testReadObjectsWithFailedPut() throws SignatureException, IOException, XmlProcessingException {
        final Ds3Client ds3Client = mock(Ds3Client.class);
        when(ds3Client.bulkGet(any(BulkGetRequest.class))).thenReturn(new StubBulkGetResponse());
        when(ds3Client.getObject(getRequestHas("foo"))).thenReturn(new StubFailedGetObjectResponse());
        when(ds3Client.getObject(getRequestHas("bar"))).thenReturn(new StubFailedGetObjectResponse());
        when(ds3Client.getObject(getRequestHas("baz"))).thenReturn(new StubGetObjectResponse("baz contents"));
        
        // Build input list.
        final ArrayList<Ds3Object> objectsToGet = Lists.newArrayList(
            new Ds3Object("foo"),
            new Ds3Object("bar"),
            new Ds3Object("baz")
        );
        
        final ReadJob job = Ds3ClientHelpers.wrap(ds3Client).startReadJob(MYBUCKET, objectsToGet);

        try {
            job.read(new ObjectGetter() {
                @Override
                public void writeContents(final String key, final InputStream contents, final Md5Hash md5) throws IOException {
                    // We don't care about the contents since we just want to know that the exception handling works correctly.
                }
            });
        } catch (final StubException e) {
            // This is what we want.
            return;
        }
        Assert.fail("Should have failed with an exception before we got here.");
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

    private static GetObjectRequest getRequestHas(final String key) {
        return argThat(new ArgumentMatcher<GetObjectRequest>() {
            @Override
            public boolean matches(final Object argument) {
                if (!(argument instanceof GetObjectRequest)) {
                    return false;
                }
                final GetObjectRequest getObjectRequest = (GetObjectRequest)argument;
                return
                        getObjectRequest.getBucketName().equals(MYBUCKET)
                        && getObjectRequest.getObjectName().equals(key);
            }
        });
    }

    private static PutObjectRequest putRequestHas(final String key, final String contents) {
        return argThat(new ArgumentMatcher<PutObjectRequest>() {
            @Override
            public boolean matches(final Object argument) {
                if (!(argument instanceof PutObjectRequest)) {
                    return false;
                }
                final PutObjectRequest putObjectRequest = (PutObjectRequest)argument;
                final InputStream stream = putObjectRequest.getStream();
                return
                        putObjectRequest.getBucketName().equals(MYBUCKET)
                        && putObjectRequest.getObjectName().equals(key)
                        && streamToString(stream).equals(contents);
            }
        });
    }
    
    private static GetBucketRequest getBucketHas(final String marker) {
        return argThat(new ArgumentMatcher<GetBucketRequest>() {
            @Override
            public boolean matches(final Object argument) {
                if (!(argument instanceof GetBucketRequest)) {
                    return false;
                }
                final GetBucketRequest getBucketRequest = ((GetBucketRequest)argument);
                return
                        getBucketRequest.getBucket().equals(MYBUCKET)
                        && (marker == null
                            ? null == getBucketRequest.getNextMarker()
                            : marker.equals(getBucketRequest.getNextMarker()));
                
            }
        });
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
            listBucketResult.setName(MYBUCKET);
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

    private static UUID jobId = new UUID(0x0123456789abcdefl, 0xfedcba9876543210l);

    private static MasterObjectList buildMasterObjectList() {
        final MasterObjectList masterObjectList = new MasterObjectList();
        masterObjectList.setJobId(jobId);
        masterObjectList.setObjects(Arrays.asList(
            makeObjects("192.168.56.100", Arrays.asList(new BulkObject("baz", 12, false, 0))),
            makeObjects("192.168.56.100", Arrays.asList(new BulkObject("foo", 12, false, 0))),
            makeObjects("192.168.56.101", Arrays.asList(new BulkObject("bar", 12, false, 0)))
        ));
        return masterObjectList;
    }

    private static Objects makeObjects(final String serverId, final List<BulkObject> objectList) {
        final Objects objects = new Objects();
        objects.setServerId(serverId);
        objects.setObjects(objectList);
        return objects;
    }
    
    private static final class StubException extends RuntimeException {
        private static final long serialVersionUID = 5121719894916333278L;
    }
    
    private static final class StubFailedGetObjectResponse extends GetObjectResponse {
        public StubFailedGetObjectResponse() throws IOException {
            super(null);
        }

        @Override
        protected void processResponse() throws IOException {
        }
        
        @Override
        public void close() throws IOException {
        }
        
        @Override
        public InputStream getContent() {
            throw new StubException();
        }
    }
    
    private static final class StubGetObjectResponse extends GetObjectResponse {
        private final String content;

        public StubGetObjectResponse(final String content) throws IOException {
            super(null);
            this.content = content;
        }

        @Override
        protected void processResponse() throws IOException {
        }
        
        @Override
        public void close() throws IOException {
        }
        
        @Override
        public InputStream getContent() {
            return streamFromString(this.content);
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
            content.reset();
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
