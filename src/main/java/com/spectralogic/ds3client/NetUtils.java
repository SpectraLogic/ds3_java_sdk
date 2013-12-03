package com.spectralogic.ds3client;

import com.spectralogic.ds3client.models.ConnectionDetails;
import com.spectralogic.ds3client.models.SignatureDetails;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NetUtils {
    final static private String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    final static private String RFC822FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";

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
        return result;
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

    public static URL buildUrl(final String path, final ConnectionDetails connectionDetails) throws MalformedURLException {
        return buildUrl(path, connectionDetails, null);
    }

    public static URL buildUrl(final String path, final ConnectionDetails connectionDetails, final Map<String, String> params) throws MalformedURLException {
        final StringBuilder builder = new StringBuilder();
        builder.append(connectionDetails.isSecure()? "https": "http").append("://");
        builder.append(connectionDetails.getEndpoint()).append(':');
        builder.append(connectionDetails.getPort());
        builder.append(path);

        if (params != null && params.size() > 0) {
            builder.append('?');

            final Set<Map.Entry<String, String>> paramSet = params.entrySet();
            final Iterator<Map.Entry<String, String>> paramIterator = paramSet.iterator();
            Map.Entry<String, String> paramEntry = paramIterator.next();
            addQueryParam(builder, paramEntry);
            while(paramIterator.hasNext()) {
                paramEntry = paramIterator.next();
                builder.append('&');
                addQueryParam(builder, paramEntry);
            }
        }

        return new URL(builder.toString());
    }

    public static void addQueryParam(StringBuilder builder, Map.Entry<String, String> entry) {
        builder.append(entry.getKey());
        if (entry.getValue() != null) {
            builder.append('=');
            builder.append(entry.getValue());
        }
    }

    public static String buildPath(final String basePath, final String path) {
        //Make sure to have some guard statements for null.
        if(basePath == null && path == null) {
            return "";
        }
        if (basePath == null) {
            return path;
        }
        if (path == null) {
            return basePath;
        }

        final StringBuilder builder = new StringBuilder();
        if(!basePath.startsWith("/")) {
            builder.append('/');
        }
        builder.append(basePath);

        if(!(path.startsWith("/") || basePath.endsWith("/"))) {
            builder.append('/');
        }

        if(path.startsWith("/") && basePath.endsWith("/")) {
            builder.append(path.substring(1));
        }
        else {
            builder.append(path);
        }

        return builder.toString();
    }

    /**
     * Returns a RFC-882 formatted string with the current time.
     * @return
     */
    public static String dateToRfc882() {
        return dateToRfc882(new Date());
    }

    public static String dateToRfc882(final Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat(RFC822FORMAT);
        return sdf.format(date);
    }
}
