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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.IOUtils;

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectGetter;

public class FileObjectGetter implements ObjectGetter {
    private final Path root;

    public FileObjectGetter(final Path root) {
        this.root = root;
    }

    @Override
    public void writeContents(final String key, final InputStream contents) throws IOException {
        final Path file = this.root.resolve(key);
        Files.createDirectories(file.getParent());
        try (final OutputStream output = Files.newOutputStream(
                    file,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.TRUNCATE_EXISTING
                )) {
            IOUtils.copy(contents, output);
        }
    }
}
