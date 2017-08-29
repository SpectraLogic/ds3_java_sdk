package com.spectralogic.ds3client.utils.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Wraps a Stream as an Iterable
 */
public class StreamWrapper<T> implements Iterable<T> {

    private final Stream<T> stream;

    public static <T> Iterable<T> wrapStream(final Stream<T> stream) {
        return new StreamWrapper<>(stream);
    }

    private StreamWrapper(final Stream<T> stream) {
        this.stream = stream;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return stream.iterator();
    }
}
