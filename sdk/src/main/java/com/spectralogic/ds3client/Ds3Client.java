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

import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.*;

import java.io.IOException;
import java.security.SignatureException;

/**
 * The main interface for communicating with a DS3 appliance.  All communication with a DS3 appliance should start with
 * this class.
 *
 * Here is an example showing how the Ds3Client interface is used to get a list of buckets from a remote DS3 appliance.
 *
 * <pre>
 *     {@code
 *     final Ds3Client client = Ds3ClientBuilder.create("ds3Endpoint:8080",
 *                                  new Credentials("accessKey", "secretKey")).build();
 *
 *     final GetServiceResponse response = client.getService(new GetServiceRequest());
 *
 *     for(final Bucket bucket: response.getResult().getBuckets()) {
 *         System.out.println(bucket.getName());
 *     }
 *     }
 * </pre>
 *
 * See {@link Ds3ClientBuilder} on what options can be used to create a Ds3Client instance.
 */
public interface Ds3Client {

    /**
     * Gets the list of buckets.
     * @param request The Service Request object used to customize the HTTP request, {@link com.spectralogic.ds3client.commands.GetServiceRequest}
     * @return The response object contains the list of buckets that the user has access to.
     * @throws IOException
     * @throws SignatureException
     */
    public abstract GetServiceResponse getService(GetServiceRequest request)
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
    public abstract GetBucketResponse getBucket(GetBucketRequest request)
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
    public abstract PutBucketResponse putBucket(PutBucketRequest request)
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
    public abstract HeadBucketResponse headBucket(HeadBucketRequest request)
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
    public abstract DeleteBucketResponse deleteBucket(
            DeleteBucketRequest request) throws IOException, SignatureException;

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
    public abstract DeleteObjectResponse deleteObject(
            DeleteObjectRequest request) throws IOException, SignatureException;

    /**
     * Get an object in a bucket from a DS3 endpoint
     * @param request The Get Object Request object used to customize the HTTP request.  The get object request object
     *                has some options for customizing the request.  See {@link GetObjectRequest} for the full list of
     *                options that can be configured.
     * @return The response object contains a stream that can be used to read the contents of the object from.  See
     *         {@link com.spectralogic.ds3client.commands.GetObjectResponse} for any other properties.
     * @throws IOException
     * @throws SignatureException
     */
    public abstract GetObjectResponse getObject(GetObjectRequest request)
            throws IOException, SignatureException;

    /**
     * Puts a new object to an existing bucket to a DS3 endpoint.  If the InputStream passed into the PutObjectRequest
     * is not seekable, meaning if InputStream.markSupported() returns false, the request will fail and throw a
     * {@link com.spectralogic.ds3client.networking.RequiresMarkSupportedException}.  If using a FileInputStream see
     * {@link com.spectralogic.ds3client.helpers.ResettableFileInputStream} to make seekable.
     * @param request The Put Object Request object used to customize the HTTP request.  The put object request object
     *                has some options for customizing the request.  See {@link PutObjectRequest} for the full list of
     *                options that can be configured.
     * @return The response object is returned primarily to be consistent with the rest of the API.  Additional data
     *         may be returned here in the future but nothing is currently.  See {@link PutObjectResponse} for the most
     *         up to date information on what is returned.
     * @throws IOException
     * @throws SignatureException
     */
    public abstract PutObjectResponse putObject(PutObjectRequest request)
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
    public abstract BulkGetResponse bulkGet(BulkGetRequest request)
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
    public abstract BulkPutResponse bulkPut(BulkPutRequest request)
            throws IOException, SignatureException;
}
