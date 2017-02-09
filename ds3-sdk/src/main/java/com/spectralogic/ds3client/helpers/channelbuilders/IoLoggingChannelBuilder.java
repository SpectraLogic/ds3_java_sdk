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
import com.spectralogic.ds3client.utils.LoggingSeekableByteChannel;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;


/**
 * This ObjectChannelBuilder is used to wrap an existing ObjectChannelBuilder and wrap all the channels it creates
 * in a logging channel to log how many bytes were read or written on each call.  This should only be used
 * for debugging and should not be used in a production deployment.
 */
public class IoLoggingChannelBuilder implements Ds3ClientHelpers.ObjectChannelBuilder {

    public static final String IO_CHANNEL_DEFAULT_NAME = "IoChannel";

    private final Ds3ClientHelpers.ObjectChannelBuilder wrappedObjectChannelBuilder;
    private final String streamName;

    public IoLoggingChannelBuilder(final Ds3ClientHelpers.ObjectChannelBuilder wrappedObjectChannelBuilder, final String streamName) {
        this.wrappedObjectChannelBuilder = wrappedObjectChannelBuilder;
        this.streamName = streamName;
    }

    public IoLoggingChannelBuilder(final Ds3ClientHelpers.ObjectChannelBuilder wrappedObjectChannelBuilder) {
        this(wrappedObjectChannelBuilder, IO_CHANNEL_DEFAULT_NAME);
    }

    @Override
    public SeekableByteChannel buildChannel(final String key) throws IOException {

        final SeekableByteChannel seekableByteChannel = wrappedObjectChannelBuilder.buildChannel(key);

        return new LoggingSeekableByteChannel(seekableByteChannel, streamName);
    }
}
