package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Owner {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("DisplayName")
    private String displayName;

    public Owner() {}

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String toString() {
        return "id: " + id + " displayName: " + displayName;
    }
}
