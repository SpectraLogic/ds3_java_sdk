/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers.channels;

import com.spectralogic.ds3client.helpers.TruncateNotAllowedException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public class WindowedSeekableByteChannel implements SeekableByteChannel {
    private final SeekableByteChannel channel;
    private final Object lock;
    private final long offset;
    private final long length;
    private long position = 0L;
    private boolean isOpen = true;
    private final Copy readCopy = new ReadCopy();
    private final Copy writeCopy = new WriteCopy();

    public WindowedSeekableByteChannel(
            final SeekableByteChannel channel,
            final Object lock,
            final long offset,
            final long length) {
        this.channel = channel;
        this.lock = lock;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public boolean isOpen() {
        synchronized (this.lock) {
            return this.isOpen;
        }
    }

    @Override
    public void close() throws IOException {
        synchronized (this.lock) {
            this.isOpen = false;
        }
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        return copy(dst, readCopy);
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        return copy(src, writeCopy);
    }

    private int copy(final ByteBuffer buffer, final Copy copy) throws IOException {
        synchronized (this.lock) {
            checkClosed();
            if (this.position >= this.length) {
                return -1;
            }

            final int oldLimit = buffer.limit();
            final long distanceFromEnd = this.length - this.position;
            if (buffer.remaining() > distanceFromEnd) {
                buffer.limit(oldLimit + (int)distanceFromEnd - buffer.remaining());
            }

            this.channel.position(this.offset + this.position);
            final int bytesCopied = copy.copy(buffer);
            this.position += bytesCopied;

            buffer.limit(oldLimit);

            return bytesCopied;
        }
    }

    @Override
    public long position() throws IOException {
        synchronized (this.lock) {
            checkClosed();
            return this.position;
        }
    }

    @Override
    public SeekableByteChannel position(final long newPosition) throws IOException {
        synchronized (this.lock) {
            checkClosed();
            this.position = newPosition;
            return this;
        }
    }

    @Override
    public long size() throws IOException {
        synchronized (this.lock) {
            checkClosed();
            return this.length;
        }
    }

    @Override
    public SeekableByteChannel truncate(final long size) throws IOException {
        throw new TruncateNotAllowedException();
    }

    private void checkClosed() {
        if (!this.isOpen) {
            throw new IllegalStateException("Object already closed");
        }
    }

    private interface Copy {
        int copy(final ByteBuffer buffer) throws IOException;
    }
    
    private final class ReadCopy implements Copy {
        @Override
        public int copy(final ByteBuffer buffer) throws IOException {
            return WindowedSeekableByteChannel.this.channel.read(buffer);
        }
    }
    
    private final class WriteCopy implements Copy {
        @Override
        public int copy(final ByteBuffer buffer) throws IOException {
            return WindowedSeekableByteChannel.this.channel.write(buffer);
        }
    }
}
