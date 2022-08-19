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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FileObjectGetter_Test {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testThatNamedPipeThrows() throws IOException, InterruptedException {
        Assume.assumeFalse(Platform.isWindows());

        final String tempPathPrefix = null;
        final Path tempDirectory = Files.createTempDirectory(Paths.get("."), tempPathPrefix);
        tempDirectory.toFile().deleteOnExit();
        final String FIFO_NAME = "bFifo";

        final AtomicBoolean caughtException = new AtomicBoolean(false);

        try {
            Runtime.getRuntime().exec("mkfifo " + Paths.get(tempDirectory.toString(), FIFO_NAME)).waitFor();
            new FileObjectGetter(tempDirectory).buildChannel(FIFO_NAME);
        } catch (final UnrecoverableIOException e) {
            assertTrue(e.getMessage().contains(FIFO_NAME));
            caughtException.set(true);
        }

        assertTrue(caughtException.get());
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
            new FileObjectGetter(tempDirectory).buildChannel(A_FILE_NAME);
        } catch (final UnrecoverableIOException e) {
            assertTrue(e.getMessage().contains(A_FILE_NAME));
            caughtException.set(true);
        } finally {
            FileUtils.deleteDirectory(tempDirectory.toFile());
        }

        assertFalse(caughtException.get());
    }

    @Test
    public void testThatSymbolicLinksAreResolved() {
        Assume.assumeFalse(Platform.isWindows());
        final String message = "Hello World";
        final String file = "file.txt";
        try {
            final Path tempDirectory = Files.createTempDirectory(Paths.get(System.getProperty("java.io.tmpdir")), "ds3");
            final Path realDirectory = Files.createDirectory(Paths.get(tempDirectory.toString(), "dir"));
            final Path symbolicDirectory = Paths.get(tempDirectory.toString(), "symbolic");
            Files.createSymbolicLink(symbolicDirectory, realDirectory);
            Files.createFile(Paths.get(realDirectory.toString(), file));
            final ByteBuffer bb = ByteBuffer.wrap(message.getBytes());

            final SeekableByteChannel getterChannel = new FileObjectGetter(symbolicDirectory).buildChannel(file);
            getterChannel.write(bb);
            getterChannel.close();
            final String content = new String(Files.readAllBytes(Paths.get(realDirectory.toString(), file)));
            assertTrue(message.equals(content));
        } catch (final IOException e) {
            fail("Symbolic links are not handled correctly");
        }
    }
}
