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

package com.spectralogic.ds3client.helpers.strategy.blobstrategy;

import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link ChunkAttemptRetryDelayBehavior} that uses the delay a client specifies,
 * as opposed to using the delay a Black Pearl returns in its payload when a chunk operation does not complete.
 */
public class ClientDefinedChunkAttemptRetryDelayBehavior implements ChunkAttemptRetryDelayBehavior {
    private static final Logger LOG = LoggerFactory.getLogger(ClientDefinedChunkAttemptRetryDelayBehavior.class);

    private final int clientDefinedDelayInSeconds;
    private final EventDispatcher eventDispatcher;

    /**
     * @param clientDefinedDelayInSeconds A user-specified delay in seconds between retry attempts when a chunk
     *                                    operation does not complete.
     * @param eventDispatcher
     */
    public ClientDefinedChunkAttemptRetryDelayBehavior(final int clientDefinedDelayInSeconds,
                                                       final EventDispatcher eventDispatcher)
    {
        Preconditions.checkState(clientDefinedDelayInSeconds >= 0, "clientDefinedDelayInSeconds must be >= 0");
        Preconditions.checkNotNull(eventDispatcher, "eventDispatcher may not be null");

        this.clientDefinedDelayInSeconds = clientDefinedDelayInSeconds;
        this.eventDispatcher = eventDispatcher;
    }

    /**
     * @param delayIntervalInSeconds The delay in seconds, between chunk operation retry attempts, that comes from a Black Pearl payload returned
     *                               when a chunk operation does not complete.  This {@link ChunkAttemptRetryDelayBehavior}
     *                               subclass will use the {@code clientDefinedDelayInSeconds} as the delay, and not the
     *                               delay the Black Pearl specifies.
     * @throws InterruptedException
     */
    @Override
    public void delay(final int delayIntervalInSeconds) throws InterruptedException {
        LOG.debug("Will retry allocate chunk call after {} seconds", clientDefinedDelayInSeconds);

        // Even though the BP is telling us how long to wait before trying to allocate a chunk again,
        // we'll use the delay time the client has specified.
        eventDispatcher.emitWaitingForChunksEvents(clientDefinedDelayInSeconds);

        Thread.sleep(clientDefinedDelayInSeconds * 1000);
    }
}
