/*
 * ****************************************************************************
 *    Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
 *    Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *    this file except in compliance with the License. A copy of the License is located at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    or in the "license" file accompanying this file.
 *    This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *    CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *    specific language governing permissions and limitations under the License.
 *  ****************************************************************************
 */

package com.spectralogic.ds3client.diagnostics;

import com.spectralogic.ds3client.Ds3Client;
import com.spectralogic.ds3client.models.Tape;

import java.io.IOException;
import java.util.List;

/**
 * A wrapper around the {@link com.spectralogic.ds3client.Ds3Client} which performs diagnostics.
 */
public abstract class Ds3Diagnostics {

    /**
     * Wraps the given {@link com.spectralogic.ds3client.Ds3ClientImpl} with diagnostics methods.
     * @param client An instance of {@link com.spectralogic.ds3client.Ds3Client}, usually gotten from a call to
     *               {@link com.spectralogic.ds3client.Ds3ClientBuilder}
     * @return An instance of {@link com.spectralogic.ds3client.Ds3Client} wrapped with diagnostics methods.
     */
    public static Ds3Diagnostics wrap(final Ds3Client client) {
        return new Ds3DiagnosticsImpl(client);
    }

    //TODO test and comment
    public abstract void checkCacheAvailability() throws IOException;

    //TODO test and comment
    public abstract List<Tape> getOfflineTapes() throws IOException;
}
