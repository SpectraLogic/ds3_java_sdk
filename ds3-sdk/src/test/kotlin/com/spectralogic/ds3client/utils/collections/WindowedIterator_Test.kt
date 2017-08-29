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