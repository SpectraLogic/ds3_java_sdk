package com.spectralogic.ds3client.helpers;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class FileObjectPutter_Test {

    final private static String testString = "This is some test data.";
    final private static byte[] testData = testString.getBytes(Charset.forName("UTF-8"));

    /**
     * This test cannot run on Windows without extra privileges
     */
    @Test
    public void testSymlink() throws IOException {
        assumeThat(System.getProperty("os.name"), not(containsString("Windows")));
        final Path tempDir = Files.createTempDirectory("ds3_file_object_putter_");
        final Path tempPath = Files.createTempFile(tempDir, "temp_", ".txt");

        try {
            try (final SeekableByteChannel channel = Files.newByteChannel(tempPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
                channel.write(ByteBuffer.wrap(testData));
            }

            final Path symLinkPath = tempDir.resolve("sym_" + tempPath.getFileName().toString());
            Files.createSymbolicLink(symLinkPath, tempPath);

            try {
                final FileObjectPutter putter = new FileObjectPutter(tempDir);

                final SeekableByteChannel newChannel = putter.buildChannel(symLinkPath.getFileName().toString());
                assertThat(newChannel, is(notNullValue()));

                final ByteBuffer buff = ByteBuffer.allocate(testData.length);
                assertThat(newChannel.read(buff), is(testData.length));

                assertThat(new String(buff.array(), Charset.forName("UTF-8")), is(testString));
            } finally {
                Files.deleteIfExists(symLinkPath);
            }
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
            final FileObjectPutter putter = new FileObjectPutter(tempDir);

             try (final SeekableByteChannel newChannel = putter.buildChannel(tempPath.getFileName().toString())) {
                 assertThat(newChannel, is(notNullValue()));

                 final ByteBuffer buff = ByteBuffer.allocate(testData.length);
                 assertThat(newChannel.read(buff), is(testData.length));

                 assertThat(new String(buff.array(), Charset.forName("UTF-8")), is(testString));
             }
        } finally {
            Files.deleteIfExists(tempPath);
            Files.deleteIfExists(tempDir);
        }
    }
}
