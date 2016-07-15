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
    public AbortMultiPartUploadResponse abortMultiPartUpload(AbortMultiPartUploadRequest request) throws IOException {
        return new AbortMultiPartUploadResponse(this.netClient.getResponse(request));
    }
    @Override
    public CompleteMultiPartUploadResponse completeMultiPartUpload(CompleteMultiPartUploadRequest request) throws IOException {
        return new CompleteMultiPartUploadResponse(this.netClient.getResponse(request));
    }
    @Override
    public PutBucketResponse putBucket(PutBucketRequest request) throws IOException {
        return new PutBucketResponse(this.netClient.getResponse(request));
    }
    @Override
    public PutMultiPartUploadPartResponse putMultiPartUploadPart(PutMultiPartUploadPartRequest request) throws IOException {
        return new PutMultiPartUploadPartResponse(this.netClient.getResponse(request));
    }
    @Override
    public PutObjectResponse putObject(PutObjectRequest request) throws IOException {
        return new PutObjectResponse(this.netClient.getResponse(request));
    }
    @Override
    public DeleteBucketResponse deleteBucket(DeleteBucketRequest request) throws IOException {
        return new DeleteBucketResponse(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectResponse deleteObject(DeleteObjectRequest request) throws IOException {
        return new DeleteObjectResponse(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectsResponse deleteObjects(DeleteObjectsRequest request) throws IOException {
        return new DeleteObjectsResponse(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketResponse getBucket(GetBucketRequest request) throws IOException {
        return new GetBucketResponse(this.netClient.getResponse(request));
    }
    @Override
    public GetServiceResponse getService(GetServiceRequest request) throws IOException {
        return new GetServiceResponse(this.netClient.getResponse(request));
    }
    @Override
    public HeadBucketResponse headBucket(HeadBucketRequest request) throws IOException {
        return new HeadBucketResponse(this.netClient.getResponse(request));
    }
    @Override
    public HeadObjectResponse headObject(HeadObjectRequest request) throws IOException {
        return new HeadObjectResponse(this.netClient.getResponse(request));
    }
    @Override
    public InitiateMultiPartUploadResponse initiateMultiPartUpload(InitiateMultiPartUploadRequest request) throws IOException {
        return new InitiateMultiPartUploadResponse(this.netClient.getResponse(request));
    }
    @Override
    public ListMultiPartUploadPartsResponse listMultiPartUploadParts(ListMultiPartUploadPartsRequest request) throws IOException {
        return new ListMultiPartUploadPartsResponse(this.netClient.getResponse(request));
    }
    @Override
    public ListMultiPartUploadsResponse listMultiPartUploads(ListMultiPartUploadsRequest request) throws IOException {
        return new ListMultiPartUploadsResponse(this.netClient.getResponse(request));
    }
    @Override
    public PutBucketAclForGroupSpectraS3Response putBucketAclForGroupSpectraS3(PutBucketAclForGroupSpectraS3Request request) throws IOException {
        return new PutBucketAclForGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutBucketAclForUserSpectraS3Response putBucketAclForUserSpectraS3(PutBucketAclForUserSpectraS3Request request) throws IOException {
        return new PutBucketAclForUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataPolicyAclForGroupSpectraS3Response putDataPolicyAclForGroupSpectraS3(PutDataPolicyAclForGroupSpectraS3Request request) throws IOException {
        return new PutDataPolicyAclForGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataPolicyAclForUserSpectraS3Response putDataPolicyAclForUserSpectraS3(PutDataPolicyAclForUserSpectraS3Request request) throws IOException {
        return new PutDataPolicyAclForUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutGlobalBucketAclForGroupSpectraS3Response putGlobalBucketAclForGroupSpectraS3(PutGlobalBucketAclForGroupSpectraS3Request request) throws IOException {
        return new PutGlobalBucketAclForGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutGlobalBucketAclForUserSpectraS3Response putGlobalBucketAclForUserSpectraS3(PutGlobalBucketAclForUserSpectraS3Request request) throws IOException {
        return new PutGlobalBucketAclForUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutGlobalDataPolicyAclForGroupSpectraS3Response putGlobalDataPolicyAclForGroupSpectraS3(PutGlobalDataPolicyAclForGroupSpectraS3Request request) throws IOException {
        return new PutGlobalDataPolicyAclForGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutGlobalDataPolicyAclForUserSpectraS3Response putGlobalDataPolicyAclForUserSpectraS3(PutGlobalDataPolicyAclForUserSpectraS3Request request) throws IOException {
        return new PutGlobalDataPolicyAclForUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteBucketAclSpectraS3Response deleteBucketAclSpectraS3(DeleteBucketAclSpectraS3Request request) throws IOException {
        return new DeleteBucketAclSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDataPolicyAclSpectraS3Response deleteDataPolicyAclSpectraS3(DeleteDataPolicyAclSpectraS3Request request) throws IOException {
        return new DeleteDataPolicyAclSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketAclSpectraS3Response getBucketAclSpectraS3(GetBucketAclSpectraS3Request request) throws IOException {
        return new GetBucketAclSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketAclsSpectraS3Response getBucketAclsSpectraS3(GetBucketAclsSpectraS3Request request) throws IOException {
        return new GetBucketAclsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPolicyAclSpectraS3Response getDataPolicyAclSpectraS3(GetDataPolicyAclSpectraS3Request request) throws IOException {
        return new GetDataPolicyAclSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3(GetDataPolicyAclsSpectraS3Request request) throws IOException {
        return new GetDataPolicyAclsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutBucketSpectraS3Response putBucketSpectraS3(PutBucketSpectraS3Request request) throws IOException {
        return new PutBucketSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteBucketSpectraS3Response deleteBucketSpectraS3(DeleteBucketSpectraS3Request request) throws IOException {
        return new DeleteBucketSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketSpectraS3Response getBucketSpectraS3(GetBucketSpectraS3Request request) throws IOException {
        return new GetBucketSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketsSpectraS3Response getBucketsSpectraS3(GetBucketsSpectraS3Request request) throws IOException {
        return new GetBucketsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyBucketSpectraS3Response modifyBucketSpectraS3(ModifyBucketSpectraS3Request request) throws IOException {
        return new ModifyBucketSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ForceFullCacheReclaimSpectraS3Response forceFullCacheReclaimSpectraS3(ForceFullCacheReclaimSpectraS3Request request) throws IOException {
        return new ForceFullCacheReclaimSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetCacheFilesystemSpectraS3Response getCacheFilesystemSpectraS3(GetCacheFilesystemSpectraS3Request request) throws IOException {
        return new GetCacheFilesystemSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetCacheFilesystemsSpectraS3Response getCacheFilesystemsSpectraS3(GetCacheFilesystemsSpectraS3Request request) throws IOException {
        return new GetCacheFilesystemsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetCacheStateSpectraS3Response getCacheStateSpectraS3(GetCacheStateSpectraS3Request request) throws IOException {
        return new GetCacheStateSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyCacheFilesystemSpectraS3Response modifyCacheFilesystemSpectraS3(ModifyCacheFilesystemSpectraS3Request request) throws IOException {
        return new ModifyCacheFilesystemSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketCapacitySummarySpectraS3Response getBucketCapacitySummarySpectraS3(GetBucketCapacitySummarySpectraS3Request request) throws IOException {
        return new GetBucketCapacitySummarySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainCapacitySummarySpectraS3Response getStorageDomainCapacitySummarySpectraS3(GetStorageDomainCapacitySummarySpectraS3Request request) throws IOException {
        return new GetStorageDomainCapacitySummarySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemCapacitySummarySpectraS3Response getSystemCapacitySummarySpectraS3(GetSystemCapacitySummarySpectraS3Request request) throws IOException {
        return new GetSystemCapacitySummarySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPathBackendSpectraS3Response getDataPathBackendSpectraS3(GetDataPathBackendSpectraS3Request request) throws IOException {
        return new GetDataPathBackendSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPlannerBlobStoreTasksSpectraS3Response getDataPlannerBlobStoreTasksSpectraS3(GetDataPlannerBlobStoreTasksSpectraS3Request request) throws IOException {
        return new GetDataPlannerBlobStoreTasksSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyDataPathBackendSpectraS3Response modifyDataPathBackendSpectraS3(ModifyDataPathBackendSpectraS3Request request) throws IOException {
        return new ModifyDataPathBackendSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataPersistenceRuleSpectraS3Response putDataPersistenceRuleSpectraS3(PutDataPersistenceRuleSpectraS3Request request) throws IOException {
        return new PutDataPersistenceRuleSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataPolicySpectraS3Response putDataPolicySpectraS3(PutDataPolicySpectraS3Request request) throws IOException {
        return new PutDataPolicySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataReplicationRuleSpectraS3Response putDataReplicationRuleSpectraS3(PutDataReplicationRuleSpectraS3Request request) throws IOException {
        return new PutDataReplicationRuleSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDataPersistenceRuleSpectraS3Response deleteDataPersistenceRuleSpectraS3(DeleteDataPersistenceRuleSpectraS3Request request) throws IOException {
        return new DeleteDataPersistenceRuleSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDataPolicySpectraS3Response deleteDataPolicySpectraS3(DeleteDataPolicySpectraS3Request request) throws IOException {
        return new DeleteDataPolicySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDataReplicationRuleSpectraS3Response deleteDataReplicationRuleSpectraS3(DeleteDataReplicationRuleSpectraS3Request request) throws IOException {
        return new DeleteDataReplicationRuleSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPersistenceRuleSpectraS3Response getDataPersistenceRuleSpectraS3(GetDataPersistenceRuleSpectraS3Request request) throws IOException {
        return new GetDataPersistenceRuleSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPersistenceRulesSpectraS3Response getDataPersistenceRulesSpectraS3(GetDataPersistenceRulesSpectraS3Request request) throws IOException {
        return new GetDataPersistenceRulesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPoliciesSpectraS3Response getDataPoliciesSpectraS3(GetDataPoliciesSpectraS3Request request) throws IOException {
        return new GetDataPoliciesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPolicySpectraS3Response getDataPolicySpectraS3(GetDataPolicySpectraS3Request request) throws IOException {
        return new GetDataPolicySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataReplicationRuleSpectraS3Response getDataReplicationRuleSpectraS3(GetDataReplicationRuleSpectraS3Request request) throws IOException {
        return new GetDataReplicationRuleSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataReplicationRulesSpectraS3Response getDataReplicationRulesSpectraS3(GetDataReplicationRulesSpectraS3Request request) throws IOException {
        return new GetDataReplicationRulesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyDataPersistenceRuleSpectraS3Response modifyDataPersistenceRuleSpectraS3(ModifyDataPersistenceRuleSpectraS3Request request) throws IOException {
        return new ModifyDataPersistenceRuleSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyDataPolicySpectraS3Response modifyDataPolicySpectraS3(ModifyDataPolicySpectraS3Request request) throws IOException {
        return new ModifyDataPolicySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyDataReplicationRuleSpectraS3Response modifyDataReplicationRuleSpectraS3(ModifyDataReplicationRuleSpectraS3Request request) throws IOException {
        return new ModifyDataReplicationRuleSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ClearSuspectBlobPoolsSpectraS3Response clearSuspectBlobPoolsSpectraS3(ClearSuspectBlobPoolsSpectraS3Request request) throws IOException {
        return new ClearSuspectBlobPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ClearSuspectBlobTapesSpectraS3Response clearSuspectBlobTapesSpectraS3(ClearSuspectBlobTapesSpectraS3Request request) throws IOException {
        return new ClearSuspectBlobTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ClearSuspectBlobTargetsSpectraS3Response clearSuspectBlobTargetsSpectraS3(ClearSuspectBlobTargetsSpectraS3Request request) throws IOException {
        return new ClearSuspectBlobTargetsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDegradedBlobsSpectraS3Response getDegradedBlobsSpectraS3(GetDegradedBlobsSpectraS3Request request) throws IOException {
        return new GetDegradedBlobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDegradedBucketsSpectraS3Response getDegradedBucketsSpectraS3(GetDegradedBucketsSpectraS3Request request) throws IOException {
        return new GetDegradedBucketsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDegradedDataPersistenceRulesSpectraS3Response getDegradedDataPersistenceRulesSpectraS3(GetDegradedDataPersistenceRulesSpectraS3Request request) throws IOException {
        return new GetDegradedDataPersistenceRulesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDegradedDataReplicationRulesSpectraS3Response getDegradedDataReplicationRulesSpectraS3(GetDegradedDataReplicationRulesSpectraS3Request request) throws IOException {
        return new GetDegradedDataReplicationRulesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSuspectBlobPoolsSpectraS3Response getSuspectBlobPoolsSpectraS3(GetSuspectBlobPoolsSpectraS3Request request) throws IOException {
        return new GetSuspectBlobPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSuspectBlobTapesSpectraS3Response getSuspectBlobTapesSpectraS3(GetSuspectBlobTapesSpectraS3Request request) throws IOException {
        return new GetSuspectBlobTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSuspectBlobTargetsSpectraS3Response getSuspectBlobTargetsSpectraS3(GetSuspectBlobTargetsSpectraS3Request request) throws IOException {
        return new GetSuspectBlobTargetsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSuspectBucketsSpectraS3Response getSuspectBucketsSpectraS3(GetSuspectBucketsSpectraS3Request request) throws IOException {
        return new GetSuspectBucketsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSuspectObjectsSpectraS3Response getSuspectObjectsSpectraS3(GetSuspectObjectsSpectraS3Request request) throws IOException {
        return new GetSuspectObjectsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSuspectObjectsWithFullDetailsSpectraS3Response getSuspectObjectsWithFullDetailsSpectraS3(GetSuspectObjectsWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetSuspectObjectsWithFullDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public MarkSuspectBlobPoolsAsDegradedSpectraS3Response markSuspectBlobPoolsAsDegradedSpectraS3(MarkSuspectBlobPoolsAsDegradedSpectraS3Request request) throws IOException {
        return new MarkSuspectBlobPoolsAsDegradedSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public MarkSuspectBlobTapesAsDegradedSpectraS3Response markSuspectBlobTapesAsDegradedSpectraS3(MarkSuspectBlobTapesAsDegradedSpectraS3Request request) throws IOException {
        return new MarkSuspectBlobTapesAsDegradedSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public MarkSuspectBlobTargetsAsDegradedSpectraS3Response markSuspectBlobTargetsAsDegradedSpectraS3(MarkSuspectBlobTargetsAsDegradedSpectraS3Request request) throws IOException {
        return new MarkSuspectBlobTargetsAsDegradedSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutGroupGroupMemberSpectraS3Response putGroupGroupMemberSpectraS3(PutGroupGroupMemberSpectraS3Request request) throws IOException {
        return new PutGroupGroupMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutGroupSpectraS3Response putGroupSpectraS3(PutGroupSpectraS3Request request) throws IOException {
        return new PutGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutUserGroupMemberSpectraS3Response putUserGroupMemberSpectraS3(PutUserGroupMemberSpectraS3Request request) throws IOException {
        return new PutUserGroupMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteGroupMemberSpectraS3Response deleteGroupMemberSpectraS3(DeleteGroupMemberSpectraS3Request request) throws IOException {
        return new DeleteGroupMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteGroupSpectraS3Response deleteGroupSpectraS3(DeleteGroupSpectraS3Request request) throws IOException {
        return new DeleteGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetGroupMemberSpectraS3Response getGroupMemberSpectraS3(GetGroupMemberSpectraS3Request request) throws IOException {
        return new GetGroupMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetGroupMembersSpectraS3Response getGroupMembersSpectraS3(GetGroupMembersSpectraS3Request request) throws IOException {
        return new GetGroupMembersSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetGroupSpectraS3Response getGroupSpectraS3(GetGroupSpectraS3Request request) throws IOException {
        return new GetGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetGroupsSpectraS3Response getGroupsSpectraS3(GetGroupsSpectraS3Request request) throws IOException {
        return new GetGroupsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyGroupSpectraS3Response modifyGroupSpectraS3(ModifyGroupSpectraS3Request request) throws IOException {
        return new ModifyGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyUserIsMemberOfGroupSpectraS3Response verifyUserIsMemberOfGroupSpectraS3(VerifyUserIsMemberOfGroupSpectraS3Request request) throws IOException {
        return new VerifyUserIsMemberOfGroupSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public AllocateJobChunkSpectraS3Response allocateJobChunkSpectraS3(AllocateJobChunkSpectraS3Request request) throws IOException {
        return new AllocateJobChunkSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelActiveJobSpectraS3Response cancelActiveJobSpectraS3(CancelActiveJobSpectraS3Request request) throws IOException {
        return new CancelActiveJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelAllActiveJobsSpectraS3Response cancelAllActiveJobsSpectraS3(CancelAllActiveJobsSpectraS3Request request) throws IOException {
        return new CancelAllActiveJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelAllJobsSpectraS3Response cancelAllJobsSpectraS3(CancelAllJobsSpectraS3Request request) throws IOException {
        return new CancelAllJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelJobSpectraS3Response cancelJobSpectraS3(CancelJobSpectraS3Request request) throws IOException {
        return new CancelJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ClearAllCanceledJobsSpectraS3Response clearAllCanceledJobsSpectraS3(ClearAllCanceledJobsSpectraS3Request request) throws IOException {
        return new ClearAllCanceledJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ClearAllCompletedJobsSpectraS3Response clearAllCompletedJobsSpectraS3(ClearAllCompletedJobsSpectraS3Request request) throws IOException {
        return new ClearAllCompletedJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBulkJobSpectraS3Response getBulkJobSpectraS3(GetBulkJobSpectraS3Request request) throws IOException {
        return new GetBulkJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutBulkJobSpectraS3Response putBulkJobSpectraS3(PutBulkJobSpectraS3Request request) throws IOException {
        return new PutBulkJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyBulkJobSpectraS3Response verifyBulkJobSpectraS3(VerifyBulkJobSpectraS3Request request) throws IOException {
        return new VerifyBulkJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetActiveJobSpectraS3Response getActiveJobSpectraS3(GetActiveJobSpectraS3Request request) throws IOException {
        return new GetActiveJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetActiveJobsSpectraS3Response getActiveJobsSpectraS3(GetActiveJobsSpectraS3Request request) throws IOException {
        return new GetActiveJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetCanceledJobSpectraS3Response getCanceledJobSpectraS3(GetCanceledJobSpectraS3Request request) throws IOException {
        return new GetCanceledJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetCanceledJobsSpectraS3Response getCanceledJobsSpectraS3(GetCanceledJobsSpectraS3Request request) throws IOException {
        return new GetCanceledJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetCompletedJobSpectraS3Response getCompletedJobSpectraS3(GetCompletedJobSpectraS3Request request) throws IOException {
        return new GetCompletedJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetCompletedJobsSpectraS3Response getCompletedJobsSpectraS3(GetCompletedJobsSpectraS3Request request) throws IOException {
        return new GetCompletedJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobChunkDaoSpectraS3Response getJobChunkDaoSpectraS3(GetJobChunkDaoSpectraS3Request request) throws IOException {
        return new GetJobChunkDaoSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobChunkSpectraS3Response getJobChunkSpectraS3(GetJobChunkSpectraS3Request request) throws IOException {
        return new GetJobChunkSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobChunksReadyForClientProcessingSpectraS3Response getJobChunksReadyForClientProcessingSpectraS3(GetJobChunksReadyForClientProcessingSpectraS3Request request) throws IOException {
        return new GetJobChunksReadyForClientProcessingSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobSpectraS3Response getJobSpectraS3(GetJobSpectraS3Request request) throws IOException {
        return new GetJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobToReplicateSpectraS3Response getJobToReplicateSpectraS3(GetJobToReplicateSpectraS3Request request) throws IOException {
        return new GetJobToReplicateSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobsSpectraS3Response getJobsSpectraS3(GetJobsSpectraS3Request request) throws IOException {
        return new GetJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyActiveJobSpectraS3Response modifyActiveJobSpectraS3(ModifyActiveJobSpectraS3Request request) throws IOException {
        return new ModifyActiveJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyJobSpectraS3Response modifyJobSpectraS3(ModifyJobSpectraS3Request request) throws IOException {
        return new ModifyJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ReplicatePutJobSpectraS3Response replicatePutJobSpectraS3(ReplicatePutJobSpectraS3Request request) throws IOException {
        return new ReplicatePutJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public TruncateActiveJobSpectraS3Response truncateActiveJobSpectraS3(TruncateActiveJobSpectraS3Request request) throws IOException {
        return new TruncateActiveJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public TruncateAllActiveJobsSpectraS3Response truncateAllActiveJobsSpectraS3(TruncateAllActiveJobsSpectraS3Request request) throws IOException {
        return new TruncateAllActiveJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public TruncateAllJobsSpectraS3Response truncateAllJobsSpectraS3(TruncateAllJobsSpectraS3Request request) throws IOException {
        return new TruncateAllJobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public TruncateJobSpectraS3Response truncateJobSpectraS3(TruncateJobSpectraS3Request request) throws IOException {
        return new TruncateJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifySafeToCreatePutJobSpectraS3Response verifySafeToCreatePutJobSpectraS3(VerifySafeToCreatePutJobSpectraS3Request request) throws IOException {
        return new VerifySafeToCreatePutJobSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetNodeSpectraS3Response getNodeSpectraS3(GetNodeSpectraS3Request request) throws IOException {
        return new GetNodeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetNodesSpectraS3Response getNodesSpectraS3(GetNodesSpectraS3Request request) throws IOException {
        return new GetNodesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyNodeSpectraS3Response modifyNodeSpectraS3(ModifyNodeSpectraS3Request request) throws IOException {
        return new ModifyNodeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutDs3TargetFailureNotificationRegistrationSpectraS3Response putDs3TargetFailureNotificationRegistrationSpectraS3(PutDs3TargetFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutDs3TargetFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutJobCompletedNotificationRegistrationSpectraS3Response putJobCompletedNotificationRegistrationSpectraS3(PutJobCompletedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutJobCompletedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutJobCreatedNotificationRegistrationSpectraS3Response putJobCreatedNotificationRegistrationSpectraS3(PutJobCreatedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutJobCreatedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutJobCreationFailedNotificationRegistrationSpectraS3Response putJobCreationFailedNotificationRegistrationSpectraS3(PutJobCreationFailedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutJobCreationFailedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutObjectCachedNotificationRegistrationSpectraS3Response putObjectCachedNotificationRegistrationSpectraS3(PutObjectCachedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutObjectCachedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutObjectLostNotificationRegistrationSpectraS3Response putObjectLostNotificationRegistrationSpectraS3(PutObjectLostNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutObjectLostNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutObjectPersistedNotificationRegistrationSpectraS3Response putObjectPersistedNotificationRegistrationSpectraS3(PutObjectPersistedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutObjectPersistedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutPoolFailureNotificationRegistrationSpectraS3Response putPoolFailureNotificationRegistrationSpectraS3(PutPoolFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutPoolFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutStorageDomainFailureNotificationRegistrationSpectraS3Response putStorageDomainFailureNotificationRegistrationSpectraS3(PutStorageDomainFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutStorageDomainFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutSystemFailureNotificationRegistrationSpectraS3Response putSystemFailureNotificationRegistrationSpectraS3(PutSystemFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutSystemFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutTapeFailureNotificationRegistrationSpectraS3Response putTapeFailureNotificationRegistrationSpectraS3(PutTapeFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutTapeFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutTapePartitionFailureNotificationRegistrationSpectraS3Response putTapePartitionFailureNotificationRegistrationSpectraS3(PutTapePartitionFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutTapePartitionFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDs3TargetFailureNotificationRegistrationSpectraS3Response deleteDs3TargetFailureNotificationRegistrationSpectraS3(DeleteDs3TargetFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteDs3TargetFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteJobCompletedNotificationRegistrationSpectraS3Response deleteJobCompletedNotificationRegistrationSpectraS3(DeleteJobCompletedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteJobCompletedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteJobCreatedNotificationRegistrationSpectraS3Response deleteJobCreatedNotificationRegistrationSpectraS3(DeleteJobCreatedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteJobCreatedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteJobCreationFailedNotificationRegistrationSpectraS3Response deleteJobCreationFailedNotificationRegistrationSpectraS3(DeleteJobCreationFailedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteJobCreationFailedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectCachedNotificationRegistrationSpectraS3Response deleteObjectCachedNotificationRegistrationSpectraS3(DeleteObjectCachedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteObjectCachedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectLostNotificationRegistrationSpectraS3Response deleteObjectLostNotificationRegistrationSpectraS3(DeleteObjectLostNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteObjectLostNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectPersistedNotificationRegistrationSpectraS3Response deleteObjectPersistedNotificationRegistrationSpectraS3(DeleteObjectPersistedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteObjectPersistedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePoolFailureNotificationRegistrationSpectraS3Response deletePoolFailureNotificationRegistrationSpectraS3(DeletePoolFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeletePoolFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteStorageDomainFailureNotificationRegistrationSpectraS3Response deleteStorageDomainFailureNotificationRegistrationSpectraS3(DeleteStorageDomainFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteStorageDomainFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteSystemFailureNotificationRegistrationSpectraS3Response deleteSystemFailureNotificationRegistrationSpectraS3(DeleteSystemFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteSystemFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapeFailureNotificationRegistrationSpectraS3Response deleteTapeFailureNotificationRegistrationSpectraS3(DeleteTapeFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteTapeFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapePartitionFailureNotificationRegistrationSpectraS3Response deleteTapePartitionFailureNotificationRegistrationSpectraS3(DeleteTapePartitionFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteTapePartitionFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetFailureNotificationRegistrationSpectraS3Response getDs3TargetFailureNotificationRegistrationSpectraS3(GetDs3TargetFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetDs3TargetFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetFailureNotificationRegistrationsSpectraS3Response getDs3TargetFailureNotificationRegistrationsSpectraS3(GetDs3TargetFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetDs3TargetFailureNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCompletedNotificationRegistrationSpectraS3Response getJobCompletedNotificationRegistrationSpectraS3(GetJobCompletedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetJobCompletedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCompletedNotificationRegistrationsSpectraS3Response getJobCompletedNotificationRegistrationsSpectraS3(GetJobCompletedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetJobCompletedNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCreatedNotificationRegistrationSpectraS3Response getJobCreatedNotificationRegistrationSpectraS3(GetJobCreatedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetJobCreatedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCreatedNotificationRegistrationsSpectraS3Response getJobCreatedNotificationRegistrationsSpectraS3(GetJobCreatedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetJobCreatedNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCreationFailedNotificationRegistrationSpectraS3Response getJobCreationFailedNotificationRegistrationSpectraS3(GetJobCreationFailedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetJobCreationFailedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCreationFailedNotificationRegistrationsSpectraS3Response getJobCreationFailedNotificationRegistrationsSpectraS3(GetJobCreationFailedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetJobCreationFailedNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectCachedNotificationRegistrationSpectraS3Response getObjectCachedNotificationRegistrationSpectraS3(GetObjectCachedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetObjectCachedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectCachedNotificationRegistrationsSpectraS3Response getObjectCachedNotificationRegistrationsSpectraS3(GetObjectCachedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetObjectCachedNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectLostNotificationRegistrationSpectraS3Response getObjectLostNotificationRegistrationSpectraS3(GetObjectLostNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetObjectLostNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectLostNotificationRegistrationsSpectraS3Response getObjectLostNotificationRegistrationsSpectraS3(GetObjectLostNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetObjectLostNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectPersistedNotificationRegistrationSpectraS3Response getObjectPersistedNotificationRegistrationSpectraS3(GetObjectPersistedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetObjectPersistedNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectPersistedNotificationRegistrationsSpectraS3Response getObjectPersistedNotificationRegistrationsSpectraS3(GetObjectPersistedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetObjectPersistedNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolFailureNotificationRegistrationSpectraS3Response getPoolFailureNotificationRegistrationSpectraS3(GetPoolFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetPoolFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolFailureNotificationRegistrationsSpectraS3Response getPoolFailureNotificationRegistrationsSpectraS3(GetPoolFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetPoolFailureNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainFailureNotificationRegistrationSpectraS3Response getStorageDomainFailureNotificationRegistrationSpectraS3(GetStorageDomainFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetStorageDomainFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainFailureNotificationRegistrationsSpectraS3Response getStorageDomainFailureNotificationRegistrationsSpectraS3(GetStorageDomainFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetStorageDomainFailureNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemFailureNotificationRegistrationSpectraS3Response getSystemFailureNotificationRegistrationSpectraS3(GetSystemFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetSystemFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemFailureNotificationRegistrationsSpectraS3Response getSystemFailureNotificationRegistrationsSpectraS3(GetSystemFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetSystemFailureNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeFailureNotificationRegistrationSpectraS3Response getTapeFailureNotificationRegistrationSpectraS3(GetTapeFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetTapeFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeFailureNotificationRegistrationsSpectraS3Response getTapeFailureNotificationRegistrationsSpectraS3(GetTapeFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetTapeFailureNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionFailureNotificationRegistrationSpectraS3Response getTapePartitionFailureNotificationRegistrationSpectraS3(GetTapePartitionFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetTapePartitionFailureNotificationRegistrationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionFailureNotificationRegistrationsSpectraS3Response getTapePartitionFailureNotificationRegistrationsSpectraS3(GetTapePartitionFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetTapePartitionFailureNotificationRegistrationsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteFolderRecursivelySpectraS3Response deleteFolderRecursivelySpectraS3(DeleteFolderRecursivelySpectraS3Request request) throws IOException {
        return new DeleteFolderRecursivelySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBlobPersistenceSpectraS3Response getBlobPersistenceSpectraS3(GetBlobPersistenceSpectraS3Request request) throws IOException {
        return new GetBlobPersistenceSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectDetailsSpectraS3Response getObjectDetailsSpectraS3(GetObjectDetailsSpectraS3Request request) throws IOException {
        return new GetObjectDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectsDetailsSpectraS3Response getObjectsDetailsSpectraS3(GetObjectsDetailsSpectraS3Request request) throws IOException {
        return new GetObjectsDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectsWithFullDetailsSpectraS3Response getObjectsWithFullDetailsSpectraS3(GetObjectsWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetObjectsWithFullDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPhysicalPlacementForObjectsSpectraS3Response getPhysicalPlacementForObjectsSpectraS3(GetPhysicalPlacementForObjectsSpectraS3Request request) throws IOException {
        return new GetPhysicalPlacementForObjectsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response getPhysicalPlacementForObjectsWithFullDetailsSpectraS3(GetPhysicalPlacementForObjectsWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyPhysicalPlacementForObjectsSpectraS3Response verifyPhysicalPlacementForObjectsSpectraS3(VerifyPhysicalPlacementForObjectsSpectraS3Request request) throws IOException {
        return new VerifyPhysicalPlacementForObjectsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response verifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3(VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Request request) throws IOException {
        return new VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelImportOnAllPoolsSpectraS3Response cancelImportOnAllPoolsSpectraS3(CancelImportOnAllPoolsSpectraS3Request request) throws IOException {
        return new CancelImportOnAllPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelImportPoolSpectraS3Response cancelImportPoolSpectraS3(CancelImportPoolSpectraS3Request request) throws IOException {
        return new CancelImportPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CompactAllPoolsSpectraS3Response compactAllPoolsSpectraS3(CompactAllPoolsSpectraS3Request request) throws IOException {
        return new CompactAllPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CompactPoolSpectraS3Response compactPoolSpectraS3(CompactPoolSpectraS3Request request) throws IOException {
        return new CompactPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutPoolPartitionSpectraS3Response putPoolPartitionSpectraS3(PutPoolPartitionSpectraS3Request request) throws IOException {
        return new PutPoolPartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeallocatePoolSpectraS3Response deallocatePoolSpectraS3(DeallocatePoolSpectraS3Request request) throws IOException {
        return new DeallocatePoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePermanentlyLostPoolSpectraS3Response deletePermanentlyLostPoolSpectraS3(DeletePermanentlyLostPoolSpectraS3Request request) throws IOException {
        return new DeletePermanentlyLostPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePoolFailureSpectraS3Response deletePoolFailureSpectraS3(DeletePoolFailureSpectraS3Request request) throws IOException {
        return new DeletePoolFailureSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePoolPartitionSpectraS3Response deletePoolPartitionSpectraS3(DeletePoolPartitionSpectraS3Request request) throws IOException {
        return new DeletePoolPartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ForcePoolEnvironmentRefreshSpectraS3Response forcePoolEnvironmentRefreshSpectraS3(ForcePoolEnvironmentRefreshSpectraS3Request request) throws IOException {
        return new ForcePoolEnvironmentRefreshSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public FormatAllForeignPoolsSpectraS3Response formatAllForeignPoolsSpectraS3(FormatAllForeignPoolsSpectraS3Request request) throws IOException {
        return new FormatAllForeignPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public FormatForeignPoolSpectraS3Response formatForeignPoolSpectraS3(FormatForeignPoolSpectraS3Request request) throws IOException {
        return new FormatForeignPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBlobsOnPoolSpectraS3Response getBlobsOnPoolSpectraS3(GetBlobsOnPoolSpectraS3Request request) throws IOException {
        return new GetBlobsOnPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolFailuresSpectraS3Response getPoolFailuresSpectraS3(GetPoolFailuresSpectraS3Request request) throws IOException {
        return new GetPoolFailuresSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolPartitionSpectraS3Response getPoolPartitionSpectraS3(GetPoolPartitionSpectraS3Request request) throws IOException {
        return new GetPoolPartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolPartitionsSpectraS3Response getPoolPartitionsSpectraS3(GetPoolPartitionsSpectraS3Request request) throws IOException {
        return new GetPoolPartitionsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolSpectraS3Response getPoolSpectraS3(GetPoolSpectraS3Request request) throws IOException {
        return new GetPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolsSpectraS3Response getPoolsSpectraS3(GetPoolsSpectraS3Request request) throws IOException {
        return new GetPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ImportAllPoolsSpectraS3Response importAllPoolsSpectraS3(ImportAllPoolsSpectraS3Request request) throws IOException {
        return new ImportAllPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ImportPoolSpectraS3Response importPoolSpectraS3(ImportPoolSpectraS3Request request) throws IOException {
        return new ImportPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyAllPoolsSpectraS3Response modifyAllPoolsSpectraS3(ModifyAllPoolsSpectraS3Request request) throws IOException {
        return new ModifyAllPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyPoolPartitionSpectraS3Response modifyPoolPartitionSpectraS3(ModifyPoolPartitionSpectraS3Request request) throws IOException {
        return new ModifyPoolPartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyPoolSpectraS3Response modifyPoolSpectraS3(ModifyPoolSpectraS3Request request) throws IOException {
        return new ModifyPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyAllPoolsSpectraS3Response verifyAllPoolsSpectraS3(VerifyAllPoolsSpectraS3Request request) throws IOException {
        return new VerifyAllPoolsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyPoolSpectraS3Response verifyPoolSpectraS3(VerifyPoolSpectraS3Request request) throws IOException {
        return new VerifyPoolSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ConvertStorageDomainToDs3TargetSpectraS3Response convertStorageDomainToDs3TargetSpectraS3(ConvertStorageDomainToDs3TargetSpectraS3Request request) throws IOException {
        return new ConvertStorageDomainToDs3TargetSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutPoolStorageDomainMemberSpectraS3Response putPoolStorageDomainMemberSpectraS3(PutPoolStorageDomainMemberSpectraS3Request request) throws IOException {
        return new PutPoolStorageDomainMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutStorageDomainSpectraS3Response putStorageDomainSpectraS3(PutStorageDomainSpectraS3Request request) throws IOException {
        return new PutStorageDomainSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutTapeStorageDomainMemberSpectraS3Response putTapeStorageDomainMemberSpectraS3(PutTapeStorageDomainMemberSpectraS3Request request) throws IOException {
        return new PutTapeStorageDomainMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteStorageDomainFailureSpectraS3Response deleteStorageDomainFailureSpectraS3(DeleteStorageDomainFailureSpectraS3Request request) throws IOException {
        return new DeleteStorageDomainFailureSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteStorageDomainMemberSpectraS3Response deleteStorageDomainMemberSpectraS3(DeleteStorageDomainMemberSpectraS3Request request) throws IOException {
        return new DeleteStorageDomainMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteStorageDomainSpectraS3Response deleteStorageDomainSpectraS3(DeleteStorageDomainSpectraS3Request request) throws IOException {
        return new DeleteStorageDomainSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainFailuresSpectraS3Response getStorageDomainFailuresSpectraS3(GetStorageDomainFailuresSpectraS3Request request) throws IOException {
        return new GetStorageDomainFailuresSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainMemberSpectraS3Response getStorageDomainMemberSpectraS3(GetStorageDomainMemberSpectraS3Request request) throws IOException {
        return new GetStorageDomainMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainMembersSpectraS3Response getStorageDomainMembersSpectraS3(GetStorageDomainMembersSpectraS3Request request) throws IOException {
        return new GetStorageDomainMembersSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainSpectraS3Response getStorageDomainSpectraS3(GetStorageDomainSpectraS3Request request) throws IOException {
        return new GetStorageDomainSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainsSpectraS3Response getStorageDomainsSpectraS3(GetStorageDomainsSpectraS3Request request) throws IOException {
        return new GetStorageDomainsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyStorageDomainMemberSpectraS3Response modifyStorageDomainMemberSpectraS3(ModifyStorageDomainMemberSpectraS3Request request) throws IOException {
        return new ModifyStorageDomainMemberSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyStorageDomainSpectraS3Response modifyStorageDomainSpectraS3(ModifyStorageDomainSpectraS3Request request) throws IOException {
        return new ModifyStorageDomainSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemFailuresSpectraS3Response getSystemFailuresSpectraS3(GetSystemFailuresSpectraS3Request request) throws IOException {
        return new GetSystemFailuresSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemInformationSpectraS3Response getSystemInformationSpectraS3(GetSystemInformationSpectraS3Request request) throws IOException {
        return new GetSystemInformationSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ResetInstanceIdentifierSpectraS3Response resetInstanceIdentifierSpectraS3(ResetInstanceIdentifierSpectraS3Request request) throws IOException {
        return new ResetInstanceIdentifierSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifySystemHealthSpectraS3Response verifySystemHealthSpectraS3(VerifySystemHealthSpectraS3Request request) throws IOException {
        return new VerifySystemHealthSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelEjectOnAllTapesSpectraS3Response cancelEjectOnAllTapesSpectraS3(CancelEjectOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelEjectOnAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelEjectTapeSpectraS3Response cancelEjectTapeSpectraS3(CancelEjectTapeSpectraS3Request request) throws IOException {
        return new CancelEjectTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelFormatOnAllTapesSpectraS3Response cancelFormatOnAllTapesSpectraS3(CancelFormatOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelFormatOnAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelFormatTapeSpectraS3Response cancelFormatTapeSpectraS3(CancelFormatTapeSpectraS3Request request) throws IOException {
        return new CancelFormatTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelImportOnAllTapesSpectraS3Response cancelImportOnAllTapesSpectraS3(CancelImportOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelImportOnAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelImportTapeSpectraS3Response cancelImportTapeSpectraS3(CancelImportTapeSpectraS3Request request) throws IOException {
        return new CancelImportTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelOnlineOnAllTapesSpectraS3Response cancelOnlineOnAllTapesSpectraS3(CancelOnlineOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelOnlineOnAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelOnlineTapeSpectraS3Response cancelOnlineTapeSpectraS3(CancelOnlineTapeSpectraS3Request request) throws IOException {
        return new CancelOnlineTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelVerifyOnAllTapesSpectraS3Response cancelVerifyOnAllTapesSpectraS3(CancelVerifyOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelVerifyOnAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CancelVerifyTapeSpectraS3Response cancelVerifyTapeSpectraS3(CancelVerifyTapeSpectraS3Request request) throws IOException {
        return new CancelVerifyTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public CleanTapeDriveSpectraS3Response cleanTapeDriveSpectraS3(CleanTapeDriveSpectraS3Request request) throws IOException {
        return new CleanTapeDriveSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutTapeDensityDirectiveSpectraS3Response putTapeDensityDirectiveSpectraS3(PutTapeDensityDirectiveSpectraS3Request request) throws IOException {
        return new PutTapeDensityDirectiveSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePermanentlyLostTapeSpectraS3Response deletePermanentlyLostTapeSpectraS3(DeletePermanentlyLostTapeSpectraS3Request request) throws IOException {
        return new DeletePermanentlyLostTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapeDensityDirectiveSpectraS3Response deleteTapeDensityDirectiveSpectraS3(DeleteTapeDensityDirectiveSpectraS3Request request) throws IOException {
        return new DeleteTapeDensityDirectiveSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapeDriveSpectraS3Response deleteTapeDriveSpectraS3(DeleteTapeDriveSpectraS3Request request) throws IOException {
        return new DeleteTapeDriveSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapeFailureSpectraS3Response deleteTapeFailureSpectraS3(DeleteTapeFailureSpectraS3Request request) throws IOException {
        return new DeleteTapeFailureSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapePartitionFailureSpectraS3Response deleteTapePartitionFailureSpectraS3(DeleteTapePartitionFailureSpectraS3Request request) throws IOException {
        return new DeleteTapePartitionFailureSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapePartitionSpectraS3Response deleteTapePartitionSpectraS3(DeleteTapePartitionSpectraS3Request request) throws IOException {
        return new DeleteTapePartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public EjectAllTapesSpectraS3Response ejectAllTapesSpectraS3(EjectAllTapesSpectraS3Request request) throws IOException {
        return new EjectAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public EjectStorageDomainBlobsSpectraS3Response ejectStorageDomainBlobsSpectraS3(EjectStorageDomainBlobsSpectraS3Request request) throws IOException {
        return new EjectStorageDomainBlobsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public EjectStorageDomainSpectraS3Response ejectStorageDomainSpectraS3(EjectStorageDomainSpectraS3Request request) throws IOException {
        return new EjectStorageDomainSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public EjectTapeSpectraS3Response ejectTapeSpectraS3(EjectTapeSpectraS3Request request) throws IOException {
        return new EjectTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ForceTapeEnvironmentRefreshSpectraS3Response forceTapeEnvironmentRefreshSpectraS3(ForceTapeEnvironmentRefreshSpectraS3Request request) throws IOException {
        return new ForceTapeEnvironmentRefreshSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public FormatAllTapesSpectraS3Response formatAllTapesSpectraS3(FormatAllTapesSpectraS3Request request) throws IOException {
        return new FormatAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public FormatTapeSpectraS3Response formatTapeSpectraS3(FormatTapeSpectraS3Request request) throws IOException {
        return new FormatTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetBlobsOnTapeSpectraS3Response getBlobsOnTapeSpectraS3(GetBlobsOnTapeSpectraS3Request request) throws IOException {
        return new GetBlobsOnTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeDensityDirectiveSpectraS3Response getTapeDensityDirectiveSpectraS3(GetTapeDensityDirectiveSpectraS3Request request) throws IOException {
        return new GetTapeDensityDirectiveSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeDensityDirectivesSpectraS3Response getTapeDensityDirectivesSpectraS3(GetTapeDensityDirectivesSpectraS3Request request) throws IOException {
        return new GetTapeDensityDirectivesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeDriveSpectraS3Response getTapeDriveSpectraS3(GetTapeDriveSpectraS3Request request) throws IOException {
        return new GetTapeDriveSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeDrivesSpectraS3Response getTapeDrivesSpectraS3(GetTapeDrivesSpectraS3Request request) throws IOException {
        return new GetTapeDrivesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeFailuresSpectraS3Response getTapeFailuresSpectraS3(GetTapeFailuresSpectraS3Request request) throws IOException {
        return new GetTapeFailuresSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeLibrariesSpectraS3Response getTapeLibrariesSpectraS3(GetTapeLibrariesSpectraS3Request request) throws IOException {
        return new GetTapeLibrariesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeLibrarySpectraS3Response getTapeLibrarySpectraS3(GetTapeLibrarySpectraS3Request request) throws IOException {
        return new GetTapeLibrarySpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionFailuresSpectraS3Response getTapePartitionFailuresSpectraS3(GetTapePartitionFailuresSpectraS3Request request) throws IOException {
        return new GetTapePartitionFailuresSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionSpectraS3Response getTapePartitionSpectraS3(GetTapePartitionSpectraS3Request request) throws IOException {
        return new GetTapePartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionWithFullDetailsSpectraS3Response getTapePartitionWithFullDetailsSpectraS3(GetTapePartitionWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetTapePartitionWithFullDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionsSpectraS3Response getTapePartitionsSpectraS3(GetTapePartitionsSpectraS3Request request) throws IOException {
        return new GetTapePartitionsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionsWithFullDetailsSpectraS3Response getTapePartitionsWithFullDetailsSpectraS3(GetTapePartitionsWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetTapePartitionsWithFullDetailsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeSpectraS3Response getTapeSpectraS3(GetTapeSpectraS3Request request) throws IOException {
        return new GetTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapesSpectraS3Response getTapesSpectraS3(GetTapesSpectraS3Request request) throws IOException {
        return new GetTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ImportAllTapesSpectraS3Response importAllTapesSpectraS3(ImportAllTapesSpectraS3Request request) throws IOException {
        return new ImportAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ImportTapeSpectraS3Response importTapeSpectraS3(ImportTapeSpectraS3Request request) throws IOException {
        return new ImportTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public InspectAllTapesSpectraS3Response inspectAllTapesSpectraS3(InspectAllTapesSpectraS3Request request) throws IOException {
        return new InspectAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public InspectTapeSpectraS3Response inspectTapeSpectraS3(InspectTapeSpectraS3Request request) throws IOException {
        return new InspectTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyAllTapePartitionsSpectraS3Response modifyAllTapePartitionsSpectraS3(ModifyAllTapePartitionsSpectraS3Request request) throws IOException {
        return new ModifyAllTapePartitionsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyTapePartitionSpectraS3Response modifyTapePartitionSpectraS3(ModifyTapePartitionSpectraS3Request request) throws IOException {
        return new ModifyTapePartitionSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyTapeSpectraS3Response modifyTapeSpectraS3(ModifyTapeSpectraS3Request request) throws IOException {
        return new ModifyTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public OnlineAllTapesSpectraS3Response onlineAllTapesSpectraS3(OnlineAllTapesSpectraS3Request request) throws IOException {
        return new OnlineAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public OnlineTapeSpectraS3Response onlineTapeSpectraS3(OnlineTapeSpectraS3Request request) throws IOException {
        return new OnlineTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyAllTapesSpectraS3Response verifyAllTapesSpectraS3(VerifyAllTapesSpectraS3Request request) throws IOException {
        return new VerifyAllTapesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyTapeSpectraS3Response verifyTapeSpectraS3(VerifyTapeSpectraS3Request request) throws IOException {
        return new VerifyTapeSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PutDs3TargetReadPreferenceSpectraS3Response putDs3TargetReadPreferenceSpectraS3(PutDs3TargetReadPreferenceSpectraS3Request request) throws IOException {
        return new PutDs3TargetReadPreferenceSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDs3TargetFailureSpectraS3Response deleteDs3TargetFailureSpectraS3(DeleteDs3TargetFailureSpectraS3Request request) throws IOException {
        return new DeleteDs3TargetFailureSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDs3TargetReadPreferenceSpectraS3Response deleteDs3TargetReadPreferenceSpectraS3(DeleteDs3TargetReadPreferenceSpectraS3Request request) throws IOException {
        return new DeleteDs3TargetReadPreferenceSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDs3TargetSpectraS3Response deleteDs3TargetSpectraS3(DeleteDs3TargetSpectraS3Request request) throws IOException {
        return new DeleteDs3TargetSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ForceTargetEnvironmentRefreshSpectraS3Response forceTargetEnvironmentRefreshSpectraS3(ForceTargetEnvironmentRefreshSpectraS3Request request) throws IOException {
        return new ForceTargetEnvironmentRefreshSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetDataPoliciesSpectraS3Response getDs3TargetDataPoliciesSpectraS3(GetDs3TargetDataPoliciesSpectraS3Request request) throws IOException {
        return new GetDs3TargetDataPoliciesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetFailuresSpectraS3Response getDs3TargetFailuresSpectraS3(GetDs3TargetFailuresSpectraS3Request request) throws IOException {
        return new GetDs3TargetFailuresSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetReadPreferenceSpectraS3Response getDs3TargetReadPreferenceSpectraS3(GetDs3TargetReadPreferenceSpectraS3Request request) throws IOException {
        return new GetDs3TargetReadPreferenceSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetReadPreferencesSpectraS3Response getDs3TargetReadPreferencesSpectraS3(GetDs3TargetReadPreferencesSpectraS3Request request) throws IOException {
        return new GetDs3TargetReadPreferencesSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetSpectraS3Response getDs3TargetSpectraS3(GetDs3TargetSpectraS3Request request) throws IOException {
        return new GetDs3TargetSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetsSpectraS3Response getDs3TargetsSpectraS3(GetDs3TargetsSpectraS3Request request) throws IOException {
        return new GetDs3TargetsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyAllDs3TargetsSpectraS3Response modifyAllDs3TargetsSpectraS3(ModifyAllDs3TargetsSpectraS3Request request) throws IOException {
        return new ModifyAllDs3TargetsSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyDs3TargetSpectraS3Response modifyDs3TargetSpectraS3(ModifyDs3TargetSpectraS3Request request) throws IOException {
        return new ModifyDs3TargetSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public PairBackRegisteredDs3TargetSpectraS3Response pairBackRegisteredDs3TargetSpectraS3(PairBackRegisteredDs3TargetSpectraS3Request request) throws IOException {
        return new PairBackRegisteredDs3TargetSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public RegisterDs3TargetSpectraS3Response registerDs3TargetSpectraS3(RegisterDs3TargetSpectraS3Request request) throws IOException {
        return new RegisterDs3TargetSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyDs3TargetSpectraS3Response verifyDs3TargetSpectraS3(VerifyDs3TargetSpectraS3Request request) throws IOException {
        return new VerifyDs3TargetSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DelegateCreateUserSpectraS3Response delegateCreateUserSpectraS3(DelegateCreateUserSpectraS3Request request) throws IOException {
        return new DelegateCreateUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public DelegateDeleteUserSpectraS3Response delegateDeleteUserSpectraS3(DelegateDeleteUserSpectraS3Request request) throws IOException {
        return new DelegateDeleteUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetUserSpectraS3Response getUserSpectraS3(GetUserSpectraS3Request request) throws IOException {
        return new GetUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public GetUsersSpectraS3Response getUsersSpectraS3(GetUsersSpectraS3Request request) throws IOException {
        return new GetUsersSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyUserSpectraS3Response modifyUserSpectraS3(ModifyUserSpectraS3Request request) throws IOException {
        return new ModifyUserSpectraS3Response(this.netClient.getResponse(request));
    }
    @Override
    public RegenerateUserSecretKeySpectraS3Response regenerateUserSecretKeySpectraS3(RegenerateUserSecretKeySpectraS3Request request) throws IOException {
        return new RegenerateUserSecretKeySpectraS3Response(this.netClient.getResponse(request));
    }

    @Override
    public GetObjectResponse getObject(GetObjectRequest request) throws IOException {
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