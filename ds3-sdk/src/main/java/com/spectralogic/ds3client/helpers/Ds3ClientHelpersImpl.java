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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.helpers.events.EventRunner;
import com.spectralogic.ds3client.helpers.events.SameThreadEventRunner;
import com.spectralogic.ds3client.helpers.pagination.GetBucketLoaderFactory;
import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.helpers.options.ReadJobOptions;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.helpers.util.PartialObjectHelpers;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.bulk.RequestType;
import com.spectralogic.ds3client.models.common.CommonPrefixes;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.models.bulk.*;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.utils.Guard;
import com.spectralogic.ds3client.utils.collections.LazyIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

class Ds3ClientHelpersImpl extends Ds3ClientHelpers {

    public final static int DEFAULT_RETRY_DELAY = -1;
    public final static int DEFAULT_RETRY_AFTER = -1;
    public final static int DEFAULT_OBJECT_TRANSFER_ATTEMPTS = 5;
    public final static String DEFAULT_DELIMITER = null;

    private final static Logger LOG = LoggerFactory.getLogger(Ds3ClientHelpersImpl.class);
    private final static int DEFAULT_LIST_OBJECTS_RETRIES = 5;

    private final Ds3Client client;
    private final int retryAfter;
    private final int retryDelay;
    private final int objectTransferAttempts;
    private final EventRunner eventRunner;
    private final FileSystemHelper fileSystemHelper;

    public Ds3ClientHelpersImpl(final Ds3Client client) {
        this(client, DEFAULT_RETRY_AFTER);
    }

    public Ds3ClientHelpersImpl(final Ds3Client client, final int retryAfter) {
        this(client, retryAfter, DEFAULT_OBJECT_TRANSFER_ATTEMPTS);
    }

    public Ds3ClientHelpersImpl(final Ds3Client client, final int retryAfter, final int objectTransferAttempts) {
        this(client, retryAfter, objectTransferAttempts, DEFAULT_RETRY_DELAY);
    }

    public Ds3ClientHelpersImpl(final Ds3Client client, final int retryAfter, final int objectTransferAttempts, final int retryDelay) {
        this(client, retryAfter, objectTransferAttempts, retryDelay, new SameThreadEventRunner());
    }

    public Ds3ClientHelpersImpl(final Ds3Client client,
                                final int retryAfter,
                                final int objectTransferAttempts,
                                final int retryDelay,
                                final EventRunner eventRunner)
    {
        this(client, retryAfter, objectTransferAttempts, retryDelay, eventRunner, new FileSystemHelperImpl());
    }

    public Ds3ClientHelpersImpl(final Ds3Client client,
                                final int retryAfter,
                                final int objectTransferAttempts,
                                final int retryDelay,
                                final EventRunner eventRunner,
                                final FileSystemHelper fileSystemHelper)
    {
        this.client = client;
        this.retryAfter = retryAfter;
        this.objectTransferAttempts = objectTransferAttempts;
        this.retryDelay = retryDelay;
        this.eventRunner = eventRunner;
        this.fileSystemHelper = fileSystemHelper;
    }

    @Override
    public Ds3ClientHelpers.Job startWriteJob(final String bucket, final Iterable<Ds3Object> objectsToWrite)
            throws IOException {
        return innerStartWriteJob(bucket, objectsToWrite, WriteJobOptions.create());
    }

    @Override
    public Ds3ClientHelpers.Job startWriteJob(final String bucket,
                                                   final Iterable<Ds3Object> objectsToWrite,
                                                   final WriteJobOptions options)
            throws IOException {
        if (options == null) {
            return innerStartWriteJob(bucket, objectsToWrite, WriteJobOptions.create());
        }
        return innerStartWriteJob(bucket, objectsToWrite, options);
    }

    private Ds3ClientHelpers.Job innerStartWriteJob(final String bucket,
                                                         final Iterable<Ds3Object> objectsToWrite,
                                                         final WriteJobOptions options)
            throws IOException {
        final PutBulkJobSpectraS3Request request = new PutBulkJobSpectraS3Request(bucket, Lists.newArrayList(objectsToWrite))
                .withPriority(options.getPriority())
                .withAggregating(options.isAggregating())
                .withForce(options.isForce())
                .withIgnoreNamingConflicts(options.doIgnoreNamingConflicts());

        if (options.getMaxUploadSize() > 0) {
            request.withMaxUploadSize(options.getMaxUploadSize());
        }

        final PutBulkJobSpectraS3Response putBulkJobSpectraS3Response = this.client.putBulkJobSpectraS3(
                request);


        return new WriteJobImpl(
                this.client,
                putBulkJobSpectraS3Response.getMasterObjectList(),
                this.retryAfter,
                options.getChecksumType(),
                this.objectTransferAttempts,
                this.retryDelay,
                this.eventRunner);
    }

    @Override
    public Ds3ClientHelpers.Job startReadJob(final String bucket, final Iterable<Ds3Object> objectsToRead)
            throws IOException {
        return innerStartReadJob(bucket, objectsToRead, ReadJobOptions.create());
    }

    @Override
    public Ds3ClientHelpers.Job startReadJob(final String bucket, final Iterable<Ds3Object> objectsToRead, final ReadJobOptions options)
            throws IOException {
        if (options == null) {
            return innerStartReadJob(bucket, objectsToRead, ReadJobOptions.create());
        }
        return innerStartReadJob(bucket, objectsToRead, options);
    }

    private Ds3ClientHelpers.Job innerStartReadJob(final String bucket, final Iterable<Ds3Object> objectsToRead, final ReadJobOptions options)
            throws IOException {
        final List<Ds3Object> objects = Lists.newArrayList(objectsToRead);
        final GetBulkJobSpectraS3Request getBulkJobSpectraS3Request = new GetBulkJobSpectraS3Request(bucket, objects)
                .withChunkClientProcessingOrderGuarantee(JobChunkClientProcessingOrderGuarantee.NONE)
                .withPriority(options.getPriority());
        if (!Guard.isStringNullOrEmpty(options.getName())) {
            getBulkJobSpectraS3Request.withName(options.getName());
        }

        final GetBulkJobSpectraS3Response getBulkJobSpectraS3Response = this.client.getBulkJobSpectraS3(getBulkJobSpectraS3Request);

        final ImmutableMultimap<String, Range> partialRanges = PartialObjectHelpers.getPartialObjectsRanges(objects);

        return new ReadJobImpl(
                this.client,
                getBulkJobSpectraS3Response.getMasterObjectList(),
                partialRanges,
                this.objectTransferAttempts,
                this.retryAfter,
                this.retryDelay,
                this.eventRunner);
    }

    @Override
    public Ds3ClientHelpers.Job startReadAllJob(final String bucket)
            throws IOException {
        return innerStartReadAllJob(bucket, ReadJobOptions.create());
    }

    @Override
    public Ds3ClientHelpers.Job startReadAllJob(final String bucket, final ReadJobOptions options)
            throws IOException {
        if (options == null) {
            return innerStartReadAllJob(bucket, ReadJobOptions.create());
        }
        return innerStartReadAllJob(bucket, options);
    }

    private Ds3ClientHelpers.Job innerStartReadAllJob(final String bucket, final ReadJobOptions options)
            throws IOException {
        final Iterable<Contents> contentsList = this.listObjects(bucket);

        final Iterable<Ds3Object> ds3Objects = this.toDs3Iterable(contentsList, FolderNameFilter.filter());

        return this.startReadJob(bucket, ds3Objects, options);
    }

    @Override
    public Ds3ClientHelpers.Job recoverWriteJob(final UUID jobId) throws IOException, JobRecoveryException {
        innerVerifyJobActive(jobId);
        final ModifyJobSpectraS3Response jobResponse = this.client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(jobId.toString()));
        if (JobRequestType.PUT != jobResponse.getMasterObjectListResult().getRequestType()) {
            throw new JobRecoveryTypeException(
                    RequestType.PUT.toString(),
                    jobResponse.getMasterObjectListResult().getRequestType().toString());
        }
        // TODO Need to allow the user to pass in the checksumming information again
        return new WriteJobImpl(
                this.client,
                jobResponse.getMasterObjectListResult(),
                this.retryAfter,
                ChecksumType.Type.NONE,
                this.objectTransferAttempts,
                this.retryDelay,
                this.eventRunner);
    }

    @Override
    //TODO add a partial object read recovery method.  That method will require the list of partial objects.
    public Ds3ClientHelpers.Job recoverReadJob(final UUID jobId) throws IOException, JobRecoveryException {
        innerVerifyJobActive(jobId);
        final ModifyJobSpectraS3Response jobResponse = this.client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(jobId.toString()));
        if (JobRequestType.GET != jobResponse.getMasterObjectListResult().getRequestType()){
            throw new JobRecoveryTypeException(
                    RequestType.GET.toString(),
                    jobResponse.getMasterObjectListResult().getRequestType().toString() );
        }
        return new ReadJobImpl(
                this.client,
                jobResponse.getMasterObjectListResult(),
                ImmutableMultimap.<String, Range>of(),
                this.objectTransferAttempts,
                this.retryAfter,
                this.retryDelay,
                this.eventRunner);
    }

    /**
     * Verifies that the specified job is active. If the job is not active, then a
     * JobRecoveryNotActiveException is thrown.
     * @throws IOException
     * @throws JobRecoveryNotActiveException
     */
    private void innerVerifyJobActive(final UUID jobId) throws IOException, JobRecoveryNotActiveException {
        try {
            client.getActiveJobSpectraS3(new GetActiveJobSpectraS3Request(jobId));
        } catch (final FailedRequestException e) {
            if (e.getStatusCode() == 404) {
                throw new JobRecoveryNotActiveException(jobId, e);
            }
            throw e;
        }
    }

    @Override
    public void ensureBucketExists(final String bucket) throws IOException {
        final HeadBucketResponse response = this.client.headBucket(new HeadBucketRequest(bucket));
        if (response.getStatus() == HeadBucketResponse.Status.DOESNTEXIST) {
            try {
                this.client.putBucket(new PutBucketRequest(bucket));
            } catch (final FailedRequestException e) {
                if (e.getStatusCode() != 409) {
                    throw e;
                }
                LOG.warn("Creating {} failed because it was created by another thread or process", bucket);
            }
        }
    }

    @Override
    public void ensureBucketExists(final String bucket, final UUID dataPolicy) throws IOException {
        final HeadBucketResponse response = this.client.headBucket(new HeadBucketRequest(bucket));
        if (response.getStatus() == HeadBucketResponse.Status.DOESNTEXIST) {
            try {
                this.client.putBucketSpectraS3(new PutBucketSpectraS3Request(bucket).withDataPolicyId(dataPolicy));
            } catch (final FailedRequestException e) {
                if (e.getStatusCode() != 409) {
                    throw e;
                }
                LOG.warn("Creating {} failed because it was created by another thread or process", bucket);
            }
        }
    }

    @Override
    public Iterable<Contents> listObjects(final String bucket) throws IOException {
        return this.listObjects(bucket, null);
    }

    @Override
    public Iterable<Contents> listObjects(final String bucket, final String keyPrefix) throws IOException {
        return this.listObjects(bucket, keyPrefix, null, Integer.MAX_VALUE);
    }

    @Override
    public Iterable<Contents> listObjects(final String bucket, final String keyPrefix, final String nextMarker) throws IOException {
        return this.listObjects(bucket, keyPrefix, nextMarker, Integer.MAX_VALUE);
    }

    @Override
    public Iterable<Contents> listObjects(final String bucket, final String keyPrefix, final String nextMarker, final int maxKeys) {

        return new LazyIterable<>(new GetBucketLoaderFactory(client, bucket, keyPrefix, DEFAULT_DELIMITER, nextMarker, maxKeys, DEFAULT_LIST_OBJECTS_RETRIES));
    }

    @Override
    public Iterable<Contents> listObjects(final String bucket, final String keyPrefix, final String nextMarker, final int maxKeys, final int retries) {

        return new LazyIterable<>(new GetBucketLoaderFactory(client, bucket, keyPrefix, DEFAULT_DELIMITER, nextMarker, maxKeys, retries));
    }

    @Override
    public Iterable<Ds3Object> listObjectsForDirectory(final Path directory) throws IOException {
        final ImmutableList.Builder<Ds3Object> objects = ImmutableList.builder();
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                objects.add(new Ds3Object(directory.relativize(file).toString().replace("\\", "/"), Files.size(file)));
                return FileVisitResult.CONTINUE;
            }
        });
        return objects.build();
    }

    @Override
    public ContentPrefix remoteListDirectory(final String bucket, final String keyPrefix, final String nextMarker, final int maxKeys) throws IOException {
        return remoteListDirectory(bucket,keyPrefix,"/", nextMarker, maxKeys);
    }

    public ContentPrefix remoteListDirectory(final String bucket, final String keyPrefix, final String delimiter, final String nextMarker, final int maxKeys) throws IOException {
        final Iterable<Contents> contents = new LazyIterable<>(new GetBucketLoaderFactory(client, bucket, keyPrefix, delimiter, nextMarker, maxKeys, DEFAULT_LIST_OBJECTS_RETRIES));
        final GetBucketRequest request = new GetBucketRequest(bucket);
        if (keyPrefix != null) {
            request.withPrefix(keyPrefix);
        }
        if (delimiter != null) {
            request.withDelimiter(delimiter);
        }
        if (nextMarker != null) {
            request.withMarker(nextMarker);
        }
        request.withMaxKeys(10);
        final GetBucketResponse response;
        final List<CommonPrefixes> commonPrefixes;
        try {
            response = this.client.getBucket(request);
            final ListBucketResult result = response.getListBucketResult();
            commonPrefixes = result.getCommonPrefixes();

        } catch (final FailedRequestException e) {
            throw new RuntimeException("Failed to get the list of objects due to a failed request", e);
        }
       return new ContentPrefix(contents, commonPrefixes);
    }

    public Iterable<Ds3Object> addPrefixToDs3ObjectsList(final Iterable<Ds3Object> objectsList, final String prefix) {
        final FluentIterable<Ds3Object> objectIterable = FluentIterable.from(objectsList);

        return objectIterable.transform(new Function<Ds3Object, Ds3Object>() {
            @Nullable
            @Override
            public Ds3Object apply(@Nullable final Ds3Object object) {
                return new Ds3Object( prefix + object.getName(), object.getSize());
            }
        });
    }

    public Iterable<Ds3Object> removePrefixFromDs3ObjectsList(final Iterable<Ds3Object> objectsList, final String prefix) {
        final FluentIterable<Ds3Object> objectIterable = FluentIterable.from(objectsList);

        return objectIterable.transform(new Function<Ds3Object, Ds3Object>() {
            @Nullable
            @Override
            public Ds3Object apply(@Nullable final Ds3Object object) {
                return new Ds3Object(stripLeadingPath(object.getName(), prefix), object.getSize());
            }
        });
    }

    /**
     * Determine if the file system directory specified in the destinationDirectory parameter
     * has enough storage space to contain the objects listed in the parameter objectNames contained in
     * the bucket specified in the parameter buckName.  You can use this method prior to starting a read
     * job to ensure that your file system has enough storage space to contain the objects you wish to
     * retrieve.
     *
     * @param bucketName           The Black Pearl bucket containing the objects you wish to retrieve.
     * @param objectNames          The names of the objects you wish to retrieve.
     * @param destinationDirectory The file system directory in you intend to store retrieved objects.
     * @return {@link ObjectStorageSpaceVerificationResult}
     */
    @Override
    public ObjectStorageSpaceVerificationResult objectsFromBucketWillFitInDirectory(final String bucketName,
                                                                                    final Collection<String> objectNames,
                                                                                    final Path destinationDirectory)
    {
        return fileSystemHelper.objectsFromBucketWillFitInDirectory(this,
                bucketName, objectNames, destinationDirectory);
    }
}
