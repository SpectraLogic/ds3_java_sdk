package com.spectralogic.ds3client.helpers

import com.google.common.collect.ImmutableList
import com.spectralogic.ds3client.models.bulk.Ds3Object
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat

class FileTreeWalker_Test {

    @get:Rule
    val tempDir = TemporaryFolder()

    @Test
    fun basicFileList() {
        tempDir.newFile("bar")
        tempDir.newFile("baz")

        val walk = FileTreeWalker.walk(tempDir.root.toPath())

        val listBuilder = ImmutableList.Builder<Ds3Object>()

        walk.forEach {
            listBuilder.add(it)
            println(it.name)
        }

        val fileList = listBuilder.build()

        assertThat(fileList.size, `is`(2))

    }
}