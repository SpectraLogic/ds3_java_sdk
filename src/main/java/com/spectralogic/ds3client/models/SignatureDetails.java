package com.spectralogic.ds3client.models;

import com.spectralogic.ds3client.HttpVerb;

public class SignatureDetails {
    final private HttpVerb verb;
    final private String contentMd5;
    final private String contentType;
    final private String date;
    final private String canonicalizedAmzHeaders;
    final private String canonicalizedResource;
    final private Credentials userCredentials;

    public SignatureDetails(final HttpVerb verb, final String contentMd5,
                            final String contentType, final String date,
                            final String canonicalizedAmzHeaders,
                            final String canonicalizedResource,
                            final Credentials userCredentials) {
        this.verb = verb;
        this.contentMd5 = contentMd5;
        this.contentType = contentType;
        this.date = date;
        this.canonicalizedAmzHeaders = canonicalizedAmzHeaders;
        this.canonicalizedResource = canonicalizedResource;
        this.userCredentials = userCredentials;
    }

    public HttpVerb getVerb() {
        return verb;
    }

    public String getContentMd5() {
        return contentMd5;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDate() {
        return date;
    }

    public String getCanonicalizedAmzHeaders() {
        return canonicalizedAmzHeaders;
    }

    public String getCanonicalizedResource() {
        return canonicalizedResource;
    }

    public Credentials getCredentials() {
        return userCredentials;
    }
}
