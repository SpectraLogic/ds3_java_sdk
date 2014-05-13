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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class ResettableFileInputStream_Test {
    @Test
    public void testReset() throws IOException {
        final byte[] expectedBytes = Files.readAllBytes(Paths.get(this.resource("LoremIpsumTwice.testdata")));
        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(expectedBytes.length)) {
            try (final InputStream inputStream = new ResettableFileInputStream(new FileInputStream(this.resource("LoremIpsum.testdata")))) {
                IOUtils.copy(inputStream, byteArrayOutputStream);
                inputStream.reset();
                IOUtils.copy(inputStream, byteArrayOutputStream);
            }
            Assert.assertArrayEquals(expectedBytes, byteArrayOutputStream.toByteArray());
        }
    }

    private String resource(final String name) {
        return this.getClass().getResource(name).getFile();
    }
}
