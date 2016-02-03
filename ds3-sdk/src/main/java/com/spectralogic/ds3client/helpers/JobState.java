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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.spectralogic.ds3client.helpers.AutoCloseableCache.ValueBuilder;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.helpers.channels.RangedSeekableByteChannel;
import com.spectralogic.ds3client.helpers.channels.WindowedChannelFactory;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.JobChunkApiBean;
import com.spectralogic.ds3client.models.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

class JobState implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(JobState.class);

    private final AtomicInteger objectsRemaining;
    private final AutoCloseableCache<String, WindowedChannelFactory> channelCache;
    private final JobPartTracker partTracker;

    public JobState(
            final ObjectChannelBuilder channelBuilder,
            final Collection<JobChunkApiBean> filteredChunks,
            final JobPartTracker partTracker,
            final ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> objectRanges) {
        this.objectsRemaining = new AtomicInteger(getObjectCount(filteredChunks));
        this.channelCache = buildCache(channelBuilder, objectRanges);
        this.partTracker = partTracker.attachObjectCompletedListener(new ObjectCompletedListenerImpl());
    }
    
    public boolean hasObjects() {
        return this.objectsRemaining.get() > 0;
    }

    private static int getObjectCount(final Collection<JobChunkApiBean> chunks) {
        final HashSet<String> result = new HashSet<>();
        for (final JobChunkApiBean chunk : chunks) {
            for (final BulkObject bulkObject : chunk.getObjects()) {
                result.add(bulkObject.getName());
            }
        }
        return result.size();
    }

    private static AutoCloseableCache<String, WindowedChannelFactory> buildCache(
            final ObjectChannelBuilder channelBuilder,
            final ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> objectRanges) {
        return new AutoCloseableCache<>(
            new ValueBuilder<String, WindowedChannelFactory>() {
                @Override
                public WindowedChannelFactory get(final String key) {
                    try {
                        LOG.debug("Opening channel for : " + key);
                        return new WindowedChannelFactory(RangedSeekableByteChannel
                                .wrap(channelBuilder.buildChannel(key), objectRanges.get(key)));
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        );
    }

    @Override
    public void close() throws Exception {
        this.channelCache.close();
    }

    public JobPartTracker getPartTracker() {
        return partTracker;
    }

    private final class ObjectCompletedListenerImpl implements ObjectCompletedListener {
        @Override
        public void objectCompleted(final String name) {
            JobState.this.objectsRemaining.decrementAndGet();
            try {
                LOG.debug("Closing file: "  + name);
                JobState.this.channelCache.close(name);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public SeekableByteChannel getChannel(final String name, final long offset, final long length) {
        return this.channelCache.get(name).get(offset, length);
    }
}
