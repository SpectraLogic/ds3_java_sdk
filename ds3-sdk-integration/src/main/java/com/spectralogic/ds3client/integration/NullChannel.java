package com.spectralogic.ds3client.integration;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public class NullChannel implements SeekableByteChannel {

    private boolean open = true;

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        throw new IllegalStateException("you can not read from a null channel");
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        return src.limit();
    }

    @Override
    public long position() throws IOException {
        return 0;
    }

    @Override
    public SeekableByteChannel position(final long newPosition) throws IOException {
        return this;
    }

    @Override
    public long size() throws IOException {
        return 0;
    }

    @Override
    public SeekableByteChannel truncate(final long size) throws IOException {
        return this;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void close() throws IOException {
        open = false;
    }
}
