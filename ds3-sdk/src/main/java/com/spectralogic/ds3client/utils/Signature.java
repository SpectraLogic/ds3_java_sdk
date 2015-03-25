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

import com.spectralogic.ds3client.models.SignatureDetails;

import org.apache.commons.codec.binary.Base64;

import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Signature {

    final static private String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
     * Computes RFC 2104-compliant HMAC signature.
     * * @param data
     * The data to be signed.
     * @param key
     * The signing key.
     * @return
     * The Base64-encoded RFC 2104-compliant HMAC signature.
     * @throws
     * java.security.SignatureException when signature generation fails
     */
    public static String calculateRFC2104HMAC(final String data, final String key)
            throws java.security.SignatureException
    {
        final String result;
        try {
            // get an hmac_sha1 key from the raw key bytes
            final SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            final Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            final byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64.encodeBase64String(rawHmac);
        } catch (final Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result.trim();
    }

    /**
     * Auth signature as described by AWS
     * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/RESTAuthentication.html#ConstructingTheAuthenticationHeader">AWS Authentication Header</a>
     */
    public static String signature(final SignatureDetails signatureDetails)
            throws SignatureException {
        return calculateRFC2104HMAC(
                String.valueOf(signatureDetails.getVerb()) + '\n'
                    + signatureDetails.getContentMd5() + '\n'
                    + signatureDetails.getContentType() + '\n'
                    + signatureDetails.getDate() + '\n'
                    + signatureDetails.getCanonicalizedAmzHeaders()
                    + signatureDetails.getCanonicalizedResource(),
                signatureDetails.getCredentials().getKey()
        );
    }
}
