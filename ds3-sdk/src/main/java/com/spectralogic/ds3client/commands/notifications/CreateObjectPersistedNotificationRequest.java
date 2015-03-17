package com.spectralogic.ds3client.commands.notifications;

import java.util.UUID;

public class CreateObjectPersistedNotificationRequest extends AbstractCreateNotification{

    public CreateObjectPersistedNotificationRequest(final String endpoint, final UUID jobId) {
        super(endpoint);
        this.getQueryParams().put("job_id", jobId.toString());
    }

    @Override
    public String getPath() {
        return "/_rest_/object_persisted_notification_registration";
    }

}
