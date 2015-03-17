package com.spectralogic.ds3client.commands.notifications;

import java.util.UUID;

public class CreateJobCompletedNotificationRequest extends AbstractCreateNotificationRequest {
    public CreateJobCompletedNotificationRequest(final String endpoint) {
        super(endpoint);
    }

    public CreateJobCompletedNotificationRequest(final String endpoint, final UUID jobId) {
        super(endpoint);
        this.getQueryParams().put("job_id", jobId.toString());
    }

    @Override
    public String getPath() {
        return "/_rest_/job_completed_notification_registration";
    }
}
