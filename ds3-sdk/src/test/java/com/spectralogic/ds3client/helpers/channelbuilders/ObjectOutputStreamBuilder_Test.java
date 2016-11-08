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

package com.spectralogic.ds3client.helpers.channelbuilders;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ObjectOutputStreamBuilder_Test {

    private final String content = "some text";
    private final byte[] contentBytes = content.getBytes();

    @Test
    public void testConversion() throws IOException {
        final StreamObjectGetter getter = new StreamObjectGetter();
        final OutputStream stream = getter.buildOutputStream("key");
        stream.write(contentBytes);
        final byte[] result = getter.getBytes();
        assertThat(result.length, is(contentBytes.length));
        assertThat(result, is(contentBytes));
    }
}
