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

package com.spectralogic.ds3client.metadata;

import com.google.common.collect.ImmutableMap;
import com.spectralogic.ds3client.helpers.FailureEventListener;
import com.spectralogic.ds3client.helpers.events.FailureEvent;
import com.spectralogic.ds3client.utils.Platform;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assume;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataAccessImpl_Test {
    private static final Logger LOG = LoggerFactory.getLogger(MetadataAccessImpl_Test.class);

    @Test
    public void testGettingMetadata() throws IOException {
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

    @Test
    public void testMetadataAccessFailureHandler() {
        Assume.assumeFalse(Platform.isWindows());

        try {
            final String tempPathPrefix = null;
            final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

            final String fileName = "Gracie.txt";

            final Path filePath = Files.createFile(Paths.get(tempDirectory.toString(), fileName));

            try {
                tempDirectory.toFile().setExecutable(false);

                final ImmutableMap.Builder<String, Path> fileMapper = ImmutableMap.builder();

                fileMapper.put(filePath.toString(), filePath);
                new MetadataAccessImpl(fileMapper.build()).getMetadataValue(filePath.toString());
            } finally {
                tempDirectory.toFile().setExecutable(true);
                FileUtils.deleteDirectory(tempDirectory.toFile());
            }
        } catch (final Throwable t) {
            fail("Throwing exceptions from metadata est verbotten");
        }
    }

    @Test
    public void testMetadataAccessFailureHandlerWithEventHandler() throws IOException {
        Assume.assumeFalse(Platform.isWindows());

        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String fileName = "Gracie.txt";

        final Path filePath = Files.createFile(Paths.get(tempDirectory.toString(), fileName));

        try {
            tempDirectory.toFile().setExecutable(false);

            final ImmutableMap.Builder<String, Path> fileMapper = ImmutableMap.builder();

            fileMapper.put(filePath.toString(), filePath);

            final AtomicInteger numTimesFailureHandlerCalled = new AtomicInteger(0);

            try (final InputStream inputStream = Runtime.getRuntime().exec("ls -lR").getInputStream()) {
                LOG.info(IOUtils.toString(inputStream, Charset.defaultCharset()) );
            }
            new MetadataAccessImpl(fileMapper.build(),
                    new FailureEventListener() {
                        @Override
                        public void onFailure(final FailureEvent failureEvent) {
                            numTimesFailureHandlerCalled.incrementAndGet();
                            assertEquals(FailureEvent.FailureActivity.RecordingMetadata, failureEvent.doingWhat());
                        }
                    },
                    "localhost")
                    .getMetadataValue("forceAFailureByUsingANonExistentFileBecauseTheDockerImageRunsAsRoot");

            assertEquals(1, numTimesFailureHandlerCalled.get());
        } finally {
            tempDirectory.toFile().setExecutable(true);
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }
    }

    @Test
    public void testMetadataAccessFailureHandlerWindows() {
        Assume.assumeTrue(Platform.isWindows());

        try {
            final ImmutableMap.Builder<String, Path> fileMapper = ImmutableMap.builder();

            final String fileName = "file";

            fileMapper.put(fileName, Paths.get(fileName));

            final AtomicInteger numTimesFailureHandlerCalled = new AtomicInteger(0);

            new MetadataAccessImpl(fileMapper.build(),
                    new FailureEventListener() {
                        @Override
                        public void onFailure(final FailureEvent failureEvent) {
                            numTimesFailureHandlerCalled.incrementAndGet();
                            assertEquals(FailureEvent.FailureActivity.RecordingMetadata, failureEvent.doingWhat());
                        }
                    },
                    "localhost")
                    .getMetadataValue(fileName);

            assertEquals(1, numTimesFailureHandlerCalled.get());
        } catch (final Throwable t) {
            fail("Throwing exceptions from metadata est verbotten.");
        }
    }
}
