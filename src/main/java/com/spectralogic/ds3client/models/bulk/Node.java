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
}
