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

package com.spectralogic.ds3client.helpers.channelbuilders;

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Writes a file to an output stream to be used when performing a read job with
 * {@link Ds3ClientHelpers#startReadJob(String, Iterable)} and performing the
 * transfer with {@link Ds3ClientHelpers.Job#transfer(Ds3ClientHelpers.ObjectChannelBuilder)}
 */
public abstract class ObjectOutputStreamBuilder implements Ds3ClientHelpers.ObjectChannelBuilder {

    public abstract OutputStream buildOutputStream(final String key) throws IOException;

    @Override
    public SeekableByteChannel buildChannel(final String key) throws IOException {
        final OutputStream outputStream = buildOutputStream(key);
        final WritableByteChannel channel = Channels.newChannel(outputStream); 
        return new WriteOnlySeekableByteChannel(channel);
    }
}
