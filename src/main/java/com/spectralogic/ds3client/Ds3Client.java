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
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SignatureException;

import com.spectralogic.ds3client.commands.BulkGetRequest;
import com.spectralogic.ds3client.commands.BulkGetResponse;
import com.spectralogic.ds3client.commands.BulkPutRequest;
import com.spectralogic.ds3client.commands.BulkPutResponse;
import com.spectralogic.ds3client.commands.DeleteBucketRequest;
import com.spectralogic.ds3client.commands.DeleteBucketResponse;
import com.spectralogic.ds3client.commands.DeleteObjectRequest;
import com.spectralogic.ds3client.commands.DeleteObjectResponse;
import com.spectralogic.ds3client.commands.GetBucketRequest;
import com.spectralogic.ds3client.commands.GetBucketResponse;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.commands.GetServiceRequest;
import com.spectralogic.ds3client.commands.GetServiceResponse;
import com.spectralogic.ds3client.commands.PutBucketRequest;
import com.spectralogic.ds3client.commands.PutBucketResponse;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.commands.PutObjectResponse;
import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.networking.NetworkClient;

/**
 * The main class for communicating with a DS3 appliance.  All communication with a DS3 appliance should start with
 * this class.
 *
 * Here is an example showing how the Ds3Client class is used to get a list of buckets from a remote DS3 appliance.
 *
 * <pre>
 *     {@code
 *     final Ds3Client client = Ds3Client.builder("ds3Endpoint:8080",
 *                                  new Credentials("accessKey", "secretKey")).build();
 *
 *     final GetServiceResponse response = client.getService(new GetServiceRequest());
 *
 *     for(final Bucket bucket: response.getResult().getBuckets()) {
 *         System.out.println(bucket.getName());
 *     }
 *     }
 * </pre>
 */
public class Ds3Client {

    /**
     * A Builder class used to create a Ds3Client instance.
     */
    public static class Builder implements com.spectralogic.ds3client.utils.Builder<Ds3Client> {

        final private String endpoint;
        final private Credentials credentials;

        private boolean secure = true;
        private URI proxy = null;
        private int retries = 5;

        private Builder(final String endpoint, final Credentials credentials) throws IllegalArgumentException {
            if (endpoint == null || endpoint.isEmpty()) {
                throw new IllegalArgumentException("Endpoint must be non empty");
            }
            if(credentials == null || !credentials.isValid()) {
                throw new IllegalArgumentException("Credentials must be filled out.");
            }
            this.endpoint = endpoint;
            this.credentials = credentials;
        }

        /**
         * Specifies if the library should use HTTP or HTTPS.  The default is HTTP.
         * @param secure True will use HTTPS, false will use HTTP.
         * @return The current builder.
         */
        public Builder withHttpSecure(final boolean secure) {
            this.secure = secure;
            return this;
        }

        /**
         * Sets a HTTP proxy.
         * @param proxy The endpoint of the HTTP proxy.
         * @return The current builder.
         * @throws IllegalArgumentException This will be thrown if the proxy endpoint is not a valid URI.
         */
        public Builder withProxy(final String proxy) throws IllegalArgumentException {
            try {
                final URI proxyUri;
                if(!proxy.startsWith("http")) {
                    throw new IllegalArgumentException("Invalid proxy format.  The web address must start with either http or https.");
                }
                proxyUri = new URI(proxy);

                this.proxy = proxyUri;
            } catch (final URISyntaxException e) {
                throw new IllegalArgumentException("Invalid proxy format.  Must be a web address.");
            }

            return this;
        }

        /**
         * Sets the number of retries the library will attempt to perform when it receives 307 redirects from a
         * DS3 appliance.  The default is 5.
         * @param retries The number of times the library should perform retries on 307.
         * @return The current builder.
         */
        public Builder withRedirectRetries(final int retries) {
            this.retries = retries;
            return this;
        }

        /**
         * Returns a new Ds3Client instance.
         */
        @Override
        public Ds3Client build() {
            final ConnectionDetailsImpl.Builder connBuilder = ConnectionDetailsImpl.builder(endpoint, credentials)
                .withProxy(proxy).withSecure(secure).withRedirectRetries(retries);

            final NetworkClient netClient = new NetworkClientImpl(connBuilder.build());
            return new Ds3Client(netClient);
        }
    }

    /**
     * Returns a Builder which is used to customize the behavior of the Ds3Client library.
     * @param endpoint The DS3 endpoint the library should connect to.
     * @param creds The {@link Credentials} used for connecting to a DS3 endpoint.
     * @return The Builder for the {@link Ds3Client} object.
     */
    public static Builder builder(final String endpoint, final Credentials creds) {
        return new Builder(endpoint, creds);
    }
    
    private final NetworkClient netClient;

    Ds3Client(final NetworkClient netClient) {
        this.netClient = netClient;
    }

    NetworkClient getNetClient() {
        return netClient;
    }

    /**
     * Gets the list of buckets.
     * @param request The Service Request object used to customize the HTTP request, {@link GetServiceRequest}
     * @return The response object contains the list of buckets that the user has access to.
     * @throws IOException
     * @throws SignatureException
     */
    public GetServiceResponse getService(final GetServiceRequest request) throws IOException, SignatureException {
        return new GetServiceResponse(netClient.getResponse(request));
    }

    /**
     * Gets the list of objects in a bucket.
     * @param request The Get Bucket Request object used to customize the HTTP request.  The bucket request object has
     *                several options for customizing the request.  See {@link GetBucketRequest} for the full list of options
     *                that can be configured.
     * @return The response object contains the list of objects that a bucket contains.  There is some additional
     *         information that is returned which is used for pagination, {@link GetBucketResponse} for the full
     *         list of properties
     * that are returned.
     * @throws IOException
     * @throws SignatureException
     */
    public GetBucketResponse getBucket(final GetBucketRequest request) throws IOException, SignatureException {
        return new GetBucketResponse(netClient.getResponse(request));
    }

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
    public PutBucketResponse putBucket(final PutBucketRequest request) throws IOException, SignatureException {
        return new PutBucketResponse(netClient.getResponse(request));
    }

    /**
     * Deletes a bucket from a DS3 endpoint.  <b>Note:</b> all objects must be deleted first before deleteBucket will
     * succeed.
     * @param request The Delete Bucket Request object used to customize the HTTP request.  The delete bucket request object
     *                has some options for customizing the request.  See {@link DeleteBucketRequest} for the full list of
     *                options that can be configured.
     * @return The response object is returned primarily to be consistent with the rest of the API.  Additional data
     *         may be returned here in the future but nothing is currently.  See {@link DeleteBucketResponse} for the most
     *         up to date information on what is returned.
     * @throws IOException
     * @throws SignatureException
     */
    public DeleteBucketResponse deleteBucket(final DeleteBucketRequest request) throws IOException, SignatureException {
        return new DeleteBucketResponse(netClient.getResponse(request));
    }

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
    public DeleteObjectResponse deleteObject(final DeleteObjectRequest request) throws IOException, SignatureException {
        return new DeleteObjectResponse(netClient.getResponse(request));
    }

    /**
     * Get an object in a bucket from a DS3 endpoint
     * @param request The Get Object Request object used to customize the HTTP request.  The get object request object
     *                has some options for customizing the request.  See {@link GetObjectRequest} for the full list of
     *                options that can be configured.
     * @return The response object contains a stream that can be used to read the contents of the object from.  See
     *         {@link GetObjectResponse} for any other properties.
     * @throws IOException
     * @throws SignatureException
     */
    public GetObjectResponse getObject(final GetObjectRequest request) throws IOException, SignatureException {
        return new GetObjectResponse(netClient.getResponse(request));
    }

    /**
     * Puts a new object to an existing bucket to a DS3 endpoint
     * @param request The Put Object Request object used to customize the HTTP request.  The put object request object
     *                has some options for customizing the request.  See {@link PutObjectRequest} for the full list of
     *                options that can be configured.
     * @return The response object is returned primarily to be consistent with the rest of the API.  Additional data
     *         may be returned here in the future but nothing is currently.  See {@link PutObjectResponse} for the most
     *         up to date information on what is returned.
     * @throws IOException
     * @throws SignatureException
     */
    public PutObjectResponse putObject(final PutObjectRequest request) throws IOException, SignatureException {
        return new PutObjectResponse(netClient.getResponse(request));
    }

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
    public BulkGetResponse bulkGet(final BulkGetRequest request) throws IOException, SignatureException {
        return new BulkGetResponse(netClient.getResponse(request));
    }

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
    public BulkPutResponse bulkPut(final BulkPutRequest request) throws IOException, SignatureException {
        return new BulkPutResponse(netClient.getResponse(request));
    }
}

