package com.spectralogic.ds3client.helpers.channelbuilders;

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

public class PrefixAdderObjectChannelBuilder implements Ds3ClientHelpers.ObjectChannelBuilder {
    final private Ds3ClientHelpers.ObjectChannelBuilder channelBuilder;
    final private String prefix;

    public PrefixAdderObjectChannelBuilder(final Ds3ClientHelpers.ObjectChannelBuilder channelBuilder, final String prefix) {
        this.channelBuilder = channelBuilder;
        this.prefix = prefix;
    }

    @Override
    public SeekableByteChannel buildChannel(final String s) throws IOException {
        return channelBuilder.buildChannel(Ds3ClientHelpers.stripLeadingPath(s, prefix));
    }
}
