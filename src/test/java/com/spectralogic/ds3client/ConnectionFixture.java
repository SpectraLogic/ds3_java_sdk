package com.spectralogic.ds3client;

import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.models.Credentials;

public class ConnectionFixture {

    public static ConnectionDetails getConnection() {
        return getConnection(8080);
    }

    public static ConnectionDetails getConnection(final int port) {
        return ConnectionDetailsImpl.builder("localhost:" + port, new Credentials("id", "key")).build();
    }
}
