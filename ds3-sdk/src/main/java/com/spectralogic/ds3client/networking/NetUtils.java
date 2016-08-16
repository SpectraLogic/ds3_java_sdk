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

package com.spectralogic.ds3client.networking;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterators;
import com.google.common.escape.Escaper;
import com.spectralogic.ds3client.BulkCommand;
import com.spectralogic.ds3client.utils.Guard;
import com.spectralogic.ds3client.utils.SafeStringManipulation;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class NetUtils {

    private NetUtils() {
        throw new IllegalStateException("This component should never be initialized.");
    }

    public static URL buildUrl(final ConnectionDetails connectionDetails, final String path) throws MalformedURLException {
        return buildUrl(connectionDetails, path, null);
    }

    public static URL buildUrl(final ConnectionDetails connectionDetails, final String path, final Map<String, String> params) throws MalformedURLException {
        final StringBuilder builder = new StringBuilder();
        if (!connectionDetails.getEndpoint().startsWith("http")) {
            builder.append(connectionDetails.isHttps() ? "https" : "http").append("://");
        }
        builder.append(connectionDetails.getEndpoint());
        if(!path.startsWith("/")) {
            builder.append('/');
        }

        final Escaper urlEscaper = SafeStringManipulation.getDs3Escaper();

        builder.append(urlEscaper.escape(path));

        if(params != null && params.size() > 0) {
            builder.append('?');
            builder.append(urlEscaper.escape(buildQueryString(params)));
        }
        return new URL(builder.toString());
    }

    public static String buildQueryString(final Map<String, String> queryParams) {
        final SortedMap<String, String> sortedMap = new TreeMap<>(queryParams);
        final Iterator<String> stringIter = Iterators.transform(sortedMap.entrySet().iterator(), new Function<Map.Entry<String, String>, String>() {
            @Override
            public String apply(@Nonnull final Map.Entry<String, String> input) {
                if (Guard.isStringNullOrEmpty(input.getValue())) {
                    return input.getKey();
                }
                return input.getKey() + "=" + input.getValue();
            }
        });

        final Joiner join = Joiner.on('&');
        return join.join(stringIter);
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
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", command.toString());
        return NetUtils.buildUrl(connectionDetails, bucketPath(bucketName), queryParams);
    }

    private static String bucketPath(final String bucket) {
        return "/_rest_/buckets/" + bucket;
    }

    public static String buildHostField(final ConnectionDetails details) {
        return filterHttp(details.getEndpoint());
    }

    public static String filterHttp(final String url) {
        if (url.startsWith("http://") || url.startsWith("https://"))
        {
            final int colonIndex = url.indexOf(":");
            return url.substring(colonIndex + 3);
        }
        return url;
    }

    public static int getPort(final URL url) {
        final int port = url.getPort();
        if(port > 0) {
            return port;
        }

        if (url.getProtocol().equals("https")) {
            return 443;
        }
        return 80;
    }

}
