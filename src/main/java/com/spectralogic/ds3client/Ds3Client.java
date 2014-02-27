package com.spectralogic.ds3client;

import com.google.common.collect.Lists;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.networking.NetUtils;
import com.spectralogic.ds3client.networking.NetworkClient;
import com.spectralogic.ds3client.serializer.*;
import org.apache.commons.io.IOUtils;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.*;
import java.security.SignatureException;
import java.util.*;

public class Ds3Client {
    final static private String UTF8 = "UTF-8";

    private final NetworkClient netClient;

    protected Ds3Client(final NetworkClient netClient) {
        this.netClient = netClient;
    }

    protected NetworkClient getNetClient() {
        return netClient;
    }

    public ListAllMyBucketsResult getService() throws IOException, SignatureException {
        try(final CloseableHttpResponse response = netClient.get("/")) {
            final StringWriter writer = new StringWriter();

            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);
            checkStatusCode(response, 200);

            return XmlOutput.fromXml(writer.toString(), ListAllMyBucketsResult.class);
        }
    }

    public Ds3Bucket createBucket(final String bucketName) throws IOException, SignatureException {
        final Ds3Bucket bucket = new Ds3Bucket(bucketName);

        try(final CloseableHttpResponse response = netClient.put("/" + bucketName, "", null, null, 0)) {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);
            checkStatusCode(response, 200);
        }
        return bucket;
    }

    public ListBucketResult listBucket(final String bucketName) throws IOException, SignatureException {
        try(final CloseableHttpResponse response = netClient.get("/" + bucketName)) {
            final StringWriter writer = new StringWriter();

            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);
            checkStatusCode(response, 200);

            return XmlOutput.fromXml(writer.toString(), ListBucketResult.class);
        }
    }

    public MasterObjectList bulkGet(final String bucketName, final Iterator<Ds3Object> files)
            throws XmlProcessingException, IOException, SignatureException {

        return bulkCommands(bucketName, files, BulkCommand.GET);
    }

    public MasterObjectList bulkPut(final String bucketName, final Iterator<Ds3Object> files)
            throws XmlProcessingException, IOException, SignatureException {
        return bulkCommands(bucketName, files, BulkCommand.PUT);
    }

    private MasterObjectList bulkCommands(final String bucketName, final Iterator<Ds3Object> files, final BulkCommand command)
            throws XmlProcessingException, IOException, SignatureException {
        final com.spectralogic.ds3client.models.Objects objects = new com.spectralogic.ds3client.models.Objects();
        objects.setObject(Lists.newArrayList(files));

        final String xmlOutput = XmlOutput.toXml(objects, command);

        try(final CloseableHttpResponse response = netClient.bulk(bucketName, xmlOutput, command)) {
            final StringWriter writer = new StringWriter();

            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);
            checkStatusCode(response, 200);

            return XmlOutput.fromXml(writer.toString(), MasterObjectList.class);
        }
    }

    /**
     * The caller must close the InputStream that is returned.
     * @param bucketName
     * @param object
     * @return
     * @throws IOException
     * @throws SignatureException
     * @throws FailedRequestException
     */
    public InputStream getObject(final String bucketName, final String object) throws IOException, SignatureException {
        final String objectPath = NetUtils.buildPath(bucketName,object);
        final CloseableHttpResponse response = netClient.get(objectPath);

        checkStatusCode(response, 200);
        return response.getEntity().getContent();
    }

    public void putObject(final String bucketName, final String objectName, final long fileSize, final InputStream inStream) throws IOException, SignatureException {
        final String objectPath = NetUtils.buildPath(bucketName, objectName);

        try(final CloseableHttpResponse response = netClient.put(objectPath, "", inStream, null, fileSize)) {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);

            checkStatusCode(response, 200);
        }
    }

    private void checkStatusCode(final CloseableHttpResponse response, int expectedStatus) throws FailedRequestException {
        final StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != expectedStatus) {
            throw new FailedRequestException("Request failed with a non-200 status code.  Actual status code: " + statusLine.getStatusCode());
        }
    }
}
