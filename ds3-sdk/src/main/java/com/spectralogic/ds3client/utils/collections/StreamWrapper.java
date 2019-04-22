/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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
