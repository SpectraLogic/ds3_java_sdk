/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class ByteArraySeekableByteChannel_Test {
    private static final byte[] WRITTEN_DATA = {
        0x49, 0x20, 0x67, 0x61, 0x76, 0x65, 0x20, 0x61, 0x20, 0x63,
        0x72, 0x79, 0x20, 0x6f, 0x66, 0x20, 0x61, 0x73, 0x74, 0x6f,
        0x6e, 0x69, 0x73, 0x68, 0x6d
    };

    @Test
    public void closeChangesIsOpen() throws IOException {
        final SeekableByteChannel channel = new ByteArraySeekableByteChannel();
        assertThat(channel.isOpen(), is(true));
        channel.close();
        assertThat(channel.isOpen(), is(false));
    }
    
    @Test
    public void writeAndGetByteArray() throws IOException {
        try (final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(5)) {
            writeToChannel(channel);
            assertArrayEquals(WRITTEN_DATA, channel.toByteArray());
            assertThat(channel.size(), is((long)WRITTEN_DATA.length));
        }
    }
    
    @Test
    public void toStringReturnsData() throws IOException {
        try (final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(5)) {
            writeToChannel(channel);
            assertThat(channel.toString(), is("I gave a cry of astonishm"));
        }
    }
    
    @Test
    public void writeSeekRead() throws IOException {
        try (final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(10)) {
            writeToChannel(channel);
            assertThat(channel.position(5L), sameInstance((SeekableByteChannel)channel));
            assertThat(channel.position(), is(5L));
            final byte[] readBytes1 = new byte[10];
            final byte[] readBytes2 = new byte[15];
            assertThat(channel.read(ByteBuffer.wrap(readBytes1)), is(10));
            assertThat(channel.read(ByteBuffer.wrap(readBytes2)), is(10));
            assertArrayEquals(Arrays.copyOfRange(WRITTEN_DATA, 5, 15), readBytes1);
            assertArrayEquals(Arrays.copyOfRange(WRITTEN_DATA, 15, 25), Arrays.copyOfRange(readBytes2, 0, 10));
        }
    }
    
    @Test
    public void truncate() throws IOException {
        try (final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel(15)) {
            writeToChannel(channel);
            assertThat(channel.truncate(30L), is((SeekableByteChannel)channel));
            assertThat(channel.size(), is(25L));
            assertThat(channel.position(), is(25L));
            assertThat(channel.truncate(20L), is((SeekableByteChannel)channel));
            assertThat(channel.size(), is(20L));
            assertThat(channel.position(), is(20L));
            channel.position(5L);
            assertThat(channel.truncate(10L), is((SeekableByteChannel)channel));
            assertThat(channel.size(), is(10L));
            assertThat(channel.position(), is(5L));
        }
    }

    private static void writeToChannel(final WritableByteChannel channel) throws IOException {
        assertThat(channel.write(ByteBuffer.wrap(new byte[] { 0x49, 0x20, 0x67, 0x61, 0x76, 0x65, 0x20, 0x61, 0x20, 0x63 })), is(10));
        assertThat(channel.write(ByteBuffer.wrap(new byte[] { 0x72, 0x79, 0x20, 0x6f, 0x66, 0x20, 0x61, 0x73, 0x74, 0x6f })), is(10));
        final ByteBuffer lastBuffer = ByteBuffer.wrap(new byte[] { 0x6e, 0x69, 0x73, 0x68, 0x6d, 0x65, 0x6e, 0x74, 0x2e, 0x20 });
        lastBuffer.limit(5);
        assertThat(channel.write(lastBuffer), is(5));
    }
}
