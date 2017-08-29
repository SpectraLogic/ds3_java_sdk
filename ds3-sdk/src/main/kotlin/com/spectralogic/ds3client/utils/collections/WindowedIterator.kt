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

