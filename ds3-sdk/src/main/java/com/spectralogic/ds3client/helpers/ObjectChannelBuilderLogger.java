package com.spectralogic.ds3client.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

public class ObjectChannelBuilderLogger implements Ds3ClientHelpers.ObjectChannelBuilder {
    final private static Logger LOG = LoggerFactory.getLogger(ObjectChannelBuilderLogger.class);
    final private Ds3ClientHelpers.ObjectChannelBuilder channelBuilder;

    public ObjectChannelBuilderLogger(final Ds3ClientHelpers.ObjectChannelBuilder channelBuilder) {
        this.channelBuilder = channelBuilder;
    }

    @Override
    public SeekableByteChannel buildChannel(final String s) throws IOException {
        LOG.info("Opening channel for: {}", s);
        return this.channelBuilder.buildChannel(s);
    }
}
