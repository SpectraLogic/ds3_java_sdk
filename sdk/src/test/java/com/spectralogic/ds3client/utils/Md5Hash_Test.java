package com.spectralogic.ds3client.utils;

import com.spectralogic.ds3client.utils.Md5Hash;
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
