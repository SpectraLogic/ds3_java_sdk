package com.spectralogic.ds3client.utils.hashing;

import org.apache.commons.codec.binary.Base64;

import java.nio.ByteBuffer;
import java.util.zip.Checksum;

public abstract class ChecksumHasher implements Hasher {

    private final Checksum checksum;

    public ChecksumHasher() {
        this.checksum = getChecksum();
    }

    protected abstract Checksum getChecksum();

    @Override
    public void update(final byte[] bytes, final int offset, final int length) {
        checksum.update(bytes, offset, length);
    }

    @Override
    public String digest() {
        return Base64.encodeBase64String(longToBytes(checksum.getValue()));
    }

    private byte[] longToBytes(long x) {
        final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

}
