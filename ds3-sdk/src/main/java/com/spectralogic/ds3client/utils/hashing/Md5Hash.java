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

package com.spectralogic.ds3client.utils.hashing;

import com.spectralogic.ds3client.utils.InvalidMd5Exception;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;
import java.util.Objects;

public class Md5Hash {
    private final byte[] hash;

    public static Md5Hash fromHexString(final String hex) throws InvalidMd5Exception {
        try {
            return new Md5Hash(Hex.decodeHex(hex.toCharArray()));
        } catch (final DecoderException e) {
            throw new InvalidMd5Exception(e);
        }
    }

    public static Md5Hash fromBase64String(final String base64) {
        return new Md5Hash(Base64.decodeBase64(base64));
    }

    public static Md5Hash fromByteArray(final byte[] bytes) {
        return new Md5Hash(bytes);
    }

    private Md5Hash(final byte[] hash) {
        this.hash = hash;
    }

    public byte[] getHash() {
        return Arrays.copyOfRange(this.hash, 0, this.hash.length);
    }

    public String toHexString() {
        return Hex.encodeHexString(this.hash);
    }

    public String toBase64String() {
        return Base64.encodeBase64String(this.hash);
    }

    @Override
    public String toString() {
        return this.toBase64String();
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Md5Hash)) {
            return false;
        }
        final Md5Hash otherHash = (Md5Hash) obj;
        return Arrays.equals(this.hash, otherHash.getHash());
    }
}
