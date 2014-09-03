package com.spectralogic.ds3client.models.bulk;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.ArrayList;

public enum Priority {
    CRITICAL,
    VERY_HIGH,
    HIGH,
    NORMAL,
    LOW,
    BACKGROUND,
    MINIMIZED_DUE_TO_TOO_MANY_RETRIES;

    public static String valuesString() {
        final ArrayList<Priority> list = Lists.newArrayList(Priority.values());
        return Joiner.on(", ").join(Lists.transform(list, new Function<Priority, String>() {
            @Override
            public String apply(final Priority input) {
	        return input.toString().toLowerCase();
            }
        }));
    }
}
