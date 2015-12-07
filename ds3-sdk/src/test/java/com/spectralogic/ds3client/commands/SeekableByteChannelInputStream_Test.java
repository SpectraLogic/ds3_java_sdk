/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.commands;

import com.spectralogic.ds3client.utils.ResourceUtils;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SeekableByteChannelInputStream_Test {
    @Test
    public void markSupported() throws IOException, URISyntaxException {
        try (final InputStream inputStream = buildSystemUnderTest()) {
            assertThat(inputStream.markSupported(), is(true));
        }
    }

    @Test
    public void readAllByByteWorks() throws IOException, URISyntaxException {
        try (final InputStream inputStream = buildSystemUnderTest()) {
            int b;
            final byte[] expectedBytes = getFileContents();
            final byte[] buffer = new byte[expectedBytes.length];
            for (int i = 0; -1 != (b = inputStream.read()); i++)
            {
                buffer[i] = (byte)b;
            }
            assertThat(buffer, equalTo(expectedBytes));
        }
    }
    
    @Test
    public void readAllByBuffer() throws IOException, URISyntaxException {
        try (final InputStream inputStream = buildSystemUnderTest()) {
            readAndVerify(inputStream, getFileContents());
        }
    }
     
    @Test
    public void resetAllowsDoubleRead() throws IOException, URISyntaxException {
        try (final InputStream inputStream = buildSystemUnderTest()) {
            final byte[] expectedBytes = getFileContents();
            readAndVerify(inputStream, expectedBytes);
            inputStream.reset();
            readAndVerify(inputStream, expectedBytes);
        }
    }
     
    @Test
    public void resetToMark() throws IOException, URISyntaxException {
        try (final InputStream inputStream = buildSystemUnderTest()) {
            final byte[] expectedBytes = getFileContents();
            final byte[] expectedSlice = Arrays.copyOfRange(expectedBytes, 50, expectedBytes.length);
            inputStream.skip(50);
            inputStream.mark(0);
            readAndVerify(inputStream, expectedSlice);
            inputStream.reset();
            readAndVerify(inputStream, expectedSlice);
        }
    }
    
    @Test
    public void closeCallsClose() throws IOException {
        final SeekableByteChannel mockChannel = Mockito.mock(SeekableByteChannel.class);
        new SeekableByteChannelInputStream(mockChannel).close();
        Mockito.verify(mockChannel).close();
        Mockito.verifyNoMoreInteractions(mockChannel);
    }

    private void readAndVerify(final InputStream inputStream, final byte[] expectedBytes)
            throws IOException, URISyntaxException {
        final byte[] resultBuffer = new byte[expectedBytes.length];
        final byte[] buffer = new byte[10];
        int position = 0;
        int bytesRead = 0;
        while (0 < (bytesRead = inputStream.read(buffer, 5, 5))) {
            for (int i = 0; i < bytesRead; i++) {
                resultBuffer[position] = buffer[5 + i];
                position++;
            }
        }
        assertThat(resultBuffer, equalTo(expectedBytes));
    }
    
    private static byte[] getFileContents() throws IOException, URISyntaxException {
        return Files.readAllBytes(ResourceUtils.loadFileResource("LoremIpsumTwice.txt").toPath());
    }
    
    private static InputStream buildSystemUnderTest() throws IOException, URISyntaxException {
        final Path path = ResourceUtils.loadFileResource("LoremIpsumTwice.txt").toPath();
        return new SeekableByteChannelInputStream(FileChannel.open(path, StandardOpenOption.READ));
    }
}
