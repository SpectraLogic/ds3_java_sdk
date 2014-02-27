package com.spectralogic.ds3client.networking;

import com.spectralogic.ds3client.BulkCommand;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class NetUtils {

    public static URL buildUrl(final String path, final ConnectionDetails connectionDetails) throws MalformedURLException {
        return buildUrl(path, connectionDetails, null);
    }

    public static URL buildUrl(final String path, final ConnectionDetails connectionDetails, final Map<String, String> params) throws MalformedURLException {
        final StringBuilder builder = new StringBuilder();
        builder.append(connectionDetails.isSecure()? "https": "http").append("://");
        builder.append(connectionDetails.getEndpoint());
        if(!path.startsWith("/")) {
            builder.append('/');
        }

        builder.append(path);

        if(params != null && params.size() > 0) {
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

    private static void addQueryParam(StringBuilder builder, Map.Entry<String, String> entry) {
        builder.append(entry.getKey());
        if(entry.getValue() != null) {
            builder.append('=');
            builder.append(entry.getValue());
        }
    }

    public static String buildPath(final String basePath, final String path) {
        //Make sure to have some guard statements for null.
        if(basePath == null && path == null) {
            return "";
        }

        if(basePath == null) {
            return path;
        }

        if(path == null) {
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
        else{
            builder.append(path);
        }

        return builder.toString();
    }

    public static URL buildBucketPath(final String bucketName, final ConnectionDetails connectionDetails, final BulkCommand command) throws MalformedURLException {
        final Map<String, String> queryParams = new HashMap<String,String>();
        queryParams.put("operation", command.toString());
        return NetUtils.buildUrl(bucketPath(bucketName), connectionDetails, queryParams);
    }

    private static String bucketPath(final String bucket) {
        final StringBuilder builder = new StringBuilder();
        builder.append("/_rest_/buckets/").append(bucket);
        return builder.toString();
    }

    public static String buildHostField(final ConnectionDetails details) {
        return details.getEndpoint();
    }
}
