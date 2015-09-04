package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

public class PrefixedObjectPutter implements Ds3ClientHelpers.ObjectChannelBuilder {

    final private Ds3ClientHelpers.ObjectChannelBuilder channelBuilder;
    final private String prefix;

    public PrefixedObjectPutter(final Ds3ClientHelpers.ObjectChannelBuilder channelBuilder, final String prefix) {
        this.channelBuilder = channelBuilder;
        this.prefix = prefix;
    }

    @Override
    public SeekableByteChannel buildChannel(final String key) throws IOException {

        return channelBuilder.buildChannel(key.substring(prefix.length()));
    }
}
