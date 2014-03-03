package com.spectralogic.ds3client;

import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.networking.NetworkClient;

import java.io.*;
import java.security.SignatureException;

public class Ds3Client {

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
