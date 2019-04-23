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

package com.spectralogic.ds3client.helpers;

import com.google.common.util.concurrent.Striped;
import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectChannelBuilder;
import com.spectralogic.ds3client.utils.FileUtils;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.Lock;

/**
 * Writes files to the local file system preserving the path.
 */
public class FileObjectGetter implements ObjectChannelBuilder {
    private final Path root;
    private final Striped<Lock> striped;

    /**
     * Creates a new FileObjectGetter to retrieve files from a remote DS3 system to the local file system.
     *
     * @param root The {@code root} directory of the local file system for all files being transferred.
     */
    public FileObjectGetter(final Path root) {
        this.root = root;
        this.striped = Striped.lazyWeakLock(10);
    }

    @Override
    public SeekableByteChannel buildChannel(final String key) throws IOException {
        final Path objectPath = this.root.resolve(key);
        final Path parentPath = objectPath.getParent();
        if (parentPath != null) {
            Files.createDirectories(FileUtils.resolveForSymbolic(parentPath));
        }

        if (!FileUtils.isTransferablePath(objectPath)) {
            throw new UnrecoverableIOException(objectPath + " is not a regular file.");
        }

        final Lock lock = striped.get(key);
        try {
            lock.lock();
            if (Files.notExists(objectPath)) {
                Files.createDirectories(objectPath.getParent());
                return FileChannel.open(
                        objectPath,
                        StandardOpenOption.SPARSE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.CREATE_NEW
                );
            }
        } finally {
            lock.unlock();
        }
        return FileChannel.open(
                objectPath,
                StandardOpenOption.WRITE
        );
    }
}