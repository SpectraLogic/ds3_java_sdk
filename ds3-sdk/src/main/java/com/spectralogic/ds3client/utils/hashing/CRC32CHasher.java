package com.spectralogic.ds3client.utils.hashing;

import java.util.zip.Checksum;

public class CRC32CHasher extends ChecksumHasher{
    @Override
    protected Checksum getChecksum() {
        return new Crc32c();
    }
}
