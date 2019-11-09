package com.spectralogic.ds3client.utils

import com.spectralogic.ds3client.helpers.channelbuilders.ReadOnlySeekableByteChannel

class ReadOnlySeekableByteChannelInputStream(
    readOnlySeekableByteChannel: ReadOnlySeekableByteChannel
) : SeekableByteChannelInputStream(readOnlySeekableByteChannel) {
    override fun reset(): Unit = Unit
}