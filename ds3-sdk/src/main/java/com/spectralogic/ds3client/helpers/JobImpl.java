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

import com.google.common.collect.Sets;
import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.Job;
import com.spectralogic.ds3client.helpers.events.EventRunner;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.ChecksumType;
import com.spectralogic.ds3client.models.MasterObjectList;
import com.spectralogic.ds3client.models.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.spectralogic.ds3client.helpers.ReadJobImpl.getAllBlobApiBeans;

abstract class JobImpl implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(JobImpl.class);

    protected final Ds3Client client;
    protected final MasterObjectList masterObjectList;
    protected boolean running = false;
    protected int maxParallelRequests = 10;
    private final int objectTransferAttempts;
    private final JobPartTrackerDecorator jobPartTracker;
    private final EventRunner eventRunner;
    private final Set<FailureEventListener> failureEventListeners;
    private final Set<WaitingForChunksListener> waitingForChunksListeners;
    private final Set<ChecksumListener> checksumListeners;

    public JobImpl(final Ds3Client client,
                   final MasterObjectList masterObjectList,
                   final int objectTransferAttempts,
                   final EventRunner eventRunner) {
        this.client = client;
        this.masterObjectList = masterObjectList;
        this.objectTransferAttempts = objectTransferAttempts;
        this.eventRunner = eventRunner;
        this.failureEventListeners = Sets.newIdentityHashSet();
        this.waitingForChunksListeners = Sets.newIdentityHashSet();
        this.checksumListeners = Sets.newIdentityHashSet();
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

    protected EventRunner getEventRunner() {
        return eventRunner;
    }

    @Override
    public void attachChecksumListener(final ChecksumListener listener) {
        checkRunning();
        this.checksumListeners.add(listener);
    }

    @Override
    public void removeChecksumListener(final ChecksumListener listener) {
        checkRunning();
        this.checksumListeners.remove(listener);
    }

    @Override
    public void attachWaitingForChunksListener(final WaitingForChunksListener listener) {
        checkRunning();
        this.waitingForChunksListeners.add(listener);
    }

    @Override
    public void removeWaitingForChunksListener(final WaitingForChunksListener listener) {
        checkRunning();
        this.waitingForChunksListeners.remove(listener);
    }

    @Override
    public void attachFailureEventListener(final FailureEventListener listener) {
        checkRunning();
        this.failureEventListeners.add(listener);
    }

    @Override
    public void removeFailureEventListener(final FailureEventListener listener) {
        checkRunning();
        this.failureEventListeners.remove(listener);
    }

    @Override
    public void attachDataTransferredListener(final DataTransferredListener listener) {
        checkRunning();
        getJobPartTracker().attachDataTransferredListener(listener);
    }

    @Override
    public void removeDataTransferredListener(final DataTransferredListener listener) {
        checkRunning();
        getJobPartTracker().removeDataTransferredListener(listener);
    }

    @Override
    public void attachObjectCompletedListener(final ObjectCompletedListener listener) {
        checkRunning();
        getJobPartTracker().attachClientObjectCompletedListener(listener);
    }

    @Override
    public void removeObjectCompletedListener(final ObjectCompletedListener listener) {
        checkRunning();
        getJobPartTracker().removeClientObjectCompletedListener(listener);
    }

    protected void emitFailureEvent(final FailureEvent failureEvent) {
        for (final FailureEventListener failureEventListener : failureEventListeners) {
            eventRunner.emitEvent(new Runnable() {
                @Override
                public void run() {
                    failureEventListener.onFailure(failureEvent);
                }
            });
        }
    }

    protected FailureEvent makeFailureEvent(final FailureEvent.FailureActivity failureActivity,
                                            final Throwable causalException,
                                            final Objects chunk)
    {
        return FailureEvent.builder()
                .doingWhat(failureActivity)
                .withCausalException(causalException)
                .withObjectNamed(getLabelForChunk(chunk))
                .usingSystemWithEndpoint(client.getConnectionDetails().getEndpoint())
                .build();
    }

    protected String getLabelForChunk(final Objects chunk) {
        try {
            return chunk.getObjects().get(0).getName();
        } catch (final Throwable t) {
            LOG.error("Failed to get label for chunk.", t);
        }

        return "unnamed object";
    }

    protected void emitWaitingForChunksEvents(final int retryAfter) {
        for (final WaitingForChunksListener waitingForChunksListener : waitingForChunksListeners) {
            eventRunner.emitEvent(new Runnable() {
                @Override
                public void run() {
                    waitingForChunksListener.waiting(retryAfter);
                }
            });
        }
    }

    protected void emitChecksumEvents(final BulkObject bulkObject, final ChecksumType.Type type, final String checksum) {
        for (final ChecksumListener listener : checksumListeners) {
            getEventRunner().emitEvent(new Runnable() {
                @Override
                public void run() {
                    listener.value(bulkObject, type, checksum);
                }
            });
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
            clientJobPartTracker = JobPartTrackerFactory.buildPartTracker(getAllBlobApiBeans(chunks), eventRunner);
            internalJobPartTracker = JobPartTrackerFactory.buildPartTracker(getAllBlobApiBeans(chunks), eventRunner);
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
