/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Writes files to a remote DS3 appliance from a directory in the local filesystem.
 */
public class FileObjectPutter implements ObjectChannelBuilder {

    private final Path root;

    /**
     * Creates a new FileObjectPutter given a directory in the local file system.
     * @param root The {@code root} directory for all the files being transferred.
     */
    public FileObjectPutter(final Path root) {
        this.root = root;
    }

    @Override
    public SeekableByteChannel buildChannel(final String key) throws IOException {

        final Path path = this.root.resolve(key);

        return FileChannel.open(resolveForSymbolic(path), StandardOpenOption.READ);
    }

    private static Path resolveForSymbolic(final Path path) throws IOException {
        if (Files.isSymbolicLink(path)) {
            final Path simLink = Files.readSymbolicLink(path);
            if (!simLink.isAbsolute()) {
                // Resolve the path such that the path is relative to the symbolically
                // linked file's directory
                final Path symLinkParent = path.toAbsolutePath().getParent();
                return symLinkParent.resolve(simLink);
            }

            return simLink;
        }
        return path;
    }
}
