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

import com.spectralogic.ds3client.commands.PutBucketRequest;
import com.spectralogic.ds3client.commands.interfaces.Ds3Request;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NetworkClientRetryDecorator_Test {
    @Test
    public void testNetworkClientDecoratorWithFailingMock() throws IOException {
        final int webResponseStatusCode = 999;

        final WebResponse mockWebResponse = mock(WebResponseImpl.class);
        when(mockWebResponse.getStatusCode()).thenReturn(webResponseStatusCode);

        final NetworkClient mockNetworkClient = mock(NetworkClientImpl.class);
        when(mockNetworkClient.getResponse(Mockito.any(Ds3Request.class))).thenReturn(mockWebResponse);

        final NetworkClientRetryDecorator networkClientRetryDecorator = new NetworkClientRetryDecorator.Builder()
                .networkClient(mockNetworkClient)
                .networkClientRetryBehavior(new NetworkClientRetryBehavior() {
                    private int numRetries = 0;

                    @Override
                    public boolean shouldRetry(final WebResponse webResponse) {
                        if(++numRetries <= 3) {
                            return true;
                        }

                        assertEquals(4, numRetries);
                        return false;
                    }
                })
                .build();

        final WebResponse webResponse = networkClientRetryDecorator.getResponse(new PutBucketRequest("Gracie"));

        assertEquals(webResponseStatusCode, webResponse.getStatusCode());
    }

    @Test
    public void testNetworkClientDecoratorWithPassingMock() throws IOException {
        final int webResponseStatusCode = 200;

        final WebResponse mockWebResponse = mock(WebResponseImpl.class);
        when(mockWebResponse.getStatusCode()).thenReturn(webResponseStatusCode);

        final NetworkClient mockNetworkClient = mock(NetworkClientImpl.class);
        when(mockNetworkClient.getResponse(Mockito.any(Ds3Request.class))).thenReturn(mockWebResponse);

        final RetryCounter retryCounter = new RetryCounter();

        final NetworkClientRetryDecorator networkClientRetryDecorator = new NetworkClientRetryDecorator.Builder()
                .networkClient(mockNetworkClient)
                .networkClientRetryBehavior(new NetworkClientRetryBehavior() {
                    @Override
                    public boolean shouldRetry(final WebResponse webResponse) {
                        retryCounter.increment();

                        if(webResponse.getStatusCode() >= 200 && webResponse.getStatusCode() < 300) {
                            return false;
                        }

                        fail("Getting here means that the network client decorator callback returned an incorrect value.");
                        return true;
                    }
                })
                .build();

        final WebResponse webResponse = networkClientRetryDecorator.getResponse(new PutBucketRequest("Gracie"));

        assertEquals(webResponseStatusCode, webResponse.getStatusCode());

        assertEquals(1, retryCounter.getRetryCount());
    }

    private static class RetryCounter {
        private int retryCount = 0;

        int increment() {
            return ++retryCount;
        }

        int getRetryCount() {
            return retryCount;
        }
    }

    @Test(expected = NullPointerException.class)
    public void testBuildingNetworkClientRetryDecoratorWithInvalidNetworkClient() throws IOException {
        new NetworkClientRetryDecorator.Builder()
                .networkClient(null)
                .networkClientRetryBehavior(new NetworkClientRetryBehavior() {
                    @Override
                    public boolean shouldRetry(final WebResponse webResponse) {
                        return false;
                    }
                })
                .build();
    }

    @Test(expected = NullPointerException.class)
    public void testBuildingNetworkClientRetryDecoratorWithInvalidCallback() throws IOException {
        final NetworkClient mockNetworkClient = mock(NetworkClientImpl.class);

        new NetworkClientRetryDecorator.Builder()
                .networkClient(mockNetworkClient)
                .networkClientRetryBehavior(null)
                .build();
    }

    @Test
    public void coverTheRemainingNetworkInterfaceMethods() throws IOException {
        final String endpoint = "Gracie";

        final ConnectionDetails mockConnectionDetails = mock(ConnectionDetails.class);
        when(mockConnectionDetails.getEndpoint()).thenReturn(endpoint);

        final NetworkClient mockNetworkClient = mock(NetworkClientImpl.class);
        when(mockNetworkClient.getConnectionDetails()).thenReturn(mockConnectionDetails);
        Mockito.doThrow(IllegalArgumentException.class).when(mockNetworkClient).close();

        final NetworkClientRetryDecorator networkClientRetryDecorator = new NetworkClientRetryDecorator.Builder()
                .networkClient(mockNetworkClient)
                .networkClientRetryBehavior(new NetworkClientRetryBehavior() {
                    @Override
                    public boolean shouldRetry(final WebResponse webResponse) {
                        return false;
                    }
                })
                .build();

        final ConnectionDetails connectionDetails = networkClientRetryDecorator.getConnectionDetails();
        assertEquals(endpoint, connectionDetails.getEndpoint());

        boolean gotAnIllegalArgumentException = false;

        try {
            networkClientRetryDecorator.close();
        } catch (final IllegalArgumentException e) {
            gotAnIllegalArgumentException = true;
        }

        assertTrue(gotAnIllegalArgumentException);
    }
}
