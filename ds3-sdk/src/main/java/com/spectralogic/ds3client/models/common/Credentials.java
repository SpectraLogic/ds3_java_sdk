/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3client.models.common;


import com.spectralogic.ds3client.utils.Guard;

public class Credentials {

    private final String clientId;
    private final String key;

    public Credentials(final String clientId, final String key) {
        this.clientId = clientId;
        this.key = key;
    }

    public String getClientId() {
        return clientId;
    }

    public String getKey() {
        return key;
    }

    public boolean isValid() {
        return !(Guard.isStringNullOrEmpty(clientId) || Guard.isStringNullOrEmpty(key));
    }
}
