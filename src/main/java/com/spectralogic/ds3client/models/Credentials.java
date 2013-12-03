package com.spectralogic.ds3client.models;


public class Credentials {

    private final String clientId;
    private final String key;

    public Credentials(final String clientId, final String key) {
        this.clientId = clientId;
        this.key = key;
    }

    public String getClientId() {
        return clientId;
    }

    public String getKey() {
        return key;
    }

    public boolean isValid() {
        return !(clientId == null || clientId.isEmpty() || key == null || key.isEmpty());
    }
}
