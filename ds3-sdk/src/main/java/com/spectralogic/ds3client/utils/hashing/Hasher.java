package com.spectralogic.ds3client.utils.hashing;

public interface Hasher {
    void update(byte[] bytes, int offset, int length);
    String digest();
}
