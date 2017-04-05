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

package com.spectralogic.ds3client.helpers.strategy.channelstrategy;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.channels.RangedSeekableByteChannel;
import com.spectralogic.ds3client.helpers.channels.WindowedSeekableByteChannel;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.common.Range;
import com.spectralogic.ds3client.utils.Guard;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * A subclass of {@link ChannelStrategy} that creates a channel per blob positioned at the offset
 * correct for the current blob offset.
 */
public class RandomAccessChannelStrategy implements ChannelStrategy {
    private final Object lock = new Object();

    private final Ds3ClientHelpers.ObjectChannelBuilder objectChannelBuilder;
    private final ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> rangesForBlobs;
    private final ChannelPreparable channelPreparer;
    private final Map<BulkObject, SeekableByteChannel> blobChannelMap;

    /**
     * @param objectChannelBuilder An instance of {@link com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder};
     *                             usually an instance of {@link com.spectralogic.ds3client.helpers.FileObjectPutter}
     *                             or {@link com.spectralogic.ds3client.helpers.FileObjectGetter}.
     * @param rangesForBlobs A map that associates a blob's name with the {@link Range}(s) transfered as part of a get.  This
     *                       parameter may be null.
     * @param channelPreparer An instance of {@link ChannelPreparable} used to prepare a channel prior to moving data, most likely
     *                        either {@link TruncatingChannelPreparable} or {@link NullChannelPreparable}.
     */
    public RandomAccessChannelStrategy(final Ds3ClientHelpers.ObjectChannelBuilder objectChannelBuilder,
                                       final ImmutableMap<String, ImmutableMultimap<BulkObject, Range>> rangesForBlobs,
                                       final ChannelPreparable channelPreparer)
    {
        Preconditions.checkNotNull(objectChannelBuilder, "objectChannelBuilder may not be null.");
        Preconditions.checkNotNull(channelPreparer, "channelPreparer may not be null.");

        this.objectChannelBuilder = objectChannelBuilder;
        this.rangesForBlobs = rangesForBlobs;
        this.channelPreparer = channelPreparer;
        blobChannelMap  = new HashMap<>();
    }

    /**
     * For a blob to be transferred, create a channel that will be the source for or destination
     * of that transferred blob.
     * @param blob The blob to be transferred.
     * @return A {@link SeekableByteChannel} that will be the source for or destination
     * of that transferred blob.
     * @throws IOException
     */
    @Override
    public SeekableByteChannel acquireChannelForBlob(final BulkObject blob) throws IOException {
        synchronized (lock) {
            channelPreparer.prepareChannel(blob.getName(), objectChannelBuilder);

            SeekableByteChannel seekableByteChannel = blobChannelMap.get(blob);

            if (seekableByteChannel == null) {
                seekableByteChannel = new RangedSeekableByteChannel(objectChannelBuilder.buildChannel(blob.getName()),
                        getRangesForABlob(blob),
                        blob.getName());
                blobChannelMap.put(blob, seekableByteChannel);
            }

            return new WindowedSeekableByteChannel(seekableByteChannel, lock, blob.getOffset(), blob.getLength());
        }
    }

    private ImmutableMultimap<BulkObject, Range> getRangesForABlob(final BulkObject blob) {
        if (Guard.isMapNullOrEmpty(rangesForBlobs)) {
            return ImmutableMultimap.of();
        }

        final ImmutableMultimap<BulkObject, Range> rangesForABlob = rangesForBlobs.get(blob.getName());

        if (Guard.isMultiMapNullOrEmpty(rangesForABlob)) {
            return ImmutableMultimap.of();
        }

        return rangesForABlob;
    }

    /**
     * When a blob has been transferred, release the source or destination channel associated with that
     * blob.
     * @param seekableByteChannel A {@link SeekableByteChannel} that had been allocated as the source for or destination
     *                            of a blob transfer.
     * @param blob The blob {@code seekableByteChannel} was allocated to source or sink {@code blob}'s data.
     * @throws IOException
     */
    @Override
    public void releaseChannelForBlob(final SeekableByteChannel seekableByteChannel, final BulkObject blob) throws IOException {
        synchronized (lock) {
            final SeekableByteChannel cachedChannel = blobChannelMap.remove(blob);

            Throwable closingCachedChannelException = null;

            try {
                cachedChannel.close();
            } catch (final Throwable t) {
                closingCachedChannelException = t;
            }

            try {
                seekableByteChannel.close();
            } catch (final Throwable t) {
                throw new IOException(t);
            }

            if (closingCachedChannelException != null) {
                throw new IOException(closingCachedChannelException);
            }
        }
    }
}
