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

package com.spectralogic.ds3client.helpers;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.Job;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.helpers.pagination.FileSystemKey;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.Error;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.spectralogic.ds3client.helpers.RequestMatchers.*;
import static com.spectralogic.ds3client.helpers.ResponseBuilders.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Ds3ClientHelpers_Test {
    private static final String MYBUCKET = "mybucket";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Before
    public void setTimeZone() {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    @Test
    public void testReadObjects() throws IOException, ParseException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();

        final GetBulkJobSpectraS3Response buildBulkGetResponse = buildBulkGetResponse();
        Mockito.when(ds3Client.getBulkJobSpectraS3(hasChunkOrdering(JobChunkClientProcessingOrderGuarantee.NONE))).thenReturn(buildBulkGetResponse);

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
        
        final Map<String, ByteArraySeekableByteChannel> channelMap = new HashMap<>();
        channelMap.put("foo", new ByteArraySeekableByteChannel());
        channelMap.put("bar", new ByteArraySeekableByteChannel());
        channelMap.put("baz", new ByteArraySeekableByteChannel());
        
        final Stopwatch stopwatch = Stopwatch.createUnstarted();
        stopwatch.start();
        job.transfer(new ObjectChannelBuilder() {
            @Override
            public SeekableByteChannel buildChannel(final String key) throws IOException {
                return channelMap.get(key);
            }
        });
        stopwatch.stop();
        assertThat(stopwatch.elapsed(TimeUnit.MILLISECONDS), is(lessThan(5000L)));

        for (final Map.Entry<String, ByteArraySeekableByteChannel> channelEntry : channelMap.entrySet()) {
            assertThat(channelEntry.getValue().toString(), is(channelEntry.getKey() + " contents"));
        }
    }

    @Test(expected = StubException.class)
    public void testReadObjectsWithFailedGet() throws IOException, ParseException {
        final Ds3Client ds3Client = mock(Ds3Client.class);

        Mockito.when(ds3Client.newForNode(any())).thenReturn(ds3Client);

        final GetBulkJobSpectraS3Response buildBulkGetResponse = buildBulkGetResponse();
        Mockito.when(ds3Client.getBulkJobSpectraS3(hasChunkOrdering(JobChunkClientProcessingOrderGuarantee.NONE))).thenReturn(buildBulkGetResponse);

        final GetJobChunksReadyForClientProcessingSpectraS3Response jobChunksResponse = buildJobChunksResponse2();
        Mockito.when(ds3Client.getJobChunksReadyForClientProcessingSpectraS3(hasJobId(jobId))).thenReturn(jobChunksResponse);

        Mockito.when(ds3Client.getObject(getRequestHas(MYBUCKET, "foo", jobId, 6))).thenThrow(new StubException());
        Mockito.when(ds3Client.getObject(getRequestHas(MYBUCKET, "baz", jobId, 6))).then(getObjectAnswer("ntents"));

        final ConnectionDetails connectionDetails = Mockito.mock(ConnectionDetails.class);
        Mockito.when(connectionDetails.getEndpoint()).thenReturn("endpoint");
        Mockito.when(ds3Client.getConnectionDetails()).thenReturn(connectionDetails);

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
    public void testWriteObjects() throws IOException, ParseException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();

        final PutBulkJobSpectraS3Response bulkPutResponse = buildBulkPutResponse();
        Mockito.when(ds3Client.putBulkJobSpectraS3(any(PutBulkJobSpectraS3Request.class))).thenReturn(bulkPutResponse);
        
        final AllocateJobChunkSpectraS3Response allocateResponse1 = buildAllocateResponse1();
        final AllocateJobChunkSpectraS3Response allocateResponse2 = buildAllocateResponse2();
        final AllocateJobChunkSpectraS3Response allocateResponse3 = buildAllocateResponse3();
        Mockito.when(ds3Client.allocateJobChunkSpectraS3(hasChunkId(CHUNK_ID_1)))
            .thenReturn(allocateResponse1)
            .thenReturn(allocateResponse2);
        Mockito.when(ds3Client.allocateJobChunkSpectraS3(hasChunkId(CHUNK_ID_2)))
            .thenReturn(allocateResponse3);

        final PutObjectResponse response = mock(PutObjectResponse.class);
        Mockito.when(ds3Client.putObject(putRequestHas(MYBUCKET, "foo", jobId, 0, "foo co"))).thenReturn(response);
        Mockito.when(ds3Client.putObject(putRequestHas(MYBUCKET, "foo", jobId, 6, "ntents"))).thenReturn(response);

        Mockito.when(ds3Client.putObject(putRequestHas(MYBUCKET, "bar", jobId, 0, "foo contents"))).thenReturn(response);

        Mockito.when(ds3Client.putObject(putRequestHas(MYBUCKET, "baz", jobId, 0, "foo co"))).thenReturn(response);
        Mockito.when(ds3Client.putObject(putRequestHas(MYBUCKET, "baz", jobId, 6, "ntents"))).thenReturn(response);

        final String fooContents = "foo contents";
        final String barContents = "bar contents";
        final String bazContents = "baz contents";
        final Job job = Ds3ClientHelpers.wrap(ds3Client).startWriteJob(MYBUCKET, Lists.newArrayList(
                new Ds3Object("foo", fooContents.length()),
                new Ds3Object("bar", barContents.length()),
                new Ds3Object("baz", bazContents.length())
        ));
        
        assertThat(job.getJobId(), is(jobId));
        assertThat(job.getBucketName(), is(MYBUCKET));
        
        final HashMap<String, SeekableByteChannel> channelMap = new HashMap<>();
        channelMap.put("foo", channelWithContents(fooContents));
        channelMap.put("bar", channelWithContents(barContents));
        channelMap.put("baz", channelWithContents(bazContents));
        
        final Stopwatch stopwatch = Stopwatch.createUnstarted();
        stopwatch.start();
        job.transfer(new ObjectChannelBuilder() {
            @Override
            public SeekableByteChannel buildChannel(final String key) throws IOException {
                return channelMap.get(key);
            }
        });
        stopwatch.stop();

        // Assert that the job finishes
        assertThat(stopwatch.elapsed(TimeUnit.MILLISECONDS), is(lessThan(5000L)));
        assertEquals(channelMap.get("foo").position(), fooContents.length());
        assertEquals(channelMap.get("bar").position(), barContents.length());
        assertEquals(channelMap.get("baz").position(), bazContents.length());
    }
    
    @Test
    public void testWriteObjectsWithFailedPut() throws IOException, ParseException {
        final Ds3Client ds3Client = mock(Ds3Client.class);
        final ConnectionDetails details = mock(ConnectionDetails.class);
        Mockito.when(details.getEndpoint()).thenReturn("localhost");

        Mockito.when(ds3Client.newForNode(any())).thenReturn(ds3Client);
        Mockito.when(ds3Client.getConnectionDetails()).thenReturn(details);

        final PutBulkJobSpectraS3Response buildBulkPutResponse = buildBulkPutResponse();
        Mockito.when(ds3Client.putBulkJobSpectraS3(any(PutBulkJobSpectraS3Request.class))).thenReturn(buildBulkPutResponse);

        final AllocateJobChunkSpectraS3Response allocateResponse2 = buildAllocateResponse2();
        final AllocateJobChunkSpectraS3Response allocateResponse3 = buildAllocateResponse3();

        Mockito.when(ds3Client.allocateJobChunkSpectraS3(hasChunkId(CHUNK_ID_1))).thenReturn(allocateResponse2);

        Mockito.when(ds3Client.allocateJobChunkSpectraS3(hasChunkId(CHUNK_ID_1)))
                .thenReturn(allocateResponse2);
        Mockito.when(ds3Client.allocateJobChunkSpectraS3(hasChunkId(CHUNK_ID_2)))
                .thenReturn(allocateResponse3);

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
    
    private static final class StubException extends IOException {
        private static final long serialVersionUID = 5121719894916333278L;
    }

    @Test
    public void testListObjects() throws IOException, ParseException {
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

    @Test
    public void testRemoteListDirectory() throws IOException {
        final Ds3Client ds3Client = mock(Ds3Client.class);
        Mockito.when(ds3Client.getBucket(getBucketHas(MYBUCKET, null))).thenReturn(new StubGetBucketResponse(0));
        Mockito.when(ds3Client.getBucket(getBucketHas(MYBUCKET, "baz"))).thenReturn(new StubGetBucketResponse(1));
        Mockito.when(ds3Client.getBucket(getBucketHas(MYBUCKET, "/foo/baz"))).thenReturn(new StubGetBucketResponse(1));
        final Iterable<FileSystemKey> fileSystemKeys = Ds3ClientHelpers.wrap(ds3Client).remoteListDirectory(MYBUCKET, null, "/", null, 0);
        final List<FileSystemKey> contentsList = Lists.newArrayList(fileSystemKeys);
        assertThat(contentsList.size(), is(3));
    }
    
    private static void checkContents(
            final Contents contents,
            final String key,
            final String eTag,
            final String lastModified,
            final long size) throws ParseException {
        assertThat(contents.getKey(), is(key));
        assertThat(contents.getETag(), is(eTag));
        assertThat(contents.getLastModified(), is(DATE_FORMAT.parse(lastModified)));
        assertThat(contents.getSize(), is(size));
    }


    @Test
    public void testRecoverWriteJob() throws IOException, JobRecoveryException, ParseException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();

        final ModifyJobSpectraS3Response modifyWriteJobResponse = buildModifyWriteJobResponse();
        Mockito.when(ds3Client.modifyJobSpectraS3(any(ModifyJobSpectraS3Request.class))).thenReturn(modifyWriteJobResponse);

        final Job job = Ds3ClientHelpers.wrap(ds3Client).recoverWriteJob(jobId);
        assertThat(job.getJobId(), is(jobId));
        assertThat(job.getBucketName(), is(MYBUCKET));
    }

    @Test(expected = com.spectralogic.ds3client.helpers.JobRecoveryException.class)
    public void testRecoverWriteJobThrowsJobRecoveryExceptionForWrongRequestType() throws IOException, JobRecoveryException, ParseException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();

        final ModifyJobSpectraS3Response modifyReadJobResponse = buildModifyReadJobResponse();
        Mockito.when(ds3Client.modifyJobSpectraS3(any(ModifyJobSpectraS3Request.class))).thenReturn(modifyReadJobResponse);

        Ds3ClientHelpers.wrap(ds3Client).recoverWriteJob(jobId);
    }

    @Test
    public void testRecoverReadJob() throws IOException, JobRecoveryException, ParseException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();

        final ModifyJobSpectraS3Response modifyReadJobResponse = buildModifyReadJobResponse();
        Mockito.when(ds3Client.modifyJobSpectraS3(any(ModifyJobSpectraS3Request.class))).thenReturn(modifyReadJobResponse);

        final Job job = Ds3ClientHelpers.wrap(ds3Client).recoverReadJob(jobId);
        assertThat(job.getJobId(), is(jobId));
        assertThat(job.getBucketName(), is(MYBUCKET));
    }

    @Test(expected = com.spectralogic.ds3client.helpers.JobRecoveryException.class)
    public void testRecoverReadJobThrowsJobRecoveryExceptionForWrongRequestType() throws IOException, JobRecoveryException, ParseException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();

        final ModifyJobSpectraS3Response modifyWriteJobResponse = buildModifyWriteJobResponse();
        Mockito.when(ds3Client.modifyJobSpectraS3(any(ModifyJobSpectraS3Request.class))).thenReturn(modifyWriteJobResponse);

        Ds3ClientHelpers.wrap(ds3Client).recoverReadJob(jobId);
    }

    private static final class StubGetBucketResponse extends GetBucketResponse {
        private final int invocationIndex;

        public StubGetBucketResponse(final int invocationIndex) throws IOException {
            super(null, null, ChecksumType.Type.NONE);
            this.invocationIndex = invocationIndex;
        }

        @Override
        public ListBucketResult getListBucketResult() {
            try {
                switch (this.invocationIndex) {
                    case 0: return buildListBucketResult(buildContentList0(), "", "baz", true);
                    case 1: return buildListBucketResult(buildContentList1(), "baz", "", false);
                    default:
                        Assert.fail("List objects in bucket called too many times");
                        return null;
                }
            } catch (final ParseException e) {
                Assert.fail(e.toString());
            }
            return null;
        }

        private static ListBucketResult buildListBucketResult(
                final List<Contents> contentList,
                final String marker,
                final String nextMarker,
                final boolean isTruncated) {
            final ListBucketResult listBucketResult = new ListBucketResult();
            listBucketResult.setObjects(contentList);
            listBucketResult.setDelimiter("");
            listBucketResult.setMarker(marker);
            listBucketResult.setMaxKeys(2);
            listBucketResult.setName(MYBUCKET);
            listBucketResult.setNextMarker(nextMarker);
            listBucketResult.setPrefix("");
            listBucketResult.setTruncated(isTruncated);
            return listBucketResult;
        }
        
        private static List<Contents> buildContentList0() throws ParseException {
            return Lists.newArrayList(
                buildContents("foo", "2cde576e5f5a613e6cee466a681f4929", "2009-10-12T17:50:30.000Z", 12),
                buildContents("bar", "f3f98ff00be128139332bcf4b772be43", "2009-10-14T17:50:31.000Z", 12)
            );
        }
        
        private static List<Contents> buildContentList1() throws ParseException {
            return Lists.newArrayList(
                buildContents("baz", "802d45fcb9a3f7d00f1481362edc0ec9", "2009-10-18T17:50:35.000Z", 12)
            );
        }
        
        private static Contents buildContents(
                final String key,
                final String eTag,
                final String lastModified,
                final long size) throws ParseException {
            final Contents contents = new Contents();
            contents.setETag(eTag);
            contents.setKey(key);
            contents.setLastModified(DATE_FORMAT.parse(lastModified));
            final User owner = new User();
            owner.setDisplayName("person@spectralogic.com");
            owner.setId(UUID.randomUUID());
            contents.setOwner(owner);
            contents.setSize(size);
            contents.setStorageClass("STANDARD");
            return contents;
        }
    }
    
    private static final UUID jobId = new UUID(0x0123456789abcdefL, 0xfedcba9876543210L);
    private static final UUID nodeId = UUID.fromString("29bf5a53-d891-407f-8f3f-749ee7e636f3");

    private static Ds3Client buildDs3ClientForBulk() throws IOException,
            ParseException {
        final Ds3Client ds3Client = mock(Ds3Client.class);
        final ConnectionDetails details = mock(ConnectionDetails.class);
        Mockito.when(details.getEndpoint()).thenReturn("localhost");
        final GetJobChunksReadyForClientProcessingSpectraS3Response jobChunksResponse1 = buildJobChunksResponse1();
        final GetJobChunksReadyForClientProcessingSpectraS3Response jobChunksResponse2 = buildJobChunksResponse2();
        final GetJobChunksReadyForClientProcessingSpectraS3Response jobChunksResponse3 = buildJobChunksResponse3();
        Mockito.when(ds3Client.getJobChunksReadyForClientProcessingSpectraS3(hasJobId(jobId)))
            .thenReturn(jobChunksResponse1)
            .thenReturn(jobChunksResponse2)
            .thenReturn(jobChunksResponse3);

        Mockito.when(ds3Client.newForNode(any())).thenReturn(ds3Client);
        Mockito.when(ds3Client.getConnectionDetails()).thenReturn(details);
        return ds3Client;
    }

    private static GetBulkJobSpectraS3Response buildBulkGetResponse() throws ParseException {
        return bulkGetResponse(buildJobResponse(
                JobRequestType.GET,
                JobChunkClientProcessingOrderGuarantee.NONE,
                0L,
                0L,
                chunk1(false),
                chunk2(false)
        ));
    }

    private static HeadBucketResponse buildHeadBucketResponse(final HeadBucketResponse.Status status) {
        return ResponseBuilders.headBucket(status);
    }
    
    private static GetJobChunksReadyForClientProcessingSpectraS3Response buildJobChunksResponse1() {
        return retryGetAvailableAfter(1);
    }
    
    private static GetJobChunksReadyForClientProcessingSpectraS3Response buildJobChunksResponse2() throws ParseException {
        return availableJobChunks(buildJobResponse(
                JobRequestType.GET,
                JobChunkClientProcessingOrderGuarantee.NONE,
                12L,
                0L,
                chunk2(true)
        ));
    }
    
    private static GetJobChunksReadyForClientProcessingSpectraS3Response buildJobChunksResponse3() throws ParseException {
        return availableJobChunks(buildJobResponse(
                JobRequestType.GET,
                JobChunkClientProcessingOrderGuarantee.NONE,
                24L,
                12L,
                chunk1(true)
        ));
    }

    private static PutBulkJobSpectraS3Response buildBulkPutResponse() throws ParseException {
        return bulkPutResponse(buildJobResponse(
                JobRequestType.PUT,
                JobChunkClientProcessingOrderGuarantee.IN_ORDER,
                0L,
                0L,
                chunk1(false),
                chunk2(false)
        ));
    }

    private static ModifyJobSpectraS3Response buildModifyWriteJobResponse() throws ParseException {
        return modifyJobResponse(buildJobResponse(
                JobRequestType.PUT,
                JobChunkClientProcessingOrderGuarantee.IN_ORDER,
                0L,
                0L,
                chunk1(false),
                chunk2(false)
        ));
    }

    private static ModifyJobSpectraS3Response buildModifyReadJobResponse() throws ParseException {
        return modifyJobResponse(buildJobResponse(
                JobRequestType.GET,
                JobChunkClientProcessingOrderGuarantee.IN_ORDER,
                0L,
                0L,
                chunk1(false),
                chunk2(false)
        ));
    }

    private static AllocateJobChunkSpectraS3Response buildAllocateResponse1() {
        return retryAllocateLater(1);
    }
    
    private static AllocateJobChunkSpectraS3Response buildAllocateResponse2() {
        return allocated(chunk1(false));
    }
    
    private static AllocateJobChunkSpectraS3Response buildAllocateResponse3() {
        return allocated(chunk2(false));
    }

    private static MasterObjectList buildJobResponse(
            final JobRequestType requestType,
            final JobChunkClientProcessingOrderGuarantee chunkOrdering,
            final long cachedSizeInBytes,
            final long completedSizeInBytes,
            final Objects... chunks) throws ParseException {
        return ResponseBuilders.jobResponse(
                jobId,
                MYBUCKET,
                requestType,
                36L,
                cachedSizeInBytes,
                completedSizeInBytes,
                chunkOrdering,
                Priority.CRITICAL,
                "2014-09-17T13:03:54.000Z",
                UUID.fromString("57919d2d-448c-4e2a-8886-0413af22243e"),
                "spectra",
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
    public void testAddPrefixToDs3ObjectsList() throws IOException, ParseException {
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
    public void testRemovePrefixToDs3ObjectsList() throws IOException, ParseException {
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
    public void testWriteObjectsWithRetryAfter() throws IOException, ParseException {
        final Ds3Client ds3Client = buildDs3ClientForBulk();

        final PutBulkJobSpectraS3Response bulkPutResponse = buildBulkPutResponse();
        Mockito.when(ds3Client
                .putBulkJobSpectraS3(any(PutBulkJobSpectraS3Request.class)))
                .thenReturn(bulkPutResponse);

        final AllocateJobChunkSpectraS3Response allocateResponse1 = buildAllocateResponse1();
        Mockito.when(ds3Client.allocateJobChunkSpectraS3(hasChunkId(CHUNK_ID_1)))
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

    @Test(expected = Ds3NoMoreRetriesException.class)
    public void testReadObjectsWithRetryAfter() throws IOException, ParseException {
        final Ds3Client ds3Client = mock(Ds3Client.class);

        final GetBulkJobSpectraS3Response buildBulkGetResponse = buildBulkGetResponse();
        Mockito.when(ds3Client.getBulkJobSpectraS3(hasChunkOrdering(JobChunkClientProcessingOrderGuarantee.NONE)))
                .thenReturn(buildBulkGetResponse);

        final GetJobChunksReadyForClientProcessingSpectraS3Response jobChunksResponse =
                mock(GetJobChunksReadyForClientProcessingSpectraS3Response.class);
        when(jobChunksResponse.getStatus())
                .thenReturn(GetJobChunksReadyForClientProcessingSpectraS3Response.Status.RETRYLATER);

        Mockito.when(ds3Client
                .getJobChunksReadyForClientProcessingSpectraS3(hasJobId(jobId)))
                .thenReturn(jobChunksResponse);

        final ConnectionDetails connectionDetails = Mockito.mock(ConnectionDetails.class);
        Mockito.when(connectionDetails.getEndpoint()).thenReturn("endpoint");
        Mockito.when(ds3Client.getConnectionDetails()).thenReturn(connectionDetails);

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

    @Test
    public void testEnsureBucketExistsRace() throws IOException {
        final Ds3Client ds3Client = mock(Ds3Client.class);
        final HeadBucketResponse response = buildHeadBucketResponse(HeadBucketResponse.Status.DOESNTEXIST);
        Mockito.when(ds3Client.headBucket(any(HeadBucketRequest.class))).thenReturn(response);
        Mockito.when(ds3Client.putBucket(any(PutBucketRequest.class)))
                .thenThrow(new FailedRequestException(ImmutableList.of(202, 409), 409, new Error(), "Conflict", "5"));

        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(ds3Client);

        helpers.ensureBucketExists("fake_bucket"); // if this throws an exception, then this test should fail
        verify(ds3Client, atLeastOnce()).putBucket(any(PutBucketRequest.class));
    }

    @Test(expected = FailedRequestException.class)
    public void testEnsureBucketExistsReturnsError() throws IOException {
        final Ds3Client ds3Client = mock(Ds3Client.class);
        final HeadBucketResponse response = buildHeadBucketResponse(HeadBucketResponse.Status.DOESNTEXIST);
        Mockito.when(ds3Client.headBucket(any(HeadBucketRequest.class))).thenReturn(response);
        Mockito.when(ds3Client.putBucket(any(PutBucketRequest.class)))
                .thenThrow(new FailedRequestException(ImmutableList.of(202, 409, 500), 500, new Error(), "Error", "5"));

        final Ds3ClientHelpers helpers = Ds3ClientHelpers.wrap(ds3Client);

        helpers.ensureBucketExists("fake_bucket");
    }
}
