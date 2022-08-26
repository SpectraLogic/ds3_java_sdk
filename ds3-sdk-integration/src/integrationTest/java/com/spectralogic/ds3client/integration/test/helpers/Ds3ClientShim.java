/*
 * ******************************************************************************
 *   Copyright 2014-2019 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.integration.test.helpers;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.Ds3ClientImpl;
import com.spectralogic.ds3client.commands.GetObjectRequest;
import com.spectralogic.ds3client.commands.GetObjectResponse;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.commands.PutObjectResponse;
import com.spectralogic.ds3client.exceptions.Ds3NoMoreRetriesException;
import com.spectralogic.ds3client.models.JobNode;
import com.spectralogic.ds3client.networking.ConnectionDetails;
import com.spectralogic.ds3client.networking.NetworkClient;
import com.spectralogic.ds3client.networking.NetworkClientImpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.fail;

public class Ds3ClientShim extends Ds3ClientImpl {
    private static Method getNetClientMethod = null;

    private final int maxNumRetries;

    private int numRetries = 0;

    static {
        try {
            getNetClientMethod = Ds3ClientImpl.class.getDeclaredMethod("getNetClient");
        } catch (final NoSuchMethodException e) {
            fail("Could not find Ds3ClientImpl method getNetClient.");
        }

        getNetClientMethod.setAccessible(true);
    }

    public Ds3ClientShim(final NetworkClient netClient, final int maxNumRetries) {
        super(netClient);

        this.maxNumRetries = maxNumRetries;
    }

    public Ds3ClientShim(final NetworkClient netClient) {
        this(netClient, 1);
    }

    public Ds3ClientShim(final Ds3ClientImpl ds3ClientImpl) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this((NetworkClient)getNetClientMethod.invoke(ds3ClientImpl), 1);
    }

    public Ds3ClientShim(final Ds3ClientImpl ds3ClientImpl, final int maxNumRetries) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this((NetworkClient)getNetClientMethod.invoke(ds3ClientImpl), maxNumRetries);
    }

    @Override
    public PutObjectResponse putObject(final PutObjectRequest request) throws IOException {
        if (numRetries++ >= maxNumRetries) {
            return super.putObject(request);
        }

        throw new Ds3NoMoreRetriesException(1);
    }

    @Override
    public GetObjectResponse getObject(final GetObjectRequest request) throws IOException {
        if (numRetries++ >= maxNumRetries) {
            return super.getObject(request);
        }

        throw new Ds3NoMoreRetriesException(1);
    }

    @Override
    public Ds3Client newForNode(final JobNode node) {
        final ConnectionDetails newConnectionDetails;
        try {
            newConnectionDetails = ((NetworkClient)getNetClientMethod.invoke(this)).getConnectionDetails();
            final NetworkClient newNetClient = new NetworkClientImpl(newConnectionDetails);
            return new Ds3ClientShim(newNetClient);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            fail("Failure trying to create Ds3Client used in verifying putObject retries: " + e.getMessage());
        }

        return null;
    }
}
