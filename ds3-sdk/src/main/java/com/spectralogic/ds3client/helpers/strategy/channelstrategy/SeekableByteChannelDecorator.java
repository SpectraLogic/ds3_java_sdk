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
 *
 * This class positions the content of a blob within the bounds of a channel that may be capable
 * of containing more than one blob.
 */
class SeekableByteChannelDecorator implements SeekableByteChannel {
    private final Object lock = new Object();

    private final SeekableByteChannel seekableByteChannel;
    private final long blobOffset;
    private final long blobLength;
    private long position = 0;

    SeekableByteChannelDecorator(final SeekableByteChannel seekableByteChannel, final long blobOffset, final long blobLength) throws IOException {
        Preconditions.checkNotNull(seekableByteChannel, "seekableByteChannel may not be null.");
        Preconditions.checkArgument(blobOffset >= 0, "blobOffset must be >= 0.");
        Preconditions.checkArgument(blobLength >= 0, "blobLength must be >= 0.");
        this.seekableByteChannel = seekableByteChannel;
        this.blobOffset = blobOffset;
        this.blobLength = blobLength;

        seekableByteChannel.position(blobOffset);
    }

    SeekableByteChannel wrappedSeekableByteChannel() {
        return seekableByteChannel;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        synchronized (lock) {
            final long remainingInChannel = blobLength - position;
            final long numBytesWeCanRead = Math.min(dst.remaining(), remainingInChannel);

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

            return numBytesRead;
        }
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        synchronized (lock) {
            final long remainingInChannel = blobLength - position;
            final long numBytesWeCanWrite = Math.min(src.remaining(), remainingInChannel);

            if (numBytesWeCanWrite <= 0) {
                return 0;
            }

            final int numBytesWritten;

            if (numBytesWeCanWrite != src.remaining()) {
                final ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[(int) numBytesWeCanWrite]);
                byteBuffer.put(src);
                byteBuffer.flip();
                numBytesWritten = seekableByteChannel.write(byteBuffer);
            } else {
                numBytesWritten = seekableByteChannel.write(src);
            }

            position += numBytesWritten;

            return numBytesWritten;
        }
    }

    @Override
    public long position() throws IOException {
        synchronized (lock) {
            return seekableByteChannel.position();
        }
    }

    @Override
    public SeekableByteChannel position(final long newPosition) throws IOException {
        synchronized (lock) {
            final long lastPossiblePosition = blobLength - 1;
            position = Math.min(newPosition, lastPossiblePosition);
            seekableByteChannel.position(blobOffset + position);

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
