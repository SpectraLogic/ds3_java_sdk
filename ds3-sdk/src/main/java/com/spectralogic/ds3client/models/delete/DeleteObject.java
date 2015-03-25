package com.spectralogic.ds3client.models.delete;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteObject {

    @JsonProperty("Key")
    private String key;

    public DeleteObject(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }
}
