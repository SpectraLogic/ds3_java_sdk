/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

import com.spectralogic.ds3client.utils.Guard;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Produces an Iterator that will lazily load content.
 * Our definition of lazy for this class, is that the Iterator is returned right
 * away with no data.  Data is only then loaded, as needed, in pieces as defined by the LazyIterableLoader.
 *
 * To use, supply a {@link LazyLoaderFactory} which is used to fetch data as it is needed.
 */
public class LazyIterable<T> implements Iterable<T> {

    final private LazyLoaderFactory<T> lazyLoaderFactory;

    public LazyIterable(final LazyLoaderFactory<T> lazyLoaderFactory) {
        this.lazyLoaderFactory = lazyLoaderFactory;
    }

    @Override
    public Iterator<T> iterator() {
        return new LazyObjectIterator<>(lazyLoaderFactory.create());
    }

    /**
     * A factory that creates instances of LazyLoaders.  It's important that this factory
     * does not return the same loader, otherwise every iterator created with this factory
     * will share the same state and can lead to undefined behavior.
     * @param <T> The model that is returned by the iterator
     */
    public interface LazyLoaderFactory<T> {
        LazyLoader<T> create();
    }

    /**
     * A loader is responsible for returning new data on each call to {@link LazyLoader#getNextValues()}.
     *
     * If using a Spectra S3 API call see {@link com.spectralogic.ds3client.helpers.pagination.SpectraS3PaginationLoader}
     * as the base implementation to handle most of the details of creating a new LazyLoader
     * @param <T> The model that is returned by the iterator
     */
    public interface LazyLoader<T> {
        List<T> getNextValues();
    }

    private class LazyObjectIterator<E> implements Iterator<E> {

        private final LazyLoader<E> iterableLoader;

        private List<E> cache = null;
        private int cachePointer;
        private boolean endOfContent = false;

        private LazyObjectIterator(final LazyLoader<E> iterableLoader) {
            this.iterableLoader = iterableLoader;
        }

        @Override
        public boolean hasNext() {
            if (endOfContent) {
                return false;
            }

            if (cache == null || cachePointer == cache.size()) {
                loadCache();
            }
            return !endOfContent;
        }

        @Override
        public E next() {
            if (endOfContent) {
                throw new NoSuchElementException("No more content");
            }
            if (cache == null || cachePointer >= cache.size()) {
                loadCache();
            }
            final E contents = cache.get(cachePointer);
            this.cachePointer++;
            return contents;
        }

        private void loadCache() {
            this.cache = iterableLoader.getNextValues();
            if (Guard.isNullOrEmpty(this.cache)) {
                endOfContent = true;
            } else {
                this.cachePointer = 0;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("The remove method on the LazyObjectIterator is not supported");
        }
    }
}
