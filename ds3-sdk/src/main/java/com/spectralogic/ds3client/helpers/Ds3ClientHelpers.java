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

package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.helpers.options.ReadJobOptions;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.Predicate;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.security.SignatureException;
import java.util.Iterator;
import java.util.Map;
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

        void attachDataTransferredListener(final DataTransferredListener listener);
        void attachObjectCompletedListener(final ObjectCompletedListener listener);
        void removeDataTransferredListener(final DataTransferredListener listener);
        void removeObjectCompletedListener(final ObjectCompletedListener listener);
        void attachMetadataReceivedListener(final MetadataReceivedListener listener);
        void removeMetadataReceivedListener(final MetadataReceivedListener listener);
        void attachChecksumListener(final ChecksumListener listener);
        void removeChecksumListener(final ChecksumListener listener);
        /**
         * Sets the maximum number of requests to execute at a time when fulfilling the job.
         */
        Job withMaxParallelRequests(final int maxParallelRequests);
        
        Job withMetadata(final MetadataAccess access);

        Job withChecksum(final ChecksumFunction checksumFunction);

        /**
         * Transfers the files in this job using the given seekable channel creator.  The is a blocking call.
         * @throws SignatureException
         * @throws IOException
         * @throws XmlProcessingException
         */
        void transfer(final ObjectChannelBuilder channelBuilder)
            throws SignatureException, IOException, XmlProcessingException;
    }

    public interface MetadataAccess {
        Map<String, String> getMetadataValue(final String filename);
    }

    /**
     * Wraps the given {@link com.spectralogic.ds3client.Ds3ClientImpl} with helper methods.
     */
    public static Ds3ClientHelpers wrap(final Ds3Client client) {
        return new Ds3ClientHelpersImpl(client);
    }

    public static Ds3ClientHelpers wrap(final Ds3Client client, final int retryAfter) {
        return new Ds3ClientHelpersImpl(client, retryAfter);
    }

    /**
     * Performs a bulk put job creation request and returns an {@link WriteJob}.
     * See {@link WriteJob} for information on how to write the objects for the job.
     *
     * @throws SignatureException
     * @throws IOException
     * @throws XmlProcessingException
     */
    public abstract Ds3ClientHelpers.Job startWriteJob(final String bucket, final Iterable<Ds3Object> objectsToWrite)
            throws SignatureException, IOException, XmlProcessingException;

    /**
     * Performs a bulk put job creation request and returns an {@link WriteJob}.
     * See {@link WriteJob} for information on how to write the objects for the job.
     *
     * @throws SignatureException
     * @throws IOException
     * @throws XmlProcessingException
     */
    public abstract Ds3ClientHelpers.Job startWriteJob(final String bucket, final Iterable<Ds3Object> objectsToWrite, final WriteJobOptions options)
            throws SignatureException, IOException, XmlProcessingException;

    /**
     * Performs a bulk get job creation request and returns an {@link ReadJob}.
     * See {@link ReadJob} for information on how to read the objects for the job.
     *
     * @throws SignatureException
     * @throws IOException
     * @throws XmlProcessingException
     */
    public abstract Ds3ClientHelpers.Job startReadJob(final String bucket, final Iterable<Ds3Object> objectsToRead)
            throws SignatureException, IOException, XmlProcessingException;

    /**
     * Performs a bulk get job creation request and returns an {@link ReadJob}.
     * See {@link ReadJob} for information on how to read the objects for the job.
     *
     * @throws SignatureException
     * @throws IOException
     * @throws XmlProcessingException
     */
    public abstract Ds3ClientHelpers.Job startReadJob(
            final String bucket,
            final Iterable<Ds3Object> objectsToRead,
            final ReadJobOptions options)
            throws SignatureException, IOException, XmlProcessingException;

    /**
     * Performs a bulk get job creation request for all of the objects in the given bucket and returns an {@link ReadJob}.
     *
     * @throws SignatureException
     * @throws IOException
     * @throws XmlProcessingException
     */
    public abstract Ds3ClientHelpers.Job startReadAllJob(final String bucket)
            throws SignatureException, IOException, XmlProcessingException;

    /**
     * Performs a bulk get job creation request for all of the objects in the given bucket and returns an {@link ReadJob}.
     *
     * @throws SignatureException
     * @throws IOException
     * @throws XmlProcessingException
     */
    public abstract Ds3ClientHelpers.Job startReadAllJob(final String bucket, final ReadJobOptions options)
            throws SignatureException, IOException, XmlProcessingException;

    /**
     * Queries job information based on job id and returns a {@link ReadJob} that can resume the job.
     * @throws SignatureException
     * @throws IOException
     * @throws XmlProcessingException
     * @throws JobRecoveryException
     */
    public abstract Ds3ClientHelpers.Job recoverWriteJob(final UUID jobId)
            throws SignatureException, IOException, XmlProcessingException, JobRecoveryException;

    /**
     * Queries job information based on job id and returns a {@link WriteJob} that can resume the job.
     * @throws SignatureException
     * @throws IOException
     * @throws XmlProcessingException
     * @throws JobRecoveryException
     */
    public abstract Ds3ClientHelpers.Job recoverReadJob(final UUID jobId)
            throws SignatureException, IOException, XmlProcessingException, JobRecoveryException;

    /**
     * Ensures that a bucket exists.  The the bucket does not exist, it will be created.
     * @param bucket The name of the bucket to check that it exists.
     * @throws IOException
     * @throws SignatureException
     */
    public abstract void ensureBucketExists(final String bucket) throws IOException, SignatureException;

    /**
     * Ensures that a bucket exists.  The the bucket does not exist, it will be created.
     * @param bucket The name of the bucket to check that it exists.
     * @param dataPolicy The data policy for the bucket
     * @throws IOException
     * @throws SignatureException
     */
    public abstract void ensureBucketExists(final String bucket, final UUID dataPolicy) throws IOException, SignatureException;

    /**
     * Returns information about all of the objects in the bucket, regardless of how many objects the bucket contains.
     *
     * @throws SignatureException
     * @throws IOException
     */
    public abstract Iterable<Contents> listObjects(final String bucket) throws SignatureException, IOException;

    /**
     * Returns information about objects in the bucket, filtered by names that start with keyPrefix.
     *
     * @param keyPrefix Limits the response to keys that begin with the specified prefix. You can use prefixes to separate a
     *                  bucket into different groupings of keys. (You can think of using prefix to make groups in the same
     *                  way you'd use a folder in a file system.)
     *
     * @throws SignatureException
     * @throws IOException
     */
    public abstract Iterable<Contents> listObjects(final String bucket, final String keyPrefix)
            throws SignatureException, IOException;

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
     * @throws SignatureException
     * @throws IOException
     */
    public abstract Iterable<Contents> listObjects(final String bucket, final String keyPrefix, final String nextMarker)
            throws SignatureException, IOException;

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
     * @throws SignatureException
     * @throws IOException
     */
    public abstract Iterable<Contents> listObjects(final String bucket, final String keyPrefix, final String nextMarker, final int maxKeys)
            throws SignatureException, IOException;


    /**
     * Returns an object list with which you can call {@code startWriteJob} based on the files in a {@code directory}.
     * This method traverses the {@code directory} recursively.
     *
     * @throws IOException
     */
    public abstract Iterable<Ds3Object> listObjectsForDirectory(final Path directory) throws IOException;

    /**
     *
     * @param objectsList
     * @param prefix
     * @return
     */
    public abstract Iterable<Ds3Object> addPrefixToDs3ObjectsList(final Iterable<Ds3Object> objectsList, final String prefix);

    /**
     *
     * @param objectsList
     * @param prefix
     * @return
     */
    public abstract Iterable<Ds3Object> removePrefixFromDs3ObjectsList(final Iterable<Ds3Object> objectsList, final String prefix);

    /**
     *  Filter out folders from the object list.  Use this method when piping the list of objects from listObjects to a bulk job.
      * @param objects
     * @return
     */
    public abstract Iterable<Ds3Object> toDs3Iterable(final Iterable<Contents> objects);

    public abstract Iterable<Ds3Object> toDs3Iterable(final Iterable<Contents> objects, final Predicate<Contents> filter);
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
}
