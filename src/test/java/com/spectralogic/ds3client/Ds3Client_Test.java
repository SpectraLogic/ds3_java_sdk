package com.spectralogic.ds3client;

import com.spectralogic.ds3client.models.Ds3Object;
import com.spectralogic.ds3client.models.ListAllMyBucketsResult;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.NetworkClient;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

public class Ds3Client_Test {

    public static final class MockedResponse extends MockUp<CloseableHttpResponse> {

        final String payload;
        final int statusCode;

        public MockedResponse(final String payload, final int statusCode) {
            this.payload = payload;
            this.statusCode = statusCode;
        }

        @Mock(invocations = 1)
        public HttpEntity getEntity() {
            return new MockUp<HttpEntity>() {
                @Mock(invocations = 1)
                public InputStream getContent() throws IOException, IllegalStateException {
                   return new ByteArrayInputStream(payload.getBytes());
                }
            }.getMockInstance();
        }

        @Mock(invocations = 1)
        public void close() {
            assertThat(true, is(true));
        }

        @Mock(invocations = 1)
        public StatusLine getStatusLine() {
            return new MockUp<StatusLine>() {
                @Mock(maxInvocations = 2, minInvocations = 1)
                public int getStatusCode() {
                    return statusCode;
                }
            }.getMockInstance();
        }
    }


    @Mocked
    private NetworkClient netClient;

    private Ds3Client client;

    @Before
    public void setup() {
        client = new Ds3Client(netClient);
    }

    @Test
    public void getService() throws IOException, SignatureException, FailedRequestException {
        final String stringResponse = "<ListAllMyBucketsResult xmlns=\"http://doc.s3.amazonaws.com/2006-03-01\">\n" +
                "<Owner><ID>ryan</ID><DisplayName>ryan</DisplayName></Owner><Buckets><Bucket><Name>testBucket2</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest1</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest2</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest3</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest4</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest5</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest6</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>testBucket3</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>testBucket1</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>testbucket</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket></Buckets></ListAllMyBucketsResult>";
        new Expectations() {{
            netClient.get("/");
            result = new MockedResponse(stringResponse, 200).getMockInstance();
        }};

        final ListAllMyBucketsResult result = client.getService();
        assertThat(result.getOwner().getDisplayName(), is("ryan"));
    }

    @Test(expected = FailedRequestException.class)
    public void getBadService() throws IOException, SignatureException, FailedRequestException {
        final String stringResponse = "Failed";
        new Expectations() {{
            netClient.get("/");
            result = new MockedResponse(stringResponse, 400).getMockInstance();
        }};

        client.getService();
    }

    @Test
    public void getBucketList() throws IOException, SignatureException, FailedRequestException {
        final String xmlResponse = "<ListBucketResult xmlns=\"http://s3.amazonaws.com/doc/2006-03-01/\"><Name>remoteTest16</Name><Prefix/><Marker/><MaxKeys>1000</MaxKeys><IsTruncated>false</IsTruncated><Contents><Key>user/hduser/gutenberg/20417.txt.utf-8</Key><LastModified>2014-01-03T13:26:47.000Z</LastModified><ETag>NOTRETURNED</ETag><Size>674570</Size><StorageClass>STANDARD</StorageClass><Owner><ID>ryan</ID><DisplayName>ryan</DisplayName></Owner></Contents><Contents><Key>user/hduser/gutenberg/5000.txt.utf-8</Key><LastModified>2014-01-03T13:26:47.000Z</LastModified><ETag>NOTRETURNED</ETag><Size>1423803</Size><StorageClass>STANDARD</StorageClass><Owner><ID>ryan</ID><DisplayName>ryan</DisplayName></Owner></Contents><Contents><Key>user/hduser/gutenberg/4300.txt.utf-8</Key><LastModified>2014-01-03T13:26:47.000Z</LastModified><ETag>NOTRETURNED</ETag><Size>1573150</Size><StorageClass>STANDARD</StorageClass><Owner><ID>ryan</ID><DisplayName>ryan</DisplayName></Owner></Contents></ListBucketResult>";
        new Expectations() {{
            netClient.get("/remoteTest16");
            result = new MockedResponse(xmlResponse, 200).getMockInstance();
        }};

        final ListBucketResult result = client.listBucket("remoteTest16");
        assertThat(result.getName(), is("remoteTest16"));
    }

    @Test(expected = FailedRequestException.class)
    public void getBadBucket() throws IOException, SignatureException, FailedRequestException {
        final String stringResponse = "Failed";
        new Expectations() {{
            netClient.get("/remoteTest16");
            result = new MockedResponse(stringResponse, 400).getMockInstance();
        }};

        client.listBucket("remoteTest16");
    }

    @Test
    public void getObject() throws IOException, SignatureException, FailedRequestException {
        final String stringResponse = "Response";
        new Expectations() {{
            netClient.get("/bucketName/object");
            result = new MockedResponse(stringResponse, 200).getMockInstance();
        }};

        final InputStream stream = client.getObject("bucketName", "object");
        final StringWriter writer = new StringWriter();
        IOUtils.copy(stream, writer, "UTF-8");

        assertThat(writer.toString(), is("Response"));
    }

    @Test
    public void bulkPut() throws IOException, SignatureException, FailedRequestException, XmlProcessingException {
        final List<Ds3Object> objects = new ArrayList<Ds3Object>();
        objects.add(new Ds3Object("file1",256));
        objects.add(new Ds3Object("file2",1202));
        objects.add(new Ds3Object("file3",2523));

        final String expectedXmlBody = "<objects><object name=\"file1\" size=\"256\"/><object name=\"file2\" size=\"1202\"/><object name=\"file3\" size=\"2523\"/></objects>";
        final String xmlResponse = "<masterobjectlist><objects><object name='file1' size='256'/><object name='file2' size='1202'/><object name='file3' size='2523'/></objects></masterobjectlist>";

        new Expectations() {{
            netClient.bulk("bulkTest",expectedXmlBody, BulkCommand.PUT);
            result = new MockedResponse(xmlResponse, 200).getMockInstance();
        }};

        final MasterObjectList masterObjectList = client.bulkPut("bulkTest", objects.iterator());
        assertThat(masterObjectList, is(notNullValue()));
        assertThat(masterObjectList.getObjects().size(), is(1));
        assertThat(masterObjectList.getObjects().get(0).getObject().size(), is(3));
    }
}
