package com.spectralogic.ds3client.commands.notifications;

import java.util.UUID;

public class GetJobCreatedNotificationRequest extends AbstractGetNotificationRequest {
    public GetJobCreatedNotificationRequest(final UUID notificationId) {
        super(notificationId);
    }

    @Override
    public String getPath() {
        return "/_rest_/job_created_notification_registration/" + this.getNotificationId().toString();
    }
}