package com.spectralogic.ds3client.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

public class Md5Hash {
    private final byte[] hash;

    public static Md5Hash fromHexString(final String hex) throws InvalidMd5Exception {
        try {
            return new Md5Hash(Hex.decodeHex(hex.toCharArray()));
        } catch (final DecoderException e) {
            throw new InvalidMd5Exception(e);
        }
    }

    public static Md5Hash fromBase64String(final String base64) {
        return new Md5Hash(Base64.decodeBase64(base64));
    }

    public static Md5Hash fromByteArray(final byte[] bytes) {
        return new Md5Hash(bytes);
    }

    private Md5Hash(final byte[] hash) {
        this.hash = hash;
    }

    public byte[] getHash() {
        return hash;
    }

    public String toHexString() {
        return Hex.encodeHexString(hash);
    }

    public String toBase64String() {
        return Base64.encodeBase64String(hash);
    }

    public String toString() {
        return toBase64String();
    }


    public boolean equals(final Object obj) {
        if (!(obj instanceof Md5Hash)) {
            return false;
        }
        final Md5Hash otherHash = (Md5Hash) obj;
        return Arrays.equals(this.hash, otherHash.getHash());
    }
}
