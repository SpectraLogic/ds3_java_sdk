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

import com.google.common.collect.*;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Request;
import com.spectralogic.ds3client.commands.spectrads3.GetJobChunksReadyForClientProcessingSpectraS3Response;
import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;
import com.spectralogic.ds3client.helpers.ChunkTransferrer.ItemTransferrer;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.helpers.util.PartialObjectHelpers;
import com.spectralogic.ds3client.models.*;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.serializer.XmlProcessingException;
import com.spectralogic.ds3client.utils.Guard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.SignatureException;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

class ReadJobImpl extends JobImpl {

    private static final Logger LOG = LoggerFactory.getLogger(ReadJobImpl.class);

    private final JobPartTracker partTracker;
    private final List<Objects> chunks;
    private final ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> blobToRanges;
    private final int retryAfter; // Negative retryAfter value represent infinity retries
    private int retryAfterLeft; // The number of retries left
    private Map<MetadataReceivedListener, MetadataReceivedListener> metadataListeners;
    private Map<ChecksumListener, ChecksumListener> checksumListeners;

    public ReadJobImpl(
            final Ds3Client client,
            final MasterObjectList masterObjectList,
            final ImmutableMultimap<String, Range>
            objectRanges, final int retryAfter) {
        super(client, masterObjectList);

        this.chunks = this.masterObjectList.getObjects();
        this.partTracker = JobPartTrackerFactory
                .buildPartTracker(Iterables.concat(getAllBlobApiBeans(chunks)));
        this.blobToRanges = PartialObjectHelpers.mapRangesToBlob(masterObjectList.getObjects(), objectRanges);
        this.retryAfter = this.retryAfterLeft = retryAfter;
        this.metadataListeners = new IdentityHashMap<>();
        this.checksumListeners = new IdentityHashMap<>();
    }

    protected static ImmutableList<BulkObject> getAllBlobApiBeans(final List<Objects> jobWithChunksApiBeans) {
        ImmutableList.Builder<BulkObject> builder = ImmutableList.builder();
        for (final Objects objects : jobWithChunksApiBeans) {
            builder.addAll(objects.getObjects());
        }
        return builder.build();
    }

    @Override
    public void attachDataTransferredListener(final DataTransferredListener listener) {
        checkRunning();
        this.partTracker.attachDataTransferredListener(listener);
    }

    @Override
    public void attachObjectCompletedListener(final ObjectCompletedListener listener) {
        checkRunning();
        this.partTracker.attachObjectCompletedListener(listener);
    }

    @Override
    public void removeDataTransferredListener(final DataTransferredListener listener) {
        checkRunning();
        this.partTracker.removeDataTransferredListener(listener);
    }

    @Override
    public void removeObjectCompletedListener(final ObjectCompletedListener listener) {
        checkRunning();
        this.partTracker.removeObjectCompletedListener(listener);
    }

    @Override
    public void attachMetadataReceivedListener(final MetadataReceivedListener listener) {
        checkRunning();
        this.metadataListeners.put(listener, listener);
    }

    @Override
    public void removeMetadataReceivedListener(final MetadataReceivedListener listener) {
        checkRunning();
        this.metadataListeners.remove(listener);
    }

    @Override
    public void attachChecksumListener(final ChecksumListener listener) {
        checkRunning();
        this.checksumListeners.put(listener, listener);
    }

    @Override
    public void removeChecksumListener(final ChecksumListener listener) {
        checkRunning();
        this.checksumListeners.remove(listener);
    }

    @Override
    public Ds3ClientHelpers.Job withMetadata(final Ds3ClientHelpers.MetadataAccess access) {
        throw new IllegalStateException("withMetadata method is not used with Read Jobs");
    }

    @Override
    public Ds3ClientHelpers.Job withChecksum(final ChecksumFunction checksumFunction) {
        throw new IllegalStateException("withChecksum is not supported on Read Jobs");
    }

    @Override
    public void transfer(final ObjectChannelBuilder channelBuilder)
            throws SignatureException, IOException, XmlProcessingException {
                running = true;
        try (final JobState jobState = new JobState(
                channelBuilder,
                this.masterObjectList.getObjects(),
                partTracker, blobToRanges)) {
            final ChunkTransferrer chunkTransferrer = new ChunkTransferrer(
                new GetObjectTransferrer(jobState),
                this.client,
                jobState.getPartTracker(),
                this.maxParallelRequests
            );
            while (jobState.hasObjects()) {
                transferNextChunks(chunkTransferrer);
            }
        } catch (final RuntimeException | SignatureException | IOException | XmlProcessingException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void transferNextChunks(final ChunkTransferrer chunkTransferrer)
            throws IOException, SignatureException, XmlProcessingException, InterruptedException {
        final GetJobChunksReadyForClientProcessingSpectraS3Response availableJobChunks =
            this.client.getJobChunksReadyForClientProcessingSpectraS3(new GetJobChunksReadyForClientProcessingSpectraS3Request(this.masterObjectList.getJobId()));
        switch(availableJobChunks.getStatus()) {
        case AVAILABLE: {
            final MasterObjectList availableMol = availableJobChunks.getMasterObjectListResult();
            chunkTransferrer.transferChunks(availableMol.getNodes(), availableMol.getObjects());
            retryAfterLeft = retryAfter; // Reset the number of retries to the initial value
            break;
        }
        case RETRYLATER: {
            if (retryAfterLeft == 0) {
                throw new Ds3NoMoreRetriesException(this.retryAfter);
            }
            retryAfterLeft--;

            Thread.sleep(availableJobChunks.getRetryAfterSeconds() * 1000);
            break;
        }
        default:
            assert false : "This line of code should be impossible to hit.";
        }
    }

    private final class GetObjectTransferrer implements ItemTransferrer {
        private final JobState jobState;

        private GetObjectTransferrer(final JobState jobState) {
            this.jobState = jobState;
        }

        @Override
        public void transferItem(final Ds3Client client, final BulkObject ds3Object)
                throws SignatureException, IOException {

            final ImmutableCollection<Range> ranges = getRangesForBlob(blobToRanges, ds3Object);

            final GetObjectRequest request = new GetObjectRequest(
                    ReadJobImpl.this.masterObjectList.getBucketName(),
                    ds3Object.getName(),
                    jobState.getChannel(ds3Object.getName(), ds3Object.getOffset(), ds3Object.getLength()),
                    ReadJobImpl.this.getJobId(),
                    ds3Object.getOffset()
            );

            if (Guard.isNotNullAndNotEmpty(ranges)) {
                request.withByteRanges(ranges);
            }

            final GetObjectResponse response = client.getObject(request);
            final Metadata metadata = response.getMetadata();

            sendChecksumEvents(ds3Object, response.getChecksumType(), response.getChecksum());
            sendMetadataEvents(ds3Object.getName(), metadata);
        }
    }

    private void sendChecksumEvents(final BulkObject ds3Object, final ChecksumType.Type type, final String checksum) {
            for (final ChecksumListener listener : this.checksumListeners.values()) {
                listener.value(ds3Object, type, checksum);
            }
    }

    private void sendMetadataEvents(final String objName , final Metadata metadata) {
        for (final MetadataReceivedListener listener : this.metadataListeners.values()) {
            listener.metadataReceived(objName, metadata);
        }
    }

    private static ImmutableCollection<Range> getRangesForBlob(
            final ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> blobToRanges,
            final BulkObject ds3Object) {
        final ImmutableMultimap<BulkObject, Range> ranges =  blobToRanges.get(ds3Object.getName());
        if (ranges == null) return null;
        return ranges.get(ds3Object);
    }
}
