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

import java.io.Closeable;
import java.io.IOException;
import java.security.SignatureException;

/**
 * The main interface for communicating with a DS3 appliance.  All communication with a DS3 appliance starts with
 * this class.
 *
 * See {@link Ds3ClientBuilder} on what options can be used to create a Ds3Client instance.
 */
public interface Ds3Client extends Closeable {

    ConnectionDetails getConnectionDetails();

    /**
     * Gets the list of buckets.
     * @param request The Service Request object used to customize the HTTP request, {@link com.spectralogic.ds3client.commands.GetServiceRequest}
     * @return The response object contains the list of buckets that the user has access to.
     * @throws IOException
     * @throws SignatureException
     */
    GetServiceResponse getService(GetServiceRequest request)
            throws IOException, SignatureException;

    /**
     * Gets the list of objects in a bucket.
     * @param request The Get Bucket Request object used to customize the HTTP request.  The bucket request object has
     *                several options for customizing the request.  See {@link com.spectralogic.ds3client.commands.GetBucketRequest} for the full list of options
     *                that can be configured.
     * @return The response object contains the list of objects that a bucket contains.  There is some additional
     *         information that is returned which is used for pagination, {@link GetBucketResponse} for the full
     *         list of properties
     * that are returned.
     * @throws IOException
     * @throws SignatureException
     */
    GetBucketResponse getBucket(GetBucketRequest request)
            throws IOException, SignatureException;

    /**
     * Puts a new bucket to a DS3 endpoint
     * @param request The Put Bucket Request object used to customize the HTTP request.  The put bucket request object
     *                has some options for customizing the request.  See {@link PutBucketRequest} for the full list of
     *                options that can be configured.
     * @return The response object is returned primarily to be consistent with the rest of the API.  Additional data
     *         may be returned here in the future but nothing is currently.  See {@link PutBucketResponse} for the most
     *         up to date information on what is returned.
     * @throws IOException
     * @throws SignatureException
     */
    PutBucketResponse putBucket(PutBucketRequest request)
            throws IOException, SignatureException;

    /**
     * Performs a HTTP HEAD for a bucket.  The HEAD will return information about if the bucket exists, or if the user
     * has access to that bucket.
     * @param request The Head Bucket Request object used to customize the HTTP request.  See {@link HeadBucketRequest}.
     * @return The response object is returned and contains the status of the bucket.  See {@link HeadBucketResponse} for
     *         the full list of status that a bucket can be in.
     * @throws IOException
     * @throws SignatureException
     */
    HeadBucketResponse headBucket(HeadBucketRequest request)
            throws IOException, SignatureException;

    /**
     * Deletes a bucket from a DS3 endpoint.  <b>Note:</b> all objects must be deleted first before deleteBucket will
     * succeed.
     * @param request The Delete Bucket Request object used to customize the HTTP request.  The delete bucket request object
     *                has some options for customizing the request.  See {@link DeleteBucketRequest} for the full list of
     *                options that can be configured.
     * @return The response object is returned primarily to be consistent with the rest of the API.  Additional data
     *         may be returned here in the future but nothing is currently.  See {@link com.spectralogic.ds3client.commands.DeleteBucketResponse} for the most
     *         up to date information on what is returned.
     * @throws IOException
     * @throws SignatureException
     */
    DeleteBucketResponse deleteBucket(
            DeleteBucketRequest request) throws IOException, SignatureException;

    /**
     * Recursively deletes the specified folder from a DS3 endpoint.
     * @param request The Delete Folder Request object used to customize the HTTP request.  The put bucket request object
     *                has some options for customizing the request.  See {@link DeleteFolderRequest} for the full list of
     *                options that can be configured.
     * @return The response object is returned primarily to be consistent with the rest of the API.  Additional data
     *         may be returned here in the future but nothing is currently.  See {@link DeleteFolderResponse} for the most
     *         up to date information on what is returned.
     * @throws IOException
     * @throws SignatureException
     */
    DeleteFolderResponse deleteFolder(
            DeleteFolderRequest request) throws IOException, SignatureException;

    /**
     * Deletes an object in a bucket from a DS3 endpoint
     * @param request The Put Bucket Request object used to customize the HTTP request.  The put bucket request object
     *                has some options for customizing the request.  See {@link PutBucketRequest} for the full list of
     *                options that can be configured.
     * @return The response object is returned primarily to be consistent with the rest of the API.  Additional data
     *         may be returned here in the future but nothing is currently.  See {@link DeleteObjectResponse} for the most
     *         up to date information on what is returned.
     * @throws IOException
     * @throws SignatureException
     */
    DeleteObjectResponse deleteObject(DeleteObjectRequest request)
            throws IOException, SignatureException;

    DeleteMultipleObjectsResponse deleteMultipleObjects(DeleteMultipleObjectsRequest request)
            throws IOException, SignatureException;

    /**
     * Retrieves an object from DS3
     * @param request The Get Object Request object used to customize the HTTP request.  The get object request object
     *                has some options for customizing the request.  See {@link GetObjectRequest} for the full list of
     *                options that can be configured.
     * @return The response object contains a stream that can be used to read the contents of the object from.  See
     *         {@link com.spectralogic.ds3client.commands.GetObjectResponse} for any other properties.
     * @throws IOException
     * @throws SignatureException
     */
    GetObjectResponse getObject(GetObjectRequest request)
            throws IOException, SignatureException;

    /**
     * Puts a new object to an existing bucket.  The call will fail if the bucket does not exist.
     * @param request The Put Object Request object used to customize the HTTP request.  The put object request object
     *                has some options for customizing the request.  See {@link PutObjectRequest} for the full list of
     *                options that can be configured.
     * @return The response object is returned primarily to be consistent with the rest of the API.  Additional data
     *         may be returned here in the future but nothing is currently.  See {@link PutObjectResponse} for the most
     *         up to date information on what is returned.
     * @throws IOException
     * @throws SignatureException
     */
    PutObjectResponse putObject(PutObjectRequest request)
            throws IOException, SignatureException;

    /**
     * Performs an HTTP HEAD for an object.  If the object exists then a successful response is returned and any metadata
     * associated with the object.
     * @param request The Object to perform the HTTP HEAD for.
     * @return If successful a response is returned with any metadata that was assigned with the object was created.
     * @throws IOException
     * @throws SignatureException
     */
    HeadObjectResponse headObject(HeadObjectRequest request)
            throws IOException, SignatureException;

    /**
     * Primes the Ds3 appliance for a Bulk Get.  This does not perform the gets for each individual files.  See
     * {@link #getObject(GetObjectRequest)} for performing the get.
     * @param request The Bulk Get Request object used to customize the HTTP request.  The bulk get request object
     *                has some options for customizing the request.  See {@link BulkGetRequest} for the full list of
     *                options that can be configured.
     * @return The response object contains a list of lists of files to get from the DS3 endpoint.  Make sure that the
     *         files are gotten in the order specified in the response.
     * @throws IOException
     * @throws SignatureException
     */
    BulkGetResponse bulkGet(BulkGetRequest request)
            throws IOException, SignatureException;

    /**
     * Primes the Ds3 appliance for a Bulk Put.  This does not perform the puts for each individual files.  See
     * {@link #putObject(PutObjectRequest)} for performing the put.
     * @param request The Bulk Put Request object used to customize the HTTP request.  The bulk put request object
     *                has some options for customizing the request.  See {@link BulkPutRequest} for the full list of
     *                options that can be configured.
     * @return The response object contains a list of lists of files to put to the DS3 endpoint.  Make sure that the
     *         files are put in the order specified in the response.
     * @throws IOException
     * @throws SignatureException
     */
    BulkPutResponse bulkPut(BulkPutRequest request)
            throws IOException, SignatureException;

    /**
     * Requests that the server allocate space for a given ChunkId in a BulkPutResponse.
     * For best performance, run this call before putting objects for an object
     * list.
     * @return Whether the server responded with allocated, retry later, or not found.
     * @throws IOException
     * @throws SignatureException
     */
    AllocateJobChunkResponse allocateJobChunk(AllocateJobChunkRequest request)
            throws IOException, SignatureException;

    /**
     * Returns all of the chunks immediately available for get from the server
     * for a given JobId in a BulkGetResponse.
     * @return Whether the server responded with the available chunks, retry later, or not found.
     * @throws IOException
     * @throws SignatureException
     */
    GetAvailableJobChunksResponse getAvailableJobChunks(GetAvailableJobChunksRequest request)
            throws IOException, SignatureException;

    /**
     * Returns all of the jobs currently on the black pearl.
     * @return A list of jobs.
     * @throws IOException
     * @throws SignatureException
     */
    GetJobsResponse getJobs(GetJobsRequest request)
            throws IOException, SignatureException;

    /**
     * Returns details about a bulk job given a job id.
     * @return A master object list.
     * @throws IOException
     * @throws SignatureException
     */
    GetJobResponse getJob(GetJobRequest request)
            throws IOException, SignatureException;

    /**
     * Cancels an in-progress job.
     * @throws IOException
     * @throws SignatureException
     */
    CancelJobResponse cancelJob(CancelJobRequest request)
            throws IOException, SignatureException;

    /**
     * Updates the last modified date of a job and returns the job details.
     * @return A master object list.
     * @throws IOException
     * @throws SignatureException
     */
    ModifyJobResponse modifyJob(ModifyJobRequest request)
            throws IOException, SignatureException;

    DeleteTapeDriveResponse deleteTapeDrive(DeleteTapeDriveRequest request)
            throws IOException, SignatureException;

    DeleteTapePartitionResponse deleteTapePartition(DeleteTapePartitionRequest request)
            throws IOException, SignatureException;

    NotificationResponse createObjectCachedNotification(CreateObjectCachedNotificationRequest request)
            throws IOException, SignatureException;

    NotificationResponse getObjectCachedNotification(GetObjectCachedNotificationRequest request)
            throws IOException, SignatureException;

    DeleteNotificationResponse deleteObjectCachedNotification(DeleteObjectCachedNotificationRequest request)
            throws IOException, SignatureException;

    NotificationResponse createJobCompletedNotification(CreateJobCompletedNotificationRequest request)
            throws IOException, SignatureException;

    NotificationResponse getJobCompletedNotification(GetJobCompletedNotificationRequest request)
            throws IOException, SignatureException;

    DeleteNotificationResponse deleteJobCompleteNotification(DeleteJobCompletedNotificationRequest request)
            throws IOException, SignatureException;

    NotificationResponse createJobCreatedNotification(CreateJobCreatedNotificationRequest request)
            throws IOException, SignatureException;

    NotificationResponse getJobCreatedNotification(GetJobCreatedNotificationRequest request)
            throws IOException, SignatureException;

    DeleteNotificationResponse deleteJobCreatedNotification(DeleteJobCreatedNotificationRequest request)
            throws IOException, SignatureException;

    NotificationResponse createObjectLostNotification(CreateObjectLostNotificationRequest request)
            throws IOException, SignatureException;

    NotificationResponse getObjectLostNotification(GetObjectLostNotificationRequest request)
            throws IOException, SignatureException;

    DeleteNotificationResponse deleteObjectLostNotification(DeleteObjectLostNotificationRequest request)
            throws IOException, SignatureException;

    NotificationResponse createObjectPersistedNotification(CreateObjectPersistedNotificationRequest request)
            throws IOException, SignatureException;

    NotificationResponse getObjectPersistedNotification(GetObjectPersistedNotificationRequest request)
            throws IOException, SignatureException;

    DeleteNotificationResponse deleteObjectPersistedNotification(DeleteObjectPersistedNotificationRequest request)
            throws IOException, SignatureException;

    NotificationResponse createPartitionFailureNotification(CreatePartitionFailureNotificationRequest request)
            throws IOException, SignatureException;

    NotificationResponse getPartitionFailureNotification(GetPartitionFailureNotificationRequest request)
            throws IOException, SignatureException;

    DeleteNotificationResponse deletePartitionFailureNotification(DeletePartitionFailureNotificationRequest request)
            throws IOException, SignatureException;

    NotificationResponse createTapeFailureNotification(CreateTapeFailureNotificationRequest request)
            throws IOException, SignatureException;

    NotificationResponse getTapeFailureNotification(GetTapeFailureNotificationRequest request)
            throws IOException, SignatureException;

    DeleteNotificationResponse deleteTapeFailureNotification(DeleteTapeFailureNotificationRequest request)
            throws IOException, SignatureException;

    GetSystemHealthResponse getSystemHealth(GetSystemHealthRequest request)
            throws IOException, SignatureException;

    GetSystemInformationResponse getSystemInformation(GetSystemInformationRequest request)
            throws IOException, SignatureException;

    GetTapeLibrariesResponse getTapeLibraries(GetTapeLibrariesRequest request)
            throws IOException, SignatureException;

    GetTapeLibraryResponse getTapeLibrary(GetTapeLibraryRequest request)
            throws IOException, SignatureException;

    GetTapeDrivesResponse getTapeDrives(GetTapeDrivesRequest request)
            throws IOException, SignatureException;

    GetTapeDriveResponse getTapeDrive(GetTapeDriveRequest request)
            throws IOException, SignatureException;

    GetTapeFailureResponse getTapeFailure(GetTapeFailureRequest request)
            throws IOException, SignatureException;

    /**
     * Creates a new Ds3Client instance for the system pointed to by Node.
     */
    Ds3Client newForNode(Node node);
}
