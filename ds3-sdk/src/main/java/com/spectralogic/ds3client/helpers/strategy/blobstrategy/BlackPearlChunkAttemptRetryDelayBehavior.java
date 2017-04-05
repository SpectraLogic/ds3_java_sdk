package com.spectralogic.ds3client.helpers.strategy.blobstrategy;

import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.helpers.strategy.transferstrategy.EventDispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of {@link ChunkAttemptRetryDelayBehavior} that uses the delay a Black Pearl returns
 * in its payload when a chunk operation does not complete.
 */
public class BlackPearlChunkAttemptRetryDelayBehavior implements ChunkAttemptRetryDelayBehavior {
    private static final Logger LOG = LoggerFactory.getLogger(BlackPearlChunkAttemptRetryDelayBehavior.class);

    private final EventDispatcher eventDispatcher;

    /**
     * @param eventDispatcher An instance of {@link EventDispatcher} used to send {@link com.spectralogic.ds3client.helpers.WaitingForChunksListener}
     *                        events.
     */
    public BlackPearlChunkAttemptRetryDelayBehavior(final EventDispatcher eventDispatcher) {
        Preconditions.checkNotNull(eventDispatcher, "eventDispatcher may not be null.");

        this.eventDispatcher = eventDispatcher;
    }

    /**
     * Delay for the amount of time a Black Pearl specifies in the payload it returns when a chunk operation does
     * not complete.
     * @param delayIntervalInSeconds The delay in seconds, between chunk operation retry attempts, that comes from a Black Pearl payload returned
     *                               when a chunk operation does not complete.
     * @throws InterruptedException
     */
    @Override
    public void delay(final int delayIntervalInSeconds) throws InterruptedException {
        LOG.debug("Will retry allocate chunk call after {} seconds", delayIntervalInSeconds);

        eventDispatcher.emitWaitingForChunksEvents(delayIntervalInSeconds);

        Thread.sleep(delayIntervalInSeconds * 1000);
    }
}
