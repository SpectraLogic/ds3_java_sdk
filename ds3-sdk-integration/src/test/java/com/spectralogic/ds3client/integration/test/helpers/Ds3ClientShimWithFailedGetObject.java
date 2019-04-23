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
import com.spectralogic.ds3client.models.JobNode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.fail;

public class Ds3ClientShimWithFailedGetObject extends Ds3ClientShim {
    public Ds3ClientShimWithFailedGetObject(final Ds3ClientImpl ds3ClientImpl)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        super(ds3ClientImpl);
    }

    @Override
    public GetObjectResponse getObject(final GetObjectRequest request) throws IOException {
        throw new IOException("A terrible, horrible thing happened!");
    }

    @Override
    public Ds3Client newForNode(final JobNode node) {
        try {
            final Ds3Client newClient = super.newForNode(node);
            return new Ds3ClientShimWithFailedGetObject((Ds3ClientImpl)newClient);
        } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            fail("Failure trying to create Ds3Client used in verifying putObject retries: " + e.getMessage());
        }

        return null;
    }
}
