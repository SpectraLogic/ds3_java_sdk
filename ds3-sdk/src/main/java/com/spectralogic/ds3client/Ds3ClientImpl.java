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

import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.notifications.*;
import com.spectralogic.ds3client.models.bulk.Node;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.networking.NetworkClient;

import java.io.IOException;
import java.security.SignatureException;

public class Ds3ClientImpl implements Ds3Client {
    private final NetworkClient netClient;

    public Ds3ClientImpl(final NetworkClient netClient) {
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
    public DeleteFolderResponse deleteFolder(final DeleteFolderRequest request) throws IOException, SignatureException {
        return new DeleteFolderResponse(this.netClient.getResponse(request));
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
            this.netClient.getConnectionDetails().getBufferSize(),
            request.getObjectName()
        );
    }

    @Override
    public GetObjectsResponse getObjects(final GetObjectsRequest request) throws IOException, SignatureException {
        return new GetObjectsResponse(this.netClient.getResponse(request));
    }

    @Override
    public PutObjectResponse putObject(final PutObjectRequest request) throws IOException, SignatureException {
        return new PutObjectResponse(this.netClient.getResponse(request));
    }

    @Override
    public HeadObjectResponse headObject(final HeadObjectRequest request) throws IOException, SignatureException {
        return new HeadObjectResponse(this.netClient.getResponse(request));
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
    public DeleteTapeDriveResponse deleteTapeDrive(final DeleteTapeDriveRequest request) throws IOException, SignatureException {
        return new DeleteTapeDriveResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteTapePartitionResponse deleteTapePartition(final DeleteTapePartitionRequest request) throws IOException, SignatureException {
        return new DeleteTapePartitionResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetTapesResponse getTapes(final GetTapesRequest request) throws IOException, SignatureException {
        return new GetTapesResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteTapeResponse deleteTape(final DeleteTapeRequest request) throws IOException, SignatureException {
        return new DeleteTapeResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetTapeResponse getTape(final GetTapeRequest request) throws IOException, SignatureException {
        return new GetTapeResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse createObjectCachedNotification(final CreateObjectCachedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse getObjectCachedNotification(final GetObjectCachedNotificationRequest request) throws IOException, SignatureException {
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
    public NotificationResponse getJobCompletedNotification(final GetJobCompletedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteNotificationResponse deleteJobCompleteNotification(final DeleteJobCompletedNotificationRequest request) throws IOException, SignatureException {
        return new DeleteNotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse createJobCreatedNotification(final CreateJobCreatedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse getJobCreatedNotification(final GetJobCreatedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteNotificationResponse deleteJobCreatedNotification(final DeleteJobCreatedNotificationRequest request) throws IOException, SignatureException {
        return new DeleteNotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse createObjectLostNotification(final CreateObjectLostNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse getObjectLostNotification(final GetObjectLostNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteNotificationResponse deleteObjectLostNotification(final DeleteObjectLostNotificationRequest request) throws IOException, SignatureException {
        return new DeleteNotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse createObjectPersistedNotification(final CreateObjectPersistedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse getObjectPersistedNotification(final GetObjectPersistedNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteNotificationResponse deleteObjectPersistedNotification(final DeleteObjectPersistedNotificationRequest request) throws IOException, SignatureException {
        return new DeleteNotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse createPartitionFailureNotification(final CreatePartitionFailureNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse getPartitionFailureNotification(final GetPartitionFailureNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteNotificationResponse deletePartitionFailureNotification(final DeletePartitionFailureNotificationRequest request) throws IOException, SignatureException {
        return new DeleteNotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse createTapeFailureNotification(final CreateTapeFailureNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public NotificationResponse getTapeFailureNotification(final GetTapeFailureNotificationRequest request) throws IOException, SignatureException {
        return new NotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public DeleteNotificationResponse deleteTapeFailureNotification(final DeleteTapeFailureNotificationRequest request) throws IOException, SignatureException {
        return new DeleteNotificationResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetSystemHealthResponse getSystemHealth(final GetSystemHealthRequest request) throws IOException, SignatureException {
        return new GetSystemHealthResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetSystemInformationResponse getSystemInformation(final GetSystemInformationRequest request) throws IOException, SignatureException {
        return new GetSystemInformationResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetTapeLibrariesResponse getTapeLibraries(final GetTapeLibrariesRequest request) throws IOException, SignatureException {
        return new GetTapeLibrariesResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetTapeLibraryResponse getTapeLibrary(final GetTapeLibraryRequest request) throws IOException, SignatureException {
        return new GetTapeLibraryResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetTapeDrivesResponse getTapeDrives(final GetTapeDrivesRequest request) throws IOException, SignatureException {
        return new GetTapeDrivesResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetTapeDriveResponse getTapeDrive(final GetTapeDriveRequest request) throws IOException, SignatureException {
        return new GetTapeDriveResponse(this.netClient.getResponse(request));
    }

    @Override
    public GetTapeFailureResponse getTapeFailure(final GetTapeFailureRequest request) throws IOException, SignatureException {
        return new GetTapeFailureResponse(this.netClient.getResponse(request));
    }

    @Override
    public Ds3Client newForNode(final Node node) {
        final ConnectionDetails newConnectionDetails = ConnectionDetailsImpl.newForNode(node, this.getConnectionDetails());
        final NetworkClient newNetClient = new NetworkClientImpl(newConnectionDetails);
        return new Ds3ClientImpl(newNetClient);
    }

    @Override
    public void close() throws IOException {
        this.netClient.close();
    }
}
