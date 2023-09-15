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

package com.spectralogic.ds3client.utils.hashing;

import org.apache.commons.codec.binary.Base64;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.Checksum;

abstract class ChecksumHasher implements Hasher {

    private final Checksum checksum;

    public ChecksumHasher() {
        this.checksum = getChecksum();
    }

    protected abstract Checksum getChecksum();

    @Override
    public void update(final byte[] bytes, final int offset, final int length) {
        checksum.update(bytes, offset, length);
    }

    /**
     * This must return a string where the checksum is an int in network byte ordering that is base64 encoded
     */
    @Override
    public String digest() {
        return Base64.encodeBase64String(toBytes(checksum.getValue()));
    }

    private static byte[] toBytes(final long x) {
        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(x);

        final byte[] bytes = new byte[4];
        System.arraycopy(buffer.array(), 4, bytes, 0, 4);

        return bytes;
    }

}
