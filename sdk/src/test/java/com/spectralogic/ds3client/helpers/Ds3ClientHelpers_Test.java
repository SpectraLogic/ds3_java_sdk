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

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.Job;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectTransferrer;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.models.Owner;
import com.spectralogic.ds3client.models.bulk.BulkObject;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.MasterObjectList;
import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.SignatureException;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;


public class Ds3ClientHelpers_Test {
    private static final String MYBUCKET = "mybucket";

    @Test
    public void testReadObjects() throws SignatureException, IOException, XmlProcessingException {
        final Ds3Client ds3Client = mock(Ds3Client.class);
        when(ds3Client.bulkGet(any(BulkGetRequest.class))).thenReturn(new StubBulkGetResponse());
        when(ds3Client.getObject(getRequestHas("foo"))).then(new GetObjectAnswer("foo"));
        when(ds3Client.getObject(getRequestHas("bar"))).then(new GetObjectAnswer("bar"));
        when(ds3Client.getObject(getRequestHas("baz"))).then(new GetObjectAnswer("baz"));
        
        final List<Ds3Object> objectsToGet = Lists.newArrayList(
            new Ds3Object("foo"),
            new Ds3Object("bar"),
            new Ds3Object("baz")
        );
        final Job readJob = Ds3ClientHelpers.wrap(ds3Client).startReadJob(MYBUCKET, objectsToGet);
        
        assertThat(readJob.getJobId(), is(jobId));
        assertThat(readJob.getBucketName(), is(MYBUCKET));
        
        final HashMap<String, ByteArraySeekableByteChannel> channelMap = new HashMap<>();
        channelMap.put("foo", new ByteArraySeekableByteChannel());
        channelMap.put("bar", new ByteArraySeekableByteChannel());
        channelMap.put("baz", new ByteArraySeekableByteChannel());
        
        readJob.transfer(new ObjectTransferrer() {
            @Override
            public SeekableByteChannel buildChannel(final String key) throws IOException {
                return channelMap.get(key);
            }
        });
        
        for (final Map.Entry<String, ByteArraySeekableByteChannel> channelEntry : channelMap.entrySet()) {
            assertThat(channelEntry.getValue().toString(), is(channelEntry.getKey() + " contents"));
        }
        
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
        final Job job = Ds3ClientHelpers.wrap(ds3Client).startWriteJob(MYBUCKET, objectsToPut);
        
        assertThat(job.getJobId(), is(jobId));
        assertThat(job.getBucketName(), is(MYBUCKET));
        
        final HashMap<String, SeekableByteChannel> channels = new HashMap<>();
        channels.put("baz", mock(SeekableByteChannel.class));
        channels.put("foo", mock(SeekableByteChannel.class));
        channels.put("bar", mock(SeekableByteChannel.class));
        
        job.transfer(new ObjectTransferrer() {
            @Override
            public SeekableByteChannel buildChannel(final String key) throws IOException {
                return channels.get(key);
            }
        });
        
        final InOrder clientInOrder = inOrder(ds3Client);
        clientInOrder.verify(ds3Client).bulkPut(any(BulkPutRequest.class));
        clientInOrder.verify(ds3Client).putObject(putRequestHas("baz", channels.get("baz")));
        clientInOrder.verify(ds3Client).putObject(putRequestHas("foo", channels.get("foo")));
        clientInOrder.verify(ds3Client).putObject(putRequestHas("bar", channels.get("bar")));
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
        when(ds3Client.getObject(getRequestHas("foo"))).thenThrow(new StubException());
        when(ds3Client.getObject(getRequestHas("bar"))).thenThrow(new StubException());
        when(ds3Client.getObject(getRequestHas("baz"))).then(new GetObjectAnswer("baz"));
        
        // Build input list.
        final ArrayList<Ds3Object> objectsToGet = Lists.newArrayList(
            new Ds3Object("foo"),
            new Ds3Object("bar"),
            new Ds3Object("baz")
        );
        
        final Job job = Ds3ClientHelpers.wrap(ds3Client).startReadJob(MYBUCKET, objectsToGet);

        try {
            job.transfer(new ObjectTransferrer() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    // We don't care about the contents since we just want to know that the exception handling works correctly.
                    return new ByteArraySeekableByteChannel();
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

    private static PutObjectRequest putRequestHas(final String key, final SeekableByteChannel channel) {
        return argThat(new ArgumentMatcher<PutObjectRequest>() {
            @Override
            public boolean matches(final Object argument) {
                if (!(argument instanceof PutObjectRequest)) {
                    return false;
                }
                final PutObjectRequest putObjectRequest = (PutObjectRequest)argument;
                return
                        putObjectRequest.getBucketName().equals(MYBUCKET)
                        && putObjectRequest.getObjectName().equals(key)
                        && putObjectRequest.getChannel() == channel;
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
    
    private static final class GetObjectAnswer implements Answer<GetObjectResponse> {
        private final String key;

        private GetObjectAnswer(final String key) {
            this.key = key;
        }

        @Override
        public GetObjectResponse answer(final InvocationOnMock invocation) throws Throwable {
            final WritableByteChannel channel = ((GetObjectRequest)invocation.getArguments()[0]).getDestinationChannel();
            final Writer writer = Channels.newWriter(channel, "UTF-8");
            writer.write(key);
            writer.write(" contents");
            writer.flush();
            return new StubGetObjectResponse();
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
            makeObjects("020c860d-9bb4-4e42-86ff-a1779da0bf16", Arrays.asList(new BulkObject("baz", 12, false, 0))),
            makeObjects("47b83776-7cfd-4f2c-b439-c264bba354cc", Arrays.asList(new BulkObject("foo", 12, false, 0))),
            makeObjects("66a2414a-6690-410b-86c9-dd79be4d72ae", Arrays.asList(new BulkObject("bar", 12, false, 0)))
        ));
        return masterObjectList;
    }

    private static Objects makeObjects(final String nodeId, final List<BulkObject> objectList) {
        final Objects objects = new Objects();
        objects.setNodeId(UUID.fromString(nodeId));
        objects.setObjects(objectList);
        return objects;
    }
    
    private static final class StubException extends RuntimeException {
        private static final long serialVersionUID = 5121719894916333278L;
    }
    
    private static final class StubGetObjectResponse extends GetObjectResponse {
        public StubGetObjectResponse() throws IOException {
            super(null, null, 0);
        }

        @Override
        protected void processResponse() throws IOException {
        }
        
        @Override
        protected void download(final WritableByteChannel destinationChannel, final int bufferSize) throws IOException {
        }
        
        @Override
        public void close() throws IOException {
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
}
