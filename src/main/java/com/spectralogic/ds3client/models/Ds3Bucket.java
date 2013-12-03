package com.spectralogic.ds3client.models;


public class Ds3Bucket {

    private final String bucketName;
    public Ds3Bucket(final String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
