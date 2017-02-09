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

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3client.models.Error;

public class  FailedRequestUsingMgmtPortException extends  FailedRequestException {

    public final static int MGMT_PORT_STATUS_CODE = 404;
    public final static String MGMT_PORT_HEADER = "Spectra-Data-Path-Request-Made-On-Management-Path";
    public final static String MGMT_PORT_RESPONSE = "REST (data) request made on management port";

    public FailedRequestUsingMgmtPortException(final ImmutableList<Integer> expectedStatusCodes, final String requestId) {
        super(expectedStatusCodes, MGMT_PORT_STATUS_CODE, new Error(), MGMT_PORT_RESPONSE, requestId);
    }

}

