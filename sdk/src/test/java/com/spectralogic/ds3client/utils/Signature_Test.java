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

package com.spectralogic.ds3client.utils;

import com.spectralogic.ds3client.HttpVerb;
import com.spectralogic.ds3client.models.Credentials;
import com.spectralogic.ds3client.models.SignatureDetails;
import org.junit.Test;

import java.security.SignatureException;

import static org.hamcrest.CoreMatchers.is;
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
                new SignatureDetails(HttpVerb.GET, "", "", "Tue, 27 Mar 2007 19:36:42 +0000",
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
                new SignatureDetails(HttpVerb.PUT, "", "image/jpeg", "Tue, 27 Mar 2007 21:15:45 +0000",
                        "", "/johnsmith/photos/puppy.jpg", credentials);

        assertThat(Signature.signature(details), is("MyyxeRY7whkBe+bq8fHCL/2kKUg="));
    }
}
