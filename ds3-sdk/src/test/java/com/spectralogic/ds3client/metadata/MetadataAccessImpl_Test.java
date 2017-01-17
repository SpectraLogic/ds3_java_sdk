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

import com.spectralogic.ds3client.metadata.interfaces.MetadataStoreListener;
import com.spectralogic.ds3client.utils.Platform;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class MetadataAccessImpl_Test {
    @Test
    public void testGettingMetadata() throws IOException, InterruptedException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String fileName = "Gracie.txt";

        try {
            final Path filePath = Files.createFile(Paths.get(tempDirectory.toString(), fileName));

            final PosixFileAttributes attributes = Files.readAttributes(filePath, PosixFileAttributes.class);
            final Set<PosixFilePermission> permissions = attributes.permissions();
            permissions.clear();
            permissions.add(PosixFilePermission.OWNER_READ);
            permissions.add(PosixFilePermission.OWNER_WRITE);
            Files.setPosixFilePermissions(filePath, permissions);

            final Map<String, Path> fileMapper = new HashMap<>(1);
            fileMapper.put(filePath.toString(), filePath);
            final Map<String, String> metadata = new MetadataAccessImpl(fileMapper, new MetadataStoreListener() {
                @Override
                public void onMetadataFailed(final String message) {
                    fail("Error getting file metadata: " + message);
                }
            }).getMetadataValue(filePath.toString());

            assertEquals(metadata.size(), 10);

            if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
                assertTrue(metadata.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_OS).toLowerCase().startsWith("mac"));
            } else if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {
                assertTrue(metadata.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_OS).toLowerCase().startsWith("linux"));
            } else if (Platform.isWindows()) {
                assertTrue(metadata.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_OS).toLowerCase().startsWith("windows"));
            }

            assertEquals("100600", metadata.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_MODE));
            assertEquals("600(rw-------)", metadata.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_PERMISSION));
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }

    @Test
    public void testMetadataAccessFailureHandler() throws IOException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String fileName = "Gracie.txt";

        try {
            final Path filePath = Files.createFile(Paths.get(tempDirectory.toString(), fileName));

            tempDirectory.toFile().setExecutable(false);

            final Map<String, Path> fileMapper = new HashMap<>(1);

            final AtomicInteger numTimesHandlerCalled = new AtomicInteger(0);

            fileMapper.put(filePath.toString(), filePath);
            final Map<String, String> metadata = new MetadataAccessImpl(fileMapper, new MetadataStoreListener() {
                @Override
                public void onMetadataFailed(final String message) {
                    numTimesHandlerCalled.incrementAndGet();
                }
            }).getMetadataValue(filePath.toString());

            assertEquals(1, numTimesHandlerCalled.get());
        } finally {
            tempDirectory.toFile().setExecutable(true);
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }
    /*
    if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS) {
            // Deny write data access to everyone, making the directory unwritable
            Runtime.getRuntime().exec("icacls dir /deny Everyone:(WD)").waitFor();
        } else {
            directory.toFile().setWritable(false);
        }
     */

    /*
    if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS) {
                // Grant write data access to everyone, making the directory writable, so we can delete it.
                Runtime.getRuntime().exec("icacls dir /grant Everyone:(WD)").waitFor();
            } else {
                directory.toFile().setWritable(true);
            }
     */
}
