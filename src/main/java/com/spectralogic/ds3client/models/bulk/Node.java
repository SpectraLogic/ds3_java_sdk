package com.spectralogic.ds3client.models.bulk;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Node {

    @JsonProperty("EndPoint")
    private String endpoint;

    @JsonProperty("HttpPort")
    private int httpPort;

    @JsonProperty("HttpsPort")
    private int httpsPort;

    @JsonProperty("Id")
    private UUID id;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(final int httpPort) {
        this.httpPort = httpPort;
    }

    public int getHttpsPort() {
        return httpsPort;
    }

    public void setHttpsPort(final int httpsPort) {
        this.httpsPort = httpsPort;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }
}
