package com.spectralogic.ds3client.networking;

import com.google.common.base.Preconditions;
import com.spectralogic.ds3client.commands.interfaces.Ds3Request;

import java.io.IOException;

public class NetworkClientRetryDecorator extends NetworkClientDecorator {
    private final NetworkClientRetryBehavior networkClientRetryBehavior;

    private NetworkClientRetryDecorator(final NetworkClient networkClient,
                                        final NetworkClientRetryBehavior networkClientRetryBehavior) {
        super(networkClient);

        this.networkClientRetryBehavior = networkClientRetryBehavior;
    }

    @Override
    public WebResponse getResponse(final Ds3Request request) throws IOException {
        final WebResponse webResponse = super.getResponse(request);

        if (networkClientRetryBehavior.shouldRetry(webResponse))
            return getResponse(request);

        return webResponse;
    }

    public static class Builder {
        NetworkClient networkClient;
        NetworkClientRetryBehavior networkClientRetryBehavior;

        public Builder networkClient(final NetworkClient networkClient) {
            this.networkClient = networkClient;
            return this;
        }

        public Builder networkClientRetryBehavior(final NetworkClientRetryBehavior networkClientRetryBehavior) {
            this.networkClientRetryBehavior = networkClientRetryBehavior;
            return this;
        }

        public NetworkClientRetryDecorator build() {
            Preconditions.checkNotNull(networkClient);
            Preconditions.checkNotNull(networkClientRetryBehavior);

            return new NetworkClientRetryDecorator(networkClient, networkClientRetryBehavior);
        }
    }
}
