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
