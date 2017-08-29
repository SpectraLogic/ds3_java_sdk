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

package com.spectralogic.ds3client;

import com.google.common.collect.*;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.exceptions.ContentLengthNotMatchException;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.PartialDs3Object;
import com.spectralogic.ds3client.models.common.Credentials;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.models.multipart.CompleteMultipartUpload;
import com.spectralogic.ds3client.models.multipart.Part;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.FailedRequestUsingMgmtPortException;
import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;
import com.spectralogic.ds3client.utils.ResourceUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class Ds3Client_Test {
    private static final UUID MASTER_OBJECT_LIST_JOB_ID = UUID.fromString("1a85e743-ec8f-4789-afec-97e587a26936");
    private static final String MASTER_OBJECT_LIST_XML =
        "<MasterObjectList BucketName=\"bucket8192000000\" JobId=\"1a85e743-ec8f-4789-afec-97e587a26936\" Priority=\"NORMAL\" RequestType=\"GET\" StartDate=\"2014-07-01T20:12:52.000Z\">"
        + "  <Nodes>"
        + "    <Node EndPoint=\"10.1.18.12\" HttpPort=\"80\" HttpsPort=\"443\" Id=\"a02053b9-0147-11e4-8d6a-002590c1177c\"/>"
        + "    <Node EndPoint=\"10.1.18.13\" HttpsPort=\"443\" Id=\"95e97010-8e70-4733-926c-aeeb21796848\"/>"
        + "  </Nodes>"
        + "  <Objects ChunkId=\"f58370c2-2538-4e78-a9f8-e4d2676bdf44\" ChunkNumber=\"0\" NodeId=\"a02053b9-0147-11e4-8d6a-002590c1177c\">"
        + "    <Object Name=\"client00obj000004-8000000\" InCache=\"true\" Length=\"5368709120\" Offset=\"0\"/>"
        + "    <Object Name=\"client00obj000004-8000000\" InCache=\"true\" Length=\"2823290880\" Offset=\"5368709120\"/>"
        + "  </Objects>"
        + "  <Objects ChunkId=\"4137d768-25bb-4942-9d36-b92dfbe75e01\" ChunkNumber=\"1\" NodeId=\"95e97010-8e70-4733-926c-aeeb21796848\">"
        + "    <Object Name=\"client00obj000008-8000000\" InCache=\"true\" Length=\"2823290880\" Offset=\"5368709120\"/>"
        + "    <Object Name=\"client00obj000008-8000000\" InCache=\"true\" Length=\"5368709120\" Offset=\"0\"/>"
        + "  </Objects>"
        + "</MasterObjectList>";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private final static String IDS_REQUEST_PAYLOAD = "<Ids><Id>id1</Id><Id>id2</Id><Id>id3</Id></Ids>";
    private final static ImmutableList<String> IDS_REQUEST_PAYLOAD_LIST = ImmutableList.of("id1", "id2", "id3");

    private static final String SIMPLE_OBJECT_REQUEST_PAYLOAD = "<Objects><Object Name=\"file1\"/><Object Name=\"file2\"/><Object Name=\"file3\"/></Objects>";
    private static final List<Ds3Object> SIMPLE_OBJECT_LIST = Arrays.asList(
            new Ds3Object("file1"),
            new Ds3Object("file2"),
            new Ds3Object("file3")
    );

    private static final String SIMPLE_BULK_OBJECT_LIST_RESPONSE = "<Data><Object Bucket=\"default_bucket_name\" Id=\"161853d9-d409-4775-b4c1-ace43cb4dc57\" Latest=\"true\" Length=\"10\" Name=\"o1\" Offset=\"0\" Version=\"1\"/><Object Bucket=\"default_bucket_name\" Id=\"1022fdf0-6d5c-4fb5-83f5-031423af8a8b\" Latest=\"true\" Length=\"10\" Name=\"o2\" Offset=\"0\" Version=\"1\"/></Data>";

    @Before
    public void setTimeZone() {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void getBuckets() throws IOException, ParseException {
        final UUID id = UUID.randomUUID();
        final String stringResponse = "<ListAllMyBucketsResult xmlns=\"http://doc.s3.amazonaws.com/2006-03-01\">\n" +
                "<Owner><ID>" + id.toString() + "</ID><DisplayName>ryan</DisplayName></Owner><Buckets><Bucket><Name>testBucket2</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest1</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest2</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest3</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest4</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest5</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest6</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>testBucket3</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>testBucket1</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>testbucket</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket></Buckets></ListAllMyBucketsResult>";
        
        final List<String> expectedBucketNames = Arrays.asList(
            "testBucket2",
            "bulkTest",
            "bulkTest1",
            "bulkTest2",
            "bulkTest3",
            "bulkTest4",
            "bulkTest5",
            "bulkTest6",
            "testBucket3",
            "testBucket1",
            "testbucket"
        );
        
        final GetServiceResponse response = MockNetwork
            .expecting(HttpVerb.GET, "/", null, null)
            .returning(200, stringResponse)
            .asClient()
            .getService(new GetServiceRequest());
        final ListAllMyBucketsResult result = response.getListAllMyBucketsResult();
        assertThat(result.getOwner().getDisplayName(), is("ryan"));
        assertThat(result.getOwner().getId(), is(id));
        
        final List<BucketDetails> buckets = result.getBuckets();
        final List<String> bucketNames = new ArrayList<>();
        for (final BucketDetails bucket : buckets) {
            bucketNames.add(bucket.getName());
            assertThat(bucket.getCreationDate(), is(DATE_FORMAT.parse("2013-12-11T23:20:09.000Z")));
        }
        assertThat(bucketNames, is(expectedBucketNames));
    }

    @Test(expected = FailedRequestUsingMgmtPortException.class)
    public void getSystemInfoOffMgmtPort() throws IOException {
        final Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put(FailedRequestUsingMgmtPortException.MGMT_PORT_HEADER, "true");
        MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/system_information", null, null)
                .returning(FailedRequestUsingMgmtPortException.MGMT_PORT_STATUS_CODE, "", responseHeaders)
                .asClient()
                .getSystemInformationSpectraS3(new GetSystemInformationSpectraS3Request());
    }

    @Test(expected = FailedRequestException.class)
    public void getBadBuckets() throws IOException {
        MockNetwork
                .expecting(HttpVerb.GET, "/", null, null)
                .returning(400, "")
                .asClient()
                .getService(new GetServiceRequest());
    }

    @Test
    public void getBucket() throws IOException, ParseException {
        final UUID id = UUID.randomUUID();
        final String xmlResponse = "<ListBucketResult xmlns=\"http://s3.amazonaws.com/doc/2006-03-01/\"><Name>remoteTest16</Name><Prefix/><Marker/><MaxKeys>1000</MaxKeys><IsTruncated>false</IsTruncated><Contents><Key>user/hduser/gutenberg/20417.txt.utf-8</Key><LastModified>2014-01-03T13:26:47.000Z</LastModified><ETag>8B19F3F41868106382A677C3435BDCE5</ETag><Size>674570</Size><StorageClass>STANDARD</StorageClass>" +
                "<Owner><ID>" + id.toString() + "</ID><DisplayName>ryan</DisplayName></Owner></Contents><Contents><Key>user/hduser/gutenberg/5000.txt.utf-8</Key><LastModified>2014-01-03T13:26:47.000Z</LastModified><ETag>9DE344878423E44B129730CE22B4B137</ETag><Size>1423803</Size><StorageClass>STANDARD</StorageClass>" +
                "<Owner><ID>" + id.toString() + "</ID><DisplayName>ryan</DisplayName></Owner></Contents><Contents><Key>user/hduser/gutenberg/4300.txt.utf-8</Key><LastModified>2014-01-03T13:26:47.000Z</LastModified><ETag>33EE4519EA7DDAB27CA4E2742326D70B</ETag><Size>1573150</Size><StorageClass>DEEP</StorageClass>" +
                "<Owner><ID>" + id.toString() + "</ID><DisplayName>ryan</DisplayName></Owner></Contents></ListBucketResult>";
        
        final ListBucketResult result = MockNetwork
            .expecting(HttpVerb.GET, "/remoteTest16", null, null)
            .returning(200, xmlResponse)
            .asClient()
            .getBucket(new GetBucketRequest("remoteTest16"))
            .getListBucketResult();
        
        assertThat(result.getName(), is("remoteTest16"));
        assertThat(result.getPrefix(), is(nullValue()));
        assertThat(result.getMarker(), is(nullValue()));
        assertThat(result.getMaxKeys(), is(1000));

        final List<Contents> objects = result.getObjects();
        assertThat(objects, is(notNullValue()));
        assertThat(objects.size(), is(3));
        this.assertObjectsEquals(
                objects.get(0),
                "user/hduser/gutenberg/20417.txt.utf-8",
                DATE_FORMAT.parse("2014-01-03T13:26:47.000Z"),
                "8B19F3F41868106382A677C3435BDCE5",
                674570,
                "STANDARD"
        );
        this.assertObjectsEquals(
                objects.get(1),
                "user/hduser/gutenberg/5000.txt.utf-8",
                DATE_FORMAT.parse("2014-01-03T13:26:47.000Z"),
                "9DE344878423E44B129730CE22B4B137",
                1423803,
                "STANDARD"
        );
        this.assertObjectsEquals(
                objects.get(2),
                "user/hduser/gutenberg/4300.txt.utf-8",
                DATE_FORMAT.parse("2014-01-03T13:26:47.000Z"),
                "33EE4519EA7DDAB27CA4E2742326D70B",
                1573150,
                "DEEP"
        );
    }

    private void assertObjectsEquals(
            final Contents objects,
            final String key,
            final Date lastModified,
            final String eTag,
            final long size,
            final Object storageClass) {
        assertThat(objects.getKey(), is(key));
        assertThat(objects.getLastModified(), is(lastModified));
        assertThat(objects.getETag(), is(eTag));
        assertThat(objects.getSize(), is(size));
        assertThat(objects.getStorageClass(), is(storageClass));
    }

    @Test
    public void getObjectsSpectraS3() throws IOException, ParseException {
        final Map<String, String> queryParams = new HashMap<>();
        final String bucketId = "a24d14f3-e2f0-4bfb-ab71-f99d5ef43745";
        queryParams.put("bucket_id", bucketId);

        final String stringResponse = "<Data>" +
                "<S3Object><BucketId>a24d14f3-e2f0-4bfb-ab71-f99d5ef43745</BucketId><CreationDate>2015-09-21T20:06:47.694Z</CreationDate><Id>e37c3ce0-12aa-4f54-87e3-42532aca0e5e</Id><Name>beowulf.txt</Name><Type>DATA</Type><Version>1</Version></S3Object><S3Object><BucketId>a24d14f3-e2f0-4bfb-ab71-f99d5ef43745</BucketId><CreationDate>2015-09-21T20:06:47.779Z</CreationDate><Id>dc628815-c723-4c4e-b68b-5f5d10f38af5</Id><Name>sherlock_holmes.txt</Name><Type>DATA</Type><Version>1</Version></S3Object><S3Object><BucketId>a24d14f3-e2f0-4bfb-ab71-f99d5ef43745</BucketId><CreationDate>2015-09-21T20:06:47.772Z</CreationDate><Id>4f6985fd-fbae-4421-ba27-66fdb96187c5</Id><Name>tale_of_two_cities.txt</Name><Type>DATA</Type><Version>1</Version></S3Object><S3Object><BucketId>a24d14f3-e2f0-4bfb-ab71-f99d5ef43745</BucketId><CreationDate>2015-09-21T20:06:47.696Z</CreationDate><Id>82c18910-fadb-4461-a152-bf714ae91b55</Id><Name>ulysses.txt</Name><Type>DATA</Type><Version>1</Version></S3Object></Data>";

        final ImmutableMap<String, String> responseHeaders = ImmutableMap.of(
                "page-truncated", "0",
                "total-result-count", "4");

        final List<S3Object> objects = MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/object", queryParams, null)
                .returning(200, stringResponse, responseHeaders)
                .asClient()
                .getObjectsDetailsSpectraS3(new GetObjectsDetailsSpectraS3Request().withBucketId(bucketId))
                .getS3ObjectListResult()
                .getS3Objects();

        final S3Object beowulf = new S3Object();
        beowulf.setBucketId(UUID.fromString("a24d14f3-e2f0-4bfb-ab71-f99d5ef43745"));
        beowulf.setCreationDate(DATE_FORMAT.parse("2015-09-21T20:06:47.694Z"));
        beowulf.setId(UUID.fromString("e37c3ce0-12aa-4f54-87e3-42532aca0e5e"));
        beowulf.setName("beowulf.txt");
        beowulf.setType(S3ObjectType.DATA);
        beowulf.setVersion(1);

        final S3Object notBeowulf = new S3Object();
        notBeowulf.setBucketId(UUID.fromString("a24d14f3-e2f0-4bfb-ab71-f99d5ef43745"));
        notBeowulf.setCreationDate(DATE_FORMAT.parse("2015-09-21T20:06:47.694Z"));
        notBeowulf.setId(UUID.fromString("e37c3ce0-12aa-4f54-87e3-42532aca0e5e"));
        notBeowulf.setName("notBeowulf.txt");
        notBeowulf.setType(S3ObjectType.DATA);
        notBeowulf.setVersion(1);

        assertThat(objects.size(), is(4));
        assertThat(s3ObjectExists(objects, beowulf), is(true));
        assertThat(s3ObjectExists(objects, notBeowulf), is(false));
    }

    @Test (expected = IllegalArgumentException.class)
    public void getObjectsSpectraS3ParseHeaderException() throws IOException, ParseException {
        final Map<String, String> queryParams = new HashMap<>();
        final String bucketId = "a24d14f3-e2f0-4bfb-ab71-f99d5ef43745";
        queryParams.put("bucket_id", bucketId);

        final String stringResponse = "<Data>" +
                "<S3Object><BucketId>a24d14f3-e2f0-4bfb-ab71-f99d5ef43745</BucketId><CreationDate>2015-09-21T20:06:47.694Z</CreationDate><Id>e37c3ce0-12aa-4f54-87e3-42532aca0e5e</Id><Name>beowulf.txt</Name><Type>DATA</Type><Version>1</Version></S3Object><S3Object><BucketId>a24d14f3-e2f0-4bfb-ab71-f99d5ef43745</BucketId><CreationDate>2015-09-21T20:06:47.779Z</CreationDate><Id>dc628815-c723-4c4e-b68b-5f5d10f38af5</Id><Name>sherlock_holmes.txt</Name><Type>DATA</Type><Version>1</Version></S3Object><S3Object><BucketId>a24d14f3-e2f0-4bfb-ab71-f99d5ef43745</BucketId><CreationDate>2015-09-21T20:06:47.772Z</CreationDate><Id>4f6985fd-fbae-4421-ba27-66fdb96187c5</Id><Name>tale_of_two_cities.txt</Name><Type>DATA</Type><Version>1</Version></S3Object><S3Object><BucketId>a24d14f3-e2f0-4bfb-ab71-f99d5ef43745</BucketId><CreationDate>2015-09-21T20:06:47.696Z</CreationDate><Id>82c18910-fadb-4461-a152-bf714ae91b55</Id><Name>ulysses.txt</Name><Type>DATA</Type><Version>1</Version></S3Object></Data>";

        final ImmutableMap<String, String> responseHeaders = ImmutableMap.of(
                "page-truncated", "0",
                "page-truncated", "1",
                "total-result-count", "4");

        MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/object", queryParams, null)
                .returning(200, stringResponse, responseHeaders)
                .asClient()
                .getObjectsDetailsSpectraS3(new GetObjectsDetailsSpectraS3Request().withBucketId(bucketId))
                .getS3ObjectListResult()
                .getS3Objects();

    }

    private boolean s3ObjectExists(final List<S3Object> objects, final S3Object s3obj) {
        for (final S3Object obj : objects) {
            if (s3obj.equals(obj)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void putBucket() throws IOException {
        MockNetwork
            .expecting(HttpVerb.PUT, "/bucketName", null, null)
            .returning(200, "")
            .asClient()
            .putBucket(new PutBucketRequest("bucketName"));
    }
    
    @Test
    public void deleteBucket() throws IOException {
        MockNetwork
            .expecting(HttpVerb.DELETE, "/bucketName", null, null)
            .returning(204, "")
            .asClient()
            .deleteBucket(new DeleteBucketRequest("bucketName"));
    }

    @Test
    public void deleteFolderRecursivelySpectraS3() throws IOException {
        final UUID bucketId = UUID.randomUUID();
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("bucket_id", bucketId.toString());
        queryParams.put("recursive", null);

        MockNetwork
                .expecting(HttpVerb.DELETE, "/_rest_/folder/folderName", queryParams, null)
                .returning(204, "")
                .asClient()
                .deleteFolderRecursivelySpectraS3(
                        new DeleteFolderRecursivelySpectraS3Request(bucketId.toString(), "folderName"));
    }

    @Test
    public void deleteObject() throws IOException {
        MockNetwork
            .expecting(HttpVerb.DELETE, "/bucketName/my/file.txt", null, null)
            .returning(204, "")
            .asClient()
            .deleteObject(new DeleteObjectRequest("bucketName", "my/file.txt"));
    }

    @Test
    public void multiObjectDelete() throws IOException {
        final List<String> objsToDelete = Lists.newArrayList("sample1.txt", "sample2.txt");
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("delete", null);
        final String payload = "<Delete><Quiet>false</Quiet><Object><Key>sample1.txt</Key></Object><Object><Key>sample2.txt</Key></Object></Delete>";

        final DeleteObjectsResponse response = MockNetwork
            .expecting(HttpVerb.POST, "/bucketName", queryParams, payload)
            .returning(200, "<DeleteResult>\n" +
                "  <Deleted>\n" +
                "    <Key>sample1.txt</Key>\n" +
                "  </Deleted>\n" +
                "  <Error>\n" +
                "    <Key>sample2.txt</Key>\n" +
                "    <Code>AccessDenied</Code>\n" +
                "    <Message>Access Denied</Message>\n" +
                "  </Error>\n" +
                "</DeleteResult>")
            .asClient()
            .deleteObjects(new DeleteObjectsRequest("bucketName", objsToDelete));
        assertThat(response.getDeleteResult().getDeletedObjects().size(), is(1));
        assertThat(response.getDeleteResult().getErrors().size(), is(1));
    }

    @Test(expected = FailedRequestException.class)
    public void getBadBucket() throws IOException {
        MockNetwork
            .expecting(HttpVerb.GET, "/remoteTest16", null, null)
            .returning(400, "")
            .asClient()
            .getBucket(new GetBucketRequest("remoteTest16"));
    }

    @Test
    public void getObject() throws IOException {
        final String jobIdString = "a4a586a1-cb80-4441-84e2-48974e982d51";
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("job", jobIdString);
        queryParams.put("offset", Long.toString(0));

        final ByteArraySeekableByteChannel resultChannel = new ByteArraySeekableByteChannel();
        final String stringResponse = "Response";
        MockNetwork
            .expecting(HttpVerb.GET, "/bucketName/object", queryParams, null)
            .returning(200, stringResponse)
            .asClient()
            .getObject(new GetObjectRequest(
                    "bucketName",
                    "object",
                    resultChannel,
                    jobIdString,
                    0));
        assertThat(resultChannel.toString(), is(stringResponse));
    }
    
    @Test
    public void createObject() throws IOException, URISyntaxException {
        final String jobIdString = "a4a586a1-cb80-4441-84e2-48974e982d51";
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("job", jobIdString);
        queryParams.put("offset", Long.toString(0));
        
        final Path resourcePath = ResourceUtils.loadFileResource("LoremIpsumTwice.txt");
        final byte[] fileBytes = Files.readAllBytes(resourcePath);
        final String output = new String(fileBytes, Charset.forName("UTF-8"));
        final FileChannel channel = FileChannel.open(resourcePath, StandardOpenOption.READ);
        MockNetwork
            .expecting(HttpVerb.PUT, "/bucketName/objectName", queryParams, output)
            .returning(200, "")
            .asClient()
            .putObject(new PutObjectRequest(
                    "bucketName",
                    "objectName",
                    channel,
                    jobIdString,
                    0,
                    fileBytes.length));
    }
    
    @Test
    public void createObjectWithMetadata() throws IOException, URISyntaxException {
        final String jobIdString = "a4a586a1-cb80-4441-84e2-48974e982d51";
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("job", jobIdString);
        queryParams.put("offset", Long.toString(0));
        
        final Path resourcePath = ResourceUtils.loadFileResource("LoremIpsumTwice.txt");
        final byte[] fileBytes = Files.readAllBytes(resourcePath);
        final String output = new String(fileBytes, Charset.forName("UTF-8"));
        final FileChannel channel = FileChannel.open(resourcePath, StandardOpenOption.READ);

        final PutObjectRequest por = new PutObjectRequest(
                "bucketName",
                "objectName",
                channel,
                jobIdString,
                0,
                fileBytes.length);
        
        final Multimap<String, String> expectedRequestHeaders = TreeMultimap.create();
        expectedRequestHeaders.put("Naming-Convention", "s3"); //default from AbstractRequest
        expectedRequestHeaders.put("x-amz-meta-test1", "test1value");
        expectedRequestHeaders.put("x-amz-meta-test2", "test2value");
        expectedRequestHeaders.put("x-amz-meta-test2", "test2value2");
        
        por.withMetaData("x-amz-meta-test1", "test1value");
        por.withMetaData("test2", "test2value");
        por.withMetaData("test2", "test2value2");
        MockNetwork
            .expecting(HttpVerb.PUT, "/bucketName/objectName", queryParams, expectedRequestHeaders, output)
            .returning(200, "")
            .asClient()
            .putObject(por);
    }

    @Test
    public void createObjectWithNullAndEmptyMetadata() throws IOException, URISyntaxException {
        final String jobIdString = "a4a586a1-cb80-4441-84e2-48974e982d51";
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("job", jobIdString);
        queryParams.put("offset", Long.toString(0));

        final Path resourcePath = ResourceUtils.loadFileResource("LoremIpsumTwice.txt");
        final byte[] fileBytes = Files.readAllBytes(resourcePath);
        final String output = new String(fileBytes, Charset.forName("UTF-8"));
        final FileChannel channel = FileChannel.open(resourcePath, StandardOpenOption.READ);

        final PutObjectRequest por = new PutObjectRequest(
                "bucketName",
                "objectName",
                channel,
                UUID.fromString(jobIdString),
                0,
                fileBytes.length);

        final Multimap<String, String> expectedRequestHeaders = TreeMultimap.create();
        expectedRequestHeaders.put("Naming-Convention", "s3"); //default from AbstractRequest

        por.withMetaData("test1", null);
        por.withMetaData("test2", "");

        MockNetwork
                .expecting(HttpVerb.PUT, "/bucketName/objectName", queryParams, expectedRequestHeaders, output)
                .returning(200, "")
                .asClient()
                .putObject(por);
    }

    @Test
    public void headObjectWithMetadata() throws IOException {

        final Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("x-amz-meta-key", "value");


        final HeadObjectRequest request = new HeadObjectRequest("bucket", "obj");

        MockNetwork
                .expecting(HttpVerb.HEAD, "/bucket/obj", null, null, null)
                .returning(200, "", responseHeaders)
                .asClient()
                .headObject(request);
    }

    @Test
    public void getObjectWithMetaData() throws IOException {

        final Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("x-amz-meta-key", "value");

        final ByteArraySeekableByteChannel resultChannel = new ByteArraySeekableByteChannel();
        final GetObjectRequest request = new GetObjectRequest(
                "bucket",
                "obj",
                resultChannel,
                UUID.randomUUID().toString(),
                0);

        MockNetwork
                .expecting(HttpVerb.GET, "/bucket/obj", null, null, null)
                .returning(200, "response", responseHeaders)
                .asClient()
                .getObject(request);

        assertThat(resultChannel.toString(), is("response"));
    }

    @Test
    public void createPutJobSpectraS3() throws IOException {
        this.runBulkTest(BulkCommand.PUT, (client, bucket, objects) -> client.putBulkJobSpectraS3(new PutBulkJobSpectraS3Request(bucket, objects)).getMasterObjectList());
    }

    @Test
    public void createGetJobSpectraS3() throws IOException {
        this.runBulkTest(BulkCommand.GET, (client, bucket, objects) -> client.getBulkJobSpectraS3(new GetBulkJobSpectraS3Request(bucket, objects)).getMasterObjectList());
    }
    
    private interface BulkTestDriver {
        MasterObjectList performRestCall(final Ds3Client client, final String bucket, final List<Ds3Object> objects)
                throws IOException;
    }
    
    private void runBulkTest(final BulkCommand command, final Ds3Client_Test.BulkTestDriver driver) throws IOException {
        final List<Ds3Object> objects = Arrays.asList(
            new Ds3Object("file1", 256),
            new Ds3Object("file2", 1202),
            new Ds3Object("file3", 2523)
        );

        final String expectedXmlBody;

        if (command == BulkCommand.GET) {
            expectedXmlBody = "<Objects><Object Name=\"file1\"/><Object Name=\"file2\"/><Object Name=\"file3\"/></Objects>";
        }
        else {
            expectedXmlBody = "<Objects><Object Name=\"file1\" Size=\"256\"/><Object Name=\"file2\" Size=\"1202\"/><Object Name=\"file3\" Size=\"2523\"/></Objects>";
        }

        final String xmlResponse = "<MasterObjectList BucketName=\"lib\" JobId=\"9652a41a-218a-4158-af1b-064ab9e4ef71\" Priority=\"NORMAL\" RequestType=\"PUT\" StartDate=\"2014-07-29T16:08:39.000Z\"><Nodes><Node EndPoint=\"FAILED_TO_DETERMINE_DATAPATH_IP_ADDRESS\" HttpPort=\"80\" HttpsPort=\"443\" Id=\"b18ee082-1352-11e4-945e-080027ebeb6d\"/></Nodes><Objects ChunkId=\"cfa3153f-57de-41c7-b1fb-f30fa4154232\" ChunkNumber=\"0\"><Object Name=\"file2\" InCache=\"false\" Length=\"1202\" Offset=\"0\"/><Object Name=\"file1\" InCache=\"false\" Length=\"256\" Offset=\"0\"/><Object Name=\"file3\" InCache=\"false\" Length=\"2523\" Offset=\"0\"/></Objects></MasterObjectList>";
        
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", command.toString());
        
        final Ds3Client client = MockNetwork
            .expecting(HttpVerb.PUT, "/_rest_/bucket/bulkTest", queryParams, expectedXmlBody)
            .returning(200, xmlResponse)
            .asClient();
        
        final List<Objects> objectListList = driver.performRestCall(client, "bulkTest", objects).getObjects();
        assertThat(objectListList.size(), is(1));
        
        final List<BulkObject> objectList = objectListList.get(0).getObjects();
        assertThat(objectList.size(), is(3));

        assertObjectEquals(objectList.get(0), "file2", 1202);
        assertObjectEquals(objectList.get(1), "file1", 256);
        assertObjectEquals(objectList.get(2), "file3", 2523);
    }
    
    private static void assertObjectEquals(final BulkObject object, final String name, final long size) {
        assertThat(object.getName(), is(name));
        assertThat(object.getLength(), is(size));
    }
    
    @Test
    public void allocateJobChunkSpectraS3() throws IOException {
        final String responseString =
            "<Objects ChunkId=\"203f6886-b058-4f7c-a012-8779176453b1\" ChunkNumber=\"3\" NodeId=\"a02053b9-0147-11e4-8d6a-002590c1177c\">"
            + "  <Object Name=\"client00obj000004-8000000\" InCache=\"true\" Length=\"5368709120\" Offset=\"0\"/>"
            + "  <Object Name=\"client00obj000004-8000000\" InCache=\"true\" Length=\"2823290880\" Offset=\"5368709120\"/>"
            + "  <Object Name=\"client00obj000003-8000000\" InCache=\"true\" Length=\"2823290880\" Offset=\"5368709120\"/>"
            + "  <Object Name=\"client00obj000003-8000000\" InCache=\"true\" Length=\"5368709120\" Offset=\"0\"/>"
            + "</Objects>";
        final UUID chunkId = UUID.fromString("203f6886-b058-4f7c-a012-8779176453b1");
        final UUID nodeId = UUID.fromString("a02053b9-0147-11e4-8d6a-002590c1177c");

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "allocate");
        final AllocateJobChunkSpectraS3Response response = MockNetwork
            .expecting(HttpVerb.PUT, "/_rest_/job_chunk/203f6886-b058-4f7c-a012-8779176453b1", queryParams, null)
            .returning(200, responseString)
            .asClient()
            .allocateJobChunkSpectraS3(new AllocateJobChunkSpectraS3Request(chunkId.toString()));
        
        assertThat(response.getStatus(), is(AllocateJobChunkSpectraS3Response.Status.ALLOCATED));
        final Objects chunk = response.getObjectsResult();
        
        assertThat(chunk.getChunkId(), is(chunkId));
        assertThat(chunk.getChunkNumber(), is(3));
        assertThat(chunk.getNodeId(), is(nodeId));
        
        final List<BulkObject> objects = chunk.getObjects();
        assertThat(objects.size(), is(4));

        final BulkObject object0 = objects.get(0);
        assertThat(object0.getName(), is("client00obj000004-8000000"));
        assertThat(object0.getInCache(), is(true));
        assertThat(object0.getOffset(), is(0L));
        assertThat(object0.getLength(), is(5368709120L));

        final BulkObject object1 = objects.get(1);
        assertThat(object1.getName(), is("client00obj000004-8000000"));
        assertThat(object1.getInCache(), is(true));
        assertThat(object1.getOffset(), is(5368709120L));
        assertThat(object1.getLength(), is(2823290880L));

        final BulkObject object2 = objects.get(2);
        assertThat(object2.getName(), is("client00obj000003-8000000"));
        assertThat(object2.getInCache(), is(true));
        assertThat(object2.getOffset(), is(5368709120L));
        assertThat(object2.getLength(), is(2823290880L));

        final BulkObject object3 = objects.get(3);
        assertThat(object3.getName(), is("client00obj000003-8000000"));
        assertThat(object3.getInCache(), is(true));
        assertThat(object3.getOffset(), is(0L));
        assertThat(object3.getLength(), is(5368709120L));
    }
    
    @Test
    public void allocateJobChunkSpectraS3ReturnsRetryAfter() throws IOException {
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "allocate");
        final Map<String, String> headers = new HashMap<>();
        headers.put("retry-after", "300");
        final AllocateJobChunkSpectraS3Response response = MockNetwork
            .expecting(HttpVerb.PUT, "/_rest_/job_chunk/203f6886-b058-4f7c-a012-8779176453b1", queryParams, null)
            .returning(307, "", headers)
            .asClient()
            .allocateJobChunkSpectraS3(new AllocateJobChunkSpectraS3Request("203f6886-b058-4f7c-a012-8779176453b1"));
        
        assertThat(response.getStatus(), is(AllocateJobChunkSpectraS3Response.Status.RETRYLATER));
        assertThat(response.getRetryAfterSeconds(), is(300));
    }
    
    @Test
    public void getJobChunksReadyForClientProcessingSpectraS3() throws IOException, ParseException {

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("job", MASTER_OBJECT_LIST_JOB_ID.toString());
        final GetJobChunksReadyForClientProcessingSpectraS3Response response = MockNetwork
            .expecting(HttpVerb.GET, "/_rest_/job_chunk", queryParams, null)
            .returning(200, MASTER_OBJECT_LIST_XML)
            .asClient()
            .getJobChunksReadyForClientProcessingSpectraS3(new GetJobChunksReadyForClientProcessingSpectraS3Request(MASTER_OBJECT_LIST_JOB_ID.toString()));
        
        assertThat(response.getStatus(), is(GetJobChunksReadyForClientProcessingSpectraS3Response.Status.AVAILABLE));

        checkJobWithChunksApiBean(response.getMasterObjectListResult());
    }

    @Test
    public void getJobChunksReadyForClientProcessingSpectraS3ReturnsRetryLater() throws IOException {
        final String responseString =
            "<MasterObjectList BucketName=\"bucket8192000000\" JobId=\"1a85e743-ec8f-4789-afec-97e587a26936\" Priority=\"NORMAL\" RequestType=\"GET\" StartDate=\"2014-07-01T20:12:52.000Z\">"
            + "  <Nodes>"
            + "    <Node EndPoint=\"10.1.18.12\" HttpPort=\"80\" HttpsPort=\"443\" Id=\"a02053b9-0147-11e4-8d6a-002590c1177c\"/>"
            + "    <Node EndPoint=\"10.1.18.13\" HttpsPort=\"443\" Id=\"95e97010-8e70-4733-926c-aeeb21796848\"/>"
            + "  </Nodes>"
            + "</MasterObjectList>";
        final UUID jobId = UUID.fromString("1a85e743-ec8f-4789-afec-97e587a26936");

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("job", jobId.toString());
        final Map<String, String> headers = new HashMap<>();
        headers.put("Retry-After", "300");
        final GetJobChunksReadyForClientProcessingSpectraS3Response response = MockNetwork
            .expecting(HttpVerb.GET, "/_rest_/job_chunk", queryParams, null)
            .returning(200, responseString, headers)
            .asClient()
            .getJobChunksReadyForClientProcessingSpectraS3(new GetJobChunksReadyForClientProcessingSpectraS3Request(jobId.toString()));
        
        assertThat(response.getStatus(), is(GetJobChunksReadyForClientProcessingSpectraS3Response.Status.RETRYLATER));
        assertThat(response.getRetryAfterSeconds(), is(300));
    }
    
    @Test
    public void getJobsSpectraS3() throws IOException, ParseException {
        final String responseString =
            "<Jobs>"
            + "  <Job BucketName=\"bucket_1\" CachedSizeInBytes=\"69880\" ChunkClientProcessingOrderGuarantee=\"IN_ORDER\" CompletedSizeInBytes=\"0\" JobId=\"0807ff11-a9f6-4d55-bb92-b452c1bb00c7\" OriginalSizeInBytes=\"69880\" Priority=\"NORMAL\" RequestType=\"PUT\" StartDate=\"2014-09-04T17:23:45.000Z\" UserId=\"a7d3eff9-e6d2-4e37-8a0b-84e76211a18a\" UserName=\"spectra\">"
            + "    <Nodes>"
            + "      <Node EndPoint=\"10.10.10.10\" HttpPort=\"80\" HttpsPort=\"443\" Id=\"edb8cc38-32f2-11e4-bce1-080027ecf0d4\"/>"
            + "    </Nodes>"
            + "  </Job>"
            + "  <Job BucketName=\"bucket_2\" CachedSizeInBytes=\"0\" ChunkClientProcessingOrderGuarantee=\"IN_ORDER\" CompletedSizeInBytes=\"0\" JobId=\"c18554ba-e3a8-4905-91fd-3e6eec71bf45\" OriginalSizeInBytes=\"69880\" Priority=\"HIGH\" RequestType=\"GET\" StartDate=\"2014-09-04T17:24:04.000Z\" UserId=\"a7d3eff9-e6d2-4e37-8a0b-84e76211a18a\" UserName=\"spectra\">"
            + "    <Nodes>"
            + "      <Node EndPoint=\"10.10.10.10\" HttpPort=\"80\" HttpsPort=\"443\" Id=\"edb8cc38-32f2-11e4-bce1-080027ecf0d4\"/>"
            + "    </Nodes>"
            + "  </Job>"
            + "</Jobs>";
        final GetJobsSpectraS3Response response = MockNetwork
            .expecting(HttpVerb.GET, "/_rest_/job", null, null)
            .returning(200, responseString)
            .asClient()
            .getJobsSpectraS3(new GetJobsSpectraS3Request());
        
        final List<Job> jobs = response.getJobListResult().getJobs();
        //final Date date = this.dateFormat.parse("2014-09-04T17:23:45.000Z");
        assertThat(jobs.size(), is(2));
        checkJob(
                jobs.get(0),
                "bucket_1",
                69880L,
                JobChunkClientProcessingOrderGuarantee.IN_ORDER,
                0L,
                UUID.fromString("0807ff11-a9f6-4d55-bb92-b452c1bb00c7"),
                69880L,
                Priority.NORMAL,
                JobRequestType.PUT,
                //date,
                DATE_FORMAT.parse("2014-09-04T17:23:45.000Z"),
                UUID.fromString("a7d3eff9-e6d2-4e37-8a0b-84e76211a18a"),
                "spectra"
        );
        checkJob(
                jobs.get(1),
                "bucket_2",
                0L,
                JobChunkClientProcessingOrderGuarantee.IN_ORDER,
                0L,
                UUID.fromString("c18554ba-e3a8-4905-91fd-3e6eec71bf45"),
                69880L,
                Priority.HIGH,
                JobRequestType.GET,
                DATE_FORMAT.parse("2014-09-04T17:24:04.000Z"),
                UUID.fromString("a7d3eff9-e6d2-4e37-8a0b-84e76211a18a"),
                "spectra"
        );
    }

    private static void checkJob(
            final Job job,
            final String bucketName,
            final long cachedSizeInBytes,
            final JobChunkClientProcessingOrderGuarantee chunkProcessingOrderGuarantee,
            final long completedSizeInBytes, final UUID jobId,
            final long originalSizeInBytes, final Priority priority,
            final JobRequestType requestType, final Date startDate,
            final UUID userId, final String userName) {
        assertThat(job.getBucketName(), is(bucketName));
        assertThat(job.getCachedSizeInBytes(), is(cachedSizeInBytes));
        assertThat(job.getChunkClientProcessingOrderGuarantee(), is(chunkProcessingOrderGuarantee));
        assertThat(job.getCompletedSizeInBytes(), is(completedSizeInBytes));
        assertThat(job.getJobId(), is(jobId));
        assertThat(job.getOriginalSizeInBytes(), is(originalSizeInBytes));
        assertThat(job.getPriority(), is(priority));
        assertThat(job.getRequestType(), is(requestType));
        assertThat(job.getStartDate(), is(startDate));
        assertThat(job.getUserId(), is(userId));
        assertThat(job.getUserName(), is(userName));
        final JobNode node = job.getNodes().get(0);
        assertThat(node.getEndPoint(), is("10.10.10.10"));
        assertThat(node.getHttpPort(), is(80));
        assertThat(node.getHttpsPort(), is(443));
    }
    
    @Test
    public void getJobSpectraS3() throws IOException, ParseException {
        checkJobWithChunksApiBean(
                MockNetwork
                        .expecting(HttpVerb.GET, "/_rest_/job/1a85e743-ec8f-4789-afec-97e587a26936", null, null)
                        .returning(200, MASTER_OBJECT_LIST_XML)
                        .asClient()
                        .getJobSpectraS3(new GetJobSpectraS3Request("1a85e743-ec8f-4789-afec-97e587a26936"))
                        .getMasterObjectListResult()
        );
    }

    private static void checkJobWithChunksApiBean(final MasterObjectList masterObjectList) throws ParseException {
        assertThat(masterObjectList.getBucketName(), is("bucket8192000000"));
        assertThat(masterObjectList.getJobId(), is(MASTER_OBJECT_LIST_JOB_ID));
        assertThat(masterObjectList.getPriority(), is(Priority.NORMAL));
        assertThat(masterObjectList.getRequestType(), is(JobRequestType.GET));
        assertThat(masterObjectList.getStartDate(), is(DATE_FORMAT.parse("2014-07-01T20:12:52.000Z")));

        final List<JobNode> nodes = masterObjectList.getNodes();
        assertThat(nodes.size(), is(2));
        final JobNode node0 = nodes.get(0);
        assertThat(node0.getEndPoint(), is("10.1.18.12"));
        assertThat(node0.getHttpPort(), is(80));
        assertThat(node0.getHttpsPort(), is(443));
        final JobNode node1 = nodes.get(1);
        assertThat(node1.getEndPoint(), is("10.1.18.13"));
        assertThat(node1.getHttpPort(), is(nullValue()));
        assertThat(node1.getHttpsPort(), is(443)); 
        
        final List<Objects> chunkList = masterObjectList.getObjects();
        assertThat(chunkList.size(), is(2));

        final Objects chunk0 = chunkList.get(0);
        assertThat(chunk0.getChunkId(), is(UUID.fromString("f58370c2-2538-4e78-a9f8-e4d2676bdf44")));
        assertThat(chunk0.getChunkNumber(), is(0));
        assertThat(chunk0.getNodeId(), is(UUID.fromString("a02053b9-0147-11e4-8d6a-002590c1177c")));
        final List<BulkObject> objects0 = chunk0.getObjects();
        assertThat(objects0.size(), is(2));
        final BulkObject bulkObject0_0 = objects0.get(0);
        assertThat(bulkObject0_0.getName(), is("client00obj000004-8000000"));
        assertThat(bulkObject0_0.getOffset(), is(0L));
        assertThat(bulkObject0_0.getLength(), is(5368709120L));
        assertThat(bulkObject0_0.getInCache(), is(true));
        final BulkObject bulkObject0_1 = objects0.get(1);
        assertThat(bulkObject0_1.getName(), is("client00obj000004-8000000"));
        assertThat(bulkObject0_1.getOffset(), is(5368709120L));
        assertThat(bulkObject0_1.getLength(), is(2823290880L));
        assertThat(bulkObject0_1.getInCache(), is(true));

        final Objects chunk1 = chunkList.get(1);
        assertThat(chunk1.getChunkId(), is(UUID.fromString("4137d768-25bb-4942-9d36-b92dfbe75e01")));
        assertThat(chunk1.getChunkNumber(), is(1));
        assertThat(chunk1.getNodeId(), is(UUID.fromString("95e97010-8e70-4733-926c-aeeb21796848")));
        final List<BulkObject> objects1 = chunk1.getObjects();
        assertThat(objects1.size(), is(2));
        final BulkObject bulkObject1_0 = objects1.get(0);
        assertThat(bulkObject1_0.getName(), is("client00obj000008-8000000"));
        assertThat(bulkObject1_0.getOffset(), is(5368709120L));
        assertThat(bulkObject1_0.getLength(), is(2823290880L));
        assertThat(bulkObject1_0.getInCache(), is(true));
        final BulkObject bulkObject1_1 = objects1.get(1);
        assertThat(bulkObject1_1.getName(), is("client00obj000008-8000000"));
        assertThat(bulkObject1_1.getOffset(), is(0L));
        assertThat(bulkObject1_1.getLength(), is(5368709120L));
        assertThat(bulkObject1_1.getInCache(), is(true));
    }
    
    @Test
    public void cancelJobSpectraS3() throws IOException {
        final CancelJobSpectraS3Response response = MockNetwork
            .expecting(HttpVerb.DELETE, "/_rest_/job/1a85e743-ec8f-4789-afec-97e587a26936", null, null)
            .returning(204, "")
            .asClient()
            .cancelJobSpectraS3(new CancelJobSpectraS3Request("1a85e743-ec8f-4789-afec-97e587a26936"));
        assertThat(response, notNullValue());
    }
    
    @Test
    public void modifyJobSpectraS3() throws IOException, ParseException {
        checkJobWithChunksApiBean(
                MockNetwork
                        .expecting(HttpVerb.PUT, "/_rest_/job/1a85e743-ec8f-4789-afec-97e587a26936", null, null)
                        .returning(200, MASTER_OBJECT_LIST_XML)
                        .asClient()
                        .modifyJobSpectraS3(new ModifyJobSpectraS3Request(MASTER_OBJECT_LIST_JOB_ID.toString()))
                        .getMasterObjectListResult()
        );
    }

    @Test
    public void deleteTapeDriveSpectraS3() throws IOException {
        final DeleteTapeDriveSpectraS3Response response = MockNetwork
            .expecting(HttpVerb.DELETE, "/_rest_/tape_drive/30a8dbf8-12e1-49dd-bede-0b4a7e1dd773", null, null)
            .returning(204, "")
            .asClient()
            .deleteTapeDriveSpectraS3(new DeleteTapeDriveSpectraS3Request("30a8dbf8-12e1-49dd-bede-0b4a7e1dd773"));
        assertThat(response, notNullValue());
    }

    @Test
    public void deleteTapePartitionSpectraS3() throws IOException {
        final DeleteTapePartitionSpectraS3Response response = MockNetwork
            .expecting(HttpVerb.DELETE, "/_rest_/tape_partition/30a8dbf8-12e1-49dd-bede-0b4a7e1dd773", null, null)
            .returning(204, "")
            .asClient()
            .deleteTapePartitionSpectraS3(new DeleteTapePartitionSpectraS3Request("30a8dbf8-12e1-49dd-bede-0b4a7e1dd773"));
        assertThat(response, notNullValue());
    }
    @Test
    public void newForNode() {
        final Ds3Client client = Ds3ClientBuilder.create("endpoint", new Credentials("access", "key")).build();

        final JobNode node = new JobNode();
        node.setEndPoint("newEndpoint");
        node.setHttpPort(80);
        node.setHttpsPort(443);

        final Ds3Client newClient = client.newForNode(node);
        assertThat(newClient.getConnectionDetails().getEndpoint(), is("newEndpoint:443"));
        assertThat(newClient.getConnectionDetails().getCredentials().getClientId(), is("access"));
        assertThat(newClient.getConnectionDetails().getCredentials().getKey(), is("key"));
    }

    @Test
    public void testSettingUserAgent() {
        final String userAgent = "Gracie Eskimo";

        final Ds3Client client = Ds3ClientBuilder.create("endpoint", new Credentials("access", "key"))
                .withUserAgent(userAgent)
                .build();

        final JobNode node = new JobNode();
        node.setEndPoint("newEndpoint");
        node.setHttpPort(80);
        node.setHttpsPort(443);

        final Ds3Client newClient = client.newForNode(node);
        assertThat(newClient.getConnectionDetails().getEndpoint(), is("newEndpoint:443"));
        assertThat(newClient.getConnectionDetails().getCredentials().getClientId(), is("access"));
        assertThat(newClient.getConnectionDetails().getCredentials().getKey(), is("key"));
        assertThat(newClient.getConnectionDetails().getUserAgent(), is(userAgent));
    }

    @Test
    public void testGettingDefaultUserAgent() {
        final Ds3Client client = Ds3ClientBuilder.create("endpoint", new Credentials("access", "key"))
                .build();

        final JobNode node = new JobNode();
        node.setEndPoint("newEndpoint");
        node.setHttpPort(80);
        node.setHttpsPort(443);

        final Ds3Client newClient = client.newForNode(node);
        assertThat(newClient.getConnectionDetails().getEndpoint(), is("newEndpoint:443"));
        assertThat(newClient.getConnectionDetails().getCredentials().getClientId(), is("access"));
        assertThat(newClient.getConnectionDetails().getCredentials().getKey(), is("key"));

        final String userAgent = newClient.getConnectionDetails().getUserAgent();
        final String[] userAgentFields = userAgent.split("-");

        assertThat(userAgentFields.length, is(greaterThanOrEqualTo(2)));

        // look for a pattern like 3.4.0, but leave open the possibility of a string like 3.4.0-SNAPSHOT
        final Pattern matchPattern = Pattern.compile("\\d+\\.\\d+\\.\\d+");
        final Matcher matcher = matchPattern.matcher(userAgentFields[1]);

        assertThat(matcher.find(), is(true));
    }

    @Test
    public void VerifySystemHealthSpectraS3() throws IOException {
        final String responsePayload = "<Data><MsRequiredToVerifyDataPlannerHealth>0</MsRequiredToVerifyDataPlannerHealth></Data>";

        final VerifySystemHealthSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/system_health", null, null)
                .returning(200, responsePayload)
                .asClient()
                .verifySystemHealthSpectraS3(new VerifySystemHealthSpectraS3Request());

        assertThat(response.getHealthVerificationResult(), is(notNullValue()));
        assertThat(response.getHealthVerificationResult().getMsRequiredToVerifyDataPlannerHealth(), is(0L));
    }

    @Test
    public void systemInformationSpectraS3() throws IOException {
        final String responsePayload = "<Data><ApiVersion>518B3F2A95B71AC7325EFB12B2937376.15F3CC0489CBCD4648ECFF0FBF371B8A</ApiVersion><BuildInformation><Branch/><Revision/><Version/></BuildInformation><SerialNumber>UNKNOWN</SerialNumber></Data>";

        final GetSystemInformationSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/system_information", null, null)
                .returning(200, responsePayload)
                .asClient()
                .getSystemInformationSpectraS3(new GetSystemInformationSpectraS3Request());

        assertThat(response.getSystemInformationResult(), is(notNullValue()));
    }

    @Test
    public void getTapeLibrariesSpectraS3() throws IOException {
        final String responsePayload = "<Data><TapeLibrary><Id>f4dae25d-e52a-4430-82bd-525e4f15493c</Id><ManagementUrl>a</ManagementUrl><Name>test library</Name><SerialNumber>test library</SerialNumber></TapeLibrary><TapeLibrary><Id>82bdab72-d79a-4b43-95d7-f2c16cd9aa45</Id><ManagementUrl>a</ManagementUrl><Name>test library 2</Name><SerialNumber>test library 2</SerialNumber></TapeLibrary></Data>";

        final Map<String, String> headers = new HashMap<>();
        headers.put("page-truncated", "2");
        headers.put("total-result-count", "3");
        final GetTapeLibrariesSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/tape_library", null, null)
                .returning(200, responsePayload, headers)
                .asClient()
                .getTapeLibrariesSpectraS3(new GetTapeLibrariesSpectraS3Request());

        assertThat(response.getPagingTotalResultCount(), is(3));
        assertThat(response.getPagingTruncated(), is(2));

        final List<TapeLibrary> libraries = response.getTapeLibraryListResult().getTapeLibraries();

        assertThat(libraries.size(), is(2));
        assertThat(libraries.get(0).getId().toString(), is("f4dae25d-e52a-4430-82bd-525e4f15493c"));
    }

    @Test
    public void getTapeLibrarySpectraS3() throws IOException {
        final String responsePayload = "<Data><Id>e23030e5-9b8d-4594-bdd1-15d3c45abb9f</Id><ManagementUrl>a</ManagementUrl><Name>125ca16e-60e3-43b2-a26f-0bc81843745f</Name><SerialNumber>test library</SerialNumber></Data>";

        final GetTapeLibrarySpectraS3Response response = MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/tape_library/e23030e5-9b8d-4594-bdd1-15d3c45abb9f", null, null)
                .returning(200, responsePayload)
                .asClient()
                .getTapeLibrarySpectraS3(new GetTapeLibrarySpectraS3Request("e23030e5-9b8d-4594-bdd1-15d3c45abb9f"));

        assertThat(response.getTapeLibraryResult(), is(notNullValue()));
        assertThat(response.getTapeLibraryResult().getId().toString(), is("e23030e5-9b8d-4594-bdd1-15d3c45abb9f"));
    }

    @Test
    public void getTapeFailures() throws IOException {
        final String responsePayload = "<Data><TapeFailure><Date>2015-03-11T16:23:29.741</Date><ErrorMessage>AAA</ErrorMessage><Id>375ae624-d39f-47d8-95c0-0aaec4494ad2</Id><TapeDriveId>b06c8900-6d88-4a29-9a03-d0c4494b29ff</TapeDriveId><TapeId>badbb1e7-8654-4b38-8d3b-112c9fd68d58</TapeId><Type>BLOB_READ_FAILED</Type></TapeFailure></Data>";

        final Map<String, String> headers = new HashMap<>();
        headers.put("page-truncated", "2");
        headers.put("total-result-count", "3");
        final GetTapeFailuresSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/tape_failure", null, null)
                .returning(200, responsePayload, headers)
                .asClient()
                .getTapeFailuresSpectraS3(new GetTapeFailuresSpectraS3Request());

        assertThat(response.getPagingTruncated(), is(2));
        assertThat(response.getPagingTotalResultCount(), is(3));

        final List<DetailedTapeFailure> tapeFailures = response.getDetailedTapeFailureListResult().getDetailedTapeFailures();

        assertThat(tapeFailures, is(notNullValue()));
        assertThat(tapeFailures.size(), is(1));
        assertThat(tapeFailures.get(0).getId().toString(), is("375ae624-d39f-47d8-95c0-0aaec4494ad2"));
    }

    @Test
    public void getTapeDrivesSpectraS3() throws IOException {
        final String responsePayload = "<Data><TapeDrive><ErrorMessage/><ForceTapeRemoval>false</ForceTapeRemoval><Id>ebeb0ec7-7912-4870-a0da-bbeb270ac049</Id><PartitionId>aa947aaa-23bf-4301-8173-2553bb1a3f1c</PartitionId><SerialNumber>test tape drive</SerialNumber><State>NORMAL</State><TapeId>b9085cd3-f1fd-4193-8763-c013d25cd135</TapeId><Type>UNKNOWN</Type></TapeDrive><TapeDrive><ErrorMessage/><ForceTapeRemoval>false</ForceTapeRemoval><Id>5dc2add1-b6e7-42a9-b551-a46339176c4b</Id><PartitionId>aa947aaa-23bf-4301-8173-2553bb1a3f1c</PartitionId><SerialNumber>test tape drive 2</SerialNumber><State>NORMAL</State><TapeId/><Type>UNKNOWN</Type></TapeDrive></Data>";

        final Map<String, String> headers = new HashMap<>();
        headers.put("page-truncated", "2");
        headers.put("total-result-count", "3");
        final GetTapeDrivesSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/tape_drive", null, null)
                .returning(200, responsePayload, headers)
                .asClient()
                .getTapeDrivesSpectraS3(new GetTapeDrivesSpectraS3Request());

        assertThat(response.getPagingTotalResultCount(), is(3));
        assertThat(response.getPagingTruncated(), is(2));

        final List<TapeDrive> tapeDrives = response.getTapeDriveListResult().getTapeDrives();

        assertThat(tapeDrives, is(notNullValue()));
        assertThat(tapeDrives.size(), is(2));
        assertThat(tapeDrives.get(0).getId().toString(), is("ebeb0ec7-7912-4870-a0da-bbeb270ac049"));
    }

    @Test
    public void getTapeDriveSpectraS3() throws IOException {
        final String responsePayload = "<Data><ErrorMessage/><ForceTapeRemoval>false</ForceTapeRemoval><Id>ff5df6c8-7e24-4e4f-815d-a8a1a4cddc98</Id><PartitionId>ca69b187-47cf-425e-b92f-c09bacc7d3b3</PartitionId><SerialNumber>test tape drive</SerialNumber><State>NORMAL</State><TapeId>0ea07c32-8ff6-443f-b7c8-420667b0df84</TapeId><Type>UNKNOWN</Type></Data>";

        final GetTapeDriveSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/tape_drive/ff5df6c8-7e24-4e4f-815d-a8a1a4cddc98", null, null)
                .returning(200, responsePayload)
                .asClient()
                .getTapeDriveSpectraS3(new GetTapeDriveSpectraS3Request("ff5df6c8-7e24-4e4f-815d-a8a1a4cddc98"));

        final TapeDrive tapeDrive = response.getTapeDriveResult();

        assertThat(tapeDrive, is(notNullValue()));
        assertThat(tapeDrive.getId().toString(), is("ff5df6c8-7e24-4e4f-815d-a8a1a4cddc98"));
    }

    @Test
    public void getTapesSpectraS3() throws IOException {
        final String responsePayload = "<Data><Tape><AssignedToStorageDomain>false</AssignedToStorageDomain><AvailableRawCapacity>2408082046976</AvailableRawCapacity><BarCode>101000L6</BarCode><BucketId/><DescriptionForIdentification/><EjectDate/><EjectLabel/><EjectLocation/><EjectPending/><FullOfData>false</FullOfData><Id>c7c431df-f95d-4533-b350-ffd7a8a5caac</Id><LastAccessed>2015-09-04T06:53:08.236</LastAccessed><LastCheckpoint>eb77ea67-3c83-47ec-8714-cd46a97dc392:2</LastCheckpoint><LastModified>2015-08-21T16:14:30.714</LastModified><LastVerified/><PartitionId>4f8a5cbb-9837-41d9-afd1-cebed41f18f7</PartitionId><PreviousState/><SerialNumber>HP-W130501213</SerialNumber><State>NORMAL</State><TotalRawCapacity>2408088338432</TotalRawCapacity><Type>LTO6</Type><WriteProtected>false</WriteProtected></Tape></Data>";

        final Map<String, String> headers = new HashMap<>();
        headers.put("page-truncated", "2");
        headers.put("total-result-count", "3");
        final GetTapesSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/tape", null, null)
                .returning(200, responsePayload, headers)
                .asClient()
                .getTapesSpectraS3(new GetTapesSpectraS3Request());

        assertThat(response.getPagingTotalResultCount(), is(3));
        assertThat(response.getPagingTruncated(), is(2));

        final List<Tape> tapes = response.getTapeListResult().getTapes();

        assertThat(tapes.size(), is(not(0)));
        assertThat(tapes.get(0).getId(), is(notNullValue()));
    }

    @Test
    public void deletePermanentlyLostTapeSpectraS3() throws IOException {

        final UUID id = UUID.randomUUID();

        final DeletePermanentlyLostTapeSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.DELETE, "/_rest_/tape/" + id.toString(), null, null)
                .returning(204, "")
                .asClient()
                .deletePermanentlyLostTapeSpectraS3(new DeletePermanentlyLostTapeSpectraS3Request(id.toString()));

        assertThat(response, is(notNullValue()));
    }

    @Test
    public void getTapeSpectraS3() throws IOException {
        final String responsePayload = "<Data><AssignedToStorageDomain>false</AssignedToStorageDomain><AvailableRawCapacity>2408082046976</AvailableRawCapacity><BarCode>101000L6</BarCode><BucketId/><DescriptionForIdentification/><EjectDate/><EjectLabel/><EjectLocation/><EjectPending/><FullOfData>false</FullOfData><Id>c7c431df-f95d-4533-b350-ffd7a8a5caac</Id><LastAccessed>2015-09-04T06:53:08.236</LastAccessed><LastCheckpoint>eb77ea67-3c83-47ec-8714-cd46a97dc392:2</LastCheckpoint><LastModified>2015-08-21T16:14:30.714</LastModified><LastVerified/><PartitionId>4f8a5cbb-9837-41d9-afd1-cebed41f18f7</PartitionId><PreviousState/><SerialNumber>HP-W130501213</SerialNumber><State>NORMAL</State><TotalRawCapacity>2408088338432</TotalRawCapacity><Type>LTO6</Type><WriteProtected>false</WriteProtected></Data>";

        final GetTapeSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/tape/c7c431df-f95d-4533-b350-ffd7a8a5caac", null, null)
                .returning(200, responsePayload)
                .asClient()
                .getTapeSpectraS3(new GetTapeSpectraS3Request("c7c431df-f95d-4533-b350-ffd7a8a5caac"));

        final Tape tape = response.getTapeResult();

        assertThat(tape.getId(), is(notNullValue()));
        assertThat(tape.getId(), is(UUID.fromString("c7c431df-f95d-4533-b350-ffd7a8a5caac")));
    }

    @Test(expected = ContentLengthNotMatchException.class)
    public void getObjectVerifyFullPayload() throws IOException {
        final String jobIdString = "a4a586a1-cb80-4441-84e2-48974e982d51";
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("job", jobIdString);
        queryParams.put("offset", Long.toString(0));

        final Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Length", "4");

        final ByteArraySeekableByteChannel resultChannel = new ByteArraySeekableByteChannel();
        final String stringResponse = "Response";

        MockNetwork
                .expecting(HttpVerb.GET, "/bucketName/object", queryParams, null)
                .returning(200, stringResponse, responseHeaders)
                .asClient()
                .getObject(new GetObjectRequest(
                        "bucketName",
                        "object",
                        resultChannel,
                        jobIdString,
                        0));
    }

    @Test
    public void clearSuspectBlobAzureTargetsSpectraS3Test() throws IOException {
        MockNetwork
                .expecting(HttpVerb.DELETE, "/_rest_/suspect_blob_azure_target", new HashMap<>(), IDS_REQUEST_PAYLOAD)
                .returning(204, "")
                .asClient()
                .clearSuspectBlobAzureTargetsSpectraS3(new ClearSuspectBlobAzureTargetsSpectraS3Request(IDS_REQUEST_PAYLOAD_LIST));
    }

    @Test
    public void clearSuspectBlobPoolsSpectraS3Test() throws IOException {
        MockNetwork
                .expecting(HttpVerb.DELETE, "/_rest_/suspect_blob_pool", new HashMap<>(), IDS_REQUEST_PAYLOAD)
                .returning(204, "")
                .asClient()
                .clearSuspectBlobPoolsSpectraS3(new ClearSuspectBlobPoolsSpectraS3Request(IDS_REQUEST_PAYLOAD_LIST));
    }

    @Test
    public void clearSuspectBlobS3TargetsSpectraS3Test() throws IOException {
        MockNetwork
                .expecting(HttpVerb.DELETE, "/_rest_/suspect_blob_s3_target", new HashMap<>(), IDS_REQUEST_PAYLOAD)
                .returning(204, "")
                .asClient()
                .clearSuspectBlobS3TargetsSpectraS3(new ClearSuspectBlobS3TargetsSpectraS3Request(IDS_REQUEST_PAYLOAD_LIST));
    }

    @Test
    public void clearSuspectBlobTapesSpectraS3Test() throws IOException {
        MockNetwork
                .expecting(HttpVerb.DELETE, "/_rest_/suspect_blob_tape", new HashMap<>(), IDS_REQUEST_PAYLOAD)
                .returning(204, "")
                .asClient()
                .clearSuspectBlobTapesSpectraS3(new ClearSuspectBlobTapesSpectraS3Request(IDS_REQUEST_PAYLOAD_LIST));
    }

    @Test
    public void markSuspectBlobAzureTargetsAsDegradedSpectraS3Test() throws IOException {
        MockNetwork
                .expecting(HttpVerb.PUT, "/_rest_/suspect_blob_azure_target", new HashMap<>(), IDS_REQUEST_PAYLOAD)
                .returning(204, "")
                .asClient()
                .markSuspectBlobAzureTargetsAsDegradedSpectraS3(new MarkSuspectBlobAzureTargetsAsDegradedSpectraS3Request(IDS_REQUEST_PAYLOAD_LIST));
    }

    @Test
    public void markSuspectBlobDs3TargetsAsDegradedSpectraS3Test() throws IOException {
        MockNetwork
                .expecting(HttpVerb.PUT, "/_rest_/suspect_blob_ds3_target", new HashMap<>(), IDS_REQUEST_PAYLOAD)
                .returning(204, "")
                .asClient()
                .markSuspectBlobDs3TargetsAsDegradedSpectraS3(new MarkSuspectBlobDs3TargetsAsDegradedSpectraS3Request(IDS_REQUEST_PAYLOAD_LIST));
    }

    @Test
    public void markSuspectBlobPoolsAsDegradedSpectraS3Test() throws IOException {
        MockNetwork
                .expecting(HttpVerb.PUT, "/_rest_/suspect_blob_pool", new HashMap<>(), IDS_REQUEST_PAYLOAD)
                .returning(204, "")
                .asClient()
                .markSuspectBlobPoolsAsDegradedSpectraS3(new MarkSuspectBlobPoolsAsDegradedSpectraS3Request(IDS_REQUEST_PAYLOAD_LIST));
    }

    @Test
    public void markSuspectBlobS3TargetsAsDegradedSpectraS3Test() throws IOException {
        MockNetwork
                .expecting(HttpVerb.PUT, "/_rest_/suspect_blob_s3_target", new HashMap<>(), IDS_REQUEST_PAYLOAD)
                .returning(204, "")
                .asClient()
                .markSuspectBlobS3TargetsAsDegradedSpectraS3(new MarkSuspectBlobS3TargetsAsDegradedSpectraS3Request(IDS_REQUEST_PAYLOAD_LIST));
    }

    @Test
    public void markSuspectBlobTapesAsDegradedSpectraS3Test() throws IOException {
        MockNetwork
                .expecting(HttpVerb.PUT, "/_rest_/suspect_blob_tape", new HashMap<>(), IDS_REQUEST_PAYLOAD)
                .returning(204, "")
                .asClient()
                .markSuspectBlobTapesAsDegradedSpectraS3(new MarkSuspectBlobTapesAsDegradedSpectraS3Request(IDS_REQUEST_PAYLOAD_LIST));
    }

    @Test
    public void completeMultiPartUploadTest() throws IOException {
        final String expectedRequestContent = "<CompleteMultipartUpload><Part><PartNumber>1</PartNumber><ETag>7a112844c1a2327e617f530cb06dccf8</ETag></Part><Part><PartNumber>2</PartNumber><ETag>7162e29f4e40da7f521d0794b57770ba</ETag></Part></CompleteMultipartUpload>";
        final String expectedResponse = "<CompleteMultipartUploadResult><Location>http://my-server/bucketName/object</Location><Bucket>bucketName</Bucket><Key>object</Key><ETag>b54357faf0632cce46e942fa68356b38</ETag></CompleteMultipartUploadResult>";

        final List<Part> parts = new ArrayList<>();
        parts.add(new Part(1, "7a112844c1a2327e617f530cb06dccf8"));
        parts.add(new Part(2, "7162e29f4e40da7f521d0794b57770ba"));

        final String bucketName = "bucketName";
        final String objectName = "object";
        final String uploadId = "VXBsb2FkIElEIGZvciA2aWWpbmcncyBteS1tb3ZpZS5tMnRzIHVwbG9hZA";
        final String etag = "b54357faf0632cce46e942fa68356b38";
        final String location = "http://my-server/bucketName/object";

        final CompleteMultipartUpload completeMultipartUpload = new CompleteMultipartUpload(parts);

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("upload_id", uploadId);

        final Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("etag", etag);

        final CompleteMultiPartUploadResponse response = MockNetwork
                .expecting(HttpVerb.POST, "/bucketName/object", queryParams, expectedRequestContent)
                .returning(200, expectedResponse, responseHeaders)
                .asClient()
                .completeMultiPartUpload(new CompleteMultiPartUploadRequest(
                        bucketName,
                        objectName,
                        completeMultipartUpload,
                        uploadId));

        assertThat(response.getCompleteMultipartUploadResult().getLocation(), is(location));
        assertThat(response.getCompleteMultipartUploadResult().getBucket(), is(bucketName));
        assertThat(response.getCompleteMultipartUploadResult().getKey(), is(objectName));
        assertThat(response.getCompleteMultipartUploadResult().getETag(), is(etag));
    }

    @Test
    public void putMultiPartUploadPartTest() throws IOException {
        final String requestContent = "this is the part content";
        final String bucketName = "bucketName";
        final String objectName = "object";
        final int partNumber = 2;
        final String uploadId = "VXBsb2FkIElEIGZvciA2aWWpbmcncyBteS1tb3ZpZS5tMnRzIHVwbG9hZA";
        final String eTag = "b54357faf0632cce46e942fa68356b38";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("part_number", String.valueOf(partNumber));
        queryParams.put("upload_id", uploadId);

        final Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("etag", eTag);

        MockNetwork
                .expecting(HttpVerb.PUT, "/bucketName/object", queryParams, requestContent)
                .returning(200, "", responseHeaders)
                .asClient()
                .putMultiPartUploadPart(new PutMultiPartUploadPartRequest(
                        bucketName,
                        objectName,
                        new ByteArraySeekableByteChannel(requestContent.getBytes()),
                        partNumber, requestContent.getBytes().length,
                        uploadId));
    }

    @Test
    public void getBulkJobSpectraS3WithMixedPayloadTest() throws IOException {
        final List<Ds3Object> objects = Arrays.asList(
                new Ds3Object("file1", 256),
                new PartialDs3Object("file2", new Range(1202, 1402)),
                new Ds3Object("file3", 2523)
        );

        final String bucketName = "bulkTest";

        final String expectedRequestPayload = "<Objects><Object Name=\"file1\"/><Object Name=\"file2\" Offset=\"1202\" Length=\"201\"/><Object Name=\"file3\"/></Objects>";

        final String xmlResponse = "<MasterObjectList BucketName=\"lib\" JobId=\"9652a41a-218a-4158-af1b-064ab9e4ef71\" Priority=\"NORMAL\" RequestType=\"PUT\" StartDate=\"2014-07-29T16:08:39.000Z\"><Nodes><Node EndPoint=\"FAILED_TO_DETERMINE_DATAPATH_IP_ADDRESS\" HttpPort=\"80\" HttpsPort=\"443\" Id=\"b18ee082-1352-11e4-945e-080027ebeb6d\"/></Nodes><Objects ChunkId=\"cfa3153f-57de-41c7-b1fb-f30fa4154232\" ChunkNumber=\"0\"><Object Name=\"file2\" InCache=\"false\" Length=\"1202\" Offset=\"0\"/><Object Name=\"file1\" InCache=\"false\" Length=\"256\" Offset=\"0\"/><Object Name=\"file3\" InCache=\"false\" Length=\"2523\" Offset=\"0\"/></Objects></MasterObjectList>";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "start_bulk_get");

        final GetBulkJobSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.PUT, "/_rest_/bucket/" + bucketName, queryParams, expectedRequestPayload)
                .returning(200, xmlResponse)
                .asClient()
                .getBulkJobSpectraS3(new GetBulkJobSpectraS3Request(bucketName, objects));

        final List<Objects> objectListList = response.getMasterObjectList().getObjects();
        assertThat(objectListList.size(), is(1));

        final List<BulkObject> objectList = objectListList.get(0).getObjects();
        assertThat(objectList.size(), is(3));

        assertObjectEquals(objectList.get(0), "file2", 1202);
        assertObjectEquals(objectList.get(1), "file1", 256);
        assertObjectEquals(objectList.get(2), "file3", 2523);
    }


    @Test
    public void verifyBulkJobSpectraS3Test() throws IOException {
        final List<Ds3Object> objects = Arrays.asList(
                new Ds3Object("file1"),
                new PartialDs3Object("file2", new Range(1202, 1402)),
                new Ds3Object("file3")
        );

        final String bucketName = "bulkTest";

        final String expectedRequestPayload = "<Objects><Object Name=\"file1\"/><Object Name=\"file2\" Offset=\"1202\" Length=\"201\"/><Object Name=\"file3\"/></Objects>";

        final String xmlResponse = "<MasterObjectList BucketName=\"lib\" JobId=\"9652a41a-218a-4158-af1b-064ab9e4ef71\" Priority=\"NORMAL\" RequestType=\"PUT\" StartDate=\"2014-07-29T16:08:39.000Z\"><Nodes><Node EndPoint=\"FAILED_TO_DETERMINE_DATAPATH_IP_ADDRESS\" HttpPort=\"80\" HttpsPort=\"443\" Id=\"b18ee082-1352-11e4-945e-080027ebeb6d\"/></Nodes><Objects ChunkId=\"cfa3153f-57de-41c7-b1fb-f30fa4154232\" ChunkNumber=\"0\"><Object Name=\"file2\" InCache=\"false\" Length=\"1202\" Offset=\"0\"/><Object Name=\"file1\" InCache=\"false\" Length=\"256\" Offset=\"0\"/><Object Name=\"file3\" InCache=\"false\" Length=\"2523\" Offset=\"0\"/></Objects></MasterObjectList>";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "start_bulk_verify");

        final VerifyBulkJobSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.PUT, "/_rest_/bucket/" + bucketName, queryParams, expectedRequestPayload)
                .returning(200, xmlResponse)
                .asClient()
                .verifyBulkJobSpectraS3(new VerifyBulkJobSpectraS3Request(bucketName, objects));

        final List<Objects> objectListList = response.getMasterObjectListResult().getObjects();
        assertThat(objectListList.size(), is(1));

        final List<BulkObject> objectList = objectListList.get(0).getObjects();
        assertThat(objectList.size(), is(3));

        assertObjectEquals(objectList.get(0), "file2", 1202);
        assertObjectEquals(objectList.get(1), "file1", 256);
        assertObjectEquals(objectList.get(2), "file3", 2523);
    }

    @Test
    public void getPhysicalPlacementForObjectsTest() throws IOException {
        final String responsePayload = "<Data><AzureTargets/><Ds3Targets/><Pools/><S3Targets/><Tapes/></Data>";
        final String bucketName = "BucketName";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "get_physical_placement");

        final GetPhysicalPlacementForObjectsSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.PUT, "/_rest_/bucket/" + bucketName, queryParams, SIMPLE_OBJECT_REQUEST_PAYLOAD)
                .returning(200, responsePayload)
                .asClient()
                .getPhysicalPlacementForObjectsSpectraS3(new GetPhysicalPlacementForObjectsSpectraS3Request(bucketName, SIMPLE_OBJECT_LIST));

        assertThat(response.getPhysicalPlacementResult().getAzureTargets(), is(nullValue()));
        assertThat(response.getPhysicalPlacementResult().getDs3Targets(), is(nullValue()));
        assertThat(response.getPhysicalPlacementResult().getPools(), is(nullValue()));
        assertThat(response.getPhysicalPlacementResult().getS3Targets(), is(nullValue()));
        assertThat(response.getPhysicalPlacementResult().getTapes(), is(nullValue()));
    }

    @Test
    public void getPhysicalPlacementForObjectsWithFullDetailsTest() throws IOException {
        final String responsePayload = "<Data><Object Bucket=\"b1\" Id=\"a2897bbd-3e0b-4c0f-83d7-29e1e7669bdd\" InCache=\"false\" Latest=\"true\" Length=\"10\" Name=\"o4\" Offset=\"0\" Version=\"1\"><PhysicalPlacement><AzureTargets/><Ds3Targets/><Pools/><S3Targets/><Tapes/></PhysicalPlacement></Object></Data>";
        final String bucketName = "BucketName";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "get_physical_placement");
        queryParams.put("full_details", null);

        final GetPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.PUT, "/_rest_/bucket/" + bucketName, queryParams, SIMPLE_OBJECT_REQUEST_PAYLOAD)
                .returning(200, responsePayload)
                .asClient()
                .getPhysicalPlacementForObjectsWithFullDetailsSpectraS3(new GetPhysicalPlacementForObjectsWithFullDetailsSpectraS3Request(bucketName, SIMPLE_OBJECT_LIST));

        assertThat(response.getBulkObjectListResult().getObjects().size(), is(1));
    }

    @Test
    public void verifyPhysicalPlacementForObjectsTest() throws IOException {
        final String responsePayload = "<Data><AzureTargets/><Ds3Targets/><Pools/><S3Targets/><Tapes><Tape><AssignedToStorageDomain>false</AssignedToStorageDomain><AvailableRawCapacity>10000</AvailableRawCapacity><BarCode>t1</BarCode><BucketId/><DescriptionForIdentification/><EjectDate/><EjectLabel/><EjectLocation/><EjectPending/><FullOfData>false</FullOfData><Id>48d30ecb-84f1-4721-9832-7aa165a1dd77</Id><LastAccessed/><LastCheckpoint/><LastModified/><LastVerified/><PartiallyVerifiedEndOfTape/><PartitionId>76343269-c32a-4cb0-aec4-57a9dccce6ea</PartitionId><PreviousState/><SerialNumber/><State>PENDING_INSPECTION</State><StorageDomainId/><TakeOwnershipPending>false</TakeOwnershipPending><TotalRawCapacity>20000</TotalRawCapacity><Type>LTO5</Type><VerifyPending/><WriteProtected>false</WriteProtected></Tape></Tapes></Data>";
        final String bucketName = "BucketName";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "verify_physical_placement");

        final VerifyPhysicalPlacementForObjectsSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/bucket/" + bucketName, queryParams, SIMPLE_OBJECT_REQUEST_PAYLOAD)
                .returning(200, responsePayload)
                .asClient()
                .verifyPhysicalPlacementForObjectsSpectraS3(new VerifyPhysicalPlacementForObjectsSpectraS3Request(bucketName, SIMPLE_OBJECT_LIST));

        assertThat(response.getPhysicalPlacementResult().getAzureTargets(), is(nullValue()));
        assertThat(response.getPhysicalPlacementResult().getDs3Targets(), is(nullValue()));
        assertThat(response.getPhysicalPlacementResult().getPools(), is(nullValue()));
        assertThat(response.getPhysicalPlacementResult().getTapes().size(), is(1));
    }

    @Test
    public void verifyPhysicalPlacementForObjectsWithFullDetailsTest() throws IOException {
        final String responsePayload = "<Data><Object Bucket=\"b1\" Id=\"15ad85a5-aab6-4d85-bf33-831bcba13b8e\" InCache=\"false\" Latest=\"true\" Length=\"10\" Name=\"o1\" Offset=\"0\" Version=\"1\"><PhysicalPlacement><AzureTargets/><Ds3Targets/><Pools/><S3Targets/><Tapes><Tape><AssignedToStorageDomain>false</AssignedToStorageDomain><AvailableRawCapacity>10000</AvailableRawCapacity><BarCode>t1</BarCode><BucketId/><DescriptionForIdentification/><EjectDate/><EjectLabel/><EjectLocation/><EjectPending/><FullOfData>false</FullOfData><Id>5a7bb215-4aff-4806-b217-5fe01ade6a2c</Id><LastAccessed/><LastCheckpoint/><LastModified/><LastVerified/><PartiallyVerifiedEndOfTape/><PartitionId>2e5b25fc-546e-45b0-951e-8f3d80bb7823</PartitionId><PreviousState/><SerialNumber/><State>PENDING_INSPECTION</State><StorageDomainId/><TakeOwnershipPending>false</TakeOwnershipPending><TotalRawCapacity>20000</TotalRawCapacity><Type>LTO5</Type><VerifyPending/><WriteProtected>false</WriteProtected></Tape></Tapes></PhysicalPlacement></Object></Data>";
        final String bucketName = "BucketName";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "verify_physical_placement");
        queryParams.put("full_details", null);

        final VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/bucket/" + bucketName, queryParams, SIMPLE_OBJECT_REQUEST_PAYLOAD)
                .returning(200, responsePayload)
                .asClient()
                .verifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3(new VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Request(bucketName, SIMPLE_OBJECT_LIST));

        assertThat(response.getBulkObjectListResult().getObjects().size(), is(1));
    }

    @Test
    public void ejectStorageDomainBlobsTest() throws IOException {
        final String bucketId = "BucketId";
        final String storageDomainId = "StorageDomainId";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "eject");
        queryParams.put("blobs", null);
        queryParams.put("bucket_id", bucketId);
        queryParams.put("storage_domain_id", storageDomainId);

        MockNetwork
                .expecting(HttpVerb.PUT, "/_rest_/tape", queryParams, SIMPLE_OBJECT_REQUEST_PAYLOAD)
                .returning(204, "")
                .asClient()
                .ejectStorageDomainBlobsSpectraS3(new EjectStorageDomainBlobsSpectraS3Request(bucketId, SIMPLE_OBJECT_LIST, storageDomainId));
    }

    @Test
    public void replicatePutJobTest() throws IOException {
        final String responsePayload = "<MasterObjectList Aggregating=\"false\" BucketName=\"existing_bucket\" CachedSizeInBytes=\"0\" ChunkClientProcessingOrderGuarantee=\"IN_ORDER\" CompletedSizeInBytes=\"0\" EntirelyInCache=\"false\" JobId=\"95dcda9b-26d2-4b95-87e2-36ac217d7230\" Naked=\"false\" Name=\"Replicate Untitled\" OriginalSizeInBytes=\"10\" Priority=\"NORMAL\" RequestType=\"PUT\" StartDate=\"2017-03-23T23:24:24.000Z\" Status=\"IN_PROGRESS\" UserId=\"1dc9953a-c778-4cdd-b217-2a6b325cde5e\" UserName=\"test_user\"><Nodes><Node EndPoint=\"NOT_INITIALIZED_YET\" Id=\"782ee70f-692e-4240-8ee1-c049b3a7b91e\"/></Nodes><Objects ChunkId=\"33a7ed12-d7b7-4f85-ac67-b3a2834170cc\" ChunkNumber=\"1\"><Object Id=\"eee15242-d7c1-44dc-b352-811adc6e5c0e\" InCache=\"false\" Latest=\"true\" Length=\"10\" Name=\"o1\" Offset=\"0\" Version=\"1\"/></Objects></MasterObjectList>";
        final String requestPayload = "This is the request payload content";
        final String bucketName = "BucketName";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "start_bulk_put");
        queryParams.put("replicate", null);

        final ReplicatePutJobSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.PUT, "/_rest_/bucket/" + bucketName, queryParams, requestPayload)
                .returning(200, responsePayload)
                .asClient()
                .replicatePutJobSpectraS3(new ReplicatePutJobSpectraS3Request(bucketName, requestPayload));

        assertThat(response.getMasterObjectListResult().getObjects().size(), is(1));
    }

    @Test
    public void getBlobPersistenceTest() throws IOException {
        final String responsePayload = "This is the response payload content";
        final String requestPayload = "This is the request payload content";

        final Map<String, String> queryParams = new HashMap<>();

        final GetBlobPersistenceSpectraS3Response response = MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/blob_persistence", queryParams, requestPayload)
                .returning(200, responsePayload)
                .asClient()
                .getBlobPersistenceSpectraS3(new GetBlobPersistenceSpectraS3Request(requestPayload));

        assertThat(response.getStringResult(), is(responsePayload));
    }

    @Test
    public void getBlobsOnAzureTargetTest() throws IOException {
        final String target = "Target";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "get_physical_placement");

        MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/azure_target/" + target, queryParams, null)
                .returning(200, SIMPLE_BULK_OBJECT_LIST_RESPONSE)
                .asClient()
                .getBlobsOnAzureTargetSpectraS3(new GetBlobsOnAzureTargetSpectraS3Request(target));
    }

    @Test
    public void getBlobsOnTapeTest() throws IOException {
        final String target = "Target";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "get_physical_placement");

        MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/tape/" + target, queryParams, null)
                .returning(200, SIMPLE_BULK_OBJECT_LIST_RESPONSE)
                .asClient()
                .getBlobsOnTapeSpectraS3(new GetBlobsOnTapeSpectraS3Request(target));
    }

    @Test
    public void getBlobsOnS3TargetTest() throws IOException {
        final String target = "Target";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "get_physical_placement");

        MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/s3_target/" + target, queryParams, null)
                .returning(200, SIMPLE_BULK_OBJECT_LIST_RESPONSE)
                .asClient()
                .getBlobsOnS3TargetSpectraS3(new GetBlobsOnS3TargetSpectraS3Request(target));
    }

    @Test
    public void getBlobsOnPoolTest() throws IOException {
        final String target = "Target";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "get_physical_placement");

        MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/pool/" + target, queryParams, null)
                .returning(200, SIMPLE_BULK_OBJECT_LIST_RESPONSE)
                .asClient()
                .getBlobsOnPoolSpectraS3(new GetBlobsOnPoolSpectraS3Request(target));
    }

    @Test
    public void getBlobsOnDs3TargetTest() throws IOException {
        final String target = "Target";

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "get_physical_placement");

        MockNetwork
                .expecting(HttpVerb.GET, "/_rest_/ds3_target/" + target, queryParams, null)
                .returning(200, SIMPLE_BULK_OBJECT_LIST_RESPONSE)
                .asClient()
                .getBlobsOnDs3TargetSpectraS3(new GetBlobsOnDs3TargetSpectraS3Request(target));
    }
}
