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

package com.spectralogic.ds3client.helpers.channelbuilders;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ObjectInputStreamBuilder_Test {

    private static final String content = "some text";
    private static final byte[] contentBytes = content.getBytes();

    @Test
    public void testConversion() throws IOException {
        final StreamObjectPutter putter = new StreamObjectPutter(contentBytes);
        final InputStream result = putter.buildInputStream("key");
        assertThat(result.available(), is(contentBytes.length));
        for (final byte contentByte : contentBytes) {
            assertThat(result.read(), is((int) contentByte));
        }
    }
}
