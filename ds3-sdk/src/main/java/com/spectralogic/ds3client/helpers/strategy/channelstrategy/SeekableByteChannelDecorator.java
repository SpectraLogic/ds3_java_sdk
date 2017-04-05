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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/**
 * An instance of {@link SeekableByteChannel} used to decorate another SeekableByteChannel in the
 * situation where we re-use the same channel for more than 1 blob.  This subclass prevents closing
 * a channel when there are other blobs still referencing the shared channel.
 */
class SeekableByteChannelDecorator implements SeekableByteChannel {
    private final SeekableByteChannel seekableByteChannel;

    SeekableByteChannelDecorator(final SeekableByteChannel seekableByteChannel) {
        Preconditions.checkNotNull(seekableByteChannel, "seekableByteChannel may not be null");
        this.seekableByteChannel = seekableByteChannel;
    }

    protected SeekableByteChannel wrappedSeekableByteChannel() {
        return seekableByteChannel;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        return seekableByteChannel.read(dst);
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        return seekableByteChannel.write(src);
    }

    @Override
    public long position() throws IOException {
        return seekableByteChannel.position();
    }

    @Override
    public SeekableByteChannel position(final long newPosition) throws IOException {
        return seekableByteChannel.position(newPosition);
    }

    @Override
    public long size() throws IOException {
        return seekableByteChannel.size();
    }

    @Override
    public SeekableByteChannel truncate(final long size) throws IOException {
        return seekableByteChannel.truncate(size);
    }

    @Override
    public boolean isOpen() {
        return seekableByteChannel.isOpen();
    }

    @Override
    public void close() throws IOException {
        // Intentionally not implemented
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof SeekableByteChannelDecorator)) return false;

        final SeekableByteChannelDecorator that = (SeekableByteChannelDecorator) o;

        return seekableByteChannel.equals(that.seekableByteChannel);
    }

    @Override
    public int hashCode() {
        return seekableByteChannel.hashCode();
    }
}
