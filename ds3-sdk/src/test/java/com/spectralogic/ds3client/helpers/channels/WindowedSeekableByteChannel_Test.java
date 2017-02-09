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

package com.spectralogic.ds3client.helpers.channels;

import com.spectralogic.ds3client.helpers.TruncateNotAllowedException;
import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class WindowedSeekableByteChannel_Test {
    @Test(timeout = 1000)
    public void closeChangesIsOpen() throws IOException {
        try (final SeekableByteChannel channel = stringToChannel("aabbbcccc")) {
            final Object lock = new Object();
            final SeekableByteChannel window = new WindowedSeekableByteChannel(channel, lock, 0L, 2L);
            assertThat(window.isOpen(), is(true));
            window.close();
            assertThat(window.isOpen(), is(false));
        }
    }

    @Test(timeout = 1000)
    public void readChannelSections() throws IOException {
        try (final SeekableByteChannel channel = stringToChannel("aabbbcccc")) {
            final Object lock = new Object();
            final SeekableByteChannel channelOfAs = new WindowedSeekableByteChannel(channel, lock, 0L, 2L);
            final SeekableByteChannel channelOfBs = new WindowedSeekableByteChannel(channel, lock, 2L, 3L);
            final SeekableByteChannel channelOfCs = new WindowedSeekableByteChannel(channel, lock, 5L, 4L);
            
            assertThat(channelOfAs.size(), is(2L));
            assertThat(channelOfBs.size(), is(3L));
            assertThat(channelOfCs.size(), is(4L));
            
            assertThat(channelToString(channelOfAs), is("aa"));
            assertThat(channelToString(channelOfBs), is("bbb"));
            assertThat(channelToString(channelOfCs), is("cccc"));
        }
    }

    @Test(timeout = 1000)
    public void writeChannelSections() throws IOException {
        try (final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel()) {
            final Object lock = new Object();
            final SeekableByteChannel channelOfAs = new WindowedSeekableByteChannel(channel, lock, 0L, 2L);
            final SeekableByteChannel channelOfBs = new WindowedSeekableByteChannel(channel, lock, 2L, 3L);
            final SeekableByteChannel channelOfCs = new WindowedSeekableByteChannel(channel, lock, 5L, 4L);
            
            assertThat(channelOfAs.size(), is(2L));
            assertThat(channelOfBs.size(), is(3L));
            assertThat(channelOfCs.size(), is(4L));
            
            writeToChannel("cccc", channelOfCs);
            writeToChannel("bbb", channelOfBs);
            writeToChannel("aa", channelOfAs);
            
            channel.position(0);
            
            assertThat(channel.toString(), is("aabbbcccc"));
        }
    }

    @Test(timeout = 1000)
    public void writeDoesNotExceedWindow() throws IOException {
        try (final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel()) {
            final Object lock = new Object();
            try (final WindowedSeekableByteChannel window = new WindowedSeekableByteChannel(channel, lock, 2L, 7L)) {
                final ByteBuffer buffer = Charset.forName("UTF-8").encode("0123456789");
                buffer.position(1);
                assertThat(window.write(buffer), is(7));
                assertThat(window.position(), is(7L));
                assertThat(buffer.position(), is(8));
                assertThat(buffer.limit(), is(10));
                
                assertThat(window.write(buffer), is(-1));
                assertThat(window.position(), is(7L));

                assertThat(channel.size(), is(9L));
                channel.position(0);

                assertThat(channel.toString(), is("\0\0" + "1234567"));
            }
        }
    }

    @Test(timeout = 1000)
    public void readPositionTracking() throws IOException {
        try (final SeekableByteChannel channel = stringToChannel("aabbbcccc")) {
            final Object lock = new Object();
            try (final WindowedSeekableByteChannel window = new WindowedSeekableByteChannel(channel, lock, 2L, 7L)) {
                final byte[] bytes = new byte[10];
                final ByteBuffer buffer = ByteBuffer.wrap(bytes);

                buffer.limit(3);
                assertThat(window.read(buffer), is(3));
                assertThat(window.position(), is(3L));
                assertThat(buffer.position(), is(3));
                assertThat(buffer.limit(), is(3));
                assertThat(new String(bytes, 0, 3, Charset.forName("UTF-8")), is("bbb"));

                buffer.limit(10);
                assertThat(window.read(buffer), is(4));
                assertThat(window.position(), is(7L));
                assertThat(buffer.position(), is(7));
                assertThat(buffer.limit(), is(10));
                assertThat(new String(bytes, 3, 4, Charset.forName("UTF-8")), is("cccc"));
            }
        }
    }

    @Test(timeout = 1000)
    public void writePositionTracking() throws IOException {
        try (final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel()) {
            final Object lock = new Object();
            try (final WindowedSeekableByteChannel window = new WindowedSeekableByteChannel(channel, lock, 2L, 7L)) {
                ByteBuffer buffer = Charset.forName("UTF-8").encode("bbb");
                assertThat(window.write(buffer), is(3));
                assertThat(buffer.position(), is(3));

                buffer = Charset.forName("UTF-8").encode("cccc");
                assertThat(window.write(buffer), is(4));
                assertThat(buffer.position(), is(4));

                channel.position(0);
                
                assertThat(channel.toString(), is("\0\0bbbcccc"));
            }
        }
    }

    @Test(timeout = 1000)
    public void positionUpdate() throws IOException {
        try (final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel()) {
            final Object lock = new Object();
            try (final WindowedSeekableByteChannel window = new WindowedSeekableByteChannel(channel, lock, 2L, 7L)) {
                assertThat(window.position(), is(0L));
                assertThat(window.position(5L), is((SeekableByteChannel)window));
                assertThat(window.position(), is(5L));
                assertThat(window.position(), is(5L));
            }
        }
    }

    @Test(timeout = 1000)
    public void positionThenWriteReturnsEOF() throws IOException {
        try (final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel()) {
            final Object lock = new Object();
            try (final WindowedSeekableByteChannel window = new WindowedSeekableByteChannel(channel, lock, 2L, 7L)) {
                window.position(10L);
                assertThat(window.position(), is(10L));
                final ByteBuffer fakeBuffer = mock(ByteBuffer.class);
                assertThat(window.write(fakeBuffer), is(-1));
                verifyNoMoreInteractions(fakeBuffer);
            }
        }
    }

    @Test(timeout = 1000)
    public void positionThenReadReturnsEOF() throws IOException {
        try (final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel()) {
            final Object lock = new Object();
            try (final WindowedSeekableByteChannel window = new WindowedSeekableByteChannel(channel, lock, 2L, 7L)) {
                window.position(10L);
                assertThat(window.position(), is(10L));
                final ByteBuffer fakeBuffer = mock(ByteBuffer.class);
                assertThat(window.read(fakeBuffer), is(-1));
                verifyNoMoreInteractions(fakeBuffer);
            }
        }
    }

    @Test(timeout = 1000)
    public void truncateNotAllowed() throws IOException {
        try (final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel()) {
            final Object lock = new Object();
            try (final WindowedSeekableByteChannel window = new WindowedSeekableByteChannel(channel, lock, 2L, 7L)) {
                try {
                    window.truncate(5L);
                    fail();
                } catch (final TruncateNotAllowedException e) {
                }
                try {
                    window.truncate(10L);
                    fail();
                } catch (final TruncateNotAllowedException e) {
                }
            }
        }
    }

    @Test(timeout = 1000)
    public void interactAfterCloseNotAllowed() throws IOException {
        try (final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel()) {
            final Object lock = new Object();
            try (final WindowedSeekableByteChannel window = new WindowedSeekableByteChannel(channel, lock, 2L, 7L)) {
                window.close();
                try {
                    window.read(ByteBuffer.wrap(new byte[10]));
                    fail();
                } catch (final IllegalStateException e) {
                }
                try {
                    window.write(ByteBuffer.wrap(new byte[10]));
                    fail();
                } catch (final IllegalStateException e) {
                }
                try {
                    window.position();
                    fail();
                } catch (final IllegalStateException e) {
                }
                try {
                    window.position(1L);
                    fail();
                } catch (final IllegalStateException e) {
                }
                try {
                    window.size();
                    fail();
                } catch (final IllegalStateException e) {
                }
            }
        }
    }

    private static void writeToChannel(final String string, final SeekableByteChannel channel) throws IOException {
        final Writer writer = Channels.newWriter(channel, "UTF-8");
        writer.write(string);
        writer.flush();
    }

    private static SeekableByteChannel stringToChannel(final String string) throws IOException {
        final SeekableByteChannel channel = new ByteArraySeekableByteChannel();
        writeToChannel(string, channel);
        channel.position(0);
        return channel;
    }

    private static String channelToString(final SeekableByteChannel channel) throws IOException {
        return IOUtils.toString(Channels.newReader(channel, "UTF-8"));
    }
}
