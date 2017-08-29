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

import com.google.common.collect.Lists
import org.junit.Test

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat

class WindowedIterator_Test {
    @Test
    fun iterateWholeCollection() {
        val list = Lists.newArrayList("value", "hi", "anothervalue")

        val iterator = list.iterator().take(Int.MAX_VALUE)

        var count = 0
        iterator.forEach {
            count++
        }

        assertThat(count, `is`(3))
    }

    @Test
    fun iteratePart() {
        val list = Lists.newArrayList("value", "hi", "anothervalue")

        val iterator = list.iterator().take(1)

        var count = 0
        iterator.forEach {
            count++
        }

        assertThat(count, `is`(1))
    }

    @Test
    fun iterateAfterFirstIteration() {
        val list = Lists.newArrayList("value", "hi", "anothervalue")

        val parentIterator = list.iterator()
        val firstIterator = parentIterator.take(1)

        var firstCount = 0
        firstIterator.forEach {
            firstCount++
        }

        assertThat(firstCount, `is`(1))

        val secondIterator = parentIterator.take(Int.MAX_VALUE)

        var secondCount = 0
        secondIterator.forEach {
            secondCount++
        }

        assertThat(secondCount, `is`(2))
    }
}