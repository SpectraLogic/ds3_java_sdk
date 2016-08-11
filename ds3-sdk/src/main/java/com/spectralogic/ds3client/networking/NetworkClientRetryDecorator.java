/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

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
        WebResponse webResponse;

        do {
            webResponse = super.getResponse(request);
        } while (networkClientRetryBehavior.shouldRetry(webResponse));

        return webResponse;
    }

    public static class Builder {
        private NetworkClient networkClient;
        private NetworkClientRetryBehavior networkClientRetryBehavior;

        public Builder networkClient(final NetworkClient networkClient) {
            this.networkClient = networkClient;
            return this;
        }

        public Builder networkClientRetryBehavior(final NetworkClientRetryBehavior networkClientRetryBehavior) {
            this.networkClientRetryBehavior = networkClientRetryBehavior;
            return this;
        }

        public NetworkClientRetryDecorator build() throws NullPointerException {
            Preconditions.checkNotNull(networkClient);
            Preconditions.checkNotNull(networkClientRetryBehavior);

            return new NetworkClientRetryDecorator(networkClient, networkClientRetryBehavior);
        }
    }
}
