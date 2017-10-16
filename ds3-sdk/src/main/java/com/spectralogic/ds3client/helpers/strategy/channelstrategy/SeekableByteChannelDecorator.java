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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An instance of {@link SeekableByteChannel} used to decorate another SeekableByteChannel in the
 * situation where we re-use the same channel for more than 1 blob.  This subclass prevents closing
 * a channel when there are other blobs still referencing the shared channel.
 */
class SeekableByteChannelDecorator implements SeekableByteChannel {
    private static final Logger LOG = LoggerFactory.getLogger(SeekableByteChannelDecorator.class);

    private final Object lock = new Object();

    private final SeekableByteChannel seekableByteChannel;
    private final long initialOffset;
    private final long length;
    private long position;

    SeekableByteChannelDecorator(final SeekableByteChannel seekableByteChannel, final long initialOffset, final long length) {
        Preconditions.checkNotNull(seekableByteChannel, "seekableByteChannel may not be null.");
        Preconditions.checkArgument(initialOffset >= 0, "initialOffset must be >= 0.");
        Preconditions.checkArgument(length >= 0, "length must be >= 0.");
        this.seekableByteChannel = seekableByteChannel;
        this.initialOffset = initialOffset;
        this.position = initialOffset;
        this.length = length;

        LOG.debug("==> initialOffset: {}, position: {}, length: {}", initialOffset, position, length);
    }

    SeekableByteChannel wrappedSeekableByteChannel() {
        return seekableByteChannel;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        synchronized (lock) {
            final long remainingInChannel = length - position;
            LOG.debug("==> remainingInChannel: {},", remainingInChannel);
            final long numBytesWeCanRead = Math.min(dst.remaining(), remainingInChannel);
            LOG.debug("==> numBytesWeCanRead: {},", numBytesWeCanRead);

            if (numBytesWeCanRead <= 0) {
                return 0;
            }

            final int numBytesRead;

            if (numBytesWeCanRead != dst.remaining()) {
                final ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[(int) numBytesWeCanRead]);
                numBytesRead = seekableByteChannel.read(byteBuffer);
                byteBuffer.flip();
                dst.put(byteBuffer);
            } else {
                numBytesRead = seekableByteChannel.read(dst);
            }

            position += numBytesRead;

            LOG.debug("==> numBytesRead: {}, position: {}", numBytesRead, position);

            return numBytesRead;
        }
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        synchronized (lock) {
            return seekableByteChannel.write(src);
        }
    }

    @Override
    public long position() throws IOException {
        synchronized (lock) {
            LOG.debug("get position: {}, seekableByteChannel.position: {}", position, seekableByteChannel.position());
            return seekableByteChannel.position();
        }
    }

    @Override
    public SeekableByteChannel position(final long newPosition) throws IOException {
        synchronized (lock) {
            final long lastPossiblePosition = length - 1;
            position = Math.min(newPosition, lastPossiblePosition);
            seekableByteChannel.position(initialOffset + position);

            LOG.debug("==> set position: {}, seekableByteChannel.position: {}", position, seekableByteChannel.position());

            return this;
        }
    }

    @Override
    public long size() throws IOException {
        synchronized (lock) {
            return seekableByteChannel.size();
        }
    }

    @Override
    public SeekableByteChannel truncate(final long size) throws IOException {
        synchronized (lock) {
            return seekableByteChannel.truncate(size);
        }
    }

    @Override
    public boolean isOpen() {
        synchronized (lock) {
            return seekableByteChannel.isOpen();
        }
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
