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

import com.spectralogic.ds3client.helpers.Ds3ClientHelpers.ObjectGetter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Writes files to the local file system preserving the path.
 */
public class VerifyingFileObjectGetter implements ObjectGetter {
    private final Path root;

    /**
     * Creates a new FileObjectGetter to retrieve files from a remote DS3 system to the local file system.
     * @param root The {@code root} directory of the local file system for all files being transferred.
     */
    public VerifyingFileObjectGetter(final Path root) {
        this.root = root;
    }

    @Override
    public void writeContents(final String key, final InputStream contents, final String md5) throws IOException {
        final Path file = this.root.resolve(key);
        Files.createDirectories(file.getParent());
        try {
            final MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            try (final OutputStream output = new DigestOutputStream(Files.newOutputStream(
                    file,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.TRUNCATE_EXISTING
            ), md5Digest)) {
                IOUtils.copy(contents, output);
            }
            final String hashedComputedDigest = Base64.encodeBase64String(md5Digest.digest());
            if (!hashedComputedDigest.equalsIgnoreCase(md5)) {
                throw new IOException("Computed MD5 " + hashedComputedDigest + " does not match the expected value " + md5 + " for object " + key);
            }
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
