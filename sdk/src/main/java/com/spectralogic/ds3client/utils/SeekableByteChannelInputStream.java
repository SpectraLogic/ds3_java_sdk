/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public class SeekableByteChannelInputStream extends InputStream {
    private final SeekableByteChannel seekableByteChannel;
    private long markPosition = 0L;

    public SeekableByteChannelInputStream(final SeekableByteChannel seekableByteChannel) {
        this.seekableByteChannel = seekableByteChannel;
    }

    @Override
    public int read() throws IOException {
        final ByteBuffer buffer = ByteBuffer.wrap(new byte[1]);
        final int bytesRead = this.seekableByteChannel.read(buffer);
        if (bytesRead > 0) {
            buffer.position(0);
            return buffer.get();
        } else {
            return -1;
        }
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        final ByteBuffer buffer = ByteBuffer.wrap(b);
        buffer.position(off);
        buffer.limit(off + len);
        return this.seekableByteChannel.read(buffer);
    }
    
    @Override
    public long skip(final long n) throws IOException {
        this.seekableByteChannel.position(this.seekableByteChannel.position() + n);
        return this.seekableByteChannel.position();
    }
    
    @Override
    public boolean markSupported() {
        return true;
    }
    
    @Override
    public void mark(final int readlimit) {
        try {
            this.markPosition = this.seekableByteChannel.position();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void reset() throws IOException {
        this.seekableByteChannel.position(this.markPosition);
        this.markPosition = 0;
    }
    
    @Override
    public void close() throws IOException {
        this.seekableByteChannel.close();
    }
}
