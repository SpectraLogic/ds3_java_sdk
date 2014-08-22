package com.spectralogic.ds3client.models.bulk;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.util.ArrayList;

public enum WriteOptimization {
    CAPACITY, PERFORMANCE;
	
	    public static String valuesString() {
        final ArrayList<WriteOptimization> list = Lists.newArrayList(WriteOptimization.values());
        return Joiner.on(", ").join(Lists.transform(list, new Function<WriteOptimization, String>() {
            @Override
            public String apply(final WriteOptimization input) {
                return input.toString().toLowerCase();
            }
        }));
    }
}
