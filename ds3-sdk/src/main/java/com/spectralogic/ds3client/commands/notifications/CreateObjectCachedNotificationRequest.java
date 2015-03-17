package com.spectralogic.ds3client.commands.notifications;

import java.util.UUID;

public class CreateObjectCachedNotificationRequest extends AbstractCreateNotificationRequest {

    public CreateObjectCachedNotificationRequest(final String endpoint) {
        super(endpoint);
    }

        public CreateObjectCachedNotificationRequest(final String endpoint, final UUID jobId) {
        super(endpoint);
        this.getQueryParams().put("job_id", jobId.toString());
    }

    @Override
    public String getPath() {
        return "/_rest_/object_cached_notification_registration";
    }
}
