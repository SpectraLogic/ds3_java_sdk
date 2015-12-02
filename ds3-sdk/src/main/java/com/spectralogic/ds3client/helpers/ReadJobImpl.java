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

import com.google.common.collect.Iterables;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.commands.GetAvailableJobChunksRequest;
import com.spectralogic.ds3client.commands.GetAvailableJobChunksResponse;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.helpers.ChunkTransferrer.ItemTransferrer;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.models.bulk.BulkObject;
import com.spectralogic.ds3client.models.bulk.MasterObjectList;
import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import java.io.IOException;
import java.security.SignatureException;
import java.util.List;

class ReadJobImpl extends JobImpl {

    private final JobPartTracker partTracker;
    private final List<Objects> chunks;

    public ReadJobImpl(final Ds3Client client, final MasterObjectList masterObjectList) {
        super(client, masterObjectList);

        this.chunks = this.masterObjectList.getObjects();
        this.partTracker = JobPartTrackerFactory
                .buildPartTracker(Iterables.concat(chunks));
    }

    @Override
    public void attachDataTransferredListener(final DataTransferredListener listener) {
        this.partTracker.attachDataTransferredListener(listener);
    }

    @Override
    public void attachObjectCompletedListener(final ObjectCompletedListener listener) {
        this.partTracker.attachObjectCompletedListener(listener);
    }

    @Override
    public void removeDataTransferredListener(final DataTransferredListener listener) {
        this.partTracker.removeDataTransferredListener(listener);
    }

    @Override
    public void removeObjectCompletedListener(final ObjectCompletedListener listener) {
        this.partTracker.removeObjectCompletedListener(listener);
    }

    @Override
    public void transfer(final ObjectChannelBuilder channelBuilder)
            throws SignatureException, IOException, XmlProcessingException {
        try (final JobState jobState = new JobState(channelBuilder, this.masterObjectList.getObjects(), partTracker)) {
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
        final GetAvailableJobChunksResponse availableJobChunks =
            this.client.getAvailableJobChunks(new GetAvailableJobChunksRequest(this.masterObjectList.getJobId()));
        switch(availableJobChunks.getStatus()) {
        case AVAILABLE:
            final MasterObjectList availableMol = availableJobChunks.getMasterObjectList();
            chunkTransferrer.transferChunks(availableMol.getNodes(), availableMol.getObjects());
            break;
        case RETRYLATER:
            Thread.sleep(availableJobChunks.getRetryAfterSeconds() * 1000);
            break;
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
            client.getObject(new GetObjectRequest(
                ReadJobImpl.this.masterObjectList.getBucketName(),
                ds3Object.getName(),
                ds3Object.getOffset(),
                ReadJobImpl.this.getJobId(),
                jobState.getChannel(ds3Object.getName(), ds3Object.getOffset(), ds3Object.getLength())
            ));
        }
    }
}
