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

package com.spectralogic.ds3client.helpers.channelbuilders;

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;

/**
 * Writes a file to an input stream to be used when performing a write job with
 * {@link Ds3ClientHelpers#startWriteJob(String, Iterable)} and performing the
 * transfer with {@link Ds3ClientHelpers.Job#transfer(Ds3ClientHelpers.ObjectChannelBuilder)}
 */
public abstract class ObjectInputStreamBuilder implements Ds3ClientHelpers.ObjectChannelBuilder {

    public abstract InputStream buildInputStream(final String key);

    @Override
    public SeekableByteChannel buildChannel(final String key) throws IOException {
        final InputStream inputStream = buildInputStream(key);
        final ReadableByteChannel channel = Channels.newChannel(inputStream);
        return new ReadOnlySeekableByteChannel(channel);
    }
}
