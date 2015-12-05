package com.spectralogic.ds3client.utils;

import java.util.Collection;

public final class Guard {

    private Guard() {
        //pass
    }

    public static boolean isNullOrEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotNullAndNotEmpty(final Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }
}
