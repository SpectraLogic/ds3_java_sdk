package com.spectralogic.ds3client.helpers;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Paths;

public class PrefixedFileObjectPutter implements Ds3ClientHelpers.ObjectChannelBuilder {

    final private LoggingFileObjectPutter objectPutter;
    final private String remotePrefix;

    public PrefixedFileObjectPutter(final String inputDirectory, final String remotePrefix) {
        this.objectPutter = new LoggingFileObjectPutter(Paths.get(inputDirectory));
        this.remotePrefix = remotePrefix;
    }

    @Override
    public SeekableByteChannel buildChannel(final String s) throws IOException {

        return objectPutter.buildChannel(Ds3ClientHelpers.stripLeadingPath(s, remotePrefix));
    }
}
