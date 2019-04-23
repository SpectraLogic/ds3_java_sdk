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

import com.spectralogic.ds3client.utils.Platform;
import org.apache.commons.io.FileUtils;
import org.junit.Assume;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

public class FileObjectPutter_Test {

    final private static Logger LOG = LoggerFactory.getLogger(FileObjectPutter_Test.class);

    final private static String testString = "This is some test data.";
    final private static byte[] testData = testString.getBytes(Charset.forName("UTF-8"));

    /**
     * This test cannot run on Windows without extra privileges
     */
    @Test
    public void testSymlink() throws IOException {
        assumeFalse(Platform.isWindows());
        final Path tempDir = Files.createTempDirectory("ds3_file_object_putter_");
        final Path tempPath = Files.createTempFile(tempDir, "temp_", ".txt");

        try {
            try (final SeekableByteChannel channel = Files.newByteChannel(tempPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
                channel.write(ByteBuffer.wrap(testData));
            }

            final Path symLinkPath = tempDir.resolve("sym_" + tempPath.getFileName().toString());
            Files.createSymbolicLink(symLinkPath, tempPath);

            getFileWithPutter(tempDir, symLinkPath);
        } finally {
            Files.deleteIfExists(tempPath);
            Files.deleteIfExists(tempDir);
        }
    }

    @Test
    public void testRegularFile() throws IOException {
        final Path tempDir = Files.createTempDirectory("ds3_file_object_putter_");
        final Path tempPath = Files.createTempFile(tempDir, "temp_", ".txt");

        try {
            try (final SeekableByteChannel channel = Files.newByteChannel(tempPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
                channel.write(ByteBuffer.wrap(testData));
            }

            getFileWithPutter(tempDir, tempPath);
        } finally {
            Files.deleteIfExists(tempPath);
            Files.deleteIfExists(tempDir);
        }
    }

    @Test
    public void testRelativeSymlink() throws IOException, URISyntaxException {
        assumeFalse(Platform.isWindows());
        final Path tempDir = Files.createTempDirectory("ds3_file_object_rel_test_");
        final Path tempPath = Files.createTempFile(tempDir, "temp_", ".txt");

        try {
            try (final SeekableByteChannel channel = Files.newByteChannel(tempPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
                channel.write(ByteBuffer.wrap(testData));
            }

            final Path symLinkPath = tempDir.resolve("sym_" + tempPath.getFileName().toString());
            final Path relPath = Paths.get("..", getParentDir(tempPath), tempPath.getFileName().toString());

            LOG.info("Creating symlink from " + symLinkPath.toString() + " to " + relPath.toString());

            Files.createSymbolicLink(symLinkPath, relPath);
            getFileWithPutter(tempDir, symLinkPath);

        } finally {
            Files.deleteIfExists(tempPath);
            Files.deleteIfExists(tempDir);
        }
    }

    private void getFileWithPutter(final Path dir, final Path file) throws IOException {
             try {
                final FileObjectPutter putter = new FileObjectPutter(dir);
                try (final SeekableByteChannel newChannel = putter.buildChannel(file.getFileName().toString())) {
                    assertThat(newChannel, is(notNullValue()));
                    final ByteBuffer buff = ByteBuffer.allocate(testData.length);
                    assertThat(newChannel.read(buff), is(testData.length));
                    assertThat(new String(buff.array(), Charset.forName("UTF-8")), is(testString));
                }
            } finally {
                Files.deleteIfExists(file);
            }
    }

    private static String getParentDir(final Path path) {
        final String parentPath = path.getParent().toString();
        final int lastIndex = parentPath.lastIndexOf(File.separator);
        return parentPath.substring(lastIndex + 1);
    }

    @Test
    public void testThatFileReportsAsRegularOnWindows() throws IOException, InterruptedException {
        Assume.assumeTrue(Platform.isWindows());

        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String A_FILE_NAME = "aFile.txt";

        final AtomicBoolean caughtException = new AtomicBoolean(false);

        try {
            Files.createFile(Paths.get(tempDirectory.toString(), A_FILE_NAME));
            new FileObjectPutter(tempDirectory).buildChannel(A_FILE_NAME);
        } catch (final UnrecoverableIOException e) {
            assertTrue(e.getMessage().contains(A_FILE_NAME));
            caughtException.set(true);
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }

        assertFalse(caughtException.get());
    }

    @Test
    public void testThatNamedPipeThrows() throws IOException, InterruptedException {
        Assume.assumeFalse(Platform.isWindows());

        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);

        final String FIFO_NAME = "bFifo";

        final AtomicBoolean caughtException = new AtomicBoolean(false);

        try {
            Runtime.getRuntime().exec("mkfifo " + Paths.get(tempDirectory.toString(), FIFO_NAME)).waitFor();
            new FileObjectPutter(tempDirectory).buildChannel(FIFO_NAME);
        } catch (final UnrecoverableIOException e) {
            assertTrue(e.getMessage().contains(FIFO_NAME));
            caughtException.set(true);
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }

        assertTrue(caughtException.get());
    }
}
