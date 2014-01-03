package com.spectralogic.ds3client;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.serializer.XmlOutput;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import java.io.*;
import java.net.URL;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ds3Client {

    final static private String HOST = "HOST";
    final static private String DATE = "DATE";
    final static private String AUTHORIZATION = "Authorization";
    final static private String CONTENTMD5 = "Content-MD5";
    final static private String CONTENT_TYPE = "Content-Type";
    final static private String STREAM_TYPE = "application/octet-stream";
    final static private String GET = "GET";
    final static private String PUT = "PUT";
    final static private String UTF8 = "UTF-8";

    final private ConnectionDetails connectionDetails;

    protected Ds3Client(ConnectionDetails connectionDetails) {
        this.connectionDetails = connectionDetails;
    }

    public ConnectionDetails getConnectionDetails() {
        return connectionDetails;
    }

    public ListAllMyBucketsResult getService() throws IOException, SignatureException, FailedRequestException {
        final List<Ds3Bucket> buckets = new ArrayList<Ds3Bucket>();
        final CloseableHttpResponse response = sendGetRequest("/");

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

        final CloseableHttpResponse response = sendPutRequest("/","",null,headers,0);

        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);

            System.out.println(writer.toString());
            System.out.println("Response code: " + response.getStatusLine().getStatusCode());
        }
        finally {
            response.close();
        }
        return bucket;
    }

    public ListBucketResult listBucket(final String bucketName) throws IOException, SignatureException {

        final CloseableHttpResponse response = sendGetRequest("/" + bucketName);

        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);

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
        final String xmlOutput = toXml(objects, command);

        final CloseableHttpResponse response = sendBulkCommand(bucketName, xmlOutput, command);
        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, UTF8);
            System.out.println(writer.toString());
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

    private String toXml(final Objects objects, final BulkCommand command) throws XmlProcessingException {
        if (command == BulkCommand.GET) {
            final FilterProvider filters = new SimpleFilterProvider().addFilter("sizeFilter",
                    SimpleBeanPropertyFilter.serializeAllExcept("size"));
            return XmlOutput.toXml(objects, filters);
        }
        return XmlOutput.toXml(objects);
    }

    public void putObject(final String bucketName, final String objectName, final long fileSize, final InputStream inStream) throws IOException, SignatureException {
        final String objectPath = NetUtils.buildPath(bucketName, objectName);

        final CloseableHttpResponse response = sendPutRequest(objectPath,"",inStream, null, fileSize);
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
        final CloseableHttpResponse response = sendPutRequest(objectPath, file);
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

    public InputStream getObject(String testBucket, String object) throws IOException, SignatureException {
        final String objectPath = NetUtils.buildPath(testBucket,object);
        final CloseableHttpResponse response = sendGetRequest(objectPath);
        return response.getEntity().getContent();
    }

    private CloseableHttpResponse sendPutRequest(final String path, final File fileName) throws SignatureException, IOException {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final HttpPut putRequest = new HttpPut(NetUtils.buildUrl(path, connectionDetails).toString());

        final String date = NetUtils.dateToRfc882();
        putRequest.addHeader(PUT, connectionDetails.getEndpoint());
        putRequest.addHeader(DATE, date);
        putRequest.addHeader(CONTENT_TYPE, STREAM_TYPE);
        putRequest.addHeader(AUTHORIZATION, "AWS " + connectionDetails.getCredentials().getClientId() +":"
                + NetUtils.signature(new SignatureDetails(PUT, "", STREAM_TYPE, date, "", path,connectionDetails.getCredentials())));
        final HttpEntity entity = EntityBuilder.create().setFile(fileName).build();
        putRequest.setEntity(entity);

        return httpClient.execute(putRequest);
    }

    //TODO: this may get moved into it's own class so that it can be using threaded.
    /**
     * The caller must close the HttpResponse object.
     * @param path
     * @return
     */
    private CloseableHttpResponse sendGetRequest(final String path) throws IOException, SignatureException {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final HttpGet getRequest = new HttpGet(NetUtils.buildUrl(path, connectionDetails).toString());

        final String date = NetUtils.dateToRfc882();
        getRequest.addHeader(HOST, connectionDetails.getEndpoint());
        getRequest.addHeader(DATE, date);
        getRequest.addHeader(AUTHORIZATION, "AWS " + connectionDetails.getCredentials().getClientId() +":"
                + NetUtils.signature(new SignatureDetails(GET, "", "", date, "", path,connectionDetails.getCredentials())));

        return httpClient.execute(getRequest);
    }

    private CloseableHttpResponse sendPutRequest(final String path, final String mdf5, final InputStream dataStream, final List<Header> headers, final long fileSize) throws IOException, SignatureException {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final HttpPut putRequest = new HttpPut(NetUtils.buildUrl(path, connectionDetails).toString());

        if (dataStream != null) {
            putRequest.setEntity(new InputStreamEntity(dataStream, fileSize));
        }

        if(headers != null) {
            for (final Header header:headers) {
                putRequest.addHeader(header);
            }
        }

        final String date = NetUtils.dateToRfc882();
        putRequest.addHeader(HOST, connectionDetails.getEndpoint());
        putRequest.addHeader(DATE,date);
        if(!mdf5.isEmpty()) {
            putRequest.addHeader(CONTENTMD5,mdf5);
        }
        putRequest.addHeader(AUTHORIZATION,"AWS " + connectionDetails.getCredentials().getClientId() +":"
                + NetUtils.signature(new SignatureDetails(PUT, mdf5, "", date, "", path,connectionDetails.getCredentials())));

        return httpClient.execute(putRequest);
    }

    public CloseableHttpResponse sendBulkCommand(final String bucketName, final String xmlBody, final BulkCommand command)
            throws IOException, SignatureException {
        System.out.println(xmlBody);
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final Map<String, String> queryParams = new HashMap<String,String>();
        queryParams.put(command.toString(), null);
        final URL url = NetUtils.buildUrl(ensureBucketHasSlash(bucketName), connectionDetails, queryParams);
        final HttpPut putRequest = new HttpPut(url.toString());
        final String date = NetUtils.dateToRfc882();

        putRequest.addHeader(PUT, connectionDetails.getEndpoint());
        putRequest.addHeader(DATE, date);
        putRequest.addHeader(CONTENT_TYPE, ContentType.APPLICATION_XML.toString());
        putRequest.addHeader(AUTHORIZATION, "AWS " + connectionDetails.getCredentials().getClientId() +":"
                + NetUtils.signature(new SignatureDetails(PUT, "", ContentType.APPLICATION_XML.toString(), date, "", url.getPath() + "?" + url.getQuery(),
                connectionDetails.getCredentials())));
        final HttpEntity entity = EntityBuilder.create().
                setText(xmlBody).
                setContentType(ContentType.APPLICATION_XML)
                .build();
        putRequest.setEntity(entity);

        return httpClient.execute(putRequest);
    }

    private enum BulkCommand {
        PUT, GET;

        public String toString() {
            if (this == PUT) {
                return "start-bulk-put";
            }
            else {
                return "start-bulk-get";
            }
        }
    }

    private String ensureBucketHasSlash(final String bucket) {
        if(!bucket.endsWith("/")) {
            return bucket + "/";
        }
        return bucket;
    }
}
