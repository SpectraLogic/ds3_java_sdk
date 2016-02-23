package com.spectralogic.ds3client.utils.hashing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Sha256Hash_Test {
    @Test
    public void calculateSha256() {
        final SHA256Hasher hasher = new SHA256Hasher();

        assertThat(hasher.digest(), is("47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU="));
    }
}
