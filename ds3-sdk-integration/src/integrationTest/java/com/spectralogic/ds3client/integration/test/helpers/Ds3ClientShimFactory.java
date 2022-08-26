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

import java.lang.reflect.InvocationTargetException;

public final class Ds3ClientShimFactory {
    public enum ClientFailureType {
        ChunkAllocation,
        PutObject,
        GetObject
    }

    private Ds3ClientShimFactory() { }

    public static Ds3Client makeWrappedDs3Client(final ClientFailureType clientFailureType, final Ds3Client ds3Client)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        switch (clientFailureType) {
            case ChunkAllocation:
                return new Ds3ClientShimWithFailedChunkAllocation((Ds3ClientImpl)ds3Client);
            case PutObject:
                return new Ds3ClientShimWithFailedPutObject((Ds3ClientImpl)ds3Client);
            case GetObject:
                return new Ds3ClientShimWithFailedGetObject((Ds3ClientImpl)ds3Client);
            default:
                throw new IllegalArgumentException("I don't know what kind of thingy to make.");
        }
    }
}
