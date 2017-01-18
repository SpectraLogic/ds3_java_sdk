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

import com.spectralogic.ds3client.MockedHeadersReturningKeys;
import com.spectralogic.ds3client.commands.interfaces.MetadataImpl;
import com.spectralogic.ds3client.networking.Metadata;
import com.spectralogic.ds3client.utils.Platform;
import org.apache.commons.io.FileUtils;
import org.junit.Assume;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MetadataReceivedListenerImpl_Test {
    @Test
    public void testGettingMetadata() throws IOException, InterruptedException {
        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String fileName = "Gracie.txt";

        final Path filePath = Files.createFile(Paths.get(tempDirectory.toString(), fileName));

        try {
            // set permissions
            if ( ! Platform.isWindows()) {
                final PosixFileAttributes attributes = Files.readAttributes(filePath, PosixFileAttributes.class);
                final Set<PosixFilePermission> permissions = attributes.permissions();
                permissions.clear();
                permissions.add(PosixFilePermission.OWNER_READ);
                permissions.add(PosixFilePermission.OWNER_WRITE);
                Files.setPosixFilePermissions(filePath, permissions);
            }

            // get permissions
            final Map<String, Path> fileMapper = new HashMap<>(1);
            fileMapper.put(filePath.toString(), filePath);
            final Map<String, String> metadataFromFile = new MetadataAccessImpl(fileMapper).getMetadataValue(filePath.toString());

            // change permissions
            if (Platform.isWindows()) {
                Runtime.getRuntime().exec("attrib -A " + filePath.toString()).waitFor();
            } else {
                final PosixFileAttributes attributes = Files.readAttributes(filePath, PosixFileAttributes.class);
                final Set<PosixFilePermission> permissions = attributes.permissions();
                permissions.clear();
                permissions.add(PosixFilePermission.OWNER_READ);
                permissions.add(PosixFilePermission.OWNER_WRITE);
                permissions.add(PosixFilePermission.OWNER_EXECUTE);
                Files.setPosixFilePermissions(filePath, permissions);
            }

            // put old permissions back
            final Metadata metadata = new MetadataImpl(new MockedHeadersReturningKeys(metadataFromFile));

            new MetadataReceivedListenerImpl(tempDirectory.toString()).metadataReceived(fileName, metadata);

            // see that we put back the original metadata
            fileMapper.put(filePath.toString(), filePath);
            final Map<String, String> metadataFromUpdatedFile = new MetadataAccessImpl(fileMapper).getMetadataValue(filePath.toString());

            if (Platform.isWindows()) {
                assertEquals("A", metadataFromUpdatedFile.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_FLAGS));
            } else {
                assertEquals("100600", metadataFromUpdatedFile.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_MODE));
                assertEquals("600(rw-------)", metadataFromUpdatedFile.get(MetadataKeyConstants.METADATA_PREFIX + MetadataKeyConstants.KEY_PERMISSION));
            }

        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }

    @Test(expected = NoSuchFileException.class)
    public void testGettingMetadataFailureHandler() throws IOException, InterruptedException {
        Assume.assumeFalse(Platform.isWindows());

        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String fileName = "Gracie.txt";

        final Path filePath = Files.createFile(Paths.get(tempDirectory.toString(), fileName));

        try {
            // set permissions
            if ( ! Platform.isWindows()) {
                final PosixFileAttributes attributes = Files.readAttributes(filePath, PosixFileAttributes.class);
                final Set<PosixFilePermission> permissions = attributes.permissions();
                permissions.clear();
                permissions.add(PosixFilePermission.OWNER_READ);
                permissions.add(PosixFilePermission.OWNER_WRITE);
                Files.setPosixFilePermissions(filePath, permissions);
            }

            // get permissions
            final Map<String, Path> fileMapper = new HashMap<>(1);
            fileMapper.put(filePath.toString(), filePath);
            final Map<String, String> metadataFromFile = new MetadataAccessImpl(fileMapper).getMetadataValue(filePath.toString());

            FileUtils.deleteDirectory(tempDirectory.toFile());

            // put old permissions back
            final Metadata metadata = new MetadataImpl(new MockedHeadersReturningKeys(metadataFromFile));

            new MetadataReceivedListenerImpl(tempDirectory.toString()).metadataReceived(fileName, metadata);
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }

    @Test(expected = NoSuchFileException.class)
    public void testGettingMetadataFailureHandlerWindows() throws IOException, InterruptedException {
        Assume.assumeTrue(Platform.isWindows());

        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String fileName = "Gracie.txt";

        final Path filePath = Files.createFile(Paths.get(tempDirectory.toString(), fileName));

        try {
            // get permissions
            final Map<String, Path> fileMapper = new HashMap<>(1);
            fileMapper.put(filePath.toString(), filePath);
            final Map<String, String> metadataFromFile = new MetadataAccessImpl(fileMapper).getMetadataValue(filePath.toString());

            FileUtils.deleteDirectory(tempDirectory.toFile());

            // put old permissions back
            final Metadata metadata = new MetadataImpl(new MockedHeadersReturningKeys(metadataFromFile));

            new MetadataReceivedListenerImpl(tempDirectory.toString()).metadataReceived(fileName, metadata);
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }
}
