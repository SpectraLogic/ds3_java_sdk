package com.spectralogic.ds3client.utils.hashing;

import org.apache.commons.codec.binary.Base64;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.Checksum;

abstract class ChecksumHasher implements Hasher {

    private final Checksum checksum;

    public ChecksumHasher() {
        this.checksum = getChecksum();
    }

    protected abstract Checksum getChecksum();

    @Override
    public void update(final byte[] bytes, final int offset, final int length) {
        checksum.update(bytes, offset, length);
    }

    /**
     * This must return a string where the checksum is an int in network byte ordering that is base64 encoded
     */
    @Override
    public String digest() {
        return Base64.encodeBase64String(toBytes(checksum.getValue()));
    }

    private byte[] toBytes(long x) {
        final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(x);

        final byte[] bytes = new byte[Integer.BYTES];
        System.arraycopy(buffer.array(), 4, bytes, 0, 4);

        return bytes;
    }

}
