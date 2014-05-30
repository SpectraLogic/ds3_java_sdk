/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.helpers;

import com.spectralogic.ds3client.Ds3Client;

public class Ds3ClientFactoryImpl implements Ds3ClientFactory {
    private final Ds3Client client;

    public Ds3ClientFactoryImpl(final Ds3Client client) {
        this.client = client;
    }

    //TODO: need to actually return a client that points to a particular server id.
    @Override
    public Ds3Client GetClientForServerId(final String serverId) {
        return this.client;
    }
}
