package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonPrefixes {
    @JsonProperty("Prefix")
    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
}
