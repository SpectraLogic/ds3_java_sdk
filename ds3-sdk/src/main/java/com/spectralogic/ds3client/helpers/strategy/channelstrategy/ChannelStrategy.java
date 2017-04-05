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

import com.spectralogic.ds3client.models.BulkObject;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

/**
 * An interface whose concrete subclasses specify the behavior used to read or write a channel.
 */
public interface ChannelStrategy {
    /**
     * For a blob to be transferred, create a channel that will be the source for or destination
     * of that transferred blob.
     * @param blob The blob to be transferred.
     * @return A {@link SeekableByteChannel} that will be the source for or destination
     * of that transferred blob.
     * @throws IOException
     */
    SeekableByteChannel acquireChannelForBlob(final BulkObject blob) throws IOException;

    /**
     * When a blob has been transferred, release the source or destination channel associated with that
     * blob.
     * @param seekableByteChannel A {@link SeekableByteChannel} that had been allocated as the source for or destination
     *                            of a blob transfer.
     * @param blob The blob {@code seekableByteChannel} was allocated to source or sink {@code blob}'s data.
     * @throws IOException
     */
    void releaseChannelForBlob(final SeekableByteChannel seekableByteChannel, final BulkObject blob) throws IOException;
}
