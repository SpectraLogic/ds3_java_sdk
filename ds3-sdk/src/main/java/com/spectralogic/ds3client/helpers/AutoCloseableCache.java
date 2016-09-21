/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

package com.spectralogic.ds3client.helpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class AutoCloseableCache<Key, Value extends AutoCloseable> implements AutoCloseable {
    private final ValueBuilder<Key, Value> valueBuilder;
    private final Set<Key> closedKeys = new HashSet<>();
    private Map<Key, Value> values = new HashMap<>();

    public interface ValueBuilder<Key, Value extends AutoCloseable> {
        Value get(final Key key);
    }
    
    public AutoCloseableCache(final ValueBuilder<Key, Value> valueBuilder) {
        this.valueBuilder = valueBuilder;
    }

    public synchronized Value get(final Key key) {
        if (this.values == null) {
            throw new IllegalStateException("Cache already closed.");
        }
        if (this.closedKeys.contains(key)) {
            throw new IllegalStateException("Cache has already closed the requested key.");
        }
        Value value = this.values.get(key);
        if (value == null) {
            value = this.valueBuilder.get(key);
            this.values.put(key, value);
        }
        return value;
    }

    public synchronized void close(final Key key) throws Exception {
        if (this.values == null) {
            throw new IllegalStateException("Cache already closed.");
        }
        final Value value = this.values.remove(key);
        if (value != null) {
            value.close();
        }
        this.closedKeys.add(key);
    }

    @Override
    public synchronized void close() throws Exception {
        if (this.values != null) {
            for (final Value value : this.values.values()) {
                value.close();
            }
            this.values = null;
        }
    }
}
