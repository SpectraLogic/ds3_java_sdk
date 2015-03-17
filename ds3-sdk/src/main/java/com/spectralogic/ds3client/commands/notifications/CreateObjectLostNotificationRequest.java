package com.spectralogic.ds3client.commands.notifications;

public class CreateObjectLostNotificationRequest extends AbstractCreateNotificationRequest {
    public CreateObjectLostNotificationRequest(final String endpoint) {
        super(endpoint);
    }

    @Override
    public String getPath() {
        return "/_rest_/object_lost_notification_registration";
    }
}
