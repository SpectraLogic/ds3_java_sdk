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

package com.spectralogic.ds3client.utils;

import com.spectralogic.ds3client.utils.hashing.Md5Hash;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class Md5Hash_Test {

    @Test
    public void equalStringHash() {
        final String base64String = "Q2hlY2sgSW50ZWdyaXR5IQ==";
        final Md5Hash hash = Md5Hash.fromBase64String(base64String);
        assertThat(hash.toBase64String(), is(base64String));
    }

    @Test
    public void equalHash() {
        final Md5Hash hash1 = Md5Hash.fromBase64String("Q2hlY2sgSW50ZWdyaXR5IQ==");
        final Md5Hash hash2 = Md5Hash.fromBase64String("Q2hlY2sgSW50ZWdyaXR5IQ==");
        assertTrue(hash1.equals(hash2));
    }

    @Test
    public void notEqualHash() {
        final Md5Hash hash1 = Md5Hash.fromBase64String("Q2hlY2sgSW50ZWdyaXR5IV==");
        final Md5Hash hash2 = Md5Hash.fromBase64String("Q2afs2sgSW50ZWdyaXR5IQ==");
        assertFalse(hash1.equals(hash2));
    }

    @Test
    public void notEqualDifferentObj() {
        final Md5Hash hash1 = Md5Hash.fromBase64String("Q2hlY2sgSW50ZWdyaXR5IV==");
        assertFalse(hash1.equals(new Object()));
    }
}
