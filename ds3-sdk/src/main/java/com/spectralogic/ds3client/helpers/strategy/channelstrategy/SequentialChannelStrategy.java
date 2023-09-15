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

package com.spectralogic.ds3client.helpers.strategy.channelstrategy;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.helpers.channelbuilders.ReadOnlySeekableByteChannel;
import com.spectralogic.ds3client.models.BulkObject;
import com.spectralogic.ds3client.models.MasterObjectList;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * A subclass of {@link ChannelStrategy} used to provide "streamed" access to a channel,
 * where streamed means that the channel will be accessed in sequentially increasing offsets.
 * This class acts as an aggregator that references 1 underlying channel for any number of blobs that
 * reference the channel.
 */
public class SequentialChannelStrategy implements ChannelStrategy {
    private final Object lock = new Object();

    private final SetMultimap<String, Long> blobNameOffsetMap = HashMultimap.create();
    private final Map<String, SeekableByteChannelDecorator> blobNameChannelMap = new HashMap<>();

    private final ChannelStrategy channelStrategyDelegate;
    private final Ds3ClientHelpers.ObjectChannelBuilder objectChannelBuilder;
    private final ChannelPreparable channelPreparer;
    private final MasterObjectList masterObjectList;

    /**
     * @param channelStrategy The instance of {@link ChannelStrategy} that holds the 1 channel reference a blob needs
     *                        to transfer data.
     * @param objectChannelBuilder An instance of {@link com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder};
     *                             usually an instance of {@link com.spectralogic.ds3client.helpers.FileObjectPutter}
     *                             or {@link com.spectralogic.ds3client.helpers.FileObjectGetter}.
     * @param channelPreparer An instance of {@link ChannelPreparable} used to prepare a channel prior to moving data, most likely
     *                        either {@link TruncatingChannelPreparable} or {@link NullChannelPreparable}.
     */
    public SequentialChannelStrategy(final ChannelStrategy channelStrategy,
            final Ds3ClientHelpers.ObjectChannelBuilder objectChannelBuilder,
            final ChannelPreparable channelPreparer,
            final MasterObjectList masterObjectList)
    {
        channelStrategyDelegate = channelStrategy;
        this.objectChannelBuilder = objectChannelBuilder;
        this.channelPreparer = channelPreparer;
        this.masterObjectList = masterObjectList;
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
            final String blobName = blob.getName();

            blobNameOffsetMap.put(blobName, blob.getOffset());

            final SeekableByteChannel seekableByteChannel = blobNameChannelMap.get(blobName);

            if (seekableByteChannel != null) {
                return seekableByteChannel;
            }

            return makeNewChannel(blob);
        }
    }

    private SeekableByteChannel makeNewChannel(final BulkObject blob) throws IOException {
        channelPreparer.prepareChannel(blob.getName(), objectChannelBuilder);

        final SeekableByteChannel seekableByteChannel = channelStrategyDelegate.acquireChannelForBlob(blob);
        final SeekableByteChannelDecorator seekableByteChannelDecorator = new SeekableByteChannelDecorator(seekableByteChannel, blob.getOffset(), blob.getLength());

        blobNameChannelMap.put(blob.getName(), seekableByteChannelDecorator);
        return seekableByteChannelDecorator;
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
            final String blobName = blob.getName();

            blobNameOffsetMap.remove(blobName, blob.getOffset());

            final Long maximumOffset = masterObjectList.getObjects().stream()
                    .flatMap(objects -> objects.getObjects().stream())
                    .filter(bulkObject -> bulkObject.getName().equals(blobName))
                    .map(bulkObject -> bulkObject.getOffset())
                    .max(Long::compareTo).orElseGet(() -> blob.getOffset());

            final boolean isReadOnly = ((SeekableByteChannelDecorator) seekableByteChannel).wrappedSeekableByteChannel() instanceof ReadOnlySeekableByteChannel;

            if (blobNameOffsetMap.get(blobName).size() == 0 && (blob.getOffset() == maximumOffset || !(isReadOnly))) {
                blobNameChannelMap.remove(blobName);
                channelStrategyDelegate.releaseChannelForBlob(((SeekableByteChannelDecorator)seekableByteChannel).wrappedSeekableByteChannel(), blob);
            }

        }
    }
}
