package com.spectralogic.ds3client.utils;

import com.spectralogic.ds3client.models.SignatureDetails;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SignatureException;

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
    public static String calculateRFC2104HMAC(String data, String key)
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
        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result.trim();
    }

    /**
     * Auth signature as described by AWS
     * @see <a href="http://docs.aws.amazon.com/AmazonS3/latest/dev/RESTAuthentication.html#ConstructingTheAuthenticationHeader">AWS Authentication Header</a>
     * @param signatureDetails
     * @return
     */
    public static String signature(final SignatureDetails signatureDetails)
            throws SignatureException {
        final StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(signatureDetails.getVerb()).append('\n');
        stringToSign.append(signatureDetails.getContentMd5()).append('\n');
        stringToSign.append(signatureDetails.getContentType()).append('\n');
        stringToSign.append(signatureDetails.getDate()).append('\n');
        stringToSign.append(signatureDetails.getCanonicalizedAmzHeaders());
        stringToSign.append(signatureDetails.getCanonicalizedResource());
        System.out.println(stringToSign.toString());
        return calculateRFC2104HMAC(stringToSign.toString(), signatureDetails.getCredentials().getKey());
    }



}
