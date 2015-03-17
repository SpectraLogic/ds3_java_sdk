package com.spectralogic.ds3client.commands.notifications;

public class CreateTapeFailureNotificationRequest extends AbstractCreateNotificationRequest {
    public CreateTapeFailureNotificationRequest(final String endpoint) {
        super(endpoint);
    }

    @Override
    public String getPath() {
        return "/_rest_/tape_failure_notification_registration";
    }
}
