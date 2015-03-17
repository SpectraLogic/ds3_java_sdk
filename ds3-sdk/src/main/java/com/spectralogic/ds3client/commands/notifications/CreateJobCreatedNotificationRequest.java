package com.spectralogic.ds3client.commands.notifications;

public class CreateJobCreatedNotificationRequest extends AbstractCreateNotificationRequest {
    public CreateJobCreatedNotificationRequest(final String endpoint) {
        super(endpoint);
    }

    @Override
    public String getPath() {
        return "/_rest_/job_created_notification_registration";
    }
}
