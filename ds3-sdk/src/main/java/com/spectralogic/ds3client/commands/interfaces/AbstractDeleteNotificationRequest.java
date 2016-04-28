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

package com.spectralogic.ds3client.commands.interfaces;

import com.spectralogic.ds3client.networking.HttpVerb;

import java.util.UUID;

public abstract class AbstractDeleteNotificationRequest extends AbstractRequest {

    private final String notificationId;

    public AbstractDeleteNotificationRequest(final UUID notificationId) {
        super();
        this.notificationId = notificationId.toString();
    }

    public AbstractDeleteNotificationRequest(final String notificationId) {
        super();
        this.notificationId = notificationId;
    }

    public String getNotificationId() {
        return this.notificationId;
    }

    @Override
    public HttpVerb getVerb() {
        return HttpVerb.DELETE;
    }
}
