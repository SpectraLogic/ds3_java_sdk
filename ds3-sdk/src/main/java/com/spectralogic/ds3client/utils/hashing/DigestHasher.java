package com.spectralogic.ds3client.utils.hashing;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

abstract class DigestHasher implements Hasher {

    private final MessageDigest digest;

    DigestHasher() {
        this.digest = getDigest();
    }

    abstract MessageDigest getDigest();

    @Override
    public void update(final byte[] bytes, int offset, int length) {
        digest.update(bytes, offset, length);
    }


    @Override
    public String digest() {
        return Base64.encodeBase64String(digest.digest());
    }
}
