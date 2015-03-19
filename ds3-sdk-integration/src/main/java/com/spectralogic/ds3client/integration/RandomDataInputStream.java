package com.spectralogic.ds3client.integration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class RandomDataInputStream extends InputStream {

    private final Random rand;
    private final long length;
    private long count = 0;

    public RandomDataInputStream(final long seed, final long length) {
        this.rand = new Random(seed);
        this.length = length;
    }

    @Override
    public int read() throws IOException {
        if (count >= length) {
            return -1;
        }

        count++;
        return rand.nextInt();
    }
}
