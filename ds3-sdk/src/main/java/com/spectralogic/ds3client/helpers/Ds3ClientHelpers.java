/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.helpers.options.ReadJobOptions;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.utils.Predicate;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.util.Collection;
import java.util.UUID;

/**
 * A wrapper around the {@link com.spectralogic.ds3client.Ds3Client} which automates common tasks.
 */
public abstract class Ds3ClientHelpers {

    public interface ObjectChannelBuilder {
        /**
         * Returns a channel for the given object key. Depending on whether the
         * job is a GET or PUT, the returned channel must support either writing
         * or reading, respectively.
         *
         * @throws IOException
         */
        SeekableByteChannel buildChannel(final String key) throws IOException;
    }


    /**
     * Represents a bulk job operation.
     * When you call one of the start* methods it's recommended that you save the
     * JobId so the job can be recovered in the event of a failure.
     */
    public interface Job {
        UUID getJobId();
        String getBucketName();

        /**
         * Attaches an event handler that is invoked when a blob is successfully
         * transferred to Spectra S3.
         */
        void attachDataTransferredListener(final DataTransferredListener listener);
        void removeDataTransferredListener(final DataTransferredListener listener);

        /**
         * Attaches an event handler that is invoked when a full object is
         * successfully transferred to Spectra S3.
         */
        void attachObjectCompletedListener(final ObjectCompletedListener listener);
        void removeObjectCompletedListener(final ObjectCompletedListener listener);

        /**
         * Attaches an event handler that is invoked when metadata is received for
         * an object.
         */
        void attachMetadataReceivedListener(final MetadataReceivedListener listener);
        void removeMetadataReceivedListener(final MetadataReceivedListener listener);

        /**
         * Attaches an event handler that is invoked when an object checksum is received.
         */
        void attachChecksumListener(final ChecksumListener listener);
        void removeChecksumListener(final ChecksumListener listener);

        /**
         * Attaches an event handler that will be invoked only when there are no chunks available
         * for processing.
         */
        void attachWaitingForChunksListener(final WaitingForChunksListener listener);
        void removeWaitingForChunksListener(final WaitingForChunksListener listener);

        /**
         * Attaches an event handler when an object transfer fails
         */
        void attachFailureEventListener(final FailureEventListener listener);
        void removeFailureEventListener(final FailureEventListener listener);

        /**
         * Sets the maximum number of requests to execute at a time when fulfilling the job.
         */
        Job withMaxParallelRequests(final int maxParallelRequests);

        /**
         * Register a handler that is invoked when metadata is requested for an object
         */
        Job withMetadata(final MetadataAccess access);

        /**
         * Register a handler that is invoked when an object checksum is requested for a blob
         */
        Job withChecksum(final ChecksumFunction checksumFunction);

        /**
         * Transfers the files in this job using the given seekable channel creator.  The is a blocking call.
         * @throws IOException
         */
        void transfer(final ObjectChannelBuilder channelBuilder)
            throws IOException;
    }

    /**
     * Wraps the given {@link com.spectralogic.ds3client.Ds3ClientImpl} with helper methods.
     * @param client An instance of {@link com.spectralogic.ds3client.Ds3Client}, usually gotten from a call to
     *               {@link com.spectralogic.ds3client.Ds3ClientBuilder}
     * @return An instance of {@link com.spectralogic.ds3client.Ds3Client} wrapped with helper methods.
     */
    public static Ds3ClientHelpers wrap(final Ds3Client client) {
        return new Ds3ClientHelpersImpl(client);
    }

    /**
     * Wraps the given {@link com.spectralogic.ds3client.Ds3ClientImpl} with helper methods.
     * @param client An instance of {@link com.spectralogic.ds3client.Ds3Client}, usually gotten from a call to
     *               {@link com.spectralogic.ds3client.Ds3ClientBuilder}
     * @param retryAfter The number of times to attempt to allocate a chunk before giving up.
     * @return An instance of {@link com.spectralogic.ds3client.Ds3Client} wrapped with helper methods.
     */
    public static Ds3ClientHelpers wrap(final Ds3Client client, final int retryAfter) {
        return new Ds3ClientHelpersImpl(client, retryAfter);
    }

    /**
     * Wraps the given {@link com.spectralogic.ds3client.Ds3ClientImpl} with helper methods.
     * @param client An instance of {@link com.spectralogic.ds3client.Ds3Client}, usually gotten from a call to
     *               {@link com.spectralogic.ds3client.Ds3ClientBuilder}
     * @param retryAfter The number of times to attempt to allocate a chunk before giving up.
     * @param objectTransferAttempts The number of times to attempt to transfer an object before giving up.
     * @return An instance of {@link com.spectralogic.ds3client.Ds3Client} wrapped with helper methods.
     */
    public static Ds3ClientHelpers wrap(final Ds3Client client, final int retryAfter, final int objectTransferAttempts) {
        return new Ds3ClientHelpersImpl(client, retryAfter, objectTransferAttempts);
    }

    /**
     * Performs a bulk put job creation request and returns an {@link WriteJobImpl}.
     * See {@link WriteJobImpl} for information on how to write the objects for the job.
     *
     * @throws IOException
     */
    public abstract Ds3ClientHelpers.Job startWriteJob(final String bucket, final Iterable<Ds3Object> objectsToWrite)
            throws IOException;

    /**
     * Performs a bulk put job creation request and returns an {@link WriteJobImpl}.
     * See {@link WriteJobImpl} for information on how to write the objects for the job.
     *
     * @throws IOException
     */
    public abstract Ds3ClientHelpers.Job startWriteJob(final String bucket, final Iterable<Ds3Object> objectsToWrite, final WriteJobOptions options)
            throws IOException;

    /**
     * Performs a bulk get job creation request and returns an {@link ReadJobImpl}.
     * See {@link ReadJobImpl} for information on how to read the objects for the job.
     *
     * @throws IOException
     */
    public abstract Ds3ClientHelpers.Job startReadJob(final String bucket, final Iterable<Ds3Object> objectsToRead)
            throws IOException;

    /**
     * Performs a bulk get job creation request and returns an {@link ReadJobImpl}.
     * See {@link ReadJobImpl} for information on how to read the objects for the job.
     *
     * @throws IOException
     */
    public abstract Ds3ClientHelpers.Job startReadJob(
            final String bucket,
            final Iterable<Ds3Object> objectsToRead,
            final ReadJobOptions options)
            throws IOException;

    /**
     * Performs a bulk get job creation request for all of the objects in the given bucket and returns an {@link ReadJobImpl}.
     *
     * @throws IOException
     */
    public abstract Ds3ClientHelpers.Job startReadAllJob(final String bucket)
            throws IOException;

    /**
     * Performs a bulk get job creation request for all of the objects in the given bucket and returns an {@link ReadJobImpl}.
     *
     * @throws IOException
     */
    public abstract Ds3ClientHelpers.Job startReadAllJob(final String bucket, final ReadJobOptions options)
            throws IOException;

    /**
     * Queries job information based on job id and returns a {@link ReadJobImpl} that can resume the job.
     * @throws IOException
     * @throws JobRecoveryException
     */
    public abstract Ds3ClientHelpers.Job recoverWriteJob(final UUID jobId)
            throws IOException, JobRecoveryException;

    /**
     * Queries job information based on job id and returns a {@link WriteJobImpl} that can resume the job.
     * @throws IOException
     * @throws JobRecoveryException
     */
    public abstract Ds3ClientHelpers.Job recoverReadJob(final UUID jobId)
            throws IOException, JobRecoveryException;

    /**
     * Ensures that a bucket exists.  The the bucket does not exist, it will be created.
     * @param bucket The name of the bucket to check that it exists.
     * @throws IOException
     */
    public abstract void ensureBucketExists(final String bucket) throws IOException;

    /**
     * Ensures that a bucket exists.  The the bucket does not exist, it will be created.
     * @param bucket The name of the bucket to check that it exists.
     * @param dataPolicy The data policy for the bucket
     * @throws IOException
     */
    public abstract void ensureBucketExists(final String bucket, final UUID dataPolicy) throws IOException;

    /**
     * Returns information about all of the objects in the bucket, regardless of how many objects the bucket contains.
     *
     * @throws IOException
     */
    public abstract Iterable<Contents> listObjects(final String bucket) throws IOException;

    /**
     * Returns information about objects in the bucket, filtered by names that start with keyPrefix.
     *
     * @param keyPrefix Limits the response to keys that begin with the specified prefix. You can use prefixes to separate a
     *                  bucket into different groupings of keys. (You can think of using prefix to make groups in the same
     *                  way you'd use a folder in a file system.)
     *
     * @throws IOException
     */
    public abstract Iterable<Contents> listObjects(final String bucket, final String keyPrefix)
            throws IOException;

    /**
     * Returns information about objects in the bucket in alphabetical order, starting with key after the next_marker in order,
     * regardless of how many objects the bucket contains.
     *
     * @param keyPrefix Limits the response to keys that begin with the specified prefix. You can use prefixes to separate a
     *                  bucket into different groupings of keys. (You can think of using prefix to make groups in the same
     *                  way you'd use a folder in a file system.)
     *                  Set to null if unused but need to specify nextMarker.
     * @param nextMarker Specifies the key to start with when listing objects in a bucket. Returns object keys in alphabetical
     *                   order, starting with key after the nextMarker in order.
     *
     * @throws IOException
     */
    public abstract Iterable<Contents> listObjects(final String bucket, final String keyPrefix, final String nextMarker)
            throws IOException;

    /**
     * Returns information about objects in the bucket in alphabetical order, starting with key after the next_marker in order,
     * regardless of how many objects the bucket contains.
     *
     * @param keyPrefix Limits the response to keys that begin with the specified prefix. You can use prefixes to separate a
     *                  bucket into different groupings of keys. (You can think of using prefix to make groups in the same
     *                  way you'd use a folder in a file system.)
     *                  Set to null if unused but need to specify nextMarker or maxKeys.
     * @param nextMarker Specifies the key to start with when listing objects in a bucket. Returns object keys in
     *                   alphabetical order, starting with key after the nextMarker in order.
     *                   Set to null if unused but need to specify maxKeys.
     * @param maxKeys    Sets the maximum number of keys returned in the response body. You can add this to your request
     *                   if you want to retrieve fewer than the default 1000 keys.
     *                   The response might contain fewer keys but will never contain more. If there are additional keys
     *                   that satisfy the search criteria but were not returned because max-keys was exceeded, the
     *                   response.isTruncated() is true. To return the additional keys, see nextMarker.
     */
    public abstract Iterable<Contents> listObjects(final String bucket, final String keyPrefix, final String nextMarker, final int maxKeys);

    /**
     * Returns information about objects in the bucket in alphabetical order, starting with key after the next_marker in order,
     * regardless of how many objects the bucket contains.
     *
     * @param keyPrefix Limits the response to keys that begin with the specified prefix. You can use prefixes to separate a
     *                  bucket into different groupings of keys. (You can think of using prefix to make groups in the same
     *                  way you'd use a folder in a file system.)
     *                  Set to null if unused but need to specify nextMarker or maxKeys.
     * @param nextMarker Specifies the key to start with when listing objects in a bucket. Returns object keys in
     *                   alphabetical order, starting with key after the nextMarker in order.
     *                   Set to null if unused but need to specify maxKeys.
     * @param maxKeys    Sets the maximum number of keys returned in the response body. You can add this to your request
     *                   if you want to retrieve fewer than the default 1000 keys.
     *                   The response might contain fewer keys but will never contain more. If there are additional keys
     *                   that satisfy the search criteria but were not returned because max-keys was exceeded, the
     *                   response.isTruncated() is true. To return the additional keys, see nextMarker.
     * @param retries    Specifies how many times the helper function will attempt to retry a request for failing. Default - 5
     */
    public abstract Iterable<Contents> listObjects(final String bucket, final String keyPrefix, final String nextMarker, final int maxKeys, final int retries);

    /**
     * Returns an object list with which you can call {@code startWriteJobImpl} based on the files in a {@code directory}.
     * This method traverses the {@code directory} recursively.
     *
     * @throws IOException
     */
    public abstract Iterable<Ds3Object> listObjectsForDirectory(final Path directory) throws IOException;

    /**
     * Returns an Iterable of {@link Ds3Object} that have a prefix added.
     */
    public abstract Iterable<Ds3Object> addPrefixToDs3ObjectsList(final Iterable<Ds3Object> objectsList, final String prefix);

    /**
     * Returns an Iterable of {@link Ds3Object} that have a prefix removed.
     */
    public abstract Iterable<Ds3Object> removePrefixFromDs3ObjectsList(final Iterable<Ds3Object> objectsList, final String prefix);

    /**
     * Converts an {@link Contents} to a {@link Ds3Object}.  Optionally a caller can supply many filters
     * that will be applied before converting.
     */
    @SafeVarargs
    public final Iterable<Ds3Object> toDs3Iterable(final Iterable<Contents> objects, final Predicate<Contents>... filters) {

        FluentIterable<Contents> fluentIterable = FluentIterable.from(objects).filter(new com.google.common.base.Predicate<Contents>() {
            @Override
            public boolean apply(@Nullable final Contents input) {
                return input != null;
            }
        });

        if (filters != null) {
            for (final Predicate<Contents> filter : filters) {
                fluentIterable = fluentIterable.filter(new com.google.common.base.Predicate<Contents>() {
                    @Override
                    public boolean apply(@Nullable final Contents input) {
                        return filter == null || filter.test(input); // do not filter anything if filter is null
                    }
                });
            }
        }

        return fluentIterable.transform(new Function<Contents, Ds3Object>() {
            @Nullable
            @Override
            public Ds3Object apply(@Nullable final Contents input) {
                return new Ds3Object(input.getKey(), input.getSize());
            }
        });
    }

    /**
     * Strip prefix from the beginning of objectName.  If objectName does not start with prefix, return objectName unmodified.
     * @param objectName
     * @param prefix
     * @return
     */
    public static String stripLeadingPath(final String objectName, final String prefix) {
        String returnString = objectName;
        if (objectName.startsWith(prefix)) {
            returnString = objectName.substring(prefix.length());
        }
        return returnString;
    }

    /**
     * Determine if the file system directory specified in the destinationDirectory parameter
     * has enough storage space to contain the objects listed in the parameter objectNames contained in
     * the bucket specified in the parameter buckName.  You can use this method prior to starting a read
     * job to ensure that your file system has enough storage space to contain the objects you wish to
     * retrieve.
     * @param bucketName The Black Pearl bucket containing the objects you wish to retrieve.
     * @param objectNames The names of the objects you wish to retrieve.
     * @param destinationDirectory The file system directory in you intend to store retrieved objects.
     * @return {@link ObjectStorageSpaceVerificationResult}
     */
    public abstract ObjectStorageSpaceVerificationResult objectsFromBucketWillFitInDirectory(final String bucketName,
                                                                                             final Collection<String> objectNames,
                                                                                             final Path destinationDirectory);
}
