/*
 * ******************************************************************************
 *   Copyright 2014-2017 Spectra Logic Corporation. All Rights Reserved.
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

import com.spectralogic.ds3client.models.common.Credentials;

import java.net.URI;

public interface ConnectionDetails {
    String getEndpoint();

    Credentials getCredentials();

    /**
     * If true the network layer will use Https.
     */
    boolean isHttps();

    URI getProxy();

    int getRetries();

    int getBufferSize();

    int getConnectionTimeout();

    int getSocketTimeout();

    /**
     * Returns true if the network layer should perform certificate authentication for SSL.  False will disable
     * certificate authentication.
     */
    boolean isCertificateVerification();

    String getUserAgent();
}
