/*
 * ****************************************************************************
 *    Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

package com.spectralogic.ds3client.helpers.strategy.transferstrategy;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientBuilder;
import com.spectralogic.ds3client.commands.spectrads3.GetBulkJobSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.PutBulkJobSpectraS3Request;
import com.spectralogic.ds3client.helpers.ChecksumFunction;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.JobPartTracker;
import com.spectralogic.ds3client.helpers.JobPartTrackerFactory;
import com.spectralogic.ds3client.helpers.MetadataAccess;
import com.spectralogic.ds3client.helpers.ObjectCompletedListener;
import com.spectralogic.ds3client.helpers.ObjectPart;
import com.spectralogic.ds3client.helpers.events.EventRunner;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.helpers.events.SameThreadEventRunner;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.ChunkFilter;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.NullMasterObjectListFilter;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.OriginatingBlobChunkFilter;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.OriginatingBlobMasterObjectListFilter;
import com.spectralogic.ds3client.helpers.strategy.StrategyUtils;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.BlackPearlChunkAttemptRetryDelayBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.BlobStrategy;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.BlobStrategyMaker;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.ChunkAttemptRetryDelayBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.ClientDefinedChunkAttemptRetryDelayBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.ContinueForeverChunkAttemptsRetryBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.GetSequentialBlobStrategy;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.MasterObjectListFilter;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.MaxChunkAttemptsRetryBehavior;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.PutSequentialBlobStrategy;
import com.spectralogic.ds3client.helpers.strategy.blobstrategy.ChunkAttemptRetryBehavior;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.ChannelStrategy;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.NullChannelPreparable;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.RandomAccessChannelStrategy;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.SequentialChannelStrategy;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.SequentialFileReaderChannelStrategy;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.SequentialFileWriterChannelStrategy;
import com.spectralogic.ds3client.helpers.strategy.channelstrategy.TruncatingChannelPreparable;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.utils.Guard;
import com.spectralogic.ds3client.utils.SeekableByteChannelInputStream;
import com.spectralogic.ds3client.utils.hashing.ChecksumUtils;
import com.spectralogic.ds3client.utils.hashing.Hasher;
import com.spectralogic.ds3client.helpers.JobState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ByteChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * This class allows for building configurable behavior when putting objects to or getting objects
 * from a Black Pearl.  The intent is to provide the ability for transfers to be "streamed" or "random access".
 * Streaming, as used in a transfer strategy, means that channels and blobs are read or written sequentially.
 * You would use a streaming strategy when it is important that the source or destination channel is read or
 * written in a predictable, sequential order while a transfer is in progress.
 * Random access means that channels and blobs are read and written in no particular order.  You would use
 * a random access strategy when it is not important that a source or destination channel is read or written
 * in a predictable order while a transfer is in progress.  Once a transfer is complete, the destination channel
 * and Black Pearl objects are ordered as was the original archived object.
 *
 * You specify that you want streamed behavior by calling {@link TransferStrategyBuilder#usingStreamedTransferBehavior()}
 * and random access behavior by calling {@link TransferStrategyBuilder#usingRandomAccessTransferBehavior()}.  If you
 * specify neither, the transfer strategy used will behave as this SDK behaved before introducing the ability
 * to specify transfer behavior.
 *
 * The 2 methods to call to get an instance of a {@link TransferStrategy}, which is the interface that performs data
 * movement, are {@link TransferStrategyBuilder#makePutTransferStrategy()} and
 * {@link TransferStrategyBuilder#makeGetTransferStrategy()}.  Once you have a TransferStrategy
 * interface instance, calling {@link TransferStrategy#transfer()} on the TransferStrategy interface initiates
 * data movement.
 *
 * If using a {@link TransferStrategy} directly, as opposed to going through a {@link Ds3ClientHelpers.Job}, the minimum
 * needed as configuration items in a put transfer strategy builder are:
 * <ul>
 *     <li>{@link TransferStrategyBuilder#withDs3Client(Ds3Client)}</li>
 *     <li>{@link TransferStrategyBuilder#withMasterObjectList(MasterObjectList)}</li>
 *     <li>{@link TransferStrategyBuilder#withChannelBuilder(Ds3ClientHelpers.ObjectChannelBuilder)}</li>
 * </ul>
 *
 * When doing a get using a transfer strategy directly, you also need to specify:
 * <ul>
 *     <li>{@link TransferStrategyBuilder#withRangesForBlobs(ImmutableMap)}</li>
 * </ul>
 */
public final class TransferStrategyBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(TransferStrategyBuilder.class);

    private static final int DEFAULT_MAX_CONCURRENT_TRANSFER_THREADS = 10;
    public static final int DEFAULT_CHUNK_ATTEMPT_RETRY_INTERVAL = -1;
    public static final int DEFAULT_CHUNK_ATTEMPT_RETRY_ATTEMPTS = -1;
    public static final int DEFAULT_OBJECT_TRANSFER_ATTEMPTS = 5;

    private BlobStrategy blobStrategy;
    private ChannelStrategy channelStrategy;
    private String bucketName;
    private String jobId;
    private TransferRetryDecorator transferRetryDecorator = new MaxNumObjectTransferAttemptsDecorator(DEFAULT_OBJECT_TRANSFER_ATTEMPTS);
    private ChecksumFunction checksumFunction;
    private ChecksumType.Type checksumType = ChecksumType.Type.NONE;
    private EventRunner eventRunner = new SameThreadEventRunner();
    private EventDispatcher eventDispatcher = new EventDispatcherImpl(eventRunner);
    private JobPartTracker jobPartTracker;
    private JobState jobState;
    private int numTransferRetries = DEFAULT_OBJECT_TRANSFER_ATTEMPTS;
    private int numConcurrentTransferThreads = DEFAULT_MAX_CONCURRENT_TRANSFER_THREADS;
    private int numChunkAttemptRetries = DEFAULT_CHUNK_ATTEMPT_RETRY_ATTEMPTS;
    private int chunkRetryDelayInSeconds = DEFAULT_CHUNK_ATTEMPT_RETRY_INTERVAL;
    private Ds3Client ds3Client;
    private MasterObjectList masterObjectList;
    private Ds3ClientHelpers.ObjectChannelBuilder channelBuilder;
    private ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> rangesForBlobs;
    private MetadataAccess metadataAccess;
    private ChunkAttemptRetryBehavior chunkAttemptRetryBehavior;
    private ChunkAttemptRetryDelayBehavior chunkAttemptRetryDelayBehavior;
    private TransferBehaviorType transferBehaviorType = TransferBehaviorType.OriginalSdkTransferBehavior;
    private FailureEvent.FailureActivity failureActivity = FailureEvent.FailureActivity.PuttingObject;
    private boolean usingJobAggregation = false;
    private Iterable<Ds3Object> objectsInJob;
    private ChunkFilter chunkFilter;
    private MasterObjectListFilter masterObjectListFilter;

    /**
     * Use an instance of {@link BlobStrategy} you wish to create or retrieve blobs from a Black Pearl.  There are
     * 2 primary implementations: {@link PutSequentialBlobStrategy}; and {@link GetSequentialBlobStrategy}.  Blob
     * retrieval order is specified in the
     * {@link com.spectralogic.ds3client.commands.spectrads3.GetBulkJobSpectraS3Request#withChunkClientProcessingOrderGuarantee(com.spectralogic.ds3client.models.JobChunkClientProcessingOrderGuarantee)}
     * HTTP request.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish,
     * for example:
     * <pre>
     *     {@code
     *     final PutBulkJobSpectraS3Request request = new PutBulkJobSpectraS3Request(BUCKET_NAME, Lists.newArrayList(objectsToWrite));
     *     final PutBulkJobSpectraS3Response putBulkJobSpectraS3Response = client.putBulkJobSpectraS3(request);
     *
     *     final MasterObjectList masterObjectList = putBulkJobSpectraS3Response.getResult();
     *
     *     final EventDispatcher eventDispatcher = new EventDispatcherImpl(new SameThreadEventRunner());
     *
     *     final AtomicInteger numChunkAllocationAttempts = new AtomicInteger(0);
     *
     *     final BlobStrategy blobStrategy = new UserSuppliedPutBlobStrategy(client,
     *                                                                       masterObjectList,
     *                                                                       eventDispatcher,
     *                                                                       new MaxChunkAttemptsRetryBehavior(5),
     *                                                                       new ClientDefinedChunkAttemptRetryDelayBehavior(1, eventDispatcher),
     *                                                                           new Monitorable() {
     *                                                                               @Override
     *                                                                                   public void monitor() {
     *                                                                                       numChunkAllocationAttempts.incrementAndGet();
     *                                                                                   }
     *                                                                           });
     *
     *     final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
     *         .withDs3Client(Ds3ClientBuilder.fromEnv().withHttps(false).build())
     *         .withMasterObjectList(masterObjectList)
     *         .withChannelBuilder(new FileObjectPutter(dirPath))
     *         .withBlobStrategy(blobStrategy);
     *
     *     final TransferStrategy transferStrategy = transferStrategyBuilder.makePutTransferStrategy();
     *     transferStrategy.transfer();
     * </pre>
     */
    public TransferStrategyBuilder withBlobStrategy(final BlobStrategy blobStrategy) {
        this.blobStrategy = blobStrategy;
        return this;
    }

    /**
     * Use an instance of {@link ChannelStrategy} you wish to manage the source or destination of a transfer.
     * The 2 primary channel strategies are {@link RandomAccessChannelStrategy} and {@link SequentialChannelStrategy}.
     * A channel strategy normally has a {@link Ds3ClientHelpers.ObjectChannelBuilder} that allocates the
     * source or destination.  Random access behavior creates a channel per blob; streamed access creates a single
     * channel to reference all blobs associated with a DS3 object.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withChannelStrategy(final ChannelStrategy channelStrategy) {
        this.channelStrategy = channelStrategy;
        return this;
    }

    /**
     * The {@link Ds3ClientHelpers.ObjectChannelBuilder} you wish to use to allocate source or destination
     * channels.  This is used in a {@link ChannelStrategy} to manage streamed or random access behavior.
     * This is the same interface used in {@link Ds3ClientHelpers.Job#transfer(Ds3ClientHelpers.ObjectChannelBuilder)}.
     * The 2 primary implementations of this interface are {@link com.spectralogic.ds3client.helpers.FileObjectGetter}
     * and {@link com.spectralogic.ds3client.helpers.FileObjectPutter}.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withChannelBuilder(final Ds3ClientHelpers.ObjectChannelBuilder channelBuilder) {
        this.channelBuilder = channelBuilder;
        return this;
    }

    /**
     * The {@link TransferRetryDecorator} to use when retrying a failed transfer attempt.  If not specified directly,
     * the builder will create a retry decorator based on the value specified in
     * {@link TransferStrategyBuilder#withNumTransferRetries(int)}.  The 2 primary transfer retry decorators
     * are {@link MaxNumObjectTransferAttemptsDecorator} and {@link ContinueForeverTransferRetryDecorator}
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withTransferRetryDecorator(final TransferRetryDecorator transferRetryDecorator) {
        this.transferRetryDecorator = transferRetryDecorator;
        return this;
    }

    /**
     * The transfer function to use when putting an object to a Black Pearl when the value in
     * {@link TransferStrategyBuilder#withChecksumType(ChecksumType.Type)} is not
     * {@link ChecksumType.None}.  This builder will make a checksum function of the correct type
     * if you specify a checksum type other than {@link ChecksumType.None} but do not specify a
     * checksum function.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withChecksumFunction(final ChecksumFunction checksumFunction) {
        this.checksumFunction = checksumFunction;
        return this;
    }

    /**
     * The {@link ChecksumType.None} you would like to compute and include in the payload sent to a
     * Black Pearl when putting an object.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withChecksumType(final ChecksumType.Type checksumType) {
        this.checksumType = checksumType;
        return this;
    }

    /**
     * The {@link EventDispatcher} you would like to use when registering as an event observer and emitting
     * events.  The primary event dispatcher, {@link EventDispatcherImpl}, uses an {@link EventRunner} to deliver events.
     * You specify the event runner behavior you want by calling {@link TransferStrategyBuilder#withEventRunner(EventRunner)}.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withEventDispatcher(final EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
        return this;
    }

    /**
     * {@link Ds3ClientHelpers.Job} uses the event dispatcher configured into this builder to emit events
     * during a transfer.
     */
    public EventDispatcher eventDispatcher() {
        return eventDispatcher;
    }

    /**
     * When not directly specifying a {@link TransferRetryDecorator}, this property is used to make a transfer
     * retry decorator instance.  Doing so uses the behavior this SDK used prior to introducing transfer behavior
     * configuration: a value less than 0 causes a failed transfer to be retried indefinitely; a value greater than 0
     * causes a failed transfer to be retried no more than the value specified before giving up.  If you do not
     * specify a value, this builder will use the same value as used in this SDK prior to introducing transfer behavior.
     * @param numRetries
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withNumTransferRetries(final int numRetries) {
        this.numTransferRetries = numRetries;
        return this;
    }

    /**
     * The number of threads used to transfer blobs.  Random access behavior uses the number of threads you
     * specify in this property.  Streamed behavior uses a single thread when transferring blobs.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withNumConcurrentTransferThreads(final int numConcurrentTransferThreads) {
        this.numConcurrentTransferThreads = numConcurrentTransferThreads;
        return this;
    }

    /**
     * When not directly using {@link TransferStrategyBuilder#withChunkAttemptRetryBehavior(ChunkAttemptRetryBehavior)},
     * this property is used to make a {@link ChunkAttemptRetryBehavior} instance.  Doing so uses the behavior this SDK used prior to introducing transfer behavior
     * configuration: a value less than 0 causes a failed chunk operation to be retried indefinitely; a value greater than 0
     * causes a failed chunk operation to be retried no more than the value specified before giving up.  If you do not
     * specify a value, this builder will use the same value as used in this SDK prior to introducing transfer behavior.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withNumChunkAttemptRetries(final int numChunkAttemptRetries) {
        this.numChunkAttemptRetries = numChunkAttemptRetries;
        return this;
    }

    /**
     * When not directly using {@link TransferStrategyBuilder#withChunkAttemptRetryDelayBehavior(ChunkAttemptRetryDelayBehavior)},
     * this property is used to create a {@link ChunkAttemptRetryDelayBehavior} instance.  Doing so uses the behavior this SDK used prior to introducing transfer behavior
     * configuration: a value less than 0 causes a failed chunk operation to use the retry delay value a Black Pearl returns
     * as part of the chunk operation failure response; a value greater than 0
     * causes a failed chunk operation to the value specified in the {@code retryDelayInSeconds} parameter.  If you do not
     * specify a value, this builder will use the same value as used in this SDK prior to introducing transfer behavior.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withChunkRetryDelayInSeconds(final int retryDelayInSeconds) {
        this.chunkRetryDelayInSeconds = retryDelayInSeconds;
        return this;
    }

    /**
     * The {@link Ds3Client} instance representing the Black Pearl you want to work with.  The primary way to
     * get a client instance is to call {@link Ds3ClientBuilder#fromEnv()}.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withDs3Client(final Ds3Client ds3Client) {
        this.ds3Client = ds3Client;
        return this;
    }

    /**
     * The {@link MasterObjectList} returned primarily retrieved from a call to {@link Ds3Client#putBulkJobSpectraS3(PutBulkJobSpectraS3Request)}
     * or {@link Ds3Client#getBulkJobSpectraS3(GetBulkJobSpectraS3Request)}.  The master object list is used in
     * creating the ability to work with a blob strategy.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withMasterObjectList(final MasterObjectList masterObjectList) {
        this.masterObjectList = masterObjectList;
        jobId = masterObjectList.getJobId().toString();
        bucketName = masterObjectList.getBucketName();
        return this;
    }

    public MasterObjectList masterObjectList() {
        return filterChunksContainingBlobsNotOriginallyIncludedInJob();
    }

    private MasterObjectList filterChunksContainingBlobsNotOriginallyIncludedInJob() {
        Preconditions.checkNotNull(masterObjectList, "masterObjectList may not be null.");

        if ( ! usingJobAggregation && ! masterObjectList.getAggregating()) {
            return masterObjectList;
        }

        return getOrCreateMasterObjectListFilter().apply(masterObjectList);
    }

    private MasterObjectListFilter getOrCreateMasterObjectListFilter() {
        if (masterObjectListFilter == null) {
            withMasterObjectListFilter(new OriginatingBlobMasterObjectListFilter(getOrCreateChunkFilter()));
        }

        return masterObjectListFilter;
    }

    private ChunkFilter getOrCreateChunkFilter() {
        Preconditions.checkNotNull(objectsInJob, "objectsInJob may not be null.");

        if (chunkFilter == null) {
            withChunkFilter(new OriginatingBlobChunkFilter(objectsInJob));
        }

        return chunkFilter;
    }

    /**
     * Call this method when you wish put jobs to combine separate put jobs into one.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withJobAggregation() {
        usingJobAggregation = true;
        return this;
    }

    /**
     * When aggregating jobs from more than one process, it is possible that one process will see a
     * {@link com.spectralogic.ds3client.models.MasterObjectList}
     * that contains blobs defined in the other process.  To prevent one process from trying to transfer
     * blobs defined in another process, we apply a filter to master object lists to eliminate blobs not
     * originally defined in a particular process.  The {@link MasterObjectListFilter} and {@link ChunkFilter}
     * work together to filter the chunks in a master object list.  The {@link OriginatingBlobChunkFilter}
     * implementation uses the names of the {@link Ds3Object} originally included in job creation to know what
     * chunks to filter.
     * @param objectsInJob The {@link Ds3Object} originally included in job creation.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withObjectsInJob(final Iterable<Ds3Object> objectsInJob) {
        this.objectsInJob = objectsInJob;
        return this;
    }

    /**
     * When aggregating jobs from more than one process, it is possible that one process will see a
     * {@link com.spectralogic.ds3client.models.MasterObjectList}
     * that contains blobs defined in the other process.  To prevent one process from trying to transfer
     * blobs defined in another process, we apply a filter to master object lists to eliminate blobs not
     * originally defined in a particular process.  The ChunkFilter interface implements the behavior
     * in a {@link MasterObjectListFilter} filter that decides what chunks to include in the resultant
     * master object list.
     * @param chunkFilter An instance of {@link ChunkFilter} whose behavior you wish to decide which chunks get
     *                    filtered.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withChunkFilter(final ChunkFilter chunkFilter) {
        this.chunkFilter = chunkFilter;
        return this;
    }

    /**
     * When aggregating jobs from more than one process, it is possible that one process will see a
     * {@link com.spectralogic.ds3client.models.MasterObjectList}
     * that contains blobs defined in the other process.  To prevent one process from trying to transfer
     * blobs defined in another process, we apply a filter to master object lists to eliminate blobs not
     * originally defined in a particular process.
     * @param masterObjectListFilter An instance of {@link MasterObjectListFilter} whose behavior you wish to decide which chunks get
     *                    filtered.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withMasterObjectListFilter(final MasterObjectListFilter masterObjectListFilter) {
        this.masterObjectListFilter = masterObjectListFilter;
        return this;
    }

    /**
     * When getting portions of objects from a Black Pearl, object ranges are the mechanism used to tell the Black Pearl
     * which portions of obejcts to get.
     * @param rangesForBlobs When retrieving whole objects, you supply an empty range, e.g.:
     *                       <pre>
     *                       {@code
     *                       final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
     *                           .withDs3Client(client)
     *                           .withMasterObjectList(masterObjectList)
     *                           .withChannelBuilder(new FileObjectGetter(tempDirectory))
     *                           .withRangesForBlobs(PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(),
     *                               PartialObjectHelpers.getPartialObjectsRanges(objects)));
     *                       }
     *                       </pre>
     *
     *                       When retrieving partial objects, you can build a list by doing something like:
     *                       <pre>
     *                       {@code
     *                       final List<Ds3Object> filesToGet = new ArrayList<>();
     *                       final int offsetIntoFirstRange = 10;
     *                       filesToGet.add(new PartialDs3Object(FILE_NAME, Range.byLength(200000, 100000)));
     *                       filesToGet.add(new PartialDs3Object(FILE_NAME, Range.byLength(100000, 100000)));
     *                       filesToGet.add(new PartialDs3Object(FILE_NAME, Range.byLength(offsetIntoFirstRange, 100000)));
     *
     *                       final TransferStrategyBuilder transferStrategyBuilder = new TransferStrategyBuilder()
     *                           .withDs3Client(client)
     *                           .withMasterObjectList(masterObjectList)
     *                           .withChannelBuilder(new FileObjectGetter(tempDirectory))
     *                           .withRangesForBlobs(PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(),
     *                               PartialObjectHelpers.getPartialObjectsRanges(filesToGet)));
     *                       }
     *                       </pre>
     *
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withRangesForBlobs(final ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> rangesForBlobs) {
        this.rangesForBlobs = rangesForBlobs;
        return this;
    }

    /**
     * Supply an instance of {@link MetadataAccess} to record metadata as part pf a put transfer.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withMetadataAccess(final MetadataAccess metadataAccess) {
        this.metadataAccess = metadataAccess;
        return this;
    }

    /**
     * Supply an instance of {@link ChunkAttemptRetryBehavior} when you need to have specialized control over retrying
     * a failed chunk operation.  The 2 primary implementations for this are {@link MaxChunkAttemptsRetryBehavior}
     * and {@link ContinueForeverChunkAttemptsRetryBehavior}.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withChunkAttemptRetryBehavior(final ChunkAttemptRetryBehavior chunkAttemptRetryBehavior) {
        this.chunkAttemptRetryBehavior = chunkAttemptRetryBehavior;
        return this;
    }

    /**
     * Supply an instance of {@link ChunkAttemptRetryDelayBehavior} when you need to have specialized control over the length
     * of time to delay between retrying a filed chunk operation.  The 2 primary implementations are {@link ClientDefinedChunkAttemptRetryDelayBehavior},
     * which will always use the delay interval a client specifies, and {@link BlackPearlChunkAttemptRetryDelayBehavior},
     * which will use the retry delay a Black Pearl returns in its payload when a chunk operation cannot be completed.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withChunkAttemptRetryDelayBehavior(final ChunkAttemptRetryDelayBehavior chunkAttemptRetryDelayBehavior) {
        this.chunkAttemptRetryDelayBehavior = chunkAttemptRetryDelayBehavior;
        return this;
    }

    /**
     * Specify an {@link EventRunner} to use when dispatching events.  The primary reason for specifying this is to determine
     * which threading model is used to deliver events.  If it is important that events are delivered on the same thread,
     * meaning that the deleivery order is deterministic, use {@link SameThreadEventRunner}, which is the event runner instance this SDK uses.
     * You may specify the {@link com.spectralogic.ds3client.helpers.events.ConcurrentEventRunner} if you want events
     * delivered on separate threads.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder withEventRunner(final EventRunner eventRunner) {
        this.eventRunner = eventRunner;
        return this;
    }

    /**
     * Setting this property causes this SDK to read and write channels from beginning to end sequentially and causes
     * blob retrieval to be in order.  Transfers happen on a single-threaded executor.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder usingStreamedTransferBehavior() {
        transferBehaviorType = TransferBehaviorType.StreamingTransferBehavior;
        return this;
    }

    /**
     * Setting this property causes this SDK to read and write channels in no particular order and
     * specifies no order in retrieving blobs.  Transfers happen in a multi-threaded executor.
     * @return The instance of this builder, with the intent that you can string together the behaviors you wish.
     */
    public TransferStrategyBuilder usingRandomAccessTransferBehavior() {
        transferBehaviorType = TransferBehaviorType.RandomAccessTransferBehavior;
        return this;
    }

    /**
     * Get an instance of {@link TransferStrategy} configured with this builders current settings.  Once you
     * have an instance, you initiate a put by calling {@link TransferStrategy#transfer()}.
     * @return Once you have an instance, you initiate a get by calling {@link TransferStrategy#transfer()}.
     */
    public TransferStrategy makePutTransferStrategy() {
        failureActivity = FailureEvent.FailureActivity.PuttingObject;

        switch (transferBehaviorType) {
            case StreamingTransferBehavior:
                return makeStreamingPutTransferStrategy();

            case RandomAccessTransferBehavior:
                return makeRandomAccessPutTransferStrategy();

            case OriginalSdkTransferBehavior:
            default:
                return makeOriginalSdkSemanticsPutTransferStrategy();
        }
    }

    private TransferStrategy makeStreamingPutTransferStrategy() {
        maybeMakeStreamedPutChannelStrategy();
        getOrMakeTransferRetryDecorator();

        return makeTransferStrategy(
                new BlobStrategyMaker() {
                    @Override
                    public BlobStrategy makeBlobStrategy(final Ds3Client client,
                                                         final MasterObjectList masterObjectList,
                                                         final EventDispatcher eventDispatcher)
                    {
                        return new PutSequentialBlobStrategy(ds3Client,
                                masterObjectList,
                                eventDispatcher,
                                getOrMakeChunkAttemptRetryBehavior(),
                                getOrMakeChunkAllocationRetryDelayBehavior()
                        );
                    }
                },
                new TransferMethodMaker() {
                    @Override
                    public TransferMethod makeTransferMethod() {
                        return makePutTransferMethod();
                    }
                });
    }

    private void maybeMakeStreamedPutChannelStrategy() {
        if (channelStrategy == null) {
            Preconditions.checkNotNull(channelBuilder, "channelBuilder my not be null");

            channelStrategy = new SequentialChannelStrategy(new SequentialFileReaderChannelStrategy(channelBuilder),
                    channelBuilder, new NullChannelPreparable());
        }
    }

    private TransferRetryDecorator getOrMakeTransferRetryDecorator() {
        if (transferRetryDecorator != null) {
            return transferRetryDecorator;
        }

        if (numTransferRetries > 0) {
            transferRetryDecorator = new MaxNumObjectTransferAttemptsDecorator(numTransferRetries);
        } else {
            transferRetryDecorator = new ContinueForeverTransferRetryDecorator();
        }

        return transferRetryDecorator;
    }

    private TransferStrategy makeTransferStrategy(final BlobStrategyMaker blobStrategyMaker,
                                                  final TransferMethodMaker transferMethodMaker)
    {
        Preconditions.checkNotNull(ds3Client, "ds3Client may not be null.");
        Preconditions.checkNotNull(eventDispatcher, "eventDispatcher may not be null.");
        Preconditions.checkNotNull(masterObjectList(), "masterObjectList may not be null.");
        Preconditions.checkNotNull(blobStrategyMaker, "blobStrategyMaker may not be null.");
        Preconditions.checkNotNull(transferMethodMaker, "transferMethodMaker may not be null.");
        Preconditions.checkNotNull(channelStrategy, "channelStrategy may not be null");
        Preconditions.checkNotNull(transferRetryDecorator, "transferRetryDecorator may not be null");
        Guard.throwOnNullOrEmptyString(bucketName, "bucketName may not be null or empty.");
        Guard.throwOnNullOrEmptyString(jobId, "jobId may not be null or empty.");

        maybeMakeBlobStrategy(blobStrategyMaker);

        eventDispatcher.attachBlobTransferredEventObserver(new BlobTransferredEventObserver(new UpdateStrategy<BulkObject>() {
            @Override
            public void update(final BulkObject eventData) {
                jobPartTracker.completePart(eventData.getName(), new ObjectPart(eventData.getOffset(), eventData.getLength()));
            }
        }));

        return makeTransferStrategy(transferMethodMaker.makeTransferMethod());
    }

    private void maybeMakeBlobStrategy(final BlobStrategyMaker blobStrategyMaker) {
        if (blobStrategy == null) {
            blobStrategy = blobStrategyMaker.makeBlobStrategy(ds3Client, masterObjectList(), eventDispatcher);
        }
    }

    private TransferStrategy makeTransferStrategy(final TransferMethod transferMethod) {
        switch (transferBehaviorType) {
            case StreamingTransferBehavior:
                return new SingleThreadedTransferStrategy(blobStrategy,
                        jobState,
                        eventDispatcher,
                        masterObjectList(),
                        failureActivity)
                        .withTransferMethod(transferMethod);

            case RandomAccessTransferBehavior:
                return new MultiThreadedTransferStrategy(blobStrategy,
                        jobState,
                        numConcurrentTransferThreads,
                        eventDispatcher,
                        masterObjectList(),
                        failureActivity)
                        .withTransferMethod(transferMethod);

            case OriginalSdkTransferBehavior:
            default:
                return makeOriginalSdkSemanticsTransferStrategy(transferMethod);
        }
    }

    private ChunkAttemptRetryBehavior getOrMakeChunkAttemptRetryBehavior() {
        if (chunkAttemptRetryBehavior != null) {
            return chunkAttemptRetryBehavior;
        }

        if (numChunkAttemptRetries > 0) {
            chunkAttemptRetryBehavior = new MaxChunkAttemptsRetryBehavior(numChunkAttemptRetries);
        } else {
            chunkAttemptRetryBehavior = new ContinueForeverChunkAttemptsRetryBehavior();
        }

        return chunkAttemptRetryBehavior;
    }

    private ChunkAttemptRetryDelayBehavior getOrMakeChunkAllocationRetryDelayBehavior() {
        Preconditions.checkNotNull(eventDispatcher, "eventDispatcher may not be null.");

        if (chunkAttemptRetryDelayBehavior != null) {
            return chunkAttemptRetryDelayBehavior;
        }

        if (chunkRetryDelayInSeconds > 0) {
            chunkAttemptRetryDelayBehavior = new ClientDefinedChunkAttemptRetryDelayBehavior(chunkRetryDelayInSeconds, eventDispatcher);
        } else {
            chunkAttemptRetryDelayBehavior = new BlackPearlChunkAttemptRetryDelayBehavior(eventDispatcher);
        }

        return chunkAttemptRetryDelayBehavior;
    }

    private TransferStrategy makeOriginalSdkSemanticsPutTransferStrategy() {
        maybeMakeRandomAccessPutChannelStrategy();
        getOrMakeTransferRetryDecorator();

        return makeTransferStrategy(
                new BlobStrategyMaker() {
                    @Override
                    public BlobStrategy makeBlobStrategy(final Ds3Client client,
                                                         final MasterObjectList masterObjectList,
                                                         final EventDispatcher eventDispatcher)
                    {
                        return new PutSequentialBlobStrategy(ds3Client,
                                masterObjectList,
                                eventDispatcher,
                                getOrMakeChunkAttemptRetryBehavior(),
                                getOrMakeChunkAllocationRetryDelayBehavior()
                                );
                    }
                },
                new TransferMethodMaker() {
                    @Override
                    public TransferMethod makeTransferMethod() {
                        return makePutTransferMethod();
                    }
                });
    }

    private void maybeMakeRandomAccessPutChannelStrategy() {
        if (channelStrategy == null) {
            Preconditions.checkNotNull(channelBuilder, "channelBuilder my not be null");
            channelStrategy = new RandomAccessChannelStrategy(channelBuilder, rangesForBlobs, new NullChannelPreparable());
        }
    }

    private TransferMethod makePutTransferMethod() {
        getOrMakeJobStateForPutJob();

        if (checksumType != ChecksumType.Type.NONE) {
            maybeAddChecksumFunction();
        }

        final TransferMethod transferMethod = new PutJobTransferMethod(channelStrategy,
                bucketName, jobId, eventDispatcher, checksumFunction, checksumType, metadataAccess);

        if (transferRetryDecorator != null) {
            return transferRetryDecorator.wrap(transferMethod);
        }

        return transferMethod;
    }

    private JobState getOrMakeJobStateForPutJob() {
        if (jobState != null) {
            return jobState;
        }

        Preconditions.checkNotNull(masterObjectList(), "masterObjectList may not be null.");
        Preconditions.checkNotNull(eventDispatcher, "eventDispatcher may not be null.");
        Preconditions.checkNotNull(eventRunner, "eventRunner may not be null.");

        List<Objects> chunks = masterObjectList().getObjects();

        if (chunks == null) {
            chunks = new ArrayList<>();
        }

        final List<Objects> chunksNotYetCompleted = StrategyUtils.filterChunks(chunks);

        getOrMakeJobPartTrackerForPutJob(chunksNotYetCompleted);

        jobState = new JobState(chunksNotYetCompleted);

        return jobState;
    }

    private void maybeAddChecksumFunction() {
        if (checksumFunction == null) {
            makeDefaultChecksumFunction();
        }
    }

    private TransferStrategy makeOriginalSdkSemanticsTransferStrategy(final TransferMethod transferMethod) {
        if (numConcurrentTransferThreads > 1) {
            return new MultiThreadedTransferStrategy(blobStrategy,
                    jobState,
                    numConcurrentTransferThreads,
                    eventDispatcher,
                    masterObjectList(),
                    failureActivity)
                    .withTransferMethod(transferMethod);
        } else {
            return new SingleThreadedTransferStrategy(blobStrategy,
                    jobState,
                    eventDispatcher,
                    masterObjectList(),
                    failureActivity)
                    .withTransferMethod(transferMethod);
        }
    }

    private JobPartTracker getOrMakeJobPartTrackerForPutJob(final List<Objects> chunksNotYetCompleted) {
        if (jobPartTracker != null) {
            return jobPartTracker;
        }

        final JobPartTracker result = JobPartTrackerFactory.buildPartTracker(Iterables.concat(getBlobs(chunksNotYetCompleted)), eventRunner);

        result.attachObjectCompletedListener(new ObjectCompletedListener() {
            @Override
            public void objectCompleted(final String name) {
                eventDispatcher.emitObjectCompletedEvent(name);
            }
        });

        jobPartTracker = result;

        return jobPartTracker;
    }

    private ImmutableList<BulkObject> getBlobs(final List<Objects> chunks) {
        final ImmutableList.Builder<BulkObject> builder = ImmutableList.builder();
        for (final Objects objects : chunks) {
            builder.addAll(objects.getObjects());
        }
        return builder.build();
    }

    private void makeDefaultChecksumFunction() {
        final ChecksumFunction newChecksumFunction = new ChecksumFunction() {
            @Override
            public String compute(final BulkObject obj, final ByteChannel channel) {
                String checksum = null;

                try
                {
                    final InputStream dataStream = new SeekableByteChannelInputStream(channelStrategy.acquireChannelForBlob(obj));

                    dataStream.mark(Integer.MAX_VALUE);

                    final Hasher hasher = ChecksumUtils.getHasher(checksumType);

                    checksum = ChecksumUtils.hashInputStream(hasher, dataStream);

                    LOG.info("Computed checksum for blob: {}", checksum);

                    dataStream.reset();
                } catch (final IOException e) {
                    eventDispatcher.emitFailureEvent(FailureEvent.builder()
                            .withObjectNamed(obj.getName())
                            .withCausalException(e)
                            .usingSystemWithEndpoint(ds3Client.getConnectionDetails().getEndpoint())
                            .doingWhat(FailureEvent.FailureActivity.ComputingChecksum)
                            .build());
                    LOG.error("Error computing checksum.", e);
                }

                return checksum;
            }
        };

        checksumFunction = newChecksumFunction;
    }

    private TransferStrategy makeRandomAccessPutTransferStrategy() {
        maybeMakeRandomAccessPutChannelStrategy();
        getOrMakeTransferRetryDecorator();

        return makeTransferStrategy(
                new BlobStrategyMaker() {
                    @Override
                    public BlobStrategy makeBlobStrategy(final Ds3Client client,
                                                         final MasterObjectList masterObjectList,
                                                         final EventDispatcher eventDispatcher)
                    {
                        return new PutSequentialBlobStrategy(ds3Client,
                                masterObjectList,
                                eventDispatcher,
                                getOrMakeChunkAttemptRetryBehavior(),
                                getOrMakeChunkAllocationRetryDelayBehavior()
                        );
                    }
                },
                new TransferMethodMaker() {
                    @Override
                    public TransferMethod makeTransferMethod() {
                        return makePutTransferMethod();
                    }
                });
    }

    /**
     * Get an instance of {@link TransferStrategy} configured with this builders current settings.
     * @return  Once you have an instance, you initiate a get by calling {@link TransferStrategy#transfer()}.
     */
    public TransferStrategy makeGetTransferStrategy() {
        failureActivity = FailureEvent.FailureActivity.GettingObject;

        switch (transferBehaviorType) {
            case StreamingTransferBehavior:
                return makeStreamingGetTransferStrategy();

            case RandomAccessTransferBehavior:
            case OriginalSdkTransferBehavior:
            default:
                return makeOriginalSdkSemanticsGetTransferStrategy();
        }
    }

    private TransferStrategy makeStreamingGetTransferStrategy() {
        maybeMakeSequentialGetChannelStrategy();
        getOrMakeTransferRetryDecorator();

        return makeTransferStrategy(
                new BlobStrategyMaker() {
                    @Override
                    public BlobStrategy makeBlobStrategy(final Ds3Client client, final MasterObjectList masterObjectList, final EventDispatcher eventDispatcher) {
                        return new GetSequentialBlobStrategy(ds3Client,
                                masterObjectList,
                                eventDispatcher,
                                getOrMakeChunkAttemptRetryBehavior(),
                                getOrMakeChunkAllocationRetryDelayBehavior(),
                                getMasterObjectListFilterForBlobStrategy(masterObjectList));
                    }
                },
                new TransferMethodMaker() {
                    @Override
                    public TransferMethod makeTransferMethod() {
                        return makeGetTransferMethod();
                    }
                });
    }

    private void maybeMakeSequentialGetChannelStrategy() {
        if (channelStrategy == null) {
            Preconditions.checkNotNull(channelBuilder, "channelBuilder my not be null");

            channelStrategy = new SequentialChannelStrategy(new SequentialFileWriterChannelStrategy(channelBuilder),
                    channelBuilder, new TruncatingChannelPreparable());
        }
    }

    private TransferMethod makeGetTransferMethod() {
        getOrMakeJobStateForGetJob();

        final TransferMethod transferMethod = new GetJobNetworkFailureRetryDecorator(channelStrategy,
                bucketName, jobId, eventDispatcher, rangesForBlobs);

        if (transferRetryDecorator != null) {
            return transferRetryDecorator.wrap(transferMethod);
        }

        return transferMethod;
    }

    private JobState getOrMakeJobStateForGetJob() {
        if (jobState != null) {
            return jobState;
        }

        Preconditions.checkNotNull(masterObjectList(), "masterObjectList may not be null.");
        Preconditions.checkNotNull(eventDispatcher, "eventDispatcher may not be null.");
        Preconditions.checkNotNull(eventRunner, "eventRunner may not be null.");

        final List<Objects> chunks = masterObjectList().getObjects();

        getOrMakeJobPartTrackerForGetJob(chunks);

        jobState = new JobState(chunks);

        return jobState;
    }

    private JobPartTracker getOrMakeJobPartTrackerForGetJob(final List<Objects> chunks) {
        if (jobPartTracker != null) {
            return jobPartTracker;
        }

        final JobPartTracker result = JobPartTrackerFactory.buildPartTracker(getBlobs(chunks), eventRunner);

        result.attachObjectCompletedListener(new ObjectCompletedListener() {
            @Override
            public void objectCompleted(final String name) {
                eventDispatcher.emitObjectCompletedEvent(name);
            }
        });

        jobPartTracker = result;

        return jobPartTracker;
    }

    private MasterObjectListFilter getMasterObjectListFilterForBlobStrategy(final MasterObjectList masterObjectList) {
        if (masterObjectList.getAggregating()) {
            return getOrCreateMasterObjectListFilter();
        }

        return new NullMasterObjectListFilter();
    }

    private TransferStrategy makeOriginalSdkSemanticsGetTransferStrategy() {
        maybeMakeRandomAccessGetChannelStrategy();
        getOrMakeTransferRetryDecorator();

        return makeTransferStrategy(
                new BlobStrategyMaker() {
                    @Override
                    public BlobStrategy makeBlobStrategy(final Ds3Client client,
                                                         final MasterObjectList masterObjectList,
                                                         final EventDispatcher eventDispatcher) {
                        return new GetSequentialBlobStrategy(ds3Client,
                                masterObjectList,
                                eventDispatcher,
                                getOrMakeChunkAttemptRetryBehavior(),
                                getOrMakeChunkAllocationRetryDelayBehavior(),
                                getMasterObjectListFilterForBlobStrategy(masterObjectList));
                    }
                },
                new TransferMethodMaker() {
                    @Override
                    public TransferMethod makeTransferMethod() {
                        return makeGetTransferMethod();
                    }
                });
    }

    private void maybeMakeRandomAccessGetChannelStrategy() {
        if (channelStrategy == null) {
            Preconditions.checkNotNull(channelBuilder, "channelBuilder my not be null");

            channelStrategy = new RandomAccessChannelStrategy(channelBuilder, rangesForBlobs, new TruncatingChannelPreparable());
        }
    }

    private enum TransferBehaviorType {
        OriginalSdkTransferBehavior,
        StreamingTransferBehavior,
        RandomAccessTransferBehavior
    }
}
