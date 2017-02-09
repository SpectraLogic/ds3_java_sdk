/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

import com.google.common.base.Joiner;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.models.common.SignatureDetails;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.security.SignatureException;
import java.util.Collection;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class Signature {
    private final static Logger LOG = LoggerFactory.getLogger(Signature.class);
    private final static String HMAC_SHA1_ALGORITHM = "HmacSHA1";

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
            throws SignatureException
    {
        LOG.debug("String to sign: {}", data.replace("\n", "\\n"));
        final String result;
        try {
            // get an hmac_sha1 key from the raw key bytes
            final SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")), HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            final Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            final byte[] rawHmac = mac.doFinal(data.getBytes(Charset.forName("UTF-8")));
            result = Base64.encodeBase64String(rawHmac);
        } catch (final Exception e) {
            throw new SignatureException("Failed to generate HMAC: " + e.getMessage(), e);
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

    public static String canonicalizeResource(final String path, final Map<String, String> queryParams) {
        final StringBuilder canonicalizedResource = new StringBuilder();
        // path is escaped
        canonicalizedResource.append(path);

        if (queryParams != null) {
            if (queryParams.containsKey("delete")) {
                canonicalizedResource.append("?delete");
            }
            if (queryParams.containsKey("versioning")) {
                canonicalizedResource.append("?versioning=").append(queryParams.get("versioning"));
            }
            if (queryParams.containsKey("uploads")) {
                canonicalizedResource.append("?uploads");
            }
        }
        return canonicalizedResource.toString();
    }

    public static String canonicalizeAmzHeaders(
            final MultiMap<String, String> customHeaders) {
        final StringBuilder ret = new StringBuilder();
        for (final Map.Entry<String, Collection<String>> header : customHeaders.entrySet()) {
            final String key = header.getKey().toLowerCase();
            if (key.startsWith(PutObjectRequest.AMZ_META_HEADER)
                    && header.getValue().size() > 0) {
                ret.append(key).append(":");
                ret.append(Joiner.on(",").join(header.getValue()));
                ret.append('\n');
            }
        }
        return ret.toString();
    }
}
