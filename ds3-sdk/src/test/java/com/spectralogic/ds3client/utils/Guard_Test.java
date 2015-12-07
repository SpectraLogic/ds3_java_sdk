package com.spectralogic.ds3client.utils;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Guard_Test {

    @Test
    public void testCollectionNull() {
        assertTrue(Guard.isNullOrEmpty(null));
        assertFalse(Guard.isNotNullAndNotEmpty(null));
    }

    @Test
    public void testCollectionEmpty() {
        final List<String> data = Lists.newArrayList();
        assertTrue(Guard.isNullOrEmpty(data));
        assertFalse(Guard.isNotNullAndNotEmpty(data));
    }

    @Test
    public void testNotEmptyCollection() {
        final List<String> data = Lists.newArrayList("hi");
        assertFalse(Guard.isNullOrEmpty(data));
        assertTrue(Guard.isNotNullAndNotEmpty(data));
    }
}
