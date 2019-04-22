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

package com.spectralogic.ds3client.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public class LoggingSeekableByteChannel implements SeekableByteChannel {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingSeekableByteChannel.class);

    private final SeekableByteChannel wrappedChannel;
    private final String channelName;

    public LoggingSeekableByteChannel(final SeekableByteChannel wrappedChannel, final String channelName) {
        this.wrappedChannel = wrappedChannel;
        this.channelName = channelName;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        final int readCount = wrappedChannel.read(dst);
        LOG.info("Read {} bytes from {}", readCount, channelName);
        return readCount;
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        final int writeCount = wrappedChannel.write(src);
        LOG.info("Wrote {} bytes to {}", writeCount, channelName);
        return writeCount;
    }

    @Override
    public long position() throws IOException {
        return wrappedChannel.position();
    }

    @Override
    public SeekableByteChannel position(final long newPosition) throws IOException {
        return wrappedChannel.position(newPosition);
    }

    @Override
    public long size() throws IOException {
        return wrappedChannel.size();
    }

    @Override
    public SeekableByteChannel truncate(final long size) throws IOException {
        return wrappedChannel.truncate(size);
    }

    @Override
    public boolean isOpen() {
        return wrappedChannel.isOpen();
    }

    @Override
    public void close() throws IOException {
        wrappedChannel.close();
    }
}
