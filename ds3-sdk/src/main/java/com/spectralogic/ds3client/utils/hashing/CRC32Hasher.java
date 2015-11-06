package com.spectralogic.ds3client.utils.hashing;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class CRC32Hasher extends ChecksumHasher {
    @Override
    protected Checksum getChecksum() {
        return new CRC32();
    }
}
