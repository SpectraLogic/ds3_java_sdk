package com.spectralogic.ds3client.integration;

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers;
import com.spectralogic.ds3client.utils.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.StandardOpenOption;

public class ResourceObjectPutter implements Ds3ClientHelpers.ObjectTransferrer {

    private final String basePath;

    public ResourceObjectPutter(final String basePath) {
        this.basePath = basePath;
    }

    @Override
    public SeekableByteChannel buildChannel(final String key) throws IOException {
        try {
            return FileChannel.open(ResourceUtils.loadFileResource(basePath + key).toPath(), StandardOpenOption.READ);
        } catch (final URISyntaxException e) {
            e.printStackTrace();
            throw new FileNotFoundException(key);
        }
    }
}
