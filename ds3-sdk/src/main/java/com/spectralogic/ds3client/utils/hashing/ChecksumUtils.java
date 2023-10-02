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

import com.spectralogic.ds3client.models.ChecksumType;

import java.io.IOException;
import java.io.InputStream;

public final class ChecksumUtils {
    private static final int READ_BUFFER_SIZE = 10 * 1024 * 1024;

    private ChecksumUtils() {}

    public static Hasher getHasher(final ChecksumType.Type checksumType) {
        switch (checksumType) {
            case MD5:
                return new MD5Hasher();
            case SHA_256:
                return new SHA256Hasher();
            case SHA_512:
                return new SHA512Hasher();
            case CRC_32:
                return new CRC32Hasher();
            case CRC_32C:
                return new CRC32CHasher();
            default:
                throw new RuntimeException("Unknown checksum type " + checksumType);
        }
    }

    public static String hashInputStream(final Hasher digest, final InputStream stream) throws IOException {
        final byte[] buffer = new byte[READ_BUFFER_SIZE];
        int bytesRead;

        while (true) {
            bytesRead = stream.read(buffer);

            if (bytesRead < 0) {
                break;
            }

            digest.update(buffer, 0, bytesRead);
        }

        return digest.digest();
    }
}
