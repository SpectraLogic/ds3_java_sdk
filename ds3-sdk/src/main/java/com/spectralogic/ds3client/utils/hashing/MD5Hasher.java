package com.spectralogic.ds3client.utils.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hasher extends DigestHasher {

    @Override
    MessageDigest getDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
