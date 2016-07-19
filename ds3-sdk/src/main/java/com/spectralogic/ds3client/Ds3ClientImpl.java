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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client;

import java.io.IOException;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.commands.spectrads3.notifications.*;
import com.spectralogic.ds3client.models.JobNode;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.networking.NetworkClient;
import com.spectralogic.ds3client.networking.NetworkClientImpl;

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
    public AbortMultiPartUploadResponse abortMultiPartUpload(final AbortMultiPartUploadRequest request) throws IOException {
        return new AbortMultiPartUploadResponse(this.netClient.getResponse(request));
    }
    @Override
    public CompleteMultiPartUploadResponse completeMultiPartUpload(final CompleteMultiPartUploadRequest request) throws IOException {
        return new CompleteMultiPartUploadResponse(this.netClient.getResponse(request));
    }
    @Override
    public PutBucketResponse putBucket(final PutBucketRequest request) throws IOException {
        return new PutBucketResponse(this.netClient.getResponse(request));
    }
    @Override
    public PutMultiPartUploadPartResponse putMultiPartUploadPart(final PutMultiPartUploadPartRequest request) throws IOException {
        return new PutMultiPartUploadPartResponse(this.netClient.getResponse(request));
    }
    @Override
    public PutObjectResponse putObject(final PutObjectRequest request) throws IOException {
        return new PutObjectResponse(this.netClient.getResponse(request));
    }
    @Override
    public DeleteBucketResponse deleteBucket(final DeleteBucketRequest request) throws IOException {
        return new DeleteBucketResponse(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectResponse deleteObject(final DeleteObjectRequest request) throws IOException {
        return new DeleteObjectResponse(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectsResponse deleteObjects(final DeleteObjectsRequest request) throws IOException {
        return new DeleteObjectsResponse(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketResponse getBucket(final GetBucketRequest request) throws IOException {
        return new GetBucketResponse(this.netClient.getResponse(request));
    }
    @Override
    public GetServiceResponse getService(final GetServiceRequest request) throws IOException {
        return new GetServiceResponse(this.netClient.getResponse(request));
    }
    @Override
    public HeadBucketResponse headBucket(final HeadBucketRequest request) throws IOException {
        return new HeadBucketResponse(this.netClient.getResponse(request));
    }
    @Override
    public HeadObjectResponse headObject(final HeadObjectRequest request) throws IOException {
        return new HeadObjectResponse(this.netClient.getResponse(request));
    }
    @Override
    public InitiateMultiPartUploadResponse initiateMultiPartUpload(final InitiateMultiPartUploadRequest request) throws IOException {
        return new InitiateMultiPartUploadResponse(this.netClient.getResponse(request));
    }
    @Override
    public ListMultiPartUploadPartsResponse listMultiPartUploadParts(final ListMultiPartUploadPartsRequest request) throws IOException {
        return new ListMultiPartUploadPartsResponse(this.netClient.getResponse(request));
    }
    @Override
    public ListMultiPartUploadsResponse listMultiPartUploads(final ListMultiPartUploadsRequest request) throws IOException {
        return new ListMultiPartUploadsResponse(this.netClient.getResponse(request));
    }
    @Override
    public PutBucketAclForGroupSpectraS3Response putBucketAclForGroupSpectraS3(final PutBucketAclForGroupSpectraS3Request request) throws IOException {
        return new PutBucketAclForGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutBucketAclForUserSpectraS3Response putBucketAclForUserSpectraS3(final PutBucketAclForUserSpectraS3Request request) throws IOException {
        return new PutBucketAclForUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataPolicyAclForGroupSpectraS3Response putDataPolicyAclForGroupSpectraS3(final PutDataPolicyAclForGroupSpectraS3Request request) throws IOException {
        return new PutDataPolicyAclForGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataPolicyAclForUserSpectraS3Response putDataPolicyAclForUserSpectraS3(final PutDataPolicyAclForUserSpectraS3Request request) throws IOException {
        return new PutDataPolicyAclForUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutGlobalBucketAclForGroupSpectraS3Response putGlobalBucketAclForGroupSpectraS3(final PutGlobalBucketAclForGroupSpectraS3Request request) throws IOException {
        return new PutGlobalBucketAclForGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutGlobalBucketAclForUserSpectraS3Response putGlobalBucketAclForUserSpectraS3(final PutGlobalBucketAclForUserSpectraS3Request request) throws IOException {
        return new PutGlobalBucketAclForUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutGlobalDataPolicyAclForGroupSpectraS3Response putGlobalDataPolicyAclForGroupSpectraS3(final PutGlobalDataPolicyAclForGroupSpectraS3Request request) throws IOException {
        return new PutGlobalDataPolicyAclForGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutGlobalDataPolicyAclForUserSpectraS3Response putGlobalDataPolicyAclForUserSpectraS3(final PutGlobalDataPolicyAclForUserSpectraS3Request request) throws IOException {
        return new PutGlobalDataPolicyAclForUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteBucketAclSpectraS3Response deleteBucketAclSpectraS3(final DeleteBucketAclSpectraS3Request request) throws IOException {
        return new DeleteBucketAclSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDataPolicyAclSpectraS3Response deleteDataPolicyAclSpectraS3(final DeleteDataPolicyAclSpectraS3Request request) throws IOException {
        return new DeleteDataPolicyAclSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketAclSpectraS3Response getBucketAclSpectraS3(final GetBucketAclSpectraS3Request request) throws IOException {
        return new GetBucketAclSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketAclsSpectraS3Response getBucketAclsSpectraS3(final GetBucketAclsSpectraS3Request request) throws IOException {
        return new GetBucketAclsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPolicyAclSpectraS3Response getDataPolicyAclSpectraS3(final GetDataPolicyAclSpectraS3Request request) throws IOException {
        return new GetDataPolicyAclSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3(final GetDataPolicyAclsSpectraS3Request request) throws IOException {
        return new GetDataPolicyAclsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutBucketSpectraS3Response putBucketSpectraS3(final PutBucketSpectraS3Request request) throws IOException {
        return new PutBucketSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteBucketSpectraS3Response deleteBucketSpectraS3(final DeleteBucketSpectraS3Request request) throws IOException {
        return new DeleteBucketSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketSpectraS3Response getBucketSpectraS3(final GetBucketSpectraS3Request request) throws IOException {
        return new GetBucketSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketsSpectraS3Response getBucketsSpectraS3(final GetBucketsSpectraS3Request request) throws IOException {
        return new GetBucketsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyBucketSpectraS3Response modifyBucketSpectraS3(final ModifyBucketSpectraS3Request request) throws IOException {
        return new ModifyBucketSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ForceFullCacheReclaimSpectraS3Response forceFullCacheReclaimSpectraS3(final ForceFullCacheReclaimSpectraS3Request request) throws IOException {
        return new ForceFullCacheReclaimSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetCacheFilesystemSpectraS3Response getCacheFilesystemSpectraS3(final GetCacheFilesystemSpectraS3Request request) throws IOException {
        return new GetCacheFilesystemSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetCacheFilesystemsSpectraS3Response getCacheFilesystemsSpectraS3(final GetCacheFilesystemsSpectraS3Request request) throws IOException {
        return new GetCacheFilesystemsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetCacheStateSpectraS3Response getCacheStateSpectraS3(final GetCacheStateSpectraS3Request request) throws IOException {
        return new GetCacheStateSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyCacheFilesystemSpectraS3Response modifyCacheFilesystemSpectraS3(final ModifyCacheFilesystemSpectraS3Request request) throws IOException {
        return new ModifyCacheFilesystemSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketCapacitySummarySpectraS3Response getBucketCapacitySummarySpectraS3(final GetBucketCapacitySummarySpectraS3Request request) throws IOException {
        return new GetBucketCapacitySummarySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainCapacitySummarySpectraS3Response getStorageDomainCapacitySummarySpectraS3(final GetStorageDomainCapacitySummarySpectraS3Request request) throws IOException {
        return new GetStorageDomainCapacitySummarySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemCapacitySummarySpectraS3Response getSystemCapacitySummarySpectraS3(final GetSystemCapacitySummarySpectraS3Request request) throws IOException {
        return new GetSystemCapacitySummarySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPathBackendSpectraS3Response getDataPathBackendSpectraS3(final GetDataPathBackendSpectraS3Request request) throws IOException {
        return new GetDataPathBackendSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPlannerBlobStoreTasksSpectraS3Response getDataPlannerBlobStoreTasksSpectraS3(final GetDataPlannerBlobStoreTasksSpectraS3Request request) throws IOException {
        return new GetDataPlannerBlobStoreTasksSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyDataPathBackendSpectraS3Response modifyDataPathBackendSpectraS3(final ModifyDataPathBackendSpectraS3Request request) throws IOException {
        return new ModifyDataPathBackendSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataPersistenceRuleSpectraS3Response putDataPersistenceRuleSpectraS3(final PutDataPersistenceRuleSpectraS3Request request) throws IOException {
        return new PutDataPersistenceRuleSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataPolicySpectraS3Response putDataPolicySpectraS3(final PutDataPolicySpectraS3Request request) throws IOException {
        return new PutDataPolicySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDataPersistenceRuleSpectraS3Response deleteDataPersistenceRuleSpectraS3(final DeleteDataPersistenceRuleSpectraS3Request request) throws IOException {
        return new DeleteDataPersistenceRuleSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDataPolicySpectraS3Response deleteDataPolicySpectraS3(final DeleteDataPolicySpectraS3Request request) throws IOException {
        return new DeleteDataPolicySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPersistenceRuleSpectraS3Response getDataPersistenceRuleSpectraS3(final GetDataPersistenceRuleSpectraS3Request request) throws IOException {
        return new GetDataPersistenceRuleSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPersistenceRulesSpectraS3Response getDataPersistenceRulesSpectraS3(final GetDataPersistenceRulesSpectraS3Request request) throws IOException {
        return new GetDataPersistenceRulesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPoliciesSpectraS3Response getDataPoliciesSpectraS3(final GetDataPoliciesSpectraS3Request request) throws IOException {
        return new GetDataPoliciesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPolicySpectraS3Response getDataPolicySpectraS3(final GetDataPolicySpectraS3Request request) throws IOException {
        return new GetDataPolicySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyDataPersistenceRuleSpectraS3Response modifyDataPersistenceRuleSpectraS3(final ModifyDataPersistenceRuleSpectraS3Request request) throws IOException {
        return new ModifyDataPersistenceRuleSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyDataPolicySpectraS3Response modifyDataPolicySpectraS3(final ModifyDataPolicySpectraS3Request request) throws IOException {
        return new ModifyDataPolicySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDegradedBucketsSpectraS3Response getDegradedBucketsSpectraS3(final GetDegradedBucketsSpectraS3Request request) throws IOException {
        return new GetDegradedBucketsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDegradedDataPersistenceRulesSpectraS3Response getDegradedDataPersistenceRulesSpectraS3(final GetDegradedDataPersistenceRulesSpectraS3Request request) throws IOException {
        return new GetDegradedDataPersistenceRulesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutGroupGroupMemberSpectraS3Response putGroupGroupMemberSpectraS3(final PutGroupGroupMemberSpectraS3Request request) throws IOException {
        return new PutGroupGroupMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutGroupSpectraS3Response putGroupSpectraS3(final PutGroupSpectraS3Request request) throws IOException {
        return new PutGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutUserGroupMemberSpectraS3Response putUserGroupMemberSpectraS3(final PutUserGroupMemberSpectraS3Request request) throws IOException {
        return new PutUserGroupMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteGroupMemberSpectraS3Response deleteGroupMemberSpectraS3(final DeleteGroupMemberSpectraS3Request request) throws IOException {
        return new DeleteGroupMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteGroupSpectraS3Response deleteGroupSpectraS3(final DeleteGroupSpectraS3Request request) throws IOException {
        return new DeleteGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetGroupMemberSpectraS3Response getGroupMemberSpectraS3(final GetGroupMemberSpectraS3Request request) throws IOException {
        return new GetGroupMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetGroupMembersSpectraS3Response getGroupMembersSpectraS3(final GetGroupMembersSpectraS3Request request) throws IOException {
        return new GetGroupMembersSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetGroupSpectraS3Response getGroupSpectraS3(final GetGroupSpectraS3Request request) throws IOException {
        return new GetGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetGroupsSpectraS3Response getGroupsSpectraS3(final GetGroupsSpectraS3Request request) throws IOException {
        return new GetGroupsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyGroupSpectraS3Response modifyGroupSpectraS3(final ModifyGroupSpectraS3Request request) throws IOException {
        return new ModifyGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyUserIsMemberOfGroupSpectraS3Response verifyUserIsMemberOfGroupSpectraS3(final VerifyUserIsMemberOfGroupSpectraS3Request request) throws IOException {
        return new VerifyUserIsMemberOfGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public AllocateJobChunkSpectraS3Response allocateJobChunkSpectraS3(final AllocateJobChunkSpectraS3Request request) throws IOException {
        return new AllocateJobChunkSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelAllJobsSpectraS3Response cancelAllJobsSpectraS3(final CancelAllJobsSpectraS3Request request) throws IOException {
        return new CancelAllJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelJobSpectraS3Response cancelJobSpectraS3(final CancelJobSpectraS3Request request) throws IOException {
        return new CancelJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ClearAllCanceledJobsSpectraS3Response clearAllCanceledJobsSpectraS3(final ClearAllCanceledJobsSpectraS3Request request) throws IOException {
        return new ClearAllCanceledJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ClearAllCompletedJobsSpectraS3Response clearAllCompletedJobsSpectraS3(final ClearAllCompletedJobsSpectraS3Request request) throws IOException {
        return new ClearAllCompletedJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBulkJobSpectraS3Response getBulkJobSpectraS3(final GetBulkJobSpectraS3Request request) throws IOException {
        return new GetBulkJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutBulkJobSpectraS3Response putBulkJobSpectraS3(final PutBulkJobSpectraS3Request request) throws IOException {
        return new PutBulkJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyBulkJobSpectraS3Response verifyBulkJobSpectraS3(final VerifyBulkJobSpectraS3Request request) throws IOException {
        return new VerifyBulkJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetActiveJobsSpectraS3Response getActiveJobsSpectraS3(final GetActiveJobsSpectraS3Request request) throws IOException {
        return new GetActiveJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetCanceledJobsSpectraS3Response getCanceledJobsSpectraS3(final GetCanceledJobsSpectraS3Request request) throws IOException {
        return new GetCanceledJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetCompletedJobsSpectraS3Response getCompletedJobsSpectraS3(final GetCompletedJobsSpectraS3Request request) throws IOException {
        return new GetCompletedJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobChunkSpectraS3Response getJobChunkSpectraS3(final GetJobChunkSpectraS3Request request) throws IOException {
        return new GetJobChunkSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobChunksReadyForClientProcessingSpectraS3Response getJobChunksReadyForClientProcessingSpectraS3(final GetJobChunksReadyForClientProcessingSpectraS3Request request) throws IOException {
        return new GetJobChunksReadyForClientProcessingSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobSpectraS3Response getJobSpectraS3(final GetJobSpectraS3Request request) throws IOException {
        return new GetJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobsSpectraS3Response getJobsSpectraS3(final GetJobsSpectraS3Request request) throws IOException {
        return new GetJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPutJobToReplicateSpectraS3Response getPutJobToReplicateSpectraS3(final GetPutJobToReplicateSpectraS3Request request) throws IOException {
        return new GetPutJobToReplicateSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyJobSpectraS3Response modifyJobSpectraS3(final ModifyJobSpectraS3Request request) throws IOException {
        return new ModifyJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ReplicatePutJobSpectraS3Response replicatePutJobSpectraS3(final ReplicatePutJobSpectraS3Request request) throws IOException {
        return new ReplicatePutJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetNodeSpectraS3Response getNodeSpectraS3(final GetNodeSpectraS3Request request) throws IOException {
        return new GetNodeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetNodesSpectraS3Response getNodesSpectraS3(final GetNodesSpectraS3Request request) throws IOException {
        return new GetNodesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyNodeSpectraS3Response modifyNodeSpectraS3(final ModifyNodeSpectraS3Request request) throws IOException {
        return new ModifyNodeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutJobCompletedNotificationRegistrationSpectraS3Response putJobCompletedNotificationRegistrationSpectraS3(final PutJobCompletedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutJobCompletedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutJobCreatedNotificationRegistrationSpectraS3Response putJobCreatedNotificationRegistrationSpectraS3(final PutJobCreatedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutJobCreatedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutObjectCachedNotificationRegistrationSpectraS3Response putObjectCachedNotificationRegistrationSpectraS3(final PutObjectCachedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutObjectCachedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutObjectLostNotificationRegistrationSpectraS3Response putObjectLostNotificationRegistrationSpectraS3(final PutObjectLostNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutObjectLostNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutObjectPersistedNotificationRegistrationSpectraS3Response putObjectPersistedNotificationRegistrationSpectraS3(final PutObjectPersistedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutObjectPersistedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutPoolFailureNotificationRegistrationSpectraS3Response putPoolFailureNotificationRegistrationSpectraS3(final PutPoolFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutPoolFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutStorageDomainFailureNotificationRegistrationSpectraS3Response putStorageDomainFailureNotificationRegistrationSpectraS3(final PutStorageDomainFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutStorageDomainFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutSystemFailureNotificationRegistrationSpectraS3Response putSystemFailureNotificationRegistrationSpectraS3(final PutSystemFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutSystemFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutTapeFailureNotificationRegistrationSpectraS3Response putTapeFailureNotificationRegistrationSpectraS3(final PutTapeFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutTapeFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutTapePartitionFailureNotificationRegistrationSpectraS3Response putTapePartitionFailureNotificationRegistrationSpectraS3(final PutTapePartitionFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutTapePartitionFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteJobCompletedNotificationRegistrationSpectraS3Response deleteJobCompletedNotificationRegistrationSpectraS3(final DeleteJobCompletedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteJobCompletedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteJobCreatedNotificationRegistrationSpectraS3Response deleteJobCreatedNotificationRegistrationSpectraS3(final DeleteJobCreatedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteJobCreatedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectCachedNotificationRegistrationSpectraS3Response deleteObjectCachedNotificationRegistrationSpectraS3(final DeleteObjectCachedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteObjectCachedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectLostNotificationRegistrationSpectraS3Response deleteObjectLostNotificationRegistrationSpectraS3(final DeleteObjectLostNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteObjectLostNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectPersistedNotificationRegistrationSpectraS3Response deleteObjectPersistedNotificationRegistrationSpectraS3(final DeleteObjectPersistedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteObjectPersistedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePoolFailureNotificationRegistrationSpectraS3Response deletePoolFailureNotificationRegistrationSpectraS3(final DeletePoolFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeletePoolFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteStorageDomainFailureNotificationRegistrationSpectraS3Response deleteStorageDomainFailureNotificationRegistrationSpectraS3(final DeleteStorageDomainFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteStorageDomainFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteSystemFailureNotificationRegistrationSpectraS3Response deleteSystemFailureNotificationRegistrationSpectraS3(final DeleteSystemFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteSystemFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapeFailureNotificationRegistrationSpectraS3Response deleteTapeFailureNotificationRegistrationSpectraS3(final DeleteTapeFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteTapeFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapePartitionFailureNotificationRegistrationSpectraS3Response deleteTapePartitionFailureNotificationRegistrationSpectraS3(final DeleteTapePartitionFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteTapePartitionFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCompletedNotificationRegistrationSpectraS3Response getJobCompletedNotificationRegistrationSpectraS3(final GetJobCompletedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetJobCompletedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCompletedNotificationRegistrationsSpectraS3Response getJobCompletedNotificationRegistrationsSpectraS3(final GetJobCompletedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetJobCompletedNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCreatedNotificationRegistrationSpectraS3Response getJobCreatedNotificationRegistrationSpectraS3(final GetJobCreatedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetJobCreatedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCreatedNotificationRegistrationsSpectraS3Response getJobCreatedNotificationRegistrationsSpectraS3(final GetJobCreatedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetJobCreatedNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectCachedNotificationRegistrationSpectraS3Response getObjectCachedNotificationRegistrationSpectraS3(final GetObjectCachedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetObjectCachedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectCachedNotificationRegistrationsSpectraS3Response getObjectCachedNotificationRegistrationsSpectraS3(final GetObjectCachedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetObjectCachedNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectLostNotificationRegistrationSpectraS3Response getObjectLostNotificationRegistrationSpectraS3(final GetObjectLostNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetObjectLostNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectLostNotificationRegistrationsSpectraS3Response getObjectLostNotificationRegistrationsSpectraS3(final GetObjectLostNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetObjectLostNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectPersistedNotificationRegistrationSpectraS3Response getObjectPersistedNotificationRegistrationSpectraS3(final GetObjectPersistedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetObjectPersistedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectPersistedNotificationRegistrationsSpectraS3Response getObjectPersistedNotificationRegistrationsSpectraS3(final GetObjectPersistedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetObjectPersistedNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolFailureNotificationRegistrationSpectraS3Response getPoolFailureNotificationRegistrationSpectraS3(final GetPoolFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetPoolFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolFailureNotificationRegistrationsSpectraS3Response getPoolFailureNotificationRegistrationsSpectraS3(final GetPoolFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetPoolFailureNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainFailureNotificationRegistrationSpectraS3Response getStorageDomainFailureNotificationRegistrationSpectraS3(final GetStorageDomainFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetStorageDomainFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainFailureNotificationRegistrationsSpectraS3Response getStorageDomainFailureNotificationRegistrationsSpectraS3(final GetStorageDomainFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetStorageDomainFailureNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemFailureNotificationRegistrationSpectraS3Response getSystemFailureNotificationRegistrationSpectraS3(final GetSystemFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetSystemFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemFailureNotificationRegistrationsSpectraS3Response getSystemFailureNotificationRegistrationsSpectraS3(final GetSystemFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetSystemFailureNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeFailureNotificationRegistrationSpectraS3Response getTapeFailureNotificationRegistrationSpectraS3(final GetTapeFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetTapeFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeFailureNotificationRegistrationsSpectraS3Response getTapeFailureNotificationRegistrationsSpectraS3(final GetTapeFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetTapeFailureNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionFailureNotificationRegistrationSpectraS3Response getTapePartitionFailureNotificationRegistrationSpectraS3(final GetTapePartitionFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetTapePartitionFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionFailureNotificationRegistrationsSpectraS3Response getTapePartitionFailureNotificationRegistrationsSpectraS3(final GetTapePartitionFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetTapePartitionFailureNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteFolderRecursivelySpectraS3Response deleteFolderRecursivelySpectraS3(final DeleteFolderRecursivelySpectraS3Request request) throws IOException {
        return new DeleteFolderRecursivelySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectDetailsSpectraS3Response getObjectDetailsSpectraS3(final GetObjectDetailsSpectraS3Request request) throws IOException {
        return new GetObjectDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectsSpectraS3Response getObjectsSpectraS3(final GetObjectsSpectraS3Request request) throws IOException {
        return new GetObjectsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectsWithFullDetailsSpectraS3Response getObjectsWithFullDetailsSpectraS3(final GetObjectsWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetObjectsWithFullDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPhysicalPlacementForObjectsSpectraS3Response getPhysicalPlacementForObjectsSpectraS3(final GetPhysicalPlacementForObjectsSpectraS3Request request) throws IOException {
        return new GetPhysicalPlacementForObjectsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response getPhysicalPlacementForObjectsWithFullDetailsSpectraS3(final GetPhysicalPlacementForObjectsWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyPhysicalPlacementForObjectsSpectraS3Response verifyPhysicalPlacementForObjectsSpectraS3(final VerifyPhysicalPlacementForObjectsSpectraS3Request request) throws IOException {
        return new VerifyPhysicalPlacementForObjectsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response verifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3(final VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Request request) throws IOException {
        return new VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelImportOnAllPoolsSpectraS3Response cancelImportOnAllPoolsSpectraS3(final CancelImportOnAllPoolsSpectraS3Request request) throws IOException {
        return new CancelImportOnAllPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelImportPoolSpectraS3Response cancelImportPoolSpectraS3(final CancelImportPoolSpectraS3Request request) throws IOException {
        return new CancelImportPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CompactAllPoolsSpectraS3Response compactAllPoolsSpectraS3(final CompactAllPoolsSpectraS3Request request) throws IOException {
        return new CompactAllPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CompactPoolSpectraS3Response compactPoolSpectraS3(final CompactPoolSpectraS3Request request) throws IOException {
        return new CompactPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutPoolPartitionSpectraS3Response putPoolPartitionSpectraS3(final PutPoolPartitionSpectraS3Request request) throws IOException {
        return new PutPoolPartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeallocatePoolSpectraS3Response deallocatePoolSpectraS3(final DeallocatePoolSpectraS3Request request) throws IOException {
        return new DeallocatePoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePermanentlyLostPoolSpectraS3Response deletePermanentlyLostPoolSpectraS3(final DeletePermanentlyLostPoolSpectraS3Request request) throws IOException {
        return new DeletePermanentlyLostPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePoolFailureSpectraS3Response deletePoolFailureSpectraS3(final DeletePoolFailureSpectraS3Request request) throws IOException {
        return new DeletePoolFailureSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePoolPartitionSpectraS3Response deletePoolPartitionSpectraS3(final DeletePoolPartitionSpectraS3Request request) throws IOException {
        return new DeletePoolPartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ForcePoolEnvironmentRefreshSpectraS3Response forcePoolEnvironmentRefreshSpectraS3(final ForcePoolEnvironmentRefreshSpectraS3Request request) throws IOException {
        return new ForcePoolEnvironmentRefreshSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public FormatAllForeignPoolsSpectraS3Response formatAllForeignPoolsSpectraS3(final FormatAllForeignPoolsSpectraS3Request request) throws IOException {
        return new FormatAllForeignPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public FormatForeignPoolSpectraS3Response formatForeignPoolSpectraS3(final FormatForeignPoolSpectraS3Request request) throws IOException {
        return new FormatForeignPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBlobsOnPoolSpectraS3Response getBlobsOnPoolSpectraS3(final GetBlobsOnPoolSpectraS3Request request) throws IOException {
        return new GetBlobsOnPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolFailuresSpectraS3Response getPoolFailuresSpectraS3(final GetPoolFailuresSpectraS3Request request) throws IOException {
        return new GetPoolFailuresSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolPartitionSpectraS3Response getPoolPartitionSpectraS3(final GetPoolPartitionSpectraS3Request request) throws IOException {
        return new GetPoolPartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolPartitionsSpectraS3Response getPoolPartitionsSpectraS3(final GetPoolPartitionsSpectraS3Request request) throws IOException {
        return new GetPoolPartitionsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolSpectraS3Response getPoolSpectraS3(final GetPoolSpectraS3Request request) throws IOException {
        return new GetPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolsSpectraS3Response getPoolsSpectraS3(final GetPoolsSpectraS3Request request) throws IOException {
        return new GetPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ImportAllPoolsSpectraS3Response importAllPoolsSpectraS3(final ImportAllPoolsSpectraS3Request request) throws IOException {
        return new ImportAllPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ImportPoolSpectraS3Response importPoolSpectraS3(final ImportPoolSpectraS3Request request) throws IOException {
        return new ImportPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyAllPoolsSpectraS3Response modifyAllPoolsSpectraS3(final ModifyAllPoolsSpectraS3Request request) throws IOException {
        return new ModifyAllPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyPoolPartitionSpectraS3Response modifyPoolPartitionSpectraS3(final ModifyPoolPartitionSpectraS3Request request) throws IOException {
        return new ModifyPoolPartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyPoolSpectraS3Response modifyPoolSpectraS3(final ModifyPoolSpectraS3Request request) throws IOException {
        return new ModifyPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyAllPoolsSpectraS3Response verifyAllPoolsSpectraS3(final VerifyAllPoolsSpectraS3Request request) throws IOException {
        return new VerifyAllPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyPoolSpectraS3Response verifyPoolSpectraS3(final VerifyPoolSpectraS3Request request) throws IOException {
        return new VerifyPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutPoolStorageDomainMemberSpectraS3Response putPoolStorageDomainMemberSpectraS3(final PutPoolStorageDomainMemberSpectraS3Request request) throws IOException {
        return new PutPoolStorageDomainMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutStorageDomainSpectraS3Response putStorageDomainSpectraS3(final PutStorageDomainSpectraS3Request request) throws IOException {
        return new PutStorageDomainSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutTapeStorageDomainMemberSpectraS3Response putTapeStorageDomainMemberSpectraS3(final PutTapeStorageDomainMemberSpectraS3Request request) throws IOException {
        return new PutTapeStorageDomainMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteStorageDomainFailureSpectraS3Response deleteStorageDomainFailureSpectraS3(final DeleteStorageDomainFailureSpectraS3Request request) throws IOException {
        return new DeleteStorageDomainFailureSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteStorageDomainMemberSpectraS3Response deleteStorageDomainMemberSpectraS3(final DeleteStorageDomainMemberSpectraS3Request request) throws IOException {
        return new DeleteStorageDomainMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteStorageDomainSpectraS3Response deleteStorageDomainSpectraS3(final DeleteStorageDomainSpectraS3Request request) throws IOException {
        return new DeleteStorageDomainSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainFailuresSpectraS3Response getStorageDomainFailuresSpectraS3(final GetStorageDomainFailuresSpectraS3Request request) throws IOException {
        return new GetStorageDomainFailuresSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainMemberSpectraS3Response getStorageDomainMemberSpectraS3(final GetStorageDomainMemberSpectraS3Request request) throws IOException {
        return new GetStorageDomainMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainMembersSpectraS3Response getStorageDomainMembersSpectraS3(final GetStorageDomainMembersSpectraS3Request request) throws IOException {
        return new GetStorageDomainMembersSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainSpectraS3Response getStorageDomainSpectraS3(final GetStorageDomainSpectraS3Request request) throws IOException {
        return new GetStorageDomainSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainsSpectraS3Response getStorageDomainsSpectraS3(final GetStorageDomainsSpectraS3Request request) throws IOException {
        return new GetStorageDomainsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyStorageDomainMemberSpectraS3Response modifyStorageDomainMemberSpectraS3(final ModifyStorageDomainMemberSpectraS3Request request) throws IOException {
        return new ModifyStorageDomainMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyStorageDomainSpectraS3Response modifyStorageDomainSpectraS3(final ModifyStorageDomainSpectraS3Request request) throws IOException {
        return new ModifyStorageDomainSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemFailuresSpectraS3Response getSystemFailuresSpectraS3(final GetSystemFailuresSpectraS3Request request) throws IOException {
        return new GetSystemFailuresSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemInformationSpectraS3Response getSystemInformationSpectraS3(final GetSystemInformationSpectraS3Request request) throws IOException {
        return new GetSystemInformationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifySystemHealthSpectraS3Response verifySystemHealthSpectraS3(final VerifySystemHealthSpectraS3Request request) throws IOException {
        return new VerifySystemHealthSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelEjectOnAllTapesSpectraS3Response cancelEjectOnAllTapesSpectraS3(final CancelEjectOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelEjectOnAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelEjectTapeSpectraS3Response cancelEjectTapeSpectraS3(final CancelEjectTapeSpectraS3Request request) throws IOException {
        return new CancelEjectTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelFormatOnAllTapesSpectraS3Response cancelFormatOnAllTapesSpectraS3(final CancelFormatOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelFormatOnAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelFormatTapeSpectraS3Response cancelFormatTapeSpectraS3(final CancelFormatTapeSpectraS3Request request) throws IOException {
        return new CancelFormatTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelImportOnAllTapesSpectraS3Response cancelImportOnAllTapesSpectraS3(final CancelImportOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelImportOnAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelImportTapeSpectraS3Response cancelImportTapeSpectraS3(final CancelImportTapeSpectraS3Request request) throws IOException {
        return new CancelImportTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelOnlineOnAllTapesSpectraS3Response cancelOnlineOnAllTapesSpectraS3(final CancelOnlineOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelOnlineOnAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelOnlineTapeSpectraS3Response cancelOnlineTapeSpectraS3(final CancelOnlineTapeSpectraS3Request request) throws IOException {
        return new CancelOnlineTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CleanTapeDriveSpectraS3Response cleanTapeDriveSpectraS3(final CleanTapeDriveSpectraS3Request request) throws IOException {
        return new CleanTapeDriveSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutTapeDensityDirectiveSpectraS3Response putTapeDensityDirectiveSpectraS3(final PutTapeDensityDirectiveSpectraS3Request request) throws IOException {
        return new PutTapeDensityDirectiveSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePermanentlyLostTapeSpectraS3Response deletePermanentlyLostTapeSpectraS3(final DeletePermanentlyLostTapeSpectraS3Request request) throws IOException {
        return new DeletePermanentlyLostTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapeDensityDirectiveSpectraS3Response deleteTapeDensityDirectiveSpectraS3(final DeleteTapeDensityDirectiveSpectraS3Request request) throws IOException {
        return new DeleteTapeDensityDirectiveSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapeDriveSpectraS3Response deleteTapeDriveSpectraS3(final DeleteTapeDriveSpectraS3Request request) throws IOException {
        return new DeleteTapeDriveSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapeFailureSpectraS3Response deleteTapeFailureSpectraS3(final DeleteTapeFailureSpectraS3Request request) throws IOException {
        return new DeleteTapeFailureSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapePartitionFailureSpectraS3Response deleteTapePartitionFailureSpectraS3(final DeleteTapePartitionFailureSpectraS3Request request) throws IOException {
        return new DeleteTapePartitionFailureSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapePartitionSpectraS3Response deleteTapePartitionSpectraS3(final DeleteTapePartitionSpectraS3Request request) throws IOException {
        return new DeleteTapePartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public EjectAllTapesSpectraS3Response ejectAllTapesSpectraS3(final EjectAllTapesSpectraS3Request request) throws IOException {
        return new EjectAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public EjectStorageDomainBlobsSpectraS3Response ejectStorageDomainBlobsSpectraS3(final EjectStorageDomainBlobsSpectraS3Request request) throws IOException {
        return new EjectStorageDomainBlobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public EjectStorageDomainSpectraS3Response ejectStorageDomainSpectraS3(final EjectStorageDomainSpectraS3Request request) throws IOException {
        return new EjectStorageDomainSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public EjectTapeSpectraS3Response ejectTapeSpectraS3(final EjectTapeSpectraS3Request request) throws IOException {
        return new EjectTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ForceTapeEnvironmentRefreshSpectraS3Response forceTapeEnvironmentRefreshSpectraS3(final ForceTapeEnvironmentRefreshSpectraS3Request request) throws IOException {
        return new ForceTapeEnvironmentRefreshSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public FormatAllTapesSpectraS3Response formatAllTapesSpectraS3(final FormatAllTapesSpectraS3Request request) throws IOException {
        return new FormatAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public FormatTapeSpectraS3Response formatTapeSpectraS3(final FormatTapeSpectraS3Request request) throws IOException {
        return new FormatTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBlobsOnTapeSpectraS3Response getBlobsOnTapeSpectraS3(final GetBlobsOnTapeSpectraS3Request request) throws IOException {
        return new GetBlobsOnTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeDensityDirectiveSpectraS3Response getTapeDensityDirectiveSpectraS3(final GetTapeDensityDirectiveSpectraS3Request request) throws IOException {
        return new GetTapeDensityDirectiveSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeDensityDirectivesSpectraS3Response getTapeDensityDirectivesSpectraS3(final GetTapeDensityDirectivesSpectraS3Request request) throws IOException {
        return new GetTapeDensityDirectivesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeDriveSpectraS3Response getTapeDriveSpectraS3(final GetTapeDriveSpectraS3Request request) throws IOException {
        return new GetTapeDriveSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeDrivesSpectraS3Response getTapeDrivesSpectraS3(final GetTapeDrivesSpectraS3Request request) throws IOException {
        return new GetTapeDrivesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeFailuresSpectraS3Response getTapeFailuresSpectraS3(final GetTapeFailuresSpectraS3Request request) throws IOException {
        return new GetTapeFailuresSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeLibrariesSpectraS3Response getTapeLibrariesSpectraS3(final GetTapeLibrariesSpectraS3Request request) throws IOException {
        return new GetTapeLibrariesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeLibrarySpectraS3Response getTapeLibrarySpectraS3(final GetTapeLibrarySpectraS3Request request) throws IOException {
        return new GetTapeLibrarySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionFailuresSpectraS3Response getTapePartitionFailuresSpectraS3(final GetTapePartitionFailuresSpectraS3Request request) throws IOException {
        return new GetTapePartitionFailuresSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionSpectraS3Response getTapePartitionSpectraS3(final GetTapePartitionSpectraS3Request request) throws IOException {
        return new GetTapePartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionWithFullDetailsSpectraS3Response getTapePartitionWithFullDetailsSpectraS3(final GetTapePartitionWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetTapePartitionWithFullDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionsSpectraS3Response getTapePartitionsSpectraS3(final GetTapePartitionsSpectraS3Request request) throws IOException {
        return new GetTapePartitionsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionsWithFullDetailsSpectraS3Response getTapePartitionsWithFullDetailsSpectraS3(final GetTapePartitionsWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetTapePartitionsWithFullDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeSpectraS3Response getTapeSpectraS3(final GetTapeSpectraS3Request request) throws IOException {
        return new GetTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeWithFullDetailsSpectraS3Response getTapeWithFullDetailsSpectraS3(final GetTapeWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetTapeWithFullDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapesSpectraS3Response getTapesSpectraS3(final GetTapesSpectraS3Request request) throws IOException {
        return new GetTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapesWithFullDetailsSpectraS3Response getTapesWithFullDetailsSpectraS3(final GetTapesWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetTapesWithFullDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ImportAllTapesSpectraS3Response importAllTapesSpectraS3(final ImportAllTapesSpectraS3Request request) throws IOException {
        return new ImportAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ImportTapeSpectraS3Response importTapeSpectraS3(final ImportTapeSpectraS3Request request) throws IOException {
        return new ImportTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public InspectAllTapesSpectraS3Response inspectAllTapesSpectraS3(final InspectAllTapesSpectraS3Request request) throws IOException {
        return new InspectAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public InspectTapeSpectraS3Response inspectTapeSpectraS3(final InspectTapeSpectraS3Request request) throws IOException {
        return new InspectTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyAllTapePartitionsSpectraS3Response modifyAllTapePartitionsSpectraS3(final ModifyAllTapePartitionsSpectraS3Request request) throws IOException {
        return new ModifyAllTapePartitionsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyTapePartitionSpectraS3Response modifyTapePartitionSpectraS3(final ModifyTapePartitionSpectraS3Request request) throws IOException {
        return new ModifyTapePartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyTapeSpectraS3Response modifyTapeSpectraS3(final ModifyTapeSpectraS3Request request) throws IOException {
        return new ModifyTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public OnlineAllTapesSpectraS3Response onlineAllTapesSpectraS3(final OnlineAllTapesSpectraS3Request request) throws IOException {
        return new OnlineAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public OnlineTapeSpectraS3Response onlineTapeSpectraS3(final OnlineTapeSpectraS3Request request) throws IOException {
        return new OnlineTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyAllTapesSpectraS3Response verifyAllTapesSpectraS3(final VerifyAllTapesSpectraS3Request request) throws IOException {
        return new VerifyAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyTapeSpectraS3Response verifyTapeSpectraS3(final VerifyTapeSpectraS3Request request) throws IOException {
        return new VerifyTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetUserSpectraS3Response getUserSpectraS3(final GetUserSpectraS3Request request) throws IOException {
        return new GetUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetUsersSpectraS3Response getUsersSpectraS3(final GetUsersSpectraS3Request request) throws IOException {
        return new GetUsersSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyUserSpectraS3Response modifyUserSpectraS3(final ModifyUserSpectraS3Request request) throws IOException {
        return new ModifyUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public RegenerateUserSecretKeySpectraS3Response regenerateUserSecretKeySpectraS3(final RegenerateUserSecretKeySpectraS3Request request) throws IOException {
        return new RegenerateUserSecretKeySpectraS3Response(this.netClient.getResponse(request));
    }

    @Override
    public GetObjectResponse getObject(final GetObjectRequest request) throws IOException {
        return new GetObjectResponse(
            this.netClient.getResponse(request),
            request.getChannel(),
            this.netClient.getConnectionDetails().getBufferSize(),
            request.getObjectName()
        );
    }

    @Override
    public Ds3Client newForNode(final JobNode node) {
        final ConnectionDetails newConnectionDetails = ConnectionDetailsImpl.newForNode(node, this.getConnectionDetails());
        final NetworkClient newNetClient = new NetworkClientImpl(newConnectionDetails);
        return new Ds3ClientImpl(newNetClient);
    }

    @Override
    public void close() throws IOException {
        this.netClient.close();
    }
}