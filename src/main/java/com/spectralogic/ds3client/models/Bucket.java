package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bucket {

    @JsonProperty("Name")
    private String name;
    @JsonProperty("CreationDate")
    private String creationDate;

    public Bucket() {}

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String toString() {
        return "name: " + name + " creationDate: " + creationDate;
    }
}
