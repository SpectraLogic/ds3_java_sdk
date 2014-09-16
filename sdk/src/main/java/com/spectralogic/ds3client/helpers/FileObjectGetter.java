/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Writes files to the local file system preserving the path.
 */
public class FileObjectGetter implements ObjectChannelBuilder {
    private final Path root;

    /**
     * Creates a new FileObjectGetter to retrieve files from a remote DS3 system to the local file system.
     * @param root The {@code root} directory of the local file system for all files being transferred.
     */
    public FileObjectGetter(final Path root) {
        this.root = root;
    }

    @Override
    public SeekableByteChannel buildChannel(final String key) throws IOException {
        final Path objectPath = this.root.resolve(key);
        Files.createDirectories(objectPath.getParent());
        return FileChannel.open(
            objectPath,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE_NEW,
            StandardOpenOption.TRUNCATE_EXISTING
        );
    }
}
