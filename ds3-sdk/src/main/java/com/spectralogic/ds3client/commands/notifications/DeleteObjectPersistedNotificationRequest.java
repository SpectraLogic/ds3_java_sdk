package com.spectralogic.ds3client.commands.notifications;

import java.util.UUID;

public class DeleteObjectPersistedNotificationRequest extends AbstractDeleteNotification {

    private final UUID notificationId;

    public DeleteObjectPersistedNotificationRequest(final UUID notificationId) {
        super();
        this.notificationId = notificationId;
    }

    @Override
    public String getPath() {
        return "/_rest_/job_completed_notification_registration/" + notificationId.toString();
    }
}
