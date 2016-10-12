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
import java.util.Set;
import java.util.UUID;

abstract class JobImpl implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(JobImpl.class);

    protected final Ds3Client client;
    protected final MasterObjectList masterObjectList;
    protected boolean running = false;
    protected int maxParallelRequests = 10;
    private final int objectTransferAttempts;
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
        return new FailureEvent.Builder()
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
}
