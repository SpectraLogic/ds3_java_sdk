package com.spectralogic.ds3client.models;

public class SignatureDetails {
    final private String verb;
    final private String contentMd5;
    final private String contentType;
    final private String date;
    final private String canonicalizedAmzHeaders;
    final private String canonicalizedResource;
    final private Credentials userCredentials;

    public SignatureDetails(String verb, String contentMd5,
                            String contentType, String date,
                            String canonicalizedAmzHeaders,
                            String canonicalizedResource,
                            Credentials userCredentials) {
        this.verb = verb;
        this.contentMd5 = contentMd5;
        this.contentType = contentType;
        this.date = date;
        this.canonicalizedAmzHeaders = canonicalizedAmzHeaders;
        this.canonicalizedResource = canonicalizedResource;
        this.userCredentials = userCredentials;
    }

    public String getVerb() {
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
