package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {
    @JsonProperty("Code")
    private String code;
    
    @JsonProperty("Message")
    private String message;
    
    @JsonProperty("Resource")
    private String resource;
    
    @JsonProperty("RequestId")
    private String requestId;
    
    public String getCode() {
        return code;
    }
    public void setCode(final String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(final String message) {
        this.message = message;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(final String resource) {
        this.resource = resource;
    }
    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }
}
