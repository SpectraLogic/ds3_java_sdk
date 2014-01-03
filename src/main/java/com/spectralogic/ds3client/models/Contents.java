package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contents {

    @JsonProperty("Key")
    private String key;
    @JsonProperty("LastModified")
    private String lastModified;
    @JsonProperty("ETag")
    private String eTag;
    @JsonProperty("Size")
    private int size;
    @JsonProperty("StorageClass")
    private String storageClass;
    @JsonProperty("Owner")
    private Owner owner;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(String storageClass) {
        this.storageClass = storageClass;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("{{key:: ").append(key).append("}, ");
        builder.append("{size:: ").append(size).append("}, ");
        builder.append("{owner:: ").append(owner).append("}}");
        return builder.toString();
    }
}
