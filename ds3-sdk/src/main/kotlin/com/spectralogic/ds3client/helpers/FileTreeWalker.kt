package com.spectralogic.ds3client.helpers

import com.spectralogic.ds3client.models.bulk.Ds3Object
import com.spectralogic.ds3client.utils.Guard
import com.spectralogic.ds3client.utils.collections.StreamWrapper
import java.nio.file.Files
import java.nio.file.Path

object FileTreeWalker {
    /**
     * Walks a file system starting at {@param startDir} and returns a Lazy Iterable with all the
     * files represented as Ds3Objects.
     */
    fun walk(startDir: Path) : Iterable<Ds3Object> {
        return StreamWrapper.wrapStream(Files.walk(startDir).map {
            Ds3Object(startDir.relativize(it).toString().replace("\\", "/"), Files.size(it))
        }.filter {
            !Guard.isStringNullOrEmpty(it.name)  // skip entries that are null or are the empty string
        })
    }
}
