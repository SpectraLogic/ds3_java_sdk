/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers;

import com.google.common.collect.Iterables;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.Job;
import com.spectralogic.ds3client.helpers.events.EventRunner;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.spectralogic.ds3client.helpers.ReadJobImpl.getAllBlobApiBeans;

abstract class JobImpl implements Job {
    protected final Ds3Client client;
    protected final MasterObjectList masterObjectList;
    protected boolean running = false;
    protected int maxParallelRequests = 10;
    private final int objectTransferAttempts;
    private final JobPartTrackerDecorator jobPartTracker;

    public JobImpl(final Ds3Client client,
                   final MasterObjectList masterObjectList,
                   final int objectTransferAttempts,
                   final EventRunner eventRunner) {
        this.client = client;
        this.masterObjectList = masterObjectList;
        this.objectTransferAttempts = objectTransferAttempts;
        this.jobPartTracker = makeJobPartTracker(getChunks(masterObjectList), eventRunner);
    }
    
    @Override
    public UUID getJobId() {
        if (this.masterObjectList == null) {
            return null;
        }
        return this.masterObjectList.getJobId();
    }

    @Override
    public String getBucketName() {
        if (this.masterObjectList == null) {
            return null;
        }
        return this.masterObjectList.getBucketName();
    }
    
    @Override
    public Job withMaxParallelRequests(final int maxParallelRequests) {
        this.maxParallelRequests = maxParallelRequests;
        return this;
    }

    protected void checkRunning() {
        if (running) throw new IllegalStateException("You cannot modify a job after calling transfer");
    }

    protected void transferItem(
            final Ds3Client client,
            final BulkObject ds3Object,
            final ChunkTransferrer.ItemTransferrer itemTransferrer)
            throws IOException
    {
        int objectTransfersAttempted = 0;

        while(true) {
            try {
                itemTransferrer.transferItem(client, ds3Object);
                break;
            } catch (final Throwable t) {
                if (ExceptionClassifier.isUnrecoverableException(t) || ++objectTransfersAttempted >= objectTransferAttempts) {
                    throw t;
                }
            }
        }
    }

    protected abstract List<Objects> getChunks(final MasterObjectList masterObjectList);
    protected abstract JobPartTrackerDecorator makeJobPartTracker(final List<Objects> chunks, final EventRunner eventRunner);

    protected JobPartTrackerDecorator getJobPartTracker() {
        return jobPartTracker;
    }

    protected static class JobPartTrackerDecorator implements JobPartTracker {
        private final JobPartTracker clientJobPartTracker;
        private final JobPartTracker internalJobPartTracker;

        protected JobPartTrackerDecorator(final List<Objects> chunks, final EventRunner eventRunner) {
            clientJobPartTracker = JobPartTrackerFactory.buildPartTracker(Iterables.concat(getAllBlobApiBeans(chunks)), eventRunner);
            internalJobPartTracker = JobPartTrackerFactory.buildPartTracker(Iterables.concat(getAllBlobApiBeans(chunks)), eventRunner);
        }

        @Override
        public void completePart(final String key, final ObjectPart objectPart) {
            // It's important to fire the internal completions -- those we set up to close channels we
            // have opened -- before firing client-registered events.  The reason is that some clients
            // rely upon this ordering to know that channels are closed when their event handlers run.
            internalJobPartTracker.completePart(key, objectPart);
            clientJobPartTracker.completePart(key, objectPart);
        }

        @Override
        public boolean containsPart(final String key, final ObjectPart objectPart) {
            return internalJobPartTracker.containsPart(key, objectPart) || clientJobPartTracker.containsPart(key, objectPart);
        }

        @Override
        public JobPartTracker attachDataTransferredListener(final DataTransferredListener listener) {
            return clientJobPartTracker.attachDataTransferredListener(listener);
        }

        @Override
        public JobPartTracker attachObjectCompletedListener(final ObjectCompletedListener listener) {
            internalJobPartTracker.attachObjectCompletedListener(listener);
            return this;
        }

        @Override
        public void removeDataTransferredListener(final DataTransferredListener listener) {
            clientJobPartTracker.removeDataTransferredListener(listener);
        }

        @Override
        public void removeObjectCompletedListener(final ObjectCompletedListener listener) {
            internalJobPartTracker.removeObjectCompletedListener(listener);
        }

        protected void attachClientObjectCompletedListener(final ObjectCompletedListener listener) {
            clientJobPartTracker.attachObjectCompletedListener(listener);
        }

        protected void removeClientObjectCompletedListener(final ObjectCompletedListener listener) {
            clientJobPartTracker.removeObjectCompletedListener(listener);
        }
    }
}
