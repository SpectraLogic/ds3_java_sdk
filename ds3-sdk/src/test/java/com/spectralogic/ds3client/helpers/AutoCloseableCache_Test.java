/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.helpers.AutoCloseableCache.ValueBuilder;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class AutoCloseableCache_Test {
    @Test
    public void returnsSameInstanceWhenCalledTwice() throws Exception {
        final AutoCloseableCache<String, CloseableImpl> closeableCache =
            new AutoCloseableCache<>(new CloseableImplValueBuilder());
        final CloseableImpl item = closeableCache.get("foo");
        assertThat(item.getKey(), is("foo"));
        assertThat(closeableCache.get("foo"), is(sameInstance(item)));
        closeableCache.close();
    }

    @Test
    public void cacheDisposesAllItemsExactlyOnce() throws Exception {
        final AutoCloseableCache<String, CloseableImpl> closeableCache =
            new AutoCloseableCache<>(new CloseableImplValueBuilder());

        final CloseableImpl item1 = closeableCache.get("foo");
        final CloseableImpl item2 = closeableCache.get("bar");

        assertThat(item1.getCloseCallCount(), is(0));
        assertThat(item2.getCloseCallCount(), is(0));

        closeableCache.close();

        assertThat(item1.getCloseCallCount(), is(1));
        assertThat(item2.getCloseCallCount(), is(1));

        closeableCache.close();

        assertThat(item1.getCloseCallCount(), is(1));
        assertThat(item2.getCloseCallCount(), is(1));
    }
    
    @Test
    public void cacheCanCloseItemsEarly() throws Exception {
        final AutoCloseableCache<String, CloseableImpl> closeableCache =
            new AutoCloseableCache<>(new CloseableImplValueBuilder());

        final CloseableImpl item1 = closeableCache.get("foo");
        final CloseableImpl item2 = closeableCache.get("bar");

        assertThat(item1.getCloseCallCount(), is(0));
        assertThat(item2.getCloseCallCount(), is(0));

        closeableCache.close("foo");

        assertThat(item1.getCloseCallCount(), is(1));
        assertThat(item2.getCloseCallCount(), is(0));
        
        try {
            closeableCache.get("foo");
            fail();
        } catch (final IllegalStateException e) {
        }

        closeableCache.close();

        assertThat(item1.getCloseCallCount(), is(1));
        assertThat(item2.getCloseCallCount(), is(1));
    }

    @Test
    public void cacheCanCloseBeforeGet() throws Exception {
        final AutoCloseableCache<String, CloseableImpl> closeableCache =
            new AutoCloseableCache<>(new CloseableImplValueBuilder());

        closeableCache.close("foo");
        
        try {
            closeableCache.get("foo");
            fail();
        } catch (final IllegalStateException e) {
        }
    } 
    
    @Test
    public void cacheCallsFailWhenDisposed() throws Exception {
        final AutoCloseableCache<String, CloseableImpl> closeableCache =
            new AutoCloseableCache<>(new CloseableImplValueBuilder());
        closeableCache.close();

        try {
            closeableCache.get("foo");
            fail();
        } catch (final IllegalStateException e) {
        }
        try {
            closeableCache.close("foo");
            fail();
        } catch (final IllegalStateException e) {
        }
    }
    
    private final class CloseableImplValueBuilder implements ValueBuilder<String, CloseableImpl> {
        @Override
        public CloseableImpl get(final String key) {
            return new CloseableImpl(key);
        }
    }

    private static final class CloseableImpl implements AutoCloseable {
        private final String key;
        private int closeCallCount = 0;

        public CloseableImpl(final String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }

        public int getCloseCallCount() {
            return this.closeCallCount;
        }

        @Override
        public void close() throws Exception {
            this.closeCallCount++;
        }
    }
}
