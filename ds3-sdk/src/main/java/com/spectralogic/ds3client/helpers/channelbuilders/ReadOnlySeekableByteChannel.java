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

package com.spectralogic.ds3client.helpers.channelbuilders;

import com.spectralogic.ds3client.utils.NotImplementedException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;

public class ReadOnlySeekableByteChannel implements SeekableByteChannel {

    private final ReadableByteChannel channel;
    private int size = 0;
    private int position = 0;

    public ReadOnlySeekableByteChannel(final ReadableByteChannel channel) {
        this.channel = channel;
    }

    public ReadableByteChannel getChannel() {
        return channel;
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        size = channel.read(dst);
        position += size;
        return size;
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public long position() throws IOException {
        return position;
    }

    @Override
    public SeekableByteChannel position(long newPosition) throws IOException {
        if (newPosition == this.position) {
            return this;
        }
        throw new NotImplementedException();
    }

    @Override
    public long size() throws IOException {
        return size;
    }

    @Override
    public SeekableByteChannel truncate(long size) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
}
