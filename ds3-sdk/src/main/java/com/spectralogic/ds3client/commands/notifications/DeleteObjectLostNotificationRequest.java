package com.spectralogic.ds3client.commands.notifications;

import java.util.UUID;

public class DeleteObjectLostNotificationRequest extends AbstractDeleteNotificationRequest {
    public DeleteObjectLostNotificationRequest(final UUID notificationId) {
        super(notificationId);
    }

    @Override
    public String getPath() {
        return "/_rest_/object_lost_notification_registration/" + this.getNotificationId().toString();
    }
}
