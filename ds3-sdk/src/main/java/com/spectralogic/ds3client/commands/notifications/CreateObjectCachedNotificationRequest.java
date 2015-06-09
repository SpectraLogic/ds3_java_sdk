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

package com.spectralogic.ds3client.commands.notifications;

import java.util.UUID;

/**
 * Creates a NotificationRequest to receive notifications for when an object is finished being written to cache
 */
public class CreateObjectCachedNotificationRequest extends AbstractCreateNotificationRequest {

    /**
     * Creates a NotificationRequest to receive all object cached notifications
     */
    public CreateObjectCachedNotificationRequest(final String endpoint) {
        super(endpoint);
    }

    /**
     * Creates a NotificationRequest to receive object cached notifications for the job specified by {@param jobId}
     */
    public CreateObjectCachedNotificationRequest(final String endpoint, final UUID jobId) {
        super(endpoint);
        this.getQueryParams().put("job_id", jobId.toString());
    }

    @Override
    public String getPath() {
        return "/_rest_/object_cached_notification_registration";
    }
}
