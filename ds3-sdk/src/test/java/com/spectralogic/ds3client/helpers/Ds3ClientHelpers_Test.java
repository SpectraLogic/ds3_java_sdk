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

package com.spectralogic.ds3client.helpers;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.Job;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.models.Owner;
import com.spectralogic.ds3client.models.bulk.*;
import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.security.SignatureException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.spectralogic.ds3client.helpers.RequestMatchers.*;
import static com.spectralogic.ds3client.helpers.ResponseBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Ds3ClientHelpers_Test {
    private static final String MYBUCKET = "mybucket";
    
    @Test
    public void testReadObjects() throws SignatureException, IOException, XmlProcessingException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();


        final BulkGetResponse buildBulkGetResponse = buildBulkGetResponse();
        Mockito.when(ds3Client.bulkGet(hasChunkOrdering(ChunkClientProcessingOrderGuarantee.NONE))).thenReturn(buildBulkGetResponse);

        Mockito.when(ds3Client.getObject(getRequestHas(MYBUCKET, "foo", jobId, 0))).then(getObjectAnswer("foo co"));
        Mockito.when(ds3Client.getObject(getRequestHas(MYBUCKET, "bar", jobId, 0))).then(getObjectAnswer("bar contents"));
        Mockito.when(ds3Client.getObject(getRequestHas(MYBUCKET, "baz", jobId, 0))).then(getObjectAnswer("baz co"));
        Mockito.when(ds3Client.getObject(getRequestHas(MYBUCKET, "foo", jobId, 6))).then(getObjectAnswer("ntents"));
        Mockito.when(ds3Client.getObject(getRequestHas(MYBUCKET, "baz", jobId, 6))).then(getObjectAnswer("ntents"));
        
        final Job job = Ds3ClientHelpers.wrap(ds3Client).startReadJob(MYBUCKET, Lists.newArrayList(
            new Ds3Object("foo"),
            new Ds3Object("bar"),
            new Ds3Object("baz")
        ));
        
        assertThat(job.getJobId(), is(jobId));
        assertThat(job.getBucketName(), is(MYBUCKET));
        
        final HashMap<String, ByteArraySeekableByteChannel> channelMap = new HashMap<>();
        channelMap.put("foo", new ByteArraySeekableByteChannel());
        channelMap.put("bar", new ByteArraySeekableByteChannel());
        channelMap.put("baz", new ByteArraySeekableByteChannel());
        
        final Stopwatch stopwatch = Stopwatch.createStarted();
        job.transfer(new ObjectChannelBuilder() {
            @Override
            public SeekableByteChannel buildChannel(final String key) throws IOException {
                return channelMap.get(key);
            }
        });
        assertThat(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS), is(both(greaterThan(1000L)).and(lessThan(1250L))));
        
        for (final Map.Entry<String, ByteArraySeekableByteChannel> channelEntry : channelMap.entrySet()) {
            assertThat(channelEntry.getValue().toString(), is(channelEntry.getKey() + " contents"));
        }
    }

    @Test(expected = StubException.class)
    public void testReadObjectsWithFailedGet() throws SignatureException, IOException, XmlProcessingException {
        final Ds3Client ds3Client = mock(Ds3Client.class);

        Mockito.when(ds3Client.newForNode(Mockito.<Node>any())).thenReturn(ds3Client);

        final BulkGetResponse buildBulkGetResponse = buildBulkGetResponse();
        Mockito.when(ds3Client.bulkGet(hasChunkOrdering(ChunkClientProcessingOrderGuarantee.NONE))).thenReturn(buildBulkGetResponse);

        final GetAvailableJobChunksResponse jobChunksResponse = buildJobChunksResponse2();
        Mockito.when(ds3Client.getAvailableJobChunks(hasJobId(jobId))).thenReturn(jobChunksResponse);

        Mockito.when(ds3Client.getObject(getRequestHas(MYBUCKET, "foo", jobId, 6))).thenThrow(new StubException());
        Mockito.when(ds3Client.getObject(getRequestHas(MYBUCKET, "baz", jobId, 6))).then(getObjectAnswer("ntents"));
        
        final Job job = Ds3ClientHelpers.wrap(ds3Client).startReadJob(MYBUCKET, Lists.newArrayList(
            new Ds3Object("foo"),
            new Ds3Object("bar"),
            new Ds3Object("baz")
        ));

        job.transfer(new ObjectChannelBuilder() {
            @Override
            public SeekableByteChannel buildChannel(final String key) throws IOException {
                // We don't care about the contents since we just want to know that the exception handling works correctly.
                return new ByteArraySeekableByteChannel();
            }
        });
    }
    
    @Test
    public void testWriteObjects() throws SignatureException, IOException, XmlProcessingException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();
        
        final BulkPutResponse bulkPutResponse = buildBulkPutResponse();
        Mockito.when(ds3Client.bulkPut(Mockito.any(BulkPutRequest.class))).thenReturn(bulkPutResponse);
        
        final AllocateJobChunkResponse allocateResponse1 = buildAllocateResponse1();
        final AllocateJobChunkResponse allocateResponse2 = buildAllocateResponse2();
        final AllocateJobChunkResponse allocateResponse3 = buildAllocateResponse3();
        Mockito.when(ds3Client.allocateJobChunk(hasChunkId(CHUNK_ID_1)))
            .thenReturn(allocateResponse1)
            .thenReturn(allocateResponse2);
        Mockito.when(ds3Client.allocateJobChunk(hasChunkId(CHUNK_ID_2)))
            .thenReturn(allocateResponse3);

        final PutObjectResponse response = mock(PutObjectResponse.class);
        Mockito.when(ds3Client.putObject(putRequestHas(MYBUCKET, "foo", jobId, 0, "foo co"))).thenReturn(response);
        Mockito.when(ds3Client.putObject(putRequestHas(MYBUCKET, "bar", jobId, 0, "bar contents"))).thenReturn(response);
        Mockito.when(ds3Client.putObject(putRequestHas(MYBUCKET, "baz", jobId, 0, "baz co"))).thenReturn(response);
        Mockito.when(ds3Client.putObject(putRequestHas(MYBUCKET, "foo", jobId, 6, "ntents"))).thenReturn(response);
        Mockito.when(ds3Client.putObject(putRequestHas(MYBUCKET, "baz", jobId, 6, "ntents"))).thenReturn(response);
        
        final Job job = Ds3ClientHelpers.wrap(ds3Client).startWriteJob(MYBUCKET, Lists.newArrayList(
                new Ds3Object("foo", 12),
                new Ds3Object("bar", 12),
                new Ds3Object("baz", 12)
        ));
        
        assertThat(job.getJobId(), is(jobId));
        assertThat(job.getBucketName(), is(MYBUCKET));
        
        final HashMap<String, SeekableByteChannel> channelMap = new HashMap<>();
        channelMap.put("foo", channelWithContents("foo contents"));
        channelMap.put("bar", channelWithContents("bar contents"));
        channelMap.put("baz", channelWithContents("baz contents"));
        
        final Stopwatch stopwatch = Stopwatch.createStarted();
        job.transfer(new ObjectChannelBuilder() {
            @Override
            public SeekableByteChannel buildChannel(final String key) throws IOException {
                return channelMap.get(key);
            }
        });
        assertThat(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS), is(both(greaterThan(1000L)).and(lessThan(1250L))));
    }
    
    @Test
    public void testWriteObjectsWithFailedPut() throws SignatureException, IOException, XmlProcessingException {
        final Ds3Client ds3Client = mock(Ds3Client.class);
        final ConnectionDetails details = mock(ConnectionDetails.class);
        Mockito.when(details.getEndpoint()).thenReturn("localhost");

        Mockito.when(ds3Client.newForNode(Mockito.<Node>any())).thenReturn(ds3Client);
        Mockito.when(ds3Client.getConnectionDetails()).thenReturn(details);

        final BulkPutResponse buildBulkPutResponse = buildBulkPutResponse();
        Mockito.when(ds3Client.bulkPut(Mockito.any(BulkPutRequest.class))).thenReturn(buildBulkPutResponse);

        final AllocateJobChunkResponse allocateResponse = buildAllocateResponse2();
        Mockito.when(ds3Client.allocateJobChunk(hasChunkId(CHUNK_ID_1))).thenReturn(allocateResponse);

        final PutObjectResponse putResponse = mock(PutObjectResponse.class);
        Mockito.when(ds3Client.putObject(putRequestHas(MYBUCKET, "foo", jobId, 0, "foo co"))).thenThrow(new StubException());
        Mockito.when(ds3Client.putObject(putRequestHas(MYBUCKET, "baz", jobId, 0, "baz co"))).thenReturn(putResponse);

        final Job job = Ds3ClientHelpers.wrap(ds3Client).startWriteJob(MYBUCKET, Lists.newArrayList(
            new Ds3Object("foo"),
            new Ds3Object("bar"),
            new Ds3Object("baz")
        ));

        final HashMap<String, SeekableByteChannel> channelMap = new HashMap<>();
        channelMap.put("foo", channelWithContents("foo contents"));
        channelMap.put("bar", channelWithContents("bar contents"));
        channelMap.put("baz", channelWithContents("baz contents"));
        
        try {
            job.transfer(new ObjectChannelBuilder() {
                @Override
                public SeekableByteChannel buildChannel(final String key) throws IOException {
                    return channelMap.get(key);
                }
            });
            Assert.fail("Should have failed with an exception before we got here.");
        } catch (final StubException e) {
        }
    }
    
    private static final class StubException extends RuntimeException {
        private static final long serialVersionUID = 5121719894916333278L;
    }

    @Test
    public void testListObjects() throws SignatureException, IOException, XmlProcessingException {
        final Ds3Client ds3Client = mock(Ds3Client.class);
        Mockito.when(ds3Client.getBucket(getBucketHas(MYBUCKET, null))).thenReturn(new StubGetBucketResponse(0));
        Mockito.when(ds3Client.getBucket(getBucketHas(MYBUCKET, "baz"))).thenReturn(new StubGetBucketResponse(1));
        
        // Call the list objects method.
        final List<Contents> contentList = Lists.newArrayList(Ds3ClientHelpers.wrap(ds3Client).listObjects(MYBUCKET));
        
        // Check the results.
        assertThat(contentList.size(), is(3));
        checkContents(contentList.get(0), "foo", "2cde576e5f5a613e6cee466a681f4929", "2009-10-12T17:50:30.000Z", 12);
        checkContents(contentList.get(1), "bar", "f3f98ff00be128139332bcf4b772be43", "2009-10-14T17:50:31.000Z", 12);
        checkContents(contentList.get(2), "baz", "802d45fcb9a3f7d00f1481362edc0ec9", "2009-10-18T17:50:35.000Z", 12);
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


    @Test
    public void testRecoverWriteJob() throws SignatureException, IOException, XmlProcessingException, JobRecoveryException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();

        final ModifyJobResponse modifyWriteJobResponse = buildModifyWriteJobResponse();
        Mockito.when(ds3Client.modifyJob(Mockito.any(ModifyJobRequest.class))).thenReturn(modifyWriteJobResponse);

        final Job job = Ds3ClientHelpers.wrap(ds3Client).recoverWriteJob(jobId);
        assertThat(job.getJobId(), is(jobId));
        assertThat(job.getBucketName(), is(MYBUCKET));
    }

    @Test(expected = com.spectralogic.ds3client.helpers.JobRecoveryException.class)
    public void testRecoverWriteJobThrowsJobRecoveryExceptionForWrongRequestType() throws SignatureException, IOException, XmlProcessingException, JobRecoveryException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();

        final ModifyJobResponse modifyReadJobResponse = buildModifyReadJobResponse();
        Mockito.when(ds3Client.modifyJob(Mockito.any(ModifyJobRequest.class))).thenReturn(modifyReadJobResponse);

        Ds3ClientHelpers.wrap(ds3Client).recoverWriteJob(jobId);
    }

    @Test
    public void testRecoverReadJob() throws SignatureException, IOException, XmlProcessingException, JobRecoveryException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();

        final ModifyJobResponse modifyReadJobResponse = buildModifyReadJobResponse();
        Mockito.when(ds3Client.modifyJob(Mockito.any(ModifyJobRequest.class))).thenReturn(modifyReadJobResponse);

        final Job job = Ds3ClientHelpers.wrap(ds3Client).recoverReadJob(jobId);
        assertThat(job.getJobId(), is(jobId));
        assertThat(job.getBucketName(), is(MYBUCKET));
    }

    @Test(expected = com.spectralogic.ds3client.helpers.JobRecoveryException.class)
    public void testRecoverReadJobThrowsJobRecoveryExceptionForWrongRequestType() throws SignatureException, IOException, XmlProcessingException, JobRecoveryException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();

        final ModifyJobResponse modifyWriteJobResponse = buildModifyWriteJobResponse();
        Mockito.when(ds3Client.modifyJob(Mockito.any(ModifyJobRequest.class))).thenReturn(modifyWriteJobResponse);

        Ds3ClientHelpers.wrap(ds3Client).recoverReadJob(jobId);
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
    
    private static final UUID jobId = new UUID(0x0123456789abcdefL, 0xfedcba9876543210L);
    private static final UUID nodeId = UUID.fromString("29bf5a53-d891-407f-8f3f-749ee7e636f3");

    private static Ds3Client buildDs3ClientForBulk() throws IOException,
            SignatureException {
        final Ds3Client ds3Client = mock(Ds3Client.class);
        final ConnectionDetails details = mock(ConnectionDetails.class);
        Mockito.when(details.getEndpoint()).thenReturn("localhost");
        final GetAvailableJobChunksResponse jobChunksResponse1 = buildJobChunksResponse1();
        final GetAvailableJobChunksResponse jobChunksResponse2 = buildJobChunksResponse2();
        final GetAvailableJobChunksResponse jobChunksResponse3 = buildJobChunksResponse3();
        Mockito.when(ds3Client.getAvailableJobChunks(hasJobId(jobId)))
            .thenReturn(jobChunksResponse1)
            .thenReturn(jobChunksResponse2)
            .thenReturn(jobChunksResponse3);

        Mockito.when(ds3Client.newForNode(Mockito.<Node>any())).thenReturn(ds3Client);
        Mockito.when(ds3Client.getConnectionDetails()).thenReturn(details);
        return ds3Client;
    }

    private static BulkGetResponse buildBulkGetResponse() {
        return bulkGetResponse(buildJobResponse(
                RequestType.GET,
                ChunkClientProcessingOrderGuarantee.NONE,
                0L,
                0L,
                chunk1(false),
                chunk2(false)
        ));
    }
    
    private static GetAvailableJobChunksResponse buildJobChunksResponse1() {
        return retryGetAvailableAfter(1);
    }
    
    private static GetAvailableJobChunksResponse buildJobChunksResponse2() {
        return availableJobChunks(buildJobResponse(
                RequestType.GET,
                ChunkClientProcessingOrderGuarantee.NONE,
                12L,
                0L,
                chunk2(true)
        ));
    }
    
    private static GetAvailableJobChunksResponse buildJobChunksResponse3() {
        return availableJobChunks(buildJobResponse(
                RequestType.GET,
                ChunkClientProcessingOrderGuarantee.NONE,
                24L,
                12L,
                chunk1(true)
        ));
    }

    private static BulkPutResponse buildBulkPutResponse() {
        return bulkPutResponse(buildJobResponse(
                RequestType.PUT,
                ChunkClientProcessingOrderGuarantee.IN_ORDER,
                0L,
                0L,
                chunk1(false),
                chunk2(false)
        ));
    }

    private static ModifyJobResponse buildModifyWriteJobResponse() {
        return modifyJobResponse(buildJobResponse(
                RequestType.PUT,
                ChunkClientProcessingOrderGuarantee.IN_ORDER,
                0L,
                0L,
                chunk1(false),
                chunk2(false)
        ));
    }

    private static ModifyJobResponse buildModifyReadJobResponse() {
        return modifyJobResponse(buildJobResponse(
                RequestType.GET,
                ChunkClientProcessingOrderGuarantee.IN_ORDER,
                0L,
                0L,
                chunk1(false),
                chunk2(false)
        ));
    }

    private static AllocateJobChunkResponse buildAllocateResponse1() {
        return retryAllocateLater(1);
    }
    
    private static AllocateJobChunkResponse buildAllocateResponse2() {
        return allocated(chunk1(false));
    }
    
    private static AllocateJobChunkResponse buildAllocateResponse3() {
        return allocated(chunk2(false));
    }

    private static MasterObjectList buildJobResponse(
            final RequestType requestType,
            final ChunkClientProcessingOrderGuarantee chunkOrdering,
            final long cachedSizeInBytes,
            final long completedSizeInBytes,
            final Objects ... chunks) {
        return ResponseBuilders.jobResponse(
            jobId,
            MYBUCKET,
            requestType,
            36L,
            cachedSizeInBytes,
            completedSizeInBytes,
            chunkOrdering,
            Priority.CRITICAL,
            "9/17/2014 1:03:54 PM",
            UUID.fromString("57919d2d-448c-4e2a-8886-0413af22243e"),
            "spectra",
            WriteOptimization.CAPACITY,
            Arrays.asList(basicNode(nodeId, "black-pearl")),
            Arrays.asList(chunks)
        );
    }

    private static final UUID CHUNK_ID_1 = UUID.fromString("f44f1aab-f365-4814-883f-037d6afa6bcf");
    private static Objects chunk1(final boolean inCache) {
        return chunk(
                1,
                CHUNK_ID_1,
                nodeId,
                object("bar", 0, 12, inCache),
                object("baz", 0, 6, inCache),
                object("foo", 0, 6, inCache)
        );
    }

    private static final UUID CHUNK_ID_2 = UUID.fromString("7cda9f1a-3a7d-44a5-813e-29535228c40c");
    private static Objects chunk2(final boolean inCache) {
        return chunk(
                2,
                CHUNK_ID_2,
                nodeId,
                object("foo", 6, 6, inCache),
                object("baz", 6, 6, inCache)
        );
    }

    @Test
    public void testAddPrefixToDs3ObjectsList() throws IOException, SignatureException {
        final List<Ds3Object> ds3ObjectList = Lists.newArrayList(new Ds3Object("foo"), new Ds3Object("bar"));
        final Ds3ClientHelpers helper = Ds3ClientHelpers.wrap(buildDs3ClientForBulk());
        final Iterable<Ds3Object> modifiedDs3ObjectList = helper.addPrefixToDs3ObjectsList(ds3ObjectList, "baz/");

        boolean foundBazFoo = false;
        boolean foundBazBar = false;

        for (final Ds3Object obj : modifiedDs3ObjectList) {
            if (obj.getName().equals("baz/foo")) {
                foundBazFoo = true;
            } else if (obj.getName().equals("baz/bar")) {
                foundBazBar = true;
            }
        }

        assertTrue(foundBazFoo);
        assertTrue(foundBazBar);
    }

    @Test
    public void testRemovePrefixToDs3ObjectsList() throws IOException, SignatureException {
        final List<Ds3Object> ds3ObjectList = Lists.newArrayList(new Ds3Object("foo/bar"), new Ds3Object("foo/baz"));
        final Ds3ClientHelpers helper = Ds3ClientHelpers.wrap(buildDs3ClientForBulk());
        final Iterable<Ds3Object> modifiedDs3ObjectList = helper.removePrefixFromDs3ObjectsList(ds3ObjectList, "foo/");

        boolean foundBar = false;
        boolean foundBaz = false;

        for (final Ds3Object obj : modifiedDs3ObjectList) {
            if (obj.getName().equals("bar")) {
                foundBar = true;
            } else if (obj.getName().equals("baz")) {
                foundBaz = true;
            }
        }

        assertTrue(foundBar);
        assertTrue(foundBaz);
    }

    @Test
    public void testStripLeadingPath() {
        assertEquals(Ds3ClientHelpers.stripLeadingPath("foo/bar", "foo/"), "bar");
        assertEquals(Ds3ClientHelpers.stripLeadingPath("bar", "foo/"), "bar");
    }

    @Test(expected = Ds3NoMoreRetriesException.class)
    public void testWriteObjectsWithRetryAfter() throws SignatureException, IOException, XmlProcessingException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();

        final BulkPutResponse bulkPutResponse = buildBulkPutResponse();
        Mockito.when(ds3Client.bulkPut(Mockito.any(BulkPutRequest.class))).thenReturn(bulkPutResponse);

        final AllocateJobChunkResponse allocateResponse1 = buildAllocateResponse1();
        Mockito.when(ds3Client.allocateJobChunk(hasChunkId(CHUNK_ID_1)))
                .thenReturn(allocateResponse1);

        final PutObjectResponse response = mock(PutObjectResponse.class);
        Mockito.when(ds3Client.putObject(putRequestHas(MYBUCKET, "foo", jobId, 0, "foo co"))).thenReturn(response);

        final Job job = Ds3ClientHelpers.wrap(ds3Client, 1).startWriteJob(MYBUCKET, Lists.newArrayList(
                new Ds3Object("foo", 12)
        ));

        job.transfer(new ObjectChannelBuilder() {
            @Override
            public SeekableByteChannel buildChannel(final String key) throws IOException {
                // We don't care about the contents since we just want to know that the exception handling works correctly.
                return new ByteArraySeekableByteChannel();
            }
        });
    }

    @Test//(expected = Ds3NoMoreRetriesException.class)
    public void testReadObjectsWithRetryAfter() throws SignatureException, IOException, XmlProcessingException {
        final Ds3Client ds3Client = mock(Ds3Client.class);

        final BulkGetResponse buildBulkGetResponse = buildBulkGetResponse();
        Mockito.when(ds3Client.bulkGet(hasChunkOrdering(ChunkClientProcessingOrderGuarantee.NONE))).thenReturn(buildBulkGetResponse);

        final GetAvailableJobChunksResponse jobChunksResponse = mock(GetAvailableJobChunksResponse.class);
        when(jobChunksResponse.getStatus()).thenReturn(GetAvailableJobChunksResponse.Status.RETRYLATER);

        Mockito.when(ds3Client.getAvailableJobChunks(hasJobId(jobId))).thenReturn(jobChunksResponse);

        final Job job = Ds3ClientHelpers.wrap(ds3Client, 1).startReadJob(MYBUCKET, Lists.newArrayList(
                new Ds3Object("foo")
        ));

        job.transfer(new ObjectChannelBuilder() {
            @Override
            public SeekableByteChannel buildChannel(final String key) throws IOException {
                // We don't care about the contents since we just want to know that the exception handling works correctly.
                return new ByteArraySeekableByteChannel();
            }
        });

    }
}
