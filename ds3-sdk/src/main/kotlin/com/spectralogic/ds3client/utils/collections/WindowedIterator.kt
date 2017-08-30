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

package com.spectralogic.ds3client.utils.collections

class WindowedIterator<out T> constructor(private val iterator: Iterator<T>, private val windowSize: Int) : Iterator<T> by iterator {

    init {
        if (windowSize <= 0) throw IllegalArgumentException("windowSize must be larger than 0")
    }

    private var count = 0

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException("There are no new items to return")

        count++
        return iterator.next()
    }

    override fun hasNext(): Boolean {
        return if (count >= windowSize) {
            false
        } else {
            iterator.hasNext()
        }
    }
}

fun <T> Iterator<T>.take(n: Int): Iterator<T> = WindowedIterator(this, n)

