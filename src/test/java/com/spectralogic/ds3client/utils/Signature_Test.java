package com.spectralogic.ds3client.utils;

import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.models.SignatureDetails;
import org.junit.Test;

import java.security.SignatureException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class Signature_Test {

    /**
     * Example taken from the AWS S3 documentation site: http://docs.aws.amazon.com/AmazonS3/latest/dev/RESTAuthentication.html#ConstructingTheAuthenticationHeader
     * @throws SignatureException
     */
    @Test
    public void getSignature() throws SignatureException {
        final Credentials credentials = new Credentials("AKIAIOSFODNN7EXAMPLE",
                "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
        final SignatureDetails details =
                new SignatureDetails("GET", "", "", "Tue, 27 Mar 2007 19:36:42 +0000",
                        "", "/johnsmith/photos/puppy.jpg", credentials);

        assertThat(Signature.signature(details), is("bWq2s1WEIj+Ydj0vQ697zp+IXMU="));
    }

    /**
     * Example taken from the AWS S3 documentation site: http://docs.aws.amazon.com/AmazonS3/latest/dev/RESTAuthentication.html#ConstructingTheAuthenticationHeader
     * @throws SignatureException
     */
    @Test
    public void putSignature() throws SignatureException {
        final Credentials credentials = new Credentials("AKIAIOSFODNN7EXAMPLE",
                "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
        final SignatureDetails details =
                new SignatureDetails("PUT", "", "image/jpeg", "Tue, 27 Mar 2007 21:15:45 +0000",
                        "", "/johnsmith/photos/puppy.jpg", credentials);

        assertThat(Signature.signature(details), is("MyyxeRY7whkBe+bq8fHCL/2kKUg="));
    }
}
