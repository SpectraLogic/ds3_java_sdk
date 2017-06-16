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
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Lists;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.HeadBucketRequest;
import com.spectralogic.ds3client.commands.HeadBucketResponse;
import com.spectralogic.ds3client.commands.PutBucketRequest;
import com.spectralogic.ds3client.commands.spectrads3.*;
import com.spectralogic.ds3client.helpers.events.EventRunner;
import com.spectralogic.ds3client.helpers.events.SameThreadEventRunner;
import com.spectralogic.ds3client.helpers.options.ReadJobOptions;
import com.spectralogic.ds3client.helpers.options.WriteJobOptions;
import com.spectralogic.ds3client.helpers.pagination.FileSystemKey;
import com.spectralogic.ds3client.helpers.pagination.GetBucketKeyLoaderFactory;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcher;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcherImpl;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategy;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategyBuilder;
import com.spectralogic.ds3client.helpers.util.PartialObjectHelpers;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.networking.FailedRequestException;
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
    private final static Logger LOG = LoggerFactory.getLogger(Ds3ClientHelpersImpl.class);
    private final static int DEFAULT_LIST_OBJECTS_RETRIES = 5;
    public final static String DEFAULT_DELIMITER = null;

    private final Ds3Client client;
    private final int maxChunkAttempts;
    private final int secondsBetweenChunkAttempts;
    private final int maxObjectTransferAttempts;
    private final EventRunner eventRunner;
    private final FileSystemHelper fileSystemHelper;

    public Ds3ClientHelpersImpl(final Ds3Client client) {
        this(client, TransferStrategyBuilder.DEFAULT_CHUNK_ATTEMPT_RETRY_ATTEMPTS);
    }

    public Ds3ClientHelpersImpl(final Ds3Client client, final int maxChunkAttempts) {
        this(client, maxChunkAttempts, TransferStrategyBuilder.DEFAULT_OBJECT_TRANSFER_ATTEMPTS);
    }

    public Ds3ClientHelpersImpl(final Ds3Client client, final int maxChunkAttempts, final int maxObjectTransferAttempts) {
        this(client, maxChunkAttempts, maxObjectTransferAttempts, TransferStrategyBuilder.DEFAULT_CHUNK_ATTEMPT_RETRY_INTERVAL);
    }

    public Ds3ClientHelpersImpl(final Ds3Client client, final int maxChunkAttempts, final int maxObjectTransferAttempts, final int secondsBetweenChunkAttempts) {
        this(client, maxChunkAttempts, maxObjectTransferAttempts, secondsBetweenChunkAttempts, new SameThreadEventRunner());
    }

    public Ds3ClientHelpersImpl(final Ds3Client client,
                                final int maxChunkAttempts,
                                final int maxObjectTransferAttempts,
                                final int secondsBetweenChunkAttempts,
                                final EventRunner eventRunner)
    {
        this(client, maxChunkAttempts, maxObjectTransferAttempts, secondsBetweenChunkAttempts, eventRunner, new FileSystemHelperImpl());
    }

    public Ds3ClientHelpersImpl(final Ds3Client client,
                                final int maxChunkAttempts,
                                final int maxObjectTransferAttempts,
                                final int secondsBetweenChunkAttempts,
                                final EventRunner eventRunner,
                                final FileSystemHelper fileSystemHelper)
    {
        this.client = client;
        this.maxChunkAttempts = maxChunkAttempts;
        this.maxObjectTransferAttempts = maxObjectTransferAttempts;
        this.secondsBetweenChunkAttempts = secondsBetweenChunkAttempts;
        this.eventRunner = eventRunner;
        this.fileSystemHelper = fileSystemHelper;
    }

    @Override
    public Ds3ClientHelpers.Job startWriteJob(final String bucket, final Iterable<Ds3Object> objectsToWrite)
            throws IOException
    {
        return startWriteJob(bucket, objectsToWrite, WriteJobOptions.create());
    }

    @Override
    public Ds3ClientHelpers.Job startWriteJob(final String bucket,
                                              final Iterable<Ds3Object> objectsToWrite,
                                              final WriteJobOptions options)
            throws IOException
    {
        if (options == null) {
            return innerStartWriteJob(bucket, objectsToWrite, WriteJobOptions.create(), makeTransferStrategyBuilder());
        }

        return innerStartWriteJob(bucket, objectsToWrite, options, makeTransferStrategyBuilder());
    }

    private TransferStrategyBuilder makeTransferStrategyBuilder() {
        final EventDispatcher eventDispatcher = new EventDispatcherImpl(eventRunner);

        final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
                .withDs3Client(client)
                .withNumChunkAttemptRetries(maxChunkAttempts)
                .withNumTransferRetries(maxObjectTransferAttempts)
                .withChunkRetryDelayInSeconds(secondsBetweenChunkAttempts)
                .withEventRunner(eventRunner)
                .withEventDispatcher(eventDispatcher);

        return transferStrategyBuilder;
    }

    private Ds3ClientHelpers.Job innerStartWriteJob(final String bucket,
                                                    final Iterable<Ds3Object> objectsToWrite,
                                                    final WriteJobOptions options,
                                                    final TransferStrategyBuilder transferStrategyBuilder)
            throws IOException
    {
        final PutBulkJobSpectraS3Request request = new PutBulkJobSpectraS3Request(bucket, Lists.newArrayList(objectsToWrite))
                .withPriority(options.getPriority())
                .withAggregating(options.isAggregating())
                .withForce(options.isForce())
                .withIgnoreNamingConflicts(options.doIgnoreNamingConflicts());

        if (options.getMaxUploadSize() > 0) {
            request.withMaxUploadSize(options.getMaxUploadSize());
        }

        final PutBulkJobSpectraS3Response putBulkJobSpectraS3Response = this.client.putBulkJobSpectraS3(request);

        transferStrategyBuilder.withMasterObjectList(putBulkJobSpectraS3Response.getMasterObjectList())
                .withChecksumType(options.getChecksumType());

        return new WriteJobImpl(transferStrategyBuilder);
    }

    @Override
    public Job startWriteJob(final TransferStrategy transferStrategy)
            throws IOException
    {
        return new WriteJobImpl(transferStrategy);
    }

    @Override
    public Job startWriteJobUsingStreamedBehavior(final String bucket, final Iterable<Ds3Object> objectsToWrite) throws IOException {
        return startWriteJobUsingStreamedBehavior(bucket, objectsToWrite, WriteJobOptions.create());
    }

    @Override
    public Job startWriteJobUsingStreamedBehavior(final String bucket,
                                                  final Iterable<Ds3Object> objectsToWrite,
                                                  final WriteJobOptions options)
            throws IOException
    {
        Preconditions.checkNotNull(options, "options may not be null.");

        final TransferStrategyBuilder transferStrategyBuilder = makeTransferStrategyBuilder();
        transferStrategyBuilder.usingStreamedTransferBehavior();

        return innerStartWriteJob(bucket, objectsToWrite, options, transferStrategyBuilder);
    }

    @Override
    public Job startWriteJobUsingRandomAccessBehavior(final String bucket, final Iterable<Ds3Object> objectsToWrite) throws IOException {
        return startWriteJobUsingRandomAccessBehavior(bucket, objectsToWrite, WriteJobOptions.create());
    }

    @Override
    public Job startWriteJobUsingRandomAccessBehavior(final String bucket, final Iterable<Ds3Object> objectsToWrite, final WriteJobOptions options) throws IOException {
        Preconditions.checkNotNull(options, "options may not be null.");

        final TransferStrategyBuilder transferStrategyBuilder = makeTransferStrategyBuilder();
        transferStrategyBuilder.usingRandomAccessTransferBehavior();

        return innerStartWriteJob(bucket, objectsToWrite, options, transferStrategyBuilder);
    }

    @Override
    public Ds3ClientHelpers.Job startReadJob(final String bucket, final Iterable<Ds3Object> objectsToRead) throws IOException {
        return startReadJob(bucket, objectsToRead, ReadJobOptions.create());
    }

    @Override
    public Ds3ClientHelpers.Job startReadJob(final String bucket, final Iterable<Ds3Object> objectsToRead, final ReadJobOptions options)
            throws IOException
    {
        final List<Ds3Object> objects = Lists.newArrayList(objectsToRead);

        final GetBulkJobSpectraS3Request getBulkJobSpectraS3Request;

        if (options == null) {
            getBulkJobSpectraS3Request = makeGetBulkJobSpectraS3Request(bucket, objects, ReadJobOptions.create());
        } else {
            getBulkJobSpectraS3Request = makeGetBulkJobSpectraS3Request(bucket, objects, options);
        }

        getBulkJobSpectraS3Request.withChunkClientProcessingOrderGuarantee(JobChunkClientProcessingOrderGuarantee.NONE);

        return innerStartReadJob(objects, getBulkJobSpectraS3Request, makeTransferStrategyBuilder());
    }

    private GetBulkJobSpectraS3Request makeGetBulkJobSpectraS3Request(final String bucket, final List<Ds3Object> objects, final ReadJobOptions options) {
        return new GetBulkJobSpectraS3Request(bucket, objects)
                .withPriority(options.getPriority())
                .withName(options.getName());
    }

    private Ds3ClientHelpers.Job innerStartReadJob(final List<Ds3Object> objects,
                                                   final GetBulkJobSpectraS3Request getBulkJobSpectraS3Request,
                                                   final TransferStrategyBuilder transferStrategyBuilder)
            throws IOException
    {
        final GetBulkJobSpectraS3Response getBulkJobSpectraS3Response = this.client.getBulkJobSpectraS3(getBulkJobSpectraS3Request);

        final ImmutableMultimap<String, Range> partialRanges = PartialObjectHelpers.getPartialObjectsRanges(objects);

        final MasterObjectList masterObjectList = getBulkJobSpectraS3Response.getMasterObjectList();

        transferStrategyBuilder
                .withMasterObjectList(masterObjectList)
                .withRangesForBlobs(PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(), partialRanges));

        return new ReadJobImpl(transferStrategyBuilder);
    }

    @Override
    public Job startReadJob(final TransferStrategy transferStrategy) throws IOException {
        return new ReadJobImpl(transferStrategy);
    }

    @Override
    public Job startReadJobUsingStreamedBehavior(final String bucket, final Iterable<Ds3Object> objectsToRead) throws IOException {
        return startReadJobUsingStreamedBehavior(bucket, objectsToRead, ReadJobOptions.create());
    }

    @Override
    public Job startReadJobUsingStreamedBehavior(final String bucket, final Iterable<Ds3Object> objectsToRead, final ReadJobOptions options) throws IOException {
        Preconditions.checkNotNull(options, "options may not be null.");

        final List<Ds3Object> objects = Lists.newArrayList(objectsToRead);

        final GetBulkJobSpectraS3Request getBulkJobSpectraS3Request = makeGetBulkJobSpectraS3Request(bucket, objects, options);

        getBulkJobSpectraS3Request.withChunkClientProcessingOrderGuarantee(JobChunkClientProcessingOrderGuarantee.IN_ORDER);

        final TransferStrategyBuilder transferStrategyBuilder = makeTransferStrategyBuilder();
        transferStrategyBuilder.usingStreamedTransferBehavior();

        return innerStartReadJob(objects, getBulkJobSpectraS3Request, transferStrategyBuilder);
    }

    @Override
    public Job startReadJobUsingRandomAccessBehavior(final String bucket, final Iterable<Ds3Object> objectsToRead) throws IOException {
        return startReadJobUsingRandomAccessBehavior(bucket, objectsToRead, ReadJobOptions.create());
    }

    @Override
    public Job startReadJobUsingRandomAccessBehavior(final String bucket, final Iterable<Ds3Object> objectsToRead, final ReadJobOptions options) throws IOException {
        Preconditions.checkNotNull(options, "options may not be null.");

        final List<Ds3Object> objects = Lists.newArrayList(objectsToRead);

        final GetBulkJobSpectraS3Request getBulkJobSpectraS3Request = makeGetBulkJobSpectraS3Request(bucket, objects, options);

        getBulkJobSpectraS3Request.withChunkClientProcessingOrderGuarantee(JobChunkClientProcessingOrderGuarantee.NONE);

        final TransferStrategyBuilder transferStrategyBuilder = makeTransferStrategyBuilder();
        transferStrategyBuilder.usingRandomAccessTransferBehavior();

        return innerStartReadJob(objects, getBulkJobSpectraS3Request, transferStrategyBuilder);
    }

    @Override
    public Ds3ClientHelpers.Job startReadAllJob(final String bucket) throws IOException {
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

    private Ds3ClientHelpers.Job innerStartReadAllJob(final String bucket, final ReadJobOptions options) throws IOException {
        return this.startReadJob(bucket, makeBlobList(bucket), options);
    }

    private final Iterable<Ds3Object> makeBlobList(final String bucket) throws IOException {
        final Iterable<Contents> contentsList = listObjects(bucket);

        return toDs3Iterable(contentsList, FolderNameFilter.filter());
    }

    public Ds3ClientHelpers.Job startReadAllJobUsingStreamedBehavior(final String bucket) throws IOException {
        return startReadAllJobUsingStreamedBehavior(bucket, ReadJobOptions.create());
    }

    public Ds3ClientHelpers.Job startReadAllJobUsingStreamedBehavior(final String bucket, final ReadJobOptions options) throws IOException {
        Preconditions.checkNotNull(options, "options may not be null.");

        return startReadJobUsingStreamedBehavior(bucket, makeBlobList(bucket), options);
    }

    public Ds3ClientHelpers.Job startReadAllJobUsingRandomAccessBehavior(final String bucket) throws IOException {
        return startReadAllJobUsingRandomAccessBehavior(bucket, ReadJobOptions.create());
    }

    public Ds3ClientHelpers.Job startReadAllJobUsingRandomAccessBehavior(final String bucket, final ReadJobOptions options) throws IOException {
        Preconditions.checkNotNull(options, "options may not be null.");

        return startReadJobUsingRandomAccessBehavior(bucket, makeBlobList(bucket), options);
    }

    // TODO Need to allow the user to pass in the checksumming information again
    @Override
    public Ds3ClientHelpers.Job recoverWriteJob(final UUID jobId) throws IOException, JobRecoveryException {
        innerVerifyJobActive(jobId);

        final ModifyJobSpectraS3Response jobResponse = modifyJobSpectraS3Response(jobId, JobRequestType.PUT);

        final TransferStrategyBuilder transferStrategyBuilder = makeTransferStrategyBuilder()
                .withMasterObjectList(jobResponse.getMasterObjectListResult())
                .withChecksumType(ChecksumType.Type.NONE);

        return new WriteJobImpl(transferStrategyBuilder);
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

    private ModifyJobSpectraS3Response modifyJobSpectraS3Response(final UUID jobId, final JobRequestType jobRequestType) throws IOException, JobRecoveryException {
        final ModifyJobSpectraS3Response jobResponse = client.modifyJobSpectraS3(new ModifyJobSpectraS3Request(jobId.toString()));

        if (jobRequestType != jobResponse.getMasterObjectListResult().getRequestType()) {
            throw new JobRecoveryTypeException(jobRequestType.toString(), jobResponse.getMasterObjectListResult().getRequestType().toString());
        }

        return jobResponse;
    }

    public Ds3ClientHelpers.Job recoverWriteJobUsingStreamedBehavior(final UUID jobId) throws IOException, JobRecoveryException {
        innerVerifyJobActive(jobId);

        final ModifyJobSpectraS3Response jobResponse = modifyJobSpectraS3Response(jobId, JobRequestType.PUT);

        final TransferStrategyBuilder transferStrategyBuilder = makeTransferStrategyBuilder()
                .withMasterObjectList(jobResponse.getMasterObjectListResult())
                .withChecksumType(ChecksumType.Type.NONE)
                .usingStreamedTransferBehavior();

        return new WriteJobImpl(transferStrategyBuilder);
    }

    public Ds3ClientHelpers.Job recoverWriteJobUsingRandomAccessBehavior(final UUID jobId) throws IOException, JobRecoveryException {
        innerVerifyJobActive(jobId);

        final ModifyJobSpectraS3Response jobResponse = modifyJobSpectraS3Response(jobId, JobRequestType.PUT);

        final TransferStrategyBuilder transferStrategyBuilder = makeTransferStrategyBuilder()
                .withMasterObjectList(jobResponse.getMasterObjectListResult())
                .withChecksumType(ChecksumType.Type.NONE)
                .usingRandomAccessTransferBehavior();

        return new WriteJobImpl(transferStrategyBuilder);
    }

    @Override
    //TODO add a partial object read recovery method.  That method will require the list of partial objects.
    public Ds3ClientHelpers.Job recoverReadJob(final UUID jobId) throws IOException, JobRecoveryException {
        innerVerifyJobActive(jobId);

        final MasterObjectList masterObjectList = masterObjectListForGetJob(jobId);

        final TransferStrategyBuilder transferStrategyBuilder = makeTransferStrategyBuilder()
                .withMasterObjectList(masterObjectList)
                .withRangesForBlobs(PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(), ImmutableMultimap.<String, Range>of()));

        return new ReadJobImpl(transferStrategyBuilder);
    }

    private MasterObjectList masterObjectListForGetJob(final UUID jobId) throws IOException, JobRecoveryException {
        final ModifyJobSpectraS3Response jobResponse = modifyJobSpectraS3Response(jobId, JobRequestType.GET);

        return jobResponse.getMasterObjectListResult();
    }

    @Override
    public Job recoverReadJobsingStreamedBehavior(final UUID jobId) throws IOException, JobRecoveryException {
        innerVerifyJobActive(jobId);

        final MasterObjectList masterObjectList = masterObjectListForGetJob(jobId);

        final TransferStrategyBuilder transferStrategyBuilder = makeTransferStrategyBuilder()
                .withMasterObjectList(masterObjectList)
                .withRangesForBlobs(PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(), ImmutableMultimap.<String, Range>of()))
                .usingStreamedTransferBehavior();

        return new ReadJobImpl(transferStrategyBuilder);
    }

    @Override
    public Job recoverReadJobUsingRandomAccessBehavior(final UUID jobId) throws IOException, JobRecoveryException {
        innerVerifyJobActive(jobId);

        final MasterObjectList masterObjectList = masterObjectListForGetJob(jobId);

        final TransferStrategyBuilder transferStrategyBuilder = makeTransferStrategyBuilder()
                .withMasterObjectList(masterObjectList)
                .withRangesForBlobs(PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(), ImmutableMultimap.<String, Range>of()))
                .usingRandomAccessTransferBehavior();

        return new ReadJobImpl(transferStrategyBuilder);
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

        return this.listObjects(bucket, keyPrefix, nextMarker, maxKeys, DEFAULT_LIST_OBJECTS_RETRIES);
    }

    @Override
    public Iterable<Contents> listObjects(final String bucket, final String keyPrefix, final String nextMarker, final int maxKeys, final int retries) {

        return new LazyIterable<>(new GetBucketKeyLoaderFactory<Contents>(client, bucket, keyPrefix, DEFAULT_DELIMITER, nextMarker, maxKeys, retries, GetBucketKeyLoaderFactory.contentsFunction));
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
    public Iterable<FileSystemKey> remoteListDirectory(final String bucket, final String keyPrefix) throws IOException {
        return this.remoteListDirectory(bucket, keyPrefix, null);
    }

    @Override
    public Iterable<FileSystemKey> remoteListDirectory(final String bucket, final String keyPrefix, final String nextMarker) throws IOException {
        return this.remoteListDirectory(bucket, keyPrefix, nextMarker,1000);
    }
    @Override
    public Iterable<FileSystemKey> remoteListDirectory(final String bucket, final String keyPrefix, final String nextMarker, final int maxKeys) throws IOException {
        return remoteListDirectory(bucket,keyPrefix,"/", keyPrefix, maxKeys);
    }

    public Iterable<FileSystemKey> remoteListDirectory(final String bucket, final String keyPrefix, final String delimiter, final String nextMarker, final int maxKeys) throws IOException {
        return new LazyIterable<>(new GetBucketKeyLoaderFactory<FileSystemKey>(client, bucket, keyPrefix, delimiter, nextMarker, maxKeys, DEFAULT_LIST_OBJECTS_RETRIES, GetBucketKeyLoaderFactory.getFileSystemKeysFunction));
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
