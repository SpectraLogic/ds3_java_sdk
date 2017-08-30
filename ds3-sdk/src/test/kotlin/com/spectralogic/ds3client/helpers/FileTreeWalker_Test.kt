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
        }

        val fileList = listBuilder.build()

        assertThat(fileList.size, `is`(2))

    }
}