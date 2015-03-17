package com.spectralogic.ds3client.commands.notifications;

import java.util.UUID;

public class DeleteJobCompletedNotificationRequest extends AbstractDeleteNotificationRequest {

    public DeleteJobCompletedNotificationRequest(final UUID notificationId) {
        super(notificationId);
    }

    @Override
    public String getPath() {
        return "/_rest_/job_completed_notification_registration/" + this.getNotificationId().toString();
    }
}
