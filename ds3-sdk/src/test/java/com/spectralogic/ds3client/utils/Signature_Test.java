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

package com.spectralogic.ds3client.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.models.common.Credentials;
import com.spectralogic.ds3client.models.common.SignatureDetails;
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

    @Test
    public void putSignatureWithMeta() throws SignatureException {
        final Credentials credentials = new Credentials("Yg==", "BYY65vje");

        final ImmutableMultimap.Builder<String, String> headersBuilder = ImmutableMultimap.builder();

        headersBuilder.put("x-amz-meta-etag", "7679088556832fcef734741c031e4020");
        headersBuilder.put("accept-encoding", "gzip,deflate");
        headersBuilder.put("connection", "Keep-Alive");
        headersBuilder.put("content-length", "10240012288");
        headersBuilder.put("job-chunk-lock-holder", "205ad7dd-3117-4412-9bbe-e05ae5b3fb99");

        final ImmutableMap<String, String> queryParams = ImmutableMap.of("job", "d92f0280-8c9f-40a5-b9d0-0567af375976", "offset", "0");

        final SignatureDetails details =
                new SignatureDetails(HttpVerb.PUT,
                        "dnkIhVaDL873NHQcAx5AIA==",
                        "application/xml; charset=ISO-8859-1",
                        "Thu, 31 Mar 2016 18:08:30 +0000",
                        Signature.canonicalizeAmzHeaders(new MultiMapImpl<>(headersBuilder.build())),
                        Signature.canonicalizeResource("/bucket10240012288/client00obj000019-000010000012", queryParams),
                        credentials);

        assertThat(Signature.signature(details), is("/FDHtkaFgjSVVgBfNfTAwrhdpCc="));
    }
}
