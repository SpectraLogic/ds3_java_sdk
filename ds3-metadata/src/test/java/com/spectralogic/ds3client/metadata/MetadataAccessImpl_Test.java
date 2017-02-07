/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

package com.spectralogic.ds3client.metadata;

import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.utils.Platform;
import org.apache.commons.io.FileUtils;
import org.junit.Assume;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Map;
import java.util.Set;

public class MetadataAccessImpl_Test {
    @Test
    public void testGettingMetadata() throws IOException, InterruptedException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String fileName = "Gracie.txt";

        try {
            final Path filePath = Files.createFile(Paths.get(tempDirectory.toString(), fileName));

            if ( ! Platform.isWindows()) {
                final PosixFileAttributes attributes = Files.readAttributes(filePath, PosixFileAttributes.class);
                final Set<PosixFilePermission> permissions = attributes.permissions();
                permissions.clear();
                permissions.add(PosixFilePermission.OWNER_READ);
                permissions.add(PosixFilePermission.OWNER_WRITE);
                Files.setPosixFilePermissions(filePath, permissions);
            }

            final ImmutableMap.Builder<String, Path> fileMapper = ImmutableMap.builder();
            fileMapper.put(filePath.toString(), filePath);
            final Map<String, String> metadata = new MetadataAccessImpl(fileMapper.build()).getMetadataValue(filePath.toString());

            if (Platform.isWindows()) {
                assertEquals(metadata.size(), 13);
            } else {
                assertEquals(metadata.size(), 10);
            }

            if (Platform.isMac()) {
                assertTrue(metadata.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_OS).toLowerCase().startsWith(Platform.MAC_SYSTEM_NAME));
            } else if (Platform.isLinux()) {
                assertTrue(metadata.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_OS).toLowerCase().startsWith(Platform.LINUX_SYSTEM_NAME));
            } else if (Platform.isWindows()) {
                assertTrue(metadata.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_OS).toLowerCase().startsWith(Platform.WINDOWS_SYSTEM_NAME));
            }

            if (Platform.isWindows()) {
                assertEquals("A", metadata.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_FLAGS));
            } else {
                assertEquals("100600", metadata.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_MODE));
                assertEquals("600(rw-------)", metadata.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_PERMISSION));
            }
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testMetadataAccessFailureHandler() throws IOException, InterruptedException {
        Assume.assumeFalse(Platform.isWindows());

        final Path directory = Paths.get(".");

        final String fileName = "Gracie.txt";

        final Path filePath = Paths.get(directory.toString(), fileName);

        final ImmutableMap.Builder<String, Path> fileMapper = ImmutableMap.builder();

        fileMapper.put(filePath.toString(), filePath);
        new MetadataAccessImpl(fileMapper.build()).getMetadataValue(filePath.toString());
    }

    @Test(expected = RuntimeException.class)
    public void testMetadataAccessFailureHandlerWindows() {
        Assume.assumeTrue(Platform.isWindows());

        final ImmutableMap.Builder<String, Path> fileMapper = ImmutableMap.builder();

        fileMapper.put("file", Paths.get("file"));
        new MetadataAccessImpl(fileMapper.build()).getMetadataValue("file");
    }
}
