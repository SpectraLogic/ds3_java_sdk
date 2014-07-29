/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

import java.io.IOException;
import java.security.SignatureException;

import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.networking.NetworkClient;

class Ds3ClientImpl implements Ds3Client {
    private final NetworkClient netClient;

    Ds3ClientImpl(final NetworkClient netClient) {
        this.netClient = netClient;
    }

    NetworkClient getNetClient() {
        return this.netClient;
    }

    @Override
    public GetServiceResponse getService(final GetServiceRequest request) throws IOException, SignatureException {
        return new GetServiceResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetBucketResponse getBucket(final GetBucketRequest request) throws IOException, SignatureException {
        return new GetBucketResponse(this.netClient.getResponse(request));
    }

    @Override
    public PutBucketResponse putBucket(final PutBucketRequest request) throws IOException, SignatureException {
        return new PutBucketResponse(this.netClient.getResponse(request));
    }

    @Override
    public HeadBucketResponse headBucket(final HeadBucketRequest request) throws IOException, SignatureException {
        return new HeadBucketResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteBucketResponse deleteBucket(final DeleteBucketRequest request) throws IOException, SignatureException {
        return new DeleteBucketResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteObjectResponse deleteObject(final DeleteObjectRequest request) throws IOException, SignatureException {
        return new DeleteObjectResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetObjectResponse getObject(final GetObjectRequest request) throws IOException, SignatureException {
        return new GetObjectResponse(this.netClient.getResponse(request));
    }

    @Override
    public PutObjectResponse putObject(final PutObjectRequest request) throws IOException, SignatureException {
        return new PutObjectResponse(this.netClient.getResponse(request));
    }

    @Override
    public BulkGetResponse bulkGet(final BulkGetRequest request) throws IOException, SignatureException {
        return new BulkGetResponse(this.netClient.getResponse(request));
    }

    @Override
    public BulkPutResponse bulkPut(final BulkPutRequest request) throws IOException, SignatureException {
        return new BulkPutResponse(this.netClient.getResponse(request));
    }

}

