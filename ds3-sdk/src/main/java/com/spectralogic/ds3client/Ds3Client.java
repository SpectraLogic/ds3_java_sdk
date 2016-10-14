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

import com.spectralogic.ds3client.annotations.Action;
import com.spectralogic.ds3client.annotations.Resource;
import com.spectralogic.ds3client.annotations.ResponsePayloadModel;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.commands.spectrads3.notifications.*;
import com.spectralogic.ds3client.models.JobNode;
import com.spectralogic.ds3client.networking.ConnectionDetails;

import java.io.Closeable;
import java.io.IOException;

public interface Ds3Client extends Closeable {

    ConnectionDetails getConnectionDetails();

    
    
    AbortMultiPartUploadResponse abortMultiPartUpload(final AbortMultiPartUploadRequest request)
            throws IOException;

    
    
    CompleteMultiPartUploadResponse completeMultiPartUpload(final CompleteMultiPartUploadRequest request)
            throws IOException;

    
    
    PutBucketResponse putBucket(final PutBucketRequest request)
            throws IOException;

    
    
    PutMultiPartUploadPartResponse putMultiPartUploadPart(final PutMultiPartUploadPartRequest request)
            throws IOException;

    
    
    PutObjectResponse putObject(final PutObjectRequest request)
            throws IOException;

    
    
    DeleteBucketResponse deleteBucket(final DeleteBucketRequest request)
            throws IOException;

    
    
    DeleteObjectResponse deleteObject(final DeleteObjectRequest request)
            throws IOException;

    
    
    DeleteObjectsResponse deleteObjects(final DeleteObjectsRequest request)
            throws IOException;

    
    
    GetBucketResponse getBucket(final GetBucketRequest request)
            throws IOException;

    
    
    GetServiceResponse getService(final GetServiceRequest request)
            throws IOException;

    
    
    HeadBucketResponse headBucket(final HeadBucketRequest request)
            throws IOException;

    
    
    HeadObjectResponse headObject(final HeadObjectRequest request)
            throws IOException;

    
    
    InitiateMultiPartUploadResponse initiateMultiPartUpload(final InitiateMultiPartUploadRequest request)
            throws IOException;

    
    
    ListMultiPartUploadPartsResponse listMultiPartUploadParts(final ListMultiPartUploadPartsRequest request)
            throws IOException;

    
    
    ListMultiPartUploadsResponse listMultiPartUploads(final ListMultiPartUploadsRequest request)
            throws IOException;

    @ResponsePayloadModel("BucketAcl")
    @Action("CREATE")
    @Resource("BUCKET_ACL")
    
    PutBucketAclForGroupSpectraS3Response putBucketAclForGroupSpectraS3(final PutBucketAclForGroupSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BucketAcl")
    @Action("CREATE")
    @Resource("BUCKET_ACL")
    
    PutBucketAclForUserSpectraS3Response putBucketAclForUserSpectraS3(final PutBucketAclForUserSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPolicyAcl")
    @Action("CREATE")
    @Resource("DATA_POLICY_ACL")
    
    PutDataPolicyAclForGroupSpectraS3Response putDataPolicyAclForGroupSpectraS3(final PutDataPolicyAclForGroupSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPolicyAcl")
    @Action("CREATE")
    @Resource("DATA_POLICY_ACL")
    
    PutDataPolicyAclForUserSpectraS3Response putDataPolicyAclForUserSpectraS3(final PutDataPolicyAclForUserSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BucketAcl")
    @Action("CREATE")
    @Resource("BUCKET_ACL")
    
    PutGlobalBucketAclForGroupSpectraS3Response putGlobalBucketAclForGroupSpectraS3(final PutGlobalBucketAclForGroupSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BucketAcl")
    @Action("CREATE")
    @Resource("BUCKET_ACL")
    
    PutGlobalBucketAclForUserSpectraS3Response putGlobalBucketAclForUserSpectraS3(final PutGlobalBucketAclForUserSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPolicyAcl")
    @Action("CREATE")
    @Resource("DATA_POLICY_ACL")
    
    PutGlobalDataPolicyAclForGroupSpectraS3Response putGlobalDataPolicyAclForGroupSpectraS3(final PutGlobalDataPolicyAclForGroupSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPolicyAcl")
    @Action("CREATE")
    @Resource("DATA_POLICY_ACL")
    
    PutGlobalDataPolicyAclForUserSpectraS3Response putGlobalDataPolicyAclForUserSpectraS3(final PutGlobalDataPolicyAclForUserSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("BUCKET_ACL")
    
    DeleteBucketAclSpectraS3Response deleteBucketAclSpectraS3(final DeleteBucketAclSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("DATA_POLICY_ACL")
    
    DeleteDataPolicyAclSpectraS3Response deleteDataPolicyAclSpectraS3(final DeleteDataPolicyAclSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BucketAcl")
    @Action("SHOW")
    @Resource("BUCKET_ACL")
    
    GetBucketAclSpectraS3Response getBucketAclSpectraS3(final GetBucketAclSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BucketAclList")
    @Action("LIST")
    @Resource("BUCKET_ACL")
    
    GetBucketAclsSpectraS3Response getBucketAclsSpectraS3(final GetBucketAclsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPolicyAcl")
    @Action("SHOW")
    @Resource("DATA_POLICY_ACL")
    
    GetDataPolicyAclSpectraS3Response getDataPolicyAclSpectraS3(final GetDataPolicyAclSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPolicyAclList")
    @Action("LIST")
    @Resource("DATA_POLICY_ACL")
    
    GetDataPolicyAclsSpectraS3Response getDataPolicyAclsSpectraS3(final GetDataPolicyAclsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Bucket")
    @Action("CREATE")
    @Resource("BUCKET")
    
    PutBucketSpectraS3Response putBucketSpectraS3(final PutBucketSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("BUCKET")
    
    DeleteBucketSpectraS3Response deleteBucketSpectraS3(final DeleteBucketSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Bucket")
    @Action("SHOW")
    @Resource("BUCKET")
    
    GetBucketSpectraS3Response getBucketSpectraS3(final GetBucketSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BucketList")
    @Action("LIST")
    @Resource("BUCKET")
    
    GetBucketsSpectraS3Response getBucketsSpectraS3(final GetBucketsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Bucket")
    @Action("MODIFY")
    @Resource("BUCKET")
    
    ModifyBucketSpectraS3Response modifyBucketSpectraS3(final ModifyBucketSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("CACHE_FILESYSTEM")
    
    ForceFullCacheReclaimSpectraS3Response forceFullCacheReclaimSpectraS3(final ForceFullCacheReclaimSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("CacheFilesystem")
    @Action("SHOW")
    @Resource("CACHE_FILESYSTEM")
    
    GetCacheFilesystemSpectraS3Response getCacheFilesystemSpectraS3(final GetCacheFilesystemSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("CacheFilesystemList")
    @Action("LIST")
    @Resource("CACHE_FILESYSTEM")
    
    GetCacheFilesystemsSpectraS3Response getCacheFilesystemsSpectraS3(final GetCacheFilesystemsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("CacheInformation")
    @Action("LIST")
    @Resource("CACHE_STATE")
    
    GetCacheStateSpectraS3Response getCacheStateSpectraS3(final GetCacheStateSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("CacheFilesystem")
    @Action("MODIFY")
    @Resource("CACHE_FILESYSTEM")
    
    ModifyCacheFilesystemSpectraS3Response modifyCacheFilesystemSpectraS3(final ModifyCacheFilesystemSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("CapacitySummaryContainer")
    @Action("LIST")
    @Resource("CAPACITY_SUMMARY")
    
    GetBucketCapacitySummarySpectraS3Response getBucketCapacitySummarySpectraS3(final GetBucketCapacitySummarySpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("CapacitySummaryContainer")
    @Action("LIST")
    @Resource("CAPACITY_SUMMARY")
    
    GetStorageDomainCapacitySummarySpectraS3Response getStorageDomainCapacitySummarySpectraS3(final GetStorageDomainCapacitySummarySpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("CapacitySummaryContainer")
    @Action("LIST")
    @Resource("CAPACITY_SUMMARY")
    
    GetSystemCapacitySummarySpectraS3Response getSystemCapacitySummarySpectraS3(final GetSystemCapacitySummarySpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPathBackend")
    @Action("LIST")
    @Resource("DATA_PATH_BACKEND")
    
    GetDataPathBackendSpectraS3Response getDataPathBackendSpectraS3(final GetDataPathBackendSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BlobStoreTasksInformation")
    @Action("LIST")
    @Resource("BLOB_STORE_TASK")
    
    GetDataPlannerBlobStoreTasksSpectraS3Response getDataPlannerBlobStoreTasksSpectraS3(final GetDataPlannerBlobStoreTasksSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPathBackend")
    @Action("BULK_MODIFY")
    @Resource("DATA_PATH_BACKEND")
    
    ModifyDataPathBackendSpectraS3Response modifyDataPathBackendSpectraS3(final ModifyDataPathBackendSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPersistenceRule")
    @Action("CREATE")
    @Resource("DATA_PERSISTENCE_RULE")
    
    PutDataPersistenceRuleSpectraS3Response putDataPersistenceRuleSpectraS3(final PutDataPersistenceRuleSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPolicy")
    @Action("CREATE")
    @Resource("DATA_POLICY")
    
    PutDataPolicySpectraS3Response putDataPolicySpectraS3(final PutDataPolicySpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataReplicationRule")
    @Action("CREATE")
    @Resource("DATA_REPLICATION_RULE")
    
    PutDataReplicationRuleSpectraS3Response putDataReplicationRuleSpectraS3(final PutDataReplicationRuleSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("DATA_PERSISTENCE_RULE")
    
    DeleteDataPersistenceRuleSpectraS3Response deleteDataPersistenceRuleSpectraS3(final DeleteDataPersistenceRuleSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("DATA_POLICY")
    
    DeleteDataPolicySpectraS3Response deleteDataPolicySpectraS3(final DeleteDataPolicySpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("DATA_REPLICATION_RULE")
    
    DeleteDataReplicationRuleSpectraS3Response deleteDataReplicationRuleSpectraS3(final DeleteDataReplicationRuleSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPersistenceRule")
    @Action("SHOW")
    @Resource("DATA_PERSISTENCE_RULE")
    
    GetDataPersistenceRuleSpectraS3Response getDataPersistenceRuleSpectraS3(final GetDataPersistenceRuleSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPersistenceRuleList")
    @Action("LIST")
    @Resource("DATA_PERSISTENCE_RULE")
    
    GetDataPersistenceRulesSpectraS3Response getDataPersistenceRulesSpectraS3(final GetDataPersistenceRulesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPolicyList")
    @Action("LIST")
    @Resource("DATA_POLICY")
    
    GetDataPoliciesSpectraS3Response getDataPoliciesSpectraS3(final GetDataPoliciesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPolicy")
    @Action("SHOW")
    @Resource("DATA_POLICY")
    
    GetDataPolicySpectraS3Response getDataPolicySpectraS3(final GetDataPolicySpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataReplicationRule")
    @Action("SHOW")
    @Resource("DATA_REPLICATION_RULE")
    
    GetDataReplicationRuleSpectraS3Response getDataReplicationRuleSpectraS3(final GetDataReplicationRuleSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataReplicationRuleList")
    @Action("LIST")
    @Resource("DATA_REPLICATION_RULE")
    
    GetDataReplicationRulesSpectraS3Response getDataReplicationRulesSpectraS3(final GetDataReplicationRulesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPersistenceRule")
    @Action("MODIFY")
    @Resource("DATA_PERSISTENCE_RULE")
    
    ModifyDataPersistenceRuleSpectraS3Response modifyDataPersistenceRuleSpectraS3(final ModifyDataPersistenceRuleSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPolicy")
    @Action("MODIFY")
    @Resource("DATA_POLICY")
    
    ModifyDataPolicySpectraS3Response modifyDataPolicySpectraS3(final ModifyDataPolicySpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataReplicationRule")
    @Action("MODIFY")
    @Resource("DATA_REPLICATION_RULE")
    
    ModifyDataReplicationRuleSpectraS3Response modifyDataReplicationRuleSpectraS3(final ModifyDataReplicationRuleSpectraS3Request request)
            throws IOException;

    @Action("BULK_DELETE")
    @Resource("SUSPECT_BLOB_POOL")
    
    ClearSuspectBlobPoolsSpectraS3Response clearSuspectBlobPoolsSpectraS3(final ClearSuspectBlobPoolsSpectraS3Request request)
            throws IOException;

    @Action("BULK_DELETE")
    @Resource("SUSPECT_BLOB_TAPE")
    
    ClearSuspectBlobTapesSpectraS3Response clearSuspectBlobTapesSpectraS3(final ClearSuspectBlobTapesSpectraS3Request request)
            throws IOException;

    @Action("BULK_DELETE")
    @Resource("SUSPECT_BLOB_TARGET")
    
    ClearSuspectBlobTargetsSpectraS3Response clearSuspectBlobTargetsSpectraS3(final ClearSuspectBlobTargetsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DegradedBlobList")
    @Action("LIST")
    @Resource("DEGRADED_BLOB")
    
    GetDegradedBlobsSpectraS3Response getDegradedBlobsSpectraS3(final GetDegradedBlobsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BucketList")
    @Action("LIST")
    @Resource("DEGRADED_BUCKET")
    
    GetDegradedBucketsSpectraS3Response getDegradedBucketsSpectraS3(final GetDegradedBucketsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPersistenceRuleList")
    @Action("LIST")
    @Resource("DEGRADED_DATA_PERSISTENCE_RULE")
    
    GetDegradedDataPersistenceRulesSpectraS3Response getDegradedDataPersistenceRulesSpectraS3(final GetDegradedDataPersistenceRulesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataReplicationRuleList")
    @Action("LIST")
    @Resource("DEGRADED_DATA_REPLICATION_RULE")
    
    GetDegradedDataReplicationRulesSpectraS3Response getDegradedDataReplicationRulesSpectraS3(final GetDegradedDataReplicationRulesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("SuspectBlobPoolList")
    @Action("LIST")
    @Resource("SUSPECT_BLOB_POOL")
    
    GetSuspectBlobPoolsSpectraS3Response getSuspectBlobPoolsSpectraS3(final GetSuspectBlobPoolsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("SuspectBlobTapeList")
    @Action("LIST")
    @Resource("SUSPECT_BLOB_TAPE")
    
    GetSuspectBlobTapesSpectraS3Response getSuspectBlobTapesSpectraS3(final GetSuspectBlobTapesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("SuspectBlobTargetList")
    @Action("LIST")
    @Resource("SUSPECT_BLOB_TARGET")
    
    GetSuspectBlobTargetsSpectraS3Response getSuspectBlobTargetsSpectraS3(final GetSuspectBlobTargetsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BucketList")
    @Action("LIST")
    @Resource("SUSPECT_BUCKET")
    
    GetSuspectBucketsSpectraS3Response getSuspectBucketsSpectraS3(final GetSuspectBucketsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("PhysicalPlacement")
    @Action("LIST")
    @Resource("SUSPECT_OBJECT")
    
    GetSuspectObjectsSpectraS3Response getSuspectObjectsSpectraS3(final GetSuspectObjectsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BulkObjectList")
    @Action("LIST")
    @Resource("SUSPECT_OBJECT")
    
    GetSuspectObjectsWithFullDetailsSpectraS3Response getSuspectObjectsWithFullDetailsSpectraS3(final GetSuspectObjectsWithFullDetailsSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("SUSPECT_BLOB_POOL")
    
    MarkSuspectBlobPoolsAsDegradedSpectraS3Response markSuspectBlobPoolsAsDegradedSpectraS3(final MarkSuspectBlobPoolsAsDegradedSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("SUSPECT_BLOB_TAPE")
    
    MarkSuspectBlobTapesAsDegradedSpectraS3Response markSuspectBlobTapesAsDegradedSpectraS3(final MarkSuspectBlobTapesAsDegradedSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("SUSPECT_BLOB_TARGET")
    
    MarkSuspectBlobTargetsAsDegradedSpectraS3Response markSuspectBlobTargetsAsDegradedSpectraS3(final MarkSuspectBlobTargetsAsDegradedSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("GroupMember")
    @Action("CREATE")
    @Resource("GROUP_MEMBER")
    
    PutGroupGroupMemberSpectraS3Response putGroupGroupMemberSpectraS3(final PutGroupGroupMemberSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Group")
    @Action("CREATE")
    @Resource("GROUP")
    
    PutGroupSpectraS3Response putGroupSpectraS3(final PutGroupSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("GroupMember")
    @Action("CREATE")
    @Resource("GROUP_MEMBER")
    
    PutUserGroupMemberSpectraS3Response putUserGroupMemberSpectraS3(final PutUserGroupMemberSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("GROUP_MEMBER")
    
    DeleteGroupMemberSpectraS3Response deleteGroupMemberSpectraS3(final DeleteGroupMemberSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("GROUP")
    
    DeleteGroupSpectraS3Response deleteGroupSpectraS3(final DeleteGroupSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("GroupMember")
    @Action("SHOW")
    @Resource("GROUP_MEMBER")
    
    GetGroupMemberSpectraS3Response getGroupMemberSpectraS3(final GetGroupMemberSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("GroupMemberList")
    @Action("LIST")
    @Resource("GROUP_MEMBER")
    
    GetGroupMembersSpectraS3Response getGroupMembersSpectraS3(final GetGroupMembersSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Group")
    @Action("SHOW")
    @Resource("GROUP")
    
    GetGroupSpectraS3Response getGroupSpectraS3(final GetGroupSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("GroupList")
    @Action("LIST")
    @Resource("GROUP")
    
    GetGroupsSpectraS3Response getGroupsSpectraS3(final GetGroupsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Group")
    @Action("MODIFY")
    @Resource("GROUP")
    
    ModifyGroupSpectraS3Response modifyGroupSpectraS3(final ModifyGroupSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Group")
    @Action("MODIFY")
    @Resource("GROUP")
    
    VerifyUserIsMemberOfGroupSpectraS3Response verifyUserIsMemberOfGroupSpectraS3(final VerifyUserIsMemberOfGroupSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Objects")
    @Action("MODIFY")
    @Resource("JOB_CHUNK")
    
    AllocateJobChunkSpectraS3Response allocateJobChunkSpectraS3(final AllocateJobChunkSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("ACTIVE_JOB")
    
    CancelActiveJobSpectraS3Response cancelActiveJobSpectraS3(final CancelActiveJobSpectraS3Request request)
            throws IOException;

    @Action("BULK_DELETE")
    @Resource("ACTIVE_JOB")
    
    CancelAllActiveJobsSpectraS3Response cancelAllActiveJobsSpectraS3(final CancelAllActiveJobsSpectraS3Request request)
            throws IOException;

    @Action("BULK_DELETE")
    @Resource("JOB")
    
    CancelAllJobsSpectraS3Response cancelAllJobsSpectraS3(final CancelAllJobsSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("JOB")
    
    CancelJobSpectraS3Response cancelJobSpectraS3(final CancelJobSpectraS3Request request)
            throws IOException;

    @Action("BULK_DELETE")
    @Resource("CANCELED_JOB")
    
    ClearAllCanceledJobsSpectraS3Response clearAllCanceledJobsSpectraS3(final ClearAllCanceledJobsSpectraS3Request request)
            throws IOException;

    @Action("BULK_DELETE")
    @Resource("COMPLETED_JOB")
    
    ClearAllCompletedJobsSpectraS3Response clearAllCompletedJobsSpectraS3(final ClearAllCompletedJobsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("MasterObjectList")
    @Action("MODIFY")
    @Resource("BUCKET")
    
    GetBulkJobSpectraS3Response getBulkJobSpectraS3(final GetBulkJobSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("MasterObjectList")
    @Action("MODIFY")
    @Resource("BUCKET")
    
    PutBulkJobSpectraS3Response putBulkJobSpectraS3(final PutBulkJobSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("MasterObjectList")
    @Action("MODIFY")
    @Resource("BUCKET")
    
    VerifyBulkJobSpectraS3Response verifyBulkJobSpectraS3(final VerifyBulkJobSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("ActiveJob")
    @Action("SHOW")
    @Resource("ACTIVE_JOB")
    
    GetActiveJobSpectraS3Response getActiveJobSpectraS3(final GetActiveJobSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("ActiveJobList")
    @Action("LIST")
    @Resource("ACTIVE_JOB")
    
    GetActiveJobsSpectraS3Response getActiveJobsSpectraS3(final GetActiveJobsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("CanceledJob")
    @Action("SHOW")
    @Resource("CANCELED_JOB")
    
    GetCanceledJobSpectraS3Response getCanceledJobSpectraS3(final GetCanceledJobSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("CanceledJobList")
    @Action("LIST")
    @Resource("CANCELED_JOB")
    
    GetCanceledJobsSpectraS3Response getCanceledJobsSpectraS3(final GetCanceledJobsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("CompletedJob")
    @Action("SHOW")
    @Resource("COMPLETED_JOB")
    
    GetCompletedJobSpectraS3Response getCompletedJobSpectraS3(final GetCompletedJobSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("CompletedJobList")
    @Action("LIST")
    @Resource("COMPLETED_JOB")
    
    GetCompletedJobsSpectraS3Response getCompletedJobsSpectraS3(final GetCompletedJobsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("JobChunk")
    @Action("SHOW")
    @Resource("JOB_CHUNK_DAO")
    
    GetJobChunkDaoSpectraS3Response getJobChunkDaoSpectraS3(final GetJobChunkDaoSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Objects")
    @Action("SHOW")
    @Resource("JOB_CHUNK")
    
    GetJobChunkSpectraS3Response getJobChunkSpectraS3(final GetJobChunkSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("MasterObjectList")
    @Action("LIST")
    @Resource("JOB_CHUNK")
    
    GetJobChunksReadyForClientProcessingSpectraS3Response getJobChunksReadyForClientProcessingSpectraS3(final GetJobChunksReadyForClientProcessingSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("MasterObjectList")
    @Action("SHOW")
    @Resource("JOB")
    
    GetJobSpectraS3Response getJobSpectraS3(final GetJobSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("String")
    @Action("SHOW")
    @Resource("JOB")
    
    GetJobToReplicateSpectraS3Response getJobToReplicateSpectraS3(final GetJobToReplicateSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("JobList")
    @Action("LIST")
    @Resource("JOB")
    
    GetJobsSpectraS3Response getJobsSpectraS3(final GetJobsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("MasterObjectList")
    @Action("MODIFY")
    @Resource("ACTIVE_JOB")
    
    ModifyActiveJobSpectraS3Response modifyActiveJobSpectraS3(final ModifyActiveJobSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("MasterObjectList")
    @Action("MODIFY")
    @Resource("JOB")
    
    ModifyJobSpectraS3Response modifyJobSpectraS3(final ModifyJobSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("MasterObjectList")
    @Action("MODIFY")
    @Resource("BUCKET")
    
    ReplicatePutJobSpectraS3Response replicatePutJobSpectraS3(final ReplicatePutJobSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("ACTIVE_JOB")
    
    TruncateActiveJobSpectraS3Response truncateActiveJobSpectraS3(final TruncateActiveJobSpectraS3Request request)
            throws IOException;

    @Action("BULK_DELETE")
    @Resource("ACTIVE_JOB")
    
    TruncateAllActiveJobsSpectraS3Response truncateAllActiveJobsSpectraS3(final TruncateAllActiveJobsSpectraS3Request request)
            throws IOException;

    @Action("BULK_DELETE")
    @Resource("JOB")
    
    TruncateAllJobsSpectraS3Response truncateAllJobsSpectraS3(final TruncateAllJobsSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("JOB")
    
    TruncateJobSpectraS3Response truncateJobSpectraS3(final TruncateJobSpectraS3Request request)
            throws IOException;

    @Action("MODIFY")
    @Resource("BUCKET")
    
    VerifySafeToCreatePutJobSpectraS3Response verifySafeToCreatePutJobSpectraS3(final VerifySafeToCreatePutJobSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Node")
    @Action("SHOW")
    @Resource("NODE")
    
    GetNodeSpectraS3Response getNodeSpectraS3(final GetNodeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("NodeList")
    @Action("LIST")
    @Resource("NODE")
    
    GetNodesSpectraS3Response getNodesSpectraS3(final GetNodesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Node")
    @Action("MODIFY")
    @Resource("NODE")
    
    ModifyNodeSpectraS3Response modifyNodeSpectraS3(final ModifyNodeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Ds3TargetFailureNotificationRegistration")
    @Action("CREATE")
    @Resource("DS3_TARGET_FAILURE_NOTIFICATION_REGISTRATION")
    
    PutDs3TargetFailureNotificationRegistrationSpectraS3Response putDs3TargetFailureNotificationRegistrationSpectraS3(final PutDs3TargetFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("JobCompletedNotificationRegistration")
    @Action("CREATE")
    @Resource("JOB_COMPLETED_NOTIFICATION_REGISTRATION")
    
    PutJobCompletedNotificationRegistrationSpectraS3Response putJobCompletedNotificationRegistrationSpectraS3(final PutJobCompletedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("JobCreatedNotificationRegistration")
    @Action("CREATE")
    @Resource("JOB_CREATED_NOTIFICATION_REGISTRATION")
    
    PutJobCreatedNotificationRegistrationSpectraS3Response putJobCreatedNotificationRegistrationSpectraS3(final PutJobCreatedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("JobCreationFailedNotificationRegistration")
    @Action("CREATE")
    @Resource("JOB_CREATION_FAILED_NOTIFICATION_REGISTRATION")
    
    PutJobCreationFailedNotificationRegistrationSpectraS3Response putJobCreationFailedNotificationRegistrationSpectraS3(final PutJobCreationFailedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("S3ObjectCachedNotificationRegistration")
    @Action("CREATE")
    @Resource("OBJECT_CACHED_NOTIFICATION_REGISTRATION")
    
    PutObjectCachedNotificationRegistrationSpectraS3Response putObjectCachedNotificationRegistrationSpectraS3(final PutObjectCachedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("S3ObjectLostNotificationRegistration")
    @Action("CREATE")
    @Resource("OBJECT_LOST_NOTIFICATION_REGISTRATION")
    
    PutObjectLostNotificationRegistrationSpectraS3Response putObjectLostNotificationRegistrationSpectraS3(final PutObjectLostNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("S3ObjectPersistedNotificationRegistration")
    @Action("CREATE")
    @Resource("OBJECT_PERSISTED_NOTIFICATION_REGISTRATION")
    
    PutObjectPersistedNotificationRegistrationSpectraS3Response putObjectPersistedNotificationRegistrationSpectraS3(final PutObjectPersistedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("PoolFailureNotificationRegistration")
    @Action("CREATE")
    @Resource("POOL_FAILURE_NOTIFICATION_REGISTRATION")
    
    PutPoolFailureNotificationRegistrationSpectraS3Response putPoolFailureNotificationRegistrationSpectraS3(final PutPoolFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("StorageDomainFailureNotificationRegistration")
    @Action("CREATE")
    @Resource("STORAGE_DOMAIN_FAILURE_NOTIFICATION_REGISTRATION")
    
    PutStorageDomainFailureNotificationRegistrationSpectraS3Response putStorageDomainFailureNotificationRegistrationSpectraS3(final PutStorageDomainFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("SystemFailureNotificationRegistration")
    @Action("CREATE")
    @Resource("SYSTEM_FAILURE_NOTIFICATION_REGISTRATION")
    
    PutSystemFailureNotificationRegistrationSpectraS3Response putSystemFailureNotificationRegistrationSpectraS3(final PutSystemFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureNotificationRegistration")
    @Action("CREATE")
    @Resource("TAPE_FAILURE_NOTIFICATION_REGISTRATION")
    
    PutTapeFailureNotificationRegistrationSpectraS3Response putTapeFailureNotificationRegistrationSpectraS3(final PutTapeFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapePartitionFailureNotificationRegistration")
    @Action("CREATE")
    @Resource("TAPE_PARTITION_FAILURE_NOTIFICATION_REGISTRATION")
    
    PutTapePartitionFailureNotificationRegistrationSpectraS3Response putTapePartitionFailureNotificationRegistrationSpectraS3(final PutTapePartitionFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("DS3_TARGET_FAILURE_NOTIFICATION_REGISTRATION")
    
    DeleteDs3TargetFailureNotificationRegistrationSpectraS3Response deleteDs3TargetFailureNotificationRegistrationSpectraS3(final DeleteDs3TargetFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("JOB_COMPLETED_NOTIFICATION_REGISTRATION")
    
    DeleteJobCompletedNotificationRegistrationSpectraS3Response deleteJobCompletedNotificationRegistrationSpectraS3(final DeleteJobCompletedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("JOB_CREATED_NOTIFICATION_REGISTRATION")
    
    DeleteJobCreatedNotificationRegistrationSpectraS3Response deleteJobCreatedNotificationRegistrationSpectraS3(final DeleteJobCreatedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("JOB_CREATION_FAILED_NOTIFICATION_REGISTRATION")
    
    DeleteJobCreationFailedNotificationRegistrationSpectraS3Response deleteJobCreationFailedNotificationRegistrationSpectraS3(final DeleteJobCreationFailedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("OBJECT_CACHED_NOTIFICATION_REGISTRATION")
    
    DeleteObjectCachedNotificationRegistrationSpectraS3Response deleteObjectCachedNotificationRegistrationSpectraS3(final DeleteObjectCachedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("OBJECT_LOST_NOTIFICATION_REGISTRATION")
    
    DeleteObjectLostNotificationRegistrationSpectraS3Response deleteObjectLostNotificationRegistrationSpectraS3(final DeleteObjectLostNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("OBJECT_PERSISTED_NOTIFICATION_REGISTRATION")
    
    DeleteObjectPersistedNotificationRegistrationSpectraS3Response deleteObjectPersistedNotificationRegistrationSpectraS3(final DeleteObjectPersistedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("POOL_FAILURE_NOTIFICATION_REGISTRATION")
    
    DeletePoolFailureNotificationRegistrationSpectraS3Response deletePoolFailureNotificationRegistrationSpectraS3(final DeletePoolFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("STORAGE_DOMAIN_FAILURE_NOTIFICATION_REGISTRATION")
    
    DeleteStorageDomainFailureNotificationRegistrationSpectraS3Response deleteStorageDomainFailureNotificationRegistrationSpectraS3(final DeleteStorageDomainFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("SYSTEM_FAILURE_NOTIFICATION_REGISTRATION")
    
    DeleteSystemFailureNotificationRegistrationSpectraS3Response deleteSystemFailureNotificationRegistrationSpectraS3(final DeleteSystemFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("TAPE_FAILURE_NOTIFICATION_REGISTRATION")
    
    DeleteTapeFailureNotificationRegistrationSpectraS3Response deleteTapeFailureNotificationRegistrationSpectraS3(final DeleteTapeFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("TAPE_PARTITION_FAILURE_NOTIFICATION_REGISTRATION")
    
    DeleteTapePartitionFailureNotificationRegistrationSpectraS3Response deleteTapePartitionFailureNotificationRegistrationSpectraS3(final DeleteTapePartitionFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Ds3TargetFailureNotificationRegistration")
    @Action("SHOW")
    @Resource("DS3_TARGET_FAILURE_NOTIFICATION_REGISTRATION")
    
    GetDs3TargetFailureNotificationRegistrationSpectraS3Response getDs3TargetFailureNotificationRegistrationSpectraS3(final GetDs3TargetFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Ds3TargetFailureNotificationRegistrationList")
    @Action("LIST")
    @Resource("DS3_TARGET_FAILURE_NOTIFICATION_REGISTRATION")
    
    GetDs3TargetFailureNotificationRegistrationsSpectraS3Response getDs3TargetFailureNotificationRegistrationsSpectraS3(final GetDs3TargetFailureNotificationRegistrationsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("JobCompletedNotificationRegistration")
    @Action("SHOW")
    @Resource("JOB_COMPLETED_NOTIFICATION_REGISTRATION")
    
    GetJobCompletedNotificationRegistrationSpectraS3Response getJobCompletedNotificationRegistrationSpectraS3(final GetJobCompletedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("JobCompletedNotificationRegistrationList")
    @Action("LIST")
    @Resource("JOB_COMPLETED_NOTIFICATION_REGISTRATION")
    
    GetJobCompletedNotificationRegistrationsSpectraS3Response getJobCompletedNotificationRegistrationsSpectraS3(final GetJobCompletedNotificationRegistrationsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("JobCreatedNotificationRegistration")
    @Action("SHOW")
    @Resource("JOB_CREATED_NOTIFICATION_REGISTRATION")
    
    GetJobCreatedNotificationRegistrationSpectraS3Response getJobCreatedNotificationRegistrationSpectraS3(final GetJobCreatedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("JobCreatedNotificationRegistrationList")
    @Action("LIST")
    @Resource("JOB_CREATED_NOTIFICATION_REGISTRATION")
    
    GetJobCreatedNotificationRegistrationsSpectraS3Response getJobCreatedNotificationRegistrationsSpectraS3(final GetJobCreatedNotificationRegistrationsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("JobCreationFailedNotificationRegistration")
    @Action("SHOW")
    @Resource("JOB_CREATION_FAILED_NOTIFICATION_REGISTRATION")
    
    GetJobCreationFailedNotificationRegistrationSpectraS3Response getJobCreationFailedNotificationRegistrationSpectraS3(final GetJobCreationFailedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("JobCreationFailedNotificationRegistrationList")
    @Action("LIST")
    @Resource("JOB_CREATION_FAILED_NOTIFICATION_REGISTRATION")
    
    GetJobCreationFailedNotificationRegistrationsSpectraS3Response getJobCreationFailedNotificationRegistrationsSpectraS3(final GetJobCreationFailedNotificationRegistrationsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("S3ObjectCachedNotificationRegistration")
    @Action("SHOW")
    @Resource("OBJECT_CACHED_NOTIFICATION_REGISTRATION")
    
    GetObjectCachedNotificationRegistrationSpectraS3Response getObjectCachedNotificationRegistrationSpectraS3(final GetObjectCachedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("S3ObjectCachedNotificationRegistrationList")
    @Action("LIST")
    @Resource("OBJECT_CACHED_NOTIFICATION_REGISTRATION")
    
    GetObjectCachedNotificationRegistrationsSpectraS3Response getObjectCachedNotificationRegistrationsSpectraS3(final GetObjectCachedNotificationRegistrationsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("S3ObjectLostNotificationRegistration")
    @Action("SHOW")
    @Resource("OBJECT_LOST_NOTIFICATION_REGISTRATION")
    
    GetObjectLostNotificationRegistrationSpectraS3Response getObjectLostNotificationRegistrationSpectraS3(final GetObjectLostNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("S3ObjectLostNotificationRegistrationList")
    @Action("LIST")
    @Resource("OBJECT_LOST_NOTIFICATION_REGISTRATION")
    
    GetObjectLostNotificationRegistrationsSpectraS3Response getObjectLostNotificationRegistrationsSpectraS3(final GetObjectLostNotificationRegistrationsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("S3ObjectPersistedNotificationRegistration")
    @Action("SHOW")
    @Resource("OBJECT_PERSISTED_NOTIFICATION_REGISTRATION")
    
    GetObjectPersistedNotificationRegistrationSpectraS3Response getObjectPersistedNotificationRegistrationSpectraS3(final GetObjectPersistedNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("S3ObjectPersistedNotificationRegistrationList")
    @Action("LIST")
    @Resource("OBJECT_PERSISTED_NOTIFICATION_REGISTRATION")
    
    GetObjectPersistedNotificationRegistrationsSpectraS3Response getObjectPersistedNotificationRegistrationsSpectraS3(final GetObjectPersistedNotificationRegistrationsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("PoolFailureNotificationRegistration")
    @Action("SHOW")
    @Resource("POOL_FAILURE_NOTIFICATION_REGISTRATION")
    
    GetPoolFailureNotificationRegistrationSpectraS3Response getPoolFailureNotificationRegistrationSpectraS3(final GetPoolFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("PoolFailureNotificationRegistrationList")
    @Action("LIST")
    @Resource("POOL_FAILURE_NOTIFICATION_REGISTRATION")
    
    GetPoolFailureNotificationRegistrationsSpectraS3Response getPoolFailureNotificationRegistrationsSpectraS3(final GetPoolFailureNotificationRegistrationsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("StorageDomainFailureNotificationRegistration")
    @Action("SHOW")
    @Resource("STORAGE_DOMAIN_FAILURE_NOTIFICATION_REGISTRATION")
    
    GetStorageDomainFailureNotificationRegistrationSpectraS3Response getStorageDomainFailureNotificationRegistrationSpectraS3(final GetStorageDomainFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("StorageDomainFailureNotificationRegistrationList")
    @Action("LIST")
    @Resource("STORAGE_DOMAIN_FAILURE_NOTIFICATION_REGISTRATION")
    
    GetStorageDomainFailureNotificationRegistrationsSpectraS3Response getStorageDomainFailureNotificationRegistrationsSpectraS3(final GetStorageDomainFailureNotificationRegistrationsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("SystemFailureNotificationRegistration")
    @Action("SHOW")
    @Resource("SYSTEM_FAILURE_NOTIFICATION_REGISTRATION")
    
    GetSystemFailureNotificationRegistrationSpectraS3Response getSystemFailureNotificationRegistrationSpectraS3(final GetSystemFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("SystemFailureNotificationRegistrationList")
    @Action("LIST")
    @Resource("SYSTEM_FAILURE_NOTIFICATION_REGISTRATION")
    
    GetSystemFailureNotificationRegistrationsSpectraS3Response getSystemFailureNotificationRegistrationsSpectraS3(final GetSystemFailureNotificationRegistrationsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureNotificationRegistration")
    @Action("SHOW")
    @Resource("TAPE_FAILURE_NOTIFICATION_REGISTRATION")
    
    GetTapeFailureNotificationRegistrationSpectraS3Response getTapeFailureNotificationRegistrationSpectraS3(final GetTapeFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureNotificationRegistrationList")
    @Action("LIST")
    @Resource("TAPE_FAILURE_NOTIFICATION_REGISTRATION")
    
    GetTapeFailureNotificationRegistrationsSpectraS3Response getTapeFailureNotificationRegistrationsSpectraS3(final GetTapeFailureNotificationRegistrationsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapePartitionFailureNotificationRegistration")
    @Action("SHOW")
    @Resource("TAPE_PARTITION_FAILURE_NOTIFICATION_REGISTRATION")
    
    GetTapePartitionFailureNotificationRegistrationSpectraS3Response getTapePartitionFailureNotificationRegistrationSpectraS3(final GetTapePartitionFailureNotificationRegistrationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapePartitionFailureNotificationRegistrationList")
    @Action("LIST")
    @Resource("TAPE_PARTITION_FAILURE_NOTIFICATION_REGISTRATION")
    
    GetTapePartitionFailureNotificationRegistrationsSpectraS3Response getTapePartitionFailureNotificationRegistrationsSpectraS3(final GetTapePartitionFailureNotificationRegistrationsSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("FOLDER")
    
    DeleteFolderRecursivelySpectraS3Response deleteFolderRecursivelySpectraS3(final DeleteFolderRecursivelySpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("String")
    @Action("LIST")
    @Resource("BLOB_PERSISTENCE")
    
    GetBlobPersistenceSpectraS3Response getBlobPersistenceSpectraS3(final GetBlobPersistenceSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("S3Object")
    @Action("SHOW")
    @Resource("OBJECT")
    
    GetObjectDetailsSpectraS3Response getObjectDetailsSpectraS3(final GetObjectDetailsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("S3ObjectList")
    @Action("LIST")
    @Resource("OBJECT")
    
    GetObjectsDetailsSpectraS3Response getObjectsDetailsSpectraS3(final GetObjectsDetailsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DetailedS3ObjectList")
    @Action("LIST")
    @Resource("OBJECT")
    
    GetObjectsWithFullDetailsSpectraS3Response getObjectsWithFullDetailsSpectraS3(final GetObjectsWithFullDetailsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("PhysicalPlacement")
    @Action("MODIFY")
    @Resource("BUCKET")
    
    GetPhysicalPlacementForObjectsSpectraS3Response getPhysicalPlacementForObjectsSpectraS3(final GetPhysicalPlacementForObjectsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BulkObjectList")
    @Action("MODIFY")
    @Resource("BUCKET")
    
    GetPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response getPhysicalPlacementForObjectsWithFullDetailsSpectraS3(final GetPhysicalPlacementForObjectsWithFullDetailsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("PhysicalPlacement")
    @Action("SHOW")
    @Resource("BUCKET")
    
    VerifyPhysicalPlacementForObjectsSpectraS3Response verifyPhysicalPlacementForObjectsSpectraS3(final VerifyPhysicalPlacementForObjectsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BulkObjectList")
    @Action("SHOW")
    @Resource("BUCKET")
    
    VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Response verifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3(final VerifyPhysicalPlacementForObjectsWithFullDetailsSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("POOL")
    
    CancelImportOnAllPoolsSpectraS3Response cancelImportOnAllPoolsSpectraS3(final CancelImportOnAllPoolsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Pool")
    @Action("MODIFY")
    @Resource("POOL")
    
    CancelImportPoolSpectraS3Response cancelImportPoolSpectraS3(final CancelImportPoolSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("POOL")
    
    CompactAllPoolsSpectraS3Response compactAllPoolsSpectraS3(final CompactAllPoolsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Pool")
    @Action("MODIFY")
    @Resource("POOL")
    
    CompactPoolSpectraS3Response compactPoolSpectraS3(final CompactPoolSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("PoolPartition")
    @Action("CREATE")
    @Resource("POOL_PARTITION")
    
    PutPoolPartitionSpectraS3Response putPoolPartitionSpectraS3(final PutPoolPartitionSpectraS3Request request)
            throws IOException;

    @Action("MODIFY")
    @Resource("POOL")
    
    DeallocatePoolSpectraS3Response deallocatePoolSpectraS3(final DeallocatePoolSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("POOL")
    
    DeletePermanentlyLostPoolSpectraS3Response deletePermanentlyLostPoolSpectraS3(final DeletePermanentlyLostPoolSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("POOL_FAILURE")
    
    DeletePoolFailureSpectraS3Response deletePoolFailureSpectraS3(final DeletePoolFailureSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("POOL_PARTITION")
    
    DeletePoolPartitionSpectraS3Response deletePoolPartitionSpectraS3(final DeletePoolPartitionSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("POOL_ENVIRONMENT")
    
    ForcePoolEnvironmentRefreshSpectraS3Response forcePoolEnvironmentRefreshSpectraS3(final ForcePoolEnvironmentRefreshSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("POOL")
    
    FormatAllForeignPoolsSpectraS3Response formatAllForeignPoolsSpectraS3(final FormatAllForeignPoolsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Pool")
    @Action("MODIFY")
    @Resource("POOL")
    
    FormatForeignPoolSpectraS3Response formatForeignPoolSpectraS3(final FormatForeignPoolSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BulkObjectList")
    @Action("SHOW")
    @Resource("POOL")
    
    GetBlobsOnPoolSpectraS3Response getBlobsOnPoolSpectraS3(final GetBlobsOnPoolSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("PoolFailureList")
    @Action("LIST")
    @Resource("POOL_FAILURE")
    
    GetPoolFailuresSpectraS3Response getPoolFailuresSpectraS3(final GetPoolFailuresSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("PoolPartition")
    @Action("SHOW")
    @Resource("POOL_PARTITION")
    
    GetPoolPartitionSpectraS3Response getPoolPartitionSpectraS3(final GetPoolPartitionSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("PoolPartitionList")
    @Action("LIST")
    @Resource("POOL_PARTITION")
    
    GetPoolPartitionsSpectraS3Response getPoolPartitionsSpectraS3(final GetPoolPartitionsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Pool")
    @Action("SHOW")
    @Resource("POOL")
    
    GetPoolSpectraS3Response getPoolSpectraS3(final GetPoolSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("PoolList")
    @Action("LIST")
    @Resource("POOL")
    
    GetPoolsSpectraS3Response getPoolsSpectraS3(final GetPoolsSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("POOL")
    
    ImportAllPoolsSpectraS3Response importAllPoolsSpectraS3(final ImportAllPoolsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Pool")
    @Action("MODIFY")
    @Resource("POOL")
    
    ImportPoolSpectraS3Response importPoolSpectraS3(final ImportPoolSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("POOL")
    
    ModifyAllPoolsSpectraS3Response modifyAllPoolsSpectraS3(final ModifyAllPoolsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("PoolPartition")
    @Action("MODIFY")
    @Resource("POOL_PARTITION")
    
    ModifyPoolPartitionSpectraS3Response modifyPoolPartitionSpectraS3(final ModifyPoolPartitionSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Pool")
    @Action("MODIFY")
    @Resource("POOL")
    
    ModifyPoolSpectraS3Response modifyPoolSpectraS3(final ModifyPoolSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("POOL")
    
    VerifyAllPoolsSpectraS3Response verifyAllPoolsSpectraS3(final VerifyAllPoolsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Pool")
    @Action("MODIFY")
    @Resource("POOL")
    
    VerifyPoolSpectraS3Response verifyPoolSpectraS3(final VerifyPoolSpectraS3Request request)
            throws IOException;

    @Action("MODIFY")
    @Resource("STORAGE_DOMAIN")
    
    ConvertStorageDomainToDs3TargetSpectraS3Response convertStorageDomainToDs3TargetSpectraS3(final ConvertStorageDomainToDs3TargetSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("StorageDomainMember")
    @Action("CREATE")
    @Resource("STORAGE_DOMAIN_MEMBER")
    
    PutPoolStorageDomainMemberSpectraS3Response putPoolStorageDomainMemberSpectraS3(final PutPoolStorageDomainMemberSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("StorageDomain")
    @Action("CREATE")
    @Resource("STORAGE_DOMAIN")
    
    PutStorageDomainSpectraS3Response putStorageDomainSpectraS3(final PutStorageDomainSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("StorageDomainMember")
    @Action("CREATE")
    @Resource("STORAGE_DOMAIN_MEMBER")
    
    PutTapeStorageDomainMemberSpectraS3Response putTapeStorageDomainMemberSpectraS3(final PutTapeStorageDomainMemberSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("STORAGE_DOMAIN_FAILURE")
    
    DeleteStorageDomainFailureSpectraS3Response deleteStorageDomainFailureSpectraS3(final DeleteStorageDomainFailureSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("STORAGE_DOMAIN_MEMBER")
    
    DeleteStorageDomainMemberSpectraS3Response deleteStorageDomainMemberSpectraS3(final DeleteStorageDomainMemberSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("STORAGE_DOMAIN")
    
    DeleteStorageDomainSpectraS3Response deleteStorageDomainSpectraS3(final DeleteStorageDomainSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("StorageDomainFailureList")
    @Action("LIST")
    @Resource("STORAGE_DOMAIN_FAILURE")
    
    GetStorageDomainFailuresSpectraS3Response getStorageDomainFailuresSpectraS3(final GetStorageDomainFailuresSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("StorageDomainMember")
    @Action("SHOW")
    @Resource("STORAGE_DOMAIN_MEMBER")
    
    GetStorageDomainMemberSpectraS3Response getStorageDomainMemberSpectraS3(final GetStorageDomainMemberSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("StorageDomainMemberList")
    @Action("LIST")
    @Resource("STORAGE_DOMAIN_MEMBER")
    
    GetStorageDomainMembersSpectraS3Response getStorageDomainMembersSpectraS3(final GetStorageDomainMembersSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("StorageDomain")
    @Action("SHOW")
    @Resource("STORAGE_DOMAIN")
    
    GetStorageDomainSpectraS3Response getStorageDomainSpectraS3(final GetStorageDomainSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("StorageDomainList")
    @Action("LIST")
    @Resource("STORAGE_DOMAIN")
    
    GetStorageDomainsSpectraS3Response getStorageDomainsSpectraS3(final GetStorageDomainsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("StorageDomainMember")
    @Action("MODIFY")
    @Resource("STORAGE_DOMAIN_MEMBER")
    
    ModifyStorageDomainMemberSpectraS3Response modifyStorageDomainMemberSpectraS3(final ModifyStorageDomainMemberSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("StorageDomain")
    @Action("MODIFY")
    @Resource("STORAGE_DOMAIN")
    
    ModifyStorageDomainSpectraS3Response modifyStorageDomainSpectraS3(final ModifyStorageDomainSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("SystemFailureList")
    @Action("LIST")
    @Resource("SYSTEM_FAILURE")
    
    GetSystemFailuresSpectraS3Response getSystemFailuresSpectraS3(final GetSystemFailuresSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("SystemInformation")
    @Action("LIST")
    @Resource("SYSTEM_INFORMATION")
    
    GetSystemInformationSpectraS3Response getSystemInformationSpectraS3(final GetSystemInformationSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPathBackend")
    @Action("BULK_MODIFY")
    @Resource("INSTANCE_IDENTIFIER")
    
    ResetInstanceIdentifierSpectraS3Response resetInstanceIdentifierSpectraS3(final ResetInstanceIdentifierSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("HealthVerificationResult")
    @Action("LIST")
    @Resource("SYSTEM_HEALTH")
    
    VerifySystemHealthSpectraS3Response verifySystemHealthSpectraS3(final VerifySystemHealthSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureList")
    @Action("BULK_MODIFY")
    @Resource("TAPE")
    
    CancelEjectOnAllTapesSpectraS3Response cancelEjectOnAllTapesSpectraS3(final CancelEjectOnAllTapesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Tape")
    @Action("MODIFY")
    @Resource("TAPE")
    
    CancelEjectTapeSpectraS3Response cancelEjectTapeSpectraS3(final CancelEjectTapeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureList")
    @Action("BULK_MODIFY")
    @Resource("TAPE")
    
    CancelFormatOnAllTapesSpectraS3Response cancelFormatOnAllTapesSpectraS3(final CancelFormatOnAllTapesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Tape")
    @Action("MODIFY")
    @Resource("TAPE")
    
    CancelFormatTapeSpectraS3Response cancelFormatTapeSpectraS3(final CancelFormatTapeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureList")
    @Action("BULK_MODIFY")
    @Resource("TAPE")
    
    CancelImportOnAllTapesSpectraS3Response cancelImportOnAllTapesSpectraS3(final CancelImportOnAllTapesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Tape")
    @Action("MODIFY")
    @Resource("TAPE")
    
    CancelImportTapeSpectraS3Response cancelImportTapeSpectraS3(final CancelImportTapeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureList")
    @Action("BULK_MODIFY")
    @Resource("TAPE")
    
    CancelOnlineOnAllTapesSpectraS3Response cancelOnlineOnAllTapesSpectraS3(final CancelOnlineOnAllTapesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Tape")
    @Action("MODIFY")
    @Resource("TAPE")
    
    CancelOnlineTapeSpectraS3Response cancelOnlineTapeSpectraS3(final CancelOnlineTapeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureList")
    @Action("BULK_MODIFY")
    @Resource("TAPE")
    
    CancelVerifyOnAllTapesSpectraS3Response cancelVerifyOnAllTapesSpectraS3(final CancelVerifyOnAllTapesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Tape")
    @Action("MODIFY")
    @Resource("TAPE")
    
    CancelVerifyTapeSpectraS3Response cancelVerifyTapeSpectraS3(final CancelVerifyTapeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeDrive")
    @Action("MODIFY")
    @Resource("TAPE_DRIVE")
    
    CleanTapeDriveSpectraS3Response cleanTapeDriveSpectraS3(final CleanTapeDriveSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeDensityDirective")
    @Action("CREATE")
    @Resource("TAPE_DENSITY_DIRECTIVE")
    
    PutTapeDensityDirectiveSpectraS3Response putTapeDensityDirectiveSpectraS3(final PutTapeDensityDirectiveSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("TAPE")
    
    DeletePermanentlyLostTapeSpectraS3Response deletePermanentlyLostTapeSpectraS3(final DeletePermanentlyLostTapeSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("TAPE_DENSITY_DIRECTIVE")
    
    DeleteTapeDensityDirectiveSpectraS3Response deleteTapeDensityDirectiveSpectraS3(final DeleteTapeDensityDirectiveSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("TAPE_DRIVE")
    
    DeleteTapeDriveSpectraS3Response deleteTapeDriveSpectraS3(final DeleteTapeDriveSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("TAPE_FAILURE")
    
    DeleteTapeFailureSpectraS3Response deleteTapeFailureSpectraS3(final DeleteTapeFailureSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("TAPE_PARTITION_FAILURE")
    
    DeleteTapePartitionFailureSpectraS3Response deleteTapePartitionFailureSpectraS3(final DeleteTapePartitionFailureSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("TAPE_PARTITION")
    
    DeleteTapePartitionSpectraS3Response deleteTapePartitionSpectraS3(final DeleteTapePartitionSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureList")
    @Action("BULK_MODIFY")
    @Resource("TAPE")
    
    EjectAllTapesSpectraS3Response ejectAllTapesSpectraS3(final EjectAllTapesSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("TAPE")
    
    EjectStorageDomainBlobsSpectraS3Response ejectStorageDomainBlobsSpectraS3(final EjectStorageDomainBlobsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureList")
    @Action("BULK_MODIFY")
    @Resource("TAPE")
    
    EjectStorageDomainSpectraS3Response ejectStorageDomainSpectraS3(final EjectStorageDomainSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Tape")
    @Action("MODIFY")
    @Resource("TAPE")
    
    EjectTapeSpectraS3Response ejectTapeSpectraS3(final EjectTapeSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("TAPE_ENVIRONMENT")
    
    ForceTapeEnvironmentRefreshSpectraS3Response forceTapeEnvironmentRefreshSpectraS3(final ForceTapeEnvironmentRefreshSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureList")
    @Action("BULK_MODIFY")
    @Resource("TAPE")
    
    FormatAllTapesSpectraS3Response formatAllTapesSpectraS3(final FormatAllTapesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Tape")
    @Action("MODIFY")
    @Resource("TAPE")
    
    FormatTapeSpectraS3Response formatTapeSpectraS3(final FormatTapeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("BulkObjectList")
    @Action("SHOW")
    @Resource("TAPE")
    
    GetBlobsOnTapeSpectraS3Response getBlobsOnTapeSpectraS3(final GetBlobsOnTapeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeDensityDirective")
    @Action("SHOW")
    @Resource("TAPE_DENSITY_DIRECTIVE")
    
    GetTapeDensityDirectiveSpectraS3Response getTapeDensityDirectiveSpectraS3(final GetTapeDensityDirectiveSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeDensityDirectiveList")
    @Action("LIST")
    @Resource("TAPE_DENSITY_DIRECTIVE")
    
    GetTapeDensityDirectivesSpectraS3Response getTapeDensityDirectivesSpectraS3(final GetTapeDensityDirectivesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeDrive")
    @Action("SHOW")
    @Resource("TAPE_DRIVE")
    
    GetTapeDriveSpectraS3Response getTapeDriveSpectraS3(final GetTapeDriveSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeDriveList")
    @Action("LIST")
    @Resource("TAPE_DRIVE")
    
    GetTapeDrivesSpectraS3Response getTapeDrivesSpectraS3(final GetTapeDrivesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DetailedTapeFailureList")
    @Action("LIST")
    @Resource("TAPE_FAILURE")
    
    GetTapeFailuresSpectraS3Response getTapeFailuresSpectraS3(final GetTapeFailuresSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeLibraryList")
    @Action("LIST")
    @Resource("TAPE_LIBRARY")
    
    GetTapeLibrariesSpectraS3Response getTapeLibrariesSpectraS3(final GetTapeLibrariesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeLibrary")
    @Action("SHOW")
    @Resource("TAPE_LIBRARY")
    
    GetTapeLibrarySpectraS3Response getTapeLibrarySpectraS3(final GetTapeLibrarySpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapePartitionFailureList")
    @Action("LIST")
    @Resource("TAPE_PARTITION_FAILURE")
    
    GetTapePartitionFailuresSpectraS3Response getTapePartitionFailuresSpectraS3(final GetTapePartitionFailuresSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapePartition")
    @Action("SHOW")
    @Resource("TAPE_PARTITION")
    
    GetTapePartitionSpectraS3Response getTapePartitionSpectraS3(final GetTapePartitionSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DetailedTapePartition")
    @Action("SHOW")
    @Resource("TAPE_PARTITION")
    
    GetTapePartitionWithFullDetailsSpectraS3Response getTapePartitionWithFullDetailsSpectraS3(final GetTapePartitionWithFullDetailsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapePartitionList")
    @Action("LIST")
    @Resource("TAPE_PARTITION")
    
    GetTapePartitionsSpectraS3Response getTapePartitionsSpectraS3(final GetTapePartitionsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("NamedDetailedTapePartitionList")
    @Action("LIST")
    @Resource("TAPE_PARTITION")
    
    GetTapePartitionsWithFullDetailsSpectraS3Response getTapePartitionsWithFullDetailsSpectraS3(final GetTapePartitionsWithFullDetailsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Tape")
    @Action("SHOW")
    @Resource("TAPE")
    
    GetTapeSpectraS3Response getTapeSpectraS3(final GetTapeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeList")
    @Action("LIST")
    @Resource("TAPE")
    
    GetTapesSpectraS3Response getTapesSpectraS3(final GetTapesSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("TAPE")
    
    ImportAllTapesSpectraS3Response importAllTapesSpectraS3(final ImportAllTapesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Tape")
    @Action("MODIFY")
    @Resource("TAPE")
    
    ImportTapeSpectraS3Response importTapeSpectraS3(final ImportTapeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureList")
    @Action("BULK_MODIFY")
    @Resource("TAPE")
    
    InspectAllTapesSpectraS3Response inspectAllTapesSpectraS3(final InspectAllTapesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Tape")
    @Action("MODIFY")
    @Resource("TAPE")
    
    InspectTapeSpectraS3Response inspectTapeSpectraS3(final InspectTapeSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("TAPE_PARTITION")
    
    ModifyAllTapePartitionsSpectraS3Response modifyAllTapePartitionsSpectraS3(final ModifyAllTapePartitionsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapePartition")
    @Action("MODIFY")
    @Resource("TAPE_PARTITION")
    
    ModifyTapePartitionSpectraS3Response modifyTapePartitionSpectraS3(final ModifyTapePartitionSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Tape")
    @Action("MODIFY")
    @Resource("TAPE")
    
    ModifyTapeSpectraS3Response modifyTapeSpectraS3(final ModifyTapeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureList")
    @Action("BULK_MODIFY")
    @Resource("TAPE")
    
    OnlineAllTapesSpectraS3Response onlineAllTapesSpectraS3(final OnlineAllTapesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Tape")
    @Action("MODIFY")
    @Resource("TAPE")
    
    OnlineTapeSpectraS3Response onlineTapeSpectraS3(final OnlineTapeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("TapeFailureList")
    @Action("BULK_MODIFY")
    @Resource("TAPE")
    
    VerifyAllTapesSpectraS3Response verifyAllTapesSpectraS3(final VerifyAllTapesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Tape")
    @Action("MODIFY")
    @Resource("TAPE")
    
    VerifyTapeSpectraS3Response verifyTapeSpectraS3(final VerifyTapeSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Ds3TargetReadPreference")
    @Action("CREATE")
    @Resource("DS3_TARGET_READ_PREFERENCE")
    
    PutDs3TargetReadPreferenceSpectraS3Response putDs3TargetReadPreferenceSpectraS3(final PutDs3TargetReadPreferenceSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("DS3_TARGET_FAILURE")
    
    DeleteDs3TargetFailureSpectraS3Response deleteDs3TargetFailureSpectraS3(final DeleteDs3TargetFailureSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("DS3_TARGET_READ_PREFERENCE")
    
    DeleteDs3TargetReadPreferenceSpectraS3Response deleteDs3TargetReadPreferenceSpectraS3(final DeleteDs3TargetReadPreferenceSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("DS3_TARGET")
    
    DeleteDs3TargetSpectraS3Response deleteDs3TargetSpectraS3(final DeleteDs3TargetSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("TARGET_ENVIRONMENT")
    
    ForceTargetEnvironmentRefreshSpectraS3Response forceTargetEnvironmentRefreshSpectraS3(final ForceTargetEnvironmentRefreshSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("DataPolicyList")
    @Action("SHOW")
    @Resource("DS3_TARGET_DATA_POLICIES")
    
    GetDs3TargetDataPoliciesSpectraS3Response getDs3TargetDataPoliciesSpectraS3(final GetDs3TargetDataPoliciesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Ds3TargetFailureList")
    @Action("LIST")
    @Resource("DS3_TARGET_FAILURE")
    
    GetDs3TargetFailuresSpectraS3Response getDs3TargetFailuresSpectraS3(final GetDs3TargetFailuresSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Ds3TargetReadPreference")
    @Action("SHOW")
    @Resource("DS3_TARGET_READ_PREFERENCE")
    
    GetDs3TargetReadPreferenceSpectraS3Response getDs3TargetReadPreferenceSpectraS3(final GetDs3TargetReadPreferenceSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Ds3TargetReadPreferenceList")
    @Action("LIST")
    @Resource("DS3_TARGET_READ_PREFERENCE")
    
    GetDs3TargetReadPreferencesSpectraS3Response getDs3TargetReadPreferencesSpectraS3(final GetDs3TargetReadPreferencesSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Ds3Target")
    @Action("SHOW")
    @Resource("DS3_TARGET")
    
    GetDs3TargetSpectraS3Response getDs3TargetSpectraS3(final GetDs3TargetSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Ds3TargetList")
    @Action("LIST")
    @Resource("DS3_TARGET")
    
    GetDs3TargetsSpectraS3Response getDs3TargetsSpectraS3(final GetDs3TargetsSpectraS3Request request)
            throws IOException;

    @Action("BULK_MODIFY")
    @Resource("DS3_TARGET")
    
    ModifyAllDs3TargetsSpectraS3Response modifyAllDs3TargetsSpectraS3(final ModifyAllDs3TargetsSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Ds3Target")
    @Action("MODIFY")
    @Resource("DS3_TARGET")
    
    ModifyDs3TargetSpectraS3Response modifyDs3TargetSpectraS3(final ModifyDs3TargetSpectraS3Request request)
            throws IOException;

    @Action("MODIFY")
    @Resource("DS3_TARGET")
    
    PairBackRegisteredDs3TargetSpectraS3Response pairBackRegisteredDs3TargetSpectraS3(final PairBackRegisteredDs3TargetSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Ds3Target")
    @Action("CREATE")
    @Resource("DS3_TARGET")
    
    RegisterDs3TargetSpectraS3Response registerDs3TargetSpectraS3(final RegisterDs3TargetSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("Ds3Target")
    @Action("MODIFY")
    @Resource("DS3_TARGET")
    
    VerifyDs3TargetSpectraS3Response verifyDs3TargetSpectraS3(final VerifyDs3TargetSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("SpectraUser")
    @Action("CREATE")
    @Resource("USER")
    
    DelegateCreateUserSpectraS3Response delegateCreateUserSpectraS3(final DelegateCreateUserSpectraS3Request request)
            throws IOException;

    @Action("DELETE")
    @Resource("USER")
    
    DelegateDeleteUserSpectraS3Response delegateDeleteUserSpectraS3(final DelegateDeleteUserSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("SpectraUser")
    @Action("SHOW")
    @Resource("USER")
    
    GetUserSpectraS3Response getUserSpectraS3(final GetUserSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("SpectraUserList")
    @Action("LIST")
    @Resource("USER")
    
    GetUsersSpectraS3Response getUsersSpectraS3(final GetUsersSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("SpectraUser")
    @Action("MODIFY")
    @Resource("USER")
    
    ModifyUserSpectraS3Response modifyUserSpectraS3(final ModifyUserSpectraS3Request request)
            throws IOException;

    @ResponsePayloadModel("SpectraUser")
    @Action("MODIFY")
    @Resource("USER")
    
    RegenerateUserSecretKeySpectraS3Response regenerateUserSecretKeySpectraS3(final RegenerateUserSecretKeySpectraS3Request request)
            throws IOException;

    
    
    GetObjectResponse getObject(final GetObjectRequest request)
            throws IOException;


    Ds3Client newForNode(final JobNode node);
}