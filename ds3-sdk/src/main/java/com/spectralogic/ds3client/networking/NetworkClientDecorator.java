package com.spectralogic.ds3client.networking;

import com.spectralogic.ds3client.commands.interfaces.Ds3Request;

import java.io.IOException;

public abstract class NetworkClientDecorator implements NetworkClient {
    private final NetworkClient networkClient;

    public NetworkClientDecorator(final NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    @Override
    public WebResponse getResponse(final Ds3Request request) throws IOException {
        return networkClient.getResponse(request);
    }

    @Override
    public ConnectionDetails getConnectionDetails() {
        return networkClient.getConnectionDetails();
    }

    /**
     * Closes this stream and releases any system resources associated
     * with it. If the stream is already closed then invoking this
     * method has no effect.
     * <p>
     * <p> As noted in {@link AutoCloseable#close()}, cases where the
     * close may fail require careful attention. It is strongly advised
     * to relinquish the underlying resources and to internally
     * <em>mark</em> the {@code Closeable} as closed, prior to throwing
     * the {@code IOException}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        networkClient.close();
    }
}
