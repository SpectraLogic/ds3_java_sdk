/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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
import com.spectralogic.ds3client.commands.parsers.*;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.commands.spectrads3.notifications.*;
import com.spectralogic.ds3client.models.JobNode;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.networking.NetworkClient;
import com.spectralogic.ds3client.networking.NetworkClientImpl;

import com.spectralogic.ds3client.commands.parsers.interfaces.GetObjectCustomParser;
import com.spectralogic.ds3client.commands.parsers.interfaces.GetObjectParserConfiguration;
import com.spectralogic.ds3client.commands.parsers.interfaces.Function;

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
        return new AbortMultiPartUploadResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CompleteMultiPartUploadResponse completeMultiPartUpload(final CompleteMultiPartUploadRequest request) throws IOException {
        return new CompleteMultiPartUploadResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutBucketResponse putBucket(final PutBucketRequest request) throws IOException {
        return new PutBucketResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutMultiPartUploadPartResponse putMultiPartUploadPart(final PutMultiPartUploadPartRequest request) throws IOException {
        return new PutMultiPartUploadPartResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutObjectResponse putObject(final PutObjectRequest request) throws IOException {
        return new PutObjectResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteBucketResponse deleteBucket(final DeleteBucketRequest request) throws IOException {
        return new DeleteBucketResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectResponse deleteObject(final DeleteObjectRequest request) throws IOException {
        return new DeleteObjectResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectsResponse deleteObjects(final DeleteObjectsRequest request) throws IOException {
        return new DeleteObjectsResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketResponse getBucket(final GetBucketRequest request) throws IOException {
        return new GetBucketResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetServiceResponse getService(final GetServiceRequest request) throws IOException {
        return new GetServiceResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public HeadBucketResponse headBucket(final HeadBucketRequest request) throws IOException {
        return new HeadBucketResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public HeadObjectResponse headObject(final HeadObjectRequest request) throws IOException {
        return new HeadObjectResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public InitiateMultiPartUploadResponse initiateMultiPartUpload(final InitiateMultiPartUploadRequest request) throws IOException {
        return new InitiateMultiPartUploadResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ListMultiPartUploadPartsResponse listMultiPartUploadParts(final ListMultiPartUploadPartsRequest request) throws IOException {
        return new ListMultiPartUploadPartsResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ListMultiPartUploadsResponse listMultiPartUploads(final ListMultiPartUploadsRequest request) throws IOException {
        return new ListMultiPartUploadsResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutBucketAclForGroupSpectraS3Response putBucketAclForGroupSpectraS3(final PutBucketAclForGroupSpectraS3Request request) throws IOException {
        return new PutBucketAclForGroupSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutBucketAclForUserSpectraS3Response putBucketAclForUserSpectraS3(final PutBucketAclForUserSpectraS3Request request) throws IOException {
        return new PutBucketAclForUserSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataPolicyAclForGroupSpectraS3Response putDataPolicyAclForGroupSpectraS3(final PutDataPolicyAclForGroupSpectraS3Request request) throws IOException {
        return new PutDataPolicyAclForGroupSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataPolicyAclForUserSpectraS3Response putDataPolicyAclForUserSpectraS3(final PutDataPolicyAclForUserSpectraS3Request request) throws IOException {
        return new PutDataPolicyAclForUserSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutGlobalBucketAclForGroupSpectraS3Response putGlobalBucketAclForGroupSpectraS3(final PutGlobalBucketAclForGroupSpectraS3Request request) throws IOException {
        return new PutGlobalBucketAclForGroupSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutGlobalBucketAclForUserSpectraS3Response putGlobalBucketAclForUserSpectraS3(final PutGlobalBucketAclForUserSpectraS3Request request) throws IOException {
        return new PutGlobalBucketAclForUserSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutGlobalDataPolicyAclForGroupSpectraS3Response putGlobalDataPolicyAclForGroupSpectraS3(final PutGlobalDataPolicyAclForGroupSpectraS3Request request) throws IOException {
        return new PutGlobalDataPolicyAclForGroupSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutGlobalDataPolicyAclForUserSpectraS3Response putGlobalDataPolicyAclForUserSpectraS3(final PutGlobalDataPolicyAclForUserSpectraS3Request request) throws IOException {
        return new PutGlobalDataPolicyAclForUserSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteBucketAclSpectraS3Response deleteBucketAclSpectraS3(final DeleteBucketAclSpectraS3Request request) throws IOException {
        return new DeleteBucketAclSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDataPolicyAclSpectraS3Response deleteDataPolicyAclSpectraS3(final DeleteDataPolicyAclSpectraS3Request request) throws IOException {
        return new DeleteDataPolicyAclSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketAclSpectraS3Response getBucketAclSpectraS3(final GetBucketAclSpectraS3Request request) throws IOException {
        return new GetBucketAclSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketAclsSpectraS3Response getBucketAclsSpectraS3(final GetBucketAclsSpectraS3Request request) throws IOException {
        return new GetBucketAclsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPolicyAclSpectraS3Response getDataPolicyAclSpectraS3(final GetDataPolicyAclSpectraS3Request request) throws IOException {
        return new GetDataPolicyAclSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3(final GetDataPolicyAclsSpectraS3Request request) throws IOException {
        return new GetDataPolicyAclsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutBucketSpectraS3Response putBucketSpectraS3(final PutBucketSpectraS3Request request) throws IOException {
        return new PutBucketSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteBucketSpectraS3Response deleteBucketSpectraS3(final DeleteBucketSpectraS3Request request) throws IOException {
        return new DeleteBucketSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketSpectraS3Response getBucketSpectraS3(final GetBucketSpectraS3Request request) throws IOException {
        return new GetBucketSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketsSpectraS3Response getBucketsSpectraS3(final GetBucketsSpectraS3Request request) throws IOException {
        return new GetBucketsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyBucketSpectraS3Response modifyBucketSpectraS3(final ModifyBucketSpectraS3Request request) throws IOException {
        return new ModifyBucketSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ForceFullCacheReclaimSpectraS3Response forceFullCacheReclaimSpectraS3(final ForceFullCacheReclaimSpectraS3Request request) throws IOException {
        return new ForceFullCacheReclaimSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetCacheFilesystemSpectraS3Response getCacheFilesystemSpectraS3(final GetCacheFilesystemSpectraS3Request request) throws IOException {
        return new GetCacheFilesystemSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetCacheFilesystemsSpectraS3Response getCacheFilesystemsSpectraS3(final GetCacheFilesystemsSpectraS3Request request) throws IOException {
        return new GetCacheFilesystemsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetCacheStateSpectraS3Response getCacheStateSpectraS3(final GetCacheStateSpectraS3Request request) throws IOException {
        return new GetCacheStateSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyCacheFilesystemSpectraS3Response modifyCacheFilesystemSpectraS3(final ModifyCacheFilesystemSpectraS3Request request) throws IOException {
        return new ModifyCacheFilesystemSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetBucketCapacitySummarySpectraS3Response getBucketCapacitySummarySpectraS3(final GetBucketCapacitySummarySpectraS3Request request) throws IOException {
        return new GetBucketCapacitySummarySpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainCapacitySummarySpectraS3Response getStorageDomainCapacitySummarySpectraS3(final GetStorageDomainCapacitySummarySpectraS3Request request) throws IOException {
        return new GetStorageDomainCapacitySummarySpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemCapacitySummarySpectraS3Response getSystemCapacitySummarySpectraS3(final GetSystemCapacitySummarySpectraS3Request request) throws IOException {
        return new GetSystemCapacitySummarySpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPathBackendSpectraS3Response getDataPathBackendSpectraS3(final GetDataPathBackendSpectraS3Request request) throws IOException {
        return new GetDataPathBackendSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPlannerBlobStoreTasksSpectraS3Response getDataPlannerBlobStoreTasksSpectraS3(final GetDataPlannerBlobStoreTasksSpectraS3Request request) throws IOException {
        return new GetDataPlannerBlobStoreTasksSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyDataPathBackendSpectraS3Response modifyDataPathBackendSpectraS3(final ModifyDataPathBackendSpectraS3Request request) throws IOException {
        return new ModifyDataPathBackendSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataPersistenceRuleSpectraS3Response putDataPersistenceRuleSpectraS3(final PutDataPersistenceRuleSpectraS3Request request) throws IOException {
        return new PutDataPersistenceRuleSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataPolicySpectraS3Response putDataPolicySpectraS3(final PutDataPolicySpectraS3Request request) throws IOException {
        return new PutDataPolicySpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutDataReplicationRuleSpectraS3Response putDataReplicationRuleSpectraS3(final PutDataReplicationRuleSpectraS3Request request) throws IOException {
        return new PutDataReplicationRuleSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDataPersistenceRuleSpectraS3Response deleteDataPersistenceRuleSpectraS3(final DeleteDataPersistenceRuleSpectraS3Request request) throws IOException {
        return new DeleteDataPersistenceRuleSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDataPolicySpectraS3Response deleteDataPolicySpectraS3(final DeleteDataPolicySpectraS3Request request) throws IOException {
        return new DeleteDataPolicySpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDataReplicationRuleSpectraS3Response deleteDataReplicationRuleSpectraS3(final DeleteDataReplicationRuleSpectraS3Request request) throws IOException {
        return new DeleteDataReplicationRuleSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPersistenceRuleSpectraS3Response getDataPersistenceRuleSpectraS3(final GetDataPersistenceRuleSpectraS3Request request) throws IOException {
        return new GetDataPersistenceRuleSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPersistenceRulesSpectraS3Response getDataPersistenceRulesSpectraS3(final GetDataPersistenceRulesSpectraS3Request request) throws IOException {
        return new GetDataPersistenceRulesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPoliciesSpectraS3Response getDataPoliciesSpectraS3(final GetDataPoliciesSpectraS3Request request) throws IOException {
        return new GetDataPoliciesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataPolicySpectraS3Response getDataPolicySpectraS3(final GetDataPolicySpectraS3Request request) throws IOException {
        return new GetDataPolicySpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataReplicationRuleSpectraS3Response getDataReplicationRuleSpectraS3(final GetDataReplicationRuleSpectraS3Request request) throws IOException {
        return new GetDataReplicationRuleSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDataReplicationRulesSpectraS3Response getDataReplicationRulesSpectraS3(final GetDataReplicationRulesSpectraS3Request request) throws IOException {
        return new GetDataReplicationRulesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyDataPersistenceRuleSpectraS3Response modifyDataPersistenceRuleSpectraS3(final ModifyDataPersistenceRuleSpectraS3Request request) throws IOException {
        return new ModifyDataPersistenceRuleSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyDataPolicySpectraS3Response modifyDataPolicySpectraS3(final ModifyDataPolicySpectraS3Request request) throws IOException {
        return new ModifyDataPolicySpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyDataReplicationRuleSpectraS3Response modifyDataReplicationRuleSpectraS3(final ModifyDataReplicationRuleSpectraS3Request request) throws IOException {
        return new ModifyDataReplicationRuleSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ClearSuspectBlobPoolsSpectraS3Response clearSuspectBlobPoolsSpectraS3(final ClearSuspectBlobPoolsSpectraS3Request request) throws IOException {
        return new ClearSuspectBlobPoolsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ClearSuspectBlobTapesSpectraS3Response clearSuspectBlobTapesSpectraS3(final ClearSuspectBlobTapesSpectraS3Request request) throws IOException {
        return new ClearSuspectBlobTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ClearSuspectBlobTargetsSpectraS3Response clearSuspectBlobTargetsSpectraS3(final ClearSuspectBlobTargetsSpectraS3Request request) throws IOException {
        return new ClearSuspectBlobTargetsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDegradedBlobsSpectraS3Response getDegradedBlobsSpectraS3(final GetDegradedBlobsSpectraS3Request request) throws IOException {
        return new GetDegradedBlobsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDegradedBucketsSpectraS3Response getDegradedBucketsSpectraS3(final GetDegradedBucketsSpectraS3Request request) throws IOException {
        return new GetDegradedBucketsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDegradedDataPersistenceRulesSpectraS3Response getDegradedDataPersistenceRulesSpectraS3(final GetDegradedDataPersistenceRulesSpectraS3Request request) throws IOException {
        return new GetDegradedDataPersistenceRulesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDegradedDataReplicationRulesSpectraS3Response getDegradedDataReplicationRulesSpectraS3(final GetDegradedDataReplicationRulesSpectraS3Request request) throws IOException {
        return new GetDegradedDataReplicationRulesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetSuspectBlobPoolsSpectraS3Response getSuspectBlobPoolsSpectraS3(final GetSuspectBlobPoolsSpectraS3Request request) throws IOException {
        return new GetSuspectBlobPoolsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetSuspectBlobTapesSpectraS3Response getSuspectBlobTapesSpectraS3(final GetSuspectBlobTapesSpectraS3Request request) throws IOException {
        return new GetSuspectBlobTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetSuspectBlobTargetsSpectraS3Response getSuspectBlobTargetsSpectraS3(final GetSuspectBlobTargetsSpectraS3Request request) throws IOException {
        return new GetSuspectBlobTargetsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetSuspectBucketsSpectraS3Response getSuspectBucketsSpectraS3(final GetSuspectBucketsSpectraS3Request request) throws IOException {
        return new GetSuspectBucketsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetSuspectObjectsSpectraS3Response getSuspectObjectsSpectraS3(final GetSuspectObjectsSpectraS3Request request) throws IOException {
        return new GetSuspectObjectsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetSuspectObjectsWithFullDetailsSpectraS3Response getSuspectObjectsWithFullDetailsSpectraS3(final GetSuspectObjectsWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetSuspectObjectsWithFullDetailsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public MarkSuspectBlobPoolsAsDegradedSpectraS3Response markSuspectBlobPoolsAsDegradedSpectraS3(final MarkSuspectBlobPoolsAsDegradedSpectraS3Request request) throws IOException {
        return new MarkSuspectBlobPoolsAsDegradedSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public MarkSuspectBlobTapesAsDegradedSpectraS3Response markSuspectBlobTapesAsDegradedSpectraS3(final MarkSuspectBlobTapesAsDegradedSpectraS3Request request) throws IOException {
        return new MarkSuspectBlobTapesAsDegradedSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public MarkSuspectBlobTargetsAsDegradedSpectraS3Response markSuspectBlobTargetsAsDegradedSpectraS3(final MarkSuspectBlobTargetsAsDegradedSpectraS3Request request) throws IOException {
        return new MarkSuspectBlobTargetsAsDegradedSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutGroupGroupMemberSpectraS3Response putGroupGroupMemberSpectraS3(final PutGroupGroupMemberSpectraS3Request request) throws IOException {
        return new PutGroupGroupMemberSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutGroupSpectraS3Response putGroupSpectraS3(final PutGroupSpectraS3Request request) throws IOException {
        return new PutGroupSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutUserGroupMemberSpectraS3Response putUserGroupMemberSpectraS3(final PutUserGroupMemberSpectraS3Request request) throws IOException {
        return new PutUserGroupMemberSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteGroupMemberSpectraS3Response deleteGroupMemberSpectraS3(final DeleteGroupMemberSpectraS3Request request) throws IOException {
        return new DeleteGroupMemberSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteGroupSpectraS3Response deleteGroupSpectraS3(final DeleteGroupSpectraS3Request request) throws IOException {
        return new DeleteGroupSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetGroupMemberSpectraS3Response getGroupMemberSpectraS3(final GetGroupMemberSpectraS3Request request) throws IOException {
        return new GetGroupMemberSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetGroupMembersSpectraS3Response getGroupMembersSpectraS3(final GetGroupMembersSpectraS3Request request) throws IOException {
        return new GetGroupMembersSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetGroupSpectraS3Response getGroupSpectraS3(final GetGroupSpectraS3Request request) throws IOException {
        return new GetGroupSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetGroupsSpectraS3Response getGroupsSpectraS3(final GetGroupsSpectraS3Request request) throws IOException {
        return new GetGroupsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyGroupSpectraS3Response modifyGroupSpectraS3(final ModifyGroupSpectraS3Request request) throws IOException {
        return new ModifyGroupSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyUserIsMemberOfGroupSpectraS3Response verifyUserIsMemberOfGroupSpectraS3(final VerifyUserIsMemberOfGroupSpectraS3Request request) throws IOException {
        return new VerifyUserIsMemberOfGroupSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public AllocateJobChunkSpectraS3Response allocateJobChunkSpectraS3(final AllocateJobChunkSpectraS3Request request) throws IOException {
        return new AllocateJobChunkSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelActiveJobSpectraS3Response cancelActiveJobSpectraS3(final CancelActiveJobSpectraS3Request request) throws IOException {
        return new CancelActiveJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelAllActiveJobsSpectraS3Response cancelAllActiveJobsSpectraS3(final CancelAllActiveJobsSpectraS3Request request) throws IOException {
        return new CancelAllActiveJobsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelAllJobsSpectraS3Response cancelAllJobsSpectraS3(final CancelAllJobsSpectraS3Request request) throws IOException {
        return new CancelAllJobsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelJobSpectraS3Response cancelJobSpectraS3(final CancelJobSpectraS3Request request) throws IOException {
        return new CancelJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ClearAllCanceledJobsSpectraS3Response clearAllCanceledJobsSpectraS3(final ClearAllCanceledJobsSpectraS3Request request) throws IOException {
        return new ClearAllCanceledJobsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ClearAllCompletedJobsSpectraS3Response clearAllCompletedJobsSpectraS3(final ClearAllCompletedJobsSpectraS3Request request) throws IOException {
        return new ClearAllCompletedJobsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetBulkJobSpectraS3Response getBulkJobSpectraS3(final GetBulkJobSpectraS3Request request) throws IOException {
        return new GetBulkJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutBulkJobSpectraS3Response putBulkJobSpectraS3(final PutBulkJobSpectraS3Request request) throws IOException {
        return new PutBulkJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyBulkJobSpectraS3Response verifyBulkJobSpectraS3(final VerifyBulkJobSpectraS3Request request) throws IOException {
        return new VerifyBulkJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetActiveJobSpectraS3Response getActiveJobSpectraS3(final GetActiveJobSpectraS3Request request) throws IOException {
        return new GetActiveJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetActiveJobsSpectraS3Response getActiveJobsSpectraS3(final GetActiveJobsSpectraS3Request request) throws IOException {
        return new GetActiveJobsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetCanceledJobSpectraS3Response getCanceledJobSpectraS3(final GetCanceledJobSpectraS3Request request) throws IOException {
        return new GetCanceledJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetCanceledJobsSpectraS3Response getCanceledJobsSpectraS3(final GetCanceledJobsSpectraS3Request request) throws IOException {
        return new GetCanceledJobsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetCompletedJobSpectraS3Response getCompletedJobSpectraS3(final GetCompletedJobSpectraS3Request request) throws IOException {
        return new GetCompletedJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetCompletedJobsSpectraS3Response getCompletedJobsSpectraS3(final GetCompletedJobsSpectraS3Request request) throws IOException {
        return new GetCompletedJobsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobChunkDaoSpectraS3Response getJobChunkDaoSpectraS3(final GetJobChunkDaoSpectraS3Request request) throws IOException {
        return new GetJobChunkDaoSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobChunkSpectraS3Response getJobChunkSpectraS3(final GetJobChunkSpectraS3Request request) throws IOException {
        return new GetJobChunkSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobChunksReadyForClientProcessingSpectraS3Response getJobChunksReadyForClientProcessingSpectraS3(final GetJobChunksReadyForClientProcessingSpectraS3Request request) throws IOException {
        return new GetJobChunksReadyForClientProcessingSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobSpectraS3Response getJobSpectraS3(final GetJobSpectraS3Request request) throws IOException {
        return new GetJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobToReplicateSpectraS3Response getJobToReplicateSpectraS3(final GetJobToReplicateSpectraS3Request request) throws IOException {
        return new GetJobToReplicateSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobsSpectraS3Response getJobsSpectraS3(final GetJobsSpectraS3Request request) throws IOException {
        return new GetJobsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyActiveJobSpectraS3Response modifyActiveJobSpectraS3(final ModifyActiveJobSpectraS3Request request) throws IOException {
        return new ModifyActiveJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyJobSpectraS3Response modifyJobSpectraS3(final ModifyJobSpectraS3Request request) throws IOException {
        return new ModifyJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ReplicatePutJobSpectraS3Response replicatePutJobSpectraS3(final ReplicatePutJobSpectraS3Request request) throws IOException {
        return new ReplicatePutJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public TruncateActiveJobSpectraS3Response truncateActiveJobSpectraS3(final TruncateActiveJobSpectraS3Request request) throws IOException {
        return new TruncateActiveJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public TruncateAllActiveJobsSpectraS3Response truncateAllActiveJobsSpectraS3(final TruncateAllActiveJobsSpectraS3Request request) throws IOException {
        return new TruncateAllActiveJobsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public TruncateAllJobsSpectraS3Response truncateAllJobsSpectraS3(final TruncateAllJobsSpectraS3Request request) throws IOException {
        return new TruncateAllJobsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public TruncateJobSpectraS3Response truncateJobSpectraS3(final TruncateJobSpectraS3Request request) throws IOException {
        return new TruncateJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public VerifySafeToCreatePutJobSpectraS3Response verifySafeToCreatePutJobSpectraS3(final VerifySafeToCreatePutJobSpectraS3Request request) throws IOException {
        return new VerifySafeToCreatePutJobSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetNodeSpectraS3Response getNodeSpectraS3(final GetNodeSpectraS3Request request) throws IOException {
        return new GetNodeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetNodesSpectraS3Response getNodesSpectraS3(final GetNodesSpectraS3Request request) throws IOException {
        return new GetNodesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyNodeSpectraS3Response modifyNodeSpectraS3(final ModifyNodeSpectraS3Request request) throws IOException {
        return new ModifyNodeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutDs3TargetFailureNotificationRegistrationSpectraS3Response putDs3TargetFailureNotificationRegistrationSpectraS3(final PutDs3TargetFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutDs3TargetFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutJobCompletedNotificationRegistrationSpectraS3Response putJobCompletedNotificationRegistrationSpectraS3(final PutJobCompletedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutJobCompletedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutJobCreatedNotificationRegistrationSpectraS3Response putJobCreatedNotificationRegistrationSpectraS3(final PutJobCreatedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutJobCreatedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutJobCreationFailedNotificationRegistrationSpectraS3Response putJobCreationFailedNotificationRegistrationSpectraS3(final PutJobCreationFailedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutJobCreationFailedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutObjectCachedNotificationRegistrationSpectraS3Response putObjectCachedNotificationRegistrationSpectraS3(final PutObjectCachedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutObjectCachedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutObjectLostNotificationRegistrationSpectraS3Response putObjectLostNotificationRegistrationSpectraS3(final PutObjectLostNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutObjectLostNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutObjectPersistedNotificationRegistrationSpectraS3Response putObjectPersistedNotificationRegistrationSpectraS3(final PutObjectPersistedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutObjectPersistedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutPoolFailureNotificationRegistrationSpectraS3Response putPoolFailureNotificationRegistrationSpectraS3(final PutPoolFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutPoolFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutStorageDomainFailureNotificationRegistrationSpectraS3Response putStorageDomainFailureNotificationRegistrationSpectraS3(final PutStorageDomainFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutStorageDomainFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutSystemFailureNotificationRegistrationSpectraS3Response putSystemFailureNotificationRegistrationSpectraS3(final PutSystemFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutSystemFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutTapeFailureNotificationRegistrationSpectraS3Response putTapeFailureNotificationRegistrationSpectraS3(final PutTapeFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutTapeFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutTapePartitionFailureNotificationRegistrationSpectraS3Response putTapePartitionFailureNotificationRegistrationSpectraS3(final PutTapePartitionFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new PutTapePartitionFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDs3TargetFailureNotificationRegistrationSpectraS3Response deleteDs3TargetFailureNotificationRegistrationSpectraS3(final DeleteDs3TargetFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteDs3TargetFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteJobCompletedNotificationRegistrationSpectraS3Response deleteJobCompletedNotificationRegistrationSpectraS3(final DeleteJobCompletedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteJobCompletedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteJobCreatedNotificationRegistrationSpectraS3Response deleteJobCreatedNotificationRegistrationSpectraS3(final DeleteJobCreatedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteJobCreatedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteJobCreationFailedNotificationRegistrationSpectraS3Response deleteJobCreationFailedNotificationRegistrationSpectraS3(final DeleteJobCreationFailedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteJobCreationFailedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectCachedNotificationRegistrationSpectraS3Response deleteObjectCachedNotificationRegistrationSpectraS3(final DeleteObjectCachedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteObjectCachedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectLostNotificationRegistrationSpectraS3Response deleteObjectLostNotificationRegistrationSpectraS3(final DeleteObjectLostNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteObjectLostNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteObjectPersistedNotificationRegistrationSpectraS3Response deleteObjectPersistedNotificationRegistrationSpectraS3(final DeleteObjectPersistedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteObjectPersistedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePoolFailureNotificationRegistrationSpectraS3Response deletePoolFailureNotificationRegistrationSpectraS3(final DeletePoolFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeletePoolFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteStorageDomainFailureNotificationRegistrationSpectraS3Response deleteStorageDomainFailureNotificationRegistrationSpectraS3(final DeleteStorageDomainFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteStorageDomainFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteSystemFailureNotificationRegistrationSpectraS3Response deleteSystemFailureNotificationRegistrationSpectraS3(final DeleteSystemFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteSystemFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapeFailureNotificationRegistrationSpectraS3Response deleteTapeFailureNotificationRegistrationSpectraS3(final DeleteTapeFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteTapeFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapePartitionFailureNotificationRegistrationSpectraS3Response deleteTapePartitionFailureNotificationRegistrationSpectraS3(final DeleteTapePartitionFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new DeleteTapePartitionFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetFailureNotificationRegistrationSpectraS3Response getDs3TargetFailureNotificationRegistrationSpectraS3(final GetDs3TargetFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetDs3TargetFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetFailureNotificationRegistrationsSpectraS3Response getDs3TargetFailureNotificationRegistrationsSpectraS3(final GetDs3TargetFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetDs3TargetFailureNotificationRegistrationsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCompletedNotificationRegistrationSpectraS3Response getJobCompletedNotificationRegistrationSpectraS3(final GetJobCompletedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetJobCompletedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCompletedNotificationRegistrationsSpectraS3Response getJobCompletedNotificationRegistrationsSpectraS3(final GetJobCompletedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetJobCompletedNotificationRegistrationsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCreatedNotificationRegistrationSpectraS3Response getJobCreatedNotificationRegistrationSpectraS3(final GetJobCreatedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetJobCreatedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCreatedNotificationRegistrationsSpectraS3Response getJobCreatedNotificationRegistrationsSpectraS3(final GetJobCreatedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetJobCreatedNotificationRegistrationsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCreationFailedNotificationRegistrationSpectraS3Response getJobCreationFailedNotificationRegistrationSpectraS3(final GetJobCreationFailedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetJobCreationFailedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetJobCreationFailedNotificationRegistrationsSpectraS3Response getJobCreationFailedNotificationRegistrationsSpectraS3(final GetJobCreationFailedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetJobCreationFailedNotificationRegistrationsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectCachedNotificationRegistrationSpectraS3Response getObjectCachedNotificationRegistrationSpectraS3(final GetObjectCachedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetObjectCachedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectCachedNotificationRegistrationsSpectraS3Response getObjectCachedNotificationRegistrationsSpectraS3(final GetObjectCachedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetObjectCachedNotificationRegistrationsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectLostNotificationRegistrationSpectraS3Response getObjectLostNotificationRegistrationSpectraS3(final GetObjectLostNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetObjectLostNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectLostNotificationRegistrationsSpectraS3Response getObjectLostNotificationRegistrationsSpectraS3(final GetObjectLostNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetObjectLostNotificationRegistrationsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectPersistedNotificationRegistrationSpectraS3Response getObjectPersistedNotificationRegistrationSpectraS3(final GetObjectPersistedNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetObjectPersistedNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectPersistedNotificationRegistrationsSpectraS3Response getObjectPersistedNotificationRegistrationsSpectraS3(final GetObjectPersistedNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetObjectPersistedNotificationRegistrationsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolFailureNotificationRegistrationSpectraS3Response getPoolFailureNotificationRegistrationSpectraS3(final GetPoolFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetPoolFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolFailureNotificationRegistrationsSpectraS3Response getPoolFailureNotificationRegistrationsSpectraS3(final GetPoolFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetPoolFailureNotificationRegistrationsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainFailureNotificationRegistrationSpectraS3Response getStorageDomainFailureNotificationRegistrationSpectraS3(final GetStorageDomainFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetStorageDomainFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainFailureNotificationRegistrationsSpectraS3Response getStorageDomainFailureNotificationRegistrationsSpectraS3(final GetStorageDomainFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetStorageDomainFailureNotificationRegistrationsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemFailureNotificationRegistrationSpectraS3Response getSystemFailureNotificationRegistrationSpectraS3(final GetSystemFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetSystemFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemFailureNotificationRegistrationsSpectraS3Response getSystemFailureNotificationRegistrationsSpectraS3(final GetSystemFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetSystemFailureNotificationRegistrationsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeFailureNotificationRegistrationSpectraS3Response getTapeFailureNotificationRegistrationSpectraS3(final GetTapeFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetTapeFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeFailureNotificationRegistrationsSpectraS3Response getTapeFailureNotificationRegistrationsSpectraS3(final GetTapeFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetTapeFailureNotificationRegistrationsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionFailureNotificationRegistrationSpectraS3Response getTapePartitionFailureNotificationRegistrationSpectraS3(final GetTapePartitionFailureNotificationRegistrationSpectraS3Request request) throws IOException {
        return new GetTapePartitionFailureNotificationRegistrationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionFailureNotificationRegistrationsSpectraS3Response getTapePartitionFailureNotificationRegistrationsSpectraS3(final GetTapePartitionFailureNotificationRegistrationsSpectraS3Request request) throws IOException {
        return new GetTapePartitionFailureNotificationRegistrationsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteFolderRecursivelySpectraS3Response deleteFolderRecursivelySpectraS3(final DeleteFolderRecursivelySpectraS3Request request) throws IOException {
        return new DeleteFolderRecursivelySpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetBlobPersistenceSpectraS3Response getBlobPersistenceSpectraS3(final GetBlobPersistenceSpectraS3Request request) throws IOException {
        return new GetBlobPersistenceSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectDetailsSpectraS3Response getObjectDetailsSpectraS3(final GetObjectDetailsSpectraS3Request request) throws IOException {
        return new GetObjectDetailsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectsDetailsSpectraS3Response getObjectsDetailsSpectraS3(final GetObjectsDetailsSpectraS3Request request) throws IOException {
        return new GetObjectsDetailsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetObjectsWithFullDetailsSpectraS3Response getObjectsWithFullDetailsSpectraS3(final GetObjectsWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetObjectsWithFullDetailsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetPhysicalPlacementForObjectsSpectraS3Response getPhysicalPlacementForObjectsSpectraS3(final GetPhysicalPlacementForObjectsSpectraS3Request request) throws IOException {
        return new GetPhysicalPlacementForObjectsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response getPhysicalPlacementForObjectsWithFullDetailsSpectraS3(final GetPhysicalPlacementForObjectsWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetPhysicalPlacementForObjectsWithFullDetailsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyPhysicalPlacementForObjectsSpectraS3Response verifyPhysicalPlacementForObjectsSpectraS3(final VerifyPhysicalPlacementForObjectsSpectraS3Request request) throws IOException {
        return new VerifyPhysicalPlacementForObjectsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response verifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3(final VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Request request) throws IOException {
        return new VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelImportOnAllPoolsSpectraS3Response cancelImportOnAllPoolsSpectraS3(final CancelImportOnAllPoolsSpectraS3Request request) throws IOException {
        return new CancelImportOnAllPoolsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelImportPoolSpectraS3Response cancelImportPoolSpectraS3(final CancelImportPoolSpectraS3Request request) throws IOException {
        return new CancelImportPoolSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CompactAllPoolsSpectraS3Response compactAllPoolsSpectraS3(final CompactAllPoolsSpectraS3Request request) throws IOException {
        return new CompactAllPoolsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CompactPoolSpectraS3Response compactPoolSpectraS3(final CompactPoolSpectraS3Request request) throws IOException {
        return new CompactPoolSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutPoolPartitionSpectraS3Response putPoolPartitionSpectraS3(final PutPoolPartitionSpectraS3Request request) throws IOException {
        return new PutPoolPartitionSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeallocatePoolSpectraS3Response deallocatePoolSpectraS3(final DeallocatePoolSpectraS3Request request) throws IOException {
        return new DeallocatePoolSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePermanentlyLostPoolSpectraS3Response deletePermanentlyLostPoolSpectraS3(final DeletePermanentlyLostPoolSpectraS3Request request) throws IOException {
        return new DeletePermanentlyLostPoolSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePoolFailureSpectraS3Response deletePoolFailureSpectraS3(final DeletePoolFailureSpectraS3Request request) throws IOException {
        return new DeletePoolFailureSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePoolPartitionSpectraS3Response deletePoolPartitionSpectraS3(final DeletePoolPartitionSpectraS3Request request) throws IOException {
        return new DeletePoolPartitionSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ForcePoolEnvironmentRefreshSpectraS3Response forcePoolEnvironmentRefreshSpectraS3(final ForcePoolEnvironmentRefreshSpectraS3Request request) throws IOException {
        return new ForcePoolEnvironmentRefreshSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public FormatAllForeignPoolsSpectraS3Response formatAllForeignPoolsSpectraS3(final FormatAllForeignPoolsSpectraS3Request request) throws IOException {
        return new FormatAllForeignPoolsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public FormatForeignPoolSpectraS3Response formatForeignPoolSpectraS3(final FormatForeignPoolSpectraS3Request request) throws IOException {
        return new FormatForeignPoolSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetBlobsOnPoolSpectraS3Response getBlobsOnPoolSpectraS3(final GetBlobsOnPoolSpectraS3Request request) throws IOException {
        return new GetBlobsOnPoolSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolFailuresSpectraS3Response getPoolFailuresSpectraS3(final GetPoolFailuresSpectraS3Request request) throws IOException {
        return new GetPoolFailuresSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolPartitionSpectraS3Response getPoolPartitionSpectraS3(final GetPoolPartitionSpectraS3Request request) throws IOException {
        return new GetPoolPartitionSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolPartitionsSpectraS3Response getPoolPartitionsSpectraS3(final GetPoolPartitionsSpectraS3Request request) throws IOException {
        return new GetPoolPartitionsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolSpectraS3Response getPoolSpectraS3(final GetPoolSpectraS3Request request) throws IOException {
        return new GetPoolSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetPoolsSpectraS3Response getPoolsSpectraS3(final GetPoolsSpectraS3Request request) throws IOException {
        return new GetPoolsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ImportAllPoolsSpectraS3Response importAllPoolsSpectraS3(final ImportAllPoolsSpectraS3Request request) throws IOException {
        return new ImportAllPoolsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ImportPoolSpectraS3Response importPoolSpectraS3(final ImportPoolSpectraS3Request request) throws IOException {
        return new ImportPoolSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyAllPoolsSpectraS3Response modifyAllPoolsSpectraS3(final ModifyAllPoolsSpectraS3Request request) throws IOException {
        return new ModifyAllPoolsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyPoolPartitionSpectraS3Response modifyPoolPartitionSpectraS3(final ModifyPoolPartitionSpectraS3Request request) throws IOException {
        return new ModifyPoolPartitionSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyPoolSpectraS3Response modifyPoolSpectraS3(final ModifyPoolSpectraS3Request request) throws IOException {
        return new ModifyPoolSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyAllPoolsSpectraS3Response verifyAllPoolsSpectraS3(final VerifyAllPoolsSpectraS3Request request) throws IOException {
        return new VerifyAllPoolsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyPoolSpectraS3Response verifyPoolSpectraS3(final VerifyPoolSpectraS3Request request) throws IOException {
        return new VerifyPoolSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ConvertStorageDomainToDs3TargetSpectraS3Response convertStorageDomainToDs3TargetSpectraS3(final ConvertStorageDomainToDs3TargetSpectraS3Request request) throws IOException {
        return new ConvertStorageDomainToDs3TargetSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutPoolStorageDomainMemberSpectraS3Response putPoolStorageDomainMemberSpectraS3(final PutPoolStorageDomainMemberSpectraS3Request request) throws IOException {
        return new PutPoolStorageDomainMemberSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutStorageDomainSpectraS3Response putStorageDomainSpectraS3(final PutStorageDomainSpectraS3Request request) throws IOException {
        return new PutStorageDomainSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutTapeStorageDomainMemberSpectraS3Response putTapeStorageDomainMemberSpectraS3(final PutTapeStorageDomainMemberSpectraS3Request request) throws IOException {
        return new PutTapeStorageDomainMemberSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteStorageDomainFailureSpectraS3Response deleteStorageDomainFailureSpectraS3(final DeleteStorageDomainFailureSpectraS3Request request) throws IOException {
        return new DeleteStorageDomainFailureSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteStorageDomainMemberSpectraS3Response deleteStorageDomainMemberSpectraS3(final DeleteStorageDomainMemberSpectraS3Request request) throws IOException {
        return new DeleteStorageDomainMemberSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteStorageDomainSpectraS3Response deleteStorageDomainSpectraS3(final DeleteStorageDomainSpectraS3Request request) throws IOException {
        return new DeleteStorageDomainSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainFailuresSpectraS3Response getStorageDomainFailuresSpectraS3(final GetStorageDomainFailuresSpectraS3Request request) throws IOException {
        return new GetStorageDomainFailuresSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainMemberSpectraS3Response getStorageDomainMemberSpectraS3(final GetStorageDomainMemberSpectraS3Request request) throws IOException {
        return new GetStorageDomainMemberSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainMembersSpectraS3Response getStorageDomainMembersSpectraS3(final GetStorageDomainMembersSpectraS3Request request) throws IOException {
        return new GetStorageDomainMembersSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainSpectraS3Response getStorageDomainSpectraS3(final GetStorageDomainSpectraS3Request request) throws IOException {
        return new GetStorageDomainSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetStorageDomainsSpectraS3Response getStorageDomainsSpectraS3(final GetStorageDomainsSpectraS3Request request) throws IOException {
        return new GetStorageDomainsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyStorageDomainMemberSpectraS3Response modifyStorageDomainMemberSpectraS3(final ModifyStorageDomainMemberSpectraS3Request request) throws IOException {
        return new ModifyStorageDomainMemberSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyStorageDomainSpectraS3Response modifyStorageDomainSpectraS3(final ModifyStorageDomainSpectraS3Request request) throws IOException {
        return new ModifyStorageDomainSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemFailuresSpectraS3Response getSystemFailuresSpectraS3(final GetSystemFailuresSpectraS3Request request) throws IOException {
        return new GetSystemFailuresSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetSystemInformationSpectraS3Response getSystemInformationSpectraS3(final GetSystemInformationSpectraS3Request request) throws IOException {
        return new GetSystemInformationSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ResetInstanceIdentifierSpectraS3Response resetInstanceIdentifierSpectraS3(final ResetInstanceIdentifierSpectraS3Request request) throws IOException {
        return new ResetInstanceIdentifierSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public VerifySystemHealthSpectraS3Response verifySystemHealthSpectraS3(final VerifySystemHealthSpectraS3Request request) throws IOException {
        return new VerifySystemHealthSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelEjectOnAllTapesSpectraS3Response cancelEjectOnAllTapesSpectraS3(final CancelEjectOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelEjectOnAllTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelEjectTapeSpectraS3Response cancelEjectTapeSpectraS3(final CancelEjectTapeSpectraS3Request request) throws IOException {
        return new CancelEjectTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelFormatOnAllTapesSpectraS3Response cancelFormatOnAllTapesSpectraS3(final CancelFormatOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelFormatOnAllTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelFormatTapeSpectraS3Response cancelFormatTapeSpectraS3(final CancelFormatTapeSpectraS3Request request) throws IOException {
        return new CancelFormatTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelImportOnAllTapesSpectraS3Response cancelImportOnAllTapesSpectraS3(final CancelImportOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelImportOnAllTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelImportTapeSpectraS3Response cancelImportTapeSpectraS3(final CancelImportTapeSpectraS3Request request) throws IOException {
        return new CancelImportTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelOnlineOnAllTapesSpectraS3Response cancelOnlineOnAllTapesSpectraS3(final CancelOnlineOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelOnlineOnAllTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelOnlineTapeSpectraS3Response cancelOnlineTapeSpectraS3(final CancelOnlineTapeSpectraS3Request request) throws IOException {
        return new CancelOnlineTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelVerifyOnAllTapesSpectraS3Response cancelVerifyOnAllTapesSpectraS3(final CancelVerifyOnAllTapesSpectraS3Request request) throws IOException {
        return new CancelVerifyOnAllTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CancelVerifyTapeSpectraS3Response cancelVerifyTapeSpectraS3(final CancelVerifyTapeSpectraS3Request request) throws IOException {
        return new CancelVerifyTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public CleanTapeDriveSpectraS3Response cleanTapeDriveSpectraS3(final CleanTapeDriveSpectraS3Request request) throws IOException {
        return new CleanTapeDriveSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutTapeDensityDirectiveSpectraS3Response putTapeDensityDirectiveSpectraS3(final PutTapeDensityDirectiveSpectraS3Request request) throws IOException {
        return new PutTapeDensityDirectiveSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeletePermanentlyLostTapeSpectraS3Response deletePermanentlyLostTapeSpectraS3(final DeletePermanentlyLostTapeSpectraS3Request request) throws IOException {
        return new DeletePermanentlyLostTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapeDensityDirectiveSpectraS3Response deleteTapeDensityDirectiveSpectraS3(final DeleteTapeDensityDirectiveSpectraS3Request request) throws IOException {
        return new DeleteTapeDensityDirectiveSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapeDriveSpectraS3Response deleteTapeDriveSpectraS3(final DeleteTapeDriveSpectraS3Request request) throws IOException {
        return new DeleteTapeDriveSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapeFailureSpectraS3Response deleteTapeFailureSpectraS3(final DeleteTapeFailureSpectraS3Request request) throws IOException {
        return new DeleteTapeFailureSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapePartitionFailureSpectraS3Response deleteTapePartitionFailureSpectraS3(final DeleteTapePartitionFailureSpectraS3Request request) throws IOException {
        return new DeleteTapePartitionFailureSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteTapePartitionSpectraS3Response deleteTapePartitionSpectraS3(final DeleteTapePartitionSpectraS3Request request) throws IOException {
        return new DeleteTapePartitionSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public EjectAllTapesSpectraS3Response ejectAllTapesSpectraS3(final EjectAllTapesSpectraS3Request request) throws IOException {
        return new EjectAllTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public EjectStorageDomainBlobsSpectraS3Response ejectStorageDomainBlobsSpectraS3(final EjectStorageDomainBlobsSpectraS3Request request) throws IOException {
        return new EjectStorageDomainBlobsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public EjectStorageDomainSpectraS3Response ejectStorageDomainSpectraS3(final EjectStorageDomainSpectraS3Request request) throws IOException {
        return new EjectStorageDomainSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public EjectTapeSpectraS3Response ejectTapeSpectraS3(final EjectTapeSpectraS3Request request) throws IOException {
        return new EjectTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ForceTapeEnvironmentRefreshSpectraS3Response forceTapeEnvironmentRefreshSpectraS3(final ForceTapeEnvironmentRefreshSpectraS3Request request) throws IOException {
        return new ForceTapeEnvironmentRefreshSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public FormatAllTapesSpectraS3Response formatAllTapesSpectraS3(final FormatAllTapesSpectraS3Request request) throws IOException {
        return new FormatAllTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public FormatTapeSpectraS3Response formatTapeSpectraS3(final FormatTapeSpectraS3Request request) throws IOException {
        return new FormatTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetBlobsOnTapeSpectraS3Response getBlobsOnTapeSpectraS3(final GetBlobsOnTapeSpectraS3Request request) throws IOException {
        return new GetBlobsOnTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeDensityDirectiveSpectraS3Response getTapeDensityDirectiveSpectraS3(final GetTapeDensityDirectiveSpectraS3Request request) throws IOException {
        return new GetTapeDensityDirectiveSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeDensityDirectivesSpectraS3Response getTapeDensityDirectivesSpectraS3(final GetTapeDensityDirectivesSpectraS3Request request) throws IOException {
        return new GetTapeDensityDirectivesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeDriveSpectraS3Response getTapeDriveSpectraS3(final GetTapeDriveSpectraS3Request request) throws IOException {
        return new GetTapeDriveSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeDrivesSpectraS3Response getTapeDrivesSpectraS3(final GetTapeDrivesSpectraS3Request request) throws IOException {
        return new GetTapeDrivesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeFailuresSpectraS3Response getTapeFailuresSpectraS3(final GetTapeFailuresSpectraS3Request request) throws IOException {
        return new GetTapeFailuresSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeLibrariesSpectraS3Response getTapeLibrariesSpectraS3(final GetTapeLibrariesSpectraS3Request request) throws IOException {
        return new GetTapeLibrariesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeLibrarySpectraS3Response getTapeLibrarySpectraS3(final GetTapeLibrarySpectraS3Request request) throws IOException {
        return new GetTapeLibrarySpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionFailuresSpectraS3Response getTapePartitionFailuresSpectraS3(final GetTapePartitionFailuresSpectraS3Request request) throws IOException {
        return new GetTapePartitionFailuresSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionSpectraS3Response getTapePartitionSpectraS3(final GetTapePartitionSpectraS3Request request) throws IOException {
        return new GetTapePartitionSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionWithFullDetailsSpectraS3Response getTapePartitionWithFullDetailsSpectraS3(final GetTapePartitionWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetTapePartitionWithFullDetailsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionsSpectraS3Response getTapePartitionsSpectraS3(final GetTapePartitionsSpectraS3Request request) throws IOException {
        return new GetTapePartitionsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapePartitionsWithFullDetailsSpectraS3Response getTapePartitionsWithFullDetailsSpectraS3(final GetTapePartitionsWithFullDetailsSpectraS3Request request) throws IOException {
        return new GetTapePartitionsWithFullDetailsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapeSpectraS3Response getTapeSpectraS3(final GetTapeSpectraS3Request request) throws IOException {
        return new GetTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetTapesSpectraS3Response getTapesSpectraS3(final GetTapesSpectraS3Request request) throws IOException {
        return new GetTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ImportAllTapesSpectraS3Response importAllTapesSpectraS3(final ImportAllTapesSpectraS3Request request) throws IOException {
        return new ImportAllTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ImportTapeSpectraS3Response importTapeSpectraS3(final ImportTapeSpectraS3Request request) throws IOException {
        return new ImportTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public InspectAllTapesSpectraS3Response inspectAllTapesSpectraS3(final InspectAllTapesSpectraS3Request request) throws IOException {
        return new InspectAllTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public InspectTapeSpectraS3Response inspectTapeSpectraS3(final InspectTapeSpectraS3Request request) throws IOException {
        return new InspectTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyAllTapePartitionsSpectraS3Response modifyAllTapePartitionsSpectraS3(final ModifyAllTapePartitionsSpectraS3Request request) throws IOException {
        return new ModifyAllTapePartitionsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyTapePartitionSpectraS3Response modifyTapePartitionSpectraS3(final ModifyTapePartitionSpectraS3Request request) throws IOException {
        return new ModifyTapePartitionSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyTapeSpectraS3Response modifyTapeSpectraS3(final ModifyTapeSpectraS3Request request) throws IOException {
        return new ModifyTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public OnlineAllTapesSpectraS3Response onlineAllTapesSpectraS3(final OnlineAllTapesSpectraS3Request request) throws IOException {
        return new OnlineAllTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public OnlineTapeSpectraS3Response onlineTapeSpectraS3(final OnlineTapeSpectraS3Request request) throws IOException {
        return new OnlineTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyAllTapesSpectraS3Response verifyAllTapesSpectraS3(final VerifyAllTapesSpectraS3Request request) throws IOException {
        return new VerifyAllTapesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyTapeSpectraS3Response verifyTapeSpectraS3(final VerifyTapeSpectraS3Request request) throws IOException {
        return new VerifyTapeSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PutDs3TargetReadPreferenceSpectraS3Response putDs3TargetReadPreferenceSpectraS3(final PutDs3TargetReadPreferenceSpectraS3Request request) throws IOException {
        return new PutDs3TargetReadPreferenceSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDs3TargetFailureSpectraS3Response deleteDs3TargetFailureSpectraS3(final DeleteDs3TargetFailureSpectraS3Request request) throws IOException {
        return new DeleteDs3TargetFailureSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDs3TargetReadPreferenceSpectraS3Response deleteDs3TargetReadPreferenceSpectraS3(final DeleteDs3TargetReadPreferenceSpectraS3Request request) throws IOException {
        return new DeleteDs3TargetReadPreferenceSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DeleteDs3TargetSpectraS3Response deleteDs3TargetSpectraS3(final DeleteDs3TargetSpectraS3Request request) throws IOException {
        return new DeleteDs3TargetSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ForceTargetEnvironmentRefreshSpectraS3Response forceTargetEnvironmentRefreshSpectraS3(final ForceTargetEnvironmentRefreshSpectraS3Request request) throws IOException {
        return new ForceTargetEnvironmentRefreshSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetBlobsOnDs3TargetSpectraS3Response getBlobsOnDs3TargetSpectraS3(final GetBlobsOnDs3TargetSpectraS3Request request) throws IOException {
        return new GetBlobsOnDs3TargetSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetDataPoliciesSpectraS3Response getDs3TargetDataPoliciesSpectraS3(final GetDs3TargetDataPoliciesSpectraS3Request request) throws IOException {
        return new GetDs3TargetDataPoliciesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetFailuresSpectraS3Response getDs3TargetFailuresSpectraS3(final GetDs3TargetFailuresSpectraS3Request request) throws IOException {
        return new GetDs3TargetFailuresSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetReadPreferenceSpectraS3Response getDs3TargetReadPreferenceSpectraS3(final GetDs3TargetReadPreferenceSpectraS3Request request) throws IOException {
        return new GetDs3TargetReadPreferenceSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetReadPreferencesSpectraS3Response getDs3TargetReadPreferencesSpectraS3(final GetDs3TargetReadPreferencesSpectraS3Request request) throws IOException {
        return new GetDs3TargetReadPreferencesSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetSpectraS3Response getDs3TargetSpectraS3(final GetDs3TargetSpectraS3Request request) throws IOException {
        return new GetDs3TargetSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetDs3TargetsSpectraS3Response getDs3TargetsSpectraS3(final GetDs3TargetsSpectraS3Request request) throws IOException {
        return new GetDs3TargetsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyAllDs3TargetsSpectraS3Response modifyAllDs3TargetsSpectraS3(final ModifyAllDs3TargetsSpectraS3Request request) throws IOException {
        return new ModifyAllDs3TargetsSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyDs3TargetSpectraS3Response modifyDs3TargetSpectraS3(final ModifyDs3TargetSpectraS3Request request) throws IOException {
        return new ModifyDs3TargetSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public PairBackRegisteredDs3TargetSpectraS3Response pairBackRegisteredDs3TargetSpectraS3(final PairBackRegisteredDs3TargetSpectraS3Request request) throws IOException {
        return new PairBackRegisteredDs3TargetSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public RegisterDs3TargetSpectraS3Response registerDs3TargetSpectraS3(final RegisterDs3TargetSpectraS3Request request) throws IOException {
        return new RegisterDs3TargetSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public VerifyDs3TargetSpectraS3Response verifyDs3TargetSpectraS3(final VerifyDs3TargetSpectraS3Request request) throws IOException {
        return new VerifyDs3TargetSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DelegateCreateUserSpectraS3Response delegateCreateUserSpectraS3(final DelegateCreateUserSpectraS3Request request) throws IOException {
        return new DelegateCreateUserSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public DelegateDeleteUserSpectraS3Response delegateDeleteUserSpectraS3(final DelegateDeleteUserSpectraS3Request request) throws IOException {
        return new DelegateDeleteUserSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetUserSpectraS3Response getUserSpectraS3(final GetUserSpectraS3Request request) throws IOException {
        return new GetUserSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public GetUsersSpectraS3Response getUsersSpectraS3(final GetUsersSpectraS3Request request) throws IOException {
        return new GetUsersSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public ModifyUserSpectraS3Response modifyUserSpectraS3(final ModifyUserSpectraS3Request request) throws IOException {
        return new ModifyUserSpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }
    @Override
    public RegenerateUserSecretKeySpectraS3Response regenerateUserSecretKeySpectraS3(final RegenerateUserSecretKeySpectraS3Request request) throws IOException {
        return new RegenerateUserSecretKeySpectraS3ResponseParser().response(this.netClient.getResponse(request));
    }

    @Override
    public GetObjectResponse getObject(final GetObjectRequest request) throws IOException {
        return new GetObjectResponseParser(
                request.getChannel(),
                this.netClient.getConnectionDetails().getBufferSize(),
                request.getObjectName())
                .response(this.netClient.getResponse(request));
    }

    @Override
    public GetObjectResponse getObject(final GetObjectRequest request, Function<GetObjectParserConfiguration, GetObjectResponse> parsingFunction) throws IOException {
        return new GetObjectCustomParser(
                request.getChannel(),
                this.netClient.getConnectionDetails().getBufferSize(),
                request.getObjectName(),
                parsingFunction)
                .response(this.netClient.getResponse(request));
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