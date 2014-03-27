package com.spectralogic.ds3client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SignatureException;

import com.spectralogic.ds3client.commands.BulkGetRequest;
import com.spectralogic.ds3client.commands.BulkGetResponse;
import com.spectralogic.ds3client.commands.BulkPutRequest;
import com.spectralogic.ds3client.commands.BulkPutResponse;
import com.spectralogic.ds3client.commands.DeleteBucketRequest;
import com.spectralogic.ds3client.commands.DeleteBucketResponse;
import com.spectralogic.ds3client.commands.DeleteObjectRequest;
import com.spectralogic.ds3client.commands.DeleteObjectResponse;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.commands.GetBucketResponse;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.commands.GetServiceRequest;
import com.spectralogic.ds3client.commands.GetServiceResponse;
import com.spectralogic.ds3client.commands.PutBucketRequest;
import com.spectralogic.ds3client.commands.PutBucketResponse;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.commands.PutObjectResponse;
import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.networking.NetworkClient;

public class Ds3Client {

	public static class Builder implements com.spectralogic.ds3client.utils.Builder<Ds3Client> {

	    final private String endpoint;
	    final private Credentials credentials;

	    private boolean secure = true;
	    private URI proxy = null;
	    private int retries = 5;

	    private Builder(final String endpoint, final Credentials credentials) throws IllegalArgumentException {
	        if (endpoint == null || endpoint.isEmpty()) {
	            throw new IllegalArgumentException("Endpoint must be non empty");
	        }
	        if(credentials == null || !credentials.isValid()) {
	            throw new IllegalArgumentException("Credentials must be filled out.");
	        }
	        this.endpoint = endpoint;
	        this.credentials = credentials;
	    }

	    public Builder withHttpSecure(final boolean secure) {
	        this.secure = secure;
	        return this;
	    }

	    public Builder withProxy(final String proxy) throws IllegalArgumentException {
	        try {
	            final URI proxyUri;
	            if(!proxy.startsWith("http")) {
	                throw new IllegalArgumentException("Invalid proxy format.  The web address must start with either http or https.");
	            }
	            proxyUri = new URI(proxy);

	            this.proxy = proxyUri;
	        } catch (final URISyntaxException e) {
	            throw new IllegalArgumentException("Invalid proxy format.  Must be a web address.");
	        }

	        return this;
	    }

	    public Builder withRedirectRetries(final int retries) {
	        this.retries = retries;
	        return this;
	    }

	    public Ds3Client build() {
	        final ConnectionDetails.Builder connBuilder = ConnectionDetails.builder(endpoint, credentials)
	            .withProxy(proxy).withSecure(secure).withRedirectRetries(retries);

	        final NetworkClient netClient = new NetworkClient(connBuilder.build());
	        return new Ds3Client(netClient);
	    }
	}
	
	public static Builder builder(final String endpoint, final Credentials creds) {
		return new Builder(endpoint, creds);
		
	}
	
    private final NetworkClient netClient;

    protected Ds3Client(final NetworkClient netClient) {
        this.netClient = netClient;
    }

    protected NetworkClient getNetClient() {
        return netClient;
    }

    public GetServiceResponse getService(final GetServiceRequest request) throws IOException, SignatureException {
        return new GetServiceResponse(netClient.getResponse(request));
    }

    public GetBucketResponse getBucket(final GetBucketRequest request) throws IOException, SignatureException {
        return new GetBucketResponse(netClient.getResponse(request));
    }

    public PutBucketResponse putBucket(final PutBucketRequest request) throws IOException, SignatureException {
        return new PutBucketResponse(netClient.getResponse(request));
    }

    public DeleteBucketResponse deleteBucket(final DeleteBucketRequest request) throws IOException, SignatureException {
        return new DeleteBucketResponse(netClient.getResponse(request));
    }

    public DeleteObjectResponse deleteObject(final DeleteObjectRequest request) throws IOException, SignatureException {
        return new DeleteObjectResponse(netClient.getResponse(request));
    }

    public GetObjectResponse getObject(final GetObjectRequest request) throws IOException, SignatureException {
        return new GetObjectResponse(netClient.getResponse(request));
    }

    public PutObjectResponse putObject(final PutObjectRequest request) throws IOException, SignatureException {
        return new PutObjectResponse(netClient.getResponse(request));
    }

    public BulkGetResponse bulkGet(final BulkGetRequest request) throws IOException, SignatureException {
        return new BulkGetResponse(netClient.getResponse(request));
    }

    public BulkPutResponse bulkPut(final BulkPutRequest request) throws IOException, SignatureException {
        return new BulkPutResponse(netClient.getResponse(request));
    }
}

