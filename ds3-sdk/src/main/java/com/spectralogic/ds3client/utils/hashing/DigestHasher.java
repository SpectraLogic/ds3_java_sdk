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

import java.security.MessageDigest;

abstract class DigestHasher implements Hasher {

    private final MessageDigest digest;

    DigestHasher() {
        this.digest = getDigest();
    }

    abstract MessageDigest getDigest();

    @Override
    public void update(final byte[] bytes, final int offset, final int length) {
        digest.update(bytes, offset, length);
    }


    @Override
    public String digest() {
        return Base64.encodeBase64String(digest.digest());
    }
}
