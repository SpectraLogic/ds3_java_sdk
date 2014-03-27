package com.spectralogic.ds3client.fixtures;

import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.models.Credentials;

public class ConnectionFixture {

    public static ConnectionDetails getConnection() {
        return getConnection(8080);
    }

    public static ConnectionDetails getConnection(final int port) {
        return ConnectionDetails.builder("localhost:" + port, new Credentials("id", "key")).build();
    }
}
