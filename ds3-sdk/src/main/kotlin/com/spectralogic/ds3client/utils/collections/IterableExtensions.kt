package com.spectralogic.ds3client.utils.collections

fun <T> Iterator<T>.asIterable(): Iterable<T> {
    return IterableWrapper(this)
}

private class IterableWrapper<out T> constructor(private val iterator: Iterator<T>): Iterable<T> {
    override fun iterator(): Iterator<T> = iterator
}
