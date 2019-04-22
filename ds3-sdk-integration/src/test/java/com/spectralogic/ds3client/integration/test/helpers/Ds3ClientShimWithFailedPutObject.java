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

import com.spectralogic.ds3client.Ds3ClientImpl;
import com.spectralogic.ds3client.commands.PutObjectRequest;
import com.spectralogic.ds3client.commands.PutObjectResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Ds3ClientShimWithFailedPutObject extends Ds3ClientShim {
    public Ds3ClientShimWithFailedPutObject(final Ds3ClientImpl ds3ClientImpl)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        super(ds3ClientImpl);
    }

    @Override
    public PutObjectResponse putObject(final PutObjectRequest request) throws IOException {
        throw new IOException("A terrible, horrible thing happened!");
    }
}
