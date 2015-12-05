package com.spectralogic.ds3client.utils;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Guard_Test {

    @Test
    public void testCollectionNull() {
        assertTrue(Guard.isNullOrEmpty(null));
    }

    @Test
    public void testCollectionEmpty() {
        assertTrue(Guard.isNullOrEmpty(Lists.newArrayList()));
    }

    @Test
    public void testNotEmptyCollection() {
        assertFalse(Guard.isNullOrEmpty(Lists.newArrayList("hi")));
    }
}
