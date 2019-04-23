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

package com.spectralogic.ds3client.helpers.channels;

import com.spectralogic.ds3client.utils.ByteArraySeekableByteChannel;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class WindowedChannelFactory_Test {
    @Test
    public void getReturnsWindow() throws Exception {
        try (final ByteArraySeekableByteChannel channel = new ByteArraySeekableByteChannel()) {
            final Writer writer = Channels.newWriter(channel, "UTF-8");
            writer.write("0123456789");
            writer.close();
            
            try (final WindowedChannelFactory windowedChannelFactory = new WindowedChannelFactory(channel)) {
                try (final SeekableByteChannel window = windowedChannelFactory.get(2L, 6L)) {
                    assertThat(IOUtils.toString(Channels.newReader(window, "UTF-8")), is("234567"));
                }
            }
        }
    }

    @Test
    public void closeClosesUnderlyingChannel() throws Exception {
        final SeekableByteChannel channel = mock(SeekableByteChannel.class);
        new WindowedChannelFactory(channel).close();
        verify(channel).close();
        verifyNoMoreInteractions(channel);
    }
}
