package com.spectralogic.ds3client;

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
import java.nio.charset.Charset;
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

    public List<Ds3Bucket> getService() throws IOException, SignatureException {
        final List<Ds3Bucket> buckets = new ArrayList<Ds3Bucket>();
        final CloseableHttpResponse response = sendGetRequest("/");

        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, Charset.forName(UTF8));

            System.out.println(writer.toString());

            for(Header header: response.getAllHeaders()) {
                System.out.println(header.getName() + ": " + header.getValue());
            }
        }
        finally {
            response.close();
        }

        return buckets;
    }

    public Ds3Bucket createBucket(final String bucketName) throws IOException, SignatureException {
        final Ds3Bucket bucket = new Ds3Bucket(bucketName);
        final List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("bucket-name",bucketName));

        final CloseableHttpResponse response = sendPutRequest("/","",null,headers,0);

        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(),writer,Charset.forName(UTF8));

            System.out.println(writer.toString());
        }
        finally {
            response.close();
        }
        return bucket;
    }

    public List<Ds3Object> listBucket(final String bucketName) throws IOException, SignatureException {
        final List<Ds3Object> objects = new ArrayList<Ds3Object>();
        final CloseableHttpResponse response = sendGetRequest("/" + bucketName);

        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, Charset.forName(UTF8));

            System.out.println(writer.toString());

            for(Header header: response.getAllHeaders()) {
                System.out.println(header.getName() + ": " + header.getValue());
            }
        }
        finally {
            response.close();
        }

        return objects;
    }

    public MasterObjectList bulkPut(final String bucketName, final List<Ds3Object> files)
            throws XmlProcessingException, IOException, SignatureException, FailedRequestException {
        final Objects objects = new Objects();
        objects.setObject(files);
        final String xmlOutput = XmlOutput.toXml(objects);
        final CloseableHttpResponse response = sendBulkPut(bucketName,xmlOutput);
        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, Charset.forName(UTF8));

            final StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != 200) {
                throw new FailedRequestException("Request failed with a non-200 status code.  Actual status code: " + statusLine.getStatusCode());
            }

            return XmlOutput.fromXml(writer.toString());
        }
        finally {
            response.close();
        }
    }

    public void putObject(final String bucketName, final String objectName, final long fileSize, final InputStream inStream) throws IOException, SignatureException {
        final String objectPath = NetUtils.buildPath(bucketName, objectName);

        final CloseableHttpResponse response = sendPutRequest(objectPath,"",inStream, null, fileSize);
        try {
            final StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer, Charset.forName(UTF8));

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
            IOUtils.copy(response.getEntity().getContent(), writer, Charset.forName(UTF8));

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

    public CloseableHttpResponse sendBulkPut(final String bucketName, final String xmlBody) throws IOException, SignatureException {
        System.out.println(xmlBody);
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        final Map<String, String> queryParams = new HashMap<String,String>();
        queryParams.put("start-bulk-put",null);

        final URL url = NetUtils.buildUrl(bucketName, connectionDetails, queryParams);
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
}
