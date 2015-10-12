package com.spectralogic.ds3client.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;

public class LoggingFileObjectPutter implements Ds3ClientHelpers.ObjectChannelBuilder {
    final private FileObjectPutter objectPutter;
    final private static Logger LOG = LoggerFactory.getLogger(LoggingFileObjectPutter.class);

    public LoggingFileObjectPutter(final Path inputDirectory) {
            this.objectPutter = new FileObjectPutter(inputDirectory);
    }

    @Override
    public SeekableByteChannel buildChannel(final String s) throws IOException {
        LOG.info("Putting " + s + " to ds3 endpoint");
        return this.objectPutter.buildChannel(s);
    }
}
