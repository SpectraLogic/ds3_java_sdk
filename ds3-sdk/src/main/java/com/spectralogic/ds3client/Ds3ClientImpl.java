/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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
import com.spectralogic.ds3client.commands.notifications.*;
import com.spectralogic.ds3client.models.bulk.Node;
import com.spectralogic.ds3client.networking.ConnectionDetails;
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
    public ConnectionDetails getConnectionDetails() {
        return this.netClient.getConnectionDetails();
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
    public DeleteMultipleObjectsResponse deleteMultipleObjects(final DeleteMultipleObjectsRequest request) throws IOException, SignatureException {
        return new DeleteMultipleObjectsResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetObjectResponse getObject(final GetObjectRequest request) throws IOException, SignatureException {
        return new GetObjectResponse(
            this.netClient.getResponse(request),
            request.getDestinationChannel(),
            this.netClient.getConnectionDetails().getBufferSize()
        );
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

    @Override
    public AllocateJobChunkResponse allocateJobChunk(final AllocateJobChunkRequest request) throws IOException, SignatureException {
        return new AllocateJobChunkResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetAvailableJobChunksResponse getAvailableJobChunks(final GetAvailableJobChunksRequest request) throws IOException, SignatureException {
        return new GetAvailableJobChunksResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetJobsResponse getJobs(final GetJobsRequest request) throws IOException, SignatureException {
        return new GetJobsResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetJobResponse getJob(final GetJobRequest request) throws IOException, SignatureException {
        return new GetJobResponse(this.netClient.getResponse(request));
    }

    @Override
    public CancelJobResponse cancelJob(final CancelJobRequest request) throws IOException, SignatureException {
        return new CancelJobResponse(this.netClient.getResponse(request));
    }

    @Override
    public ModifyJobResponse modifyJob(final ModifyJobRequest request) throws IOException, SignatureException {
        return new ModifyJobResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse createObjectCachedNotification(final CreateObjectCachedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse getObjectCachedNotification(GetObjectCachedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteNotificationResponse deleteObjectCachedNotification(final DeleteObjectCachedNotificationRequest request) throws IOException, SignatureException {
        return new DeleteNotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse createJobCompletedNotification(final CreateJobCompletedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse getJobCompletedNotification(GetJobCompletedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteNotificationResponse deleteJobCompleteNotification(final DeleteJobCompletedNotificationRequest request) throws IOException, SignatureException {
        return new DeleteNotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse createJobCreatedNotification(CreateJobCreatedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse getJobCreatedNotification(GetJobCreatedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteNotificationResponse deleteJobCreatedNotification(DeleteJobCreatedNotificationRequest request) throws IOException, SignatureException {
        return new DeleteNotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse createObjectLostNotification(CreateObjectLostNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse getObjectLostNotification(GetObjectLostNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteNotificationResponse deleteObjectLostNotification(DeleteObjectLostNotificationRequest request) throws IOException, SignatureException {
        return new DeleteNotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse createObjectPersistedNotification(CreateObjectPersistedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse getObjectPersistedNotification(GetObjectPersistedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteNotificationResponse deleteObjectPersistedNotification(DeleteObjectPersistedNotificationRequest request) throws IOException, SignatureException {
        return new DeleteNotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse createPartitionFailureNotification(CreatePartitionFailureNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse getPartitionFailureNotification(GetPartitionFailureNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteNotificationResponse deletePartitionFailureNotification(DeletePartitionFailureNotificationRequest request) throws IOException, SignatureException {
        return new DeleteNotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse createTapeFailureNotification(CreateTapeFailureNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse getTapeFailureNotification(GetTapeFailureNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteNotificationResponse deleteTapeFailureNotification(DeleteTapeFailureNotificationRequest request) throws IOException, SignatureException {
        return new DeleteNotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public Ds3Client newForNode(final Node node) {
        final ConnectionDetails newConnectionDetails = ConnectionDetailsImpl.newForNode(node, this.getConnectionDetails());
        final NetworkClient newNetClient = new NetworkClientImpl(newConnectionDetails);
        return new Ds3ClientImpl(newNetClient);
    }
}
