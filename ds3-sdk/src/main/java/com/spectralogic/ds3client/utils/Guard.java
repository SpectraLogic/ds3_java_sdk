package com.spectralogic.ds3client.utils;

import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Map;

public final class Guard {

    private Guard() {
        //pass
    }

    public static boolean isNullOrEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isStringNullOrEmpty(final String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotNullAndNotEmpty(final Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }

    public static boolean isMultiMapNullOrEmpty(final Multimap<?, ?> multimap) {
        return multimap == null || multimap.isEmpty();
    }

    public static boolean isMapNullOrEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
