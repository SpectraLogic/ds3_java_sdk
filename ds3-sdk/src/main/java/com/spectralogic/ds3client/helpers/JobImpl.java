/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.Job;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcher;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.TransferStrategyBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

abstract class JobImpl implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(JobImpl.class);

    protected boolean running = false;

    private final TransferStrategyBuilder transferStrategyBuilder;

    public JobImpl() {
        this(null);
    }

    public JobImpl(final TransferStrategyBuilder transferStrategyBuilder) {
        this.transferStrategyBuilder = transferStrategyBuilder;
    }

    @Override
    public UUID getJobId() {
        try {
            return transferStrategyBuilder.masterObjectList().getJobId();
        } catch (final Throwable t) {
            LOG.warn("Could not get job id.", t);
            return null;
        }
    }

    @Override
    public String getBucketName() {
        try {
            return transferStrategyBuilder.masterObjectList().getBucketName();
        } catch (final Throwable t) {
            LOG.warn("Could not get bucket name.", t);
            return null;
        }
    }

    @Override
    public Job withMaxParallelRequests(final int maxParallelRequests) {
        transferStrategyBuilder.withNumConcurrentTransferThreads(maxParallelRequests);
        return this;
    }

    protected void checkRunning() {
        if (running) throw new IllegalStateException("You cannot modify a job after calling transfer");
    }

    @Override
    public void attachChecksumListener(final ChecksumListener listener) {
        checkRunning();
        eventDispatcher().attachChecksumListener(listener);
    }

    @Override
    public void removeChecksumListener(final ChecksumListener listener) {
        checkRunning();
        eventDispatcher().removeChecksumListener(listener);
    }

    @Override
    public void attachWaitingForChunksListener(final WaitingForChunksListener listener) {
        checkRunning();
        eventDispatcher().attachWaitingForChunksListener(listener);
    }

    @Override
    public void removeWaitingForChunksListener(final WaitingForChunksListener listener) {
        checkRunning();
        eventDispatcher().removeWaitingForChunksListener(listener);
    }

    @Override
    public void attachFailureEventListener(final FailureEventListener listener) {
        checkRunning();
        eventDispatcher().attachFailureEventListener(listener);
    }

    @Override
    public void removeFailureEventListener(final FailureEventListener listener) {
        checkRunning();
        eventDispatcher().removeFailureEventListener(listener);
    }

    @Override
    public void attachDataTransferredListener(final DataTransferredListener listener) {
        checkRunning();
        eventDispatcher().attachDataTransferredListener(listener);
    }

    @Override
    public void removeDataTransferredListener(final DataTransferredListener listener) {
        checkRunning();
        eventDispatcher().removeDataTransferredListener(listener);
    }

    @Override
    public void attachObjectCompletedListener(final ObjectCompletedListener listener) {
        checkRunning();
        eventDispatcher().attachObjectCompletedListener(listener);
    }

    @Override
    public void removeObjectCompletedListener(final ObjectCompletedListener listener) {
        checkRunning();
        eventDispatcher().removeObjectCompletedListener(listener);
    }

    @Override
    public void transfer(final Ds3ClientHelpers.ObjectChannelBuilder channelBuilder) throws IOException {
        transferStrategyBuilder.withChannelBuilder(channelBuilder);
    }

    protected TransferStrategyBuilder transferStrategyBuilder() {
        return transferStrategyBuilder;
    }

    protected EventDispatcher eventDispatcher() {
        return transferStrategyBuilder.eventDispatcher();
    }
}
