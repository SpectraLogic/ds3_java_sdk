package com.spectralogic.ds3client.fixtures;

import com.spectralogic.ds3client.models.ConnectionDetails;
import com.spectralogic.ds3client.models.Credentials;

public class ConnectionFixture {

    public static ConnectionDetails getConnection() {
        return getConnection(8080);
    }

    public static ConnectionDetails getConnection(final int port) {
        return new ConnectionDetails("localhost", new Credentials("id", "key"), port, false);
    }
}
