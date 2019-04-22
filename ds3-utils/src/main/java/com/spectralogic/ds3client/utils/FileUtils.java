/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public final class FileUtils {
    private FileUtils() {}

    public static Path resolveForSymbolic(final Path path) throws IOException {
        if (Files.isSymbolicLink(path)) {
            final Path simLink = Files.readSymbolicLink(path);
            if (!simLink.isAbsolute()) {
                // Resolve the path such that the path is relative to the symbolically
                // linked file's directory
                final Path symLinkParent = path.toAbsolutePath().getParent();
                if (symLinkParent == null) {
                    throw new IOException("Could not resolve real path of symbolic link");
                }
                return symLinkParent.resolve(simLink);
            }

            return simLink;
        }
        return path;
    }

    public static boolean isTransferablePath(final Path path) {
        if ( ! Platform.isWindows()) {
            try {
                final BasicFileAttributes fileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
                return fileAttributes.isRegularFile() || fileAttributes.isDirectory();
            } catch (final NoSuchFileException e) {
                return true;
            } catch (final IOException e) {
                return false;
            }
        }

        return true;
    }
}