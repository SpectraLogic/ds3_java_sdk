package com.spectralogic.ds3client;

import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.NetUtils;
import com.spectralogic.ds3client.networking.NetworkClient;
import com.spectralogic.ds3client.serializer.XmlOutput;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;

import java.io.*;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ds3Client {


    final static private String UTF8 = "UTF-8";

    private final NetworkClient netClient;

    protected Ds3Client(final NetworkClient netClient) {
        this.netClient = netClient;
    }

    protected NetworkClient getNetClient() {
        return netClient;
    }

    public ListAllMyBucketsResult getService() throws IOException, SignatureException, FailedRequestException {
        final CloseableHttpResponse response = netClient.sendGetRequest("/");

        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);

            final StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != 200) {
                throw new FailedRequestException("Request failed with a non-200 status code.  Actual status code: " + statusLine.getStatusCode());
            }

            return XmlOutput.fromXml(writer.toString(), ListAllMyBucketsResult.class);
        }
        finally {
            response.close();
        }

    }

    public Ds3Bucket createBucket(final String bucketName) throws IOException, SignatureException {
        final Ds3Bucket bucket = new Ds3Bucket(bucketName);
        final List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("bucket-name",bucketName));

        final CloseableHttpResponse response = netClient.sendPutRequest("/", "", null, headers, 0);

        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);

        }
        finally {
            response.close();
        }
        return bucket;
    }

    public ListBucketResult listBucket(final String bucketName) throws IOException, SignatureException {

        final CloseableHttpResponse response = netClient.sendGetRequest("/" + bucketName);

        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);
            System.out.println(writer.toString());
            return XmlOutput.fromXml(writer.toString(), ListBucketResult.class);
        }
        finally {
            response.close();
        }
    }

    public MasterObjectList bulkGet(final String bucketName, final List<Ds3Object> files)
            throws XmlProcessingException, IOException, SignatureException, FailedRequestException {

        return bulkCommands(bucketName, files, BulkCommand.GET);
    }

    public MasterObjectList bulkPut(final String bucketName, final List<Ds3Object> files)
            throws XmlProcessingException, IOException, SignatureException, FailedRequestException {
        return bulkCommands(bucketName, files, BulkCommand.PUT);
    }

    private MasterObjectList bulkCommands(final String bucketName, final List<Ds3Object> files, final BulkCommand command)
            throws XmlProcessingException, IOException, SignatureException, FailedRequestException {
        final Objects objects = new Objects();
        objects.setObject(files);
        final String xmlOutput = XmlOutput.toXml(objects, command);

        final CloseableHttpResponse response = netClient.sendBulkCommand(bucketName, xmlOutput, command);
        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);

            final StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != 200) {
                throw new FailedRequestException("Request failed with a non-200 status code.  Actual status code: " + statusLine.getStatusCode() + "\nMessage Body: " + writer.toString());
            }

            return XmlOutput.fromXml(writer.toString(), MasterObjectList.class);
        }
        finally {
            response.close();
        }
    }

    public InputStream getObject(final String bucketName, final String object) throws IOException, SignatureException {
        final String objectPath = NetUtils.buildPath(bucketName,object);
        final CloseableHttpResponse response = netClient.sendGetRequest(objectPath);
        return response.getEntity().getContent();
    }

    public void putObject(final String bucketName, final String objectName, final long fileSize, final InputStream inStream) throws IOException, SignatureException {
        final String objectPath = NetUtils.buildPath(bucketName, objectName);

        final CloseableHttpResponse response = netClient.sendPutRequest(objectPath, "", inStream, null, fileSize);
        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);

            System.out.println(writer.toString());
            System.out.println(response.getStatusLine().toString());
        }
        finally {
            response.close();
        }
    }

    public void putObject(final String bucketName, final String objectName, final File file) throws IOException, SignatureException {
        final String objectPath = NetUtils.buildPath(bucketName, objectName);
        final CloseableHttpResponse response = netClient.sendPutRequest(objectPath, file);
        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);

            System.out.println(writer.toString());
            System.out.println(response.getStatusLine().toString());
        }
        finally {
            response.close();
        }
    }

    public void listJobs(final String bucketName) throws IOException, SignatureException {
        final Map<String, String> queryParams = new HashMap<String,String>();
        queryParams.put("bucket", bucketName);
        final CloseableHttpResponse response = netClient.sendGetRequest("/_rest_/jobs", queryParams);

        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);

            System.out.println(writer.toString());
            //return XmlOutput.fromXml(writer.toString(), ListBucketResult.class);
        }
        finally {
            response.close();
        }
    }

    public void jobInfo(final String jobId) throws IOException, SignatureException {
        final CloseableHttpResponse response = netClient.sendGetRequest("/_rest_/jobs/" + jobId);

        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);

            System.out.println(writer.toString());
            //return XmlOutput.fromXml(writer.toString(), ListBucketResult.class);
        }
        finally {
            response.close();
        }
    }

}
