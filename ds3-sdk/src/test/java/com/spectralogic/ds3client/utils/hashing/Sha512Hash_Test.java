package com.spectralogic.ds3client.utils.hashing;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Sha512Hash_Test {
    @Test
    public void calculateSha512() {
        final SHA512Hasher hasher = new SHA512Hasher();

        assertThat(hasher.digest(), is("z4PhNX7vuL3xVChQ1m2AB9Yg5AULVxXcg/SpIdNs6c5H0NE8XYXysP+DGNKHfuwvY7kxvUdBeoGlODJ6+SfaPg=="));
    }
}
